#!/usr/bin/env bash
# Boots a headless API 35 AOSP emulator (from the Nix dev shell), installs the debug APK,
# launches it, fails if it crashes, and saves a screenshot.
# Usage (from repo root): nix develop --command scripts/emulator-smoke.sh
set -euo pipefail

OUT_DIR="${OUT_DIR:-build/emulator-smoke}"
AVD_NAME=ghost-smoke
PKG=org.ghost.android
BOOT_TIMEOUT="${BOOT_TIMEOUT:-1500}"
UI_TIMEOUT="${UI_TIMEOUT:-900}"
export ANDROID_AVD_HOME="${ANDROID_AVD_HOME:-$PWD/build/avd}"
export ANDROID_USER_HOME="${ANDROID_USER_HOME:-$PWD/build/android-user}"
mkdir -p "$OUT_DIR" "$ANDROID_AVD_HOME" "$ANDROID_USER_HOME"

SDK="$ANDROID_HOME"
IMG="$(ls -d "$SDK"/system-images/android-*/default/x86_64 | head -n1)"
API="$(basename "$(dirname "$(dirname "$IMG")")")"

./gradlew assembleDebug -q

if [ ! -d "$ANDROID_AVD_HOME/$AVD_NAME.avd" ]; then
  avdmanager create avd -n "$AVD_NAME" -k "system-images;$API;default;x86_64" -d pixel_6 --force </dev/null
fi

# Hardware acceleration is used when /dev/kvm is writable; otherwise fall back to (slow) software emulation.
ACCEL=()
[ -w /dev/kvm ] || ACCEL=(-no-accel)

if adb get-state >/dev/null 2>&1; then
  echo "Reusing running emulator"
else
  emulator -avd "$AVD_NAME" -no-window -no-audio -no-boot-anim -no-snapshot \
    -gpu swiftshader_indirect "${ACCEL[@]}" >"$OUT_DIR/emulator.log" 2>&1 &
  EMU_PID=$!
  trap 'adb emu kill >/dev/null 2>&1 || true; kill $EMU_PID 2>/dev/null || true' EXIT
fi

adb wait-for-device
SECONDS=0
until [ "$(adb shell getprop sys.boot_completed 2>/dev/null | tr -d '\r')" = 1 ]; do
  [ $SECONDS -lt "$BOOT_TIMEOUT" ] || { echo "Emulator boot timed out"; exit 1; }
  sleep 10
done
echo "Booted ($API) in ${SECONDS}s"
# sys.boot_completed can precede the package manager / settings services being usable
until adb shell pm path android >/dev/null 2>&1 && adb shell settings list global >/dev/null 2>&1; do sleep 5; done

adb shell settings put global window_animation_scale 0 || true
adb shell settings put global transition_animation_scale 0 || true
adb shell settings put global animator_duration_scale 0 || true

adb uninstall "$PKG" >/dev/null 2>&1 || true # debug signing keys differ between machines
adb logcat -c
for attempt in 1 2 3 4 5; do # the package service can be flaky right after boot
  adb install -r -t app/build/outputs/apk/debug/app-debug.apk && break
  [ $attempt -lt 5 ] || exit 1
  sleep 20
done
adb shell monkey -p "$PKG" -c android.intent.category.LAUNCHER 1

app_crashed() { adb logcat -d | grep -A3 "FATAL EXCEPTION" | grep -q "Process: $PKG"; }
app_running() { for _ in 1 2 3; do adb shell pidof "$PKG" >/dev/null 2>&1 && return 0; sleep 5; done; return 1; }
# Software emulation is slow enough that system apps hit ANR dialogs; tap "Wait" so they don't cover the UI.
dismiss_anr() {
  for _ in 1 2 3 4 5 6; do
    adb shell uiautomator dump /sdcard/ui.xml >/dev/null 2>&1 || true
    bounds="$(adb shell cat /sdcard/ui.xml 2>/dev/null | grep -o 'text="Wait"[^>]*bounds="\[[0-9]*,[0-9]*\]\[[0-9]*,[0-9]*\]"' | grep -o '\[[0-9]*,[0-9]*\]\[[0-9]*,[0-9]*\]' | head -n1 || true)"
    [ -n "$bounds" ] || return 0
    read -r x1 y1 x2 y2 <<<"$(echo "$bounds" | tr '][,' '   ')"
    adb shell input tap $(( (x1 + x2) / 2 )) $(( (y1 + y2) / 2 ))
    sleep 10
  done
}
capture() {
  adb logcat -d >"$OUT_DIR/logcat.txt"
  adb exec-out screencap -p >"$OUT_DIR/screenshot.png"
}

# Wait until the app's real UI is rendered (logcat "Displayed"), not just the system splash screen.
# A fresh install redirects PostListActivity -> LoginActivity.
SECONDS=0
until adb logcat -d -s ActivityTaskManager:I | grep -q -E "Displayed $PKG/.*(LoginActivity|PostListActivity)"; do
  if app_crashed; then break; fi
  [ $SECONDS -lt "$UI_TIMEOUT" ] || { echo "App UI did not appear in ${UI_TIMEOUT}s"; capture; exit 1; }
  sleep 5
done
sleep 10 # let the first frame render
dismiss_anr
sleep 5
capture

if app_crashed || ! app_running; then
  echo "App crashed:"; grep -A25 "FATAL EXCEPTION" "$OUT_DIR/logcat.txt" | grep -A25 "Process: $PKG" || true
  exit 1
fi
echo "App is running; screenshot at $OUT_DIR/screenshot.png"

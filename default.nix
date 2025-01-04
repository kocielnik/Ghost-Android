# Dockerfile: https://hub.docker.com/layers/circleci/android/api-26-node/images/sha256-f89a5589f22bc02fcf8ec0875a6081d1f772f8a484a6afa7eace85b1f69c8837?context=explore

{ pkgs ? import <nixpkgs> {
    config.android_sdk.accept_license = true;
    config.allowUnfree = true;
  }
}:

let
  androidSdk = pkgs.androidenv.androidPkgs_9_0.androidsdk;
in
pkgs.mkShell {
  buildInputs = with pkgs; [
    androidSdk
    glibc
    gradle
    openjdk8
    kotlin
    neovim
  ];
  LOCALE_ARCHIVE = "/usr/lib/locale/locale-archive";
  # override the aapt2 that gradle uses with the nix-shipped version
  GRADLE_OPTS = "-Dorg.gradle.project.android.aapt2FromMavenOverride=${androidSdk}/libexec/android-sdk/build-tools/28.0.3/aapt2";
  TARGET_SDK_VERSION = 26;
}

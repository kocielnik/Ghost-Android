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
    aapt
    androidSdk
    glibc
    gradle_8
    jdk17
    kotlin
    neovim
  ];
  LOCALE_ARCHIVE = "/usr/lib/locale/locale-archive";
  TARGET_SDK_VERSION = 34;
  ANDROID_HOME="android-sdk";
}

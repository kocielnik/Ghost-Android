# Dockerfile: https://hub.docker.com/layers/circleci/android/api-26-node/images/sha256-f89a5589f22bc02fcf8ec0875a6081d1f772f8a484a6afa7eace85b1f69c8837?context=explore
{
  description = "Ghost-Android development environment";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/nixpkgs-unstable";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs {
          inherit system;
          config.android_sdk.accept_license = true;
          config.allowUnfree = true;
        };
        androidSdk = (pkgs.androidenv.composeAndroidPackages {
          platformVersions = [ "35" "36" ];
          buildToolsVersions = [ "35.0.0" "36.0.0" ];
          includeEmulator = true;
          includeSystemImages = true;
          systemImageTypes = [ "default" ];
          abiVersions = [ "x86_64" ];
        }).androidsdk;
      in
      {
        devShells.default = pkgs.mkShell {
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
          ANDROID_HOME = "${androidSdk}/libexec/android-sdk";
        };
      });
}

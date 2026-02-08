#!/bin/bash
set -e

echo "== Android SDK setup (CLI only) =="

SDK_DIR="$HOME/android-sdk"
CMDLINE_ZIP="commandlinetools-linux-11076708_latest.zip"
CMDLINE_URL="https://dl.google.com/android/repository/$CMDLINE_ZIP"

mkdir -p "$SDK_DIR/cmdline-tools"
cd "$SDK_DIR"

if [ ! -d "$SDK_DIR/cmdline-tools/latest" ]; then
  echo ">> Download cmdline-tools"
  wget -q "$CMDLINE_URL"
  unzip -q "$CMDLINE_ZIP" -d cmdline-tools
  mv cmdline-tools/cmdline-tools cmdline-tools/latest
  rm "$CMDLINE_ZIP"
else
  echo ">> cmdline-tools already exists"
fi

echo ">> Export ANDROID_SDK_ROOT"
export ANDROID_SDK_ROOT="$SDK_DIR"
export PATH="$PATH:$ANDROID_SDK_ROOT/cmdline-tools/latest/bin"
export PATH="$PATH:$ANDROID_SDK_ROOT/platform-tools"

if ! grep -q ANDROID_SDK_ROOT ~/.bashrc; then
  echo "" >> ~/.bashrc
  echo "# Android SDK" >> ~/.bashrc
  echo "export ANDROID_SDK_ROOT=$SDK_DIR" >> ~/.bashrc
  echo "export PATH=\$PATH:\$ANDROID_SDK_ROOT/cmdline-tools/latest/bin" >> ~/.bashrc
  echo "export PATH=\$PATH:\$ANDROID_SDK_ROOT/platform-tools" >> ~/.bashrc
fi

echo ">> Accept licenses"
yes | sdkmanager --licenses

echo ">> Install SDK packages"
sdkmanager \
  "platform-tools" \
  "platforms;android-35" \
  "build-tools;35.0.0"

echo "== DONE =="
echo "You can now run: ./gradlew assembleDebug"

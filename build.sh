#!/usr/bin/env bash
set -e

echo "======================================"
echo " Java 21 + Android SDK full setup"
echo "======================================"

### -------- CONFIG --------
JAVA_21_PATH="/usr/lib/jvm/java-21-openjdk-amd64"
SDK_DIR="$HOME/android-sdk"
CMDLINE_ZIP="commandlinetools-linux-11076708_latest.zip"
CMDLINE_URL="https://dl.google.com/android/repository/$CMDLINE_ZIP"
ANDROID_PLATFORM="android-35"
BUILD_TOOLS="35.0.0"
### ------------------------

echo ">> [1/6] Install Java 21"
sudo apt update
sudo apt install -y openjdk-21-jdk

echo ">> Set JAVA_HOME"
export JAVA_HOME="$JAVA_21_PATH"
export PATH="$JAVA_HOME/bin:$PATH"

if ! grep -q "JAVA_HOME=$JAVA_21_PATH" ~/.bashrc; then
  echo "" >> ~/.bashrc
  echo "# Java 21" >> ~/.bashrc
  echo "export JAVA_HOME=$JAVA_21_PATH" >> ~/.bashrc
  echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
fi

sudo update-alternatives --set java "$JAVA_21_PATH/bin/java" || true
sudo update-alternatives --set javac "$JAVA_21_PATH/bin/javac" || true

echo ">> Java version:"
java --version

echo "--------------------------------------"
echo ">> [2/6] Setup Android SDK (CLI)"
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

echo "--------------------------------------"
echo ">> [3/6] Export Android env vars"
export ANDROID_SDK_ROOT="$SDK_DIR"
export ANDROID_HOME="$SDK_DIR"
export PATH="$PATH:$ANDROID_HOME/cmdline-tools/latest/bin"
export PATH="$PATH:$ANDROID_HOME/platform-tools"

if ! grep -q ANDROID_HOME ~/.bashrc; then
  echo "" >> ~/.bashrc
  echo "# Android SDK" >> ~/.bashrc
  echo "export ANDROID_SDK_ROOT=$SDK_DIR" >> ~/.bashrc
  echo "export ANDROID_HOME=$SDK_DIR" >> ~/.bashrc
  echo "export PATH=\$PATH:\$ANDROID_HOME/cmdline-tools/latest/bin" >> ~/.bashrc
  echo "export PATH=\$PATH:\$ANDROID_HOME/platform-tools" >> ~/.bashrc
fi

echo "--------------------------------------"
echo ">> [4/6] Accept Android licenses"
yes | sdkmanager --licenses

echo "--------------------------------------"
echo ">> [5/6] Install Android packages"
sdkmanager \
  "platform-tools" \
  "platforms;$ANDROID_PLATFORM" \
  "build-tools;$BUILD_TOOLS"

echo "--------------------------------------"
echo ">> [6/6] Create local.properties for Gradle"

# Find project root (where gradlew exists)
PROJECT_ROOT=$(pwd)
if [ ! -f "$PROJECT_ROOT/gradlew" ]; then
  PROJECT_ROOT=$(find "$HOME" -maxdepth 4 -name gradlew -type f | head -n 1 | xargs dirname)
fi

if [ -n "$PROJECT_ROOT" ]; then
  echo ">> Project root: $PROJECT_ROOT"
  echo "sdk.dir=$ANDROID_HOME" > "$PROJECT_ROOT/local.properties"
else
  echo "⚠️  gradlew not found, skip local.properties"
fi

echo "======================================"
echo "✅ SETUP DONE"
echo ""
echo "Java:"
java --version
echo ""
echo "Android SDK:"
sdkmanager --list | head -n 10
echo ""
echo "👉 Open new terminal or run: source ~/.bashrc"
echo "👉 Then run: ./gradlew assembleDebug"
echo "======================================"

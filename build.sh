#!/usr/bin/env bash
set -e

echo "======================================"
echo " Java 21 + Android SDK FULL SETUP"
echo " (Codespaces safe)"
echo "======================================"

### -------- CONFIG --------
JAVA_21_PATH="/usr/lib/jvm/java-21-openjdk-amd64"
SDK_DIR="$HOME/android-sdk"
CMDLINE_ZIP="commandlinetools-linux-11076708_latest.zip"
CMDLINE_URL="https://dl.google.com/android/repository/$CMDLINE_ZIP"
ANDROID_PLATFORM="android-35"
BUILD_TOOLS="35.0.0"
### ------------------------

echo ">> Remove Yarn repo (apt issue fix)"
sudo rm -f /etc/apt/sources.list.d/yarn.list || true

echo ">> Update system"
sudo apt update

echo "--------------------------------------"
echo ">> [1/8] Install Java 21"
sudo apt install -y openjdk-21-jdk

echo ">> Remove Microsoft OpenJDK 25 (VERY IMPORTANT)"
sudo apt remove -y msopenjdk-25 || true
sudo apt autoremove -y || true

echo "--------------------------------------"
echo ">> [2/8] Set JAVA_HOME"
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

echo ">> Java runtime:"
java --version

echo "--------------------------------------"
echo ">> [3/8] Setup Android SDK (CLI)"
mkdir -p "$SDK_DIR/cmdline-tools"
cd "$SDK_DIR"

if [ ! -d "$SDK_DIR/cmdline-tools/latest" ]; then
  wget -q "$CMDLINE_URL"
  unzip -q "$CMDLINE_ZIP" -d cmdline-tools
  mv cmdline-tools/cmdline-tools cmdline-tools/latest
  rm "$CMDLINE_ZIP"
else
  echo ">> cmdline-tools already exists"
fi

echo "--------------------------------------"
echo ">> [4/8] Export Android env vars"
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
echo ">> [5/8] Accept Android licenses"
yes | sdkmanager --licenses

echo "--------------------------------------"
echo ">> [6/8] Install Android packages"
sdkmanager \
  "platform-tools" \
  "platforms;$ANDROID_PLATFORM" \
  "build-tools;$BUILD_TOOLS"

echo "--------------------------------------"
echo ">> [7/8] Configure Gradle (FORCE Java 21)"

PROJECT_ROOT="/workspaces/$(basename "$(pwd -P | sed 's#.*/workspaces/##')")"
if [ ! -f "$PROJECT_ROOT/gradlew" ]; then
  PROJECT_ROOT=$(find /workspaces -name gradlew -type f | head -n 1 | xargs dirname)
fi

echo ">> Project root: $PROJECT_ROOT"

echo "sdk.dir=$ANDROID_HOME" > "$PROJECT_ROOT/local.properties"
echo "org.gradle.java.home=$JAVA_21_PATH" > "$PROJECT_ROOT/gradle.properties"

echo "--------------------------------------"
echo ">> [8/8] Stop Gradle daemon (important)"
cd "$PROJECT_ROOT"
./gradlew --stop || true

echo "======================================"
echo "✅ SETUP DONE"
echo ""
echo "Java (system):"
java --version
echo ""
echo "Gradle JVM:"
./gradlew -version | grep JVM
echo ""
echo "👉 Open new terminal or run: source ~/.bashrc"
echo "👉 Then run: ./gradlew :app:assembleDebug"
echo "======================================"

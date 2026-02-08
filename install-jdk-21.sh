#!/usr/bin/env bash
set -e

echo "👉 Updating system..."
sudo apt update

echo "👉 Installing OpenJDK 21..."
sudo apt install -y openjdk-21-jdk

JAVA_21_PATH="/usr/lib/jvm/java-21-openjdk-amd64"

echo "👉 Setting JAVA_HOME to Java 21..."
if ! grep -q "JAVA_HOME=.*java-21-openjdk-amd64" ~/.bashrc; then
  {
    echo ""
    echo "# Java 21"
    echo "export JAVA_HOME=$JAVA_21_PATH"
    echo "export PATH=\$JAVA_HOME/bin:\$PATH"
  } >> ~/.bashrc
fi

export JAVA_HOME=$JAVA_21_PATH
export PATH=$JAVA_HOME/bin:$PATH

echo "👉 Configuring update-alternatives..."
sudo update-alternatives --set java $JAVA_21_PATH/bin/java || true
sudo update-alternatives --set javac $JAVA_21_PATH/bin/javac || true

echo "✅ Done!"
echo "👉 Java version:"
java --version

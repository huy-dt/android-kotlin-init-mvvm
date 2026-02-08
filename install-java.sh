#!/bin/bash
set -e

# Force JDK 21 (ignore system Java 25)
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

echo "Using Java:"
java -version

# Build
./gradlew assembleDebug


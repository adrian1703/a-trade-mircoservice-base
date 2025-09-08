#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

export JAVA_HOME=/home/adrian/.jdks/openjdk-24.0.1
PREVIOUS_DIR="$(pwd)"

cd "$SCRIPT_DIR"
./gradlew ":a-trade-microservice-runtime-api:clean" &&
  ./gradlew ":a-trade-microservice-runtime-api:jar" &&
  ./gradlew ":a-trade-microservice-runtime-api:publishToMavenLocal"

cd a-trade-microservice-template
./gradlew ":clean" && ./gradlew ":jar"
cd ..
rm -r src/test/resources/*
cp -r a-trade-microservice-template/build/libs/* src/test/resources


cd "$PREVIOUS_DIR"
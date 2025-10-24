#!/usr/bin/env bash
set -euo pipefail
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
CURRENT_DIR="$(pwd)"

RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[0;33m'
CYAN='\033[0;36m'
RESET='\033[0m'

echo -e "${CYAN}==> Changing to build directory...${RESET}"
source ./gradle.properties
cd "$SCRIPT_DIR"

echo -e "${CYAN}==> Building dependencies...${RESET}"
./build_interface_and_template.bash > /dev/null

echo -e "${CYAN}==> Building project...${RESET}"
./gradlew clean build &> /dev/null

echo -e "${CYAN}==> Building Docker image...${RESET}"
docker build \
  --build-arg JAR_FILE="a-trade-microservice-base-0.0.1.jar" \
  -f ./dockerfiles/production.Dockerfile \
  -t "$PROJECT_NAME" .

echo -e "${GREEN}✔ Done! Returning to calling directory${RESET}"
cd "$CURRENT_DIR"

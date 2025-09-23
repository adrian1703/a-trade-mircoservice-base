#!/usr/bin/env bash
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

source "$SCRIPT_DIR/gradle.properties"

docker build -f "$SCRIPT_DIR/dockerfiles/integration.Dockerfile" -t "$PROJECT_NAME-integration-test" "$SCRIPT_DIR"
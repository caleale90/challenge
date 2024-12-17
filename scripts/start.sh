#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

set -e

cd ..

./mvnw clean package -DskipTests

docker compose up --build -d

#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

set -e

cd ..

./mvnw clean package -DskipTests

docker compose up --build -d

sleep 3

docker cp src/main/resources/dump.sql postgres:/tmp/script.sql

docker exec postgres psql -U postgres -d challenge -f /tmp/script.sql


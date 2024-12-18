#!/bin/bash

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

set -e

if [[ "$1" == "start" ]]; then
    echo "Starting the Recommendation system..."
    bash start.sh
    echo "Recommendation system started..."
elif [[ "$1" == "stop" ]]; then
    echo "Stopping the Recommendation system..."
    bash stop.sh
    echo "Recommendation system stopped"
elif [[ "$1" == "initialize" ]]; then
    echo "Initializing the Recommendation system..."
    bash provision.sh
    echo "Recommendation system initialized and started..."
else
    echo "Usage: $0 {start|stop|initialize}"
    exit 1
fi

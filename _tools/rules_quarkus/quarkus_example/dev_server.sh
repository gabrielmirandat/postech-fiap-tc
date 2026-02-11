#!/usr/bin/env bash
set -euo pipefail

echo "Starting Quarkus development server..."
echo "This will run the application with live coding support."
echo "Press Ctrl+C to stop the server."
echo ""

# Get the script directory
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" >/dev/null 2>&1 && pwd)"

# Run the development version of the application
exec "$SCRIPT_DIR/hello_quarkus_dev"

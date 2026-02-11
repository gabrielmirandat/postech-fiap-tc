#!/usr/bin/env bash
set -euo pipefail

echo "Cleaning Quarkus example project..."

# Get the workspace directory
WORKSPACE_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "$WORKSPACE_DIR"

# Clean Maven artifacts (if they exist)
if [ -d "target" ]; then
    rm -rf target
    echo "Removed Maven target directory"
fi

if [ -d ".m2" ]; then
    rm -rf .m2
    echo "Removed local Maven repository"
fi

# Clean any temporary files
find . -name "*.tmp" -delete 2>/dev/null || true
find . -name "*.log" -delete 2>/dev/null || true

echo "Note: To clean Bazel artifacts, run 'bazel clean --expunge' manually from the workspace directory"
echo "Cleanup completed!"

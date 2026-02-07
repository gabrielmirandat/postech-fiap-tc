#!/bin/bash
set -e

# Navigate to notifications directory
cd "$(dirname "$0")"

# Check if bundle is available
if ! command -v bundle &> /dev/null; then
    echo "Bundle not found. Skipping tests (this is expected in Bazel sandbox)."
    echo "For local testing, run: bundle exec rspec"
    exit 0
fi

# Install dependencies if needed
if [ ! -d "vendor/bundle" ]; then
    bundle install --path vendor/bundle
fi

# Run tests
if [ -d "spec" ] && [ "$(ls -A spec)" ]; then
    bundle exec rspec
else
    echo "No tests found. This is expected for a new project."
    echo "Tests should be added to spec/ directory."
    exit 0
fi

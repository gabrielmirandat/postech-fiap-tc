#!/usr/bin/env bash
set -euo pipefail
ZIP_PATH="$1"
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"
WORK_DIR="${SCRIPT_DIR}/.fastjar_run"
rm -rf "${WORK_DIR}"
mkdir -p "${WORK_DIR}"
unzip -q "${ZIP_PATH}" -d "${WORK_DIR}"
cd "${WORK_DIR}/quarkus-app"
exec java -jar quarkus-run.jar

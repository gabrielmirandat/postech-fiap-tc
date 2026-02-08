# Module extension to load paket2bazel generated dependencies
# This file loads the generated paket.main_extension.bzl file
# Run ./update-deps.sh to regenerate paket.main.bzl and paket.main_extension.bzl

load("//payments:paket.main_extension.bzl", "main_extension")

payments_deps_extension = main_extension

# rules_quarkus

A comprehensive Bazel rule set for building Quarkus applications with full dependency management, testing support, and multiple deployment options.

## Overview

This repository provides everything you need to build Quarkus applications with Bazel:

- **`quarkus/`** - The core Bazel rules for Quarkus
- **`quarkus_example/`** - Example using MODULE.bazel (Bzlmod)
- **`quarkus_example_workspace/`** - Example using WORKSPACE (traditional)

## Quick Start

### For Other Projects

To use `rules_quarkus` in your project, add this to your `WORKSPACE`:

```bazel
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

git_repository(
    name = "rules_quarkus",
    remote = "git@github.com:your-username/rules_quarkus.git",
    commit = "main",
)
```

Then load the rules:

```bazel
load("@rules_quarkus//quarkus:quarkus.bzl", "quarkus_app")
```

### Templates

Copy these template files to get started quickly:
- `quarkus/TEMPLATE_WORKSPACE` → `WORKSPACE`
- `quarkus/TEMPLATE_BUILD.bazel` → `BUILD.bazel`

## Repository Structure

```
rules_quarkus/
├── quarkus/                    # Core Bazel rules
│   ├── quarkus.bzl            # Main rule definitions
│   ├── README.md              # Detailed documentation
│   ├── USAGE_EXAMPLES.md      # Comprehensive examples
│   ├── QUICK_START.md         # 5-minute setup guide
│   ├── TEMPLATE_WORKSPACE     # WORKSPACE template
│   ├── TEMPLATE_BUILD.bazel   # BUILD.bazel template
│   ├── BUILD                  # Build configuration
│   ├── LICENSE.txt            # Apache 2.0 license
│   └── REPO.bazel            # Bzlmod repository definition
├── quarkus_example/           # MODULE.bazel example (Bzlmod)
│   ├── MODULE.bazel          # Module configuration
│   ├── BUILD.bazel           # Build rules
│   ├── src/                  # Source code
│   └── README.md             # Example documentation
├── quarkus_example_workspace/ # WORKSPACE example (traditional)
│   ├── WORKSPACE             # Workspace configuration
│   ├── BUILD.bazel           # Build rules
│   ├── src/                  # Source code
│   └── README.md             # Example documentation
└── README.md                 # This file
```

## Features

- **Quarkus Application Building** - Create fat JARs and executable applications
- **Dependency Management** - Full Maven dependency resolution through rules_jvm_external
- **Testing Support** - JUnit 5 integration with comprehensive test suites
- **Multiple Build Modes** - Production, development, and testing configurations
- **Cross-Repository Usage** - Easy integration into other Bazel projects
- **Dual Build System Support** - Both WORKSPACE and MODULE.bazel (Bzlmod)

## Installation Options

### Option 1: Git Repository (Recommended)

```bazel
git_repository(
    name = "rules_quarkus",
    remote = "git@github.com:your-username/rules_quarkus.git",
    commit = "main",  # or a specific tag like "v1.0.0"
)
```

### Option 2: Local Repository (Development)

```bazel
local_repository(
    name = "rules_quarkus",
    path = "/path/to/rules_quarkus",
)
```

### Option 3: HTTP Archive (Most robust)

```bazel
http_archive(
    name = "rules_quarkus",
    sha256 = "hash_of_the_zip_file",
    strip_prefix = "rules_quarkus-1.0.0",
    url = "https://github.com/your-username/rules_quarkus/archive/v1.0.0.zip",
)
```

### Option 4: Bzlmod (if using MODULE.bazel)

```bazel
bazel_dep(name = "rules_quarkus", version = "1.0.0")
```

## Basic Usage

### 1. Create a Java Library

```starlark
java_library(
    name = "artifact",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**"]),
    deps = [
        "@maven//:io_quarkus_quarkus_core",
        "@maven//:io_quarkus_quarkus_resteasy",
    ],
)
```

### 2. Create Quarkus Application

```starlark
quarkus_app(
    name = "my_quarkus_app",
    main_class = "com.example.Main",
    deps = [":artifact"],
)
```

### 3. Build and Run

```bash
# Build the application
bazel build //:my_quarkus_app

# Run the application
bazel run //:my_quarkus_app
```

## Examples

### Complete Project Structure

See the examples for complete working projects:

- **`quarkus_example/`** - Uses MODULE.bazel (Bzlmod)
- **`quarkus_example_workspace/`** - Uses WORKSPACE (traditional)

Both examples include:
- Full Quarkus application
- Comprehensive testing setup
- Multiple build targets
- Development and production modes

## Documentation

- **[Main Documentation](quarkus/README.md)** - Complete rule reference
- **[Usage Examples](quarkus/USAGE_EXAMPLES.md)** - Comprehensive examples
- **[Quick Start Guide](quarkus/QUICK_START.md)** - 5-minute setup
- **[Templates](quarkus/)** - Ready-to-use configuration files

## Dependencies

The `rules_quarkus` package works with `rules_jvm_external` for Maven dependency management. See the templates for complete dependency configurations.

## Testing

### Test Structure

```
src/
├── main/
│   ├── java/           # Application source code
│   └── resources/      # Configuration files
└── test/
    ├── unit/           # Unit tests
    ├── integration/    # Integration tests
    ├── contract/       # Contract tests
    ├── behavior/       # Behavior tests
    ├── utils/          # Test utilities
    └── resources/      # Test configuration
```

### Running Tests

```bash
# Run unit tests
bazel test //:unit

# Run integration tests
bazel test //:integration

# Run all tests
bazel test //:unit //:integration
```

## Build Commands

```bash
# Build all targets
bazel build //:all

# Build specific target
bazel build //:my_app

# Run application
bazel run //:my_app

# Run tests
bazel test //:unit

# Clean build
bazel clean --expunge
```

## Migration from Maven

If you're migrating from a Maven-based Quarkus project:

1. **Copy dependencies** from `pom.xml` to `WORKSPACE`
2. **Convert Maven coordinates** to Bazel format
3. **Update import statements** to use Bazel targets
4. **Test incrementally** starting with basic builds

### Maven to Bazel Dependency Mapping

| Maven | Bazel |
|-------|-------|
| `io.quarkus:quarkus-core:3.17.6` | `@maven//:io_quarkus_quarkus_core` |
| `jakarta.annotation:jakarta.annotation-api:3.0.0-M1` | `@maven//:jakarta_annotation_jakarta_annotation_api` |
| `org.junit.jupiter:junit-jupiter:5.11.3` | `@maven//:org_junit_jupiter_junit_jupiter` |

## Troubleshooting

### Common Issues

1. **Dependency Resolution**: Ensure `rules_jvm_external` is properly configured
2. **Java Version**: Use Java 21 for Quarkus 3.x
3. **Bazel Version**: Use Bazel 6.0+ for best compatibility

### Debug Commands

```bash
# Check dependencies
bazel query --output=location @maven//:io_quarkus_quarkus_core

# Show dependency graph
bazel query --noimplicit_deps "deps(//:my_app)" --output=graph

# Check what's loaded
bazel query --output=build //:my_app
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests if applicable
5. Submit a pull request

## License

This project is licensed under the Apache License 2.0.

## Support

- [Quarkus Documentation](https://quarkus.io/guides/)
- [Bazel Documentation](https://bazel.build/docs)
- [rules_jvm_external](https://github.com/bazel-contrib/rules_jvm_external)

## Version History

- **v1.0.0** - Initial release with basic Quarkus support
- **v1.1.0** - Added comprehensive testing support
- **v1.2.0** - Enhanced dependency management and examples
- **v1.3.0** - Added templates and cross-repository usage support

# rules_quarkus

A comprehensive Bazel rule set for building Quarkus applications with full dependency management, testing support, and multiple deployment options.

## Features

- **Quarkus Application Building** - Create fat JARs and executable applications
- **Dependency Management** - Full Maven dependency resolution through rules_jvm_external
- **Testing Support** - JUnit 5 integration with comprehensive test suites
- **Multiple Build Modes** - Production, development, and testing configurations
- **Cross-Repository Usage** - Easy integration into other Bazel projects

## Installation

### Option 1: Git Repository (Recommended for other projects)

Add to your `WORKSPACE`:

```bazel
load("@bazel_tools//tools/build_defs/repo:git.bzl", "git_repository")

# For private repositories (SSH)
git_repository(
    name = "rules_quarkus",
    remote = "git@github.com:your-username/rules_quarkus.git",
    commit = "main",  # or a specific tag like "v1.0.0"
)

# For public repositories (HTTPS)
# git_repository(
#     name = "rules_quarkus",
#     remote = "https://github.com/your-username/rules_quarkus.git",
#     commit = "v1.0.0",
# )
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
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_quarkus",
    sha256 = "hash_of_the_zip_file",
    strip_prefix = "rules_quarkus-1.0.0",
    url = "https://github.com/your-username/rules_quarkus/archive/v1.0.0.zip",
)
```

### Option 4: Bzlmod (if migrating to MODULE.bazel)

```bazel
# In your MODULE.bazel
bazel_dep(name = "rules_quarkus", version = "1.0.0")
```

## Loading

```starlark
load("@rules_quarkus//quarkus:quarkus.bzl", "quarkus_app")
```

## Basic Usage

### 1. Create a Java Library

```starlark
java_library(
    name = "artifact",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**"]),
    deps = [
        # Add your dependencies here
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

## Advanced Usage

### Complete Project Structure

```starlark
# Main application library
java_library(
    name = "artifact",
    srcs = glob(["src/main/java/**/*.java"]),
    resources = glob(["src/main/resources/**"]),
    deps = [
        "@maven//:io_quarkus_quarkus_core",
        "@maven//:io_quarkus_quarkus_resteasy",
        "@maven//:io_quarkus_quarkus_resteasy_jackson",
        "@maven//:jakarta_annotation_jakarta_annotation_api",
    ],
)

# Test utilities
java_library(
    name = "test_utility",
    srcs = glob(["src/test/utils/**/*.java"]),
    deps = [":artifact"],
)

# Unit tests
java_test(
    name = "unit",
    srcs = glob(["src/test/unit/java/**/*.java"]),
    deps = [
        ":artifact",
        ":test_utility",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
        "@maven//:org_assertj_assertj_core",
    ],
)

# Integration tests
java_test(
    name = "integration",
    srcs = glob(["src/test/integration/java/**/*.java"]),
    deps = [
        ":artifact",
        ":test_utility",
        "@maven//:org_junit_jupiter_junit_jupiter_api",
    ],
)

# Production Quarkus app
quarkus_app(
    name = "my_app",
    main_class = "com.example.Main",
    deps = [":artifact"],
)

# Development Quarkus app
quarkus_app(
    name = "my_app_dev",
    main_class = "com.example.Main",
    deps = [":artifact"],
)

# Test runner
java_binary(
    name = "test_runner",
    main_class = "com.example.TestRunner",
    srcs = glob(["src/test/java/**/*.java"]),
    deps = [":artifact"],
)

# Distribution package
filegroup(
    name = "distribution",
    srcs = [
        ":my_app",
        ":my_app_dev",
        "README.md",
    ],
)

# All targets
filegroup(
    name = "all",
    srcs = [
        ":my_app",
        ":my_app_dev",
        ":test_runner",
        ":distribution",
    ],
)
```

## Dependencies

The `rules_quarkus` package works with `rules_jvm_external` for Maven dependency management. Add this to your `WORKSPACE`:

```bazel
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http_archive")

http_archive(
    name = "rules_jvm_external",
    sha256 = "85776be6d8fe64abf26f463a8e12cd4c15be927348397180a01693610da7ec90",
    strip_prefix = "rules_jvm_external-6.4",
    url = "https://github.com/bazel-contrib/rules_jvm_external/releases/download/6.4/rules_jvm_external-6.4.tar.gz",
)

load("@rules_jvm_external//:repositories.bzl", "rules_jvm_external_deps")
rules_jvm_external_deps()

load("@rules_jvm_external//:setup.bzl", "rules_jvm_external_setup")
rules_jvm_external_setup()

# Maven dependencies
load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        "io.quarkus:quarkus-core:3.17.6",
        "io.quarkus:quarkus-resteasy:3.17.6",
        "io.quarkus:quarkus-resteasy-jackson:3.17.6",
        "jakarta.annotation:jakarta.annotation-api:3.0.0-M1",
        "org.junit.jupiter:junit-jupiter:5.11.3",
        "org.assertj:assertj-core:3.25.3",
    ],
    repositories = [
        "https://repo.maven.apache.org/maven2",
    ],
)
```

## Testing

### Running Tests

```bash
# Run unit tests
bazel test //:unit

# Run integration tests
bazel test //:integration

# Run all tests
bazel test //:unit //:integration

# Run tests as binary (alternative)
bazel run //:test_runner
```

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

## Examples

### Simple REST API

```java
package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/")
public class HelloResource {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus with Bazel!";
    }
}
```

### Test Example

```java
package com.example;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloResourceTest {
    
    @Test
    void testHelloEndpoint() {
        HelloResource resource = new HelloResource();
        String result = resource.hello();
        assertThat(result).contains("Hello from Quarkus");
    }
}
```

## Configuration

### application.properties

```properties
# Quarkus configuration
quarkus.http.port=8080
quarkus.http.host=0.0.0.0

# Logging
quarkus.log.level=INFO
quarkus.log.category."com.example".level=DEBUG
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
4. Add tests
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

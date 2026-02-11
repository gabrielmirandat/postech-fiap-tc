# Quarkus Example with WORKSPACE

This is a Quarkus example project that uses Bazel's traditional **WORKSPACE** system instead of the newer **MODULE.bazel** (Bzlmod) system. It follows **exactly the same pattern** as the working Spring Boot project.

## WORKSPACE vs MODULE

**This project uses WORKSPACE** (traditional Bazel):
- ✅ Uses `WORKSPACE` file for external dependencies
- ✅ Uses `rules_jvm_external` with `maven_install`
- ✅ Dependencies referenced as `@maven//:artifact_name`
- ✅ Follows the exact same pattern as the working Spring Boot project

**Not using MODULE.bazel** (Bzlmod):
- ❌ No `MODULE.bazel` file
- ❌ No `maven.deps` or `maven.install`
- ❌ No `artifact("group:artifact:version")` syntax

## Project Structure

```
quarkus_example_workspace/
├── WORKSPACE                    # Bazel workspace configuration (same pattern as Spring Boot)
├── BUILD.bazel                  # Build rules (same pattern as Spring Boot)
├── src/
│   ├── main/
│   │   ├── java/com/example/
│   │   │   └── Main.java       # Main Quarkus application
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── unit/java/com/example/      # Unit tests
│       ├── integration/java/com/example/ # Integration tests
│       ├── contract/java/com/example/   # Contract tests
│       ├── behavior/java/com/example/   # Behavior tests
│       ├── utils/java/com/example/      # Test utilities
│       └── resources/                   # Test resources
└── README.md
```

## WORKSPACE Configuration

The `WORKSPACE` file follows **exactly the same pattern** as the working Spring Boot project:

1. **Version constants** - Same pattern for dependency versions
2. **rules_jvm_external** - Same setup and configuration
3. **rules_java** - Same setup and configuration  
4. **contrib_rules_jvm** - Same setup and configuration
5. **Maven dependencies** - Same `maven_install` pattern
6. **Local repository** - References our custom `rules_quarkus`

## Usage

### Build the application
```bash
bazel build //:hello_quarkus
bazel build //:hello_quarkus_dev
```

### Run tests
```bash
# Run all test suites (same pattern as Spring Boot)
bazel test //:unit
bazel test //:integration
bazel test //:contract
bazel test //:behavior

# Run specific test
bazel test //:unit
```

### Build all targets
```bash
bazel build //:all
```

### Build distribution
```bash
bazel build //:hello_quarkus_distribution
```

## Available Targets

Following **exactly the same pattern** as the Spring Boot project:

| Target | Description | Command |
|--------|-------------|---------|
| `:artifact` | Main Java library | `bazel build //:artifact` |
| `:test_utility` | Test utilities | `bazel build //:test_utility` |
| `:unit` | Unit tests | `bazel test //:unit` |
| `:integration` | Integration tests | `bazel test //:integration` |
| `:contract` | Contract tests | `bazel test //:contract` |
| `:behavior` | Behavior tests | `bazel test //:behavior` |
| `:hello_quarkus` | Production Quarkus app | `bazel build //:hello_quarkus` |
| `:hello_quarkus_dev` | Development Quarkus app | `bazel build //:hello_quarkus_dev` |
| `:hello_quarkus_test` | Test runner | `bazel build //:hello_quarkus_test` |
| `:hello_quarkus_distribution` | Distribution package | `bazel build //:hello_quarkus_distribution` |
| `:all` | All main targets | `bazel build //:all` |

## Comparison Table

| Aspect | Spring Boot Project | This Quarkus Project |
|--------|---------------------|----------------------|
| **Build System** | WORKSPACE | WORKSPACE ✅ |
| **Dependency Management** | rules_jvm_external | rules_jvm_external ✅ |
| **Maven Dependencies** | @maven//:artifact_name | @maven//:artifact_name ✅ |
| **Test Structure** | unit/integration/contract/behavior | unit/integration/contract/behavior ✅ |
| **Test Framework** | JUnit 5 + AssertJ + Mockito | JUnit 5 + AssertJ + Mockito ✅ |
| **Java Version** | 21 | 21 ✅ |
| **Target Names** | Same pattern | Same pattern ✅ |

## WORKSPACE Advantages

1. **Proven Pattern** - Uses the exact same structure as the working Spring Boot project
2. **Full Control** - Complete control over dependency resolution
3. **Compatibility** - Works with all Bazel versions
4. **Debugging** - Easier to debug dependency issues
5. **Flexibility** - Can mix different dependency management approaches

## When to Use WORKSPACE

- ✅ When you have a working WORKSPACE-based project to reference
- ✅ When you need full control over dependencies
- ✅ When you're using older Bazel versions
- ✅ When you want to follow a proven pattern

## Next Steps

1. **Test the build**: `bazel build //:all`
2. **Run tests**: `bazel test //:unit`
3. **Customize dependencies**: Modify the `WORKSPACE` file
4. **Add more targets**: Follow the same pattern as Spring Boot

This project demonstrates that you can use WORKSPACE with Quarkus following **exactly the same pattern** as your working Spring Boot project!

# Usage Examples for rules_quarkus

This file provides complete examples of how to integrate and use `rules_quarkus` in other Bazel projects.

## Quick Start Template

Copy this complete `WORKSPACE` and `BUILD.bazel` to get started quickly.

### WORKSPACE

```bazel
# Load Bazel tools
load("@bazel_tools//tools/build_defs/repo:http.bzl", "http.bzl")

# rules_jvm_external for Maven dependency management
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

# Load rules_quarkus
# Option 1: Git repository (recommended)
git_repository(
    name = "rules_quarkus",
    remote = "git@github.com:your-username/rules_quarkus.git",
    commit = "main",  # or a specific tag
)

# Option 2: Local repository (development)
# local_repository(
#     name = "rules_quarkus",
#     path = "/path/to/rules_quarkus",
# )

# Maven dependencies
load("@rules_jvm_external//:defs.bzl", "maven_install")

maven_install(
    artifacts = [
        # Quarkus Core
        "io.quarkus:quarkus-core:3.17.6",
        "io.quarkus:quarkus-resteasy:3.17.6",
        "io.quarkus:quarkus-resteasy-jackson:3.17.6",
        "io.quarkus:quarkus-arc:3.17.6",
        "io.quarkus:quarkus-undertow:3.17.6",
        
        # Jakarta APIs
        "jakarta.annotation:jakarta.annotation-api:3.0.0-M1",
        "jakarta.validation:jakarta.validation-api:3.1.0-M1",
        "jakarta.persistence:jakarta.persistence-api:3.1.0",
        
        # Jackson
        "com.fasterxml.jackson.core:jackson-annotations:2.18.2",
        "com.fasterxml.jackson.core:jackson-core:2.18.2",
        "com.fasterxml.jackson.core:jackson-databind:2.18.2",
        "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:2.18.2",
        
        # Testing
        "org.junit.jupiter:junit-jupiter:5.11.3",
        "org.junit.jupiter:junit-jupiter-api:5.11.3",
        "org.junit.jupiter:junit-jupiter-engine:5.11.3",
        "org.junit.platform:junit-platform-launcher:1.11.3",
        "org.assertj:assertj-core:3.25.3",
        "org.mockito:mockito-core:5.14.1",
        "org.mockito:mockito-junit-jupiter:5.14.1",
        
        # Logging
        "org.slf4j:slf4j-api:2.0.12",
        "ch.qos.logback:logback-classic:1.4.14",
    ],
    repositories = [
        "https://repo.maven.apache.org/maven2",
    ],
)
```

### BUILD.bazel

```bazel
# Load rules_quarkus
load("@rules_quarkus//quarkus:quarkus.bzl", "quarkus_app")

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
        "@maven//:jakarta_validation_jakarta_validation_api",
    ],
)

# Production Quarkus application
quarkus_app(
    name = "my_app",
    main_class = "com.example.Main",
    deps = [":artifact"],
)

# Development Quarkus application
quarkus_app(
    name = "my_app_dev",
    main_class = "com.example.Main",
    deps = [":artifact"],
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

# All targets
filegroup(
    name = "all",
    srcs = [
        ":my_app",
        ":my_app_dev",
        ":unit",
        ":integration",
    ],
)
```

## Project Structure

```
my-quarkus-project/
├── WORKSPACE              # Bazel workspace configuration
├── BUILD.bazel            # Build rules
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           ├── Main.java
│   │   │           └── HelloResource.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       ├── unit/
│       │   └── java/
│       │       └── com/
│       │           └── example/
│       │               └── HelloResourceTest.java
│       ├── integration/
│       │   └── java/
│       │       └── com/
│       │           └── example/
│       │               └── HelloResourceIntegrationTest.java
│       ├── utils/
│       │   └── java/
│       │       └── com/
│       │           └── example/
│       │               └── TestUtils.java
│       └── resources/
│           └── test.properties
└── README.md
```

## Java Source Examples

### Main.java

```java
package com.example;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class Main {
    public static void main(String... args) {
        Quarkus.run(args);
    }
}
```

### HelloResource.java

```java
package com.example;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.validation.constraints.NotBlank;

@Path("/")
public class HelloResource {
    
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus with Bazel!";
    }
    
    @GET
    @Path("/greet/{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting greet(@NotBlank String name) {
        return new Greeting("Hello " + name + "!");
    }
    
    public static class Greeting {
        private String message;
        
        public Greeting() {}
        
        public Greeting(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
```

### Test Examples

#### HelloResourceTest.java

```java
package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloResourceTest {
    
    private HelloResource resource;
    
    @BeforeEach
    void setUp() {
        resource = new HelloResource();
    }
    
    @Test
    void testHelloEndpoint() {
        String result = resource.hello();
        assertThat(result).contains("Hello from Quarkus");
    }
    
    @Test
    void testGreetEndpoint() {
        HelloResource.Greeting greeting = resource.greet("World");
        assertThat(greeting.getMessage()).isEqualTo("Hello World!");
    }
}
```

#### HelloResourceIntegrationTest.java

```java
package com.example;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloResourceIntegrationTest {
    
    @Test
    void testResourceCreation() {
        HelloResource resource = new HelloResource();
        assertThat(resource).isNotNull();
    }
    
    @Test
    void testGreetingObject() {
        HelloResource.Greeting greeting = new HelloResource.Greeting("Test");
        assertThat(greeting.getMessage()).isEqualTo("Test");
    }
}
```

#### TestUtils.java

```java
package com.example;

public class TestUtils {
    
    public static String createTestMessage(String name) {
        return "Hello " + name + " from TestUtils!";
    }
    
    public static boolean isValidName(String name) {
        return name != null && !name.trim().isEmpty();
    }
}
```

## Configuration Examples

### application.properties

```properties
# Quarkus HTTP configuration
quarkus.http.port=8080
quarkus.http.host=0.0.0.0

# Logging configuration
quarkus.log.level=INFO
quarkus.log.category."com.example".level=DEBUG

# Application configuration
app.greeting.message=Hello from Quarkus with Bazel!
app.greeting.default-name=World
```

### test.properties

```properties
# Test configuration
test.environment=unit
test.timeout=30
test.retries=3
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
bazel test //:integration

# Clean build
bazel clean --expunge
```

## Advanced Configuration

### Custom Dependencies

Add more Maven dependencies to your `WORKSPACE`:

```bazel
maven_install(
    artifacts = [
        # Existing dependencies...
        
        # Additional Quarkus extensions
        "io.quarkus:quarkus-hibernate-validator:3.17.6",
        "io.quarkus:quarkus-smallrye-health:3.17.6",
        "io.quarkus:quarkus-metrics:3.17.6",
        
        # Database
        "io.quarkus:quarkus-hibernate-orm:3.17.6",
        "io.quarkus:quarkus-jdbc-postgresql:3.17.6",
        
        # Security
        "io.quarkus:quarkus-security-jwt:3.17.6",
        "io.quarkus:quarkus-oidc:3.17.6",
    ],
    repositories = [
        "https://repo.maven.apache.org/maven2",
    ],
)
```

### Custom Build Rules

Extend the build configuration with additional targets:

```bazel
# Docker image
container_image(
    name = "my_app_image",
    base = "@openjdk_21//image",
    files = [":my_app"],
    cmd = ["java", "-jar", "my_app.jar"],
)

# Kubernetes deployment
k8s_object(
    name = "my_app_deployment",
    template = "deployment.yaml",
    images = [":my_app_image"],
)
```

## Troubleshooting

### Common Issues and Solutions

1. **Dependency Resolution Errors**
   ```bash
   # Check if dependency is available
   bazel query --output=location @maven//:io_quarkus_quarkus_core
   
   # Clean and rebuild
   bazel clean --expunge
   bazel build //:my_app
   ```

2. **Java Version Issues**
   ```bash
   # Check Java version
   java -version
   
   # Ensure Java 21 is used
   export JAVA_HOME=/path/to/java21
   ```

3. **Build Cache Issues**
   ```bash
   # Clear Bazel cache
   bazel clean --expunge
   
   # Clear Maven cache
   rm -rf ~/.m2/repository
   ```

### Debug Commands

```bash
# Show dependency graph
bazel query --noimplicit_deps "deps(//:my_app)" --output=graph

# Check what's loaded
bazel query --output=build //:my_app

# Show all targets
bazel query //...
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

## Best Practices

1. **Use version constants** in WORKSPACE for easy updates
2. **Organize tests** by type (unit, integration, contract, behavior)
3. **Create utility libraries** for common test functionality
4. **Use filegroups** to organize related targets
5. **Document dependencies** and their purposes
6. **Test incrementally** to catch issues early

## Support

- Check the [main README](../README.md) for detailed documentation
- Review [Quarkus guides](https://quarkus.io/guides/) for framework-specific information
- Consult [Bazel documentation](https://bazel.build/docs) for build system details

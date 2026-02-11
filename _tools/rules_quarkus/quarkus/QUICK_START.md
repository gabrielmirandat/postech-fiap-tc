# Quick Start Guide

Get up and running with `rules_quarkus` in under 5 minutes!

## Prerequisites

- **Java 21** - Required for Quarkus 3.x
- **Bazel 6.0+** - Build system
- **Git** - For cloning repositories

## Step 1: Clone or Download

### Option A: Clone the repository
```bash
git clone git@github.com:your-username/rules_quarkus.git
cd rules_quarkus
```

### Option B: Download templates
Download the template files:
- `TEMPLATE_WORKSPACE` → `WORKSPACE`
- `TEMPLATE_BUILD.bazel` → `BUILD.bazel`

## Step 2: Copy Templates

Copy the template files to your project:

```bash
# Copy WORKSPACE template
cp quarkus/TEMPLATE_WORKSPACE your-project/WORKSPACE

# Copy BUILD.bazel template
cp quarkus/TEMPLATE_BUILD.bazel your-project/BUILD.bazel
```

## Step 3: Customize Configuration

### Update WORKSPACE

1. **Change the repository URL**:
   ```bazel
   git_repository(
       name = "rules_quarkus",
       remote = "git@github.com:YOUR_USERNAME/rules_quarkus.git",
       commit = "main",
   )
   ```

2. **Add your dependencies**:
   ```bazel
   maven_install(
       artifacts = [
           # Existing Quarkus dependencies...
           
           # Your custom dependencies
           "your.group:your-artifact:1.0.0",
           "another.group:another-artifact:2.0.0",
       ],
       repositories = [
           "https://repo.maven.apache.org/maven2",
       ],
   )
   ```

### Update BUILD.bazel

1. **Change the main class**:
   ```bazel
   quarkus_app(
       name = "my_app",
       main_class = "com.yourcompany.YourMainClass",  # Update this
       deps = [":artifact"],
   )
   ```

2. **Add your dependencies**:
   ```bazel
   java_library(
       name = "artifact",
       srcs = glob(["src/main/java/**/*.java"]),
       resources = glob(["src/main/resources/**"]),
       deps = [
           # Existing dependencies...
           
           # Your custom dependencies
           "@maven//:your_group_your_artifact",
           "@maven//:another_group_another_artifact",
       ],
   )
   ```

## Step 4: Create Project Structure

```bash
mkdir -p src/{main,test}/{java,resources}
mkdir -p src/test/{unit,integration,contract,behavior,utils}/java/com/yourcompany
```

## Step 5: Add Source Files

### Main.java
```java
package com.yourcompany;

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
package com.yourcompany;

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

### application.properties
```properties
quarkus.http.port=8080
quarkus.http.host=0.0.0.0
```

## Step 6: Test Your Setup

```bash
# Build the application
bazel build //:my_app

# Run the application
bazel run //:my_app

# Run tests
bazel test //:unit
```

## Common Issues & Solutions

### Issue: "rules_quarkus not found"
**Solution**: Ensure the repository URL in WORKSPACE is correct and accessible.

### Issue: "Maven dependency not found"
**Solution**: Check the dependency name format in BUILD.bazel (use underscores, not dots).

### Issue: "Java version incompatible"
**Solution**: Ensure you're using Java 21: `java -version`

## Next Steps

1. **Add more endpoints** to your REST API
2. **Create comprehensive tests** using the test structure
3. **Add database support** with Quarkus extensions
4. **Configure logging** and monitoring
5. **Set up CI/CD** with Bazel

## Need Help?

- Check the [main README](README.md) for detailed documentation
- Review [usage examples](USAGE_EXAMPLES.md) for advanced configurations
- Open an issue in the repository for bugs or questions

## Example Project

See `quarkus_example/` and `quarkus_example_workspace/` for complete working examples.

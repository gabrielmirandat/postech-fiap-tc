"""Minimal Quarkus Bazel rule to build a fat jar and runnable target.

This rule is intentionally minimal and does not perform Quarkus augmentation.
"""

def _get_quarkus_jar_file_name(name, package_name = None):
    if name.endswith(".jar"):
        fail("the name attribute of the quarkus_app rule should not end with '.jar'")
    # If name is "uber", avoid generating "uber_uber.jar" - use package name prefix
    if name == "uber" and package_name:
        return package_name + "_uber.jar"
    if name == "uber":
        return "app_uber.jar"  # Fallback if package_name not available
    return name + "_uber.jar"


def _depaggregator_rule_impl(ctx):
    merged = java_common.merge([dep[java_common.provider] for dep in ctx.attr.deps])
    jars = []
    for dep in merged.transitive_runtime_jars.to_list():
        jars.append(dep)
    return [DefaultInfo(files = depset(jars))]


_depaggregator_rule = rule(
    implementation = _depaggregator_rule_impl,
    attrs = {
        "deps": attr.label_list(providers = [java_common.provider]),
    },
)


def quarkus_app(
        name,
        java_library,
        main_class,
        deps = None,
        jartools_toolchains = ["@bazel_tools//tools/jdk:current_java_runtime"],
        tags = [],
        testonly = False,
        visibility = None,
        restricted_to = None,
        target_compatible_with = []):
    """Builds a runnable Quarkus uber-jar using singlejar.

    This does not execute Quarkus augmentation; it simply builds a fat jar suitable
    for apps that can run directly via a main class.

    Args:
      name: Name of the application (without .jar).
      java_library: Label of the java_library containing application classes.
      main_class: Fully qualified main class to run.
      deps: Optional extra Java deps to include in the fat jar.
      jartools_toolchains: Toolchains for running build tools like singlejar.
      tags: Optional Bazel standard attribute.
      testonly: Optional Bazel standard attribute.
      visibility: Optional Bazel standard attribute.
      restricted_to: Optional Bazel standard attribute.
      target_compatible_with: Optional Bazel standard attribute.
    """

    # Assemble deps
    java_deps = [java_library]
    if deps != None:
        java_deps = [java_library] + deps

    # SUBRULE: aggregate transitive runtime deps
    dep_aggregator_rule = name + "_" + native.package_name() + "_deps"
    _depaggregator_rule(
        name = dep_aggregator_rule,
        deps = java_deps,
        tags = tags,
        testonly = testonly,
        restricted_to = restricted_to,
        target_compatible_with = target_compatible_with,
    )

    # IDE-friendly and runnable target: java_binary
    native.java_binary(
        name = name,
        main_class = main_class,
        runtime_deps = java_deps,
        tags = tags,
        testonly = testonly,
        restricted_to = restricted_to,
        target_compatible_with = target_compatible_with,
        visibility = visibility,
    )

    # Package fat jar with singlejar: include app jar + all runtime deps
    package_name = native.package_name()
    native.genrule(
        name = name + "_genjar",
        srcs = java_deps + [":" + dep_aggregator_rule],
        tools = ["@bazel_tools//tools/jdk:singlejar"],
        outs = [_get_quarkus_jar_file_name(name, package_name)],
        cmd = "$(location @bazel_tools//tools/jdk:singlejar) --normalize --output $@ --main_class {main_class} --sources $(SRCS)".format(main_class = main_class),
        tags = tags,
        testonly = testonly,
        restricted_to = restricted_to,
        target_compatible_with = target_compatible_with,
        toolchains = jartools_toolchains,
        visibility = visibility,
    )


def quarkus_maven(
        name,
        java_library,
        pom_xml = None,
        source_files = None,
        resource_files = None,
        deps = None,
        tags = [],
        testonly = False,
        visibility = None,
        restricted_to = None,
        target_compatible_with = []):
    """Builds Quarkus fast-jar using Maven inside Docker container.
    
    This uses the official Maven image with Quarkus to generate the JAR,
    ensuring compatibility and avoiding augmentation issues.

    Args:
      name: Name of the application artifact.
      java_library: Label of the java_library containing application classes.
      pom_xml: Optional path to existing pom.xml file (e.g., "pom.xml").
                If not provided, will try to find pom.xml in the package directory.
      source_files: Optional list of source files (e.g., glob(["src/main/java/**/*.java"])).
                    If not provided, will try to extract from java_library.
      resource_files: Optional list of resource files (e.g., glob(["src/main/resources/**"])).
      deps: Optional extra Java deps to include.
    """
    java_deps = [java_library]
    if deps != None:
        java_deps = [java_library] + deps

    # Collect source files and dependencies
    # Include all JARs from dependencies to extract source files
    all_srcs = [java_library]
    if pom_xml:
        all_srcs.append(pom_xml)
    if source_files:
        all_srcs.extend(source_files)
    if resource_files:
        all_srcs.extend(resource_files)
    if deps:
        # Add dependencies - these will be JARs that we'll extract
        all_srcs.extend(deps)
    
    # Build using Maven in Docker with source files directly
    # This is the most reliable approach - Maven compiles and packages everything
    native.genrule(
        name = name + "_maven_build",
        srcs = all_srcs,
        outs = [name + "_quarkus_app.zip"],
        cmd = """
set -e
WORKDIR=$(@D)/maven_workspace
mkdir -p $$WORKDIR/src/main/java $$WORKDIR/src/main/resources
# Copy source files from srcs
# Files from Bazel come with paths like: menu/src/main/java/com/... or absolute paths
for src in $(SRCS); do
  # Handle JAR files - extract source files from dependencies
  if [[ "$$src" == *.jar ]] || [[ "$$src" == *.srcjar ]]; then
    # Extract Java source files from JAR (dependencies like core, menu_api, etc.)
    JAR_NAME=$$(basename "$$src")
    JAR_NAME=$${{JAR_NAME%.jar}}
    JAR_NAME=$${{JAR_NAME%.srcjar}}
    TEMP_DIR=$$WORKDIR/temp_extract_$$(echo "$$JAR_NAME" | tr '/' '_' | tr ':' '_')
    mkdir -p $$TEMP_DIR
    unzip -q "$$src" -d $$TEMP_DIR 2>/dev/null || true
    # Copy ALL .java files to src/main/java, preserving package structure
    # This includes proto-generated files (ProductId, IngredientId, etc.)
    find $$TEMP_DIR -name "*.java" -type f 2>/dev/null | while read java_file; do
      relpath=$${{java_file#$$TEMP_DIR/}}
      # Skip META-INF but include everything else (com/, etc.)
      if [[ "$$relpath" != META-INF/* ]] && [[ "$$relpath" == */*.java ]]; then
        mkdir -p "$$WORKDIR/src/main/java/$$(dirname $$relpath)"
        cp "$$java_file" "$$WORKDIR/src/main/java/$$relpath" 2>/dev/null || true
      fi
    done
    rm -rf $$TEMP_DIR
    continue
  fi
  # Handle directories (like menu_api_files which is a directory with generated files)
  if [ -d "$$src" ]; then
    # This might be a directory with generated source files (e.g., OpenAPI generator output)
    if [[ "$$src" == *menu_api_files* ]] || [[ "$$src" == *api_files* ]]; then
      # Find Java files in the directory
      echo "Processing directory: $$src" >&2
      if [ ! -d "$$src" ]; then
        echo "Directory does not exist: $$src" >&2
        continue
      fi
      # List files to debug
      echo "Directory contents:" >&2
      ls -la "$$src" 2>&1 | head -10 >&2
      # Check if src/gen/java exists
      if [ -d "$$src/src/gen/java" ]; then
        echo "Found src/gen/java directory" >&2
        ls -la "$$src/src/gen/java" 2>&1 | head -5 >&2
      fi
      # Find and process Java files - use simpler approach
      DIR_JAVA_COUNT=0
      # Process files from src/gen/java if it exists
      if [ -d "$$src/src/gen/java" ]; then
        find "$$src/src/gen/java" -name "*.java" -type f 2>/dev/null | while read java_file; do
          if [ -n "$$java_file" ] && [ -f "$$java_file" ]; then
            # Extract relative path: remove everything up to and including src/gen/java/
            relpath=$${{java_file#$$src/src/gen/java/}}
            # Remove leading slash if present
            relpath=$${{relpath#/}}
            if [[ -n "$$relpath" ]] && [[ "$$relpath" == com/* ]]; then
              mkdir -p "$$WORKDIR/src/main/java/$$(dirname $$relpath)"
              if cp "$$java_file" "$$WORKDIR/src/main/java/$$relpath" 2>/dev/null; then
                DIR_JAVA_COUNT=$$((DIR_JAVA_COUNT + 1))
              fi
            fi
          fi
        done
      fi
      # Also process files from src/main/java if it exists
      if [ -d "$$src/src/main/java" ]; then
        find "$$src/src/main/java" -name "*.java" -type f 2>/dev/null | while read java_file; do
          if [ -n "$$java_file" ] && [ -f "$$java_file" ]; then
            relpath=$${{java_file#$$src/src/main/java/}}
            relpath=$${{relpath#/}}
            if [[ -n "$$relpath" ]] && [[ "$$relpath" == com/* ]]; then
              mkdir -p "$$WORKDIR/src/main/java/$$(dirname $$relpath)"
              cp "$$java_file" "$$WORKDIR/src/main/java/$$relpath" 2>/dev/null || true
            fi
          fi
        done
      fi
      echo "Extracted $$DIR_JAVA_COUNT Java files from directory $$(basename "$$src")" >&2
    fi
    continue
  fi
  # Handle Java source files
  if [[ "$$src" == *.java ]]; then
    # Extract path after src/main/java
    # Pattern: package/src/main/java/com/... -> com/...
    if [[ "$$src" == *src/main/java/* ]]; then
      relpath=$${{src#*src/main/java/}}
      mkdir -p "$$WORKDIR/src/main/java/$$(dirname $$relpath)"
      cp "$$src" "$$WORKDIR/src/main/java/$$relpath"
    elif [[ "$$src" == *src/gen/java/* ]]; then
      # OpenAPI generated files
      relpath=$${{src#*src/gen/java/}}
      mkdir -p "$$WORKDIR/src/main/java/$$(dirname $$relpath)"
      cp "$$src" "$$WORKDIR/src/main/java/$$relpath"
    fi
  # Handle resource files
  elif [[ "$$src" == *.properties ]] || [[ "$$src" == *.xml ]] || [[ "$$src" == *.yaml ]] || [[ "$$src" == *.yml ]]; then
    if [[ "$$src" == *src/main/resources/* ]]; then
      relpath=$${{src#*src/main/resources/}}
      mkdir -p "$$WORKDIR/src/main/resources/$$(dirname $$relpath)"
      cp "$$src" "$$WORKDIR/src/main/resources/$$relpath"
    fi
  # Handle pom.xml
  elif [[ "$${{src##*/}}" == "pom.xml" ]]; then
    cp "$$src" $$WORKDIR/pom.xml
  fi
done
# Create standalone pom.xml if not provided or if it has parent
# If pom.xml has <parent>, we need to create a standalone version
if [ ! -f "$$WORKDIR/pom.xml" ] || grep -q "<parent>" "$$WORKDIR/pom.xml" 2>/dev/null; then
  if [ -f "$$WORKDIR/pom.xml" ] && grep -q "<parent>" "$$WORKDIR/pom.xml"; then
    echo "pom.xml has parent, creating standalone version with all dependencies" >&2
  else
    echo "Warning: pom.xml not found, creating minimal one" >&2
  fi
  # Create standalone pom.xml with all Quarkus dependencies
  # This avoids parent resolution issues
  cat > $$WORKDIR/pom.xml << 'EOFPOM'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.gabriel</groupId>
  <artifactId>{name}</artifactId>
  <version>1.0.0</version>
  <properties>
    <quarkus.platform.version>3.31.2</quarkus.platform.version>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  </properties>
  <dependencyManagement>
    <dependencies>
      <dependency>
        <groupId>io.quarkus.platform</groupId>
        <artifactId>quarkus-bom</artifactId>
        <version>$${{quarkus.platform.version}}</version>
        <type>pom</type>
        <scope>import</scope>
      </dependency>
    </dependencies>
  </dependencyManagement>
  <dependencies>
    <!-- Quarkus Core -->
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-core</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-resteasy-jackson</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-hibernate-validator</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-mongodb-client</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-grpc</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-health</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-messaging-kafka</artifactId>
    </dependency>
    <!-- MongoDB -->
    <dependency>
      <groupId>org.mongodb</groupId>
      <artifactId>mongodb-driver-sync</artifactId>
      <version>5.6.2</version>
    </dependency>
    <!-- Mongock -->
    <dependency>
      <groupId>io.mongock</groupId>
      <artifactId>mongock-standalone</artifactId>
      <version>5.4.1</version>
    </dependency>
    <dependency>
      <groupId>io.mongock</groupId>
      <artifactId>mongodb-sync-v4-driver</artifactId>
      <version>5.4.1</version>
    </dependency>
    <!-- Protobuf -->
    <dependency>
      <groupId>com.google.protobuf</groupId>
      <artifactId>protobuf-java</artifactId>
      <version>4.28.3</version>
    </dependency>
    <!-- gRPC -->
    <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-api</artifactId>
      <version>1.68.1</version>
    </dependency>
    <dependency>
      <groupId>io.grpc</groupId>
      <artifactId>grpc-stub</artifactId>
      <version>1.68.1</version>
    </dependency>
    <!-- Jackson -->
    <dependency>
      <groupId>com.fasterxml.jackson.datatype</groupId>
      <artifactId>jackson-datatype-jsr310</artifactId>
      <version>2.20.1</version>
    </dependency>
    <!-- CloudEvents -->
    <dependency>
      <groupId>io.cloudevents</groupId>
      <artifactId>cloudevents-core</artifactId>
      <version>2.5.0</version>
    </dependency>
    <dependency>
      <groupId>io.cloudevents</groupId>
      <artifactId>cloudevents-kafka</artifactId>
      <version>2.5.0</version>
    </dependency>
    <!-- Protobuf Validation -->
    <dependency>
      <groupId>build.buf</groupId>
      <artifactId>protovalidate</artifactId>
      <version>0.5.0</version>
    </dependency>
    <!-- javax.annotation for Generated annotation (used by gRPC) -->
    <dependency>
      <groupId>javax.annotation</groupId>
      <artifactId>javax.annotation-api</artifactId>
      <version>1.3.2</version>
    </dependency>
  </dependencies>
  <build>
    <plugins>
      <plugin>
        <groupId>org.apache.maven.plugins</groupId>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.13.0</version>
        <configuration>
          <source>21</source>
          <target>21</target>
        </configuration>
      </plugin>
      <plugin>
        <groupId>io.quarkus</groupId>
        <artifactId>quarkus-maven-plugin</artifactId>
        <version>$${{quarkus.platform.version}}</version>
        <extensions>true</extensions>
        <executions>
          <execution>
            <goals>
              <goal>build</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
    </plugins>
  </build>
</project>
EOFPOM
fi
# Use Docker to run Maven build with Quarkus
# Use absolute path for Docker volume (required by Docker)
WORKDIR_ABS=$$(cd $$WORKDIR && pwd)
docker run --rm \\
  -v "$$WORKDIR_ABS:/workspace" \\
  -w /workspace \\
  maven:3.9-eclipse-temurin-21 \\
  sh -c "
    mvn clean package -DskipTests -Dquarkus.package.type=fast-jar
  "
# Copy generated quarkus-app
if [ -d "$$WORKDIR/target/quarkus-app" ]; then
  cd $$WORKDIR/target
  zip -r "$$PWD/../../{name}_quarkus_app.zip" quarkus-app
else
  echo "Error: quarkus-app not found in target/" >&2
  ls -la $$WORKDIR/target/ >&2
  exit 1
fi
""".format(name = name),
        tags = tags + ["requires-docker"],
        testonly = testonly,
        restricted_to = restricted_to,
        target_compatible_with = target_compatible_with,
    )

    # Expose a filegroup for consumers
    native.filegroup(
        name = name,
        srcs = [":" + name + "_maven_build"],
        visibility = visibility,
    )


def quarkus_fastjar(
        name,
        java_library,
        deps = None,
        tags = [],
        testonly = False,
        visibility = None,
        restricted_to = None,
        target_compatible_with = []):
    """Builds Quarkus fast-jar (quarkus-app/) by invoking an augmentation tool.

    Args:
      name: Name of the application artifact.
      java_library: Label of the java_library containing application classes.
      deps: Optional extra Java deps to include.
    """
    java_deps = [java_library]
    if deps != None:
        java_deps = [java_library] + deps

    # Aggregate runtime deps
    dep_aggregator_rule = name + "_" + native.package_name() + "_deps"
    _depaggregator_rule(
        name = dep_aggregator_rule,
        deps = java_deps,
        tags = tags,
        testonly = testonly,
        restricted_to = restricted_to,
        target_compatible_with = target_compatible_with,
    )

    # Produce a plain jar of the app classes (java_library runtime jar)
    # Locate the app jar path
    appjar_path = name + "_appjar.path"
    native.genrule(
        name = name + "_locate_appjar",
        srcs = [java_library],
        outs = [appjar_path],
        cmd = "for f in $(SRCS); do case \"$${f}\" in *.jar) echo $${f} > $@ ;; esac; done",
        tags = tags,
        testonly = testonly,
        restricted_to = restricted_to,
        target_compatible_with = target_compatible_with,
    )

    # Invoke augmentation tool to produce quarkus-app
    # Build classpath from all dependency JARs and pass to augmenter via -cp
    native.genrule(
        name = name + "_augment",
        srcs = [":" + dep_aggregator_rule, ":" + name + "_locate_appjar"],
        tools = ["//_tools/rules_quarkus/quarkus/tools:augmenter", "@bazel_tools//tools/jdk:current_java_runtime"],
        outs = [name + "_quarkus_app.zip"],
        cmd = """
set -e
APPJAR=$$(cat $(location :{loc}))
OUTDIR=$(@D)/{name}_quarkus_app
DEPSDIR=$(@D)/{name}_deps
mkdir -p "$$OUTDIR"
mkdir -p "$$DEPSDIR"
# Copy all dependency JARs to deps directory (excluding the app jar itself)
for jar in $(SRCS); do
  if [[ "$${{jar}}" == *.jar ]] && [[ "$${{jar}}" != "$$APPJAR" ]]; then
    cp "$${{jar}}" "$$DEPSDIR/"
  fi
done
# Build classpath from all dependency JARs for augmenter execution
CLASSPATH_FILE=$(@D)/classpath.txt
> "$$CLASSPATH_FILE"
for jar in $(SRCS); do
  if [[ "$${{jar}}" == *.jar ]] && [[ "$${{jar}}" != "$$APPJAR" ]]; then
    echo -n "$${{jar}}:" >> "$$CLASSPATH_FILE"
  fi
done
sed -i 's/:$$//' "$$CLASSPATH_FILE"
CLASSPATH=$$(cat "$$CLASSPATH_FILE")
# Get augmenter JAR path
AUGMENTER_DIR=$$(dirname $(location //_tools/rules_quarkus/quarkus/tools:augmenter))
AUGMENTER_JAR=$$(find "$$AUGMENTER_DIR" -name "augmenter.jar" | head -1)
# Run augmenter with classpath and deps directory
# Augmenter will use AdditionalDependency to pass deps to Quarkus
java -cp "$$CLASSPATH:$$AUGMENTER_JAR" com.rulesquarkus.tools.Augment "$$APPJAR" "$$OUTDIR" "$$DEPSDIR"
cd "$$OUTDIR"
zip -r "$$PWD/../{name}_quarkus_app.zip" .
""".format(loc = name + "_locate_appjar", name = name),
        tags = tags,
        testonly = testonly,
        restricted_to = restricted_to,
        target_compatible_with = target_compatible_with,
    )

    # Expose a filegroup for consumers to unpack quarkus-app
    native.filegroup(
        name = name,
        srcs = [":" + name + "_augment"],
        visibility = visibility,
    )

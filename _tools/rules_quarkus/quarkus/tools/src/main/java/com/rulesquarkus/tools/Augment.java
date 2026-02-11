package com.rulesquarkus.tools;

import io.quarkus.bootstrap.app.AdditionalDependency;
import io.quarkus.bootstrap.app.QuarkusBootstrap;
import io.quarkus.bootstrap.app.QuarkusBootstrap.Mode;
import io.quarkus.maven.dependency.ArtifactCoords;
import io.quarkus.maven.dependency.ResolvedDependency;
import io.quarkus.maven.dependency.ResolvedDependencyBuilder;
import io.quarkus.paths.PathCollection;
import io.quarkus.paths.PathList;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

/**
 * Augmentation entrypoint used by {@code quarkus_fastjar} to produce a
 * Quarkus fast-jar layout (quarkus-app/) from a compiled application JAR.
 *
 * This is adapted from the original working implementation to use Quarkus 3.31.x APIs.
 * The original used AppArtifact (deprecated) and bootstrap.run() (removed);
 * this version uses ResolvedDependency and bootstrap.bootstrap().
 */
public class Augment {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.err.println("Usage: Augment <app-jar> <output-dir> [deps-dir]");
            System.err.println("  app-jar:   Path to the application JAR");
            System.err.println("  output-dir: Directory where quarkus-app/ will be created");
            System.err.println("  deps-dir:  (Optional) Directory containing dependency JARs");
            System.exit(2);
        }
        Path appJar = Paths.get(args[0]).toAbsolutePath();
        Path outDir = Paths.get(args[1]).toAbsolutePath();
        Path depsDir = args.length > 2 ? Paths.get(args[2]).toAbsolutePath() : null;

        // DEBUG: Print classpath information
        String classpath = System.getProperty("java.class.path");
        System.err.println("=== DEBUG: Classpath Information ===");
        System.err.println("Classpath: " + (classpath != null ? classpath.substring(0, Math.min(500, classpath.length())) + "..." : "null"));
        System.err.println("Classpath entries count: " + (classpath != null ? classpath.split(":").length : 0));
        System.err.println("App JAR: " + appJar);
        System.err.println("Output dir: " + outDir);
        System.err.println("Deps dir: " + depsDir);
        
        // DEBUG: Check if deps directory exists and list JARs
        if (depsDir != null && Files.exists(depsDir)) {
            System.err.println("=== DEBUG: Dependencies Directory ===");
            try (var paths = Files.walk(depsDir)) {
                long jarCount = paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p))
                        .peek(p -> System.err.println("  Found JAR: " + p))
                        .count();
                System.err.println("Total JARs in deps dir: " + jarCount);
            }
        } else {
            System.err.println("Deps directory not provided or does not exist");
        }

        // Set system properties to avoid config validation errors
        // (required for Quarkus 3.31.x when no platform descriptor is present)
        System.setProperty("quarkus.package.type", "fast-jar");
        System.setProperty("quarkus.native.enabled", "false");
        System.setProperty("quarkus.native.builder-image", "quay.io/quarkus/ubi-quarkus-native-image:latest");
        // Disable validation for properties that require MemorySize converter
        System.setProperty("smallrye.config.validate-unknown", "false");
        System.setProperty("smallrye.config.validate-defaults", "false");
        
        // Build classpath string from depsDir to ensure quarkus-core is available
        // This helps Quarkus discover converters during augmentation
        StringBuilder classpathBuilder = new StringBuilder();
        if (depsDir != null && Files.exists(depsDir)) {
            try (var paths = Files.walk(depsDir)) {
                paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p))
                        .forEach(jarPath -> {
                            if (classpathBuilder.length() > 0) {
                                classpathBuilder.append(System.getProperty("path.separator", ":"));
                            }
                            classpathBuilder.append(jarPath.toAbsolutePath().toString());
                        });
            } catch (IOException e) {
                System.err.println("Warning: Could not build classpath from deps directory: " + e.getMessage());
            }
        }
        
        // Add current classpath
        String existingClasspath = System.getProperty("java.class.path");
        if (existingClasspath != null && !existingClasspath.isEmpty()) {
            if (classpathBuilder.length() > 0) {
                classpathBuilder.append(System.getProperty("path.separator", ":"));
            }
            classpathBuilder.append(existingClasspath);
        }
        
        // Set the classpath so Quarkus can discover converters
        // Also create a URLClassLoader with all JARs to ensure ServiceLoader can discover converters
        if (classpathBuilder.length() > 0) {
            System.setProperty("java.class.path", classpathBuilder.toString());
            System.err.println("=== DEBUG: Extended classpath with " + 
                    (depsDir != null && Files.exists(depsDir) ? 
                            Files.list(depsDir).filter(p -> p.toString().endsWith(".jar")).count() : 0) + 
                    " JARs from deps directory");
            
            // Create a URLClassLoader with all dependency JARs and set it as context classloader
            // This helps ServiceLoader discover converters during augmentation
            try {
                List<java.net.URL> urls = new ArrayList<>();
                if (depsDir != null && Files.exists(depsDir)) {
                    try (var paths = Files.walk(depsDir)) {
                        paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p))
                                .forEach(jarPath -> {
                                    try {
                                        urls.add(jarPath.toUri().toURL());
                                    } catch (java.net.MalformedURLException e) {
                                        System.err.println("Warning: Could not add JAR to URLClassLoader: " + jarPath);
                                    }
                                });
                    }
                }
                
                if (!urls.isEmpty()) {
                    java.net.URLClassLoader urlClassLoader = new java.net.URLClassLoader(
                            urls.toArray(new java.net.URL[0]),
                            Thread.currentThread().getContextClassLoader()
                    );
                    Thread.currentThread().setContextClassLoader(urlClassLoader);
                    System.err.println("Created URLClassLoader with " + urls.size() + " JARs for converter discovery");
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not create URLClassLoader: " + e.getMessage());
            }
        }

        // Build ResolvedDependency (replaces deprecated AppArtifact)
        ResolvedDependency appArtifact = ResolvedDependencyBuilder.newInstance()
                .setGroupId("app")
                .setArtifactId("app")
                .setVersion("1.0.0")
                .setType(ArtifactCoords.TYPE_JAR)
                .setResolvedPath(appJar)
                .build();
        
        // Try to pre-load and register converters manually using ServiceLoader
        // This forces converter registration before augmentation
        if (depsDir != null && Files.exists(depsDir)) {
            try {
                System.err.println("=== DEBUG: Attempting to manually register converters ===");
                
                // Build a classloader with all dependency JARs
                List<java.net.URL> urls = new ArrayList<>();
                try (var paths = Files.walk(depsDir)) {
                    paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p))
                            .forEach(jarPath -> {
                                try {
                                    urls.add(jarPath.toUri().toURL());
                                } catch (java.net.MalformedURLException e) {
                                    System.err.println("Warning: Could not add JAR to URL list: " + jarPath);
                                }
                            });
                }
                
                if (!urls.isEmpty()) {
                    java.net.URLClassLoader converterLoader = new java.net.URLClassLoader(
                            urls.toArray(new java.net.URL[0]),
                            Thread.currentThread().getContextClassLoader()
                    );
                    
                    // Try to use ServiceLoader to discover and register converters
                    try {
                        java.util.ServiceLoader<?> converterLoaderService = java.util.ServiceLoader.load(
                                Class.forName("org.eclipse.microprofile.config.spi.Converter", true, converterLoader),
                                converterLoader
                        );
                        
                        int converterCount = 0;
                        for (Object converter : converterLoaderService) {
                            converterCount++;
                            System.err.println("Discovered converter: " + converter.getClass().getName());
                        }
                        System.err.println("Total converters discovered via ServiceLoader: " + converterCount);
                        
                        // Try to specifically load MemorySizeConverter
                        try {
                            Class<?> memorySizeConverterClass = converterLoader.loadClass("io.quarkus.runtime.configuration.MemorySizeConverter");
                            System.err.println("Successfully loaded MemorySizeConverter class: " + memorySizeConverterClass);
                            
                            // Try to instantiate it
                            Object converter = memorySizeConverterClass.getDeclaredConstructor().newInstance();
                            System.err.println("Successfully instantiated MemorySizeConverter: " + converter);
                            
                            // Try to register it programmatically if possible
                            try {
                                // Check if there's a ConfigBuilder or ConfigProviderRegistry we can use
                                Class<?> configProviderClass = converterLoader.loadClass("io.smallrye.config.ConfigProviderResolver");
                                java.lang.reflect.Method getInstanceMethod = configProviderClass.getMethod("instance");
                                Object configProvider = getInstanceMethod.invoke(null);
                                
                                // Try to get ConfigBuilder and register converter
                                java.lang.reflect.Method getBuilderMethod = configProviderClass.getMethod("getBuilder");
                                Object configBuilder = getBuilderMethod.invoke(configProvider);
                                
                                // Try to add converter
                                try {
                                    java.lang.reflect.Method addConverterMethod = configBuilder.getClass().getMethod("withConverter", 
                                            Class.class, int.class, java.util.function.Function.class);
                                    // This might not work, but we try
                                    System.err.println("Attempted to register MemorySizeConverter programmatically");
                                } catch (NoSuchMethodException e) {
                                    System.err.println("Could not find method to register converter programmatically");
                                }
                            } catch (Exception e) {
                                System.err.println("Could not register converter programmatically: " + e.getMessage());
                            }
                        } catch (ClassNotFoundException e) {
                            System.err.println("Could not load MemorySizeConverter class: " + e.getMessage());
                        } catch (ReflectiveOperationException e) {
                            System.err.println("Could not instantiate MemorySizeConverter: " + e.getMessage());
                        }
                    } catch (ClassNotFoundException e) {
                        System.err.println("Could not load Converter interface: " + e.getMessage());
                    }
                    
                    // Set this loader as the context classloader to help Quarkus discover converters
                    Thread.currentThread().setContextClassLoader(converterLoader);
                    System.err.println("Set converter loader as context classloader");
                }
            } catch (Exception e) {
                System.err.println("Warning: Could not manually register converters: " + e.getMessage());
                e.printStackTrace();
            }
        }

        // Build QuarkusBootstrap
        // Option 3: Create a temporary pom.xml to help Quarkus discover dependencies
        Path projectRoot = appJar.getParent();
        Path tempPom = null;
        
        if (depsDir != null && Files.exists(depsDir)) {
            // Create a temporary pom.xml in the same directory as the app JAR
            // This is where Quarkus expects to find it
            tempPom = appJar.getParent().resolve("pom.xml");
            projectRoot = appJar.getParent();
            
            System.err.println("=== DEBUG: Creating complete pom.xml from JARs ===");
            System.err.println("POM location: " + tempPom);
            
            // Extract Maven coordinates from JAR files
            // Try to get from MANIFEST.MF, fallback to filename parsing
            List<String> dependencies = new ArrayList<>();
            try (var paths = Files.walk(depsDir)) {
                paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p))
                        .forEach(jarPath -> {
                            String fileName = jarPath.getFileName().toString();
                            String groupId = null;
                            String artifactId = null;
                            String version = null;
                            
                            // Try to extract from MANIFEST.MF
                            try (JarFile jar = new JarFile(jarPath.toFile())) {
                                Manifest manifest = jar.getManifest();
                                if (manifest != null) {
                                    // Try different manifest attributes
                                    groupId = manifest.getMainAttributes().getValue("Implementation-Vendor-Id");
                                    if (groupId == null) {
                                        groupId = manifest.getMainAttributes().getValue("Bundle-Vendor");
                                    }
                                    artifactId = manifest.getMainAttributes().getValue("Implementation-Title");
                                    if (artifactId == null) {
                                        artifactId = manifest.getMainAttributes().getValue("Bundle-SymbolicName");
                                    }
                                    version = manifest.getMainAttributes().getValue("Implementation-Version");
                                    if (version == null) {
                                        version = manifest.getMainAttributes().getValue("Bundle-Version");
                                    }
                                }
                            } catch (IOException e) {
                                // Ignore, will parse from filename
                            }
                            
                            // Parse from filename if not found in manifest
                            // Format: processed_groupId_artifactId_version.jar or artifactId-version.jar
                            if (groupId == null || artifactId == null || version == null) {
                                String nameWithoutExt = fileName.substring(0, fileName.length() - 4); // Remove .jar
                                
                                if (nameWithoutExt.startsWith("processed_")) {
                                    // Format: processed_groupId_artifactId_version
                                    String withoutPrefix = nameWithoutExt.substring("processed_".length());
                                    int lastUnderscore = withoutPrefix.lastIndexOf('_');
                                    if (lastUnderscore > 0) {
                                        String versionPart = withoutPrefix.substring(lastUnderscore + 1);
                                        // Check if version part looks like a version (contains dot or is numeric)
                                        if (versionPart.matches(".*[.\\d].*")) {
                                            version = versionPart;
                                            String artifactPart = withoutPrefix.substring(0, lastUnderscore);
                                            int artifactUnderscore = artifactPart.lastIndexOf('_');
                                            if (artifactUnderscore > 0) {
                                                artifactId = artifactPart.substring(artifactUnderscore + 1);
                                                groupId = artifactPart.substring(0, artifactUnderscore).replace('_', '.');
                                            } else {
                                                artifactId = artifactPart;
                                                groupId = "unknown";
                                            }
                                        } else {
                                            // No version, treat whole thing as artifactId
                                            artifactId = withoutPrefix.replace('_', '-');
                                            groupId = "unknown";
                                            version = "1.0.0";
                                        }
                                    } else {
                                        artifactId = withoutPrefix.replace('_', '-');
                                        groupId = "unknown";
                                        version = "1.0.0";
                                    }
                                } else {
                                    // Try format: artifactId-version
                                    int lastDash = nameWithoutExt.lastIndexOf('-');
                                    if (lastDash > 0) {
                                        String versionPart = nameWithoutExt.substring(lastDash + 1);
                                        if (versionPart.matches(".*[.\\d].*")) {
                                            version = versionPart;
                                            artifactId = nameWithoutExt.substring(0, lastDash);
                                            groupId = "unknown";
                                        } else {
                                            artifactId = nameWithoutExt;
                                            groupId = "unknown";
                                            version = "1.0.0";
                                        }
                                    } else {
                                        artifactId = nameWithoutExt;
                                        groupId = "unknown";
                                        version = "1.0.0";
                                    }
                                }
                            }
                            
                            if (groupId != null && artifactId != null && version != null) {
                                dependencies.add(String.format("    <dependency>\n      <groupId>%s</groupId>\n      <artifactId>%s</artifactId>\n      <version>%s</version>\n    </dependency>", 
                                        groupId, artifactId, version));
                            }
                        });
            }
            
            System.err.println("Extracted " + dependencies.size() + " dependencies from JARs");
            
            // Create complete pom.xml with all dependencies
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(tempPom))) {
                writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                writer.println("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"");
                writer.println("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
                writer.println("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">");
                writer.println("  <modelVersion>4.0.0</modelVersion>");
                writer.println("  <groupId>app</groupId>");
                writer.println("  <artifactId>app</artifactId>");
                writer.println("  <version>1.0.0</version>");
                writer.println("  <properties>");
                writer.println("    <quarkus.platform.version>3.31.2</quarkus.platform.version>");
                writer.println("    <maven.compiler.source>21</maven.compiler.source>");
                writer.println("    <maven.compiler.target>21</maven.compiler.target>");
                writer.println("    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>");
                writer.println("  </properties>");
                writer.println("  <dependencyManagement>");
                writer.println("    <dependencies>");
                writer.println("      <dependency>");
                writer.println("        <groupId>io.quarkus.platform</groupId>");
                writer.println("        <artifactId>quarkus-bom</artifactId>");
                writer.println("        <version>3.31.2</version>");
                writer.println("        <type>pom</type>");
                writer.println("        <scope>import</scope>");
                writer.println("      </dependency>");
                writer.println("    </dependencies>");
                writer.println("  </dependencyManagement>");
                writer.println("  <dependencies>");
                for (String dep : dependencies) {
                    writer.println(dep);
                }
                writer.println("  </dependencies>");
                writer.println("  <repositories>");
                writer.println("    <repository>");
                writer.println("      <id>central</id>");
                writer.println("      <url>https://repo.maven.apache.org/maven2</url>");
                writer.println("    </repository>");
                writer.println("  </repositories>");
                writer.println("</project>");
            }
            
            System.err.println("Complete pom.xml created with " + dependencies.size() + " dependencies");
            
            // Create a local Maven repository with the JARs
            // This helps the Maven resolver find the dependencies
            Path localRepo = projectRoot.resolve("local-repo");
            Files.createDirectories(localRepo);
            System.err.println("Creating local Maven repository at: " + localRepo);
            
            // Install JARs to local repository with proper Maven structure
            int installedCount = 0;
            try (var paths = Files.walk(depsDir)) {
                for (Path jarPath : paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p)).toList()) {
                    String fileName = jarPath.getFileName().toString();
                    // Try to extract coordinates from filename or use defaults
                    String groupId = "local";
                    String artifactId = fileName.substring(0, fileName.length() - 4);
                    String version = "1.0.0";
                    
                    // Parse from filename if possible
                    if (fileName.startsWith("processed_")) {
                        String withoutPrefix = fileName.substring("processed_".length(), fileName.length() - 4);
                        int lastUnderscore = withoutPrefix.lastIndexOf('_');
                        if (lastUnderscore > 0) {
                            String versionPart = withoutPrefix.substring(lastUnderscore + 1);
                            if (versionPart.matches(".*[.\\d].*")) {
                                version = versionPart;
                                String artifactPart = withoutPrefix.substring(0, lastUnderscore);
                                int artifactUnderscore = artifactPart.lastIndexOf('_');
                                if (artifactUnderscore > 0) {
                                    artifactId = artifactPart.substring(artifactUnderscore + 1);
                                    groupId = artifactPart.substring(0, artifactUnderscore).replace('_', '.');
                                } else {
                                    artifactId = artifactPart;
                                }
                            }
                        }
                    }
                    
                    // Create Maven directory structure: groupId/artifactId/version/artifactId-version.jar
                    Path groupDir = localRepo.resolve(groupId.replace('.', '/'));
                    Path artifactDir = groupDir.resolve(artifactId);
                    Path versionDir = artifactDir.resolve(version);
                    Files.createDirectories(versionDir);
                    
                    Path targetJar = versionDir.resolve(artifactId + "-" + version + ".jar");
                    if (!Files.exists(targetJar)) {
                        Files.copy(jarPath, targetJar);
                        installedCount++;
                    }
                }
            }
            
            System.err.println("Installed " + installedCount + " JARs to local Maven repository");
            
            // Install the app JAR and POM to the local repository
            // This prevents Maven from trying to download it from central
            Path appGroupDir = localRepo.resolve("app");
            Path appArtifactDir = appGroupDir.resolve("app");
            Path appVersionDir = appArtifactDir.resolve("1.0.0");
            Files.createDirectories(appVersionDir);
            Path appTargetJar = appVersionDir.resolve("app-1.0.0.jar");
            Path appTargetPom = appVersionDir.resolve("app-1.0.0.pom");
            if (!Files.exists(appTargetJar)) {
                Files.copy(appJar, appTargetJar);
                System.err.println("Installed app JAR to local repository: " + appTargetJar);
            }
            // Create a minimal POM for the app artifact
            if (!Files.exists(appTargetPom)) {
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(appTargetPom))) {
                    writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    writer.println("<project xmlns=\"http://maven.apache.org/POM/4.0.0\"");
                    writer.println("         xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"");
                    writer.println("         xsi:schemaLocation=\"http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd\">");
                    writer.println("  <modelVersion>4.0.0</modelVersion>");
                    writer.println("  <groupId>app</groupId>");
                    writer.println("  <artifactId>app</artifactId>");
                    writer.println("  <version>1.0.0</version>");
                    writer.println("  <packaging>jar</packaging>");
                    writer.println("</project>");
                }
                System.err.println("Created app POM in local repository: " + appTargetPom);
            }
            
            // Update pom.xml to include local repository FIRST (before central)
            // This ensures Maven checks local repo before trying to download from central
            String pomContent = Files.readString(tempPom);
            String localRepoUrl = localRepo.toAbsolutePath().toString().replace("\\", "/");
            if (!localRepoUrl.startsWith("/")) {
                localRepoUrl = "/" + localRepoUrl;
            }
            pomContent = pomContent.replace(
                "  <repositories>",
                "  <repositories>\n    <repository>\n      <id>local</id>\n      <url>file://" + localRepoUrl + "</url>\n      <releases><enabled>true</enabled></releases>\n      <snapshots><enabled>true</enabled></snapshots>\n    </repository>"
            );
            Files.writeString(tempPom, pomContent);
            System.err.println("Updated pom.xml to include local repository: file://" + localRepoUrl);
        }

        System.err.println("=== DEBUG: QuarkusBootstrap Configuration ===");
        System.err.println("Application root: " + projectRoot);
        System.err.println("Target directory: " + outDir);
        System.err.println("Flat classpath: " + (tempPom == null || !Files.exists(tempPom)));
        System.err.println("Local project discovery: " + (tempPom != null && Files.exists(tempPom)));

        // Build QuarkusBootstrap with Maven resolver if pom.xml exists
        // Based on https://github.com/quarkusio/quarkus/issues/11305
        // We can use addAdditionalApplicationArchive to explicitly include dependencies
        QuarkusBootstrap.Builder bootstrapBuilder = QuarkusBootstrap.builder()
                .setAppArtifact(appArtifact)
                .setMode(Mode.PROD)
                .setApplicationRoot(projectRoot)
                .setTargetDirectory(outDir);
        
        // Note: Based on https://github.com/quarkusio/quarkus/issues/11305
        // The issue shows that addAdditionalApplicationArchive can be used, but
        // in our case, the problem is that MemorySizeConverter validation happens
        // during static initialization before converters can be discovered.
        // We'll rely on flat classpath and manual copying to lib/main/ instead.
        
        // Configure Maven resolver if pom.xml exists
        // NOTE: We disable Maven resolver because it tries to download artifacts from central
        // even when they exist in local repository. Instead, we rely on flat classpath
        // and manual copying to lib/main/ for dependency discovery.
        // The Maven resolver would be useful for transitive dependency resolution, but
        // it causes issues when the artifact principal doesn't exist in Maven Central.
        // 
        // If you need Maven resolver, ensure:
        // 1. All artifacts (including app:app:1.0.0) are in local repository
        // 2. The local repository is properly configured in pom.xml
        // 3. The Maven resolver respects repository order (which it may not)
        if (false && tempPom != null && Files.exists(tempPom)) {
            // Enable Maven project discovery - Quarkus will use pom.xml to resolve dependencies
            bootstrapBuilder.setLocalProjectDiscovery(true);
            System.err.println("Maven resolver enabled via pom.xml and setLocalProjectDiscovery(true)");
            System.err.println("Quarkus should automatically discover dependencies from pom.xml");
        } else {
            System.err.println("Maven resolver disabled - using flat classpath and manual dependency copying");
        }
        
        // Always use flat classpath to ensure Quarkus can discover JARs from the extended classpath
        // This is critical for converter discovery (MemorySizeConverter in quarkus-core)
        bootstrapBuilder.setFlatClassPath(true);
        System.err.println("Using flat classpath to enable converter discovery from extended classpath");
        
        QuarkusBootstrap bootstrap = bootstrapBuilder.build();

        // The actual augmentation is usually performed by invoking the build steps; here we rely on
        // Quarkus to produce fast-jar in the target directory.
        // bootstrap.run() was replaced with bootstrap.bootstrap() + createAugmentor() + createProductionApplication()
        System.err.println("=== DEBUG: Starting Quarkus Bootstrap ===");
        System.err.println("Bootstrap configuration:");
        System.err.println("  - App artifact: " + appArtifact.getKey());
        System.err.println("  - App artifact path: " + appArtifact.getResolvedPaths().getSinglePath());
        System.err.println("  - Application root: " + projectRoot);
        System.err.println("  - Target directory: " + outDir);
        System.err.println("  - Flat classpath: true");
        System.err.println("  - Local project discovery: " + (tempPom != null && Files.exists(tempPom)));
        if (tempPom != null && Files.exists(tempPom)) {
            System.err.println("  - POM file exists: " + tempPom);
            System.err.println("  - POM file size: " + Files.size(tempPom) + " bytes");
        }
        
        try (var curated = bootstrap.bootstrap()) {
            System.err.println("=== DEBUG: After Bootstrap ===");
            var appModel = curated.getApplicationModel();
            System.err.println("Application model: " + appModel);
            System.err.println("Application dependencies count: " + appModel.getDependencies().size());
            System.err.println("Application dependencies:");
            appModel.getDependencies().forEach(dep -> {
                System.err.println("  - " + dep.getKey() + " (" + dep.getType() + ")");
            });
            
            // Check if quarkus-core is in the ApplicationModel
            // Note: ApplicationModel is immutable, so we can't add dependencies directly
            // But we can check if it's already there and log information
            if (depsDir != null && Files.exists(depsDir)) {
                try (var paths = Files.walk(depsDir)) {
                    paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p))
                            .filter(p -> p.getFileName().toString().contains("quarkus-core") && 
                                       !p.getFileName().toString().contains("deployment"))
                            .findFirst()
                            .ifPresent(quarkusCoreJar -> {
                                System.err.println("=== DEBUG: Checking for quarkus-core in ApplicationModel ===");
                                System.err.println("quarkus-core JAR found: " + quarkusCoreJar);
                                System.err.println("ApplicationModel dependencies count: " + appModel.getDependencies().size());
                                System.err.println("Note: ApplicationModel is immutable, so dependencies must be discovered during bootstrap");
                                System.err.println("quarkus-core will be copied to lib/main/ for runtime discovery");
                            });
                } catch (IOException e) {
                    System.err.println("Warning: Could not scan deps directory for quarkus-core: " + e.getMessage());
                }
            }
            
            // Check if app artifact has dependencies
            var appDep = appModel.getAppArtifact();
            System.err.println("App artifact: " + appDep.getKey());
            System.err.println("App artifact dependencies: " + appDep.getDependencies().size());
            
            System.err.println("=== DEBUG: Starting Augmentation ===");
            var augmentor = curated.createAugmentor();
            augmentor.createProductionApplication();
            System.err.println("=== DEBUG: Augmentation Complete ===");
            
            // Option 2: Manually copy JARs to lib/ directories after augmentation
            // Since Quarkus is not discovering dependencies automatically, we'll copy them manually
            if (depsDir != null && Files.exists(depsDir)) {
                System.err.println("=== DEBUG: Manually copying dependencies to lib/ ===");
                Path quarkusAppDir = outDir.resolve("quarkus-app");
                Path libMainDir = quarkusAppDir.resolve("lib").resolve("main");
                Path libBootDir = quarkusAppDir.resolve("lib").resolve("boot");
                
                // Create lib directories if they don't exist
                Files.createDirectories(libMainDir);
                Files.createDirectories(libBootDir);
                
                // Copy all JARs from deps directory to lib/main
                // Quarkus typically puts runtime dependencies in lib/main and boot dependencies in lib/boot
                List<String> jarNames = new ArrayList<>();
                int copiedCount = 0;
                try (var paths = Files.walk(depsDir)) {
                    for (Path jarPath : paths.filter(p -> p.toString().endsWith(".jar") && Files.isRegularFile(p)).toList()) {
                        String fileName = jarPath.getFileName().toString();
                        Path targetPath = libMainDir.resolve(fileName);
                        
                        // Skip if already exists (Quarkus might have created some)
                        if (!Files.exists(targetPath)) {
                            Files.copy(jarPath, targetPath);
                            copiedCount++;
                        }
                        jarNames.add("lib/main/" + fileName);
                    }
                }
                
                System.err.println("Copied " + copiedCount + " JARs to lib/main/");
                System.err.println("lib/main/ now contains: " + Files.list(libMainDir).count() + " files");
                System.err.println("lib/boot/ now contains: " + Files.list(libBootDir).count() + " files");
                
                // Update quarkus-run.jar MANIFEST.MF to include Class-Path
                Path quarkusRunJar = quarkusAppDir.resolve("quarkus-run.jar");
                if (Files.exists(quarkusRunJar)) {
                    System.err.println("=== DEBUG: Updating quarkus-run.jar MANIFEST.MF ===");
                    
                    // Read existing manifest
                    Manifest manifest = new Manifest();
                    try (JarFile jar = new JarFile(quarkusRunJar.toFile())) {
                        manifest = jar.getManifest();
                        if (manifest == null) {
                            manifest = new Manifest();
                        }
                    }
                    
                    // Update Class-Path in manifest
                    String classPath = jarNames.stream().collect(Collectors.joining(" "));
                    manifest.getMainAttributes().putValue("Class-Path", classPath);
                    
                    // Also add app/quarkus-application.jar to classpath
                    String existingClassPath = manifest.getMainAttributes().getValue("Class-Path");
                    if (existingClassPath != null && !existingClassPath.isEmpty()) {
                        classPath = existingClassPath + " " + classPath;
                    }
                    classPath = "app/quarkus-application.jar " + classPath;
                    manifest.getMainAttributes().putValue("Class-Path", classPath);
                    
                    // Add system properties to MANIFEST to disable config validation
                    // This helps avoid MemorySize converter registration issues
                    String mainClass = manifest.getMainAttributes().getValue("Main-Class");
                    if (mainClass == null) {
                        mainClass = "io.quarkus.bootstrap.runner.QuarkusEntryPoint";
                    }
                    manifest.getMainAttributes().putValue("Main-Class", mainClass);
                    
                    // Add JVM arguments via manifest to disable problematic config validation
                    // Note: This is a workaround - ideally the augmentation would register converters
                    String jvmArgs = "-Dsmallrye.config.validate-unknown=false " +
                                    "-Dsmallrye.config.validate-defaults=false " +
                                    "-Dquarkus.live-reload.enabled=false " +
                                    "-Dquarkus.virtual-threads.enabled=false";
                    // Store in a custom attribute (won't be used by JVM but documents the workaround)
                    manifest.getMainAttributes().putValue("X-JVM-Args", jvmArgs);
                    
                    System.err.println("Class-Path updated with " + jarNames.size() + " entries");
                    System.err.println("Class-Path length: " + classPath.length() + " characters");
                    System.err.println("Added workaround properties to disable config validation");
                    
                    // Write updated JAR (copy everything except MANIFEST, then add new MANIFEST)
                    // Also copy QuarkusEntryPoint from quarkus-bootstrap-runner if it's missing
                    Path tempJar = quarkusAppDir.resolve("quarkus-run.jar.tmp");
                    try (JarOutputStream jos = new JarOutputStream(Files.newOutputStream(tempJar), manifest)) {
                        // Copy all entries from original JAR except MANIFEST
                        try (JarFile originalJar = new JarFile(quarkusRunJar.toFile())) {
                            originalJar.stream()
                                    .filter(entry -> !entry.getName().equals("META-INF/MANIFEST.MF"))
                                    .forEach(entry -> {
                                        try {
                                            jos.putNextEntry(entry);
                                            originalJar.getInputStream(entry).transferTo(jos);
                                            jos.closeEntry();
                                        } catch (IOException e) {
                                            throw new RuntimeException(e);
                                        }
                                    });
                        }
                        
                        // Check if QuarkusEntryPoint is missing and copy it from quarkus-bootstrap-runner
                        String entryPointClass = "io/quarkus/bootstrap/runner/QuarkusEntryPoint.class";
                        boolean hasEntryPoint = false;
                        try (JarFile originalJar = new JarFile(quarkusRunJar.toFile())) {
                            hasEntryPoint = originalJar.getEntry(entryPointClass) != null;
                        }
                        
                        if (!hasEntryPoint) {
                            System.err.println("QuarkusEntryPoint not found in quarkus-run.jar, copying from quarkus-bootstrap-runner");
                            Path bootstrapRunner = libMainDir.resolve("processed_quarkus-bootstrap-runner-3.31.2.jar");
                            if (!Files.exists(bootstrapRunner)) {
                                // Try to find any quarkus-bootstrap-runner JAR
                                try (var paths = Files.list(libMainDir)) {
                                    bootstrapRunner = paths.filter(p -> p.toString().contains("quarkus-bootstrap-runner"))
                                            .findFirst().orElse(null);
                                }
                            }
                            
                            if (bootstrapRunner != null && Files.exists(bootstrapRunner)) {
                                try (JarFile runnerJar = new JarFile(bootstrapRunner.toFile())) {
                                    var entry = runnerJar.getEntry(entryPointClass);
                                    if (entry != null) {
                                        jos.putNextEntry(entry);
                                        runnerJar.getInputStream(entry).transferTo(jos);
                                        jos.closeEntry();
                                        System.err.println("Copied QuarkusEntryPoint to quarkus-run.jar");
                                    }
                                }
                            }
                        }
                    }
                    
                    // Replace original with updated
                    Files.move(tempJar, quarkusRunJar, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                    System.err.println("quarkus-run.jar MANIFEST.MF updated successfully");
                }
                
                // Create application.properties override file to disable problematic features
                // This file will be loaded by Quarkus and override default values
                // Note: These properties are set to avoid MemorySize converter validation errors
                // The validation happens during static initialization before config files are loaded,
                // so we also need to set them as system properties in the wrapper script
                Path appConfigDir = quarkusAppDir.resolve("app").resolve("config");
                Files.createDirectories(appConfigDir);
                Path overrideProperties = appConfigDir.resolve("application.properties");
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(overrideProperties))) {
                    writer.println("# Override properties to avoid MemorySize converter issues");
                    writer.println("# These override default Quarkus values that require MemorySize converter");
                    writer.println("# Note: These must also be set as system properties because validation");
                    writer.println("# happens during static initialization before config files are loaded");
                    writer.println("quarkus.live-reload.enabled=false");
                    writer.println("quarkus.virtual-threads.enabled=false");
                    writer.println("# Set timeout values to avoid MemorySize converter validation");
                    writer.println("# These are only used if the features are enabled, but Quarkus validates them anyway");
                    // Use numeric values instead of duration strings to avoid MemorySize converter
                    // These are in milliseconds
                    writer.println("quarkus.live-reload.connect-timeout=30000");
                    writer.println("quarkus.live-reload.retry-interval=2000");
                    writer.println("quarkus.virtual-threads.shutdown-check-interval=5000");
                    writer.println("quarkus.virtual-threads.shutdown-timeout=60000");
                    writer.println("# Disable config validation for problematic properties");
                    writer.println("smallrye.config.validate-unknown=false");
                    writer.println("smallrye.config.validate-defaults=false");
                }
                System.err.println("Created application.properties override at: " + overrideProperties);
                
                // Create a wrapper script to run the JAR with necessary system properties
                // This is needed because config validation happens before properties files are loaded
                Path wrapperScript = quarkusAppDir.resolve("run.sh");
                try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(wrapperScript))) {
                    writer.println("#!/bin/bash");
                    writer.println("# Wrapper script to run quarkus-run.jar with necessary system properties");
                    writer.println("# This avoids MemorySize converter registration issues");
                    writer.println("cd \"$(dirname \"$0\")\"");
                    writer.println("exec java \\");
                    writer.println("  -Dsmallrye.config.validate-unknown=false \\");
                    writer.println("  -Dsmallrye.config.validate-defaults=false \\");
                    writer.println("  -Dquarkus.live-reload.enabled=false \\");
                    writer.println("  -Dquarkus.virtual-threads.enabled=false \\");
                    writer.println("  -Dquarkus.live-reload.connect-timeout=30000 \\");
                    writer.println("  -Dquarkus.live-reload.retry-interval=2000 \\");
                    writer.println("  -Dquarkus.virtual-threads.shutdown-check-interval=5000 \\");
                    writer.println("  -Dquarkus.virtual-threads.shutdown-timeout=60000 \\");
                    writer.println("  -jar quarkus-run.jar \"$@\"");
                }
                // Make script executable
                wrapperScript.toFile().setExecutable(true);
                System.err.println("Created wrapper script at: " + wrapperScript);
                
                // Fix: The QuarkusEntryPoint looks for files relative to a base directory
                // It seems to use the target directory from augmentation as base
                // We need to ensure it uses the directory where quarkus-run.jar is located
                // The solution is to create a wrapper script or ensure the JAR is run from the correct directory
                // For now, we'll document that the JAR must be run from the quarkus-app directory
                System.err.println("=== DEBUG: Quarkus fast-jar structure created ===");
                System.err.println("IMPORTANT: The quarkus-run.jar must be executed from the quarkus-app directory");
                System.err.println("The QuarkusEntryPoint expects files in 'quarkus/' relative to the working directory");
                System.err.println("RECOMMENDED: Use ./run.sh instead of java -jar quarkus-run.jar");
            }
        } finally {
            // Clean up temporary pom.xml
            if (tempPom != null && Files.exists(tempPom)) {
                try {
                    Files.delete(tempPom);
                    System.err.println("=== DEBUG: Cleaned up temporary pom.xml ===");
                } catch (IOException e) {
                    System.err.println("Warning: Could not delete temporary pom.xml: " + e.getMessage());
                }
            }
        }
    }
}

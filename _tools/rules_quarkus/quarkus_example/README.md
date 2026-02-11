# Quarkus Example with Bazel

This project demonstrates how to build and run a Quarkus application using Bazel build system. It provides a modern, fast development experience with both Bazel and Maven support.

## Features

- **Quarkus 3.17.6** - Latest LTS version with modern Java features
- **Bazel Build System** - Fast, incremental builds with dependency management
- **REST API** - Built with RESTEasy and Jackson for JSON handling
- **Validation** - Hibernate Validator for input validation
- **Development Mode** - Live coding support with hot reload
- **Testing** - JUnit 5 integration with Quarkus test framework

## Prerequisites

- **Java 21** - Required for Quarkus 3.x
- **Bazel 6.0+** - Build system
- **Maven 3.6+** - For dependency resolution (optional)

## Quick Start

### Build the Application

```bash
# Build the main application
bazel build :hello_quarkus

# Build with development mode
bazel build :hello_quarkus_dev

# Build all targets at once
bazel build :all
```

### Run the Application

```bash
# Run the production build
bazel run :hello_quarkus

# Run in development mode (with live coding)
bazel run :hello_quarkus_dev

# Start development server
bazel run :dev_server

# Start production server
bazel run :prod_server
```

### Run Tests

```bash
# Run all tests
bazel test :hello_quarkus_test

# Run specific test
bazel test :hello_quarkus_test --test_filter=com.example.MainTest

# Run tests as binary (alternative approach)
bazel run :hello_quarkus_test
```

### Clean Build Artifacts

```bash
# Clean Bazel and Maven artifacts
bazel run :clean
```

## Project Structure

```
quarkus_example/
├── BUILD.bazel          # Bazel build configuration
├── pom.xml              # Maven configuration (for IDE support)
├── src/
│   ├── main/
│   │   ├── java/        # Java source code
│   │   └── resources/   # Configuration files
│   └── test/
│       └── java/        # Test source code
├── clean.sh             # Cleanup script
├── dev_server.sh        # Development server script
├── prod_server.sh       # Production server script
└── README.md            # This file
```

## Maven Integration

This project includes a `pom.xml` file for IDE support and Maven compatibility. While the primary build system is Bazel, you can:

- Use Maven-based IDEs (IntelliJ IDEA, Eclipse) with full Quarkus support
- Run Maven commands for development: `mvn quarkus:dev`
- Use Maven for dependency management alongside Bazel

## Available Targets

### Build Targets

- `:hello_quarkus` - Production build (fat JAR)
- `:hello_quarkus_dev` - Development build with live coding
- `:artifact` - Java library with dependencies

### Test Targets

- `:hello_quarkus_test` - Unit and integration tests (runs as binary)

### Server Targets

- `:dev_server` - Start development server with live coding
- `:prod_server` - Start production server

### Utility Targets

- `:clean` - Clean build artifacts
- `:hello_quarkus_distribution` - Distribution package
- `:all` - Build all main targets at once

## Configuration

The application uses the following Quarkus extensions:

- **quarkus-resteasy** - REST API framework
- **quarkus-resteasy-jackson** - JSON serialization
- **quarkus-hibernate-validator** - Input validation
- **quarkus-core** - Core Quarkus functionality

## Testing

The project includes a simple testing framework that runs tests as Java binaries rather than using JUnit. This approach:

- Eliminates external testing framework dependencies
- Provides fast test execution
- Works seamlessly with Bazel's build system
- Allows for custom test logic and assertions

## Development Workflow

1. **Start Development Mode**: `bazel run :hello_quarkus_dev`
2. **Make Changes**: Edit source files
3. **Hot Reload**: Changes are automatically detected and applied
4. **Test Changes**: `bazel test :hello_quarkus_test`
5. **Build Production**: `bazel build :hello_quarkus`

## API Endpoints

Once running, the application provides:

- **Health Check**: `GET /q/health`
- **Main Application**: `GET /` (returns greeting message)

## Troubleshooting

### Common Issues

1. **Java Version**: Ensure you're using Java 21
2. **Bazel Version**: Update to Bazel 6.0+ if you encounter issues
3. **Dependencies**: Run `bazel clean --expunge` if dependency issues occur

### Build Issues

```bash
# Clean and rebuild
bazel clean --expunge
bazel build :hello_quarkus

# Check Bazel version
bazel --version
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

For issues and questions:
- Check the [Quarkus documentation](https://quarkus.io/guides/)
- Review [Bazel documentation](https://bazel.build/docs)
- Open an issue in this repository

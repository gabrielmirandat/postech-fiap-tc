BAZEL = bazel
DOTNET = dotnet

.PHONY: all clean build test unit integration deploy help

help:
	@echo "Available targets:"
	@echo "  all         - Clean, build, test and deploy"
	@echo "  clean       - Clean Bazel outputs and .NET build artifacts"
	@echo "  build       - Build payments module with Bazel and .NET"
	@echo "  test        - Run all tests (unit and integration)"
	@echo "  unit        - Run unit tests only"
	@echo "  integration - Run integration tests only"
	@echo "  deploy      - Build Docker image and push to registry"

all: clean build test deploy

clean:
	@echo "Cleaning Bazel outputs..."
	$(BAZEL) clean --expunge
	@echo "Cleaning .NET build artifacts..."
	$(DOTNET) clean Payments.sln
	@echo "Removing .NET build directories..."
	@find . -type d -name "bin" -exec rm -rf {} + 2>/dev/null || true
	@find . -type d -name "obj" -exec rm -rf {} + 2>/dev/null || true

build:
	@echo "Building with .NET..."
	$(DOTNET) build Payments.sln -c Release
	@echo "Building with Bazel..."
	@if command -v bazel >/dev/null 2>&1; then \
		cd .. && $(BAZEL) build //payments:payments_build; \
	else \
		echo "Bazel not found in PATH, skipping Bazel build"; \
	fi

test: unit integration

unit:
	@echo "Running unit tests with .NET..."
	$(DOTNET) test Payments.sln --verbosity normal
	@echo "Running unit tests with Bazel..."
	@if command -v bazel >/dev/null 2>&1; then \
		cd .. && $(BAZEL) test //payments:unit; \
	else \
		echo "Bazel not found in PATH, skipping Bazel tests"; \
	fi

integration:
	@echo "Running integration tests with .NET..."
	$(DOTNET) test Payments.sln --verbosity normal
	@echo "Running integration tests with Bazel..."
	@if command -v bazel >/dev/null 2>&1; then \
		cd .. && $(BAZEL) test //payments:integration; \
	else \
		echo "Bazel not found in PATH, skipping Bazel tests"; \
	fi

deploy:
	@echo "Building Docker image..."
	@if command -v bazel >/dev/null 2>&1; then \
		cd .. && $(BAZEL) build //payments:image && $(BAZEL) run //payments:push; \
	else \
		echo "Bazel not found in PATH, building Docker image with docker build..."; \
		docker build -t gabrielmirandat/payments:latest .; \
	fi

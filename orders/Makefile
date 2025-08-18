BAZEL = bazel

.PHONY: all clean build test deploy

all: clean build test deploy

clean:
	@echo "Cleaning Bazel outputs..."
	$(BAZEL) clean --expunge

build:
	@echo "Building..."
	$(BAZEL) build //orders:orders_api_files
	$(BAZEL) build //orders:orders_api_jar
	$(BAZEL) build //orders:orders_api
	$(BAZEL) build //orders:artifact
	
test:
	@echo "Running tests..."
	$(BAZEL) build //orders:test_utility
	$(BAZEL) test //orders:unit
	$(BAZEL) test //orders:integration
	$(BAZEL) test //orders:contract
	$(BAZEL) test //orders:behavior

deploy:
	@echo "Deploying..."
	$(BAZEL) build //orders:uber
	$(BAZEL) build //orders:layer
	$(BAZEL) build //orders:image
	$(BAZEL) run //orders:push
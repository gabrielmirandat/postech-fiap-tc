BAZEL = bazel

.PHONY: all clean build test

all: clean build test

clean:
	@echo "Cleaning Bazel outputs..."
	$(BAZEL) clean

build:
	@echo "Building all targets..."
	$(BAZEL) build //core:all

test:
	@echo "Running tests..."
	$(BAZEL) test //core:unit

model:
	@echo "Building model..."
	$(BAZEL) build //core:model_proto
	$(BAZEL) build //core:model_java_proto
	$(BAZEL) build //core:model_java_jar
	$(BAZEL) build //core:model_lib

menu:
	@echo "Building menu..."
	$(BAZEL) build //core:menu_proto
	$(BAZEL) build //core:menu_java_proto
	$(BAZEL) build //core:menu_java_grpc

permissions:
	@echo "Building permissions_proto..."
	$(BAZEL) build //core:permissions_proto
	$(BAZEL) build //core:permissions_java_proto
	$(BAZEL) build //core:permissions_java_grpc
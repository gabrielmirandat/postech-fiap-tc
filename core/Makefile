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

menu_proto:
	@echo "Building menu_proto..."
	$(BAZEL) build //core:menu_proto

menu_java_proto:
	@echo "Building menu_java_proto..."
	$(BAZEL) build //core:menu_java_proto

menu_java_grpc:
	@echo "Building menu_java_grpc..."
	$(BAZEL) build //core:menu_java_grpc

permissions_proto:
	@echo "Building permissions_proto..."
	$(BAZEL) build //core:permissions_proto

permissions_java_proto:
	@echo "Building permissions_java_proto..."
	$(BAZEL) build //core:permissions_java_proto

permissions_java_grpc:
	@echo "Building permissions_java_grpc..."
	$(BAZEL) build //core:permissions_java_grpc

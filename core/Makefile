BAZEL = bazel

.PHONY: all clean build test

all: clean build test

clean:
	@echo "Cleaning Bazel outputs..."
	$(BAZEL) clean --expunge

build: model menu permissions
	@echo "Building all targets..."
	

test:
	@echo "Running tests..."
	$(BAZEL) test //core/src/test/java:unit

model:
	@echo "Building model..."
	$(BAZEL) build //core/src/main/proto:model_proto
	$(BAZEL) build //core/src/main/java:model_java_proto
	$(BAZEL) build //core/src/main/java:model_java_jar
	$(BAZEL) build //core/src/main/java:model_lib

menu:
	@echo "Building menu..."
	$(BAZEL) build //core/src/main/proto:menu_proto
	$(BAZEL) build //core/src/main/java:menu_java_proto
	$(BAZEL) build //core/src/main/java:menu_java_grpc

permissions:
	@echo "Building permissions_proto..."
	$(BAZEL) build //core/src/main/proto:permissions_proto
	$(BAZEL) build //core/src/main/java:permissions_java_proto
	$(BAZEL) build //core/src/main/java:permissions_java_grpc
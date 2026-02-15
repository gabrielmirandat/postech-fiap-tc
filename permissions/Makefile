BAZEL = bazel

.PHONY: all clean build test deploy help
.PHONY: artifact uber unit integration layer image push

all: clean build test deploy

clean:
	@echo "Cleaning Bazel outputs..."
	$(BAZEL) clean --expunge

build:
	@echo "Building..."
	$(BAZEL) build //permissions:artifact
	$(BAZEL) build //permissions:uber

test:
	@echo "Running tests..."
	$(BAZEL) test //permissions:unit
	$(BAZEL) test //permissions:integration

deploy:
	@echo "Deploying..."
	$(BAZEL) build //permissions:uber
	$(BAZEL) build //permissions:layer
	$(BAZEL) build //permissions:image
	$(BAZEL) run //permissions:push

# --- Run individual rules (same names as orders for pipeline) ---
artifact:
	$(BAZEL) build //permissions:artifact

uber:
	$(BAZEL) build //permissions:uber

unit:
	$(BAZEL) test //permissions:unit

integration:
	$(BAZEL) test //permissions:integration

layer:
	$(BAZEL) build //permissions:layer

image:
	$(BAZEL) build //permissions:image

push:
	$(BAZEL) run //permissions:push

help:
	@echo "permissions - run each rule (same names as orders):"
	@echo "  make artifact     - build //permissions:artifact"
	@echo "  make uber         - build //permissions:uber"
	@echo "  make unit         - test //permissions:unit"
	@echo "  make integration  - test //permissions:integration"
	@echo "  make layer        - build //permissions:layer"
	@echo "  make image        - build //permissions:image"
	@echo "  make push         - run //permissions:push"
	@echo ""
	@echo "Combined:"
	@echo "  make build   - artifact + uber"
	@echo "  make test    - unit + integration"
	@echo "  make deploy  - uber + layer + image + push"

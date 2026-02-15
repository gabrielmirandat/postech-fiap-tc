# Menu (Go) - Bazel hermetic build, same target names as permissions/orders
BAZEL = bazel

.PHONY: all clean build test deploy help
.PHONY: artifact uber unit integration layer image push

all: clean build test deploy

clean:
	@echo "Cleaning Bazel outputs..."
	$(BAZEL) clean --expunge

build:
	@echo "Building..."
	$(BAZEL) build //menu:artifact
	$(BAZEL) build //menu:uber

test:
	@echo "Running tests..."
	$(BAZEL) test //menu:unit
	$(BAZEL) test //menu:integration

deploy:
	@echo "Deploying..."
	$(BAZEL) build //menu:uber
	$(BAZEL) build //menu:layer
	$(BAZEL) build //menu:image
	$(BAZEL) run //menu:push

# --- Run individual rules (same names as orders/permissions for pipeline) ---
artifact:
	$(BAZEL) build //menu:artifact

uber:
	$(BAZEL) build //menu:uber

unit:
	$(BAZEL) test //menu:unit

integration:
	$(BAZEL) test //menu:integration

layer:
	$(BAZEL) build //menu:layer

image:
	$(BAZEL) build //menu:image

push:
	$(BAZEL) run //menu:push

# --- Go (non-Bazel) ---
go-build:
	go build -o menu ./cmd/server

go-run:
	go run ./cmd/server

go-test:
	go test ./...

help:
	@echo "menu (Go) - run each Bazel rule (same names as orders/permissions):"
	@echo "  make artifact     - build //menu:artifact"
	@echo "  make uber         - build //menu:uber"
	@echo "  make unit         - test //menu:unit"
	@echo "  make integration  - test //menu:integration"
	@echo "  make layer        - build //menu:layer"
	@echo "  make image        - build //menu:image"
	@echo "  make push         - run //menu:push"
	@echo ""
	@echo "Combined:"
	@echo "  make build   - artifact + uber"
	@echo "  make test    - unit + integration"
	@echo "  make deploy  - uber + layer + image + push"
	@echo ""
	@echo "Go (without Bazel):"
	@echo "  make go-build, go-run, go-test"

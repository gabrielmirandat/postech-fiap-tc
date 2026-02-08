BAZEL = bazel

.PHONY: all clean build build-all build-ubers run-all run-permissions run-orders run-menu run-customers run-payments run-notifications help

# Default target
all: build-all

help:
	@echo "=== Global Makefile - Postech FIAP TC ==="
	@echo ""
	@echo "Build targets:"
	@echo "  make build-all      - Build all projects (artifacts)"
	@echo "  make build-ubers    - Build all executables (ubers)"
	@echo ""
	@echo "Run targets (local execution - ordered by port):"
	@echo "  make run-permissions - Run permissions service (Spring Boot) on port 8000"
	@echo "  make run-orders      - Run orders service (Spring Boot) on port 8001"
	@echo "  make run-menu        - Run menu service (Quarkus) on port 8002"
	@echo "  make run-customers   - Run customers service (FastAPI) on port 8003"
	@echo "  make run-payments    - Run payments service (.NET) on port 8004"
	@echo "  make run-notifications - Run notifications service (Rails) on port 8005"
	@echo "  make run-all        - Run all services (in background)"
	@echo ""
	@echo "Utility targets:"
	@echo "  make clean          - Clean all Bazel outputs"
	@echo "  make help           - Show this help message"

# Clean all Bazel outputs
clean:
	@echo "Cleaning all Bazel outputs..."
	$(BAZEL) clean --expunge

# Build all projects (artifacts)
build-all:
	@echo "Building all projects (artifacts)..."
	$(BAZEL) build //:global_artifact

# Build all executables (ubers)
build-ubers:
	@echo "Building all executables (ubers)..."
	$(BAZEL) build //:global_uber

# Run permissions service (Spring Boot) - Port 8000
run-permissions: build-ubers
	@echo "Running permissions service (Spring Boot) on port 8000..."
	$(BAZEL) run //permissions:uber -- --server.port=8000

# Run orders service (Spring Boot) - Port 8001
run-orders: build-ubers
	@echo "Running orders service (Spring Boot) on port 8001..."
	$(BAZEL) run //orders:uber -- --server.port=8001

# Run menu service (Quarkus) - Port 8002
run-menu: build-ubers
	@echo "Running menu service (Quarkus) on port 8002..."
	$(BAZEL) run //menu:uber -- --quarkus.http.port=8002

# Run customers service (FastAPI) - Port 8003
# Note: FastAPI/uvicorn accepts host and port via environment or command line
run-customers: build-ubers
	@echo "Running customers service (FastAPI) on port 8003..."
	@echo "Note: The service will run on port 8003 as configured in main.py"
	$(BAZEL) run //customers:uber

# Run payments service (.NET) - Port 8004
# Note: .NET service runs via Docker, so we'll use docker-compose or show instructions
run-payments: build-ubers
	@echo "Running payments service (.NET) on port 8004..."
	@echo "Note: Payments service requires Docker. Use: docker-compose up payments-service"
	@echo "Or build and run the image manually:"
	@echo "  bazel run //payments:uber"

# Run notifications service (Rails) - Port 8005
# Note: Rails service runs via Docker, so we'll use docker-compose or show instructions
run-notifications: build-ubers
	@echo "Running notifications service (Rails) on port 8005..."
	@echo "Note: Notifications service requires Docker. Use: docker-compose up notifications-service"
	@echo "Or build and run the image manually:"
	@echo "  bazel run //notifications:uber"

# Run all services (in background - use with caution)
# Services are started in order of port numbers: 8000, 8001, 8002, 8003, 8004, 8005
run-all:
	@echo "Running all services in background..."
	@echo "Warning: This will start multiple services. Make sure ports are available."
	@echo "Starting permissions service (port 8000)..."
	@$(BAZEL) run //permissions:uber -- --server.port=8000 &
	@sleep 2
	@echo "Starting orders service (port 8001)..."
	@$(BAZEL) run //orders:uber -- --server.port=8001 &
	@sleep 2
	@echo "Starting menu service (port 8002)..."
	@$(BAZEL) run //menu:uber -- --quarkus.http.port=8002 &
	@sleep 2
	@echo "Starting customers service (port 8003)..."
	@$(BAZEL) run //customers:uber &
	@sleep 2
	@echo "Starting payments service (port 8004)..."
	@echo "Note: Payments requires Docker - skipping for now"
	@sleep 2
	@echo "Starting notifications service (port 8005)..."
	@echo "Note: Notifications requires Docker - skipping for now"
	@echo "All services started. Use 'pkill -f bazel' to stop them."

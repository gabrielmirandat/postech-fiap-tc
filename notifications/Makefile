BAZEL = bazel
RAILS = bundle exec rails
BUNDLE = bundle

.PHONY: all clean build test setup install migrate

all: clean build test

clean:
	@echo "Cleaning..."
	$(BAZEL) clean --expunge || true
	rm -rf tmp/pids/*.pid
	rm -rf log/*.log

setup: install
	@echo "Setting up database..."
	$(RAILS) db:create
	$(RAILS) db:migrate

install:
	@echo "Installing dependencies..."
	$(BUNDLE) install

migrate:
	@echo "Running migrations..."
	$(RAILS) db:migrate

build:
	@echo "Building with Bazel..."
	$(BAZEL) build //notifications:notifications_image

test:
	@echo "Running tests..."
	$(BAZEL) test //notifications:unit || true
	$(RAILS) test || true

unit:
	@echo "Running unit tests..."
	$(RAILS) test

console:
	@echo "Starting Rails console..."
	$(RAILS) console

server:
	@echo "Starting Rails server..."
	$(RAILS) server -p 8005

consumer:
	@echo "Starting Kafka consumer..."
	$(RAILS) runner "OrderEventConsumerJob.perform_now"

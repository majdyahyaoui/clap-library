.PHONY: build run run-detached stop clean shell rebuild

IMAGE_NAME := clap-library
IMAGE_TAG := 1.0.0
CONTAINER_NAME := clap
PORT := 8080

build:
	@echo "Building Docker image..."
	docker build -t $(IMAGE_NAME):$(IMAGE_TAG) .
	@echo "Image built successfully: $(IMAGE_NAME):$(IMAGE_TAG)"

run:
	@echo "Starting container..."
	docker run --rm -p $(PORT):$(PORT) --name $(CONTAINER_NAME) $(IMAGE_NAME):$(IMAGE_TAG)

run-detached:
	@echo "Starting container in background..."
	docker run -d -p $(PORT):$(PORT) --name $(CONTAINER_NAME) $(IMAGE_NAME):$(IMAGE_TAG)
	@echo "Container started: $(CONTAINER_NAME)"
	@echo "API available at: http://localhost:$(PORT)/api/authors"

stop:
	@echo "Stopping container..."
	@docker stop $(CONTAINER_NAME) 2>/dev/null || echo "Container not running"
	@docker rm $(CONTAINER_NAME) 2>/dev/null || echo "Container not found"
	@echo "Container stopped"

clean: stop
	@echo "Cleaning up..."
	@docker rmi $(IMAGE_NAME):$(IMAGE_TAG) 2>/dev/null || echo "Image not found"
	@echo "Cleanup complete"

shell:
	@echo "Accessing container shell..."
	docker exec -it $(CONTAINER_NAME) /bin/sh

rebuild: clean build run-detached
	@echo "Rebuild complete!"

.DEFAULT_GOAL := help

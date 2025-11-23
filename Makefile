.PHONY: help postgres-up postgres-down postgres-logs

IMAGE_NAME := clap-library
IMAGE_TAG := 1.0.0
CONTAINER_NAME := clap
PORT := 8080

help:
	@echo "Docker Commands (PostgreSQL):"
	@echo "  make postgres-up        Start PostgreSQL + App with docker-compose"
	@echo "  make postgres-down      Stop PostgreSQL and App"
	@echo "  make postgres-logs      Show docker-compose logs"
	@echo ""

postgres-up:
	@echo "Starting PostgreSQL with Docker Compose..."
	docker compose up -d
	@echo "PostgreSQL started on port 5433"
	@echo "App running on http://localhost:$(PORT)/api/authors"

postgres-down:
	@echo "Stopping PostgreSQL and App..."
	docker compose down
	@echo "Stopped"

postgres-logs:
	@echo "PostgreSQL logs:"
	docker compose logs -f

.DEFAULT_GOAL := help

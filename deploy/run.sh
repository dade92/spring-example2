#!/bin/bash

echo "Pulling the latest images..."
docker compose pull app

echo "Starting database..."
docker compose up -d mongo
sleep 2

echo "Starting app..."
docker compose up -d app

docker ps

echo "App seems up and running. Enjoy!"



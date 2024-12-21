#!/bin/bash

cd local-env
docker compose up -d
echo "Waiting for everything up..."
sleep 2
docker ps
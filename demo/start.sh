#!/bin/bash
set -e

echo "Building cliente..."
cd cliente
./mvnw -q clean package -DskipTests
cd ..

echo "Building cuenta..."
cd cuenta
./mvnw -q clean package -DskipTests
cd ..

echo "Starting containers..."
docker-compose up -d
echo "Done. Cliente: http://localhost:8080  Cuenta: http://localhost:8081"

#!/bin/bash

echo "🚀 Starting Saga Pattern Microservices Demo"
echo "=========================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker first."
    exit 1
fi

# Start Kafka and Zookeeper
echo "📡 Starting Kafka infrastructure..."
docker-compose up -d

# Wait for Kafka to be ready
echo "⏳ Waiting for Kafka to be ready..."
sleep 30

# Build all modules
echo "🔨 Building all microservices..."
mvn clean install -DskipTests

# Start all services in separate terminals (macOS)
echo "🚀 Starting microservices..."

osascript -e 'tell app "Terminal" to do script "cd '$(pwd)'/order-service && mvn spring-boot:run"'
sleep 5

osascript -e 'tell app "Terminal" to do script "cd '$(pwd)'/payment-service && mvn spring-boot:run"'
sleep 5

osascript -e 'tell app "Terminal" to do script "cd '$(pwd)'/inventory-service && mvn spring-boot:run"'
sleep 5

osascript -e 'tell app "Terminal" to do script "cd '$(pwd)'/saga-orchestrator && mvn spring-boot:run"'
sleep 5

osascript -e 'tell app "Terminal" to do script "cd '$(pwd)'/notification-service && mvn spring-boot:run"'

echo "✅ All services are starting up!"
echo "🌐 Kafka UI available at: http://localhost:8080"
echo "📊 H2 Consoles available at:"
echo "   - Order Service: http://localhost:8081/h2-console"
echo "   - Payment Service: http://localhost:8082/h2-console"
echo "   - Inventory Service: http://localhost:8083/h2-console"
echo "   - Saga Orchestrator: http://localhost:8084/h2-console"
echo ""
echo "🎯 To test the saga, run: ./test-saga.sh"

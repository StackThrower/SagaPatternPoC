# Saga Pattern Microservices Implementation

This project demonstrates a complete implementation of the **Saga Pattern** for managing distributed transactions across multiple microservices using Java 25, Spring Boot, and Apache Kafka.

## Architecture Overview

The implementation consists of 6 main components:

### Microservices:
1. **Order Service** (Port 8081) - Manages order creation and lifecycle
2. **Payment Service** (Port 8082) - Handles payment processing with 90% success simulation
3. **Inventory Service** (Port 8083) - Manages product inventory and reservations
4. **Saga Orchestrator** (Port 8084) - Coordinates the distributed transaction workflow
5. **Notification Service** (Port 8085) - Handles notifications and compensation alerts

### Infrastructure:
6. **Apache Kafka** - Event-driven messaging between services

## Saga Pattern Implementation

This implementation uses the **Orchestration-based Saga Pattern** where the Saga Orchestrator coordinates the entire transaction flow:

### Happy Path Flow:
1. **Order Created** → Order Service publishes `OrderCreatedEvent`
2. **Payment Processing** → Payment Service processes payment and publishes `PaymentProcessedEvent`
3. **Inventory Reservation** → Inventory Service reserves items and publishes `InventoryReservedEvent`
4. **Completion** → Saga Orchestrator marks transaction as completed

### Compensation Flow:
- If payment fails → Order is cancelled
- If inventory reservation fails → Payment is refunded and order is cancelled

## Key Features

✅ **Event-Driven Architecture** - All services communicate via Kafka events
✅ **Distributed Transaction Management** - Saga pattern ensures data consistency
✅ **Automatic Compensation** - Failed transactions trigger compensating actions
✅ **Fault Tolerance** - Services can handle failures gracefully
✅ **Monitoring** - Complete saga lifecycle tracking
✅ **Scalability** - Each service can be scaled independently

## Prerequisites

- Java 25
- Maven 3.6+
- Docker and Docker Compose
- curl and jq (for testing)

## Quick Start

### 1. Clone and Build
```bash
git clone <your-repo>
cd SagaPatternPoC
mvn clean install
```

### 2. Start Infrastructure
```bash
docker-compose up -d
```

### 3. Start All Services
```bash
./start-services.sh
```

### 4. Test the Saga Pattern
```bash
./test-saga.sh
```

## Service Endpoints

| Service | Port | H2 Console | Purpose |
|---------|------|------------|---------|
| Order Service | 8081 | http://localhost:8081/h2-console | Order management |
| Payment Service | 8082 | http://localhost:8082/h2-console | Payment processing |
| Inventory Service | 8083 | http://localhost:8083/h2-console | Inventory management |
| Saga Orchestrator | 8084 | http://localhost:8084/h2-console | Saga coordination |
| Notification Service | 8085 | N/A | Notifications |
| Kafka UI | 8080 | http://localhost:8080 | Message monitoring |

## Manual Testing

### Create an Order
```bash
curl -X POST http://localhost:8081/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-001",
    "productId": "PRODUCT-1",
    "quantity": 2,
    "amount": 100.00
  }'
```

### Check Order Status
```bash
curl http://localhost:8081/api/orders/{orderId}
```

## Monitoring

### Kafka Topics
- `order-events` - Order lifecycle events
- `payment-events` - Payment processing events
- `inventory-events` - Inventory management events
- `notification-events` - Success notifications
- `compensation-events` - Failure notifications

### Database Access
Each service has its own H2 in-memory database accessible via the H2 console:
- Username: `sa`
- Password: (empty)
- JDBC URL: `jdbc:h2:mem:{service}db`

## Test Scenarios

The test script (`test-saga.sh`) includes:

1. **Successful Transaction** - Normal flow completion
2. **Payment Failure** - 10% chance of payment failure with compensation
3. **Inventory Shortage** - Large order exceeding available inventory

## Project Structure

```
SagaPatternPoC/
├── common/                 # Shared events, commands, DTOs
├── order-service/         # Order management service
├── payment-service/       # Payment processing service
├── inventory-service/     # Inventory management service
├── saga-orchestrator/     # Saga coordination service
├── notification-service/  # Notification service
├── docker-compose.yml     # Kafka infrastructure
├── start-services.sh      # Service startup script
└── test-saga.sh          # Testing script
```

## Technology Stack

- **Java 25** - Latest Java version with virtual threads support
- **Spring Boot 3.2** - Application framework
- **Spring Kafka** - Kafka integration
- **Spring Data JPA** - Database access
- **H2 Database** - In-memory database for demo
- **Apache Kafka** - Event streaming platform
- **Maven** - Build tool

## Benefits of This Implementation

1. **Consistency** - Maintains data consistency across distributed services
2. **Resilience** - Automatic compensation for failed transactions
3. **Observability** - Complete transaction tracking and monitoring
4. **Scalability** - Independent service scaling
5. **Maintainability** - Clear separation of concerns

## Future Enhancements

- Add circuit breakers for service resilience
- Implement saga timeout handling
- Add comprehensive logging and monitoring
- Integrate with distributed tracing (Zipkin/Jaeger)
- Add security and authentication
- Implement saga state persistence for recovery

## Troubleshooting

### Common Issues:
1. **Port conflicts** - Ensure ports 8080-8085, 2181, 9092 are available
2. **Kafka not ready** - Wait 30 seconds after `docker-compose up`
3. **Build failures** - Ensure Java 25 and Maven are properly installed

### Logs Location:
Check console outputs of each service for saga execution flow and any errors.

---

**Note**: This is a proof-of-concept implementation for learning purposes. For production use, consider additional patterns like circuit breakers, proper error handling, persistence, and monitoring.

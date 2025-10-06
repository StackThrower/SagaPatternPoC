#!/bin/bash

echo "🧪 Testing Saga Pattern Implementation"
echo "====================================="

BASE_URL="http://localhost:8081"

# Test 1: Successful Order
echo "📝 Test 1: Creating a successful order..."
curl -X POST $BASE_URL/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-001",
    "productId": "PRODUCT-1",
    "quantity": 2,
    "amount": 100.00
  }' | jq '.'

echo ""
echo "⏳ Waiting for saga to complete..."
sleep 10

# Test 2: Order that might fail due to payment (10% chance)
echo "📝 Test 2: Creating another order (might fail due to payment)..."
curl -X POST $BASE_URL/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-002",
    "productId": "PRODUCT-1",
    "quantity": 1,
    "amount": 50.00
  }' | jq '.'

echo ""
echo "⏳ Waiting for saga to complete..."
sleep 10

# Test 3: Large order that should fail due to inventory
echo "📝 Test 3: Creating a large order (should fail due to inventory)..."
curl -X POST $BASE_URL/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": "CUST-003",
    "productId": "PRODUCT-1",
    "quantity": 150,
    "amount": 1500.00
  }' | jq '.'

echo ""
echo "✅ Tests completed! Check the console outputs of all services to see the saga execution flow."
echo "🌐 You can also check Kafka UI at http://localhost:8080 to see the events."

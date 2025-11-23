# RTOMS (Real Time Order Management System)

---

## Overview

RTOMS provides:
- Managing real-time orders in a food delivery chain as a robust system that can
handle high throughput, low latency, and fault tolerance.
- Task was to build a backend system to manage the lifecycle of an order from placement to delivery

---

##  Entity Model Summary

### **Customer**
- Identifies the user placing orders  
- Linked to orders (Many-to-One)

### **Restaurant**
- Owns products and receives orders  
- Used for restaurant-wise filtering and validation

### **Product**
- Belongs to a specific restaurant  
- Contains pricing information  
- Connected to inventory records

### **Inventory**
- Tracks product stock  
- Validated and updated during ordering/cancellation

### **Order**
- Created when a customer places an order  
- Linked to customer, restaurant, and order items  
- Tracks status (PLACED, DISPATCHED, DELIVERED, CANCELLED)

### **OrderItem**
- Represents each product in an order  
- Stores quantity and per-item price

---

##  DTOs

### **OrderRequest**
- `customerId`
- `restaurantId`
- `items[]` list of item objects

### **OrderItemRequest**
- `productId`
- `quantity`

These DTOs are used for request bodies in API calls.

---

# Entity Structure

Below is the complete list of entities and fields:

---

## ** Customer**
| Field | Type | Notes |
|------|------|-------|
| customerId | UUID | Auto-generated |
| name | String | |
| email | String | |
| phone | String | Unique, required |

---

## ** Restaurant**
| Field | Type |
|------|------|
| restaurantId | UUID |
| name | String |
| address | String |

---

## ** Product**
| Field | Type |
|------|------|
| productId | UUID |
| restaurant | FK → Restaurant |
| name | String |
| price | double |
| createdAt | LocalDateTime |

---

## ** Inventory**
| Field | Type |
|------|--------|
| id | Long (PK) |
| product | FK → Product (One-to-One) |
| quantity | int |
| threshold | int |

---

## ** Order**
| Field | Type |
|------|--------|
| orderId | UUID |
| customer | FK → Customer |
| restaurant | FK → Restaurant |
| status | String (PLACED, PREPARING, OUT_FOR_DELIVERY, DELIVERED, CANCELLED) |
| totalAmount | double |
| createdAt | LocalDateTime |

---

## ** OrderItem**
| Field | Type |
|------|--------|
| id | Long |
| order | FK → Order |
| product | FK → Product |
| quantity | int |
| itemPrice | double |

---


##  API Endpoints

### **1. Place Order**
**POST `/order/place`**  
Creates an order after validating:
- Customer existence  
- Restaurant existence  
- Product belongs to restaurant  
- Sufficient inventory for each product  
- Deducts inventory accordingly  

#### **ExampleURL**
```json
  localhost:8080/order/place
```

#### **RequestStructure**
```json
{
  "customerId": "7e1f9b4e-8c0b-4d78-b956-683e9be51d1c",
  "restaurantId": "b8c1c44c-dfca-4340-b0b1-2ac7b219e462",
  "items": [
    {
      "productId": "4323bcbf-5d3c-43ed-891e-38c2ef704197",
      "quantity": 5
    },
    {
      "productId": "94e4b304-5d1a-44b4-80b4-af8b387a777a",
      "quantity": 5
    }
  ]
}
```

#### **ResponseStructure**
```json
{
    "orderId": "d50955ee-84de-4978-aa1a-8c3a21189795",
    "customer": {
        "customerId": "7e1f9b4e-8c0b-4d78-b956-683e9be51d1c",
        "name": "Abhishek Gupta",
        "email": "abhishek@example.com",
        "phone": "9876543210"
    },
    "restaurant": {
        "restaurantId": "b8c1c44c-dfca-4340-b0b1-2ac7b219e462",
        "name": "Tasty Bites",
        "address": "Koramangala, Bengaluru"
    },
    "status": "PLACED",
    "totalAmount": 1000.0,
    "createdAt": "2025-11-22T22:15:31.2909979"
}
```
---

### **2. Get All Orders**
**GET `/order/all`**  
Returns list of all orders in the system.

#### **ExampleURL**
```json
  localhost:8080/order/all
```

#### **ResponseStructure**
```json
[
    {
        "orderId": "d50955ee-84de-4978-aa1a-8c3a21189795",
        "customer": {
            "customerId": "7e1f9b4e-8c0b-4d78-b956-683e9be51d1c",
            "name": "Abhishek Gupta",
            "email": "abhishek@example.com",
            "phone": "9876543210"
        },
        "restaurant": {
            "restaurantId": "b8c1c44c-dfca-4340-b0b1-2ac7b219e462",
            "name": "Tasty Bites",
            "address": "Koramangala, Bengaluru"
        },
        "status": "CANCELLED",
        "totalAmount": 1000.0,
        "createdAt": "2025-11-22T22:15:31.290998"
    }
]
```
---

### **3. Get Orders by Customer**
**GET `/order/{customerId}`**  
Returns all orders placed by a specific customer.

#### **ExampleURL**
```json
    localhost:8080/order/7e1f9b4e-8c0b-4d78-b956-683e9be51d1c
```

#### **ResponseStructure**
```json
[
    {
        "orderId": "d50955ee-84de-4978-aa1a-8c3a21189795",
        "customer": {
            "customerId": "7e1f9b4e-8c0b-4d78-b956-683e9be51d1c",
            "name": "Abhishek Gupta",
            "email": "abhishek@example.com",
            "phone": "9876543210"
        },
        "restaurant": {
            "restaurantId": "b8c1c44c-dfca-4340-b0b1-2ac7b219e462",
            "name": "Tasty Bites",
            "address": "Koramangala, Bengaluru"
        },
        "status": "CANCELLED",
        "totalAmount": 1000.0,
        "createdAt": "2025-11-22T22:15:31.290998"
    }
]
```
---

### **4. Update Order Status (Restaurant Only)**
**PUT `/order/{orderId}/status?restaurantId=XXX&status=STATUS_TEXT`**  
Restaurant can update order status.  
Examples: `PREPARING`, `OUT_FOR_DELIVERY`, `DELIVERED`

#### **ExampleURL**
```json
    localhost:8080/order/d50955ee-84de-4978-aa1a-8c3a21189795/status?restaurantId=b8c1c44c-dfca-4340-b0b1-2ac7b219e462&status=PREPARING
```

#### **ResponseStructure**
```json
{
    "orderId": "d50955ee-84de-4978-aa1a-8c3a21189795",
    "customer": {
        "customerId": "7e1f9b4e-8c0b-4d78-b956-683e9be51d1c",
        "name": "Abhishek Gupta",
        "email": "abhishek@example.com",
        "phone": "9876543210"
    },
    "restaurant": {
        "restaurantId": "b8c1c44c-dfca-4340-b0b1-2ac7b219e462",
        "name": "Tasty Bites",
        "address": "Koramangala, Bengaluru"
    },
    "status": "PREPARING",
    "totalAmount": 1000.0,
    "createdAt": "2025-11-22T22:15:31.290998"
}
```
---

### **5. Cancel Order by Customer**
**DELETE `/order/{orderId}/cancel?customerId=XXX`**  
- Only the customer who placed the order can cancel it  
- Delivered orders cannot be cancelled  
- Inventory is restored for all order items  

#### **ExampleURL**
```json
    localhost:8080/order/d50955ee-84de-4978-aa1a-8c3a21189795/cancel?customerId=7e1f9b4e-8c0b-4d78-b956-683e9be51d1c
```

#### **ResponseStructure**
```json
{
    "orderId": "d50955ee-84de-4978-aa1a-8c3a21189795",
    "customer": {
        "customerId": "7e1f9b4e-8c0b-4d78-b956-683e9be51d1c",
        "name": "Abhishek Gupta",
        "email": "abhishek@example.com",
        "phone": "9876543210"
    },
    "restaurant": {
        "restaurantId": "b8c1c44c-dfca-4340-b0b1-2ac7b219e462",
        "name": "Tasty Bites",
        "address": "Koramangala, Bengaluru"
    },
    "status": "CANCELLED",
    "totalAmount": 1000.0,
    "createdAt": "2025-11-22T22:15:31.290998"
}
```
---

##  Business Rules

- Products can only be ordered from their associated restaurant  
- Inventory must be available; otherwise the order fails  
- Cancelling a delivered order is not allowed  
- Cancelling restores inventory  
- Only the restaurant owning the order can update its status  
- Only the customer who owns an order can cancel it  

---

##  Error Handling

Handled globally using:
- Custom exceptions classes  
- `@RestControllerAdvice`  
- Structured JSON error responses  

---



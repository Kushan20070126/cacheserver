# Hybrid Cache Server (AVL Tree + LRU)

A high-performance in-memory cache server built using Java and Spring Boot.  
This project combines multiple data structures to achieve fast access, efficient memory usage, and ordered data retrieval.

---

## Overview

This system works similar to a mini Redis server.  
It stores data temporarily and provides fast access to frequently used data.

It acts as a layer between applications and databases to reduce latency and improve performance.

---

## Key Features

- Fast key-value storage
- TTL (Time-To-Live) support
- LRU eviction policy
- Range queries using AVL Tree
- Memory limit management
- Real-time statistics tracking
- CLI interface
- REST API
- Web monitoring dashboard

---

## Architecture

The system uses a hybrid data structure design:

- HashMap → O(1) access for GET, SET, DELETE  
- AVL Tree → sorted keys and range queries  
- Doubly Linked List → LRU eviction  

---

## Core Operations

SET <key> <value> <ttlSeconds>


Example:


SET 10 apple 60


---

### GET

Retrieve value using key.


GET <key>


---

### DELETE

Remove a key manually.


DELETE <key>


---

### RANGE

Get all keys within a range.


RANGE <startKey> <endKey>


---

### STATS

View cache statistics.


STATS


---

## TTL Management

Each item has an expiration time.  
Expired items are automatically removed by a background cleanup process.

---

## LRU Eviction

When memory limit is reached:

- least recently used item is removed  
- ensures efficient memory usage  

---

## REST API

| Method | Endpoint            | Description         |
|--------|--------------------|---------------------|
| POST   | /cache             | Insert data         |
| GET    | /cache/{key}       | Get value           |
| DELETE | /cache/{key}       | Delete key          |
| GET    | /cache/range       | Range query         |
| GET    | /cache/stats       | Get statistics      |

---

## Web Dashboard

The system includes a real-time monitoring UI.

Features:

- Live graph for items and memory
- Memory usage bar
- Cache full warning
- Eviction spikes
- Logs panel

Access:


http://localhost:8080/stats.html


---

## Technologies Used

- Java
- Spring Boot
- Maven
- HTML, CSS, JavaScript
- Chart.js
- Embedded Tomcat

---

## Algorithms Used

- Hashing for O(1) access
- AVL Tree for sorted storage and range queries
- LRU eviction using Doubly Linked List
- TTL expiration handling
- Memory management strategy

---

## Performance

- GET → O(1)  
- SET → O(log n)  
- DELETE → O(log n)  
- RANGE → O(log n + k)  

---

## How to Run

### 1. Build project


mvn clean package


### 2. Run application


java -jar target/cacheserver-0.0.1-SNAPSHOT.jar


### 3. Open dashboard


http://localhost:8080/stats.html


---

## CLI Usage

Run commands:


SET 1 apple 60
GET 1
DELETE 1
RANGE 1 100
STATS


---

## Example


SET 1 apple 60
SET 2 banana 60
GET 1
RANGE 1 10


---

## Future Improvements

- WebSocket real-time updates
- Distributed caching
- Persistence support
- Authentication layer
- Advanced eviction policies

---

## Authors

- Kushan Randika Herath  
- Janidu  

---

## License

This project is for educational purposes.


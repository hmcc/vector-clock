# vector-clock
Implementation of the vector clock algorithm in Java.

### Build

This repository depends on hello-distributed-systems.

1. Check out hello-distributed-systems and `make install`
2. `make install` this repository

### Run

1. Start a first instance e.g. `make run service=Alice`
2. Start a second instance e.g. `make run service=Bob`
3. Send data e.g. `curl -X PUT http://localhost:5000 -H "Content-Type: application/json" -d '{"service": "alice", "amount": 1}'`
4. GET a clock  e.g. `http://localhost:5000/clock`

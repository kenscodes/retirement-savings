# Self-saving for Retirement — BlackRock Hackathon

Automated retirement savings through expense-based micro-investments.

## Quick Start

### Run with Maven
```bash
mvn clean package
mvn spring-boot:run
```

### Run with Docker
```bash
# Build
docker build -t blk-hacking-ind-participant .

# Run (port 5477)
docker run -d -p 5477:5477 blk-hacking-ind-participant
```

## API Endpoints

| Method | Endpoint                                              | Description                              |
|--------|-------------------------------------------------------|------------------------------------------|
| POST   | `/blackrock/challenge/v1/transactions/parse`          | Enrich expenses with ceiling & remanent  |
| POST   | `/blackrock/challenge/v1/transactions/validator`      | Validate transactions (negatives, dupes) |
| POST   | `/blackrock/challenge/v1/transactions/filter`         | Apply temporal constraints (q/p/k)       |
| POST   | `/blackrock/challenge/v1/returns/nps`                 | NPS returns (7.11%, with tax benefit)    |
| POST   | `/blackrock/challenge/v1/returns/index`               | NIFTY 50 returns (14.49%, no tax)        |
| GET    | `/blackrock/challenge/v1/performance`                 | System performance metrics               |

## Example: Transaction Builder

```bash
curl -X POST http://localhost:5477/blackrock/challenge/v1/transactions/parse \
  -H "Content-Type: application/json" \
  -d '[{"date":"2023-10-12 20:15:38","amount":250},{"date":"2023-02-28 15:49:28","amount":375}]'
```

## Example: Returns Calculation (NPS)

```bash
curl -X POST http://localhost:5477/blackrock/challenge/v1/returns/nps \
  -H "Content-Type: application/json" \
  -d '{
    "age": 29, "wage": 50000, "inflation": 5.5,
    "q": [{"fixed": 0, "start": "2023-07-01 00:00:00", "end": "2023-07-31 23:59:59"}],
    "p": [{"extra": 25, "start": "2023-10-01 08:00:00", "end": "2023-12-31 19:59:59"}],
    "k": [{"start": "2023-01-01 00:00:00", "end": "2023-12-31 23:59:59"}],
    "transactions": [
      {"date": "2023-10-12 20:15:00", "amount": 250},
      {"date": "2023-02-28 15:49:00", "amount": 375},
      {"date": "2023-07-01 21:59:00", "amount": 620},
      {"date": "2023-12-17 08:09:00", "amount": 480}
    ]
  }'
```

## Technology Stack
- **Java 17** (OpenJDK Temurin)
- **Spring Boot 3.2** (REST API)
- **Jackson** (JSON serialization with java.time support)
- **JUnit 5** (Testing)
- **Docker** (Containerized deployment)

## Testing
```bash
mvn test
```

## Architecture
```
com.retirement
├── config/          # Jackson configuration
├── controller/      # REST endpoints
├── dto/             # Request/Response objects
├── model/           # Core domain objects (Expense, QPeriod, PPeriod, KPeriod)
├── service/         # Business logic pipeline
│   ├── RemanentCalculator      # Step 1: Ceiling & remanent
│   ├── QPeriodProcessor        # Step 2: Fixed amount override
│   ├── PPeriodProcessor        # Step 3: Extra amount addition
│   ├── KPeriodGrouper          # Step 4: Group by evaluation periods
│   ├── InvestmentCalculator    # Step 5: Compound interest (NPS/NIFTY)
│   ├── TaxCalculator           # Step 6: Indian tax slabs + NPS benefit
│   └── InflationAdjuster       # Step 7: Real returns adjustment
└── Main.java                   # Spring Boot entry point
```

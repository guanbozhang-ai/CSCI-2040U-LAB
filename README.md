# Car Dealership Recommendation System

**Team: Git It Done**

A Java-based web application that matches users to cars through a survey-driven recommendation engine. Users answer questions about their ideal car preferences and the system computes the best matches from the inventory using a weighted Euclidean distance algorithm.

---

## Features

- **Survey-based car matching** — users input preferences for price, horsepower, mileage, seating, fuel economy, and year, each with an importance weight
- **16-predicate filter system** — filter inventory by make, body type, price, mileage, horsepower, fuel type, transmission, drivetrain, seating, cylinders, gears, and more
- **Euclidean distance matching engine** — scores every car in the inventory against user preferences and returns the top N closest matches
- **REST API** — HTTP server exposing `/api/match` (POST) for survey matching
- **Web frontend** — connected to the backend API, displays survey form and ranked results
- **JSON persistence** — car inventory stored and loaded from `cars.json` with no external libraries
- **JUnit 5 test suite** — automated unit, integration, and system tests

---

## Project Structure

```
Car-Dealership-Recommendation-System
│
├── src
│   ├── main
│   │   ├── java
│   │   │   ├── API.java                  — HTTP server, CORS, JSON conversion layer
│   │   │   ├── AttributeFormulas.java    — scoring formulas (cost, sportiness, mileage, seating, economy, recency)
│   │   │   ├── Car.java                  — car data model, toJson(), fromJson()
│   │   │   ├── CarFilter.java            — 16 composable filter predicates
│   │   │   ├── CarFilterRunner.java      — applies filter predicates to inventory
│   │   │   ├── CarStock.java             — inventory management, findBestMatch()
│   │   │   ├── CarStorage.java           — load/save cars.json
│   │   │   ├── FilterDemo.java           — CLI demo for filter system
│   │   │   ├── JSONHelper.java           — JSON parsing utilities
│   │   │   ├── MatchingDemo.java         — CLI demo for matching system
│   │   │   ├── SurveyAPI.java            — survey matching logic
│   │   │   ├── User.java                 — user model, preference survey, match()
│   │   │   └── UserAttributeMap.java     — stores user preference values and importance weights
│   │   └── resources
│   │       └── static
│   │           └── cars.json             — car inventory data
│   └── test
│       └── java
│           ├── TestAttributeFormulas.java
│           ├── TestCar.java
│           ├── TestJSON.java
│           └── TestUser.java
│
├── testserver
│   └── TestServer.java
│
├── pom.xml
└── README.md
```

---

## How to Run

### Prerequisites
- Java 17 or higher
- Maven

### 1. Clone the repository

```bash
git clone https://github.com/your-repository-link
cd Car-Dealership-Recommendation-System
```

### 2. Build the project

```bash
mvn clean compile
```

### 3. Start the backend server

```bash
mvn exec:java -Dexec.mainClass="API"
```

Server will start at `http://localhost:8080`

### 4. Open the frontend

Open `src/main/resources/static/index.html` in your browser.

### 5. Run the tests

```bash
mvn test
```

---

## API Reference

### POST `/api/match`

Accepts a survey JSON body and returns the best matching cars from the inventory.

**Request body:**

```json
{
  "price": 30000,
  "priceImportance": 4,
  "horsepower": 200,
  "powerImportance": 3,
  "mileage": 50000,
  "mileageImportance": 2,
  "seats": 5,
  "seatImportance": 3,
  "economy": 8.0,
  "economyImportance": 2,
  "year": 2020,
  "yearImportance": 3,
  "makes": ["Toyota", "Honda"],
  "bodyType": "SUV",
  "fuelType": "Gas",
  "transmission": "Automatic",
  "count": 3
}
```

**Response:** JSON object containing the top N matched car objects.

---

## How the Matching Works

1. User submits survey preferences (value + importance weight per attribute)
2. `API.java` converts the frontend JSON format to the backend attribute format
3. `User.fromJson()` builds a `UserAttributeMap` with 6 scored attributes: `cost`, `sportiness`, `mileage`, `seating`, `economy`, `recency`
4. Each attribute is normalized using `AttributeFormulas` (e.g. `cost(40000) = 2.0`, capped at 5.0)
5. `CarStock.findBestMatch()` computes the Euclidean distance between the user's attribute vector and every car in inventory
6. The N cars with the smallest distance are returned as the best matches

---

## Testing

Tests are located in `src/test/java/` and run with `mvn test`.

| Test Class | Coverage | Type |
|---|---|---|
| TestAttributeFormulas.java | All 6 scoring formulas including cap behaviour | Clear Box |
| TestCar.java | Constructor validation, toJson(), fromJson() | Clear Box |
| TestJSON.java | JSON round-trip (Car → JSON → Car) | Translucent Box |
| TestUser.java | User.fromJson(), UserAttributeMap construction | Translucent Box |

---

## Product Backlog

| Title | Estimate | Priority | Status |
|---|---|---|---|
| Create car matching survey | 10h | Must Have | ✅ Done |
| Match users to cars based on survey | 12h | Must Have | ✅ Done |
| Display recommended cars | 8h | Must Have | ✅ Done |
| Show car listings with filters | 8h | Should Have | ✅ Done |
| Show detailed car pages | 6h | Must Have | 🔄 In Progress |
| Admin can add/edit/remove listings | 10h | Must Have | 🔄 In Progress |
| Save/Favourite cars | 5h | Should Have | 📋 Backlog |
| Book a test drive | 6h | Should Have | 📋 Backlog |
| Customer reviews of sellers | 5h | Nice to Have | 📋 Backlog |

---

## Workflow

All user stories follow this process:

**Backlog → To Do → In Progress → In Testing → Done**

Each iteration is 2 weeks. Stories are selected from the backlog at the start of each iteration and move through the board as development progresses.

---

## Challenges & Solutions

| Challenge | Solution |
|---|---|
| Frontend/backend JSON field mismatch caused body type to never reach the matching engine | Added `API.java` conversion layer to remap all frontend fields to backend format |
| Copy-paste bug in `preferredBodyTypesSurvey()` added body types to the wrong list | Fixed by changing `preferredMakes.add()` to `preferredBodyTypes.add()` |
| Building JSON parsing with no external libraries | Hand-rolled `JSONHelper.java` with extractString, extractInt, extractDouble, extractStringList |
| Managing collaboration and version control | Used GitHub branches and pull requests throughout all iterations |

---

## Team

**Git It Done**

Contributions across design, development, testing, and documentation throughout all 3 iterations.

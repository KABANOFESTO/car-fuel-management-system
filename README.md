# Car Management System

Professional Car Management System with pure Java Servlet backend and CLI client.

## 🏗️ Architecture

```text
┌─────────────────────────────────────────────────────────┐
│                     CLI Client                          │
│  (Standalone Java App using HttpClient)                 │
└────────────────────┬────────────────────────────────────┘
                     │ HTTP
                     ↓
┌─────────────────────────────────────────────────────────┐
│                  Servlet Layer                          │
│  - CarServlet                                           │
│  - FuelServlet                                          │
│  - FuelStatsServlet                                     │
│  - FuelStatsQueryServlet (Mandatory)                    │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────┴────────────────────────────────────┐
│                  Service Layer                          │
│  - CarService                                           │
│  - FuelStatisticsService                                │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────┴────────────────────────────────────┐
│                Repository Layer                         │
│  - CarRepository (Singleton)                            │
│  - FuelEntryRepository (Singleton)                      │
└────────────────────┬────────────────────────────────────┘
                     │
┌────────────────────┴────────────────────────────────────┐
│              In-Memory Storage                          │
│  ConcurrentHashMap<Long, Car>                           │
│  ConcurrentHashMap<Long, FuelEntry>                     │
└─────────────────────────────────────────────────────────┘
```

## 🚀 Docker Setup

**Start all services:**
```bash
docker-compose up -d --build
```

The backend API is now available at `http://localhost:8080`.

## 🖥️ Using the CLI

Run commands directly via the docker container:

**1. Create a Car**
```bash
docker-compose exec cli java -jar app.jar create-car --brand Toyota --model Camry --year 2024
```

**2. Add Fuel**
```bash
docker-compose exec cli java -jar app.jar add-fuel --carId 1 --liters 45 --price 3.50 --odometer 10500
```

**3. View Statistics**
```bash
docker-compose exec cli java -jar app.jar fuel-stats --carId 1
```

**4. List all Cars**
```bash
docker-compose exec cli java -jar app.jar list-cars
```

## 🛑 Stop
```bash
docker-compose down
```

## 🛠 Manual Build (Optional)

**Prerequisites:** Java 17, Maven 3.8+

```bash
# Build
mvn clean install

# Run Backend
cd backend && mvn tomcat7:run

# Run CLI
java -jar cli-client/target/cli-client-1.0.0.jar help
```

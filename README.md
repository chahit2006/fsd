# BookMyShow - Loyalty Points & Promo Code Module

A self-contained Spring Boot module for managing loyalty points and promo codes in a BookMyShow clone.

## Prerequisites

- **Java 17+** (JDK)
- **PostgreSQL 14+**
- **Maven 3.9+** (or use the included `mvnw.cmd` wrapper)

## Setup

### 1. Database

Create a PostgreSQL database:

```sql
CREATE DATABASE bookmyshow;
```

### 2. Environment Variables

| Variable | Default | Description |
|----------|---------|-------------|
| `DB_URL` | `jdbc:postgresql://localhost:5432/bookmyshow` | JDBC URL |
| `DB_USERNAME` | `postgres` | DB user |
| `DB_PASSWORD` | `postgres` | DB password |
| `SERVER_PORT` | `8081` | App port |

### 3. Build & Run

```bash
# Build
mvn clean package -DskipTests

# Run
java -jar target/loyalty-promo-module-1.0.0.jar

# Or with env vars
DB_URL=jdbc:postgresql://localhost:5432/bookmyshow DB_USERNAME=myuser DB_PASSWORD=mypass java -jar target/loyalty-promo-module-1.0.0.jar
```

The schema and seed data are auto-applied on startup via `schema.sql` and `data.sql`.

### 4. Access Swagger UI

Open [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

## Endpoint Table

### Loyalty Tier Config (`/api/v1/loyalty/tiers`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/loyalty/tiers` | Create tier |
| GET | `/api/v1/loyalty/tiers/{id}` | Get by ID |
| GET | `/api/v1/loyalty/tiers` | Get all (paginated) |
| PUT | `/api/v1/loyalty/tiers/{id}` | Full update |
| PATCH | `/api/v1/loyalty/tiers/{id}` | Partial update |
| DELETE | `/api/v1/loyalty/tiers/{id}` | Delete |

### Loyalty Account (`/api/v1/loyalty/accounts`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/loyalty/accounts` | Create account |
| GET | `/api/v1/loyalty/accounts/{id}` | Get by ID |
| GET | `/api/v1/loyalty/accounts` | Get all (paginated) |
| PUT | `/api/v1/loyalty/accounts/{id}` | Full update |
| PATCH | `/api/v1/loyalty/accounts/{id}` | Partial update |
| DELETE | `/api/v1/loyalty/accounts/{id}` | Delete |

### Loyalty Transaction (`/api/v1/loyalty/transactions`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/loyalty/transactions` | Create transaction |
| GET | `/api/v1/loyalty/transactions/{id}` | Get by ID |
| GET | `/api/v1/loyalty/transactions` | Get all (paginated) |
| PUT | `/api/v1/loyalty/transactions/{id}` | Full update |
| PATCH | `/api/v1/loyalty/transactions/{id}` | Partial update |
| DELETE | `/api/v1/loyalty/transactions/{id}` | Delete |

### Loyalty Redemption (`/api/v1/loyalty/redemptions`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/loyalty/redemptions` | Create redemption |
| GET | `/api/v1/loyalty/redemptions/{id}` | Get by ID |
| GET | `/api/v1/loyalty/redemptions` | Get all (paginated) |
| PUT | `/api/v1/loyalty/redemptions/{id}` | Full update |
| PATCH | `/api/v1/loyalty/redemptions/{id}` | Partial update |
| DELETE | `/api/v1/loyalty/redemptions/{id}` | Delete |

### Coupon (`/api/v1/promos`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/promos` | Create coupon |
| GET | `/api/v1/promos/{id}` | Get by ID |
| GET | `/api/v1/promos` | Get all (paginated) |
| PUT | `/api/v1/promos/{id}` | Full update |
| PATCH | `/api/v1/promos/{id}` | Partial update / status toggle |
| DELETE | `/api/v1/promos/{id}` | Soft-delete (deactivate) |

### Coupon Applicability (`/api/v1/promos/applicability`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/promos/applicability` | Create rule |
| GET | `/api/v1/promos/applicability/{id}` | Get by ID |
| GET | `/api/v1/promos/applicability` | Get all (paginated) |
| PUT | `/api/v1/promos/applicability/{id}` | Full update |
| PATCH | `/api/v1/promos/applicability/{id}` | Partial update |
| DELETE | `/api/v1/promos/applicability/{id}` | Delete |

### Coupon Usage (`/api/v1/promos/usage`)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/promos/usage` | Record usage |
| GET | `/api/v1/promos/usage/{id}` | Get by ID |
| GET | `/api/v1/promos/usage` | Get all (paginated) |
| PUT | `/api/v1/promos/usage/{id}` | Full update |
| PATCH | `/api/v1/promos/usage/{id}` | Partial update |
| DELETE | `/api/v1/promos/usage/{id}` | Delete |

### Business Endpoints
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/v1/promos/validate` | Validate promo code against booking amount |
| POST | `/api/v1/promos/apply` | Apply promo code (records usage, enforces limits) |
| GET | `/api/v1/loyalty/{userId}/balance` | Get user's points balance & tier |
| GET | `/api/v1/loyalty/{userId}/transactions` | Get user's transaction history |
| POST | `/api/v1/loyalty/earn` | Earn points for a booking |
| POST | `/api/v1/loyalty/redeem` | Redeem points for a discount |
| POST | `/api/v1/loyalty/reverse` | Reverse points & promo on cancellation |

### Scheduled Jobs
- **Points Expiry**: Runs daily at 1:00 AM (configurable via `loyalty.scheduler.expiry-cron`)

## Running Tests

```bash
# Unit tests only
mvn test

# Full verify (unit + integration)
mvn clean verify

# Integration tests use H2 in-memory DB (no PostgreSQL needed)
```

## Postman Collection

Import `postman/loyalty-promo.postman_collection.json` into Postman.

- Collection variable `baseUrl` defaults to `http://localhost:8081`
- Run the collection top-to-bottom; test scripts auto-capture IDs for dependent requests
- Includes success and failure scenarios (404, 400, 409)

## How the Booking Module Should Call This Module

### On Booking Confirmation
1. **Earn points**: `POST /api/v1/loyalty/earn` with `{ userId, bookingId, bookingAmount }`
2. **Apply promo** (if user provided a code): `POST /api/v1/promos/apply` with `{ couponCode, userId, bookingId, bookingAmount }`
3. **Redeem points** (if user opted in): `POST /api/v1/loyalty/redeem` with `{ userId, bookingId, pointsToRedeem, bookingAmount }`

### On Booking Cancellation
1. **Reverse everything**: `POST /api/v1/loyalty/reverse` with `{ userId, bookingId }` — this reverses earned points, restores redeemed points, and deletes promo usage records for that booking.

### Before Payment (optional)
1. **Validate promo**: `POST /api/v1/promos/validate` with `{ couponCode, bookingAmount, userId }` — returns discount amount without recording usage.
2. **Check balance**: `GET /api/v1/loyalty/{userId}/balance` — shows available redeemable points.

## Configuration

Key properties in `application.yml`:

```yaml
loyalty:
  points:
    earn-rate: 1          # points earned per unit
    earn-per-amount: 10   # rupees per point unit (1 point per ₹10)
    redeem-conversion: 1.0 # 1 point = ₹1
    max-redeem-percentage: 50 # max 50% of booking can be paid via points
    expiry-days: 365      # points expire after 365 days
```

## Logging

- Console + rolling file at `logs/loyalty-promo.log`
- Request/response logging filter logs method, URI, status, and duration
- Business actions logged at INFO, rejected promos at WARN, exceptions at ERROR

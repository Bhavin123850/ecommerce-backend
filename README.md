# E-commerce Backend (Spring Boot + PostgreSQL)

A simple, no-frills e-commerce REST API. Only 5 entities: Category, Product, AppUser, Order, OrderItem.
No Flyway/Liquibase — you create the tables manually (see `schema-reference.sql`).
No deployment configs (no Docker, no CI) — just the app.

## 1. Create the database

```sql
CREATE DATABASE ecommerce_db;
```

Then run `schema-reference.sql` against it (e.g. via psql or a GUI client) to create the tables.

## 2. Configure the connection

Edit `src/main/resources/application.properties` if your Postgres username/password/port differ from the defaults:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/ecommerce_db
spring.datasource.username=postgres
spring.datasource.password=postgres
```

## 3. Run the app

```bash
mvn spring-boot:run
```

The API starts on `http://localhost:8080`.

## Endpoints

| Resource   | Method | Path                              |
|------------|--------|------------------------------------|
| Categories | GET    | /api/categories                   |
|            | GET    | /api/categories/{id}               |
|            | POST   | /api/categories                   |
|            | PUT    | /api/categories/{id}               |
|            | DELETE | /api/categories/{id}               |
| Products   | GET    | /api/products                     |
|            | GET    | /api/products/{id}                 |
|            | GET    | /api/products/category/{categoryId}|
|            | POST   | /api/products                     |
|            | PUT    | /api/products/{id}                 |
|            | DELETE | /api/products/{id}                 |
| Users      | GET    | /api/users                        |
|            | GET    | /api/users/{id}                    |
|            | POST   | /api/users                         |
|            | PUT    | /api/users/{id}                    |
|            | DELETE | /api/users/{id}                    |
| Orders     | GET    | /api/orders                       |
|            | GET    | /api/orders/{id}                   |
|            | GET    | /api/orders/user/{userId}          |
|            | POST   | /api/orders                        |
|            | PATCH  | /api/orders/{id}/status?status=SHIPPED |
|            | DELETE | /api/orders/{id}                   |

### Example: create a category
```json
POST /api/products
{
  "name": "Electronics",
  "description": "Phones, laptops, etc."
}
```

### Example: create a product
```json
POST /api/products
{
  "name": "Wireless Mouse",
  "description": "2.4GHz wireless mouse",
  "price": 19.99,
  "stockQuantity": 100,
  "category": { "id": 1 }
}
```

### Example: create a user
```json
POST /api/users
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "secret123",
  "address": "123 Main St"
}
```

### Example: place an order
```json
POST /api/orders
{
  "userId": 1,
  "items": [
    { "productId": 1, "quantity": 2 },
    { "productId": 2, "quantity": 1 }
  ]
}
```
Price and total are calculated server-side from the current product prices, and stock is decremented automatically.

## Notes
- `spring.jpa.hibernate.ddl-auto=none` — Hibernate will never touch your schema; you're fully in control.
- Passwords are stored as plain text for simplicity. Add hashing (e.g. Spring Security + BCrypt) before using this for anything real.
- No authentication/authorization is included — add Spring Security if/when you need it.

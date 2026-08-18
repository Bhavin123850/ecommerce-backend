-- ================================================================
-- Reference schema matching the JPA entities in this project.
-- This is NOT executed automatically (ddl-auto=none, no Flyway).
-- Run this manually in your PostgreSQL database (e.g. ecommerce_db)
-- before starting the application.
-- ================================================================

CREATE TABLE categories (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(1000)
);

CREATE TABLE products (
    id              BIGSERIAL PRIMARY KEY,
    name            VARCHAR(255) NOT NULL,
    description     VARCHAR(1000),
    price           NUMERIC(12, 2) NOT NULL,
    stock_quantity  INTEGER NOT NULL,
    category_id     BIGINT REFERENCES categories(id)
);

CREATE TABLE app_users (
    id       BIGSERIAL PRIMARY KEY,
    name     VARCHAR(255) NOT NULL,
    email    VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    address  VARCHAR(500)
);

CREATE TABLE orders (
    id            BIGSERIAL PRIMARY KEY,
    user_id       BIGINT NOT NULL REFERENCES app_users(id),
    order_date    TIMESTAMP NOT NULL DEFAULT NOW(),
    status        VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    total_amount  NUMERIC(12, 2) NOT NULL
);

CREATE TABLE order_items (
    id          BIGSERIAL PRIMARY KEY,
    order_id    BIGINT NOT NULL REFERENCES orders(id) ON DELETE CASCADE,
    product_id  BIGINT NOT NULL REFERENCES products(id),
    quantity    INTEGER NOT NULL,
    price       NUMERIC(12, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS "role"
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL UNIQUE
);
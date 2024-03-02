CREATE TABLE IF NOT EXISTS "user"
(
    id        SERIAL PRIMARY KEY,
    email     VARCHAR(128) NOT NULL,
    password  VARCHAR(256) NOT NULL,
    name      VARCHAR(128) NOT NULL,
    last_name VARCHAR(128) NOT NULL
);
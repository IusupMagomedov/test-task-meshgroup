CREATE TABLE IF NOT EXISTS users
(
    id            BIGSERIAL PRIMARY KEY,
    name          VARCHAR(500) NOT NULL,
    date_of_birth DATE         NOT NULL,
    password      VARCHAR(500) NOT NULL
);

CREATE TABLE IF NOT EXISTS account
(
    id              BIGSERIAL PRIMARY KEY,
    user_id         BIGINT         NOT NULL UNIQUE REFERENCES users (id),
    balance         DECIMAL(19, 2) NOT NULL,
    initial_balance DECIMAL(19, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS email_data
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT       NOT NULL REFERENCES users (id),
    email   VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS phone_data
(
    id      BIGSERIAL PRIMARY KEY,
    user_id BIGINT      NOT NULL REFERENCES users (id),
    phone   VARCHAR(13) NOT NULL UNIQUE
);

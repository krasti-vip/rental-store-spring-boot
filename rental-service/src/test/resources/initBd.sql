--- Удаление таблицы users
DROP TABLE IF EXISTS users CASCADE;
--- Создание таблицы users
CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    user_name  VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    passport   INT         NOT NULL UNIQUE,
    email      VARCHAR(50),
    bank_card  BIGINT      NOT NULL
    );

--- Инициализация таблицы users
INSERT INTO users (user_name, first_name, last_name, passport, email, bank_card)
VALUES ('bill', 'Ivanov', 'Dima', 456987123, null, 7896541236547852),
       ('tom', 'Sidorov', 'Pasha', 98741236, null, 987456321458796),
       ('jerry', 'Petrov', 'Sasha', 12365478, null, 12589745321698),
       ('ozi', 'Galcin', 'Gena', 56987415, 'gav@mail.ru', 32569874125463),
       ('eminem', 'Pugachev', 'Genya', 85297418, null, 943655557412365);

--- Удаление таблицы bikes
DROP TABLE IF EXISTS bikes CASCADE;

--- Создание таблицы bikes
CREATE TABLE IF NOT EXISTS bikes
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(50)      NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    horse_power INT              NOT NULL,
    volume      DOUBLE PRECISION NOT NULL,
    user_id      INT
);

--- Инициализация таблицы bikes
INSERT INTO bikes (name, price, horse_power, volume, user_id)
VALUES ('BMW', 2000, 200, 1.0, null),
       ('SUZUKI', 30000, 300, 1.0, null),
       ('YAMAHA', 40000, 400, 1.0, null),
       ('URAL', 2000, 200, 1.0, null),
       ('HONDA', 2000, 200, 1.0, null);

--- Удаление таблицы cars
DROP TABLE IF EXISTS cars CASCADE;

--- Создание таблицы cars
CREATE TABLE IF NOT EXISTS cars
(
    id          SERIAL PRIMARY KEY,
    title       VARCHAR(50)      NOT NULL,
    price       DOUBLE PRECISION NOT NULL,
    horse_power INT              NOT NULL,
    volume      DOUBLE PRECISION NOT NULL,
    color       VARCHAR(50)      NOT NULL,
    user_id      INT
);

--- Инициализация таблицы cars
INSERT INTO cars (title, price, horse_power, volume, color, user_id)
VALUES ('MERCEDES', 655.30, 250, 3.5, 'black', null),
       ('HONDA', 360.50, 190, 2.4, 'red', null),
       ('HYUNDAI', 320.90, 156, 2.0, 'white', null),
       ('BMW', 640.50, 450, 5.0, 'blue', null),
       ('OPEL', 210.90, 110, 1.8, 'gold', null);

--- Удаление таблицы rental
DROP TABLE IF EXISTS rentals CASCADE;

--- Создание таблицы rental
CREATE TABLE IF NOT EXISTS rentals
(
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id) ON DELETE RESTRICT,
    car_id INT REFERENCES cars(id) ON DELETE RESTRICT,
    bike_id INT REFERENCES bikes(id) ON DELETE RESTRICT,
    start_date TIMESTAMP NOT NULL,
    end_date TIMESTAMP,
    rental_amount DOUBLE PRECISION NOT NULL,
    is_paid BOOLEAN DEFAULT FALSE
    );

--- Инициализация таблицы rental
INSERT INTO rentals (user_id, car_id, bike_id, start_date, end_date, rental_amount, is_paid)
VALUES
    (1, 2, NULL, '2025-03-01 10:00:00', '2025-03-07 12:00:00', 5000.00, TRUE),
    (2, 1, NULL, '2025-03-05 14:00:00', '2025-03-10 09:00:00', 4500.50, FALSE),
    (3, NULL, 2, '2025-02-20 09:00:00', NULL, 12000.00, FALSE),
    (4, 4, NULL, '2025-03-03 15:30:00', '2025-03-08 18:00:00', 7800.75, TRUE);
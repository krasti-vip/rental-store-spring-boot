--- Удаление таблицы users
DROP TABLE IF EXISTS users CASCADE;
--- Создание таблицы users
CREATE TABLE IF NOT EXISTS users
(
    id         SERIAL PRIMARY KEY,
    user_name  VARCHAR(50) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name  VARCHAR(50) NOT NULL,
    passport   BIGINT      NOT NULL UNIQUE,
    email      VARCHAR(50)
);
--- Инициализация таблицы users
INSERT INTO users (user_name, first_name, last_name, passport, email)
VALUES ('bill', 'Ivanov', 'Dima', 456987123, null),
       ('tom', 'Sidorov', 'Pasha', 98741236, null),
       ('jerry', 'Petrov', 'Sasha', 12365478, null),
       ('ozi', 'Galcin', 'Gena', 56987415, 'gav@mail.ru'),
       ('eminem', 'Pugachev', 'Genya', 85297418, null);

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
    user_id     INT
);
--- Инициализация таблицы bikes
INSERT INTO bikes (name, price, horse_power, volume, user_id)
VALUES ('BMW', 2000, 200, 1.0, null),
       ('SUZUKI', 30000, 300, 1.0, null),
       ('YAMAHA', 40000, 400, 1.0, 1),
       ('URAL', 2000, 200, 1.0, 1),
       ('HONDA', 2000, 200, 1.0, null);
--- Удаление таблицы bicycles
DROP TABLE IF EXISTS bicycles CASCADE;
--- Создание таблицы bicycles
CREATE TABLE IF NOT EXISTS bicycles
(
    id      SERIAL PRIMARY KEY,
    model   VARCHAR(50)      NOT NULL,
    price   DOUBLE PRECISION NOT NULL,
    color   VARCHAR(50)      NOT NULL,
    user_id INT
);
--- Инициализация таблицы bicycles
INSERT INTO bicycles (model, price, color, user_id)
VALUES ('Mongust', 2000, 'blue', 1),
       ('Ctels', 1000, 'black', 1),
       ('Aist', 1500, 'white', null),
       ('BMX', 2500, 'blue', null),
       ('Mochina', 1200, 'red', null);
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
    user_id     INT
);
--- Инициализация таблицы cars
INSERT INTO cars (title, price, horse_power, volume, color, user_id)
VALUES ('MERCEDES', 655.30, 250, 3.5, 'black', null),
       ('HONDA', 360.50, 190, 2.4, 'red', 1),
       ('HYUNDAI', 320.90, 156, 2.0, 'white', null),
       ('BMW', 640.50, 450, 5.0, 'blue', null),
       ('OPEL', 210.90, 110, 1.8, 'gold', null);
--- Удаление таблицы rental
DROP TABLE IF EXISTS rentals CASCADE;
--- Создание таблицы rental
CREATE TABLE IF NOT EXISTS rentals
(
    id            SERIAL PRIMARY KEY,
    user_id       INT,
    car_id        INT,
    bike_id       INT,
    bicycle_id    INT,
    start_date    TIMESTAMP        NOT NULL,
    end_date      TIMESTAMP,
    rental_amount DOUBLE PRECISION NOT NULL,
    is_paid       BOOLEAN DEFAULT FALSE
);
--- Инициализация таблицы rental
INSERT INTO rentals (user_id, car_id, bike_id, bicycle_id, start_date, end_date, rental_amount, is_paid)
VALUES (1, 2, NULL, NULL, '2025-03-01 10:00:00', '2025-03-07 12:00:00', 5000.00, TRUE),
       (1, 1, NULL, NULL,'2025-03-05 14:00:00', '2025-03-10 09:00:00', 4500.50, FALSE),
       (3, NULL, 2, NULL,'2025-02-20 09:00:00', NULL, 12000.00, FALSE),
       (1, 4, NULL, NULL,'2025-03-03 15:30:00', '2025-03-08 18:00:00', 7800.75, TRUE);
--- Удаление таблицы bank_cards
DROP TABLE IF EXISTS bank_cards CASCADE;
--- Создание таблицы bank_cards
CREATE TABLE IF NOT EXISTS bank_cards
(
    id            SERIAL PRIMARY KEY,
    user_id     INT,
    number_card VARCHAR(20) NOT NULL,
    expiration_date VARCHAR(20) NOT NULL,
    secret_code INT NOT NULL
    );
--- Инициализация таблицы bank_cards
INSERT INTO bank_cards (user_id, number_card, expiration_date, secret_code)
VALUES (3, '1234567809876543', '12/25', 123),
       (2, '4564567809876543', '12/26', 345),
       (null, '9874567809876543', '12/43', 543),
       (4, '7654567809876543', '12/32', 567),
       (5, '6544567809876543', '11/25', 346);
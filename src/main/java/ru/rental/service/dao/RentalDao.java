package ru.rental.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.rental.service.model.Rental;
import ru.rental.service.util.ConnectionManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RentalDao implements DAO<Rental, Integer> {

    private static final Logger log = LoggerFactory.getLogger(RentalDao.class);

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS rentals (
                id SERIAL PRIMARY KEY,
                user_id INT REFERENCES users(id) ON DELETE RESTRICT,
                car_id INT REFERENCES cars(id) ON DELETE RESTRICT,
                bike_id INT REFERENCES bikes(id) ON DELETE RESTRICT,
                start_date TIMESTAMP NOT NULL,
                end_date TIMESTAMP,
                rental_amount DOUBLE PRECISION NOT NULL,
                is_paid BOOLEAN DEFAULT FALSE
            )
            """;

    private static final String INSERT_RENTAL = """
            INSERT INTO rentals (user_id, car_id, bike_id, start_date, end_date, rental_amount, is_paid)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            """;

    private static final String SELECT_RENTAL = """
            SELECT id, user_id, car_id, bike_id, start_date, end_date, rental_amount, is_paid 
            FROM rentals 
            WHERE id = ?
            """;

    private static final String UPDATE_RENTAL = """
            UPDATE rentals 
            SET
                user_id = ?,
                car_id = ?,
                bike_id = ?,
                start_date = ?,
                end_date = ?,
                rental_amount = ?,
                is_paid = ?
            WHERE id = ?
            """;

    private static final String DELETE_RENTAL = """
            DELETE FROM rentals WHERE id = ?
            """;

    private static final String SELECT_ALL_RENTALS = """
            SELECT id, user_id, car_id, bike_id, start_date, end_date, rental_amount, is_paid 
            FROM rentals
            """;

    private static final String CHECK_TABLE = """
            SELECT EXISTS (
                SELECT FROM information_schema.tables 
                WHERE table_name = ?
            )
            """;

    @Override
    public boolean checkIfTableExists(String tableName) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(CHECK_TABLE)) {

            preparedStatement.setString(1, tableName.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("Table exists: {}", tableName);
                return resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка существования таблицы", e);
        }
        log.warn("Table no exists: {}", tableName);
        return false;
    }

    @Override
    public void createTable() {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(CREATE_TABLE)) {

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка создания таблицы rentals", e);
        }
    }

    @Override
    public Rental get(Integer id) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_RENTAL)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return mapResultSetToRental(resultSet);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения записи аренды", e);
        }
    }

    @Override
    public Rental update(Integer id, Rental obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(UPDATE_RENTAL)) {

            fillPreparedStatement(preparedStatement, obj);

            preparedStatement.setInt(8, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                return obj;
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления записи аренды", e);
        }
    }

    @Override
    public Rental save(Rental obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(INSERT_RENTAL, Statement.RETURN_GENERATED_KEYS)) {

            fillPreparedStatement(preparedStatement, obj);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        obj.setId(generatedId);
                        return obj;
                    }
                }
            }
            return null;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка сохранения записи аренды", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(DELETE_RENTAL)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка удаления записи аренды", e);
        }
    }

    @Override
    public List<Rental> filterBy(Predicate<Rental> predicate) {
        return getAll().stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public List<Rental> getAll() {
        List<Rental> rentals = new ArrayList<>();

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_ALL_RENTALS);
             final var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                rentals.add(mapResultSetToRental(resultSet));
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения всех записей аренды", e);
        }
        return rentals;
    }

    private Rental mapResultSetToRental(ResultSet resultSet) throws SQLException {
        Integer bikeId = resultSet.getInt("bike_id");
        if (resultSet.wasNull()) {
            bikeId = null;
        }

        Integer carId = resultSet.getInt("car_id");
        if (resultSet.wasNull()) {
            carId = null;
        }

        return Rental.builder()
                .id(resultSet.getInt("id"))
                .userId(resultSet.getInt("user_id"))
                .carId(carId)
                .bikeId(bikeId)
                .startDate(resultSet.getTimestamp("start_date").toLocalDateTime())
                .endDate(resultSet.getTimestamp("end_date") != null ?
                        resultSet.getTimestamp("end_date").toLocalDateTime() : null)
                .rentalAmount(resultSet.getDouble("rental_amount"))
                .isPaid(resultSet.getBoolean("is_paid"))
                .build();
    }

    private void fillPreparedStatement(PreparedStatement preparedStatement, Rental obj) throws SQLException {
        preparedStatement.setInt(1, obj.getUserId());

        if (obj.getCarId() != null) {
            preparedStatement.setInt(2, obj.getCarId());
        } else {
            preparedStatement.setNull(2, Types.INTEGER);
        }

        if (obj.getBikeId() != null) {
            preparedStatement.setInt(3, obj.getBikeId());
        } else {
            preparedStatement.setNull(3, Types.INTEGER);
        }

        if (obj.getStartDate() != null) {
            preparedStatement.setTimestamp(4, Timestamp.valueOf(obj.getStartDate()));
        } else {
            preparedStatement.setNull(4, Types.TIMESTAMP);
        }

        if (obj.getEndDate() != null) {
            preparedStatement.setTimestamp(5, Timestamp.valueOf(obj.getEndDate()));
        } else {
            preparedStatement.setNull(5, Types.TIMESTAMP);
        }

        preparedStatement.setDouble(6, obj.getRentalAmount());
        preparedStatement.setBoolean(7, obj.getIsPaid() != null ? obj.getIsPaid() : false);
    }
}

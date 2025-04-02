package ru.rental.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.rental.service.model.BankCard;
import ru.rental.service.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class BankCardDao implements DAO<BankCard, Integer> {

    private static final Logger log = LoggerFactory.getLogger(BankCardDao.class);

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS bank_cards (
                id SERIAL PRIMARY KEY,
                user_id INT REFERENCES users(id) ON DELETE RESTRICT,
                number_card VARCHAR(20) NOT NULL,
                expiration_date VARCHAR(20) NOT NULL,
                secret_code VARCHAR(20) NOT NULL
            )
            """;

    private static final String INSERT_BC = """
            INSERT INTO bank_cards (user_id, number_card, expiration_date, secret_code)
            VALUES (?, ?, ?, ?)
            """;

    private static final String SELECT_BC = """
            SELECT id, user_id, number_card, expiration_date, secret_code 
            FROM bank_cards 
            WHERE id = ?
            """;

    private static final String UPDATE_BC = """
            UPDATE bank_cards 
            SET
                user_id = ?,
                number_card = ?,
                expiration_date = ?,
                secret_code = ?
            WHERE id = ?
            """;

    private static final String DELETE_BC = """
            DELETE FROM bank_cards WHERE id = ?
            """;

    private static final String SELECT_ALL_BC = """
            SELECT id, user_id, number_card, expiration_date, secret_code 
            FROM bank_cards
            """;

    private static final String CHECK_TABLE = """
            SELECT EXISTS (
                SELECT FROM information_schema.tables 
                WHERE table_name = ?
            )
            """;

    private static final String SELECT_BANK_CARDS_BY_USER_ID = """
            SELECT id, user_id, number_card, expiration_date, secret_code 
            FROM bank_cards 
            WHERE user_id = ?
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
            log.info("Table bank_cards created successfully!");

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка создания таблицы bank_cards", e);
        }
    }

    @Override
    public BankCard get(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_BC)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("BankCard with id {} found!", id);
                return mapResultSetToBankCard(resultSet);
            } else {
                log.info("BankCard with id {} not found!", id);
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения записи банковской карты", e);
        }
    }

    @Override
    public BankCard update(Integer id, BankCard obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(UPDATE_BC)) {

            fillPreparedStatement(preparedStatement, obj);
            preparedStatement.setInt(5, id);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                log.info("BankCard with id {} updated successfully!", id);
                return obj;
            } else {
                throw new IllegalStateException("BankCard not found with id: " + id);  // Кидаем исключение!
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления записи банковской карты", e);
        }
    }

    @Override
    public BankCard save(BankCard obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(INSERT_BC, Statement.RETURN_GENERATED_KEYS)) {

            fillPreparedStatement(preparedStatement, obj);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        obj.setId(generatedId);
                        log.info("BankCard saved with id: {}", generatedId);
                        return obj;
                    }
                }
            }
            log.info("BankCard not saved!");
            return null;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка сохранения записи банковской карты", e);
        }
    }

    @Override
    public boolean delete(Integer id) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(DELETE_BC)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            log.info("BankCard with id {} deleted: {}", id, rowsAffected > 0);
            return rowsAffected > 0;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка удаления записи банковской карты", e);
        }
    }

    @Override
    public List<BankCard> filterBy(Predicate<BankCard> predicate) {
        List<BankCard> allBankCards = getAll();
        log.info("Filtering bank cards by predicate");
        return allBankCards.stream()
                .filter(predicate)
                .toList();
    }

    @Override
    public List<BankCard> getAll() {
        List<BankCard> bankCards = new ArrayList<>();

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_ALL_BC);
             final var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                bankCards.add(mapResultSetToBankCard(resultSet));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения всех записей банковских карт", e);
        }
        log.info("Retrieved {} bank cards", bankCards.size());
        return bankCards;
    }

    private BankCard mapResultSetToBankCard(ResultSet resultSet) throws SQLException {
        return BankCard.builder()
                .id(resultSet.getInt("id"))
                .userId(resultSet.getInt("user_id"))
                .numberCard(resultSet.getString("number_card"))
                .expirationDate(resultSet.getString("expiration_date"))
                .secretCode(Integer.parseInt(resultSet.getString("secret_code")))
                .build();
    }

    private void fillPreparedStatement(PreparedStatement preparedStatement, BankCard bankCard) throws SQLException {
        preparedStatement.setInt(1, bankCard.getUserId());
        preparedStatement.setString(2, bankCard.getNumberCard());
        preparedStatement.setString(3, bankCard.getExpirationDate());
        preparedStatement.setString(4, bankCard.getSecretCode().toString());
    }

    public List<BankCard> getAllByUserId(int userId) {
        List<BankCard> bankCards = new ArrayList<>();

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_BANK_CARDS_BY_USER_ID)) {

            preparedStatement.setInt(1, userId);
            final var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bankCards.add(mapResultSetToBankCard(resultSet));
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения списка банковских карт пользователя", e);
        }
        log.info("Retrieved {} bank cards for user with id {}", bankCards.size(), userId);
        return bankCards;
    }
}


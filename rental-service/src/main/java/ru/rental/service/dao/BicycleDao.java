package ru.rental.service.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.rental.service.model.Bicycle;
import ru.rental.service.model.Bike;
import ru.rental.service.util.ConnectionManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Component
public class BicycleDao implements DAO<Bicycle, Integer> {

    private static final String ID = "id";

    private static final String MODEL = "model";

    private static final String PRICE = "price";

    private static final String COLOR = "color";

    private static final String USER_ID = "userId";

    private static final Logger log = LoggerFactory.getLogger(BicycleDao.class);

    private final UserDao userDao;

    @Autowired
    public BicycleDao(UserDao userDao) {
        this.userDao = userDao;
    }

    private static final String SELECT_BICYCLE = """
            SELECT id, model, price, color, user_id FROM bicycles WHERE id = ?
            """;

    private static final String CREATE_TABLE = """
            CREATE TABLE IF NOT EXISTS bicycles(
                id SERIAL PRIMARY KEY,
                model VARCHAR(50) NOT NULL,
                price DOUBLE PRECISION NOT NULL,
                color VARCHAR(50) NOT NULL,
                user_id INT REFERENCES users(id) ON DELETE RESTRICT
            )
            """;

    private static final String UPDATE_BICYCLE = """
            UPDATE bicycles 
            SET
                model = ?,
                price = ?,
                color = ?
            WHERE id = ?
            """;

    private static final String INSERT_BICYCLE = """
            INSERT INTO bicycles (model, price, color)
            VALUES (?, ?, ?)
            """;

    private static final String DELETE_BICYCLE = """
            DELETE FROM bicycles WHERE id = ?
            """;

    private static final String SELECT_ALL_BICYCLES = """
            SELECT id, model, price, color, user_id FROM bicycles
            """;

    private static final String UPDATE_BICYCLE_USER = """
                UPDATE bicycles
                SET user_id = ?
                WHERE id = ?
            """;

    private static final String SELECT_BICYCLE_BY_USER_ID = """
            SELECT id, model, price, color, user_id FROM bicycles 
            WHERE user_id = ?
            """;

    private static final String CHECK_TABLE = """
            SELECT EXISTS (
                    SELECT FROM information_schema.tables 
                    WHERE table_name = ?
            )
            """;

    private static final String CHECK_BICYCLE_ID = """
            SELECT 1 FROM bicycles WHERE id = ?
            """;


    /**
     * Метод создания таблицы в базе данных,
     * проверка на уникальность таблицы отсутствует, возможно дублирование, если по какой-то причине таблица не будет
     * создана выкинет Exception IllegalStateException("Ошибка создания таблицы", e);
     */
    @Override
    public void createTable() {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(CREATE_TABLE)) {

            preparedStatement.execute();

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка создания таблицы", e);
        }
    }

    /**
     * Метод возвращает объект по его id, присутствует проверка на null переданного id, в этом
     * случае бросит ошибку IllegalArgumentException("ID obj не может быть null"), если id существует, но
     * метод не смог вернуть его то бросит ошибку IllegalStateException("Ошибка передачи obj", e);
     *
     * @param id
     * @return
     */
    @Override
    public Bicycle get(Integer id) {
        if (id == null) {
            log.info("id is null!");
            throw new IllegalArgumentException("ID bicycle не может быть null");
        }

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_BICYCLE)) {

            preparedStatement.setInt(1, id);
            final var resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("bicycle {} exists!", id);
                return Bicycle.builder()
                        .id(resultSet.getInt(ID))
                        .model(resultSet.getString(MODEL))
                        .price(resultSet.getDouble(PRICE))
                        .color(resultSet.getString(COLOR))
                        .build();
            } else {
                log.info("bicycle with id {} not found!", id);
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка передачи bicycle", e);
        }
    }

    /**
     * Метод обновляет объект по переданному id и новому объекту для обновления, есть проверка
     * на null (через SQL запрос проверяем существует ли такой id если да, идем дальше если нет бросаем исключение),
     * если обновление по другим причинам не произошло,
     * бросит exception IllegalStateException("Ошибка обновления obj", e);
     *
     * @param id
     * @param obj
     * @return
     */
    @Override
    public Bicycle update(Integer id, Bicycle obj) {
        try (final var connection = ConnectionManager.getConnection()) {
            try (final var checkStmt = connection.prepareStatement(CHECK_BICYCLE_ID)) {
                checkStmt.setInt(1, id);
                try (final var rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("bicycle with id " + id + " does not exist");
                    }
                }
            }
            try (final var preparedStatement = connection.prepareStatement(UPDATE_BICYCLE)) {

                preparedStatement.setString(1, obj.getModel());
                preparedStatement.setDouble(2, obj.getPrice());
                preparedStatement.setString(3, obj.getColor());
                preparedStatement.setInt(4, id);

                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    log.info("bicycle {} has been updated!", id);
                    return obj;
                } else {
                    log.info("bicycle {} has not been updated!", id);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления bicycle", e);
        }
    }

    /**
     * Метод сохраняет новый переданный объект, отсутствует проверка на null (осторожней),
     * если по другой причине не сохранится obj, кинет исключение IllegalStateException("Ошибка сохранения obj", e);
     *
     * @param obj
     * @return
     */
    @Override
    public Bicycle save(Bicycle obj) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(INSERT_BICYCLE, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, obj.getModel());
            preparedStatement.setDouble(2, obj.getPrice());
            preparedStatement.setString(3, obj.getColor());

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        int generatedId = generatedKeys.getInt(1);
                        obj.setId(generatedId);
                        log.info("Bicycle {} has been saved!", obj.getId());
                        return obj;
                    }
                }
            }
            log.info("Bicycle {} has not been saved!", obj.getId());
            return null;

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка сохранения Bicycle", e);
        }
    }

    /**
     * Метод удаляет объект по переданному id, отсутствует проверка на null (осторожней),
     * в другом случае если удаление не удалось, кинет исключение IllegalStateException("Ошибка удаления obj", e);
     *
     * @param id
     * @return
     */
    @Override
    public boolean delete(Integer id) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(DELETE_BICYCLE)) {

            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            log.info("Bicycle {} has been deleted!", id);
            return rowsAffected > 0;

        } catch (SQLException e) {
            log.warn("Bicycle {} has not been deleted!", id);
            throw new IllegalStateException("Ошибка удаления Bicycle", e);
        }
    }

    /**
     * Метод осуществляет фильтрацию всех объектов находящихся в базе данных по переданному предикату и возвращает лист
     * с объектами удовлетворяющими критерии фильтрации, отсутствует проверка на null
     *
     * @param predicate
     * @return
     */
    @Override
    public List<Bicycle> filterBy(Predicate<Bicycle> predicate) {
        List<Bicycle> allBBicycles = getAll();
        log.info("filterBy");
        return allBBicycles.stream()
                .filter(predicate)
                .toList();
    }

    /**
     * Метод возвращающий все объекты класса которые хранятся в базе данных, если передача не удалась,
     * кинет ошибку IllegalStateException("Ошибка передачи всех obj", e);
     *
     * @return
     */
    @Override
    public List<Bicycle> getAll() {
        List<Bicycle> bicycles = new ArrayList<>();

        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_ALL_BICYCLES);
             final var resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Bicycle bicycle = Bicycle.builder()
                        .id(resultSet.getInt(ID))
                        .model(resultSet.getString(MODEL))
                        .price(resultSet.getDouble(PRICE))
                        .color(resultSet.getString(COLOR))
                        .build();
                bicycles.add(bicycle);
            }

        } catch (SQLException e) {
            log.warn("Ошибка передачи всех bicycle");
            throw new IllegalStateException("Ошибка передачи всех bicycle", e);
        }
        log.info("getAll");
        return bicycles;
    }

    @Override
    public boolean checkIfTableExists(String tableName) {
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(CHECK_TABLE)) {

            preparedStatement.setString(1, tableName.toLowerCase());
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                log.info("Table {} exists!", tableName);
                return resultSet.getBoolean(1);
            }

        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка существования таблицы", e);
        }
        log.info("Table {} no exists!", tableName);
        return false;
    }

    public Bicycle updateUserId(Integer bicycleId, Integer userId) {
        try (final var connection = ConnectionManager.getConnection()) {
            try (final var checkStmt = connection.prepareStatement(CHECK_BICYCLE_ID)) {
                checkStmt.setInt(1, bicycleId);
                try (final var rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        throw new IllegalStateException("bicycle with id " + bicycleId + " does not exist");
                    }
                }
            }

            try (final var preparedStatement = connection.prepareStatement(UPDATE_BICYCLE_USER)) {
                if (userId != null) {
                    preparedStatement.setInt(1, userId);
                } else {
                    preparedStatement.setNull(1, java.sql.Types.INTEGER);
                }
                preparedStatement.setInt(2, bicycleId);
                int rowsAffected = preparedStatement.executeUpdate();

                if (rowsAffected > 0) {
                    Bicycle updatedBicycle = get(bicycleId);

                    if (userId != null) {
                        var maybeUser = userDao.get(userId);
                        if (maybeUser != null) {
                            maybeUser.getListBicycle().add(updatedBicycle);
                        }
                    }
                    log.info("bicycle {} has been updated!", bicycleId);
                    return updatedBicycle;
                }
                log.warn("bicycle {} has not been updated!", bicycleId);
                return null;
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка обновления userId bicycle", e);
        }
    }

    public List<Bicycle> getAllByUserId(int userId) {
        List<Bicycle> bicycles = new ArrayList<>();
        try (final var connection = ConnectionManager.getConnection();
             final var preparedStatement = connection.prepareStatement(SELECT_BICYCLE_BY_USER_ID)) {

            preparedStatement.setInt(1, userId);
            final var resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                bicycles.add(Bicycle.builder()
                        .id(resultSet.getInt(ID))
                        .model(resultSet.getString(MODEL))
                        .price(resultSet.getDouble(PRICE))
                        .color(resultSet.getString(COLOR))
                        .userId(resultSet.getInt(USER_ID))
                        .build());
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка получения списка bicycles пользователя", e);
        }
        return bicycles;
    }
}

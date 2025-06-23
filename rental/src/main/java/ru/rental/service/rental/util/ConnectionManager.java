package ru.rental.service.rental.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {

    private static final String BD_URL = "spring.datasource.url";

    private static final String BD_USERNAME = "spring.datasource.username";

    private static final String BD_PASSWORD = "spring.datasource.password";

    private ConnectionManager() {
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    PropertiesUtil.getProperties(BD_URL),
                    PropertiesUtil.getProperties(BD_USERNAME),
                    PropertiesUtil.getProperties(BD_PASSWORD));
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка подключения к базе данных", e);
        }
    }
}

package ru.rental.service.bankcard.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionManager {

    @Value("${spring.datasource.url}")
    private String bdUrl;

    @Value("${spring.datasource.username}")
    private String bdUsername;

    @Value("${spring.datasource.password}")
    private String bdPassword;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(bdUrl, bdUsername, bdPassword);
        } catch (SQLException e) {
            throw new IllegalStateException("Ошибка подключения к базе данных", e);
        }
    }
}

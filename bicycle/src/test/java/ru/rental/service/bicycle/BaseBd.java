package ru.rental.service.bicycle;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.apache.logging.log4j.util.LoaderUtil.getClassLoader;

public class BaseBd {

    private static final Network NETWORK = Network.newNetwork();

    private static final PostgreSQLContainer postresqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.2-alpine3.21"))
            .withDatabaseName("postgres")
            .withUsername("admin")
            .withPassword("admin")
            .withReuse(true)
            .withExposedPorts(5432)
            .withNetwork(NETWORK);

    private static final GenericContainer userContainer = new GenericContainer<>("user:1.0.0")
            .withNetwork(NETWORK)
            .withExposedPorts(7873);

    private static final GenericContainer bicycleContainer = new GenericContainer<>("bicycle:1.0.0")
            .withNetwork(NETWORK)
            .withExposedPorts(7878);

    private static final GenericContainer bikeContainer = new GenericContainer<>("bike:1.0.0")
            .withNetwork(NETWORK)
            .withExposedPorts(7877);

    private static final GenericContainer carContainer = new GenericContainer<>("car:1.0.1")
            .withNetwork(NETWORK)
            .withExposedPorts(7876);

    private static final GenericContainer rentalContainer = new GenericContainer<>("rental:1.0.0")
            .withNetwork(NETWORK)
            .withExposedPorts(7874);

    private static final GenericContainer rentalServiceContainer = new GenericContainer<>("rental-service:1.0.0")
            .withNetwork(NETWORK)
            .withExposedPorts(8081);

    private static final GenericContainer bankcardContainer = new GenericContainer<>("bankcard:1.0.0")
            .withNetwork(NETWORK)
            .withExposedPorts(7875);

    static {
        postresqlContainer.start();
        try {
            Thread.sleep(10_000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        userContainer.withEnv("USER_DATASOURCE_URL", "jdbc:postgresql:/" + postresqlContainer.getContainerName() + ":5432/postgres");
        userContainer.withEnv("USER_DATASOURCE_USERNAME", postresqlContainer.getUsername());
        userContainer.withEnv("USER_DATASOURCE_PASSWORD", postresqlContainer.getPassword());
        userContainer.withEnv("BICYCLE_URL", bicycleContainer.getHost() + ":" + 7878);
        userContainer.withEnv("BIKE_URL", bikeContainer.getHost() + ":" + 7877);
        userContainer.withEnv("CAR_URL", carContainer.getHost() + ":" + 7876);
        userContainer.withEnv("BANKCARD_URL", bankcardContainer.getHost() + ":" + 7875);
        userContainer.start();

        bicycleContainer.withEnv("BICYCLE_DATASOURCE_URL", postresqlContainer.getJdbcUrl());
        bicycleContainer.withEnv("BICYCLE_DATASOURCE_USERNAME", postresqlContainer.getUsername());
        bicycleContainer.withEnv("BICYCLE_DATASOURCE_PASSWORD", postresqlContainer.getPassword());
        bicycleContainer.withEnv("USER_URL", userContainer.getHost() + ":" + 7873);
        bicycleContainer.start();

        bikeContainer.withEnv("BIKE_DATASOURCE_URL", postresqlContainer.getJdbcUrl());
        bikeContainer.withEnv("BIKE_DATASOURCE_USERNAME", postresqlContainer.getUsername());
        bikeContainer.withEnv("BIKE_DATASOURCE_PASSWORD", postresqlContainer.getPassword());
        bikeContainer.withEnv("USER_URL", userContainer.getHost() + ":" + 7873);
        bikeContainer.start();

        carContainer.withEnv("CAR_DATASOURCE_URL", postresqlContainer.getJdbcUrl());
        carContainer.withEnv("CAR_DATASOURCE_USERNAME", postresqlContainer.getUsername());
        carContainer.withEnv("CAR_DATASOURCE_PASSWORD", postresqlContainer.getPassword());
        carContainer.withEnv("USER_URL", userContainer.getHost() + ":" + 7873);
        carContainer.start();

        rentalContainer.withEnv("RENTAL_DATASOURCE_URL", postresqlContainer.getJdbcUrl());
        rentalContainer.withEnv("RENTAL_DATASOURCE_USERNAME", postresqlContainer.getUsername());
        rentalContainer.withEnv("RENTAL_DATASOURCE_PASSWORD", postresqlContainer.getPassword());
        rentalContainer.withEnv("USER_URL", userContainer.getHost() + ":" + 7873);
        rentalContainer.withEnv("BICYCLE_URL", bicycleContainer.getHost() + ":" + 7878);
        rentalContainer.withEnv("BIKE_URL", bikeContainer.getHost() + ":" + 7877);
        rentalContainer.withEnv("CAR_URL", carContainer.getHost() + ":" + 7876);
        rentalContainer.start();

        rentalServiceContainer.start();

    }

    @BeforeEach
    void initBd() {
        final var path = getClassLoader().getResource("initBd.sql").getPath();
        try (final var connection = getConnection();
             final var bufferedReader = new BufferedReader(new FileReader(path))) {
            var query = new StringBuilder();
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().startsWith("--- ")) {
                    continue;
                }

                query.append(line).append(" ");
                if (line.trim().endsWith(";")) {
                    connection.createStatement().execute(query.toString());
                    query = new StringBuilder();
                }
            }
        } catch (SQLException | FileNotFoundException e) {
            throw new IllegalStateException("Ошибка инициализации таблицы", e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    @AfterAll
//    public static void closeBd() {
//        postresqlContainer.stop();
//        userContainer.stop();
//        bicycleContainer.stop();
//        bicycleContainer.stop();
//        carContainer.stop();
//        rentalContainer.stop();
//        rentalServiceContainer.stop();
//    }

    @DynamicPropertySource
    static void registerPgProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postresqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postresqlContainer::getUsername);
        registry.add("spring.datasource.password", postresqlContainer::getPassword);

        registry.add("user.url", () -> "http://localhost:" + userContainer.getMappedPort(7873) +
                "/api/users");
    }

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
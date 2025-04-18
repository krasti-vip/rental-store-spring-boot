package ru.rental.service;

import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.BeforeEach;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import ru.rental.service.util.ConnectionManager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;

class BaseBd {

    private static final PostgreSQLContainer postresqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17.2-alpine3.21"))
            .withDatabaseName("postgres")
            .withUsername("admin")
            .withPassword("admin")
            .withReuse(true)
            .withExposedPorts(5432)
            .withCreateContainerCmdModifier(cmd -> cmd.withHostConfig(
                    new HostConfig().withPortBindings(new PortBinding(Ports.Binding.bindPort(2222), new ExposedPort(5432)))
            ));

    static {
        postresqlContainer.start();
    }

    @BeforeEach
    void initBd() {
        final var path = BikeEntityTest.class.getClassLoader().getResource("initBd.sql").getPath();
        try (final var connection = ConnectionManager.getConnection();
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
}

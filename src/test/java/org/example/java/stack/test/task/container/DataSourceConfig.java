package org.example.java.stack.test.task.container;

import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.r2dbc.R2dbcProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration
public class DataSourceConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    public JdbcDatabaseContainer<?> getJdbcDatabaseContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:10.2"))
                .waitingFor(Wait.forListeningPort());
    }

    @Bean
    @Primary
    public R2dbcProperties getR2dbcProperties(R2dbcProperties r2dbcProperties, JdbcDatabaseContainer<?> jdbcDatabaseContainer) {
        r2dbcProperties.setUrl(jdbcDatabaseContainer.getJdbcUrl().replaceAll("jdbc", "r2dbc"));
        r2dbcProperties.setUsername(jdbcDatabaseContainer.getUsername());
        r2dbcProperties.setPassword(jdbcDatabaseContainer.getPassword());

        return r2dbcProperties;
    }

    @Primary
    @Bean(initMethod = "migrate")
    public Flyway getFlyway(JdbcDatabaseContainer<?> jdbcDatabaseContainer) {
        return new Flyway(Flyway.configure()
                .baselineOnMigrate(true)
                .schemas("db/schema.sql")
                .dataSource(
                        jdbcDatabaseContainer.getJdbcUrl(),
                        jdbcDatabaseContainer.getUsername(),
                        jdbcDatabaseContainer.getPassword()
                )
        );
    }
}

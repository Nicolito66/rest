package org.example.rest.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
@ComponentScan
@Scope("singleton")
public class DatabaseConnector {
    private static String jdbcUrl = "jdbc:mysql://localhost:3306/db";
    private static String username = "test";
    private static String password = "testPassword99!";
    private DataSource dataSource;
    private DSLContext context;

    public DatabaseConnector() {
        dataSource = configureDataSource();
        context = DSL.using(dataSource, SQLDialect.MYSQL);
    }

    private static DataSource configureDataSource() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);

        return new HikariDataSource(config);
    }

    public DSLContext getContext() {
        return context;
    }

    public DataSource getDataSource() {
        return dataSource;
    }
}

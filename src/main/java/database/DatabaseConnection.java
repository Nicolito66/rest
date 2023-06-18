package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;

import javax.sql.DataSource;

public class DatabaseConnection {
    private static String jdbcUrl = "jdbc:mysql://localhost:3306/db";
    private static String username = "nicolas";
    private static String password = "Ficellejulien66!";
    private DataSource dataSource;
    private DSLContext context;

    public DatabaseConnection() {
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
}

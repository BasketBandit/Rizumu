package com.basketbandit.rizumu.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;

public class Connection {
    private static final Logger log = LoggerFactory.getLogger(Connection.class);
    private static final HikariConfig config = new HikariConfig("/config/hikari/database.properties");
    private static final HikariDataSource connectionPool = new HikariDataSource(config);

    public Connection() {
            log.trace("Invoking {}", this.getClass().getName());
        }

        /**
         * Gets the fresh database connection.
         * @return Connection.
         */
        public static java.sql.Connection getConnection() throws SQLException {
            return connectionPool.getConnection();
        }
}

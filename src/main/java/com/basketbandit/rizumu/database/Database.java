package com.basketbandit.rizumu.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database {
    private static final Logger log = LoggerFactory.getLogger(Database.class);

    /**
     * @param username {@link String}
     * @param password {@link String}
     * @return boolean
     */
    private static boolean login(String username, String password) {
        try(java.sql.Connection c = Connection.getConnection();
            PreparedStatement ps = c.prepareStatement("SELECT * FROM `users` WHERE `username` = ? AND `password` = ?")) {

            ps.setString(1, username);
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt()));
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch(Exception ex) {
            log.error("An error occurred while running the {} class, message: {}", Database.class.getSimpleName(), ex.getMessage(), ex);
            return false;
        }
    }
}

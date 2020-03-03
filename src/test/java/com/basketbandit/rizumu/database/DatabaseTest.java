package com.basketbandit.rizumu.database;

import org.junit.Test;

import java.sql.SQLException;

/**
 * Password should be hashed using BCrypt and a minimum of 12 rounds of generation for the salt.
 * All Database functions are written assuming BCrypt is used for password hashing.
 */
public class DatabaseTest {
    @Test
    public void connectionEstablished() throws SQLException {
        assert Connection.getConnection() != null;
    }

    @Test
    public void loginCorrect() {
        assert Database.login("josh", "test");
    }

    @Test
    public void loginIncorrect() {
        assert !Database.login("", "");
        assert !Database.login("josh", " test");
        assert !Database.login("josh", "test ");
        assert !Database.login("josh", "Test");
        assert !Database.login("josh", "wrong");
    }
}
package database;

import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @Test
    public void testDatabaseCRUDOperations() {
        DatabaseManager.initializeDatabase();

        String username = "test_user_noemhi";
        String password = "test_password";
        String role = "customer";

        try (Connection conn = DatabaseManager.getConnection()) {

            // DELETE first so the test can be run multiple times
            try (PreparedStatement cleanup = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?")) {
                cleanup.setString(1, username);
                cleanup.executeUpdate();
            }

            // CREATE / INSERT
            try (PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO users(username, password, role) VALUES (?, ?, ?)")) {
                insert.setString(1, username);
                insert.setString(2, password);
                insert.setString(3, role);

                int rowsInserted = insert.executeUpdate();
                assertEquals(1, rowsInserted);
            }

            // READ / SELECT
            try (PreparedStatement read = conn.prepareStatement(
                    "SELECT role FROM users WHERE username = ?")) {
                read.setString(1, username);

                ResultSet rs = read.executeQuery();

                assertTrue(rs.next());
                assertEquals("customer", rs.getString("role"));
            }

            // UPDATE
            try (PreparedStatement update = conn.prepareStatement(
                    "UPDATE users SET role = ? WHERE username = ?")) {
                update.setString(1, "admin");
                update.setString(2, username);

                int rowsUpdated = update.executeUpdate();
                assertEquals(1, rowsUpdated);
            }

            // VERIFY UPDATE
            try (PreparedStatement readUpdated = conn.prepareStatement(
                    "SELECT role FROM users WHERE username = ?")) {
                readUpdated.setString(1, username);

                ResultSet rs = readUpdated.executeQuery();

                assertTrue(rs.next());
                assertEquals("admin", rs.getString("role"));
            }

            // DELETE
            try (PreparedStatement delete = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?")) {
                delete.setString(1, username);

                int rowsDeleted = delete.executeUpdate();
                assertEquals(1, rowsDeleted);
            }

            // VERIFY DELETE
            try (PreparedStatement verifyDelete = conn.prepareStatement(
                    "SELECT * FROM users WHERE username = ?")) {
                verifyDelete.setString(1, username);

                ResultSet rs = verifyDelete.executeQuery();

                assertFalse(rs.next());
            }

        } catch (Exception e) {
            fail("CRUD test failed: " + e.getMessage());
        }
    }

    @Test
    public void testDatabaseTablesAreCreated() {
        DatabaseManager.initializeDatabase();

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            assertTrue(tableExists(stmt, "users"));
            assertTrue(tableExists(stmt, "products"));
            assertTrue(tableExists(stmt, "orders"));

        } catch (Exception e) {
            fail("Table creation test failed: " + e.getMessage());
        }
    }

    private boolean tableExists(Statement stmt, String tableName) throws Exception {
        ResultSet rs = stmt.executeQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'"
        );

        return rs.next();
    }
}
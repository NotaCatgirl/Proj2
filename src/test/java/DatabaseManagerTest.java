
import database.DatabaseManager;
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
            assertTrue(tableExists(stmt, "cart_item"));

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

    @Test
    public void testValidLogin (){
        DatabaseManager.signUp("rain","1234");
        String role = DatabaseManager.validLogin("rain","1234");
        assertEquals("user",role);
    }

    // Tests for the cart
    @Test
    public void testGetCartItemsReturnsSeededData() {
        DatabaseManager.initializeDatabase();

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "INSERT OR IGNORE INTO users (user_id, username, password, role) "
                            + "VALUES (999, 'cart_test_user', 'pass', 'user')");
            statement.executeUpdate(
                    "INSERT OR IGNORE INTO products (product_id, name, description, price, stock) "
                            + "VALUES (999, 'Test Product', 'A test', 5.00, 10)");

            statement.executeUpdate("DELETE FROM cart_item WHERE user_id = 999");

            DatabaseManager.addToCart(999, 999);
            DatabaseManager.addToCart(999, 999);

            java.util.List<model.CartItem> cartItems = DatabaseManager.getCartItems(999);

            assertEquals(1, cartItems.size());
            assertEquals(2, cartItems.get(0).getQuantity());
            assertEquals("Test Product", cartItems.get(0).getProductName());
            assertEquals(5.00, cartItems.get(0).getProductPrice());
            assertEquals(10.00, cartItems.get(0).getLineTotal());

            statement.executeUpdate("DELETE FROM cart_item WHERE user_id = 999");

        } catch (Exception exception) {
            fail("Seed verification failed: " + exception.getMessage());
        }
    }

    @Test
    public void testCheckoutMovesCartToOrdersAndClearsCart() {
        DatabaseManager.initializeDatabase();

        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "INSERT OR IGNORE INTO users (user_id, username, password, role) "
                            + "VALUES (888, 'checkout_test_user', 'pass', 'user')");
            statement.executeUpdate(
                    "INSERT OR IGNORE INTO products (product_id, name, description, price, stock) "
                            + "VALUES (888, 'Checkout Product', 'A test', 7.00, 10)");

            statement.executeUpdate("DELETE FROM cart_item WHERE user_id = 888");
            statement.executeUpdate("DELETE FROM orders WHERE user_id = 888");

            DatabaseManager.addToCart(888, 888);
            DatabaseManager.addToCart(888, 888);
            DatabaseManager.addToCart(888, 888);

            DatabaseManager.checkout(888);

            java.util.List<model.CartItem> cartItemsAfter = DatabaseManager.getCartItems(888);
            assertEquals(0, cartItemsAfter.size());

            try (PreparedStatement orderQuery = connection.prepareStatement(
                    "SELECT product_id, quantity, price_at_purchase FROM orders WHERE user_id = ?")) {
                orderQuery.setInt(1, 888);
                ResultSet orderResults = orderQuery.executeQuery();
                assertTrue(orderResults.next());
                assertEquals(888, orderResults.getInt("product_id"));
                assertEquals(3, orderResults.getInt("quantity"));
                assertEquals(7.00, orderResults.getDouble("price_at_purchase"));
                assertFalse(orderResults.next());
            }

            statement.executeUpdate("DELETE FROM orders WHERE user_id = 888");

        } catch (Exception exception) {
            fail("Checkout test failed: " + exception.getMessage());
        }
    }

    @Test
    public void testInvalidLogin(){
        String role = DatabaseManager.validLogin("Fake","test");
        assertEquals(null,role);
    }
}
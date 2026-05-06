import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static org.junit.jupiter.api.Assertions.*;

public class EcommerceDbTest {

    @Test
    void testInsertUpdateDelete() {

        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:ecommerce.db");

            // INSERT
            PreparedStatement insert = conn.prepareStatement(
                    "INSERT INTO users (username, password) VALUES (?, ?)"
            );

            insert.setString(1, "testuser");
            insert.setString(2, "1234");

            int inserted = insert.executeUpdate();

            assertEquals(1, inserted);

            // UPDATE
            PreparedStatement update = conn.prepareStatement(
                    "UPDATE users SET password = ? WHERE username = ?"
            );

            update.setString(1, "updated");
            update.setString(2, "testuser");

            int updated = update.executeUpdate();

            assertEquals(1, updated);

            // DELETE
            PreparedStatement delete = conn.prepareStatement(
                    "DELETE FROM users WHERE username = ?"
            );

            delete.setString(1, "testuser");

            int deleted = delete.executeUpdate();

            assertEquals(1, deleted);

            conn.close();

        } catch (Exception e) {
            fail();
        }
    }
}
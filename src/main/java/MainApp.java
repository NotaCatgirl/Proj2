import javafx.application.Application;
import javafx.stage.Stage;
import database.DatabaseManager;

// Imports for the testing data
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initializeDatabase();
        seedFakeData();
        SceneManager.init(primaryStage);

        primaryStage.setTitle("E-Commerce App");
        // Start on register (your scene for now)
       SceneManager.getInstance().navigateTo(SceneType.LOGIN);

        primaryStage.show();
    }

    // TODO: Remove this once product browse and login session tracking are working.
    // Temporary data so the cart scene has something to display for now.
    private void seedFakeData() {
        try (Connection connection = DatabaseManager.getConnection();
             Statement statement = connection.createStatement()) {

            statement.executeUpdate(
                    "INSERT OR IGNORE INTO users (user_id, username, password, role) "
                            + "VALUES (1, 'testuser', 'pass', 'user')");

            statement.executeUpdate(
                    "INSERT OR IGNORE INTO products (product_id, name, description, price, stock) "
                            + "VALUES (1, 'Coffee Mug', 'Ceramic 12oz mug', 8.99, 50)");
            statement.executeUpdate(
                    "INSERT OR IGNORE INTO products (product_id, name, description, price, stock) "
                            + "VALUES (2, 'Notebook', 'Spiral 100 pages', 4.50, 100)");
            statement.executeUpdate(
                    "INSERT OR IGNORE INTO products (product_id, name, description, price, stock) "
                            + "VALUES (3, 'Pen Pack', 'Black ink, 5 pack', 3.25, 200)");

            statement.executeUpdate("DELETE FROM cart_item WHERE user_id = 1");
            DatabaseManager.addToCart(1, 1);
            DatabaseManager.addToCart(1, 1);
            DatabaseManager.addToCart(1, 2);

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
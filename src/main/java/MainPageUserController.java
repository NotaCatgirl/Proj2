import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainPageUserController {

    public Scene buildScene() {

        Label titleLabel = new Label("Welcome " + DatabaseManager.getCurrentUser());
        titleLabel.setStyle("-fx-font-size: 34; -fx-font-weight: bold;");

        Button browseProducts = new Button("Browse Products");
        browseProducts.setStyle(
                "-fx-border-color: black; -fx-border-width: 2;" +
                        "-fx-background-color: white; -fx-border-radius: 12;" +
                        "-fx-min-width: 200; -fx-min-height: 80;" +
                        "-fx-font-size: 18"
        );

        Button orderHistory = new Button("Order History");
        orderHistory.setStyle(
                "-fx-border-color: black; -fx-border-width: 2;" +
                        "-fx-background-color: white; -fx-border-radius: 12;" +
                        "-fx-min-width: 200; -fx-min-height: 80;" +
                        "-fx-font-size: 18"
        );

        Button logOut = new Button("Log out");
        logOut.setStyle(
                "-fx-border-color: black; -fx-border-width: 2;" +
                        "-fx-background-color: white; -fx-border-radius: 12;" +
                        "-fx-min-width: 100; -fx-min-height: 40;" +
                        "-fx-font-size: 18"
        );

        // ✅ Navigation
        browseProducts.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.PRODUCT_BROWSE)
        );

        orderHistory.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.ORDER_HISTORY)
        );

        logOut.setOnAction(e -> {
            DatabaseManager.setUser(null);
            SceneManager.getInstance().navigateTo(SceneType.LOGIN);
        });

        VBox root = new VBox(20, titleLabel, browseProducts, orderHistory, logOut);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.CENTER);

        return new Scene(root, 400, 600);
    }
}
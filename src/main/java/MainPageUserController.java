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
        titleLabel.setStyle("-fx-font-size: 34;" + "-fx-font-weight: bold;");
        titleLabel.setTranslateY(-100);
        titleLabel.translateYProperty();

        Button browseProducts = new Button("Browse Products");
        browseProducts.setStyle("-fx-border-color: black;" + "-fx-border-width: 2;"
                + "-fx-background-color: white;" + "-fx-border-radius: 12;" + "-fx-min-width: 200;"
                + "-fx-min-height: 80;" + "-fx-translate-y: -10;" + "-fx-font-size: 18");


        Button orderHistory = new Button("Order History");
        orderHistory.setStyle("-fx-border-color: black;" + "-fx-border-width: 2;"
                + "-fx-background-color: white;" + "-fx-border-radius: 12;" + "-fx-min-width: 200;"
                + "-fx-min-height: 80;" + "-fx-translate-y: -10;" + "-fx-font-size: 18");


        browseProducts.setOnAction(e -> {
            SceneManager.getInstance().navigateTo(SceneType.PRODUCT_BROWSE);
        });

        orderHistory.setOnAction(e -> {
            SceneManager.getInstance().navigateTo(SceneType.ORDER_HISTORY);
        });

        VBox vbox1 = new VBox(12, titleLabel, browseProducts, orderHistory);
        vbox1.setPadding(new Insets(30));
        vbox1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox1, 350, 700);
        return scene;
    }
}

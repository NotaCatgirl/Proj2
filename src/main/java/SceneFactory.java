import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class SceneFactory {

    private final Stage stage;

    public SceneFactory(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene(SceneType type) {
        return switch (type) {
            case LOGIN -> buildLoginScene();
            case REGISTER -> buildRegisterScene();
            case PRODUCT_BROWSE -> buildProductScene();
            case CART -> buildCartScene();
            case ORDER_HISTORY -> buildOrderHistoryScene();
            case ADMIN -> buildAdminScene();
        };
    }



    private Scene buildRegisterScene() {
        Label label = new Label("Register Scene (Noemhi)");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }

    private Scene buildOrderHistoryScene() {
        Label label = new Label("Order History Scene (Noemhi)");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }



    private Scene buildLoginScene() {
        return placeholder("Login Scene (Alexis)");
    }

    private Scene buildProductScene() {
        return placeholder("Product Browse Scene");
    }

    private Scene buildCartScene() {
        return placeholder("Cart Scene");
    }

    private Scene buildAdminScene() {
        return placeholder("Admin Scene");
    }


    private Scene placeholder(String text) {
        Label label = new Label(text + " - Coming Soon");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }


    public void switchScene(SceneType type) {
        stage.setScene(createScene(type));
    }
}
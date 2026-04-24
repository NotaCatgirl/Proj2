import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneFactory {

    public static Scene createScene(SceneType type) {
        return switch (type) {
            case LOGIN -> new LoginController().buildScene();
            case SIGNUP -> new SignUpController().buildScene();
            case REGISTER -> buildRegisterScene();
            case PRODUCT_BROWSE -> buildProductScene();
            case CART -> buildCartScene();
            case ORDER_HISTORY -> buildOrderHistoryScene();
            case ADMIN -> buildAdminScene();
        };
    }


    private static Scene buildRegisterScene() {
        Label label = new Label("Register Scene (Noemhi)");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }

    private static Scene buildOrderHistoryScene() {
        Label label = new Label("Order History Scene (Noemhi)");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }


    private static Scene buildProductScene() {
        return placeholder("Product Browse Scene");
    }

    private static Scene buildCartScene() {
        return placeholder("Cart Scene");
    }

    private static Scene buildAdminScene() {
        return placeholder("Admin Scene");
    }


    private static Scene placeholder(String text) {
        Label label = new Label(text + " - Coming Soon");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }

//
//    public static void switchScene(SceneType type) {
//        stage.setScene(createScene(type));
//    }
}
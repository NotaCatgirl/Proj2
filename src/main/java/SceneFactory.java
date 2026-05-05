import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class SceneFactory {

    public static Scene createScene(SceneType type) {
        return switch (type) {
            case LOGIN -> new LoginController().buildScene();
            case SIGNUP -> new SignUpController().buildScene();
            case MAIN_PAGE_USER -> new MainPageUserController().buildScene();
            case PRODUCT_BROWSE -> buildProductScene();
            case CART -> buildCartScene();
            case ORDER_HISTORY -> buildOrderHistoryScene();
            case ADMIN -> new AdminController().buildScene();
        };
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

}
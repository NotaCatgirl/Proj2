import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class SceneFactory {

    public static Scene createScene(SceneType type) {
        return switch (type) {
            case LOGIN -> new LoginController().buildScene();
            case SIGNUP -> new SignUpController().buildScene();
            case MAIN_PAGE_USER -> new MainPageUserController().buildScene();
            case PRODUCT_BROWSE -> new ProductBrowseController().buildScene();
            case ADMIN -> new AdminController().buildScene();
            case CART -> new CartController().buildScene();
            case ORDER_HISTORY -> new OrderHistoryController().buildScene();

            // important safety fallback
            default -> throw new IllegalArgumentException("Unknown SceneType: " + type);
        };
    }

    private static Scene placeholder(String text) {
        Label label = new Label(text + " - Coming Soon");
        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }
}
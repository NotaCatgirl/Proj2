package ui;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class SceneFactory {

    public static Scene createScene(SceneType type) {
        return switch (type) {
            case LOGIN -> new LoginController().buildScene();
            case SIGNUP -> new SignUpController().buildScene();
            case PRODUCT_BROWSE -> new ProductBrowseController().buildScene();
            case CART -> placeholder("Cart Scene");
            case ORDER_HISTORY -> placeholder("Order History Scene");
            case ADMIN -> placeholder("Admin Scene");
        };
    }

    private static Scene placeholder(String text) {
        Label label = new Label(text + " - Coming Soon");
        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }
}
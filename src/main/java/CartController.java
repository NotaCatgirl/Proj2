import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CartItem;

import java.util.List;

public class CartController {

    // TODO: Replace with logged-in user ID this is hardcoded for now.
    private static final int currentUserId = 1;

    private VBox cartContent;

    public Scene buildScene() {
        Label titleLabel = new Label("Your Cart");

        cartContent = new VBox(10);
        cartContent.setAlignment(Pos.CENTER);
        renderCart();

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            SceneManager.getInstance().navigateTo(SceneType.PRODUCT_BROWSE);
        });

        VBox rootLayout = new VBox(15, titleLabel, cartContent, backButton);
        rootLayout.setPadding(new Insets(30));
        rootLayout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(rootLayout, 400, 600);
        return scene;
    }

    private void renderCart() {
        cartContent.getChildren().clear();
        List<CartItem> cartItems = DatabaseManager.getCartItems(currentUserId);

        if (cartItems.isEmpty()) {
            renderEmptyCart();
        } else {
            renderItems(cartItems);
        }
    }

    private void renderEmptyCart() {
        Label emptyMessageLabel = new Label("Your cart is empty!");

        Button browseProductsButton = new Button("Browse Products");
        browseProductsButton.setOnAction(event -> {
            SceneManager.getInstance().navigateTo(SceneType.PRODUCT_BROWSE);
        });

        cartContent.getChildren().addAll(emptyMessageLabel, browseProductsButton);
    }

    private void renderItems(List<CartItem> cartItems) {
        double cartTotal = 0;

        for (CartItem cartItem : cartItems) {
            HBox itemRow = buildItemRow(cartItem);
            cartContent.getChildren().add(itemRow);
            cartTotal = cartTotal + cartItem.getLineTotal();
        }

        Label totalLabel = new Label(String.format("Total: $%.2f", cartTotal));
        cartContent.getChildren().add(totalLabel);
    }

    private HBox buildItemRow(CartItem cartItem) {
        Label nameLabel = new Label(cartItem.getProductName());
        Label priceLabel = new Label(String.format("$%.2f", cartItem.getProductPrice()));
        Label quantityLabel = new Label("Qty: " + cartItem.getQuantity());
        Label lineTotalLabel = new Label(String.format("$%.2f", cartItem.getLineTotal()));

        HBox row = new HBox(15, nameLabel, priceLabel, quantityLabel, lineTotalLabel);
        row.setAlignment(Pos.CENTER);
        return row;
    }
}
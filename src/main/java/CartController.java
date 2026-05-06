import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CartItem;
import javafx.scene.control.Alert;

import java.util.List;

public class CartController {



    private VBox cartContent;

    public Scene buildScene() {
        Label titleLabel = new Label("Your Cart");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

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

        Scene scene = new Scene(rootLayout, 500, 600);
        return scene;
    }

    private void renderCart() {
        cartContent.getChildren().clear();
        List<CartItem> cartItems = DatabaseManager.getCartItems(DatabaseManager.getCurrentUserId());
        if (cartItems.isEmpty()) {
            renderEmptyCart();
        } else {
            renderItems(cartItems);
        }
    }

    private void renderEmptyCart() {
        Label emptyMessageLabel = new Label("Your cart is empty!");
        emptyMessageLabel.setStyle("-fx-font-size: 16px;");

        Button browseProductsButton = new Button("Browse Products");
        browseProductsButton.setOnAction(event -> {
            SceneManager.getInstance().navigateTo(SceneType.PRODUCT_BROWSE);
        });

        cartContent.getChildren().addAll(emptyMessageLabel, browseProductsButton);
    }

    private void renderItems(List<CartItem> cartItems) {
        HBox headerRow = buildHeaderRow();
        cartContent.getChildren().add(headerRow);
        cartContent.getChildren().add(new Separator());

        double cartTotal = 0;
        for (int index = 0; index < cartItems.size(); index++) {
            CartItem cartItem = cartItems.get(index);
            HBox itemRow = buildItemRow(cartItem);
            cartContent.getChildren().add(itemRow);
            if (index < cartItems.size() - 1) {
                cartContent.getChildren().add(new Separator());
            }
            cartTotal = cartTotal + cartItem.getLineTotal();
        }

        cartContent.getChildren().add(new Separator());

        Label totalLabel = new Label(String.format("Total: $%.2f", cartTotal));
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        cartContent.getChildren().add(totalLabel);

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        checkoutButton.setOnAction(event -> {
            DatabaseManager.checkout(DatabaseManager.getCurrentUserId());

            Alert orderAlert = new Alert(Alert.AlertType.INFORMATION);
            orderAlert.setTitle("Order");
            orderAlert.setHeaderText(null);
            orderAlert.setContentText("Order placed successfully!");
            orderAlert.showAndWait();

            renderCart();
        });
        cartContent.getChildren().add(checkoutButton);
    }

    private HBox buildHeaderRow() {
        Label itemHeader = new Label("Item");
        itemHeader.setPrefWidth(130);
        itemHeader.setStyle("-fx-font-weight: bold;");

        Label priceHeader = new Label("Price");
        priceHeader.setPrefWidth(60);
        priceHeader.setStyle("-fx-font-weight: bold;");

        Label quantityHeader = new Label("Quantity");
        quantityHeader.setPrefWidth(110);
        quantityHeader.setAlignment(Pos.CENTER);
        quantityHeader.setStyle("-fx-font-weight: bold;");

        Label subtotalHeader = new Label("Subtotal");
        subtotalHeader.setPrefWidth(70);
        subtotalHeader.setStyle("-fx-font-weight: bold;");

        HBox header = new HBox(10, itemHeader, priceHeader, quantityHeader, subtotalHeader);
        header.setAlignment(Pos.CENTER);
        return header;
    }

    private HBox buildItemRow(CartItem cartItem) {
        Label nameLabel = new Label(cartItem.getProductName());
        nameLabel.setPrefWidth(130);

        Label priceLabel = new Label(String.format("$%.2f", cartItem.getProductPrice()));
        priceLabel.setPrefWidth(60);

        Button minusButton = new Button("-");
        minusButton.setPrefWidth(30);

        Label quantityLabel = new Label(String.valueOf(cartItem.getQuantity()));
        quantityLabel.setPrefWidth(30);
        quantityLabel.setAlignment(Pos.CENTER);

        Button plusButton = new Button("+");
        plusButton.setPrefWidth(30);

        Label lineTotalLabel = new Label(String.format("$%.2f", cartItem.getLineTotal()));
        lineTotalLabel.setPrefWidth(70);

        Button removeButton = new Button("X");
        removeButton.setPrefWidth(30);

        minusButton.setOnAction(event -> {
            if (cartItem.getQuantity() <= 1) {
                DatabaseManager.removeCartItem(cartItem.getCartItemId());
            } else {
                DatabaseManager.updateCartQuantity(
                        cartItem.getCartItemId(),
                        cartItem.getQuantity() - 1);
            }
            renderCart();
        });

        plusButton.setOnAction(event -> {
            DatabaseManager.updateCartQuantity(
                    cartItem.getCartItemId(),
                    cartItem.getQuantity() + 1);
            renderCart();
        });

        removeButton.setOnAction(event -> {
            DatabaseManager.removeCartItem(cartItem.getCartItemId());
            renderCart();
        });

        HBox row = new HBox(10, nameLabel, priceLabel, minusButton,
                quantityLabel, plusButton, lineTotalLabel, removeButton);
        row.setAlignment(Pos.CENTER);
        return row;
    }
}
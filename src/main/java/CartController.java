import database.DatabaseManager;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.CartItem;

public class CartController {

    private ObservableList<CartItem> cartItems;
    private SimpleDoubleProperty cartTotal;

    public Scene buildScene() {
        cartItems = FXCollections.observableArrayList(
                DatabaseManager.getCartItems(DatabaseManager.getCurrentUserId()));

        cartTotal = new SimpleDoubleProperty(0);
        recalculateTotal();
        cartItems.addListener((ListChangeListener<CartItem>) change -> recalculateTotal());

        Label titleLabel = new Label("Your Cart");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        HBox headerRow = buildHeaderRow();
        headerRow.visibleProperty().bind(Bindings.isNotEmpty(cartItems));
        headerRow.managedProperty().bind(headerRow.visibleProperty());

        ListView<CartItem> cartListView = new ListView<>(cartItems);
        cartListView.setCellFactory(list -> new CartItemCell());
        cartListView.setPrefHeight(400);
        cartListView.setPlaceholder(buildEmptyPlaceholder());

        Label totalLabel = new Label();
        totalLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        totalLabel.textProperty().bind(cartTotal.asString("Total: $%.2f"));
        totalLabel.visibleProperty().bind(Bindings.isNotEmpty(cartItems));
        totalLabel.managedProperty().bind(totalLabel.visibleProperty());

        Button checkoutButton = new Button("Checkout");
        checkoutButton.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        checkoutButton.setOnAction(event -> handleCheckout());
        checkoutButton.visibleProperty().bind(Bindings.isNotEmpty(cartItems));
        checkoutButton.managedProperty().bind(checkoutButton.visibleProperty());

        Button backButton = new Button("Back");
        backButton.setOnAction(event -> {
            SceneManager.getInstance().navigateTo(SceneType.PRODUCT_BROWSE);
        });

        VBox rootLayout = new VBox(15, titleLabel, headerRow, cartListView,
                totalLabel, checkoutButton, backButton);
        rootLayout.setPadding(new Insets(30));
        rootLayout.setAlignment(Pos.CENTER);

        return new Scene(rootLayout, 500, 600);
    }

    private void recalculateTotal() {
        double sum = 0;
        for (CartItem cartItem : cartItems) {
            sum = sum + cartItem.getLineTotal();
        }
        cartTotal.set(sum);
    }

    private void reloadCartFromDatabase() {
        cartItems.setAll(DatabaseManager.getCartItems(DatabaseManager.getCurrentUserId()));
    }

    private void handleCheckout() {
        DatabaseManager.checkout(DatabaseManager.getCurrentUserId());

        Alert orderAlert = new Alert(Alert.AlertType.INFORMATION);
        orderAlert.setTitle("Order");
        orderAlert.setHeaderText(null);
        orderAlert.setContentText("Order placed successfully!");
        orderAlert.showAndWait();

        cartItems.clear();
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

    private VBox buildEmptyPlaceholder() {
        Label emptyMessageLabel = new Label("Your cart is empty!");
        emptyMessageLabel.setStyle("-fx-font-size: 16px;");

        Button browseProductsButton = new Button("Browse Products");
        browseProductsButton.setOnAction(event -> {
            SceneManager.getInstance().navigateTo(SceneType.PRODUCT_BROWSE);
        });

        VBox placeholder = new VBox(10, emptyMessageLabel, browseProductsButton);
        placeholder.setAlignment(Pos.CENTER);
        return placeholder;
    }

    private class CartItemCell extends ListCell<CartItem> {
        @Override
        protected void updateItem(CartItem cartItem, boolean empty) {
            super.updateItem(cartItem, empty);
            if (empty || cartItem == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(buildItemRow(cartItem));
            }
        }
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
                cartItems.remove(cartItem);
            } else {
                DatabaseManager.updateCartQuantity(
                        cartItem.getCartItemId(), cartItem.getQuantity() - 1);
                reloadCartFromDatabase();
            }
        });

        plusButton.setOnAction(event -> {
            DatabaseManager.updateCartQuantity(
                    cartItem.getCartItemId(), cartItem.getQuantity() + 1);
            reloadCartFromDatabase();
        });

        removeButton.setOnAction(event -> {
            DatabaseManager.removeCartItem(cartItem.getCartItemId());
            cartItems.remove(cartItem);
        });

        HBox row = new HBox(10, nameLabel, priceLabel, minusButton,
                quantityLabel, plusButton, lineTotalLabel, removeButton);
        row.setAlignment(Pos.CENTER);
        return row;
    }
}
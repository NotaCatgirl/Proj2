import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import database.DatabaseManager;
import database.Product;

import java.util.List;

public class AdminController {

    private VBox root;

    public Scene buildScene() {
        root = new VBox();
        root.setPadding(new Insets(20));
        root.setSpacing(15);
        root.setAlignment(Pos.CENTER);

        showDashboard();

        return new Scene(root, 800, 600);
    }

    private void showDashboard() {
        Label title = new Label("Admin Dashboard");

        Button viewOrdersBtn = new Button("View All Orders");
        Button manageUsersBtn = new Button("Manage Users");
        Button manageProductBtn = new Button("Add / Remove Products");
        Button logoutBtn = new Button("Logout");

        viewOrdersBtn.setOnAction(e -> showOrders());
        manageUsersBtn.setOnAction(e -> showUsers());
        manageProductBtn.setOnAction(e -> showProducts());

        logoutBtn.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.LOGIN)
        );

        root.getChildren().setAll(
                title,
                viewOrdersBtn,
                manageUsersBtn,
                manageProductBtn,
                logoutBtn
        );
    }

    private void showOrders() {
        Label title = new Label("All Orders");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by User ID");

        Button searchBtn = new Button("Search");
        Button backBtn = new Button("Back");

        ListView<String> ordersList = new ListView<>();
        Label emptyLabel = new Label("No orders found");

        ordersList.getItems().addAll(DatabaseManager.getAllOrders());

        searchBtn.setOnAction(e -> {
            String input = searchField.getText().toLowerCase();

            List<String> allOrders = DatabaseManager.getAllOrders();
            ordersList.getItems().clear();

            for (String order : allOrders) {
                if (order.toLowerCase().contains(input)) {
                    ordersList.getItems().add(order);
                }
            }

            if (ordersList.getItems().isEmpty()) {
                root.getChildren().setAll(title, searchField, searchBtn, emptyLabel, backBtn);
            } else {
                root.getChildren().setAll(title, searchField, searchBtn, ordersList, backBtn);
            }
        });

        backBtn.setOnAction(e -> showDashboard());

        root.getChildren().setAll(
                title,
                searchField,
                searchBtn,
                ordersList,
                backBtn
        );
    }

    private void showUsers() {
        Label title = new Label("All Users");

        TextField searchField = new TextField();
        searchField.setPromptText("Search by username or user ID");

        Button searchBtn = new Button("Search");
        Button backBtn = new Button("Back");

        ListView<String> usersList = new ListView<>();
        Label emptyLabel = new Label("No users found");

        usersList.getItems().addAll(DatabaseManager.getAllUsers());

        searchBtn.setOnAction(e -> {
            String input = searchField.getText().toLowerCase();

            List<String> allUsers = DatabaseManager.getAllUsers();
            usersList.getItems().clear();

            for (String user : allUsers) {
                if (user.toLowerCase().contains(input)) {
                    usersList.getItems().add(user);
                }
            }

            if (usersList.getItems().isEmpty()) {
                root.getChildren().setAll(title, searchField, searchBtn, emptyLabel, backBtn);
            } else {
                root.getChildren().setAll(title, searchField, searchBtn, usersList, backBtn);
            }
        });

        backBtn.setOnAction(e -> showDashboard());

        if (usersList.getItems().isEmpty()) {
            root.getChildren().setAll(title, searchField, searchBtn, emptyLabel, backBtn);
        } else {
            root.getChildren().setAll(title, searchField, searchBtn, usersList, backBtn);
        }
    }

    private void showProducts() {
        Label title = new Label("Manage Products");

        TextField nameField = new TextField();
        nameField.setPromptText("Product Name");

        TextField descField = new TextField();
        descField.setPromptText("Description");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        TextField stockField = new TextField();
        stockField.setPromptText("Stock");

        Button addBtn = new Button("Add Product");
        Button deleteBtn = new Button("Delete by ID");
        Button backBtn = new Button("Back");

        ListView<String> productList = new ListView<>();
        Label messageLabel = new Label();

        loadProducts(productList);

        addBtn.setOnAction(e -> {
            try {
                String name = nameField.getText();
                String desc = descField.getText();
                double price = Double.parseDouble(priceField.getText());
                int stock = Integer.parseInt(stockField.getText());

                DatabaseManager.addProduct(name, desc, price, stock);

                loadProducts(productList);
                messageLabel.setText("Product added!");
            } catch (Exception ex) {
                messageLabel.setText("Invalid input");
            }
        });

        deleteBtn.setOnAction(e -> {
            try {
                int id = Integer.parseInt(nameField.getText());

                DatabaseManager.deleteProduct(id);

                loadProducts(productList);
                messageLabel.setText("Product deleted!");
            } catch (Exception ex) {
                messageLabel.setText("Enter valid ID");
            }
        });

        backBtn.setOnAction(e -> showDashboard());

        root.getChildren().setAll(
                title,
                nameField,
                descField,
                priceField,
                stockField,
                addBtn,
                deleteBtn,
                productList,
                messageLabel,
                backBtn
        );
    }

    private void loadProducts(ListView<String> productList) {
        productList.getItems().clear();

        for (Product product : DatabaseManager.getAllProducts()) {
            productList.getItems().add(
                    "ID: " + product.getProductId()
                            + " | " + product.getName()
                            + " | $" + product.getPrice()
                            + " | Stock: " + product.getStock()
            );
        }
    }
}
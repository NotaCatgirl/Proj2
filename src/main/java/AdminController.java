import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

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
        Button addProductBtn = new Button("Add / Remove Products");
        Button logoutBtn = new Button("Logout");

        viewOrdersBtn.setOnAction(e -> showOrders());

        logoutBtn.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.LOGIN)
        );

        root.getChildren().setAll(
                title,
                viewOrdersBtn,
                manageUsersBtn,
                addProductBtn,
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

        // TEMP placeholder data (replace later with DB)
        ordersList.getItems().addAll(
                "User 1 - Order #101",
                "User 2 - Order #102",
                "User 3 - Order #103"
        );

        searchBtn.setOnAction(e -> {
            String input = searchField.getText();

            ordersList.getItems().clear();

            if (input.isEmpty()) {
                ordersList.getItems().addAll(
                        "User 1 - Order #101",
                        "User 2 - Order #102",
                        "User 3 - Order #103"
                );
            } else if (input.equals("1")) {
                ordersList.getItems().add("User 1 - Order #101");
            } else {
                ordersList.getItems().clear();
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
}
import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;


import java.util.List;

public class OrderHistoryController {

    public Scene buildScene() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Order History");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold;");

        ListView<String> orderList = new ListView<>();
        orderList.setPrefHeight(400);

        Label emptyLabel = new Label("No orders found.");

        List<String> orders = DatabaseManager.getAllOrders();

        if (orders.isEmpty()) {
            root.getChildren().addAll(title, emptyLabel);
        } else {
            orderList.getItems().addAll(orders);
            root.getChildren().addAll(title, orderList);
        }

        Button backButton = new Button("Back to Main Page");
        backButton.setOnAction(e ->
                SceneManager.getInstance().navigateTo(SceneType.MAIN_PAGE_USER)
        );

        root.getChildren().add(backButton);

        return new Scene(root, 800, 600);
    }
}
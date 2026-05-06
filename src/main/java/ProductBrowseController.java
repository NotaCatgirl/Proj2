import database.Product;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import database.DatabaseManager;

public class ProductBrowseController {

    public Scene buildScene() {
        VBox root = new VBox(20);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label title = new Label("Browse Products");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Button viewCartButton = new Button("View Cart");
        viewCartButton.setOnAction(event -> {
            SceneManager.getInstance().navigateTo(SceneType.CART);
        });

        TilePane productGrid = new TilePane();
        productGrid.setHgap(20);
        productGrid.setVgap(20);
        productGrid.setPadding(new Insets(20));
        productGrid.setPrefColumns(3);

        for (Product product : DatabaseManager.getAllProducts()) {
            productGrid.getChildren().add(createProductCard(product));
        }

        ScrollPane scrollPane = new ScrollPane(productGrid);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(550);

        root.getChildren().addAll(title, viewCartButton, scrollPane);

        return new Scene(root, 900, 650);
    }

    private VBox createProductCard(Product product) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(15));
        card.setAlignment(Pos.CENTER);
        card.setPrefSize(220, 280);
        card.setStyle(
                "-fx-background-color: white;" +
                        "-fx-border-color: lightgray;" +
                        "-fx-border-radius: 10;" +
                        "-fx-background-radius: 10;" +
                        "-fx-cursor: hand;"
        );

        ImageView imageView = createProductImage(product, 150, 120);

        Label name = new Label(product.getName());
        name.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label price = new Label(String.format("$%.2f", product.getPrice()));

        Button viewButton = new Button("View Details");

        card.setOnMouseClicked(e -> {
            Stage stage = (Stage) card.getScene().getWindow();
            stage.setScene(buildProductDetailScene(product));
        });

        viewButton.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(buildProductDetailScene(product));
        });

        card.getChildren().addAll(imageView, name, price, viewButton);

        return card;
    }

    private Scene buildProductDetailScene(Product product) {
        VBox root = new VBox(20);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        ImageView imageView = createProductImage(product, 300, 250);

        Label name = new Label(product.getName());
        name.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        Label price = new Label(String.format("Price: $%.2f", product.getPrice()));
        Label stock = new Label("Stock: " + product.getStock());

        Label description = new Label(product.getDescription());
        description.setWrapText(true);
        description.setMaxWidth(500);

        Button addToCart = new Button("Add to Cart");
        addToCart.setOnAction(e -> {
            DatabaseManager.addToCart(DatabaseManager.getCurrentUserId(), product.getProductId());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Cart");
            alert.setHeaderText(null);
            alert.setContentText(product.getName() + " added to cart.");
            alert.showAndWait();
        });

        Button backButton = new Button("Back to Products");
        backButton.setOnAction(e -> {
            Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            stage.setScene(buildScene());
        });

        root.getChildren().addAll(
                imageView,
                name,
                price,
                stock,
                description,
                addToCart,
                backButton
        );

        return new Scene(root, 900, 650);
    }

    private ImageView createProductImage(Product product, int width, int height) {
        ImageView imageView = new ImageView();
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        imageView.setPreserveRatio(true);

        try {
            String imagePath = product.getImagePath();
            System.out.println("Trying to load image for " + product.getName() + ": " + imagePath);

            if (imagePath == null || imagePath.isBlank()) {
                System.out.println("Image path is missing for " + product.getName());
                return imageView;
            }

            var imageUrl = ProductBrowseController.class.getResource(imagePath);

            if (imageUrl == null) {
                System.out.println("Image file was NOT found at: " + imagePath);
                return imageView;
            }

            Image image = new Image(imageUrl.toExternalForm());
            imageView.setImage(image);

        } catch (Exception e) {
            System.out.println("FAILED to load image: " + product.getImagePath());
            e.printStackTrace();
        }

        return imageView;
    }
}
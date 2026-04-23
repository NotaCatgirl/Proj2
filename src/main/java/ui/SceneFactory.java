package ui;

import database.UserDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Session;
import model.User;


public class SceneFactory {

    private final Stage stage;
    private final UserDAO userDAO;

    public SceneFactory(Stage stage) {
        this.stage = stage;
        this.userDAO = new UserDAO();
    }

    public Scene createScene(SceneType type) {
        return switch (type) {
            case LOGIN -> buildLoginScene();
            case REGISTER -> buildRegisterScene();
            case PRODUCT_BROWSE -> buildProductScene();
            case CART -> buildCartScene();
            case ORDER_HISTORY -> buildOrderHistoryScene();
            case ADMIN -> buildAdminScene();
        };
    }

    private Scene buildRegisterScene() {
        Label titleLabel = new Label("Register");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label messageLabel = new Label();

        Button registerButton = new Button("Register");
        Button goToLoginButton = new Button("Go to Login");

        registerButton.setOnAction(e -> {
            String username = usernameField.getText().trim();
            String password = passwordField.getText().trim();

            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Username and password cannot be empty.");
                return;
            }

            if (userDAO.usernameExists(username)) {
                messageLabel.setText("Username already exists.");
                return;
            }

            boolean success = userDAO.registerUser(username, password, "user");

            if (success) {
                messageLabel.setText("Registration successful! Please log in.");
                usernameField.clear();
                passwordField.clear();
            } else {
                messageLabel.setText("Registration failed.");
            }
        });

        goToLoginButton.setOnAction(e ->
                stage.setScene(createScene(SceneType.LOGIN))
        );

        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(
                titleLabel,
                usernameField,
                passwordField,
                registerButton,
                goToLoginButton,
                messageLabel
        );

        return new Scene(root, 800, 600);
    }


    private Scene buildLoginScene() {
            Label titleLabel = new Label("Login");
            titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

            TextField usernameField = new TextField();
            usernameField.setPromptText("Enter username");

            PasswordField passwordField = new PasswordField();
            passwordField.setPromptText("Enter password");

            Label messageLabel = new Label();

            Button loginButton = new Button("Login");
            Button goToRegisterButton = new Button("Go to Register");

            loginButton.setOnAction(e -> {
                String username = usernameField.getText().trim();
                String password = passwordField.getText().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Username and password cannot be empty.");
                    return;
                }

                User user = userDAO.loginUser(username, password);

                if (user != null) {
                    Session.setCurrentUser(user);
                    messageLabel.setText("Login successful!");

                    if (user.getRole().equalsIgnoreCase("admin")) {
                        stage.setScene(createScene(SceneType.ADMIN));
                    } else {
                        stage.setScene(createScene(SceneType.PRODUCT_BROWSE));
                    }
                } else {
                    messageLabel.setText("Invalid username or password.");
                }
            });

            goToRegisterButton.setOnAction(e ->
                    stage.setScene(createScene(SceneType.REGISTER))
            );

            VBox root = new VBox(15);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.CENTER);
            root.getChildren().addAll(
                    titleLabel,
                    usernameField,
                    passwordField,
                    loginButton,
                    goToRegisterButton,
                    messageLabel
            );

            return new Scene(root, 800, 600);
        }


    private Scene buildProductScene() {
        Label label = new Label("Product Browse Scene");
        VBox root = new VBox(label);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }

    private Scene buildCartScene() {
        Label label = new Label("Cart Scene");
        VBox root = new VBox(label);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }

    private Scene buildOrderHistoryScene() {
        Label label = new Label("Order History Scene");
        VBox root = new VBox(label);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }

    private Scene buildAdminScene() {
        Label label = new Label("Admin Scene");
        VBox root = new VBox(label);
        root.setAlignment(Pos.CENTER);
        return new Scene(root, 800, 600);
    }
}
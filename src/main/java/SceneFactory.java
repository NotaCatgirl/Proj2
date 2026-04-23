import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class SceneFactory {

    private final Stage stage;

    public SceneFactory(Stage stage) {
        this.stage = stage;
    }

    public Scene createScene(SceneType type) {
        return switch (type) {
            case LOGIN -> buildLoginScene();
            case SIGNUP -> buildSignUpScene();
            case REGISTER -> buildRegisterScene();
            case PRODUCT_BROWSE -> buildProductScene();
            case CART -> buildCartScene();
            case ORDER_HISTORY -> buildOrderHistoryScene();
            case ADMIN -> buildAdminScene();
        };
    }



    private Scene buildRegisterScene() {
        Label label = new Label("Register Scene (Noemhi)");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }

    private Scene buildOrderHistoryScene() {
        Label label = new Label("Order History Scene (Noemhi)");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }



    private Scene buildLoginScene() {
        Label titlelabel = new Label("Login");

        Label userNameLabel = new Label("Username: ");
        TextField userNameInput = new TextField();
        userNameInput.setPromptText("Enter Username");

        Label passwordLabel = new Label("Password: ");
        PasswordField passWordInput = new PasswordField();
        passWordInput.setPromptText("Enter Password");

        Button loginButton = new Button("Login");
        Button signupButton = new Button("Sign Up");

        Label errorLabel = new Label();

        loginButton.setOnAction(e -> {
            String username = userNameInput.getText();
            String password = passWordInput.getText();

            String role = DatabaseManager.validLogin(username, password);
            if (role == null){
                errorLabel.setText("Please Enter Valid Username or Password");
            }else if (role.equalsIgnoreCase("admin")){
                switchScene(SceneType.ADMIN);
            } else if (role.equalsIgnoreCase("user")){
                switchScene(SceneType.PRODUCT_BROWSE);
            } else {
                errorLabel.setText("Please Enter Valid Username or Password");
            }
        });

        signupButton.setOnAction(e ->{
            switchScene(SceneType.SIGNUP);
        });

        VBox vbox1 = new VBox(12,titlelabel,userNameLabel,userNameInput,passwordLabel,passWordInput,loginButton,signupButton,errorLabel);
        vbox1.setPadding(new Insets(30));
        vbox1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox1,350,700);

        return scene;
//        return placeholder("Login Scene (Alexis)");
    }

    private Scene buildSignUpScene(){
        Label titlelabel = new Label("Sign Up");

        Label userNameLabel = new Label("Username: ");
        TextField userNameInput = new TextField();
        userNameInput.setPromptText("Enter Username");

        Label passwordLabel = new Label("Password: ");
        PasswordField passWordInput = new PasswordField();
        passWordInput.setPromptText("Enter Password");

        Button signupButton = new Button("Sign Up");
        Button backButton = new Button("Back");

        Label errorLabel = new Label();

        signupButton.setOnAction(e ->{
            String username = userNameInput.getText();
            String password = passWordInput.getText();

            Boolean exist = DatabaseManager.userExist(username);
            if (exist){
                errorLabel.setText("User Already exists\n  Please Try again");
            } else {
                DatabaseManager.signUp(username,password);
                errorLabel.setText("Success!");
            }
        });

        backButton.setOnAction(e ->{
            switchScene(SceneType.LOGIN);
        });

        VBox vbox1 = new VBox(12,titlelabel,userNameLabel,userNameInput,passwordLabel,passWordInput,signupButton,backButton,errorLabel);
        vbox1.setPadding(new Insets(30));
        vbox1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox1,350,700);

        return scene;
//        return placeholder("Sign up scene");
    }

    private Scene buildProductScene() {
        return placeholder("Product Browse Scene");
    }

    private Scene buildCartScene() {
        return placeholder("Cart Scene");
    }

    private Scene buildAdminScene() {
        return placeholder("Admin Scene");
    }


    private Scene placeholder(String text) {
        Label label = new Label(text + " - Coming Soon");

        StackPane root = new StackPane(label);
        return new Scene(root, 800, 600);
    }


    public void switchScene(SceneType type) {
        stage.setScene(createScene(type));
    }
}
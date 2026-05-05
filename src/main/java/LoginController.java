import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import database.DatabaseManager;

public class LoginController {
    public Scene buildScene (){
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
                DatabaseManager.setUser(username);
                SceneManager.getInstance().navigateTo(SceneType.ADMIN);
            } else if (role.equalsIgnoreCase("user")){
                DatabaseManager.setUser(username);
                SceneManager.getInstance().navigateTo(SceneType.MAIN_PAGE_USER);
            } else {
                errorLabel.setText("Please Enter Valid Username or Password");
            }
        });

        signupButton.setOnAction(e ->{
            SceneManager.getInstance().navigateTo(SceneType.SIGNUP);
        });

        VBox vbox1 = new VBox(12,titlelabel,userNameLabel,userNameInput,passwordLabel,passWordInput,loginButton,signupButton,errorLabel);
        vbox1.setPadding(new Insets(30));
        vbox1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox1,350,700);

        return scene;
    }

}

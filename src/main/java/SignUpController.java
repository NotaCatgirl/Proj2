import database.DatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class SignUpController {
    public Scene buildScene(){
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
            SceneManager.getInstance().navigateTo(SceneType.LOGIN);
        });

        VBox vbox1 = new VBox(12,titlelabel,userNameLabel,userNameInput,passwordLabel,passWordInput,signupButton,backButton,errorLabel);
        vbox1.setPadding(new Insets(30));
        vbox1.setAlignment(Pos.CENTER);
        Scene scene = new Scene(vbox1,350,700);

        return scene;
    }
}

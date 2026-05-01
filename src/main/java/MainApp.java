import javafx.application.Application;
import javafx.stage.Stage;
import database.DatabaseManager;
import ui.SceneManager;
import ui.SceneType;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initializeDatabase();
        SceneManager.init(primaryStage);

        primaryStage.setTitle("E-Commerce App");
        // Start on register (your scene for now)
       SceneManager.getInstance().navigateTo(SceneType.LOGIN);

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
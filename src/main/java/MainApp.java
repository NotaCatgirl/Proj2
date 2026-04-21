import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        SceneFactory factory = new SceneFactory(primaryStage);

        primaryStage.setTitle("E-Commerce App");

        // Start on register (your scene for now)
        primaryStage.setScene(factory.createScene(SceneType.REGISTER));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
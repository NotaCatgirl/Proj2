package ui;

import javafx.application.Application;
import javafx.stage.Stage;
import database.DatabaseManager;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        DatabaseManager.initializeDatabase();
        SceneFactory factory = new SceneFactory(primaryStage);

        primaryStage.setTitle("E-Commerce App");

        primaryStage.setScene(factory.createScene(SceneType.REGISTER));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
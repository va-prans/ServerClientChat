package Model;

import Handler.SceneHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application
{
    public static Stage primaryStage;

    @Override
    public void start(Stage stage) throws Exception
    {
        primaryStage = stage;
        Parent root = FXMLLoader.load(getClass().getResource("/GUI/FirstScreen.fxml"));
        Scene scene = new Scene(root, 800, 600);

        primaryStage.setTitle("Chat application");
        primaryStage.setScene(scene);
        scene.getStylesheets().add("/GUI/theme.css");
        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setOnCloseRequest(SceneHandler.confirmCloseEventHandler);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

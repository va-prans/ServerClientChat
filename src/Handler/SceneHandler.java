package Handler;

import Model.Main;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Optional;

public class SceneHandler
{
    public void closeProgram()
    {
        Main.primaryStage.fireEvent(
                new WindowEvent(
                        Main.primaryStage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                ));
    }

    public void popup(String title, String header, String content, boolean error)
    {
        Alert alert = null;
        if (error)
        {
            alert = new Alert(Alert.AlertType.ERROR);
        }
        else
        {
            alert = new Alert(Alert.AlertType.INFORMATION);
        }
        alert.initOwner(Main.primaryStage);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public void changeScene(String sceneName)
    {
        Parent root = null;

        try
        {
            System.out.println("Loading " + sceneName + "...");
            root = FXMLLoader.load(getClass().getResource("/GUI/" + sceneName + ".fxml"));

        } catch (IOException e)
        {
            e.printStackTrace();
            System.out.println("FXML file not found: Make sure path is correct.");
        }

        if (root != null)
        {
            Main.primaryStage.hide();
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/GUI/theme.css");
            Main.primaryStage.setScene(scene);
            Main.primaryStage.show();
        }
    }

    public static EventHandler<WindowEvent> confirmCloseEventHandler = event ->
    {
        Alert closeConfirmation = new Alert(
                Alert.AlertType.CONFIRMATION,
                "Confirm Exit"
        );
        Button exitButton = (Button) closeConfirmation.getDialogPane().lookupButton(
                ButtonType.OK
        );
        exitButton.setText("Exit");
        closeConfirmation.setHeaderText("Exit Chat Application?");
        closeConfirmation.initModality(Modality.APPLICATION_MODAL);
        closeConfirmation.initOwner(Main.primaryStage);

        Optional<ButtonType> closeResponse = closeConfirmation.showAndWait();
        if (!ButtonType.OK.equals(closeResponse.get())) {
            event.consume();
        }
    };
}

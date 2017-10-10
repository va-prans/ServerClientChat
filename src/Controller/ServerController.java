package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import Model.ChatServer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ServerController implements Initializable
{
    //region FXML
    @FXML
    Label ipLabel;
    @FXML
    Label portLabel;
    @FXML
    TextArea chatField;
    @FXML
    ListView<String> userList;
    //endregion

    SceneHandler sceneHandler = new SceneHandler();
    ChatServer chatServer = new ChatServer();

    public void onDisconnectBtn(ActionEvent actionEvent)
    {
        chatServer.stopServer();
        sceneHandler.changeScene("FirstScreen");
    }

    void addChangeListeners()
    {
        chatServer.getInputString().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> o, String oldValue, String newValue)
            {
                String command = newValue.substring(0, 4);
                if (command.equals("LIST"))
                {
                    String[] list = newValue.split("\\:");
                    String formatList = list[1].replaceAll("[\\[\\](){}]", "");
                    String[] allNames = formatList.split(",");
                    ObservableList<String> observableList = FXCollections.observableArrayList(allNames);
                    Platform.runLater(() -> userList.getItems().setAll(observableList));
                }
                String timeString = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                chatField.setText(chatField.getText() + "- (" + timeString + ") " + newValue + "\n");
                chatField.selectPositionCaret(chatField.getLength() - 1);
                chatField.deselect();
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        addChangeListeners();
        Thread thread = new Thread(() -> chatServer.runServer());
        thread.start();
    }
}

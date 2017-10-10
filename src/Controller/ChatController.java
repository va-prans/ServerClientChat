package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import Model.ChatClient;
import Model.ChatServer;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ChatController implements Initializable
{
    //region FXML
    @FXML
    TextArea chatField;
    @FXML
    TextArea messageField;
    @FXML
    ListView<String> userList;
    @FXML
    Button sendBtn;
    @FXML
    Button joinBtn;
    @FXML
    Button quitBtn;
    @FXML
    Label ipLabel;
    @FXML
    Label portLabel;
    @FXML
    Label usernameLabel;
    @FXML
    AnchorPane listPane;
    @FXML
    AnchorPane messagePane;
    //endregion

    SceneHandler sceneHandler = new SceneHandler();

    public static ChatClient chatClient;
    public static String username;
    public static String ipAddress;
    public static int portNumber;

    public void onSendBtn(ActionEvent actionEvent)
    {
        writeToServer("DATA " + username + " " + messageField.getText());
        Platform.runLater(() -> messageField.clear());
        messageField.requestFocus();
    }

    public void onJoinBtn(ActionEvent actionEvent)
    {
        writeToServer("JOIN " + username + " " + ipAddress + ":" + portNumber);
        joinBtn.setDisable(true);
        quitBtn.setDisable(false);
        listPane.setDisable(false);
        messagePane.setDisable(false);

    }

    public void onQuitBtn(ActionEvent actionEvent)
    {
        writeToServer("QUIT");
        ObservableList<String> observableList = FXCollections.observableArrayList();
        Platform.runLater(() -> userList.getItems().setAll(observableList));
        joinBtn.setDisable(false);
        quitBtn.setDisable(true);
        listPane.setDisable(true);
        messagePane.setDisable(true);
    }

    public void onDisconnectBtn(ActionEvent actionEvent)
    {
        writeToServer("QUIT");

        try
        {
            Socket socket = chatClient.getSocket();
            socket.shutdownInput();
            socket.shutdownOutput();
            socket.close();

            sceneHandler.changeScene("ClientScreen");
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void writeToServer(String text)
    {
        try
        {
            PrintWriter printWriter = new PrintWriter(chatClient.getSocket().getOutputStream(), true);
            String readerInput = text;
            printWriter.println(readerInput);

        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    void addChangeListeners()
    {
        chatClient.getInputString().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> o, String oldValue, String newValue)
            {
                String command = newValue.substring(0, 4);
                if (command.equals("LIST"))
                {
                    String[] list = newValue.split("\\:");
                    String formatList = list[1].replaceAll("[\\[\\](){}]","");
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

        messageField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (newValue.matches(""))
                {
                    sendBtn.setDisable(true);
                }
                else
                {
                    sendBtn.setDisable(false);
                }
            }
        });

        messageField.setOnKeyPressed(new EventHandler<KeyEvent>()
        {
            @Override
            public void handle(KeyEvent keyEvent)
            {
                if (keyEvent.getCode() == KeyCode.ENTER)
                {
                    sendBtn.fire();
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ipLabel.setText("Internet Protocol address: " + ipAddress);
        portLabel.setText("Port number: " + portNumber);
        usernameLabel.setText("Username: " + username);
        addChangeListeners();
    }

}

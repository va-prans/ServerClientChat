package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import Model.ChatClient;
import Model.Main;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
    AnchorPane rootPane;
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
    @FXML
    Button minBtn;
    @FXML
    Button closeBtn;
    @FXML
    Button disconnectBtn;
    //endregion

    //region Images
    Image discBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/discbtnsmall.png"));
    Image discBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/discbtn_oversmall.png"));

    Image sendBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/send_normal.png"));
    Image sendBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/send_over.png"));

    Image joinBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/join_normal.png"));
    Image joinBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/join_over.png"));

    Image quitBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/quit_normal.png"));
    Image quitBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/quit_over.png"));

    Image minBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/-.png"));
    Image minBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/-o.png"));
    Image closeBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/x.png"));
    Image closeBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/xo.png"));
    //endregion

    double xOffset = 0;
    double yOffset = 0;

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
        sendIMAV();
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

    public void handleStageMovement()
    {
        rootPane.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        rootPane.setOnMouseDragged(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Main.primaryStage.setX(event.getScreenX() - xOffset);
                Main.primaryStage.setY(event.getScreenY() - yOffset);
            }
        });
    }

    //region ButtonGFX
    public void onDisconnectBtnEnter(MouseEvent mouseEvent)
    {
        disconnectBtn.setGraphic(new ImageView(discBtnImageOver));
    }

    public void onDisconnectBtnExit(MouseEvent mouseEvent)
    {
        disconnectBtn.setGraphic(new ImageView(discBtnImage));
    }

    public void onSendBtnEnter(MouseEvent mouseEvent)
    {
        sendBtn.setGraphic(new ImageView(sendBtnImageOver));
    }

    public void onSendBtnExit(MouseEvent mouseEvent)
    {
        sendBtn.setGraphic(new ImageView(sendBtnImage));
    }

    public void onJoinBtnEnter(MouseEvent mouseEvent)
    {
        joinBtn.setGraphic(new ImageView(joinBtnImageOver));
    }

    public void onJoinBtnExit(MouseEvent mouseEvent)
    {
        joinBtn.setGraphic(new ImageView(joinBtnImage));
    }

    public void onQuitBtnEnter(MouseEvent mouseEvent)
    {
        quitBtn.setGraphic(new ImageView(quitBtnImageOver));
    }

    public void onQuitBtnExit(MouseEvent mouseEvent)
    {
        quitBtn.setGraphic(new ImageView(quitBtnImage));
    }

    public void onMinBtnEnter(MouseEvent mouseEvent)
    {
        minBtn.setGraphic(new ImageView(minBtnImageOver));
    }

    public void onMinBtnExit(MouseEvent mouseEvent)
    {
        minBtn.setGraphic(new ImageView(minBtnImage));
    }

    public void onCloseBtnEnter(MouseEvent mouseEvent)
    {
        closeBtn.setGraphic(new ImageView(closeBtnImageOver));
    }

    public void onCloseBtnExit(MouseEvent mouseEvent)
    {
        closeBtn.setGraphic(new ImageView(closeBtnImage));
    }

    void setGraphics()
    {
        sendBtn.setGraphic(new ImageView(sendBtnImage));
        joinBtn.setGraphic(new ImageView(joinBtnImage));
        quitBtn.setGraphic(new ImageView(quitBtnImage));
        disconnectBtn.setGraphic(new ImageView(discBtnImage));
        minBtn.setGraphic(new ImageView(minBtnImage));
        closeBtn.setGraphic(new ImageView(closeBtnImage));
    }
    //endregion

    public void onMinBtn(ActionEvent actionEvent)
    {
        sceneHandler.minimize();
    }

    public void onCloseBtn(ActionEvent actionEvent)
    {
        sceneHandler.closeProgram();
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

    void sendIMAV()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true)
                {
                    try {
                        Thread.sleep(50000);
                        writeToServer("IMAV");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        ipLabel.setText("Internet Protocol address: " + ipAddress);
        portLabel.setText("Port number: " + portNumber);
        usernameLabel.setText("Username: " + username);
        addChangeListeners();
        handleStageMovement();
        setGraphics();
        chatClient.setOnExit(() -> onDisconnectBtn(null));
    }

}

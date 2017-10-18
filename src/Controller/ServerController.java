package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import Model.ChatServer;
import Model.Main;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Bounds;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.ResourceBundle;

public class ServerController implements Initializable
{
    //region FXML
    @FXML
    AnchorPane rootPane;
    @FXML
    Label ipLabel;
    @FXML
    Label portLabel;
    @FXML
    TextFlow chatField;
    @FXML
    ScrollPane chatScroll;
    @FXML
    ListView<String> userList;
    @FXML
    Button disconnectBtn;
    @FXML
    Button minBtn;
    @FXML
    Button closeBtn;
    //endregion

    //region Images
    Image disconnectBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/discbtn.png"));
    Image disconnectBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/discbtn_over.png"));

    Image minBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/-.png"));
    Image minBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/-o.png"));
    Image closeBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/x.png"));
    Image closeBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/xo.png"));
    //endregion

    double xOffset = 0;
    double yOffset = 0;
    double startY = 1.0;

    Color[] nameColors = { Color.GREEN, Color.GOLD, Color.RED, Color.LIGHTBLUE, Color.PURPLE, Color.DARKORANGE, Color.SADDLEBROWN, Color.TEAL, Color.PINK, Color.LIGHTGREEN};
    HashMap<String, Color> userColors = new HashMap<>();

    SceneHandler sceneHandler = new SceneHandler();
    ChatServer chatServer = new ChatServer();

    public void onDisconnectBtn(ActionEvent actionEvent)
    {
        chatServer.stopServer();
        createLog();
        sceneHandler.changeScene("FirstScreen");
    }

    public void onMinBtn(ActionEvent actionEvent)
    {
        sceneHandler.minimize();
    }

    public void onCloseBtn(ActionEvent actionEvent)
    {
        sceneHandler.closeProgram();
    }

    //region ButtonGFX
    public void onDisconnectBtnEnter(MouseEvent mouseEvent)
    {
        disconnectBtn.setGraphic(new ImageView(disconnectBtnImageOver));
    }

    public void onDisconnectBtnExit(MouseEvent mouseEvent)
    {
        disconnectBtn.setGraphic(new ImageView(disconnectBtnImage));
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
        disconnectBtn.setGraphic(new ImageView(disconnectBtnImage));
        minBtn.setGraphic(new ImageView(minBtnImage));
        closeBtn.setGraphic(new ImageView(closeBtnImage));
    }
    //endregion

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
                    String[] list = newValue.substring(5, newValue.length()).split(" ");
                    for (String s : userColors.keySet())
                    {
                        boolean inList = false;
                        for (String l : list)
                        {
                            if (s.equals(l))
                            {
                                inList = true;
                                break;
                            }
                        }
                        if (!inList)
                        {
                            userColors.remove(s);
                            break;
                        }
                    }
                    ObservableList<String> observableList = FXCollections.observableArrayList(list);
                    Platform.runLater(() -> userList.getItems().setAll(observableList));
                }

                if (command.equals("DATA"))
                {
                    String timeString = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));
                    String[] messageString = newValue.substring(5, newValue.length()).split(":");
                    String restOfMessage = newValue.substring(5 + messageString[0].length(), newValue.length());
                    String nameString = messageString[0];

                    if (!userColors.containsKey(nameString))
                    {
                        assignColor(nameString);
                    }

                    String newLine = "\n";
                    if (chatField.getChildren().size() == 0)
                    {
                        newLine = "";
                    }
                    Text date = new Text(newLine + "[" + timeString + "] ");
                    date.setFill(Color.LIGHTGREY);
                    Text name = new Text(nameString);
                    name.setStyle("-fx-font-weight: bold");
                    name.setFill(userColors.get(nameString));
                    Text message = new Text(restOfMessage);
                    message.setFill(Color.WHITE);
                    Platform.runLater(() ->
                    {
                        chatField.getChildren().addAll(date, name, message);
                        Animation animation = new Timeline(
                                new KeyFrame(Duration.seconds(1),
                                        new KeyValue(chatScroll.vvalueProperty(), 1)));
                        animation.play();
                    });
                }
            }
        });

        chatScroll.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            startY = e.getY();
        });

        chatScroll.addEventFilter(MouseEvent.MOUSE_DRAGGED, e -> {
            double endY = e.getY();
            Bounds viewBounds = chatScroll.getViewportBounds();

            double startEndY = startY - endY;

            Bounds contentBounds = chatScroll.getContent().getLayoutBounds();

            double vChange = startEndY / (contentBounds.getHeight() - viewBounds.getHeight());
            chatScroll.setVvalue(chatScroll.getVvalue() + vChange);

            startY = endY;
        });

        userList.setCellFactory(new Callback<ListView<String>, ListCell<String>>()
        {
            @Override
            public ListCell<String> call(ListView<String> stringListView)
            {
                return new ListCell<String>()
                {
                    @Override
                    protected void updateItem(String stringValue, boolean empty)
                    {
                        super.updateItem(stringValue, empty);

                        if (stringValue == null || empty)
                        {
                            setText(null);
                            setGraphic(null);
                        }
                        else
                        {
                            setText(stringValue);
                            Platform.runLater(() ->
                            {
                                if (!userColors.containsKey(stringValue))
                                {
                                    assignColor(stringValue);
                                }
                                Platform.runLater(() -> setTextFill(userColors.get(stringValue)));
                            });
                        }
                    }
                };
            }
        });
    }

    void assignColor(String stringValue)
    {
        Color newUserColor = Color.WHITE;
        for (int i = 0; i < nameColors.length; i++)
        {
            boolean matches = false;
            for (Color color : userColors.values())
            {
                if (color == nameColors[i])
                {
                    matches = true;
                    break;
                }
            }
            if (!matches)
            {
                newUserColor = nameColors[i];
                break;
            }
        }
        userColors.put(stringValue, newUserColor);
    }

    void createLog()
    {
        Text[] texts = new Text[chatField.getChildren().size()];
        for (int i = 0; i < chatField.getChildren().size(); i++)
        {
            texts[i] = (Text)chatField.getChildren().get(i);
        }
        String log = "";
        for (Text text : texts)
        {
            log += text.getText();
        }

        Path path = Paths.get(System.getProperty("user.dir") + "/Logs/" + LocalDate.now().toString() + "&LogID=" + System.nanoTime() + ".txt");

        try
        {
            Files.createDirectories(path.getParent());
            Files.createFile(path);
            Files.write(path, log.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void onChatPan(MouseEvent mouseEvent)
    {
        ((Node) mouseEvent.getSource()).setCursor(Cursor.CLOSED_HAND);
    }

    public void onChatPanExit(MouseEvent mouseEvent)
    {
        ((Node) mouseEvent.getSource()).setCursor(Cursor.OPEN_HAND);
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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        setGraphics();
        handleStageMovement();
        addChangeListeners();
        Thread thread = new Thread(() -> chatServer.runServer());
        thread.start();
        try {
            ChatServer.IP = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        ipLabel.setText("IP address: " + ChatServer.IP);
        portLabel.setText("Port number: " + ChatServer.PORT);
    }
}

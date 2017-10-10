package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import Model.ChatClient;
import Model.Main;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable
{

    //region FXML
    @FXML
    AnchorPane rootPane;
    @FXML
    TextField usernameField;
    @FXML
    TextField ipField;
    @FXML
    TextField portField;
    @FXML
    Button minBtn;
    @FXML
    Button closeBtn;
    @FXML
    Button connectBtn;
    @FXML
    Button backBtn;
    //endregion

    //region Images
    Image connectBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/connect.png"));
    Image connectBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/connect_over.png"));
    Image backBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/back.png"));
    Image backBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/back_over.png"));

    Image minBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/-.png"));
    Image minBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/-o.png"));
    Image closeBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/x.png"));
    Image closeBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/xo.png"));
    //endregion

    double xOffset = 0;
    double yOffset = 0;

    SceneHandler sceneHandler = new SceneHandler();

    public void onConnectBtn(ActionEvent actionEvent)
    {
        if (!usernameField.getText().equals(""))
        {
            if (isValidName())
            {
                if (!ipField.getText().equals(""))
                {
                    if (isProperIP())
                    {
                        if (!portField.getText().equals(""))
                        {
                            if (isProperPort())
                            {
                                String ip = ipField.getText();
                                int port = Integer.parseInt(portField.getText());

                                ChatController.ipAddress = ip;
                                ChatController.portNumber = port;

                                ChatClient chatClient = new ChatClient();

                                if(chatClient.run(ip, port))
                                {
                                    ChatController.chatClient = chatClient;
                                    ChatController.username = usernameField.getText();
                                    sceneHandler.changeScene("ChatScreen");
                                }
                                else
                                {
                                    sceneHandler.popup("Connection error", "Unable to connect to server", "Please try again later", true);
                                }
                            }
                            else
                            {
                                sceneHandler.popup("Invalid port number", "Port number is invalid", "Please enter a port between 0 - 65535", true);
                            }
                        }
                        else
                        {
                            sceneHandler.popup("Field empty", "Port number missing", "Please enter a port number", true);
                        }
                    }
                    else
                    {
                        sceneHandler.popup("Invalid IP", "IP address format is invalid", "Please make sure you have entered the IP properly", true);
                    }
                }
                else
                {
                    sceneHandler.popup("Field empty", "IP address missing", "Please enter an IP address", true);
                }
            }
            else
            {
                sceneHandler.popup("Invalid username", "Username format mismatch", "Username must be max 12 chars long, only letters, digits, '-' and '_' allowed.", true);
            }
        }
        else
        {
            sceneHandler.popup("Field empty", "Username missing", "Please enter a desired username", true);
        }
    }

    public void onBackBtn(ActionEvent actionEvent)
    {
        sceneHandler.changeScene("FirstScreen");
    }

    boolean isValidName()
    {
        String name = usernameField.getText();

        if (name.length() > 12)
        {
            return false;
        }

        if (!name.matches("^[a-zA-Z0-9_+-]*$")) {
            return false;
        }

        return true;
    }

    boolean isProperIP()
    {
        String[] parseIP = ipField.getText().split("\\.");
        return parseIP.length == 4;
    }

    boolean isProperPort()
    {
        int parsePort = Integer.parseInt(portField.getText());

        if (parsePort > 0 && parsePort < 65535)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public void onMinBtn(ActionEvent actionEvent)
    {
        sceneHandler.minimize();
    }

    public void onCloseBtn(ActionEvent actionEvent)
    {
        sceneHandler.closeProgram();
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
    public void onConnectBtnEnter(MouseEvent mouseEvent)
    {
        connectBtn.setGraphic(new ImageView(connectBtnImageOver));
    }

    public void onConnectBtnExit(MouseEvent mouseEvent)
    {
        connectBtn.setGraphic(new ImageView(connectBtnImage));
    }

    public void onBackBtnEnter(MouseEvent mouseEvent)
    {
        backBtn.setGraphic(new ImageView(backBtnImageOver));
    }

    public void onBackBtnExit(MouseEvent mouseEvent)
    {
        backBtn.setGraphic(new ImageView(backBtnImage));
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
        connectBtn.setGraphic(new ImageView(connectBtnImage));
        backBtn.setGraphic(new ImageView(backBtnImage));
        minBtn.setGraphic(new ImageView(minBtnImage));
        closeBtn.setGraphic(new ImageView(closeBtnImage));
    }
    //endregion

    void forceNumericValues()
    {
        ipField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*\\.*"))
                {
                    ipField.setText(newValue.replaceAll("[^\\d\\.]", ""));
                }
            }
        });

        portField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*"))
                {
                    portField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        forceNumericValues();
        handleStageMovement();
        setGraphics();
    }
}

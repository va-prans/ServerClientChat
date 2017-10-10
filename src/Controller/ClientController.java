package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import Model.ChatClient;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable
{

    //region FXML
    @FXML
    TextField usernameField;
    @FXML
    TextField ipField;
    @FXML
    TextField portField;
    //endregion

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
    }
}

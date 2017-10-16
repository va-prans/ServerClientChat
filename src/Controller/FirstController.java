package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import Model.Main;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class FirstController implements Initializable
{
    //region FXML
    @FXML
    AnchorPane rootPane;
    @FXML
    Button hostBtn;
    @FXML
    Button clientBtn;
    @FXML
    Button minBtn;
    @FXML
    Button closeBtn;
    //endregion

    double xOffset = 0;
    double yOffset = 0;

    SceneHandler sceneHandler = new SceneHandler();

    //region Images
    Image hostBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/hostbtn.png"));
    Image hostBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/hostbtn_over.png"));
    Image clientBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/clientbtn.png"));
    Image clientBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/clientbtn_over.png"));

    Image minBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/-.png"));
    Image minBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/-o.png"));
    Image closeBtnImage = new Image(getClass().getResourceAsStream("../GUI/Images/x.png"));
    Image closeBtnImageOver = new Image(getClass().getResourceAsStream("../GUI/Images/xo.png"));
    //endregion

    public void onHostBtn(ActionEvent actionEvent)
    {
        sceneHandler.changeScene("ServerScreen");
    }

    public void onClientBtn(ActionEvent actionEvent)
    {
        sceneHandler.changeScene("ClientScreen");
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
    public void onHostBtnEnter(MouseEvent mouseEvent)
    {
        hostBtn.setGraphic(new ImageView(hostBtnImageOver));
    }

    public void onHostBtnExit(MouseEvent mouseEvent)
    {
        hostBtn.setGraphic(new ImageView(hostBtnImage));
    }

    public void onClientBtnEnter(MouseEvent mouseEvent)
    {
        clientBtn.setGraphic(new ImageView(clientBtnImageOver));
    }

    public void onClientBtnExit(MouseEvent mouseEvent)
    {
        clientBtn.setGraphic(new ImageView(clientBtnImage));
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
        hostBtn.setGraphic(new ImageView(hostBtnImage));
        clientBtn.setGraphic(new ImageView(clientBtnImage));
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

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        setGraphics();
        handleStageMovement();
    }
}

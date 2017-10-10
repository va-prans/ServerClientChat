package Controller;//Magnus Svendsen DAT16i

import Handler.SceneHandler;
import javafx.event.ActionEvent;

public class FirstController
{
    SceneHandler sceneHandler = new SceneHandler();

    public void onHostBtn(ActionEvent actionEvent)
    {
        sceneHandler.changeScene("ServerScreen");
    }

    public void onClientBtn(ActionEvent actionEvent)
    {
        sceneHandler.changeScene("ClientScreen");
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import pos_client.ResourcesHandler;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import pos_client.fxml.input.InputController;
import pos_client.fxml.question.QuestionController;

/**
 *
 * @author Server
 */
public class DialogHandler
{

    public DialogHandler()
    {

    }

    public boolean question(Scene sceneParent, String title, String msg, String detail) throws IOException, InterruptedException
    {
        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlQuestionDialog());
        Parent root = (Parent) loader.load();

        QuestionController questionController = loader.getController();
        questionController.setMessage(msg);
        questionController.setDetails(detail);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());

        Stage stage = new Stage();
        stage.setTitle(title);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(sceneParent.getWindow());
        stage.setScene(scene);

        stage.showAndWait();

        return (questionController.getChoice() > 0);
    }

    public String input(Scene sceneParent, String title, String msg, String currentInput, String btnName) throws IOException, InterruptedException
    {
        FXMLLoader loader = new FXMLLoader(ResourcesHandler.getInstance().getFxmlInputDialog());
        Parent root = (Parent) loader.load();

        InputController inputController = loader.getController();
        inputController.setMessage(msg);
        inputController.setCurrentInput(currentInput);
        inputController.setButtonName(btnName);

        Scene scene = new Scene(root);

        scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());

        Stage stage = new Stage();
        stage.setTitle(title);

        stage.initModality(Modality.WINDOW_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.initOwner(sceneParent.getWindow());
        stage.setScene(scene);

        stage.showAndWait();

        return inputController.getInput();
    }

}

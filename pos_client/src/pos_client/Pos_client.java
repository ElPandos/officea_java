/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import pos_client.fxml.main.MainController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Pos_client extends Application
{

    @Override
    public void start(Stage primaryStage) throws IOException
    {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/main/Main.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root);

        MainController ctrl = loader.getController();
        ctrl.setStage(primaryStage);
        ctrl.setParent(root);
        ctrl.init();

        scene.getStylesheets().add(ResourcesHandler.getInstance().getThemeMain());

        primaryStage.getIcons().add(new Image(ResourcesHandler.getInstance().getSystemIcon()));

        //new Image("file:C:/JAVA/pos_client/src/pos_client/images/POSicon.png"));
        primaryStage.setTitle("Officea POS");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        //primaryStage.initStyle(StageStyle.UNDECORATED);
        //primaryStage.setFullScreen(true);

        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }
}

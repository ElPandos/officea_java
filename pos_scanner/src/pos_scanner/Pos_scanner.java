/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner;

import java.io.IOException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Pos_scanner extends Application
{

    @Override
    public void start(Stage primaryStage) throws IOException
    {
        java.net.URL url = getClass().getResource("fxml/main/Main.fxml");
        FXMLLoader loader = new FXMLLoader(url);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1024, 768);
        
        String css = Pos_scanner.class.getResource("css/theme.css").toExternalForm();
        scene.getStylesheets().clear();
        scene.getStylesheets().add(css);
        
        // Kills process
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>()
        {
            @Override
            public void handle(WindowEvent e)
            {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setTitle("Officea POS scanner");
        primaryStage.setScene(scene);
//        Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
//        primaryStage.setX((primScreenBounds.getWidth() - primaryStage.getWidth()) / 2);
//        primaryStage.setY((primScreenBounds.getHeight() - primaryStage.getHeight()) / 2);
        primaryStage.setMaximized(false);
        primaryStage.show();
    }

    public static void main(String[] args)
    {
        launch(args);
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.windows;

import java.io.IOException;
import java.net.URL;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pos_client.common.Core;
import pos_client.common.Updater;

/**
 *
 * @author Server
 */
public class Screen extends Updater
{

    protected String title = ""; // Arv

    protected FXMLLoader loader = null;
    protected Stage stage = null;

    public Screen()
    {

    }

    protected void loadScreen(URL url, String theme)
    {
        loader = new FXMLLoader(url);

        Parent root;
        try
        {
            root = (Parent) loader.load();

            Scene scene = new Scene(root);

            stage = new Stage();
            stage.setScene(scene);
            stage.setTitle(title);

            scene.getStylesheets().add(theme);

            stage.setOnCloseRequest(new EventHandler<WindowEvent>()
            {
                @Override
                public void handle(WindowEvent e)
                {
                    hide();
                }
            });
        } catch (IOException ex)
        {
            Core.getInstance().getLog().log("loadScreen - Lyckades inte ladda fönster: " + ex.toString(), Log.LogLevel.CRITICAL);

        }
    }

    public void show()
    {
        if (stage != null)
        {
            stage.setFullScreen(true);
            stage.show();
        }
    }

    public void hide()
    {
        if (stage != null)
        {
            stage.hide();
        }
    }

    public boolean isShowing()
    {
        return stage.isShowing();
    }

    public void changeScreen(int id)
    {
        ObservableList<javafx.stage.Screen> screens = getScreens();

        if (id < screens.size())
        {
            Rectangle2D bounds = screens.get(id).getVisualBounds();
            stage.setX(bounds.getMinX() + 100);
            stage.setY(bounds.getMinY() + 100);
            stage.setFullScreen(true);
        }
    }

    public ObservableList<javafx.stage.Screen> getScreens()
    {
        return javafx.stage.Screen.getScreens();
    }

    public int getScreensActive()
    {
        return javafx.stage.Screen.getScreens().size();
    }

}

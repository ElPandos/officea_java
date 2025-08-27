/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.windows;

import pos_client.ResourcesHandler;
import pos_client.fxml.screens.IdleController;

/**
 *
 * @author Server
 */
public class Idle extends Screen
{

    IdleController ctrl = null;

    public Idle()
    {

    }

    public void load()
    {
        loadScreen(ResourcesHandler.getInstance().getFxmlScreenIdle(), ResourcesHandler.getInstance().getThemeMain());
        changeScreen(1);

        stage.setAlwaysOnTop(true);

        ctrl = loader.getController();
    }
}

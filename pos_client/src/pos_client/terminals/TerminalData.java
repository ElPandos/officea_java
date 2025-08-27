/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.terminals;

import javafx.scene.image.Image;
import pos_client.ResourcesHandler;

/**
 *
 * @author Server
 */
public class TerminalData
{

    private final String name;
    private final String imageUrl;
    private final Image image;

    public TerminalData(String name, String imageUrl)
    {
        this.name = name;
        this.imageUrl = imageUrl;
        this.image = new Image(ResourcesHandler.getInstance().getImageTerminal() + imageUrl);
    }

    public String name()
    {
        return this.name;
    }

    public Image image()
    {
        return this.image;
    }

    public String imageUrl()
    {
        return this.imageUrl;
    }
};

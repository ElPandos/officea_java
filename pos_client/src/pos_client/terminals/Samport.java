/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.terminals;

/**
 *
 * @author Laptop
 */
public class Samport extends Terminal
{

    public static String SAMPORT = "Samport";

    public Samport()
    {
        super();
        init();
    }

    public void init()
    {
        name = SAMPORT;
    }

    public boolean Connect()
    {
        return false;
    }

    public boolean Close()
    {
        /*
        if (baxiCtrl != null) {
            connected(baxiCtrl.close() == 1);
            return baxiCtrl.close() == 1;
        } else {
            return false;
        }
         */
        return false;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.terminals;

import eu.nets.baxi.client.BaxiCtrl;
import eu.nets.baxi.client.TransferAmountArgs;

/**
 *
 * @author ola
 */
public class BaxiEventListenerCustom extends BaxiCtrl
{

    @Override
    public int transferAmount(TransferAmountArgs args)
    {
        return 0;
    }

}

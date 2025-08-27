/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.reporting;

import org.junit.Test;
import pos_client.communication.Com;

/**
 *
 * @author Laptop
 */
public class CommTest
{

    @Test
    public void commTest()
    {

        Com comm = new Com();

        String[] connections = comm.listPorts();

        /*
        // Printer
        if (comm.open(connections[0])) {
        comm.params(9600, 8, 1, 0);
        }
         */
        if (comm.open("COM4"))
        {
            comm.params(57600, 8, 1, 0);
        }

        if (comm.isOpen())
        {
            comm.write("ver 0000");
        }

        if (comm.isOpen())
        {
            try
            {
                Thread.sleep(100);
            } catch (InterruptedException ie)
            {
                //Handle exception
            }
            String buffer = comm.read();
            comm.close();
        }
    }
}

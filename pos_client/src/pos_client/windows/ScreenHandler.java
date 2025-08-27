/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.windows;

/**
 *
 * @author Server
 */
public class ScreenHandler extends Screen
{

    private Customer customer = null;
    private Idle idle = null;
    private Log log = null;

    public ScreenHandler()
    {
        customer = new Customer();
        idle = new Idle();

        load();
    }

    private void load()
    {
        customer.load();
        idle.load();
    }

    public Idle getIdle()
    {
        return idle;
    }

    public Customer getCustomer()
    {
        return customer;
    }

}

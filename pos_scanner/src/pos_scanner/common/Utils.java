/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import pos_scanner.db.DatabaseHandler;

/**
 *
 * @author Server
 */
public class Utils
{

    public Utils()
    {

    }

    public String getLocalIP()
    {
        String ipStr = "Error";
        InetAddress ip;
        try
        {
            ip = InetAddress.getLocalHost();
            if (ip != null)
            {
                ipStr = ip.getHostAddress();
            }
            else
            {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, "ip is null");
            }

        } catch (UnknownHostException e)
        {
            e.printStackTrace();
        }

        return ipStr;
    }

    public String getExternalIP()
    {
        String externalIP = "Error";
        URL whatismyip;
        try
        {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = null;
            try
            {
                in = new BufferedReader(new InputStreamReader(whatismyip.openStream()));
                externalIP = in.readLine();
            } catch (IOException ex)
            {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
            } finally
            {
                if (in != null)
                {
                    try
                    {
                        in.close();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        } catch (MalformedURLException ex)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return externalIP;
    }

    public String getMAC()
    {
        String macStr = "Error";
        InetAddress ip;
        try
        {
            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            if (network != null)
            {
                byte[] mac = network.getHardwareAddress();

                if (mac != null)
                {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < mac.length; i++)
                    {
                        sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                    }

                    macStr = sb.toString();
                } else
                {
                    Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, "MAC is null");
                }
            } else
            {
                Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, "NETWORK is null");
            }
        } catch (SocketException e)
        {
            e.printStackTrace();
        } catch (UnknownHostException ex)
        {
            Logger.getLogger(DatabaseHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return macStr;
    }
}

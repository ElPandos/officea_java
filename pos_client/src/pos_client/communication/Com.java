/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.communication;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.*;
import pos_client.common.Core;
import pos_client.common.General;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class Com
{

    protected SerialPort serialPort = null;

    public Com()
    {
    }

    public boolean isOpen()
    {
        return (serialPort != null && serialPort.isOpened());
    }

    public String[] listPorts()
    {
        String[] portNames = SerialPortList.getPortNames();

        if (portNames.length != 0)
        {
            for (int i = 0; i < portNames.length; i++)
            {
                Core.getInstance().getLog().log(portNames[i] + General.getInstance().newLine, Log.LogLevel.NORMAL);
            }
        } else
        {
            Core.getInstance().getLog().log("Inga COM-portar kunde hittas!", Log.LogLevel.CRITICAL);
        }

        return portNames;
    }

    public String[] listDevices()
    {
        String[] portNames = SerialPortList.getPortNames();
        /*
        for (String port : Serial.list())
        {
            Core.getInstance().getLog().log(port + General.getInstance().newLine, Log.LogLevel.NORMAL);
            Map<String, String> all = Serial.getProperties(port);
        }
        
        String[] allDevices = {"",""};
        
        Map<String, String> wmiObjectProperties = WMI4Java.get().properties(Arrays.asList("Name", "Caption", "ClassGuid")).filters(Arrays.asList("ClassGuid='{4d36e978-e325-11ce-bfc1-08002be10318}'")).VBSEngine().getWMIObject("Win32_PnPEntity");
        
        //VBSEngine().getWMIObject("-Query SELECT * FROM Win32_PnPEntity WHERE ClassGuid='{4d36e978-e325-11ce-bfc1-08002be10318}' | select Name, Caption, ClassGuid");
        //WMI4Java.get().properties(Arrays.asList( "Namn", "Command", "ProcessId")).getWMIObject("Win32_Process"). 
        
        List<String> wmiClassesList = WMI4Java.get().VBSEngine().listClasses();
        */
        return portNames;
    }

    public String getName()
    {
        String ret = "Error...";

        if (isOpen())
        {
            ret = serialPort.getPortName();
        } else
        {
            Core.getInstance().getLog().log("getName - Port is not created or opened", Log.LogLevel.CRITICAL);
        }

        return ret;
    }

    private String checkPortName(String port)
    {
        String connectPort = "";

        if (!port.contains("COM"))
        {
            connectPort = "COM" + port;
        }

        return connectPort;
    }

    public boolean open(String port)
    {
        boolean ret = false;

        String[] allPorts = listPorts();
        String[] allDevices = listDevices();

        if (port.isEmpty())
        {
            return ret;
        }

        if (!isOpen())
        {
            serialPort = new SerialPort(checkPortName(port));

            try
            {
                serialPort.openPort();

                if (serialPort.isOpened())
                {
                    Core.getInstance().getLog().log("Port opened: " + getName(), Log.LogLevel.NORMAL);
                } else
                {
                    Core.getInstance().getLog().log("Failed to open port: " + getName(), Log.LogLevel.CRITICAL);
                }
            } catch (SerialPortException ex)
            {
                Core.getInstance().getLog().log("open - COM error! -> " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("open - Port: " + getName() + " is already opened or not created", Log.LogLevel.CRITICAL);
        }

        return isOpen();
    }

    public boolean params(int baudRate, int dataBits, int stopBit, int parity)
    {
        boolean ret = false;

        if (isOpen())
        {
            try
            {
                if (serialPort.setParams(baudRate, dataBits, stopBit, parity))
                {
                    Core.getInstance().getLog().log("Successfylly added params for port: " + getName(), Log.LogLevel.NORMAL);
                    ret = true;
                } else
                {
                    Core.getInstance().getLog().log("Failed to add params to port: " + getName(), Log.LogLevel.CRITICAL);
                }
            } catch (SerialPortException ex)
            {
                Core.getInstance().getLog().log("params - COM error: " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("params - Port is not created or opened", Log.LogLevel.CRITICAL);
        }

        return ret;
    }

    public boolean write(String data)
    {
        boolean ret = false;

        if (isOpen())
        {

            String dataOut = data + "\r";

            try
            {
                if (serialPort.writeBytes(dataOut.getBytes()))
                {
                    Core.getInstance().getLog().log("Successfull in sending data to port: " + getName(), Log.LogLevel.NORMAL);
                    ret = true;
                } else
                {
                    Core.getInstance().getLog().log("Failed to write to port: " + getName(), Log.LogLevel.CRITICAL);
                }
            } catch (SerialPortException ex)
            {
                Core.getInstance().getLog().log("write - COM error: " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("write - Port is not created or opened", Log.LogLevel.CRITICAL);
        }

        return ret;
    }

    public String read()
    {
        String data = "Error...";

        if (isOpen())
        {
            try
            {
                try
                {
                    Thread.sleep(100);
                } catch (InterruptedException ex)
                {
                    Logger.getLogger(Com.class.getName()).log(Level.SEVERE, null, ex);
                }
                data = serialPort.readString();
                Core.getInstance().getLog().log("Reading port: " + getName() + ": " + data, Log.LogLevel.DESCRIPTIVE);
            } catch (SerialPortException ex)
            {
                Core.getInstance().getLog().log("read - COM error: " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        } else
        {
            Core.getInstance().getLog().log("read - Port is not created or opened", Log.LogLevel.CRITICAL);
        }

        return data;
    }

    public boolean close()
    {
        boolean ret = false;

        if (isOpen())
        {
            try
            {
                ret = serialPort.closePort();
            } catch (SerialPortException ex)
            {
                Core.getInstance().getLog().log("COM close error: " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        }

        return ret;
    }
}

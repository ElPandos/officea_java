/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.common;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author Server
 */
public class File
{
    public boolean fileExists(String Path)
    {
        java.io.File f = new java.io.File(Path);
        if (f.exists() && !f.isDirectory())
        {
            return true;
        } else
        {
            return false;
        }
    }

    public boolean fileDelete(String path)
    {
        java.io.File file = new java.io.File(path);
        return file.delete();
    }

    public String filePath(java.io.File file)
    {
        String absolutePath = file.getAbsolutePath();
        return absolutePath.substring(0, absolutePath.lastIndexOf(java.io.File.separator));
    }

    public boolean fileCreate(String path, String data, boolean Append)
    {
        try
        {
            java.io.File file = new java.io.File(path);
            java.io.File filePath = new java.io.File(filePath(file));

            if (!filePath.exists())
            {
                filePath.mkdirs();
            }

            if (!file.exists())
            {

                BufferedWriter output = new BufferedWriter(new FileWriter(file));
                output.write(data);
                output.close();

                return true;
            } else if (Append)
            {
                BufferedWriter output = new BufferedWriter(new FileWriter(file, Append));
                output.write(data);
                output.close();

                return true;
            }
        } catch (IOException ex)
        {
            //Core.getInstance().getLog().log("fileCreate - " + ex.toString(), Log.LogLevel.CRITICAL);
        }

        return false;
    }

    public String fileRead(String path)
    {
        String retLine = "";
        FileReader iniFile;
        try
        {
            iniFile = new FileReader(path);
            BufferedReader br = new BufferedReader(iniFile);
            String line;
            try
            {
                while ((line = br.readLine()) != null)
                {
                    //if (line.startsWith(cardInfoKey)) {
                    retLine = line; //.substring(cardInfoKey.length());
                    //retVal = Integer.parseInt(val);
                    //}
                }
                br.close();
            } catch (IOException ex)
            {
                //Core.getInstance().getLog().log("fileRead - " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        } catch (FileNotFoundException ex)
        {
            //Core.getInstance().getLog().log("fileRead - " + ex.toString(), Log.LogLevel.CRITICAL);
        }
        return retLine;
    }

    public void executeFM(String type)
    {
        if (Desktop.isDesktopSupported())
        {
            try
            {
                Desktop.getDesktop().browse(new URI(type));
            } catch (IOException ex)
            {
                //Core.getInstance().getLog().log("executeFM - " + ex.toString(), Log.LogLevel.CRITICAL);
            } catch (URISyntaxException ex)
            {
                //Core.getInstance().getLog().log("executeFM - " + ex.toString(), Log.LogLevel.CRITICAL);
            }
        }
    }
}

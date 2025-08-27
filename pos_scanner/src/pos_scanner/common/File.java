package pos_scanner.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class File
{

    public File()
    {
    }

    public static boolean fileExists(String path)
    {
        java.io.File f = new java.io.File(path);
        return (f.exists() && !f.isDirectory());
    }

    public static boolean fileDelete(String path)
    {
        java.io.File f = new java.io.File(path);
        return (f.exists() && f.delete());
    }

    public static String filePath(java.io.File file)
    {
        String absolutePath = file.getAbsolutePath();
        return absolutePath.substring(0, absolutePath.lastIndexOf(java.io.File.separator));
    }

    public static boolean fileCreate(String path, String data, boolean append)
    {
        try
        {
            java.io.File file = new java.io.File(path);
            java.io.File filePath = new java.io.File(filePath(file));

            if (!filePath.exists())
            {
                filePath.mkdirs();
            }

            BufferedWriter output = new BufferedWriter(!file.exists() ? new FileWriter(file) : new FileWriter(file, append));
            output.write(data);
            output.close();

            return true;
        } catch (IOException ex)
        {
            //Core.getInstance().getLog().log("fileCreate - " + ex.toString(), Log.LogLevel.CRITICAL);
        }

        return false;
    }

    //TODO
    public static String fileRead(String path)
    {
        String retLine = "";

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(path));
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
}

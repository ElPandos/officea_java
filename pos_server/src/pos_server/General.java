/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_server;

import eu.nets.baxi.log.FileAccess;
import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Laptop
 */
public class General {
    
    private final String newLine = System.getProperty("line.separator");
    
    public double Str2Dbl(String Data) {
        return Double.parseDouble(Data);
    }
    
    public int Str2Int(String Data) {
        return Integer.parseInt(Data);
    }
    
    public String Int2Str(int Data) {
        return Integer.toString(Data);
    } 
    
    public String Time() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd ' : ' hh:mm:ss");

        return ft.format(dNow) + " >> ";
    }
    
    public boolean FileExists(String Path) {
        File f = new File(Path);
        if (f.exists() && !f.isDirectory())
            return true;
        else
            return false;
    }
    
    public void ExecuteFM(String type) throws URISyntaxException {
        if(Desktop.isDesktopSupported())
        {
            try {
                Desktop.getDesktop().browse(new URI(type));
            } catch (IOException ex) {
                Logger.getLogger(General.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public boolean FileDelete(String Path) {
        File file = new File(Path);

        if (file.delete())
            return true; 
        else
            return false; 
    }
    
    public String FilePath(File file) {
        String absolutePath = file.getAbsolutePath();
        return absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
    }
    
    public boolean FileCreate(String Path, String Data, boolean Append) {
        try {
                File file = new File(Path);
                File filePath = new File(FilePath(file));
                
                if (!filePath.exists())
                    filePath.mkdirs();
                
                if (!file.exists()) {

                    BufferedWriter output = new BufferedWriter(new FileWriter(file));
                    output.write(Data);
                    output.close();
                    
                    return true;
                }
                else if (Append)
                {
                    BufferedWriter output = new BufferedWriter(new FileWriter(file, Append));
                    output.write(Data);
                    output.close();
                    
                     return true;
                }
                else
                    return false;

            } catch (IOException e) {
                return false;
            }
    }
    
    public String ReadFile(String sPath) {
        String retLine = "";
        FileReader iniFile;
        try {
            iniFile = new FileReader(sPath);
            BufferedReader br = new BufferedReader(iniFile);
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    //if (line.startsWith(cardInfoKey)) {
                    retLine = line; //.substring(cardInfoKey.length());
                    //retVal = Integer.parseInt(val);
                    //}
                }
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(FileAccess.class.getName()).log(Level.INFO, null, ex);
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(FileAccess.class.getName()).log(Level.INFO, "ini-file not present!");
        }

        return retLine;
    }
    
}

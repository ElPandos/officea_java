/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner.db;

public class DatabaseSetup
{

    public DatabaseSetup()
    {

    }

    public enum Location
    {
        UNDEFINED,
        LOCAL,
        ONLINE,
    }
        
    public enum Type
    {
        UNDEFINED,
        MYSQL,
        H2
    }

    public enum Version
    {
        UNDEFINED,
        SERVER,
        EMBEDDED,
        MEMORY
    }

    public String driver = "";
    public String url = "";
    public String db_name = "";
    public String port = "";
    public String config = "";
    
    public Type type = Type.UNDEFINED;
    public Version version = Version.UNDEFINED;
    public Location location = Location.UNDEFINED;

    public void clear()
    {
        driver = "";
        url = "";
        db_name = "";
        port = "";
        config = "";

        type = Type.UNDEFINED;
        version = Version.UNDEFINED;
        location = Location.UNDEFINED;
    }
}

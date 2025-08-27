/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_scanner.common;

public class Log
{

    enum Type
    {
        UNDEFINED,
        SYSTEM,
        WINDOW
    }

    Type type = Type.SYSTEM;

    public Log()
    {
        //this.type = type;
    }

    public void outDatabaseConnectedTableExists(String method, String table)
    {
        System.out.println(method + " - Databasen ej uppkopplad eller tabellen [" + table + "] finns inte!");
    }

    public void outResultset(String method, String output)
    {
        System.out.println(method + " - Fel i resultset :  " + output);
    }

    public void outDatabase(String method, String output)
    {
        System.out.println(method + " - Database error :  " + output);
    }

    public void outDatabaseSyncError(String method)
    {
        System.out.println(method + " - Båda databaserna är inte uppkopplade!");
    }

    public void outError(String method, String output)
    {
        System.out.println(method + " - Error :  " + output);
    }

    public void outDatabaseInit(String method)
    {
        System.out.println(method + " - Drivern till database är felaktig");
    }

    public void outSync(String method)
    {
        System.out.println(method + " - Don´t have to create tables online...");
    }

    public void outSyncTable(String method)
    {
        System.out.println(method + " - Could not get any tables...");
    }

    public void outAlreadySynced(String method)
    {
        System.out.println(method + " - Registreringen är redan gjord online!");
    }

    public void outCopyTable(String method, String table, int row, int rowSize)
    {
        System.out.println(method + " - Kopierar rad " + row + " av " + rowSize + " till " + table + " i den lokala databasen");
    }

    public void outUpdate(String method, String db_name, int id, String table)
    {
        System.out.println(method + " - Updaterade rad " + id + " i tabell: " + table + ", Databas: " + db_name);
    }

    public void outInsert(String method, String db_name, String table)
    {
        System.out.println(method + " - Lade till rad en rad i tabell: " + table + ", Databas: " + db_name);
    }

    public void outDelete(String method, String db_name, int id, String table)
    {
        System.out.println(method + " - Tog bort rad " + id + " i tabell: " + table + ", Databas: " + db_name);
    }

    public void outColNameDataSize(String method, int colNameSize, int colDataSize)
    {
        System.out.println(method + " - Olika storlek på columnerna: " + colNameSize + " , " + colDataSize);
    }

    public void outNotImplemented(String method)
    {
        System.out.println(method + " - Inte implementerad ännu!");
    }

    public void outRegisterFail(String method)
    {
        System.out.println(method + " - Registreringen matchar inte den som finns online!");
    }

    public void outCustomerAlreadyExists(String method)
    {
        System.out.println(method + " - Kundkortet finns redan registrerat!");
    }

}

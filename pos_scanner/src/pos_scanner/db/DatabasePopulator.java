package pos_scanner.db;

public class DatabasePopulator
{

    private static Database db = null;

    public DatabasePopulator(Database dbTo)
    {
        this.db = dbTo;
    }

    public void initTables(String table)
    {
        if (db.isConnected())
        {
            String query = "";
            // ---------------------------------------------------------------------
            if (table.toUpperCase().compareTo("shop".toUpperCase()) == 0)
            {
                query = "CREATE TABLE IF NOT EXISTS shop"
                        + "("
                        + "  Name varchar(255),"
                        + "  Adress varchar(255),"
                        + "  Postal varchar(255),"
                        + "  City varchar(255),"
                        + "  Internal_ip varchar(255),"
                        + "  External_ip varchar(255),"
                        + "  MAC varchar(255),"
                        + "  Prefix varchar(255),"
                        + "  sync int(11),"
                        + "  ID INTEGER NOT NULL AUTO_INCREMENT,"
                        + "  PRIMARY KEY (ID)"
                        + ")";

                db.query(query, true);
            }
            // ---------------------------------------------------------------------
            if (table.toUpperCase().compareTo("customer".toUpperCase()) == 0)
            {
                query = "CREATE TABLE IF NOT EXISTS customer"
                        + "("
                        + "  Firstname varchar(255),"
                        + "  Surname varchar(255),"
                        + "  Phone varchar(255),"
                        + "  Card varchar(255),"
                        + "  Email varchar(255),"
                        + "  Sms_contact varchar(255),"
                        + "  Email_contact varchar(255),"
                        + "  sync int(11),"
                        + "  ID INTEGER NOT NULL AUTO_INCREMENT,"
                        + "  PRIMARY KEY (ID)"
                        + ")";

                db.query(query, true);
            }
            // ---------------------------------------------------------------------
            if (table.toUpperCase().compareTo("scans".toUpperCase()) == 0)
            {
                query = "CREATE TABLE IF NOT EXISTS scans"
                        + "("
                        + "  Card varchar(255),"
                        + "  Date DATE,"
                        + "  Time TIME,"
                        + "  Location varchar(255),"
                        + "  sync int(11),"
                        + "  ID INTEGER NOT NULL AUTO_INCREMENT,"
                        + "  PRIMARY KEY (ID)"
                        + ")";

                db.query(query, true);
            }
        } else
        {

        }
    }
}

/*

CREATE TABLE `butik` (
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    `Name` VARCHAR(255) NOT NULL DEFAULT '0',
    `Adress` VARCHAR(255) NOT NULL DEFAULT '0',
    `Postal` VARCHAR(255) NOT NULL DEFAULT '0',
    `City` VARCHAR(255) NOT NULL DEFAULT '0',
    `Internal_ip` VARCHAR(255) NOT NULL DEFAULT '0',
    `External_ip` VARCHAR(255) NOT NULL DEFAULT '0',
    `MAC` VARCHAR(255) NOT NULL DEFAULT '0',
    `Prefix` VARCHAR(255) NOT NULL DEFAULT '0',
    PRIMARY KEY (`ID`)
)

CREATE TABLE `kunddata` (
    `Name` VARCHAR(255) NULL DEFAULT NULL,
    `Surname` VARCHAR(255) NULL DEFAULT NULL,
    `Phone` VARCHAR(255) NULL DEFAULT NULL,
    `card` INT(11) NULL DEFAULT NULL,
    `email` VARCHAR(255) NULL DEFAULT NULL,
    `smscontact` VARCHAR(255) NULL DEFAULT NULL,
    `emailcontact` VARCHAR(255) NULL DEFAULT NULL,
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`ID`)
)

CREATE TABLE `scans` (
    `Card` VARCHAR(255) NOT NULL,
    `Date` DATE NOT NULL,
    `Time` TIME NOT NULL,
    `User` VARCHAR(255) NOT NULL,
    `ID` INT(11) NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`ID`)
)

*/

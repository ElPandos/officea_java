package pos_client.db;

import pos_client.common.Core;
import pos_client.common.DefinedVariables;
import static pos_client.common.DefinedVariables.*;
import pos_client.terminals.Jbaxi;
import pos_client.windows.Log;

/**
 *
 * @author ola
 */
public class DatabasePopulator
{

    private static Database database = null;

    public DatabasePopulator()
    {
        database = Database.getInstance();
    }

    public void initTables()
    {
        if (database.isConnected())
        {
            String query = "CREATE TABLE " + TABLE_SETTINGS_DATABASE
                    + "("
                    + TABLE_SETTINGS_DATABASE + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "variable VARCHAR(255),"
                    + "setting VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_SETTINGS_LOCAL
                    + "("
                    + TABLE_SETTINGS_LOCAL + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "variable VARCHAR(255),"
                    + "setting VARCHAR(255),"
                    + "unit VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_EMPLOYEE
                    + "("
                    + TABLE_EMPLOYEE + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "firstname VARCHAR(255),"
                    + "lastname VARCHAR(255),"
                    + "adress VARCHAR(255),"
                    + "postalcode VARCHAR(255),"
                    + "city VARCHAR(255),"
                    + "phone VARCHAR(255),"
                    + "mobile VARCHAR(255),"
                    + "email VARCHAR(255),"
                    + "gender VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_CASHREGISTER
                    + "("
                    + TABLE_CASHREGISTER + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "man_name VARCHAR(255),"
                    + "man_address VARCHAR(255),"
                    + "man_postal VARCHAR(255),"
                    + "man_contact VARCHAR(255),"
                    + "man_org_no VARCHAR(255),"
                    + "man_phone VARCHAR(255),"
                    + "man_email VARCHAR(255),"
                    + "cashregister_nr VARCHAR(255),"
                    + "cashregister_model VARCHAR(255),"
                    + "cashregister_version VARCHAR(255),"
                    + "cashregister_last_update VARCHAR(255),"
                    + "cashregister_last_backup VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_RECEIPTINFO
                    + "("
                    + TABLE_RECEIPTINFO + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "HEAD_1 VARCHAR(255),"
                    + "HEAD_2 VARCHAR(255),"
                    + "HEAD_3 VARCHAR(255),"
                    + "HEAD_4 VARCHAR(255),"
                    + "HEAD_5 VARCHAR(255),"
                    + "FOOT_1 VARCHAR(255),"
                    + "FOOT_2 VARCHAR(255),"
                    + "FOOT_3 VARCHAR(255),"
                    + "FOOT_4 VARCHAR(255),"
                    + "FOOT_5 VARCHAR(255),"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_BOUTIQUE
                    + "("
                    + TABLE_BOUTIQUE + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "org_no VARCHAR(255),"
                    + "name VARCHAR(255),"
                    + "address1 VARCHAR(255),"
                    + "address2 VARCHAR(255),"
                    + "postal VARCHAR(255),"
                    + "city VARCHAR(255),"
                    + "contact VARCHAR(255),"
                    + "phone VARCHAR(255),"
                    + "website VARCHAR(255),"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_SECURITY_LEVELS
                    + "("
                    + TABLE_SECURITY_LEVELS + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_USERS
                    + "("
                    + TABLE_USERS + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "username VARCHAR(255),"
                    + "password VARCHAR(255),"
                    + TABLE_SECURITY_LEVELS + "_id INTEGER,"
                    + TABLE_EMPLOYEE + "_id INTEGER,"
                    + "FOREIGN KEY (" + TABLE_EMPLOYEE + "_id) REFERENCES " + TABLE_EMPLOYEE + "(" + TABLE_EMPLOYEE + "_id),"
                    + "FOREIGN KEY (" + TABLE_SECURITY_LEVELS + "_id) REFERENCES " + TABLE_SECURITY_LEVELS + "(" + TABLE_SECURITY_LEVELS + "_id)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_EXCHANGE
                    + "("
                    + TABLE_EXCHANGE + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "date DATE,"
                    + "time TIME,"
                    + "value VARCHAR(255),"
                    + TABLE_USERS + "_id INTEGER,"
                    + "FOREIGN KEY (" + TABLE_USERS + "_id) REFERENCES " + TABLE_USERS + "(" + TABLE_USERS + "_id)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_TERMINALS_SUPPORTED
                    + "("
                    + TABLE_TERMINALS_SUPPORTED + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "image_url VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = " CREATE TABLE " + TABLE_VAT
                    + "("
                    + TABLE_VAT + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "value FLOAT not NULL"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_SUPPLEMENTS
                    + "("
                    + TABLE_SUPPLEMENTS + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "price FLOAT"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_CASHBOX
                    + "("
                    + TABLE_CASHBOX + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "open_automatic INTEGER,"
                    + "open_manual INTEGER,"
                    + TABLE_USERS + "_id INTEGER,"
                    + "FOREIGN KEY (" + TABLE_USERS + "_id) REFERENCES " + TABLE_USERS + "(" + TABLE_USERS + "_id)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_INGREDIENCES_CATEGORY
                    + "("
                    + TABLE_INGREDIENCES_CATEGORY + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "orderNr INTEGER not NULL UNIQUE" // Får inte heta order därför orderNr
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_INGREDIENCES
                    + "("
                    + TABLE_INGREDIENCES + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "price FLOAT,"
                    + TABLE_INGREDIENCES_CATEGORY + "_id INTEGER,"
                    + "FOREIGN KEY (" + TABLE_INGREDIENCES_CATEGORY + "_id" + ") REFERENCES " + TABLE_INGREDIENCES_CATEGORY + " (" + TABLE_INGREDIENCES_CATEGORY + "_id" + ")"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_CUSTOMER
                    + "("
                    + TABLE_CUSTOMER + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "Anteckningar VARCHAR(255),"
                    + "Avvikande_leveransadress VARCHAR(255),"
                    + "Besöksadr VARCHAR(255),"
                    + "Efternamn_Namn VARCHAR(255),"
                    + "Epost VARCHAR(255),"
                    + "Fax VARCHAR(255),"
                    + "Förnamn VARCHAR(255),"
                    + "GLN VARCHAR(255),"
                    + "Godkänner_reklam_via_epost VARCHAR(255),"
                    + "Godkänner_reklam_via_sms VARCHAR(255),"
                    + "Inaktiv VARCHAR(255),"
                    + "Kundnr VARCHAR(255),"
                    + "Landskod_Land VARCHAR(255),"
                    + "Medlem VARCHAR(255),"
                    + "Medlems_kundkat VARCHAR(255),"
                    + "Mobiltelefon VARCHAR(255),"
                    + "Namn VARCHAR(255),"
                    + "Orgnr VARCHAR(255),"
                    + "Postadr VARCHAR(255),"
                    + "Postadr2 VARCHAR(255),"
                    + "Postnr_ort VARCHAR(255),"
                    + "Referens VARCHAR(255),"
                    + "Referenskod VARCHAR(255),"
                    + "Sms VARCHAR(255),"
                    + "Telefon VARCHAR(255),"
                    + "Telefon_2 VARCHAR(255),"
                    + "Telefon_3 VARCHAR(255),"
                    + "VAT_nr VARCHAR(255),"
                    + "type VARCHAR(255),"
                    + "date DATE,"
                    + "time TIME"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_RECEIPTS_TYPE
                    + "("
                    + TABLE_RECEIPTS_TYPE + "_id  INTEGER not NULL PRIMARY KEY,"
                    + "type VARCHAR(255),"
                    + "receiptno INT"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_RECEIPTS
                    + "("
                    + TABLE_RECEIPTS + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + TABLE_RECEIPTS + "_nr INTEGER not NULL AUTO_INCREMENT,"
                    + "createdate DATE,"
                    + "createtime TIME ,"
                    + "modifydate DATE,"
                    + "modifytime TIME,"
                    + "totalamount FLOAT,"
                    + "moms1 FLOAT,"
                    + "moms2 FLOAT,"
                    + "moms3 FLOAT,"
                    + "moms4 FLOAT,"
                    + "cashback FLOAT,"
                    + "cash FLOAT,"
                    + "card FLOAT,"
                    + "credit FLOAT,"
                    + "cashresult FLOAT," // ifall >=0 är köpet avklarat
                    + "cardresult FLOAT," // ifall >=0 är köpet avklarat
                    + "bordnumber VARCHAR(255),"
                    + "noofservices INTEGER,"
                    + "noofarticles INTEGER,"
                    + "copyprinted INTEGER,"
                    + TABLE_USERS + "_id INTEGER,"
                    + TABLE_RECEIPTS_TYPE + "_id INTEGER,"
                    + "FOREIGN KEY (" + TABLE_USERS + "_id) REFERENCES " + TABLE_USERS + "(" + TABLE_USERS + "_id),"
                    + "FOREIGN KEY (" + TABLE_RECEIPTS_TYPE + "_id) REFERENCES " + TABLE_RECEIPTS_TYPE + "(" + TABLE_RECEIPTS_TYPE + "_id)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_ARTICLES_SPECIAL_CATEGORY
                    + "("
                    + TABLE_ARTICLES_SPECIAL_CATEGORY + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "orderNr INTEGER not NULL UNIQUE" // Får inte heta order därför orderNr
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_ARTICLES_SPECIAL
                    + "("
                    + TABLE_ARTICLES_SPECIAL + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "price FLOAT not NULL,"
                    + "description VARCHAR(255),"
                    + TABLE_ARTICLES_SPECIAL_CATEGORY + "_id VARCHAR(255),"
                    + "FOREIGN KEY (" + TABLE_ARTICLES_SPECIAL_CATEGORY + "_id" + ") REFERENCES " + TABLE_ARTICLES_SPECIAL_CATEGORY + " (" + TABLE_ARTICLES_SPECIAL_CATEGORY + "_id)" + "ON DELETE CASCADE"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_ARTICLES_CATEGORY
                    + "("
                    + TABLE_ARTICLES_CATEGORY + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "name VARCHAR(255),"
                    + "orderNr INTEGER not NULL UNIQUE" // Får inte heta order därför orderNr
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_ARTICLES_TYPE
                    + "("
                    + TABLE_ARTICLES_TYPE + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "type VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_ARTICLES
                    + "("
                    + TABLE_ARTICLES + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "layoutindex INT,"
                    + "name VARCHAR(255),"
                    + "purchase FLOAT,"
                    + "price_incm FLOAT,"
                    + "price_excm FLOAT,"
                    + "barcode VARCHAR(255),"
                    + "description VARCHAR(255),"
                    + "image_url VARCHAR(255),"
                    + "ingrediences VARCHAR(255),"
                    + TABLE_VAT + "_id INTEGER,"
                    + TABLE_ARTICLES_CATEGORY + "_id INTEGER,"
                    + TABLE_ARTICLES_SPECIAL + "_id INTEGER,"
                    + TABLE_ARTICLES_TYPE + "_id INTEGER,"
                    + "discountValue FLOAT,"
                    + "FOREIGN KEY (" + TABLE_VAT + "_id) REFERENCES " + TABLE_VAT + "(" + TABLE_VAT + "_id),"
                    + "FOREIGN KEY (" + TABLE_ARTICLES_SPECIAL + "_id) REFERENCES " + TABLE_ARTICLES_SPECIAL + " (" + TABLE_ARTICLES_SPECIAL + "_id),"
                    + "FOREIGN KEY (" + TABLE_ARTICLES_CATEGORY + "_id) REFERENCES " + TABLE_ARTICLES_CATEGORY + " (" + TABLE_ARTICLES_CATEGORY + "_id),"
                    + "FOREIGN KEY (" + TABLE_ARTICLES_TYPE + "_id) REFERENCES " + TABLE_ARTICLES_TYPE + " (" + TABLE_ARTICLES_TYPE + "_id)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_SALES_TYPE
                    + "("
                    + TABLE_SALES_TYPE + "_id  INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "type VARCHAR(255)"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_SALES
                    + "("
                    + TABLE_SALES + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "createdate DATE,"
                    + "createtime TIME ,"
                    + "amount FLOAT ,"
                    + "ingredientsExtra VARCHAR(255),"
                    + "ingredientsExcluded VARCHAR(255),"
                    + "specials VARCHAR(255),"
                    + TABLE_RECEIPTS + "_id INTEGER,"
                    + TABLE_ARTICLES + "_id INTEGER,"
                    + TABLE_ARTICLES + "_name VARCHAR(255),"
                    + TABLE_USERS + "_id INTEGER,"
                    + TABLE_USERS + "_name VARCHAR(255),"
                    + TABLE_SALES_TYPE + "_id INTEGER, "
                    + TABLE_SALES_TYPE + "_name VARCHAR(255),"
                    + TABLE_RECEIPTS_TYPE + "_id INTEGER,"
                    + TABLE_RECEIPTS_TYPE + "_name VARCHAR(255),"
                    + "discountValue FLOAT"
                    + ")";

            database.query(query, true);

            // ---------------------------------------------------------------------
            query = "CREATE TABLE " + TABLE_PROFO
                    + "("
                    + TABLE_PROFO + "_id INTEGER not NULL AUTO_INCREMENT PRIMARY KEY,"
                    + "nr INTEGER"
                    + ")";

            database.query(query, true);
            // ---------------------------------------------------------------------

        } else
        {
            Core.getInstance().getLog().log("Kan inte skapa tabeller i databasen. Databasen är inte uppkopplad!", Log.LogLevel.CRITICAL);
        }
    }

    public void initTablesData()
    {

        String query = "INSERT INTO " + TABLE_SETTINGS_DATABASE + " (variable, setting) VALUES"
                + "('JDBC_DRIVER', 'com.mysql.jdbc.Driver'),"
                + "('DB_URL', 'jdbc:mysql://localhost'),"
                + "('DB_NAME', 'test'),"
                + "('DB_PORT', '3306');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_SETTINGS_LOCAL + " (variable, setting, unit) VALUES"
                + "('" + DefinedVariables.SETTING_UPDATE_THREAD + "', '100', 'mSek'),"
                + "('" + DefinedVariables.SETTING_TERMINALS_USE + "', '" + Jbaxi.NETS + "', 'CardReader'),"
                + "('" + DefinedVariables.SETTING_TERMINALS_CONNECT + "', '" + DefinedVariables.TRUE + "', 'Status'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT_USE + "', 'PosPlus', 'CardReader'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT_CONNECT + "', '" + DefinedVariables.TRUE + "', 'Status'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX_USE + "', 'Stor', 'CardReader'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX_CONNECT + "', '" + DefinedVariables.TRUE + "', 'Status'),"
                + "('" + DefinedVariables.SETTING_DATABASE_STATUS + "', 'Connect', 'Status'),"
                + "('" + DefinedVariables.SETTING_DISCOUNT_1 + "', '0', '%'),"
                + "('" + DefinedVariables.SETTING_DISCOUNT_2 + "', '10', '%'),"
                + "('" + DefinedVariables.SETTING_DISCOUNT_3 + "', '25', '%'),"
                + "('" + DefinedVariables.SETTING_DISCOUNT_4 + "', '50', '%'),"
                + "('" + DefinedVariables.SETTING_PRINTER_RECEIPT + "', '-', 'Printer'),"
                + "('" + DefinedVariables.SETTING_PRINTER_REPORT + "', '-', 'Printer'),"
                + "('" + DefinedVariables.SETTING_PRINTER_BONG_1 + "', '-', 'Printer'),"
                + "('" + DefinedVariables.SETTING_PRINTER_BONG_2 + "', '-', 'Printer'),"
                + "('" + DefinedVariables.SETTING_PRINTER_BONG_3 + "', '-', 'Printer'),"
                + "('" + DefinedVariables.SETTING_PRINTER_BONG_4 + "', '-', 'Printer'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT + DefinedVariables.SETTING_COM_TYPE + "', 'PosPlus', 'name'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT + DefinedVariables.SETTING_COM_PORT + "', '3', 'name'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT + DefinedVariables.SETTING_COM_BAUDRATE + "', '57600', 'unit'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT + DefinedVariables.SETTING_COM_DATABITS + "', '8', 'Status'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT + DefinedVariables.SETTING_COM_STOPBIT + "', '1', 'Status'),"
                + "('" + DefinedVariables.SETTING_COM_CONTROLUNIT + DefinedVariables.SETTING_COM_PARITY + "', '0', 'Status'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX + DefinedVariables.SETTING_COM_TYPE + "', 'Stor', 'name'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX + DefinedVariables.SETTING_COM_PORT + "', '4', 'name'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX + DefinedVariables.SETTING_COM_BAUDRATE + "', '9600', 'unit'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX + DefinedVariables.SETTING_COM_DATABITS + "', '8', 'Status'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX + DefinedVariables.SETTING_COM_STOPBIT + "', '1', 'Status'),"
                + "('" + DefinedVariables.SETTING_COM_CASHBOX + DefinedVariables.SETTING_COM_PARITY + "', '0', 'Status'),"
                + "('" + DefinedVariables.SETTING_DISPLAY_TYPE + "', '1', 'Status'),"
                + "('" + DefinedVariables.SETTING_DISPLAY_FULLSCREEN + "', '1', 'Status'),"
                + "('" + DefinedVariables.SETTING_UPDATE_PATH_CSS + "', 'c:\\css\\themes.css', 'FILE');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_RECEIPTS_TYPE + " (" + TABLE_RECEIPTS_TYPE + "_id, type, receiptno) VALUES"
                + "(0, 'Pågående',  1),"
                + "(1, 'Parkerat',  1),"
                + "(2, 'Avbrutet',  1),"
                + "(3, 'Normal',    1),"
                + "(4, 'Övning',    1),"
                + "(5, 'Kopia',     1),"
                + "(6, 'Retur',     1),"
                + "(7, 'ProFo',     1);";

        database.query(query, true);
        // ---------------------------------------------------------------------

        query = "INSERT INTO " + TABLE_EMPLOYEE + " (firstname, lastname, adress, postalcode, city, phone, mobile, email, gender) VALUES"
                + "('Adis',     'Sabanovic',    'Sakförarevägen 7', '22657', 'Lund',    '-', '+46727388899',    'Adis.Sabanovic@officea.se',    'Female'),"
                + "('Mattias',  'Keva',         'Rågångsvägen 20A', '80262', 'Gävle',   '-', '+46739885569',    'Mattias.Keva@officea.se',      'Male'),"
                + "('John',     'Doe',          'Gravvägen 1',      '66666', 'Hell',    '-', '-',               'John.Doe@Officea.se',          'Female');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_SECURITY_LEVELS + " (" + TABLE_SECURITY_LEVELS + "_id, name) VALUES"
                + "('0', 'Admin'),"
                + "('1', 'Developer'),"
                + "('2', 'Controller'),"
                + "('3', 'User');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_USERS + " (username, password, " + TABLE_SECURITY_LEVELS + "_id, " + TABLE_EMPLOYEE + "_id) VALUES"
                + "('ADMIN',    '55555',    '0',    '2'),"
                + "('ADIS',     '4444',     '1',    '2'),"
                + "('MATTIAS',  '3333',     '2',    '2'),"
                + "('JOHN',     '1',        '3',    '3');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_VAT + " (" + TABLE_VAT + "_id , value) VALUES"
                + "(0, 0.00),"
                + "(1, 0.25),"
                + "(2, 0.12),"
                + "(3, 0.06);";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_SUPPLEMENTS + " (name, price) VALUES"
                + "('lök',      '8'),"
                + "('Ananas',   '10'),"
                + "('Plast',    '5');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_ARTICLES_TYPE + " (type) VALUES"
                + "('Vara'),"
                + "('Tjänst');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_ARTICLES_CATEGORY + " (name, orderNr) VALUES"
                + "('Rulle',        '1'),"
                + "('I Bröd',       '2'),"
                + "('Tallrik',      '3'),"
                + "('Hamburgare',   '4'),"
                + "('Sallad',       '5'),"
                + "('Dryck',        '6'),"
                + "('Övrigt',       '7');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_ARTICLES_SPECIAL_CATEGORY + " (name, orderNr) VALUES"
                + "('Paketering',   '1'),"
                + "('Storlek',      '2'),"
                + "('Tillagning',   '3');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_ARTICLES_SPECIAL + " (name, price , description, " + TABLE_ARTICLES_SPECIAL_CATEGORY + "_id) VALUES"
                + "('TAKE AWAY',        '0', 'beskrivning', '1'),"
                + "('VÄRMEISOLERAT',    '0', 'beskrivning', '1'),"
                + "('X-STOR',           '0', 'beskrivning', '2'),"
                + "('Familjepizza',     '0', 'beskrivning', '2'),"
                + "('Inbakad',          '0', 'beskrivning', '2'),"
                + "('Halvinbakad',      '0', 'beskrivning', '2'),"
                + "('Dubbelinbakad',    '0', 'beskrivning', '2'),"
                + "('Well Done',        '0', 'beskrivning', '3'),"
                + "('Medium',           '0', 'beskrivning', '3'),"
                + "('Raw',              '0', 'beskrivning', '3'),"
                + "('Barn',             '0', 'beskrivning', '3');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_ARTICLES + " (layoutindex, name, purchase, price_incm, price_excm, barcode, description, image_url, ingrediences, " + TABLE_VAT + "_id, " + TABLE_ARTICLES_CATEGORY + "_id, " + TABLE_ARTICLES_TYPE + "_id) VALUES"
                + "('1',    'Kebabtallrik',                         '0',       '60',        '53.57143',     '1234567', 'bla bla',  'Article_icons/kebabtallrik.png',        '1,2,3',    '2',  '3',    1),"
                + "('1',    'Kycklingtallrik',                      '0',       '60',        '53.57143',     '1234567', 'bla bla',  'Article_icons/Default/dinner.png',      '1,2,3',    '2',  '3',    1),"
                + "('1',    'Falafeltallrik',                       '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/dinner.png',      '1,2,3',    '2',  '3',    1),"
                + "('1',    'Mixad Tallrik',                        '0',       '70',        '62.5',         '1234567', 'bla bla',  'Article_icons/Default/dinner.png',      '1,2,3',    '2',  '3',    1),"
                + "('1',    'Mixad Tallrik Special',                '0',       '75',        '66.96429',     '1234567', 'bla bla',  'Article_icons/Default/dinner.png',      '1,2,3',    '2',  '3',    1),"
                + "('1',    'Kycklingnuggets',                      '0',       '60',        '53.57143',     '1234567', 'bla bla',  'Article_icons/Default/dinner.png',      '1,2,3',    '2',  '3',    1),"
                + "('1',    'Haloumitallrik',                       '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/eat.png',         '1,2,3',    '2',  '3',    1),"
                + "('1',    'Rödspätta Tallrik',                    '0',       '60',        '53.57143',     '1234567', 'bla bla',  'Article_icons/Default/food.png',        '1,2,3',    '2',  '3',    1),"
                + "('1',    'Pommestallrik',                        '0',       '30',        '26.78571',     '1234567', 'bla bla',  'Article_icons/Default/food.png',        '1,2,3',    '2',  '3',    1),"
                + "('1',    'Grekisk Sallad',                       '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/food.png',        '1,2,3',    '2',  '5',    1),"
                + "('1',    'Kycklingsallad',                       '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/food.png',        '1,2,3',    '2',  '5',    1),"
                + "('1',    'Tonfisksallad',                        '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/food.png',        '1,2,3',    '2',  '5',    1),"
                + "('1',    'Mixsallad',                            '0',       '65',        '58.03571',     '1234567', 'bla bla',  'Article_icons/Default/food.png',        '1,2,3',    '2',  '5',    1),"
                + "('1',    'Kebabrulle',                           '0',       '45',        '40.17857',     '1234567', 'bla bla',  'Article_icons/Default/eat.png',         '1,2,3',    '2',  '1',    1),"
                + "('2',    'Falafelrulle',                         '0',       '30',        '26.78571',     '1234567', 'bla bla',  'Article_icons/Default/meat.png',        '3,4,5',    '2',  '1',    1),"
                + "('3',    'Kycklingrulle',                        '0',       '45',        '40.17857',     '1234567', 'bla bla',  'Article_icons/Default/other.png',       '6,7,8',    '2',  '1',    1),"
                + "('4',    'Mixad Rulle',                          '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/burger.png',      '1,2,3',    '2',  '1',    1),"
                + "('5',    'Haloumirulle',                         '0',       '35',        '31.25',        '1234567', 'bla bla',  'Article_icons/Default/other.png',       '1,2,3',    '2',  '1',    1),"
                + "('6',    'Kycklingnuggets Rulle',                '0',       '40',        '35.71429',     '1234567', 'bla bla',  'Article_icons/Default/other.png',       '1,2,3',    '2',  '1',    1),"
                + "('7',    'MENY - Rulle med pommes',              '0',       '70',        '62.5',         '1234567', 'bla bla',  'Article_icons/Default/soup.png',        '1,2,3',    '2',  '1',    1),"
                + "('1',    'Kebabrulle X-STOR',                    '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/eat.png',         '1,2,3',    '2',  '1',    1),"
                + "('2',    'Falafelrulle X-STOR',                  '0',       '35',        '31.25',        '1234567', 'bla bla',  'Article_icons/Default/meat.png',        '3,4,5',    '2',  '1',    1),"
                + "('3',    'Kycklingrulle X-STOR',                 '0',       '55',        '49.10714',     '1234567', 'bla bla',  'Article_icons/Default/other.png',       '6,7,8',    '2',  '1',    1),"
                + "('4',    'Mixad Rulle X-STOR',                   '0',       '65',        '58.03571',     '1234567', 'bla bla',  'Article_icons/Default/burger.png',      '1,2,3',    '2',  '1',    1),"
                + "('5',    'Haloumirulle X-STOR',                  '0',       '40',        '35.71429',     '1234567', 'bla bla',  'Article_icons/Default/other.png',       '1,2,3',    '2',  '1',    1),"
                + "('6',    'Kycklingnuggets Rulle X-STOR',         '0',       '50',        '44.64286',     '1234567', 'bla bla',  'Article_icons/Default/other.png',       '1,2,3',    '2',  '1',    1),"
                + "('7',    'MENY - Rulle med pommes X-STOR',       '0',       '80',        '71.42857',     '1234567', 'bla bla',  'Article_icons/Default/soup.png',        '1,2,3',    '2',  '1',    1),"
                + "('7',    'HEMBAKAT - Kebab',                     '0',       '50',        '44.64286',     '1234567', 'bla bla',  'Article_icons/hembakat.png',            '1,2,3',    '2',  '2',    1),"
                + "('7',    'HEMBAKAT - Falafel',                   '0',       '35',        '31.25',        '1234567', 'bla bla',  'Article_icons/hembakat.png',            '1,2,3',    '2',  '2',    1),"
                + "('7',    'HEMBAKAT - Kyckling',                  '0',       '50',        '44.64286',     '1234567', 'bla bla',  'Article_icons/hembakat.png',            '1,2,3',    '2',  '2',    1),"
                + "('7',    'HEMBAKAT - Kycklingnuggets',           '0',       '50',        '44.64286',     '1234567', 'bla bla',  'Article_icons/hembakat.png',            '1,2,3',    '2',  '2',    1),"
                + "('7',    'PITABRÖD - Kebab',                     '0',       '40',        '35.71429',     '1234567', 'bla bla',  'Article_icons/Default/cookie.png',      '1,2,3',    '2',  '2',    1),"
                + "('7',    'PITABRÖD - Falafel',                   '0',       '30',        '26.78571',     '1234567', 'bla bla',  'Article_icons/Default/cookie.png',      '1,2,3',    '2',  '2',    1),"
                + "('7',    'PITABRÖD - Kyckling',                  '0',       '40',        '35.71429',     '1234567', 'bla bla',  'Article_icons/Default/warmdrink.png',   '1,2,3',    '2',  '2',    1),"
                + "('7',    'PITABRÖD - Kycklingnuggets',           '0',       '35',        '31.25',        '1234567', 'bla bla',  'Article_icons/Default/warmdrink.png',   '1,2,3',    '2',  '2',    1),"
                + "('7',    'Kolsyrat',                             '0',       '42.56',     '38',           '1234567', 'bla bla',  'Article_icons/Default/drink.png',       '1,2,3',    '2',  '6',    1),"
                + "('7',    'Öl',                                   '38',      '42.56',     '38',           '1234567', 'bla bla',  'Article_icons/Default/beer.png',        '1,2,3',    '2',  '6',    1),"
                + "('7',    'Vin',                                  '38',      '42.56',     '38',           '1234567', 'bla bla',  'Article_icons/Default/alco.png',        '1,2,3',    '2',  '6',    1),"
                + "('8',    'Kniv',                                 '40',      '50',        '40',           '1234567', 'bla bla',  'Article_icons/Default/other.png',       '1,2,3',    '1',  '7',    1),"
                + "('9',    'chocklad',                             '21',      '23.52',     '21',           '1234567', 'bla bla',  'Article_icons/Default/dessert.png',     '1,2,3',    '2',  '7',    1),"
                + "('9',    'Kolsyrat Vatten 0,33cl',               '21',      '23.52',     '21',           '1234567', 'bla bla',  'Article_icons/Default/dessert.png',     '1,2,3',    '2',  '6',    1),"
                + "('10',   'Dryck 0,33cl',                         '12',      '13.44',     '12',           '1234567', 'bla bla',  'Article_icons/Coca-Cola-Can-icon.png',  '1,2,3',    '2',  '6',    1);";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_INGREDIENCES_CATEGORY + " (name, orderNr) VALUES"
                + "('Såser',    '1'),"
                + "('Sallad',   '2'),"
                + "('Kött',     '3'),"
                + "('Krydda',   '4'),"
                + "('Ost',      '5'),"
                + "('Övrigt',   '6'),"
                + "('Rabatter', '7');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_INGREDIENCES + " (name, price, " + TABLE_INGREDIENCES_CATEGORY + "_id) VALUES"
                + "('Bacon',                    '15',   '3'),"
                + "('Banan',                    '10',   '2'),"
                + "('Bearnaisesås',             '10',   '1'),"
                + "('Bearnaisesås I burk',      '10',   '1'),"
                + "('Cayennepeppar',            '10',   '4'),"
                + "('Champinjoner (färska)',    '10',   '2'),"
                + "('Crabfish',                 '15',   '3'),"
                + "('Curry',                    '10',   '4'),"
                + "('Curry (grön)',             '10',   '4'),"
                + "('Curry (gul)',              '10',   '4'),"
                + "('Feferoni',                 '10',   '2'),"
                + "('Fetaost',                  '10',   '5'),"
                + "('Fläskfilé',                '15',   '3'),"
                + "('Gorgonzolaost',            '10',   '5'),"
                + "('Gorgonzolasås',            '10',   '1'),"
                + "('Gorgonzolasås I burk',     '10',   '1'),"
                + "('Gurka',                    '10',   '2'),"
                + "('Hamburgerdressing',        '10',   '1'),"
                + "('Hamburgerdressing I burk', '10',   '1'),"
                + "('Isbergssallad',            '10',   '2'),"
                + "('Jalapeños',                '10',   '2'),"
                + "('Jordnötter',               '10',   '6'),"
                + "('Kapris',                   '10',   '4'),"
                + "('Kebabkött (nötkött)',      '15',   '3'),"
                + "('Kebabsås mild',            '10',   '1'),"
                + "('Kebabsås mild I burk',     '10',   '1'),"
                + "('Kebabsås stark',           '10',   '1'),"
                + "('Kebabsås stark I burk',    '10',   '1'),"
                + "('Kronärtskocka',            '10',   '2'),"
                + "('Kyckling',                 '15',   '3'),"
                + "('Körsbärstomater',          '10',   '2'),"
                + "('Köttfärs',                 '15',   '3'),"
                + "('Lök',                      '10',   '2'),"
                + "('Majs',                     '10',   '2'),"
                + "('Massamancurry',            '10',   '4'),"
                + "('Mozzarellaost',            '10',   '5'),"
                + "('Musslor',                  '15',   '3'),"
                + "('Oliver',                   '10',   '2'),"
                + "('Ost',                      '10',   '5'),"
                + "('Oxfilé',                   '15',   '3'),"
                + "('Paprika (färsk)',          '10',   '2'),"
                + "('Pepperonikorv',            '15',   '3'),"
                + "('Persilja',                 '10',   '4'),"
                + "('Persiljesmör',             '10',   '1'),"
                + "('Persiljesmör I burk',      '10',   '1'),"
                + "('Pesto',                    '10',   '1'),"
                + "('Pesto I burk',             '10',   '1'),"
                + "('Pommes frites',            '10',   '6'),"
                + "('Purjolök',                 '10',   '2'),"
                + "('Rhode Islandsås',          '10',   '1'),"
                + "('Rhode Islandsås I burk',   '10',   '1'),"
                + "('Ruccolasallad',            '10',   '2'),"
                + "('Räkor',                    '15',   '3'),"
                + "('Rödlök',                   '10',   '2'),"
                + "('Salami',                   '15',   '3'),"
                + "('Sardeller',                '15',   '3'),"
                + "('Skinka',                   '15',   '3'),"
                + "('Sparris',                  '10',   '2'),"
                + "('Svartpeppar',              '10',   '4'),"
                + "('Tacokryddmix',             '20',   '4'),"
                + "('Tomater (färska)',         '10',   '2'),"
                + "('Tomater (soltorkade)',     '10',   '2'),"
                + "('Tomatsås',                 '10',   '1'),"
                + "('Tonfisk',                  '15',   '3'),"
                + "('Tzatzikisås',              '10',   '1'),"
                + "('Tzatzikisås I burk',       '10',   '1'),"
                + "('Vitlök',                   '10',   '4'),"
                + "('Vitlök (färsk)',           '10',   '4'),"
                + "('Vitlökssås',               '10',   '1'),"
                + "('Vitlökssås I burk',        '10',   '1'),"
                + "('Rabatt 1 kr',              '-1',   '7'),"
                + "('Rabatt 5 kr',              '-5',   '7'),"
                + "('Rabatt 10 kr',             '-10',   '7'),"
                + "('Rabatt 15 kr',             '-15',   '7'),"
                + "('Rabatt 20 kr',             '-20',   '7'),"
                + "('Rabatt 25 kr',             '-25',   '7'),"
                + "('Rabatt 30 kr',             '-30',   '7'),"
                + "('Rabatt 35 kr',             '-35',   '7'),"
                + "('Rabatt 40 kr',             '-40',   '7'),"
                + "('Rabatt 45 kr',             '-45',   '7'),"
                + "('Rabatt 50 kr',             '-50',   '7'),"
                + "('Ägg',                      '10',   '3');";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_BOUTIQUE + " (org_no, name, address1, address2, postal, city, contact, phone, website) VALUES"
                + "('SE123456789', 'Officea Testkassa', 'Parternas Gränd 7', '', '226 57', 'Lund', 'info@officea.se', '07012345678', 'www.officea.se');";

        database.query(query, true);

        // ---------------------------------------------------------------------
//        query = "INSERT INTO " + TABLE_EXCHANGE + " (date, time, value, " + TABLE_USERS + "_id) VALUES"
//                + "('2016-06-02', '12:12:12', '550', '1');";
//
//        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_TERMINALS_SUPPORTED + " (name, image_url) VALUES"
                + "('-',        'nothing.jpg'),"
                + "('Nets',     'IPP350.jpg'),"
                + "('Samport',  'S300.jpg');";

        database.query(query, true);

        // ---------------------------------------------------------------------   
//        query = "INSERT INTO " + TABLE_RECEIPTS + " (" + TABLE_USERS + "_id, " + TABLE_RECEIPTS + "_nr, createdate, createtime, modifydate, modifytime, " + TABLE_RECEIPTS_TYPE + "_id, totalamount, moms1, moms2, moms3, moms4, cashback, cash, card, credit, cashresult, cardresult, bordnumber, noofservices, noofarticles, copyprinted) VALUES"
//                + "('2','0', '2015-03-24', '12:12:12', '2015-03-25', '12:12:13', '3', '79', '1','2','3','4', '0', '79', '0.00', '0.00', '79', '0.00','0',1,1,0),"
//                + "('2','0', '2015-03-24', '12:12:12', '2015-03-25', '12:12:13', '3', '79', '1','2','3','4', '21', '100', '0.00', '0.00', '79', '0.00','0',1,1,0),"
//                + "('2','0', '2015-03-24', '12:12:12', '2015-03-25', '12:12:13', '3', '150', '1','2','3','4', '0', '0.00', '0.00', '0.00', '0.00', '0.00','0',1,1,0),"
//                + "('2','1', '2015-03-24', '12:12:12', '2015-03-25', '12:12:13', '3', '100.50', '1','2','3','4', '0', '0.00', '100.50', '0.00', '0.00', '100.50','0',1,1,0);";
//
//        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_SALES_TYPE + " (type) VALUES"
                + "('None'),"
                + "('Normal'),"
                + "('Borttaget');";

        database.query(query, true);

        // ---------------------------------------------------------------------
//        query = "INSERT INTO " + TABLE_SALES + " (" + TABLE_RECEIPTS + "_id, " + TABLE_ARTICLES + "_id, " + TABLE_USERS + "_id, " + TABLE_SALES_TYPE + "_id, amount, discountValue) VALUES"
//                + "('1', '2', '3', '1', '4', 0),"
//                + "('1', '1', '3', '1', '4', 0),"
//                + "('1', '3', '3', '1', '2', 0),"
//                + "('1', '7', '3', '1', '1', 0),"
//                + "('1', '6', '3', '1', '6', 0),"
//                + "('2', '5', '2', '1', '3', 0),"
//                + "('2', '4', '2', '1', '3', 0),"
//                + "('2', '3', '2', '1', '3', 0),"
//                + "('2', '2', '2', '1', '3', 0),"
//                + "('2', '1', '2', '1', '3', 0);";
//
//        database.query(query, true);
//
//        // ---------------------------------------------------------------------
//        query = "INSERT INTO " + TABLE_CASHBOX + " (" + TABLE_USERS + "_id, open_automatic, open_manual) VALUES"
//                + "('2', '10',  '0'),"
//                + "('3', '2',   '6');";
//
//        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_CASHREGISTER + " (man_name, man_address, man_postal, man_contact, man_org_no, man_phone, man_email, cashregister_nr, cashregister_model, cashregister_version, cashregister_last_update, cashregister_last_backup) VALUES"
                + "('Officea', 'Sakförarevägen 7', '22657', 'Adis Sabanovic', '820313-XXXX','+46727388899','adis.sabanovic@officea.se','1','Pos','v1.0.0beta','Beta: no update','Beta: no backup')";

        database.query(query, true);

        // ---------------------------------------------------------------------
        query = "INSERT INTO " + TABLE_PROFO + " (nr) VALUES "
                + "('1')";

        database.query(query, true);

        // ---------------------------------------------------------------------
    }

}

package pos_scanner.db;

import java.util.ArrayList;
import pos_scanner.ShopModel;
import pos_scanner.common.Log;
import pos_scanner.common.Utils;
import pos_scanner.dao.CustomerDAO;
import pos_scanner.db.DatabaseSetup.Type;
import pos_scanner.dao.ScansDAO;
import pos_scanner.dao.ShopDAO;
import static pos_scanner.db.DatabaseHandler.Project.SCANNER;

/**
 *
 * @author Server
 */
public class DatabaseHandler
{

    private static DatabaseHandler instance = null;

    Log log = null;
    Utils utils = null;

    protected DatabaseHandler()
    {
        log = new Log();
        utils = new Utils();
    }

    public static DatabaseHandler getInstance()
    {
        if (instance == null)
        {
            instance = new DatabaseHandler();
        }
        return instance;
    }

    public enum Project
    {
        UNDEFINED,
        POS,
        SCANNER
    }

    // DRIVERS
    private final String MYSQL_JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String H2_JDBC_DRIVER = "org.h2.Driver";

    private Database db_local = null;
    private Database db_online = null;

    public Database getOnline(Project project)
    {
        if (this.db_online == null)
        {
            this.db_online = new Database();
            this.db_online.setup("root", "hemmahemma", getSetup(project, Type.MYSQL, DatabaseSetup.Version.SERVER));
            this.db_online.connect();
        }

        return this.db_online;
    }

    public Database getLocal(Project project)
    {
        if (this.db_local == null)
        {
            this.db_local = new Database();
            this.db_local.setup("root", "hemmahemma", getSetup(project, Type.H2, DatabaseSetup.Version.SERVER));
            this.db_local.connect();
        }

        return this.db_local;
    }

    public boolean verifyRegister()
    {
        String mac = utils.getMAC();
        String internal_ip = utils.getLocalIP();
        String external_ip = utils.getExternalIP();

        ShopDAO shopDAOLocal = new ShopDAO(db_local);
        ShopDAO shopDAOOnline = new ShopDAO(db_online);

        ShopModel shopLocal = shopDAOLocal.getShop(1);
        ShopModel shopOnline = shopDAOOnline.getShop(1);

        if (shopLocal.getInternalIP().compareTo(shopOnline.getInternalIP()) == 0
                && shopLocal.getExternalIP().compareTo(shopOnline.getExternalIP()) == 0
                && shopLocal.getMac().compareTo(shopOnline.getMac()) == 0)
        {
            return true;
        }

        return false;
    }

    /*
    public void sync(Project project, Location locationTo)
    {
        ArrayList<String> tablesOnline = getOnline(project).getTables();

        if (tablesOnline.size() > 0)
        {
            for (String table : tablesOnline)
            {
                if (getLocal(project).containsTable(table))
                {
                    if (locationTo == Location.LOCAL)
                    {
                        getLocal(project).clearTable(table);
                        getOnline(project).copyTable(table, db_local);
                    } else // ONLINE
                    {
                        getOnline(project).clearTable(table);
                        getLocal(project).copyTable(table, db_online);
                    }
                } else if (locationTo == Location.LOCAL)
                {
                    DatabasePopulator db_populator = new DatabasePopulator(db_local);
                    db_populator.initTables();

                    getOnline(project).copyTable(table, db_local);
                } else
                {
                    log.outSync(Thread.currentThread().getStackTrace()[1].getMethodName());
                }
            }
        } else
        {
            log.outSyncTable(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }
     */
    public void sync(Project project)
    {
        if (db_local != null && db_online != null)
        {
            if (project == Project.SCANNER)
            {
                ArrayList<String> tablesOnline = db_online.getTables();

                if (tablesOnline.size() > 0)
                {
                    for (int i = 0; i < tablesOnline.size(); i++)
                    {
                        String table = tablesOnline.get(i);

                        if (!db_local.containsTable(table))
                        {
                            DatabasePopulator dbPopulator = new DatabasePopulator(db_local);
                            dbPopulator.initTables(table);
                            db_online.copyTable(table, db_local);
                        } else
                        {
                            ScansDAO scansDAO = new ScansDAO(db_local);
                            if (scansDAO.TABLE.compareTo(table) == 0)
                            {
                                scansDAO.sync(db_online, table);
                            }

                            CustomerDAO customerDAO = new CustomerDAO(db_local);
                            if (customerDAO.TABLE.compareTo(table) == 0)
                            {
                                customerDAO.sync(db_online, table);
                            }

                            ShopDAO shopDAO = new ShopDAO(db_local);
                            if (shopDAO.TABLE.compareTo(table) == 0)
                            {
                                if (!shopDAO.isRegistered(db_online))
                                {
                                    shopDAO.sync(db_online, table);
                                }
                                else
                                {
                                    log.outAlreadySynced(Thread.currentThread().getStackTrace()[1].getMethodName());
                                }
                            }
                        }
                    }
                } else
                {
                    log.outSyncTable(Thread.currentThread().getStackTrace()[1].getMethodName());
                }
            } else
            {
                log.outNotImplemented(Thread.currentThread().getStackTrace()[1].getMethodName());
            }
        } else
        {
            log.outDatabaseSyncError(Thread.currentThread().getStackTrace()[1].getMethodName());
        }
    }

    private DatabaseSetup getSetup(Project project, DatabaseSetup.Type type, DatabaseSetup.Version version)  // NOT TESTED
    {
        DatabaseSetup setup = null;

        switch (type)
        {
            case MYSQL:
            {
                switch (version)
                {
                    case SERVER:
                    {
                        switch (project)
                        {
                            case POS:

                                break;

                            case SCANNER:

                                setup = getMYSQL_SERVER_SCANNER();

                                break;

                            case UNDEFINED:

                                break;
                        }
                    }
                    break;
                }
            }
            break;

            case H2:
            {
                switch (version)
                {
                    case SERVER:
                    {
                        switch (project)
                        {
                            case POS:

                                setup = getH2_SERVER_POS();

                                break;

                            case SCANNER:

                                setup = getH2_SERVER_SCANNER();

                                break;

                            case UNDEFINED:

                                break;
                        }
                    }
                    break;

                    case EMBEDDED:
                    {
                        switch (project)
                        {
                            case POS:

                                setup = getH2_EMBEDDED_POS();

                                break;

                            case SCANNER:

                                setup = getH2_EMBEDDED_SCANNER();

                                break;

                            case UNDEFINED:

                                break;
                        }
                    }
                    break;

                    case MEMORY:
                    {
                        switch (project)
                        {
                            case POS:

                                setup = getH2_MEMORY();

                                break;

                            case SCANNER:

                                setup = getH2_MEMORY();

                                break;

                            case UNDEFINED:

                                break;
                        }
                    }
                    break;

                    case UNDEFINED:

                        break;
                }
            }
            break;
        }

        return setup;
    }

    // ---------------------------------------------------------------------
    // - SCANNER SETUP -----------------------------------------------------
    // ---------------------------------------------------------------------
    private DatabaseSetup getMYSQL_SERVER_SCANNER()
    {
        DatabaseSetup setup = new DatabaseSetup();

        setup.driver = MYSQL_JDBC_DRIVER;
        setup.url = "jdbc:mysql://84.55.97.208/";
        setup.db_name = "scanner_test";
        setup.type = Type.MYSQL;
        setup.port = "3306";
        setup.config = "";
        setup.version = DatabaseSetup.Version.SERVER;
        setup.location = DatabaseSetup.Location.ONLINE;

        return setup;
    }

    private DatabaseSetup getH2_EMBEDDED_SCANNER()
    {
        DatabaseSetup setup = new DatabaseSetup();

        setup.driver = H2_JDBC_DRIVER;
        setup.url = "jdbc:h2:~/Officea_SCANNER/"; // "jdbc:h2:" + System.getProperty("user.home") + "/Officea_SCANNER_database/"
        setup.db_name = "SCANNER_database";
        setup.type = Type.H2;
        setup.port = "";
        setup.config = "";
        setup.version = DatabaseSetup.Version.EMBEDDED;
        setup.location = DatabaseSetup.Location.LOCAL;

        return setup;
    }

    private DatabaseSetup getH2_SERVER_SCANNER()
    {
        DatabaseSetup setup = new DatabaseSetup();

        setup.driver = H2_JDBC_DRIVER;
        setup.url = "jdbc:h2:tcp://localhost/~/Officea_SCANNER/";
        setup.db_name = "SCANNER_database";
        setup.type = Type.H2;
        setup.port = "";
        setup.config = "";
        setup.version = DatabaseSetup.Version.SERVER;
        setup.location = DatabaseSetup.Location.LOCAL;

        return setup;
    }

    // ---------------------------------------------------------------------
    // - POS SETUP ---------------------------------------------------------
    // ---------------------------------------------------------------------
    private DatabaseSetup getH2_EMBEDDED_POS()
    {
        DatabaseSetup setup = new DatabaseSetup();

        setup.driver = H2_JDBC_DRIVER;
        setup.url = "jdbc:h2:~/Officea_POS/";
        setup.db_name = "POS_database";
        setup.type = Type.H2;
        setup.port = "";
        setup.config = "";
        setup.version = DatabaseSetup.Version.EMBEDDED;
        setup.location = DatabaseSetup.Location.LOCAL;

        return setup;
    }

    private DatabaseSetup getH2_SERVER_POS()
    {
        DatabaseSetup setup = new DatabaseSetup();

        setup.driver = H2_JDBC_DRIVER;
        setup.url = "jdbc:h2:tcp://localhost/~//Officea_POS/";
        setup.db_name = "POS_database";
        setup.type = Type.H2;
        setup.port = "";
        setup.config = "";
        setup.version = DatabaseSetup.Version.SERVER;
        setup.location = DatabaseSetup.Location.LOCAL;

        return setup;
    }

    private DatabaseSetup getH2_MEMORY() // NOT TESTED
    {
        DatabaseSetup setup = new DatabaseSetup();

        setup.driver = H2_JDBC_DRIVER;
        setup.url = "jdbc:h2:mem:test";
        setup.db_name = "POS";
        setup.type = Type.H2;
        setup.port = "";
        setup.config = "";
        setup.version = DatabaseSetup.Version.MEMORY;
        setup.location = DatabaseSetup.Location.LOCAL;

        return setup;
    }

}

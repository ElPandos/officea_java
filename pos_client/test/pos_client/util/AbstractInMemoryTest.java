package pos_client.util;

import org.junit.After;
import org.junit.Before;
import pos_client.db.Database;
import pos_client.db.DatabasePopulator;

/**
 *
 * @author Ola Adolfsson
 */
public abstract class AbstractInMemoryTest
{

    DatabasePopulator databasePopulator = new DatabasePopulator();
    Database database;

    @Before
    public void setup()
    {
        database = Database.getInstance();
        if (!database.isConnected())
        {
            database.setup(Database.H2_IN_MEMORY_USERNAME, Database.H2_IN_MEMORY_PASSWORD, Database.DatabaseType.H2_IN_MEMORY);
            database.connect();
        }

        databasePopulator.initTables();
        databasePopulator.initTablesData();
    }

    @After
    public void tearDown()
    {
        if (database != null && database.isConnected())
        {
            databasePopulator.dropAllTables();
        }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client;

import pos_client.db.Database;
import java.io.IOException;
import javafx.scene.Scene;
import pos_client.common.Core;
import pos_client.common.DialogHandler;
import pos_client.db.DatabasePopulator;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.UserDAO;
import pos_client.db.Database.DatabaseStatusInfo;
import pos_client.windows.Log;

/**
 *
 * @author Laptop
 */
public class LoginHandler
{

    private UserModel user = null;
    private boolean loggedIn = false;

    public LoginHandler()
    {
    }

    public UserModel getUser()
    {
        return user;
    }

    public boolean isLoggedIn()
    {
        return loggedIn;
    }

    public DatabaseStatusInfo login(String username, String password)
    {
        if (Database.getInstance().isConnected())
        {
            Core.getInstance().getLog().log("Loggar in som", Log.LogLevel.NORMAL);
            Core.getInstance().getLog().log("Användare: " + username, Log.LogLevel.NORMAL);
            //Core.getInstance().getLog().log("Lösenord: " + password, 0);

            UserDAO userDAO = new UserDAO();

            if (username.compareTo("DEFAULT") == 0 && password.compareTo("DATABASE") == 0)
            {
                Log.LogLevel logLevel = Core.getInstance().getLog().getLogController().getLogLevel();
                Core.getInstance().getLog().setLogLevel(Log.LogLevel.ALL);

                Database.getInstance().dropAllTables();
                
                DatabasePopulator databasePopulator = new DatabasePopulator();
                databasePopulator.initTables();
                databasePopulator.initTablesData();

                Core.getInstance().getLog().setLogLevel(logLevel);

                return DatabaseStatusInfo.DATABASE_DEFAULT;
            } else
            {
                user = userDAO.getUser(username, password);

                loggedIn = (user != null);

                if (loggedIn)
                {
                    Core.getInstance().getLog().log("Inloggning lyckades!", Log.LogLevel.NORMAL);
                    return DatabaseStatusInfo.NORMAL;
                } else
                {
                    Core.getInstance().getLog().log("Inloggning misslyckades!", Log.LogLevel.CRITICAL);
                    return DatabaseStatusInfo.PASS_USER_WRONG;
                }

            }
        } else
        {
            return DatabaseStatusInfo.DATABASE_NOT_CONNECTED;
        }
    }

    public boolean logout(Scene scene)
    {
        loggedIn = false;

        Receipt receipt = user.getCurrentReceipt();

        if (receipt.hasSales())
        {
            String title = "Hantera kvitto";
            String msg = "Vill du parkera kvittot?";
            String detail = "Det finns ett pågående kvitto...";

            try
            {
                try
                {
                    DialogHandler dialogHandler = new DialogHandler();
                    if (dialogHandler.question(scene, title, msg, detail))
                    {
                        user.getCurrentReceipt().park();
                        return true;
                    }
                } catch (InterruptedException ex)
                {
                    Core.getInstance().getLog().log("logout - Lyckades inte logga ut", Log.LogLevel.CRITICAL);
                }
            } catch (IOException ex)
            {
                Core.getInstance().getLog().log("logout - Lyckades inte logga ut", Log.LogLevel.CRITICAL);
            }
        } else
        {
            ReceiptDAO receiptDAO = new ReceiptDAO();
            receiptDAO.remove(receipt.getReceiptId());
            return true;
        }

        return false;
    }

}

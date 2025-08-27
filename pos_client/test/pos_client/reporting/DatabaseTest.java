/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.reporting;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Map;
import org.junit.Test;
import pos_client.db.Database;
import pos_client.db.dao.ArticleDAO;

/**
 *
 * @author Laptop
 */
public class DatabaseTest
{

    @Test
    public void createReceipt() throws Exception
    {
        LocalTime before = LocalTime.now();
        Database database = Database.getInstance();
        boolean isConnected = database.connect();

        ArticleDAO articleDAO = new ArticleDAO();
        Map<Integer, String> moms = articleDAO.getArticleMoms();

        LocalTime after = LocalTime.now();
        Duration elapsed = Duration.between(before, after);
        System.out.println("Generated in: " + elapsed.toMillis());
    }
}

package pos_client.db.dao;

import static org.junit.Assert.assertEquals;
import org.junit.Test;
import pos_client.Receipt;
import pos_client.util.AbstractInMemoryTest;

/**
 *
 * @author Ola Adolfsson
 */
public class ReceiptDAOTest extends AbstractInMemoryTest
{

    @Test
    public void testUpdateReceiptNo()
    {
        ReceiptDAO receiptDAO = new ReceiptDAO();
        final int receiptId = receiptDAO.getLastestReceiptId();

        final int exceptedReceiptNo = 10;
        receiptDAO.updateReceiptNo(receiptId, exceptedReceiptNo);

        Receipt updatedReceipt = receiptDAO.fetchReceipt(receiptId);
        assertEquals(exceptedReceiptNo, updatedReceipt.getNr());
    }
}

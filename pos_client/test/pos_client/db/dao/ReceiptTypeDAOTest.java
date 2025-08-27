package pos_client.db.dao;

import org.junit.Test;
import pos_client.db.dao.ReceiptTypeDAO.ReceiptType;
import pos_client.util.AbstractInMemoryTest;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author Ola Adolfsson
 */
public class ReceiptTypeDAOTest extends AbstractInMemoryTest
{

    ReceiptTypeDAO receiptTypeDAO;

    @Override
    public void setup()
    {
        super.setup();

        receiptTypeDAO = new ReceiptTypeDAO();
    }

    @Test
    public void testUpdateNormalReceiptNo()
    {
        final int exceptedReceiptNo = 10;
        updateReceiptNo(Normal, exceptedReceiptNo);

        int currentReceiptNo = receiptTypeDAO.fetchCurrentReceiptNo(Normal);

        assertEquals(exceptedReceiptNo, currentReceiptNo);
    }

    @Test
    public void testUpdateReceiptMultipleTypes()
    {
        final int exceptedNormalReceiptNo = 10;
        final int exceptedProfoReceiptNo = 20;

        updateReceiptNo(Normal, exceptedNormalReceiptNo);
        updateReceiptNo(ProFo, exceptedProfoReceiptNo);

        int currentNormalReceiptNo = receiptTypeDAO.fetchCurrentReceiptNo(Normal);
        int currentProfoReceiptNo = receiptTypeDAO.fetchCurrentReceiptNo(ProFo);

        assertEquals(exceptedNormalReceiptNo, currentNormalReceiptNo);
        assertEquals(exceptedProfoReceiptNo, currentProfoReceiptNo);
    }

    private void updateReceiptNo(ReceiptType receiptType, int newReceiptNo)
    {
        receiptTypeDAO.updateReceiptNo(receiptType, newReceiptNo);
    }
}

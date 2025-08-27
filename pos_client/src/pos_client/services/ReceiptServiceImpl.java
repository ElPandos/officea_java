package pos_client.services;

import pos_client.Receipt;
import pos_client.db.dao.ProfoDAO;
import pos_client.db.dao.ReceiptDAO;
import pos_client.db.dao.ReceiptTypeDAO;

/**
 *
 * @author Ola Adolfsson
 */
public class ReceiptServiceImpl implements ReceiptService
{

    /**
     * Update receipt with next receipt number
     *
     * @param receipt
     * @return
     */
    @Override
    public int updateReceiptWithNextReceiptNo(Receipt receipt)
    {
        ReceiptTypeDAO receiptTypeDAO = new ReceiptTypeDAO();
        int currentReceiptNo = receiptTypeDAO.fetchCurrentReceiptNo(receipt.getType());
        int nextReceiptNo = currentReceiptNo + 1;

        ReceiptDAO receiptDAO = new ReceiptDAO();

        receiptDAO.updateReceiptNo(receipt.getReceiptId(), nextReceiptNo);
        receiptTypeDAO.updateReceiptNo(receipt.getType(), nextReceiptNo);

        return nextReceiptNo;
    }

    @Override
    public int getProfoNo()
    {
        ProfoDAO profoDAO = new ProfoDAO();
        return profoDAO.getProfoNr();
    }

    @Override
    public void increaseProfoNo(int currentNr)
    {
        ProfoDAO profoDAO = new ProfoDAO();
        profoDAO.increaseProfoNr(currentNr);
    }

}

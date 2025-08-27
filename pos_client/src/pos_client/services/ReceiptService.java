package pos_client.services;

import pos_client.Receipt;

/**
 *
 * @author Ola Adolfsson
 */
public interface ReceiptService
{

    public int updateReceiptWithNextReceiptNo(Receipt newReceipt);

    public int getProfoNo();

    public void increaseProfoNo(int currentNr);

}

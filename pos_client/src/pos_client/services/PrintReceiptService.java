/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pos_client.services;

import java.text.ParseException;
import pos_client.services.PrintReceiptServiceImpl.PrintReceiptCopyStatus;

/**
 *
 * @author ola
 */
public interface PrintReceiptService
{

    PrintReceiptCopyStatus printReceiptCopy(int receiptNo, ReceiptPrinter receiptPrinter) throws ParseException, Exception;

}

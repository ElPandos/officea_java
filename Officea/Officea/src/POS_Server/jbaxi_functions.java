package pos_server;

import eu.nets.baxi.client.BaxiCtrl;
import eu.nets.baxi.client.TransferAmountArgs;

public class jbaxi_functions
{
	
	public static BaxiCtrl m_Baxi = new BaxiCtrl();
	
	/* ASCII
	 0      48 0060 0x30
	 1      48 0060 0x31
	 2      48 0060 0x32
	 3      48 0060 0x33
	 4      48 0060 0x34
	 5      48 0060 0x35
	 6      48 0060 0x36
	 7      48 0060 0x37
	 8      48 0060 0x38
	 9      48 0060 0x39
	 */
	
	jbaxi_functions()
	{
		m_Baxi.open();
	}
	
	public int TransferAmount()
	{
		
		// Normal purchase transaction request (10.00 kr):
		TransferAmountArgs a = new TransferAmountArgs();

		a.setOperID("0000");
		
		a.setType1(0x30);
		a.setAmount1(1000);
		
		a.setType2(0x30);
		a.setAmount2(0);
		
		a.setType3(0x30);
		a.setAmount3(0);
		
		a.setHostData("abcdefg");
		a.setArticleDetails("");
		a.setPaymentConditionCode("");
		a.setAuthCode("");
		
		return m_Baxi.transferAmount(a);
		
	}

}

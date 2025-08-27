package eu.nets.baxi.gui.tld;

import java.io.UnsupportedEncodingException;

public enum TLDCommand {
	
	VASCustomerInfo ("2000 US 0003 US 001 RS"),
	PspCommand ("2002 US 0003 US 001 RS"),
	CustomerId ("2001 US 0000 US  RS"), 
	InformationField1 ("2006 US 0000 US  RS"),
	CardValidation ("2015 US 0000 US  RS"),
	PspVasIdCustomerInfo  ("2008 US 0003 US 007 RS");
	
	private final String value;
	private static final byte[] us = {0x1F};
	private static final byte[] rs = {0x1E};
	
	private TLDCommand(String value){
		this.value = value;
	}
	
	private static String concatenateStrings(TLDCommand[] commandList){
		StringBuilder builder = new StringBuilder();
		
		for(TLDCommand command:commandList){
			builder.append(command.toString());
		}
		
		return builder.toString();
	}
	
	public static byte[] getBytes(String text) throws UnsupportedEncodingException{
		String usStr = new String(us, "UTF-8");
        String rsStr = new String(rs, "UTF-8");
        
        return  text.replaceAll(" RS", rsStr).replaceAll(" US ", usStr).replaceAll(" ", "").getBytes("UTF-8");
	}
	
	public String toString(){
		return value;
	}
	
	public byte[] getBytes() throws UnsupportedEncodingException {    
        return getBytes(value); 
	}
	
	public static byte[] getCommandListAsBytes(TLDCommand[] commandList) throws UnsupportedEncodingException{
		return getBytes(concatenateStrings(commandList));
	}
	
	public static String getCommandListAsString(TLDCommand[] commandList){
		return concatenateStrings(commandList);
	}
}
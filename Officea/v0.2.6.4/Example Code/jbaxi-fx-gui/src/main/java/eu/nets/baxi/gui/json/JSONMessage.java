package eu.nets.baxi.gui.json;

public class JSONMessage {
	public static final String TYPE_ADMIN = "admin";
	public static final String TYPE_TA = "ta";
    public static final String TYPE_CI = "cardinfo";
	private static final String JSON_STUB = "%JSON_STUB%";
	private static final String JSON_TYPE_STUB = "%JSON_TYPE_STUB%";
    private static final String JSON_OPTIONAL_STUB = "%JSON_OPTIONAL_STUB%";
	private static final String JSON_MESSAGE =
			"{\n" +
			"   \"od\": {\n" +
			"       \"ver\": \"1.01\",\n" +
			"       \"nets\": {\n" +
			"           \"ver\": \"1.00\",\n" +
			"           \"ch13\": {\n" +
			"               \"ver\": \"1.00\",\n" +
			"               \"" + JSON_TYPE_STUB + "\": {\n" +
			"                   \"ver\": \"1.00\",\n" +
			"                   \"o\": {\n" +
			"                       \"ver\": \"1.00\",\n" +
			JSON_STUB +
			"                   }\n" +
			"               }\n" +
			"           }\n" +
			"       }\n" +
			"   }\n" +
			"}";
    private static final String JSON_OPTIONAL_MESSAGE =
            ",\n   \"o\": {\n" +
            "       \"ver\": \"1.00\",\n" +
            JSON_OPTIONAL_STUB + 
            "   }\n";
    private static final String JSON_MINI_MESSAGE =
            "{\n" +
            "   \"" + JSON_TYPE_STUB + "\": {\n" +
            "       \"ver\": \"1.00\",\n" + 
            JSON_STUB + 
            "   }" +
            JSON_OPTIONAL_MESSAGE +
            "}";
	private static final String JSON_BLANKS = 
			"                       ";
    private static final String JSON_MINI_BLANKS =
    		"       ";
	private static final String JSON_MERCH = JSON_BLANKS + "\"merch\": 733300";
    private static final String JSON_MINI_MERCH = JSON_MINI_BLANKS + "\"merch\": 733300";
	private static final String JSON_AUTODCC = JSON_BLANKS + "\"autodcc\": 2";
	private static final String JSON_TXNREF = JSON_BLANKS + "\"txnref\": \"123456789123\"";
    private static final String JSON_MU_PAN = JSON_MINI_BLANKS + "\"pan\": \"?\"," + "\n" + 
                                              JSON_MINI_BLANKS + "\"issuerid\": \"?\"," + "\n" + 
                                              JSON_MINI_BLANKS + "\"restrictions\": \"?\"," + "\n" +
                                              JSON_MINI_BLANKS + "\"track2\": \"?\""; 
    private static final String JSON_MU_ALLTAGS = JSON_MINI_BLANKS +  "\"alltags\": \"?\"";

	public static String getMerch(String type) {
		return JSON_MESSAGE
				.replace(JSON_STUB, JSON_MERCH + "\n")
				.replace(JSON_TYPE_STUB, type);
	}

	public static String getAutoDCC(String type) {
		return JSON_MESSAGE
				.replace(JSON_STUB, JSON_AUTODCC + "\n")
				.replace(JSON_TYPE_STUB, type);
	}

	public static String getTxnRef(String type) {
		return JSON_MESSAGE
				.replace(JSON_STUB, JSON_TXNREF + "\n")
				.replace(JSON_TYPE_STUB, type);
	}

	public static String getAllData(String type) {
		if (type.equalsIgnoreCase(TYPE_TA)){
			return JSON_MESSAGE
					.replace(JSON_STUB,	JSON_MERCH + ",\n" +
									   	JSON_AUTODCC + ",\n" +
									   	JSON_TXNREF + "\n")
					.replace(JSON_TYPE_STUB, type);
		}else if (type.equalsIgnoreCase(TYPE_ADMIN)){
			return JSON_MESSAGE
					.replace(JSON_STUB, JSON_MERCH + ",\n" +
										JSON_TXNREF + "\n")
					.replace(JSON_TYPE_STUB, type);
		}else{
			return "Unknown type";
		}
	}

    public static String getMUPANRef(String type, boolean optionalMerch) {
           String result = JSON_MINI_MESSAGE
                                .replace(JSON_TYPE_STUB, type)
                                .replace(JSON_STUB, JSON_MU_PAN + ",\n");
           
           if(optionalMerch){
               result = result.replace(JSON_OPTIONAL_STUB, JSON_MERCH + ",\n");
           }else{
               result = result.replace(JSON_OPTIONAL_MESSAGE, "");
           }
           
           return result;
    }
    
    public static String getMUALLTAGSref(String type, boolean optionalMerch){
         String result = JSON_MINI_MESSAGE
                                .replace(JSON_TYPE_STUB, type)
                                .replace(JSON_STUB, JSON_MU_ALLTAGS + "\n");
           
           if(optionalMerch){
               result = result.replace(JSON_OPTIONAL_STUB, JSON_MINI_MERCH + "\n");
           }else{
               result = result.replace(JSON_OPTIONAL_MESSAGE, "");
           }
           
           return result;
    }
    
    // To prevent instanciation
    private JSONMessage(){}
    
}

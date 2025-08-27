package eu.nets.baxi.gui.style;

import eu.nets.baxi.gui.Main;
import javafx.scene.Scene;

public class HandleResources {
	private static final String RESOURCE_DIR = "/eu/nets/baxi/gui/";
	private static final String STYLESHEET = "BaxiDark.css";
	private static final String FXML_MAIN = "Main.fxml";
	private static final String FXML_SEND_TLD = "tld/SendTLD.fxml";
	private static final String FXML_SET_PROPERTY = "properties/SetProperty.fxml";
	private static final String FXML_ADMIN = "admin/Admin.fxml";
	private static final String FXML_TRANSFER_AMOUNT = "transferamount/TransferAmount.fxml";
	private static final String FXML_BIB_ADMIN = "admin/bib/BibAdmin.fxml";
	private static final String FXML_BIB_TRANSACTION = "admin/bib/BibTransaction.fxml";
	private static final String FXML_SEND_JSON = "json/SendJSON.fxml";

	public static void LoadStylesheet(Scene scene){
		String css = Main.class.getResource(RESOURCE_DIR + STYLESHEET).toExternalForm();
		scene.getStylesheets().clear();
		scene.getStylesheets().add(css);
	}
	
	public static String getFxmlMain(){
		return RESOURCE_DIR + FXML_MAIN;
	}

	public static String getFxmlSendTld(){
		return RESOURCE_DIR + FXML_SEND_TLD;
	}

	public static String getFxmlSetProperty(){
		return RESOURCE_DIR + FXML_SET_PROPERTY;
	}

	public static String getFxmlAdmin(){
		return RESOURCE_DIR + FXML_ADMIN;
	}
	
	public static String getFxmlTransferAmount(){
		return RESOURCE_DIR + FXML_TRANSFER_AMOUNT;
	}
	
	public static String getFxmlBibAdmin(){
		return RESOURCE_DIR + FXML_BIB_ADMIN;
	}
	
	public static String getFxmlBibTransaction(){
		return RESOURCE_DIR + FXML_BIB_TRANSACTION;
	}

	public static String getFxmlSendJson(){
		return RESOURCE_DIR + FXML_SEND_JSON;
	}
}

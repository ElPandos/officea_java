package eu.nets.baxi.gui;

import eu.nets.baxi.client.BaxiCtrl;
import eu.nets.baxi.client.ef.BaxiEF;
import eu.nets.baxi.gui.style.HandleResources;
import eu.nets.baxi.log.FileAccess;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main extends Application {

	
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws IOException {
		final BaxiCtrl baxi;
		if(isCardInfoAll()){
			baxi = new BaxiEF();
		}else {
			baxi = new BaxiCtrl();
		}

		FXMLLoader loader = new FXMLLoader(getClass().getResource(HandleResources.getFxmlMain()));
		stage.setTitle("TestGUI:" + baxi.getVersion());
		stage.setScene(new Scene((Pane) (loader.load())));
		HandleResources.LoadStylesheet(stage.getScene());
		MainController controller = loader.getController();
		controller.initData(baxi);
		stage.sizeToScene();
		stage.centerOnScreen();
		stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent event) {
		       if(baxi.isOpen()){
		    	   baxi.close();
		       }
		    }
		});
		
		stage.show();
	}

	private boolean isCardInfoAll() {
		final String cardInfoKey = "CardInfoAll=";
		int retVal = -1;
		FileReader iniFile;
		try {
			iniFile = new FileReader("baxi.ini");
			BufferedReader br = new BufferedReader(iniFile);
			String line;
			try {
				while ((line = br.readLine()) != null) {
					if (line.startsWith(cardInfoKey)) {
						String val = line.substring(cardInfoKey.length());
						retVal = Integer.parseInt(val);
					}
				}
				br.close();
			} catch (IOException ex) {
				Logger.getLogger(FileAccess.class.getName()).log(Level.INFO, null, ex);
			}

		} catch (FileNotFoundException ex) {
			Logger.getLogger(FileAccess.class.getName()).log(Level.INFO,"ini-file not present!");
		}

		return retVal==1;
	}

}

/**
 * Song Library
 * Abid Azad
 * Diego Castellanos
 * */
package songlibrary.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import songlibrary.controller.SongLibController;

public class SongLib extends Application{

	public static void main(String[] args) {
		
		launch(args);
	}
	
	public void start(Stage primaryStage){
		try {		
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/songlibrary/views/SongLibrary.fxml"));
			
			// load the fxml
			AnchorPane root = (AnchorPane)loader.load();

			// get the controller (Do NOT create a new Controller()!!)
			// instead, get it through the loader
			SongLibController listController = loader.getController();
			listController.start(primaryStage);
		
		
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}

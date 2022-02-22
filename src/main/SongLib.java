// Diego Castellanos dac392
// Abid Azad aa2177
package main;

import controller.SongLibController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class SongLib extends Application{

	public static void main(String[] args) {
		
		launch(args);
	}
	
	public void start(Stage primaryStage){
		try {		
			FXMLLoader loader = new FXMLLoader();   
			loader.setLocation(getClass().getResource("/views/SongLibrary.fxml"));

			AnchorPane root = (AnchorPane)loader.load();

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

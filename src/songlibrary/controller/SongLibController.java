package songlibrary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class SongLibController {

	// buttons
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button exitModalBtn;
    @FXML private Button submitBtn;
    
    //TextFields
    @FXML private TextField titleText;
    @FXML private TextField artistText;
    @FXML private TextField albumText;
    @FXML private TextField yearText;
    
    // labels
    @FXML private Label albumLabel;			// required
    @FXML private Label artistLabel;		// required
    @FXML private Label releasedateLabel;	// "unknown" if not set
    @FXML private Label titleLabel;			// "unknown" if not set
    
    // elements
    @FXML private VBox list;
    @FXML private GridPane modalContainer;

    @FXML
    void addSong(ActionEvent event) {
    	System.out.println("added a song");
    	showModalView(event);
    }

    @FXML
    void deleteSong(ActionEvent event) {
    	System.out.println("deleted a song");
    }

    @FXML
    void editSong(ActionEvent event) {
    	System.out.println("edited a song");
    	showModalView(event);
    }
    
    @FXML
    void submit(ActionEvent event) {
    	if(titleText.getText().isEmpty()  || artistText.getText().isEmpty())
    	{
    		Alert alert = new Alert(AlertType.INFORMATION);
    		alert.setTitle("ERROR!");
    		alert.setHeaderText("YOU FUCKED UP!");
    		alert.setContentText("You're missing stuff!");
    		alert.showAndWait();
    	}
    	else {    	
    	Song test = new Song(titleText.getText(), artistText.getText(), albumText.getText(), Integer.parseInt(yearText.getText()));
    	

    	
    	System.out.println(test.getTitle());
    	System.out.println(test.getArtist());
    	System.out.println(test.getAlbum());
    	System.out.println(test.getYear());
    	modalContainer.setVisible(false);
    	modalContainer.setOpacity(0);
    	}
    }
    
    @FXML
    void exitModalView(ActionEvent event) {
  
    	modalContainer.setVisible(false);
    	modalContainer.setOpacity(0);
    }
    
    @FXML
    void showModalView(ActionEvent event) {
    	modalContainer.setVisible(true);
    	modalContainer.setOpacity(1);
    }

}

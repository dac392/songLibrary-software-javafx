package songlibrary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class SongLibController {

	// buttons
    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    @FXML private Button exitModalBtn;
    @FXML private Button submitBtn;
    
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

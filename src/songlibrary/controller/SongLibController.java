package songlibrary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class SongLibController {

    @FXML private Button addButton;
    @FXML private Button deleteButton;
    @FXML private Button editButton;
    
    @FXML private Label albumLabel;
    @FXML private Label artistLabel;
    @FXML private VBox list;
    @FXML private Label releasedateLabel;
    @FXML private Label titleLabel;

    @FXML
    void addSong(ActionEvent event) {
    	System.out.println("added a song");
    }

    @FXML
    void deleteSong(ActionEvent event) {
    	System.out.println("deleted a song");
    }

    @FXML
    void editSong(ActionEvent event) {
    	System.out.println("edited a song");
    }
    

}

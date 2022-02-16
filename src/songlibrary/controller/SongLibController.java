package songlibrary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import org.json.JSONArray;
import org.json.JSONException;

import org.json.JSONObject;

import javafx.collections.FXCollections;

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
    
    @FXML private ListView<String> songsList;
    private ObservableList<String> obsList = FXCollections.observableArrayList(); 
    private JSONArray data; // might not be a JSONArray, might need something else
   
    
    public void start(Stage mainStage) {
    	// will do some basic set up before the program starts
        // use org.json to parse json
    	
		try {
			String location = "src/songlibrary/controller/listData.json";
			String jsonString = new String(Files.readAllBytes(Paths.get(location)));
			JSONObject parser = new JSONObject(jsonString);
			data = parser.getJSONArray("songs");
	    	
			for(int i = 0; i < data.length(); i++) {
				JSONObject b = data.getJSONObject(i);
				Song a = new Song(b.optString("title"),b.optString("artist"),b.optString("album"),b.optString("year"), 0);
				obsList.add(a.toString());
			}
			obsList.sort(null);
			songsList.setItems(obsList);

	    	System.out.println(data.toString());  // you can remove this, just used it for debuggin
	    	
	    	songsList		
	    	.getSelectionModel()
			.selectedIndexProperty()
			.addListener( (obs, oldVal, newVal) -> select(mainStage));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Sorry, this is not a valid location");
			e.printStackTrace();
			
		}
		catch(JSONException e) {
			e.printStackTrace();
		}


    }
    
    private void addSong(Optional<String[]> songInfo) {
        // This function no longer an event listenter. it gets called from submit
    	if(songInfo.isPresent()) {
    		Song song = new Song(songInfo.get(), obsList.size());
    		if(song.canBeAdded(obsList)) {
    			obsList.add(song.toString());
    			obsList.sort(null);
    			songsList.setItems(obsList);
        		exitModalView();
            	formCleanUp();
            	
        		int newIndex = obsList.indexOf(song.toString());
        		song.setListIndex(newIndex);
        		songsList.getSelectionModel().select(newIndex);
        		
        		//wrong
        		try {
        			JSONObject input = new JSONObject();
            		input.put("title", song.getTitle());
            		input.put("artist", song.getArtist());
            		input.put("album", song.getAlbum());
            		input.put("year", song.getYear());
            		
            		data.put(input);
            	
            		
            		FileWriter file = new FileWriter("src/songlibrary/controller/listData.json");
            		file.write("{songs: "+data+"}");
            		file.flush();
            		file.close();

        		}catch(JSONException e) {
        			e.printStackTrace();
        		}
        		catch(IOException e) {
        			e.printStackTrace();
        		}
        		 

//        		data.put(song);
//        		System.out.println(data.toString());
        		
        		

    		}else {
    			showAlert("Error!", "Duplicate song found", "Cannot add the same song more than once.");
        	}
    		

    	}
    	
    	
    }


	@FXML void deleteSong(ActionEvent event) {
    	System.out.println("deleted a song");
    }

    @FXML
    void editSong(ActionEvent event) {
    	System.out.println("edited a song");
    	showModalView();
    }
    
    @FXML void submit(ActionEvent event) {
    	// event listener on submit button. 
    	Optional<String[]> songInformation = Optional.empty();
    	if(titleText.getText().isEmpty()  || artistText.getText().isEmpty()){
    		showAlert("Error!", "Title or Artist missing", "Song and artist name required to add a song.");
    	}else {
    		String newSongInfo[] = {titleText.getText().strip(), artistText.getText().strip(), albumText.getText().strip(), yearText.getText().strip()};
    		songInformation = Optional.of(newSongInfo);
    	}
    	
    	addSong(songInformation);
    	

    	
    	addSong(songInformation);
    	
    }
    
    
    
  //Helper Functions:
    
    private void showAlert(String title, String header, String content) {
        // just a function to use if we need to show alerts
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		
	}
    
	@FXML
    void exitModalView() {


    	modalContainer.setVisible(false);
    	modalContainer.setOpacity(0);
    	
    }
    
    @FXML
    void showModalView() {
        // set this as an event listener for add button
    	modalContainer.setVisible(true);
    	modalContainer.setOpacity(1);
    }

	private void select(Stage mainStage) {		//reminder, there might be a bug here             
		// this function technically works but if you make some debugging statements, it looks like it doesn't
        String song = songsList.getSelectionModel().getSelectedItem();
		if(song != null) {
			
			String info[] = song.split(" ");
	    	titleLabel.setText(info[0]);
	    	artistLabel.setText(info[1]);
	        albumLabel.setText(info[2]);
	        releasedateLabel.setText(info[3]);
		}


	}
    
    private void formCleanUp() {
        // removes text from the form fields after submitting
        titleText.clear();
        artistText.clear();
        albumText.clear();
        yearText.clear();
    }
    
    private void debugAdd(Song test) {
        // just print statements. not important. delete if you'd like
    	System.out.println(test.getTitle());
    	System.out.println(test.getArtist());
    	System.out.println(test.getAlbum());
    	System.out.println(test.getYear());
    }


}

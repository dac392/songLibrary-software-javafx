/**
 * Song Library
 * Abid Azad
 * Diego Castellanos
 * */

package songlibrary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ListView;
import javafx.collections.ObservableList;

import java.io.FileWriter;
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
    @FXML private Label albumLabel;
    @FXML private Label artistLabel;
    @FXML private Label releasedateLabel;
    @FXML private Label titleLabel;
    @FXML private Label mode;
    
    // elements
    @FXML private VBox list;
    @FXML private GridPane modalContainer;
    
    @FXML private ListView<String> songsList;
    private ObservableList<String> obsList = FXCollections.observableArrayList(); 
    private JSONArray data;
    private int instruction = -1;
    private final int ADD = 0;
    private final int EDIT = 1;
    private String oldTitle ="";
    private String oldArtist="";

   
    
    public void start(Stage mainStage) {
    	// will do some basic set up before the program starts
    	
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

			obsList.sort(String.CASE_INSENSITIVE_ORDER);
			songsList.setItems(obsList);

	    	songsList		
	    	.getSelectionModel()
			.selectedIndexProperty()
			.addListener( (obs, oldVal, newVal) -> select(mainStage));
	    	if(data.length() > 0)
	    		songsList.getSelectionModel().select(0);
		} catch (Exception e) {
			System.out.println("Sorry, this is not a valid location");
		}

    }
    
	@FXML void exitModalView() {

    	modalContainer.setVisible(false);
    	modalContainer.setOpacity(0);
    	instruction = -1;
    	formCleanUp();
    	
    }
     
    @FXML void showModalView(ActionEvent event) {
    	boolean edit = event.getTarget().toString().contains("Edit");
    	instruction = (edit)? EDIT : ADD;
    	if(edit && fetchEditInfo()) {
    		instruction = EDIT;
    		mode.setText("Editing a Song");
    		modalContainer.setVisible(true);
        	modalContainer.setOpacity(1);
    		
    	}else if(!edit){
    		instruction = ADD;
    		mode.setText("Adding a Song");
    		modalContainer.setVisible(true);
        	modalContainer.setOpacity(1);
    	}
    	
    }
    
    @FXML void submit(ActionEvent event) {
    	// event listener on submit button. 
    	Optional<String[]> songInformation = Optional.empty();
    	if(titleText.getText().isEmpty()  || artistText.getText().isEmpty() || titleText.getText().trim().length() == 0 || artistText.getText().trim().length()==0){
    		showAlert("Error!", "Title or Artist missing", "Song and artist name required to add a song.");
    	}else if( (!yearText.getText().isEmpty() && !yearText.getText().equals("unknown")) && !yearText.getText().matches("[0-9]+")) {
    		showAlert("Error!", "Invalid Year!", "Please enter a valid year.");
    	}else {
    		String newSongInfo[] = {titleText.getText().strip(), artistText.getText().strip(), albumText.getText().strip(), yearText.getText().strip()};
    		songInformation = Optional.of(newSongInfo);
    		if(instruction == ADD) {
    			addSong(songInformation);
    		}else if(instruction == EDIT) {
    			editSong(songInformation);
    		}else {
    			System.err.println("Warning: Instruction was not set");
    		}
    		
    	}
    }
    
    private void addSong(Optional<String[]> songInfo) {
    	if(songInfo.isPresent()) { 
    	
    		Song song = new Song(songInfo.get(), obsList.size());
    		if(song.canBeAdded(obsList)) {  			    			
    				obsList.add(song.toString());
    				try {
    					obsList.sort(String.CASE_INSENSITIVE_ORDER);
    				}catch(Exception e) {
    					System.err.println("UNEXPECTED: addSong");
    				}
    				songsList.setItems(obsList);
    				addToJson(song, null);
    				int newIndex = obsList.indexOf(song.toString());
	        		songsList.getSelectionModel().select(newIndex);
    				exitModalView();
	    	}else {	
    			showAlert("Error!", "Duplicate song found", "Cannot add the same song more than once.");
        	}
    	}

    }
    
    private void editSong(Optional<String[]> songInfo) {
    	if(songInfo.isPresent()) {
        	

	    	Song song = new Song(songInfo.get(), obsList.size());
	    	if(song.canBeAdded(obsList, oldTitle, oldArtist)){
	    		try {
	    		int a = songsList.getSelectionModel().getSelectedIndex();
	    		addToJson(song, data.getJSONObject(a));
        		sortData();	// i think i can delete this.
        		
        		obsList.set(a, song.toString());
        		obsList.sort(String.CASE_INSENSITIVE_ORDER);
        		songsList.setItems(obsList);
        		a = obsList.indexOf(song.toString());
        		titleLabel.setText(data.getJSONObject(a).getString("title"));
        		artistLabel.setText(data.getJSONObject(a).getString("artist"));
        	    albumLabel.setText(data.getJSONObject(a).getString("album"));
        	    releasedateLabel.setText(data.getJSONObject(a).getString("year"));
        		exitModalView();

				songsList.getSelectionModel().select(a);	
	    		}catch(JSONException e) {
	    			e.printStackTrace();
	    		}
	    	}else {	
				showAlert("Error!", "Duplicate song found", "Cannot add the same song more than once.");
	    	}
    	}
    }
    
	@FXML void deleteSong(ActionEvent event) {
    	
    	int a = songsList.getSelectionModel().getSelectedIndex();
    	if(a > -1){
    	
    	try {
    		Alert warning = new Alert(AlertType.WARNING, "Deleting "+data.getJSONObject(a).getString("title")+" by "+data.getJSONObject(a).getString("artist")+ ". Are you sure?", ButtonType.OK, ButtonType.CANCEL);
    		warning.setTitle("WARNING");
    		Optional<ButtonType> answer = warning.showAndWait();
    		if(answer.get() == ButtonType.OK){
    			data.remove(a);
    			FileWriter file = new FileWriter("src/songlibrary/controller/listData.json");
    			file.write("{\"songs\": "+data+"}");
    			file.flush();
    			file.close();  			
    			obsList.remove(a);
    			if(a > -1) {
    				sortData();
    				obsList.sort(String.CASE_INSENSITIVE_ORDER);      				
    			}
    		
    			if(data.length() == 0) {
    				
    				titleLabel.setText("unknown");
    		    	artistLabel.setText("unknown");
    		        albumLabel.setText("unknown");
    		        releasedateLabel.setText("unknown");
    			}

    			if( (a+1) <= data.length() && a!=0){
    	    		songsList.getSelectionModel().selectNext();
    			}
    			songsList.setItems(obsList);
    		}
    	
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
    	
    	}
    	

    }
    private void addToJson(Song song, JSONObject input) {
		if(input == null) {
			input = new JSONObject();
		}

    	try {
    		input.put("title", song.getTitle());
    		input.put("artist", song.getArtist());
    		input.put("album", song.getAlbum());
    		input.put("year", song.getYear());
    		if(instruction == ADD) {
    			data.put(input);
    			sortData();
    		}    		
			FileWriter file = new FileWriter("src/songlibrary/controller/listData.json");
			file.write("{\"songs\": "+data+"}");
			file.flush();
			file.close();
		
		}catch(Exception e) {
			System.err.println("Unexpected result in addToJson");
		}
			
    }
    
    private boolean fetchEditInfo() {
    	int a = songsList.getSelectionModel().getSelectedIndex();
    	if(a > -1) {
    		try {
	    		titleText.setText(data.getJSONObject(a).getString("title"));
	    		artistText.setText(data.getJSONObject(a).getString("artist"));
	    		albumText.setText(data.getJSONObject(a).getString("album"));
	    		yearText.setText(data.getJSONObject(a).getString("year"));
	    		oldTitle = data.getJSONObject(a).getString("title");
	    		oldArtist = data.getJSONObject(a).getString("artist");
	    		return true;
    		}catch(JSONException e) {
    			e.printStackTrace();
    			return false;
    		}
    	}else {
    		showAlert("Error","Empty list", "Cannot edit an empty list, please add a song first.");
    	}
    	return false;
    }



	private void select(Stage mainStage) {
        String song = songsList.getSelectionModel().getSelectedItem();
        
        int a = songsList.getSelectionModel().getSelectedIndex();
        
		if(song != null) {
			try {
		    	titleLabel.setText(data.getJSONObject(a).getString("title"));
		    	artistLabel.setText(data.getJSONObject(a).getString("artist"));
		        albumLabel.setText(data.getJSONObject(a).getString("album"));
		        releasedateLabel.setText(data.getJSONObject(a).getString("year"));
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}	


	}
	
    private void showAlert(String title, String header, String content) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(header);
		alert.setContentText(content);
		alert.showAndWait();
		
	}
    
    private void formCleanUp() {
        titleText.clear();
        artistText.clear();
        albumText.clear();
        yearText.clear();
       
    }
    
    private void sortData() {
    	boolean titleMatch = false;
    	String a;
    	String b;
    	for(int i = 0; i < data.length(); i++)
    	{
    		for(int j = i+1; j < data.length(); j++)
    		{
    			try {
    				if(!titleMatch)
    				{
    					a = data.getJSONObject(j).optString("title").toLowerCase();
       				 	b = data.getJSONObject(i).optString("title").toLowerCase();
    				}
    				else
    				{
    					a = data.getJSONObject(j).optString("artist").toLowerCase();
       				 	b = data.getJSONObject(i).optString("artist").toLowerCase();
       				 	
       			
    				}
    				 
    				 if(a.compareTo(b)<0)
    				 {
	    					JSONObject temp = new JSONObject();
	    					temp = data.getJSONObject(i);
	    					data.put(i, data.get(j));
	    					data.put(j, temp);
    				
    				}
    				if(a.compareTo(b) == 0)
    				{
    					a = data.getJSONObject(j).optString("artist").toLowerCase();
       				 	b = data.getJSONObject(i).optString("artist").toLowerCase();
       				 	
       				 if(a.compareTo(b)<0) {
       				 	JSONObject temp = new JSONObject();
       				 	temp = data.getJSONObject(i);
 						data.put(i, data.get(j));
 						data.put(j, temp);
    				}
    			}
    			}catch(JSONException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
   
    }

}

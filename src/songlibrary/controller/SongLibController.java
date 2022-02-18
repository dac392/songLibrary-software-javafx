package songlibrary.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
    private boolean editing = false;
   
    
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
    	if(songInfo.isPresent() && !editing) {
    		Song song = new Song(songInfo.get(), obsList.size());
    		if(song.canBeAdded(obsList)) {
    			obsList.add(song.toString());
    		//	obsList.sort(null);
    			songsList.setItems(obsList);
        		exitModalView();
            	formCleanUp();
            	
        		int newIndex = obsList.indexOf(song.toString());
        		song.setListIndex(newIndex);
        		
        		
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
        		songsList.getSelectionModel().select(newIndex);
        		 
//        		data.put(song);
//        		System.out.println(data.toString());
        		
        		

    		}
    	}
    		else if(editing){
        		Song song = new Song(songInfo.get(), obsList.size());

    			try {
    		        int a = songsList.getSelectionModel().getSelectedIndex();

    				data.getJSONObject(a).put("title", song.getTitle());
    				data.getJSONObject(a).put("artist", song.getArtist());
    				data.getJSONObject(a).put("album", song.getAlbum());
    				data.getJSONObject(a).put("year", song.getYear());
    				
    				editing = false;
    				FileWriter file = new FileWriter("src/songlibrary/controller/listData.json");
            		file.write("{songs: "+data+"}");
            		file.flush();
            		file.close();
            		obsList.set(a, song.toString());
            		//	obsList.sort(null);
            		songsList.setItems(obsList);
            		titleLabel.setText(data.getJSONObject(a).getString("title"));
            		artistLabel.setText(data.getJSONObject(a).getString("artist"));
            	    albumLabel.setText(data.getJSONObject(a).getString("album"));
            	    releasedateLabel.setText(data.getJSONObject(a).getString("year"));
            		exitModalView();
            		formCleanUp();
    			}catch(JSONException e) {
    				e.printStackTrace();
    				
    			}catch(IOException e) {
    				e.printStackTrace();
    			}
    			
    		}else {
    		
    		
    			showAlert("Error!", "Duplicate song found", "Cannot add the same song more than once.");
        	}
    		

    	}
    	
    	
    


	@FXML void deleteSong(ActionEvent event) {
    	System.out.println("deleted a song");
    	//Assuming select works fine.
    	
    	int a = songsList.getSelectionModel().getSelectedIndex();
    	if(a > -1 && songsList.getItems().size() > 1)
    	{
    	Alert warning = new Alert(AlertType.WARNING, "Are you sure?", ButtonType.OK, ButtonType.CANCEL);
    	warning.setTitle("WARNING");
    	Optional<ButtonType> answer = warning.showAndWait();
    	if(answer.get() == ButtonType.OK)
    	{
    	try {
    	data.remove(a);
    	FileWriter file = new FileWriter("src/songlibrary/controller/listData.json");
		file.write("{songs: "+data+"}");
		file.flush();
		file.close();
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
    	songsList.getItems().remove(a);
    	
    	}
    	}
    	else {
    		System.out.println("JK");
    	}
    }

    @FXML void editSong(ActionEvent event) {
    	int a = songsList.getSelectionModel().getSelectedIndex();
    	if(a > -1)
    	{
    		try {
    		titleText.setText(data.getJSONObject(a).getString("title"));
    		artistText.setText(data.getJSONObject(a).getString("artist"));
    		albumText.setText(data.getJSONObject(a).getString("album"));
    		yearText.setText(data.getJSONObject(a).getString("year"));
    		System.out.println("edited a song");
    		modalContainer.setVisible(true);
    		modalContainer.setOpacity(1);
    		editing = true;
    		}catch(JSONException e) {
    			e.printStackTrace();
    		}
    	}
 
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
    	formCleanUp();
    	editing = false;
    	
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
        
        int a = songsList.getSelectionModel().getSelectedIndex();
        
		if(song != null) {
			try {
			String info[] = song.split(" ");
	    	titleLabel.setText(data.getJSONObject(a).getString("title"));
	    	artistLabel.setText(data.getJSONObject(a).getString("artist"));
	        albumLabel.setText(data.getJSONObject(a).getString("album"));
	        releasedateLabel.setText(data.getJSONObject(a).getString("year"));
			}catch (JSONException e) {
				e.printStackTrace();
			}
		}
		
	


	}
    
    private void formCleanUp() {
        // removes text from the form fields after submitting
        titleText.clear();
        artistText.clear();
        albumText.clear();
        yearText.clear();
        System.out.println("This went throught");
    }
    
    private void debugAdd(Song test) {
        // just print statements. not important. delete if you'd like
    	System.out.println(test.getTitle());
    	System.out.println(test.getArtist());
    	System.out.println(test.getAlbum());
    	System.out.println(test.getYear());
    }


}

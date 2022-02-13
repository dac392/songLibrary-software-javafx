package songlibrary.controller;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;

public class Song {
	
	private String title;
	private String artist;
	private String album = "unknown";
	private String date = "unknown";
	private int listIndex;
	
	
	public Song(String title, String artist, String album, String date, int listIndex) {
		
		this.title = title;
		this.artist = artist;
		
		if(!album.equals("")) {
			this.album = album;
		}
		
		if(dateValidation(date)) {
			this.date = date;
		}
		this.setListIndex(listIndex);
		
	}
	
	public Song(String[] info, int index) {
		this.listIndex = index;
		this.title = info[0];
		this.artist = info[1];
		this.album = (!info[2].equals(""))? info[2]:"unknown";
		this.date = (!info[3].equals(""))? info[3]:"unknown";
	}

	private boolean dateValidation(String date) {
		String regex = "^(1[0-2]|0[1-9])/(3[01]|[12][0-9]|0[1-9])/[0-9]{4}$";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(date);
		return matcher.matches();
	}
	
	public String getTitle() {
		return title;
	}
	
	public String getArtist() {
		return artist;
	}	
	
	public String getAlbum() {
		return album;
	}	
	
	public String getYear() {
		return date;
	}
	
	public void setTitle(String a) {
		if(a != null)
		title = a;
	}
	
	public void setArtist(String a) {
		if(a != null)
		artist = a;
	}	
	
	public void setAlbum(String a) {
		if(a != null)
		album = a;
	}	
	
	public void setYear(String a) {
		if(dateValidation(a)) {
			date = a;
		}
		
		
	}
	public String toString() {
		return this.getTitle()+" "+this.getArtist()+" "+this.getAlbum()+" "+this.getYear()+" "+this.getListIndex();
	}

	public int getListIndex() {
		return listIndex;
	}

	public void setListIndex(int listIndex) {
		this.listIndex = listIndex;
	}


	public boolean canBeAdded(ObservableList<String> obslist) {
		if(obslist.size()>0) {
			String songTitle = this.title.toLowerCase();
			String songArtist = this.artist.toLowerCase();
			
			for(int i = 0; i < obslist.size(); i++) {
				String element = obslist.get(i);
				String songInList[] = element.toLowerCase().split(" ");
				if( songInList[0].equals(songTitle) && songInList[1].equals(songArtist)) {
					return false;
				}
			}
		}

		
		return true;
	}
}

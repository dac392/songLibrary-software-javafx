package controller;


import javafx.collections.ObservableList;


public class Song {
	
	private String title;
	private String artist;
	private String album;
	private String year;
	private int listIndex;
	
	
	public Song(String title, String artist, String album, String year, int listIndex) {
		
		this.title = title;
		this.artist = artist;
		
		this.album = "Unknown";
		if(!album.equals("")) {
			this.album = album;
		}
		
		this.year = "Unknown";
		if(!year.equals("")&&! year.toLowerCase().equals("unknown") )
		{
			int y = Integer.parseInt(year);
			this.year = (y > 0)? year : "unknown";
			this.setListIndex(listIndex);
		}
	}
	

	public Song(String[] info, int index) {
		int d = -1;
		if(!info[3].equals("") && intChecker(info[3]) == 0) {
			d = Integer.parseInt(info[3]);
		}
		
		this.listIndex = index;
		this.title = info[0];
		this.artist = info[1];
		this.album = (!info[2].equals(""))? info[2]:"unknown";
		this.year = (d > 0)? info[3] : "unknown";
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
		return year;
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
		if(Integer.parseInt(a)>0) {
			year = a;
		}
		
	}
	public String toString() {
		String song = this.getTitle();	
		String artist = this.getArtist();
		return (song+"\n"+artist);
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
				String songInList[] = element.toLowerCase().split("\n");
				if( songInList[0].equals(songTitle) && songInList[1].equals(songArtist)) {
					return false;
				}
				
			}
		}

		return true;
	}
	
 	public boolean canBeAdded(ObservableList<String> obslist, String oldTitle, String oldArtist) {
 		if(this.title.toLowerCase().equals(oldTitle.toLowerCase()) && this.artist.toLowerCase().equals(oldArtist.toLowerCase()))
 			return true;
 		if(obslist.size()>0) {
 			String songTitle = this.title.toLowerCase();
 			String songArtist = this.artist.toLowerCase();
 			for(int i = 0; i < obslist.size(); i++) {
 				String element = obslist.get(i);
 				String songInList[] = element.toLowerCase().split("\n");
 				if( songInList[0].equals(songTitle) && songInList[1].equals(songArtist)) {
 					return false;
 				}			
 			}
 		}
 		return true;
 	}
	
	private int intChecker(String potentialNum) {
		int d = 0;
		for(int i = 0; i < potentialNum.length(); i++) {
			if(potentialNum.charAt(i)<'0'| potentialNum.charAt(i)>'9') {
				d = -1;
				break;
			}
		}
		return d;
	}

}

package songlibrary.controller;

public class Song {
	
	String title;
	String artist;
	String album = "unknown";
	int year = -1;
	
	public Song(String title, String artist, String album, int year) {
		
		this.title = title;
		this.artist = artist;
		
		if(album != null) {
			this.album = album;
		}
		
		if(year != -1) {
			this.year = year;
		}
		
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
	
	public int getYear() {
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
	
	public void setYear(int a) {
		if(a > 0)
		year = a;
		
	}	
}

// James Litton jkl135 & Gianna Aprile gna11


package view;


public class Song {
	
	String title, artist, album, year;
	
	public Song(String title, String artist, String album, String year)
	{
		this.title=title;
		this.artist=artist;
		this.album=album;
		this.year=year;
	}
	
	public String getTitle() {
		return this.title;
	}	
	public String getArtist() {
		return this.artist;
	}
	public String getAlbum() {
		return this.album;
	}
	public String getYear() {
		return this.year;
	}
	
	@Override
	public String toString(){
	    return (this.title + " - " + this.artist);
	}
	
	public String getSongInfo() {
		return this.title + " - " + this.artist + " - " + this.album + " - " + this.year;
	}
	
	public String getFileFormat() {
		return this.title + "," + this.artist + "," + this.album + "," + this.year;
	}
	
	@Override
	public boolean equals(Object otherSong)
	{
		if (this == otherSong) return true;
            
        if (otherSong == null) return false;
            
        if (getClass() != otherSong.getClass()) return false;
        
        Song s2 = (Song) otherSong;
        
        if(!this.artist.toUpperCase().equals(s2.artist.toUpperCase())) return false;
        
        if(!this.title.toUpperCase().equals(s2.title.toUpperCase())) return false;
        
		return true;
	}
}


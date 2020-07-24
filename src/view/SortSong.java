// James Litton jkl135 & Gianna Aprile gna11

package view;

import java.util.Comparator;

public class SortSong implements Comparator<Song>{

	@Override
	public int compare(Song o1, Song o2) {
		// Create comparison logic where if title is the same, it is next sorted by artist
		
		int titleComp = o1.title.toUpperCase().compareTo(o2.title.toUpperCase());
	    
	    if(titleComp != 0)
	    {
	    	return titleComp;
	    }
	    else
	    {
	    	return o1.artist.toUpperCase().compareTo(o2.artist.toUpperCase());
	    }
	}

}

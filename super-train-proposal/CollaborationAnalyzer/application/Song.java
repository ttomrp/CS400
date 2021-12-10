package application;

/*
 * Basic Song class
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * @author JonSchool
 *
 */
public class Song implements Comparable<Song> {
	private final String title;
	private final String genre;
	private final ArrayList<String> artists;
	
	/**
	 * Constructor for a new Song
	 * @param title title of the song
	 * @param genre genre of the song
	 * @param artists unique collaborators on the song, the first of which is the primary artist
	 * @throws InvalidSongException if:
	 * 		-any of the parameters is null
	 * 		-the genre or title is blank
	 * 		-the artists list is empty
	 * 		-the artists list contains a duplicate artist
	 * 		-the artists list contains a null or blank artist
	 */
	public Song(String title, List<String> artists, String genre) throws InvalidSongException {
		if(artists == null || artists.size() == 0) {
			throw new InvalidSongException("at least one artist");
		}
		for(String artist : artists) {
			if(artist == null || artist.equals("")) {
				throw new InvalidSongException("no null/blank artists");
			}
		}
		if(title == null || title.equals("")) {
			throw new InvalidSongException("a non-null, non-blank title");
		}
		if(genre == null || genre.equals("")) {
			throw new InvalidSongException("a non-null, non-blank genre");
		}
		if((new HashSet<String>(artists)).size() != artists.size()) {
			throw new InvalidSongException("no duplicate artists");
		}
		this.title = title;
		this.genre = genre;
		//always store artists in a new list
		//the only real reason for this is to prevent client code from having a handle to our artists list
		this.artists = new ArrayList<String>(artists);
	}
	
	/**
	 * Constructs a new Song object using comma-delimited string of artists 
	 * @param title - must be non-null, non-empty. title of song (e.g., Happy)
	 * @param commaDelimitedArtists - first one is the primary artist, must be non-empty
	 * 		within each comma-delimited piece, any leading/trailing whitespace is removed
	 * @param genre - must be non-null, non-empty. genre of song (e.g., R&B, Rock, Blues)
	 * @return the newly constructed Song
	 * @throws InvalidSongException - if the Song is invalid because of blanks, etc.
	 */
	public static Song withCommaDelimitedArtists(String title, String commaDelimitedArtists, String genre) throws InvalidSongException {
		LinkedList<String> trimmedArtists = new LinkedList<String>();
		for(String artist : Arrays.asList(commaDelimitedArtists.split(","))) {
			trimmedArtists.add(artist.trim());
		}
		return new Song(title, trimmedArtists, genre); 
	}
	
	/**
	 * Gets the title of the song
	 * @return the song's title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Gets the genre of the song
	 * @return the song's genre
	 */
	public String getGenre() {
		return genre;
	}
	
	/**
	 * Get comma+space-separated list of artist names for this Song
	 * @return comma-delimited string of artist names
	 */
	public String getCommaDelimitedArtistNames() {
		return String.join(", ", this.getArtists());
	}

	/**
	 * Gets the list of artists on the song
	 * @return list of artists' names
	 */
	public List<String> getArtists() {
		//always return a new ArrayList to avoid client code modifying the internal structure
		return new ArrayList<String>(this.artists);
	}
	
	//public List<String>
	
	/**
	 * Gets the song's primary artist
	 * @return the primary artist's name
	 */
	public String getPrimaryArtist() {
		//primary artist is the first artist, and we are guaranteed to have one by the constructor
		return this.artists.get(0);
	}
	
	/**
	 * Determines if a Song equals another object.
	 * A Song never equals a non-Song object
	 * A Song equals another Song if the two songs' titles and primary artists are identical
	 */
	@Override
	public boolean equals(Object o) {
		if(o instanceof Song) {
			return ((Song) o).compareTo(this) == 0;
		} else {
			return false;
		}
	}

	/**
	 * Provides an ordering for Songs
	 * Songs are sorted first by their titles. Songs with the same title are instead ordered by their primary artists
	 */
	@Override
	public int compareTo(Song o) {
		//how do we compare two Songs?
		//1. by title
		//2. by primary artist
		//IGNORE non-primary artists
		int titlecmp;
		//first compare by title
		titlecmp = this.getTitle().compareTo(o.getTitle());
		if(titlecmp != 0) {
			return titlecmp;
		}
		//then look at the primary artist
		return this.getPrimaryArtist().compareTo(o.getPrimaryArtist());
	}
	
	/**
	 * Generates a hashCode for a Song
	 * codes for Songs that compare equal will always be the same, as required by hashCode()
	 */
	@Override
	public int hashCode() {
		return this.getTitle().hashCode() + 3 * this.getPrimaryArtist().hashCode();
	}
	
	/**
	 * A basic form of searching: checks if the given search string is contained in the Song's title, ignoring case differences
	 * @param titleFilter the search string
	 * @return true if this song matches, otherwise false
	 */
	public boolean searchMatches(String titleFilter) {
		assert(titleFilter != null);
		return this.getTitle().toUpperCase().contains(titleFilter.toUpperCase());
	}
}

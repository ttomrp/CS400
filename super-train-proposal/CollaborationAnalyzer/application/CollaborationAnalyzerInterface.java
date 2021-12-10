package application;

/**
 * An interface for a collaboration analyzer
 */

import java.util.List;

import javafx.beans.value.ObservableValue;

import java.io.File;
import java.io.FileNotFoundException;


/**
 * @author Jon
 *
 */
public interface CollaborationAnalyzerInterface {

	
	/**
	 * Add a song to the collaboraiton analyzer
	 * @param s the Song to add
	 * @throws DuplicateSongException if the Song is already present
	 */
	public void addSong(Song s) throws DuplicateSongException;
	
	/**
	 * Removes a song, if it's present
	 * @param s the Song to add
	 * @return true if a matching song was found and removed, otherwise false
	 */
	public boolean removeSong(Song s);
	
	/**
	 * Gets an alphabetically sorted list of all relevant artists
	 * @return a sorted list of all artists
	 */
	public List<String> getArtistNames();
	
	/**
	 * Gets a list of song titles
	 * @return
	 */
	public List<String> getSongTitles();
	
	
	public ObservableValue<Number> getLibrarySize();
	
	/**
	 * Gets a sorted (first by title, then by primary artist) list of Songs associated with an Artist
	 * @param artist the artist whose songs to get
	 * @return List of Songs associated with the artist
	 */
	public List<Song> getSongList(String artist);
	
	/**
	 * Gets a sorted (first by title, then by primary artist) list of Songs whose titles loosely match a filter string
	 * @param searchStr the filter string
	 * @return the list of Songs that match serachStr
	 */
	public List<Song> getSongListMatching(String searchStr);
	
	/**
	 * Gets a sorted (alphabetically) list of collaborators directly associated with the artist in question
	 * @param artist the artist whose collaborators to get
	 * @return list of collaborating artists' names. Note that the artist will always be in this list
	 */
	public List<String> getCollaboratorList(String artist);
	
	/**
	 * Loads Songs from a .tsv flat file
	 * @param filePath - the file to read in from
	 */
	public void importFile(File file) throws FileNotFoundException;
	
	/**
	 * Saves Songs to a .tsv flat file
	 * @param filePath - the file to write out to
	 */
	public void outputFile(String fileName)  throws FileNotFoundException;
	
	/**
	 * Gets a shortest path through songs from first to second artist
	 * @param artistName1 first artist
	 * @param artistName2 second artist
	 * @return 'path' of Songs from one artist to another through several songs
	 */
	public List<Song> getPathBetweenArtists(String artistName1, String artistName2);
	
	/**
	 * Gets list of songs an artist has collaborated with others on
	 * @param artistName artist to find collaborated songs for
	 * @return 
	 */
	public List<Song> listCollaboratedSongs(String artistName);
	
	public Song getSongForTitleAndArtist(String title, String artist);
	
}

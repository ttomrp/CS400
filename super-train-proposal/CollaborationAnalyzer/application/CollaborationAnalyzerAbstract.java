package application;

import java.io.File;
import java.io.IOException;

/**
 * An interface for a collaboration analyzer
 */

import java.util.List;

import javafx.beans.property.IntegerProperty;

/**
 * Filename: CollaborationAnalyzerAbstract.java
 * 
 * Project: Final Project
 * 
 * Team: a4
 * 
 * Authors: Adam Cook, Felix Lin, Jonathan McMahon, Tomas Perez, Matthias
 * Schmitz
 * 
 * Semester: Fall 2021 Course: CS400
 * 
 * Data structure for Collaboration Analyzer
 */
public abstract class CollaborationAnalyzerAbstract {

	/**
	 * Add a song to the collaboration analyzer
	 * 
	 * @param s the Song to add
	 * @throws DuplicateSongException if the Song is already present
	 */
	protected abstract void addSong(Song s) throws DuplicateSongException;

	/**
	 * Wrapper method to addSong. Called when adding songs manually, not via import.
	 * 
	 * @param title   - title of song
	 * @param artists - comma-delimited string of artists
	 * @param genre   - genre of song
	 * @throws DuplicateSongException if the Song is already present
	 */
	protected abstract void addSong(String title, String artists, String genre) throws DuplicateSongException, InvalidSongException;

	/**
	 * Removes a song, if it's present
	 * 
	 * @param s the Song to add
	 * @return true if a matching song was found and removed, otherwise false
	 */
	protected abstract boolean removeSong(Song s);

	/**
	 * Gets the size of the library. Similar to getSongSetSize, but used for the GUI
	 * 
	 * @return songSet.size() as an Observable Value
	 */
	protected abstract IntegerProperty getLibrarySize();

	/**
	 * Gets an alphabetically sorted list of all relevant artists
	 * 
	 * @return a sorted list of all artists
	 */
	protected abstract List<String> getArtistNames();

	/**
	 * Gets a sorted (first by title, then by primary artist) list of Songs
	 * associated with an Artist
	 * 
	 * @param artist the artist whose songs to get
	 * @return List of Songs associated with the artist
	 */
	protected abstract List<Song> getSongList(String artist);

	/**
	 * Gets a sorted (alphabetically) list of collaborators directly associated with
	 * the artist in question
	 * 
	 * @param artist the artist whose collaborators to get
	 * @return list of collaborating artists' names. Note that the artist will
	 *         always be in this list
	 */
	protected abstract List<String> getCollaboratorList(String artist);

	/**
	 * Loads Songs from a .tsv flat file
	 * 
	 * @param filePath - the file to read in from
	 */
	protected abstract void importFile(File file) throws IOException;

	/**
	 * Exports Songs to a .tsv flat file
	 * 
	 * @param file - the file to export to
	 */
	protected abstract void outputFile(File file) throws IOException ;

	/**
	 * Saves Songs to a .tsv flat file
	 * 
	 * @param fileName - string of the filename to write to
	 */
	protected abstract void outputFile(String fileName) throws IOException;

	/**
	 * Gets a shortest path through songs from first to second artist
	 * 
	 * @param artistName1 first artist
	 * @param artistName2 second artist
	 * @return 'path' of Songs from one artist to another through several songs
	 */
	protected abstract List<Song> getPathBetweenArtists(String artistName1, String artistName2);

	/**
	 * Gets a list of songs similar to a given artist
	 * 
	 * @param artistName artist to find similar songs for
	 * @return List of Song Objects of similar songs
	 */
	protected abstract List<Song> getSimilarSongs(String artistName);

	/**
	 * Given an artist name and song title, finds and returns the Song object
	 * 
	 * @param title  the title of the song to find (must match exactly)
	 * @param artist the artist's name (must match exactly)
	 * @return a Song object, or null if not found
	 */
	protected abstract Song getSongForTitleAndArtist(String title, String artist);

	/**
	 * Given an artist name, returns a list of all artists who are connected to that
	 * artist by any degree of separation
	 * 
	 * @param artist the artist's name (must match exactly)
	 * @return a List of connected artist names
	 */
	protected abstract List<String> getAllConnectedArtists(String artist);

}

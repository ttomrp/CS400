package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 * Class for Collaboration Analyzer backend
 * Will need to be integrated into the existing project and made to implement interface
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Filename: CollaborationAnalyzer.java
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
 * Back-end class that defines the implements our data structure.
 */
public class CollaborationAnalyzer extends CollaborationAnalyzerAbstract {
	private HashMap<String, HashSet<Song>> artistsToSongs; // maps artist names (Strings) to Songs
	private ObservableList<String> artistList; // ObservableList to use for artist selection ComboBoxes. same artists as
												// in artistsTosongs
	private HashSet<Song> songSet; // we cache a set of all our songs, which are also found in artistsToSongs
	private SimpleIntegerProperty librarySize; // track the number of songs in the library

	/**
	 * Private helper to put a Collection of any kind into a sorted arrayList
	 * 
	 * @param <T>        type of data in the lists/collection
	 * @param collection the existing collectiton
	 * @return the new, sorted ArrayList
	 */
	private <T> ArrayList<T> sorted(Collection<T> collection) {
		if (collection != null) {
			ArrayList<T> ret = new ArrayList<T>(collection);
			ret.sort(null); // use 'natural' ordering defined by .compareTo()
			return ret;
		} else
			return new ArrayList<T>();
	}

	/**
	 * Construct a new Collaboration Analyzer with no songs/artists and initialize
	 * data elements
	 * 
	 */
	protected CollaborationAnalyzer() {
		// we just create empty data structures
		this.artistsToSongs = new HashMap<String, HashSet<Song>>();
		this.songSet = new HashSet<Song>();
		this.artistList = FXCollections.observableArrayList();
		this.librarySize = new SimpleIntegerProperty(0);
	}

	/**
	 * Add a song to the collaboration analyzer.
	 * 
	 * @param s the Song to add
	 * @throws DuplicateSongException if the Song is already present
	 */
	@Override
	protected void addSong(Song s) throws DuplicateSongException {
		if (this.songSet.contains(s)) {
			throw new DuplicateSongException();
		}
		this.songSet.add(s);
		this.librarySize.set(songSet.size());
		for (String a : s.getArtists()) {
			// songs by artist is what we have stored or oterhwise just an empty set
			HashSet<Song> artistsSongs = this.artistsToSongs.getOrDefault(a, new HashSet<Song>());
			artistsSongs.add(s);
			this.artistsToSongs.put(a, artistsSongs); // sets HashSet in case we created a new one above
			if (!this.artistList.contains(a))
				this.artistList.add(a);
		}
	}

	/**
	 * Wrapper method to addSong. Called when adding songs manually, not via import.
	 * 
	 * @param title   - title of song
	 * @param artists - comma-delimited string of artists
	 * @param genre   - genre of song
	 * @throws DuplicateSongException if the Song is already present
	 */
	@Override
	protected void addSong(String title, String artists, String genre)
			throws DuplicateSongException, InvalidSongException {
		// first create the artist list by splitting on comma, ignoring whitespace
		ArrayList<String> artistList = new ArrayList<String>(Arrays.asList(artists.split("\\s*,\\s*")));
		Song song = new Song(title, artistList, genre);
		this.addSong(song);
	}

	/**
	 * Removes a song if it's present in the data set
	 * 
	 * @param s the Song to remove, which must not be null
	 * @return true if a matching song was found and removed, otherwise false
	 */
	@Override
	protected boolean removeSong(Song s) {
		boolean found = this.songSet.remove(s);
		this.librarySize.set(songSet.size());
		for (String a : s.getArtists()) {
			if (this.artistsToSongs.containsKey(a)) {
				this.artistsToSongs.get(a).remove(s);
				// delete artists who aren't involved with any songs anymore
				if (this.artistsToSongs.get(a).isEmpty()) {
					// remove from both Artist -> [Songs] mapping & list of artists
					this.artistsToSongs.remove(a);
					this.artistList.remove(a);
				}
			}
		}
		return found;
	}

	/**
	 * Deletes a song from the existing data set if it's present
	 * 
	 * @param title  title of the song to remove (must be exact match)
	 * @param artist one of the artists on the song to remove (must be exact match)
	 * @return true if a matching song was found and removed, otherwise false
	 */
	protected boolean removeSong(String title, String artist) {
		// Get Song object based on title & artists
		Song song = getSongForTitleAndArtist(title, artist);
		if (song != null) {
			// removeSong will throw on a null Song
			return removeSong(song);
		} else {
			return false; // song wasn't found, so can't be removed.
		}
	}

	/**
	 * Gets a Song (not necessarily the only one) matching the given title and
	 * artist
	 * 
	 * @param title  the title of the song to find (must match exactly)
	 * @param artist the artist's name (must match exactly)
	 * @return a Song object or null
	 */
	@Override
	protected Song getSongForTitleAndArtist(String title, String artist) {
		List<Song> songList = getSongList(artist);
		for (Song song : songList) {
			if (title.equals(song.getTitle())) {
				return song;
			}
		}
		return null;
	}

	/**
	 * Gets an alphabetically sorted list of all relevant artists
	 * 
	 * @return a sorted JavaFX ObservableList of all artists
	 */
	@Override
	protected ObservableList<String> getArtistNames() {
		return this.artistList;
	}

	/**
	 * Gets the size of the library. Similar to getSongSetSize, but used for the GUI
	 * 
	 * @return songSet.size() as an Observable Value
	 */
	@Override
	protected IntegerProperty getLibrarySize() {
		return librarySize;
	}

	/**
	 * Gets a sorted (first by title, then by primary artist) list of Songs
	 * associated with an Artist
	 * 
	 * @param artist the artist whose songs to get
	 * @return List of Songs associated with the artist
	 */
	@Override
	protected List<Song> getSongList(String artist) {
		return sorted(this.artistsToSongs.get(artist));
	}


	/**
	 * Gets a sorted (alphabetically) list of collaborators directly associated with
	 * the artist in question
	 * 
	 * @param artist the artist whose collaborators to get
	 * @return list of collaborating artists' names. Note that the artist will
	 *         always be in this list
	 */
	@Override
	protected List<String> getCollaboratorList(String artist) {
		HashSet<String> dedupList = new HashSet<String>();
		for (Song s : this.getSongList(artist)) {
			dedupList.addAll(s.getArtists());
		}
		return sorted(dedupList);
	}

	/**
	 * Imports list of songs from a flat file into the existing collaboration
	 * analyzer adding to whatever songs (if any) are already loaded
	 * 
	 * @param file the File object which contains the data. Must be open for reading
	 */
	@Override
	protected void importFile(File file) throws FileNotFoundException {
		// set up our processor
		MusicLibraryFileProcessor fileProcessor = new MusicLibraryFileProcessor();
		// load file into processor
		HashSet<Song> fileSongSet = fileProcessor.importFile(file);
		Iterator<Song> songIterator = fileSongSet.iterator();
		// keep adding songs as long as there are rows in the file to add
		while (songIterator.hasNext()) {
			try {
				addSong(songIterator.next());
			} catch (DuplicateSongException e) {
				// ignore duplicate song exceptions when adding from file
			}
		}
		return;
	}

	/**
	 * Writes all entered songs to a file of the given name
	 * 
	 * @param fileName the name of the file to save
	 * @throws IOException if something goes wrong
	 */
	@Override
	protected void outputFile(String fileName) throws IOException {
		// creates a new file
		File file = new File(fileName);
		outputFile(file);
	}

	/**
	 * Output library to already-existing output file
	 * 
	 * @param file the file where to export
	 * @throws IOException if something goes wrong
	 */
	@Override
	protected void outputFile(File file) throws IOException {
		MusicLibraryFileProcessor fileProcessor = new MusicLibraryFileProcessor();
		fileProcessor.exportFile(file, songSet);
	}

	/**
	 * Gets the shortest path from artist 1 to artist 2, via collaborations they've
	 * had with other artists.
	 * 
	 * @param artistName1 the artist to start from
	 * @param artistName2 the artist to search for
	 * @return a List of Song objects that connect the 2 artists via the shortest
	 *         possible path. There may be other paths with the same distance, but
	 *         only one will be returned. There is not a particular path you should
	 *         expect this to return in the event of a tie. Returns an empty list if
	 *         the artists are not connected. Returns null if either artist is not
	 *         in the data structure, or the same artist is passed in twice.
	 */
	@Override
	protected List<Song> getPathBetweenArtists(String artistName1, String artistName2) {
		// Null artist names aren't accepted
		if (artistName1 == null || artistName2 == null) {
			return null;
		}
		// If either artist isn't in the analyzer or both artists are the same, quit
		if (artistName1.equals(artistName2) || !artistsToSongs.containsKey(artistName1)
				|| !artistsToSongs.containsKey(artistName2)) {
			return null;
		}

		// Use a class for queue entries so that we know not only the next
		// artist to search, but also store the path of how we got there
		class QueueEntry {
			protected String artist;
			protected ArrayList<Song> path;

			// create a new queue entry based on a string and artist path
			protected QueueEntry(String a, ArrayList<Song> p) {
				artist = a;
				path = p;
			}
		}

		// Initialize a queue and a set to track visited artists
		Queue<QueueEntry> queue = new LinkedList<QueueEntry>();
		HashSet<String> visitedArtists = new HashSet<String>();

		// Add the first artist to the queue to kick things off
		queue.add(new QueueEntry(artistName1, new ArrayList<Song>()));
		visitedArtists.add(artistName1);

		// Go until the queue unwinds, unless we find a path to artist 2
		while (!queue.isEmpty()) {
			QueueEntry queueEntry = queue.remove();

			// Loop through this artist's songs
			for (Song song : this.getSongList(queueEntry.artist)) {

				// Add the song connecting to this next artist
				// to a shallow copy of the existing path
				// make shallow copy
				ArrayList<Song> newPath = new ArrayList<Song>(queueEntry.path);
				// append "current" song to the 'so far' path
				newPath.add(song);

				// Loop through other artists on the song
				for (String nextArtist : song.getArtists()) {
					// Skip any artists that are already visited
					// (which would include the one we're currently on)
					if (visitedArtists.contains(nextArtist)) {
						continue;
					}

					// If we found artist 2, stop and return the path
					if (nextArtist.equals(artistName2)) {
						return newPath;
					}

					// Otherwise, add this next artist to the queue to search
					queue.add(new QueueEntry(nextArtist, newPath));
					// Mark it as visited
					visitedArtists.add(nextArtist);
				}
			}

		}

		// If the queue unwound all the way, we didn't find a path from 1 to 2 :(
		return new ArrayList<Song>();
	}

	/**
	 * Get a sorted list of Songs which are a degree away from the artist in
	 * question
	 * 
	 * @param artistName the name of the artist (must be an exact match)
	 * @return the list of songs on which the passed artist's collaborators worked
	 *
	 */
	@Override
	protected List<Song> getSimilarSongs(String artistName) {
		if (artistName == null) {
			return null;
		}
		List<String> collaborators = getCollaboratorList(artistName);
		// don't want to include the initial artist in the "1-degree away" songs
		collaborators.remove(artistName);
		List<Song> songList = new ArrayList<Song>();
		for (String c : collaborators) {
			List<Song> cSongs = getSongList(c);
			for (Song s : cSongs) {
				// only add songs on which the initial artist didn't work directly
				if (!s.getArtists().contains(artistName)) {
					songList.add(s);
				}
			}
		}
		return sorted(songList);
	}

	/**
	 * Given an artist name, returns a list of all artists who are connected to that
	 * artist by any degree of separation
	 * 
	 * @param artist the artist's name (must match exactly)
	 * @return a List of connected artist names. Returns null for invalid input.
	 *         Returns an empty list if an artist has no collaborators :(
	 */
	@Override
	protected ObservableList<String> getAllConnectedArtists(String artistName) {
		// Must input the name of an artist in the library
		if (artistName == null || !artistsToSongs.containsKey(artistName)) {
			return null;
		}

		// Set up a list to return,
		ArrayList<String> connectedArtists = new ArrayList<String>();
		// a queue for a breadth-first search,
		Queue<String> queue = new LinkedList<String>();
		// and a list of visited artists for that BFS
		HashSet<String> visitedArtists = new HashSet<String>();

		// Add the first artist to the queue to kick things off
		queue.add(artistName);
		visitedArtists.add(artistName);

		// Go until the queue unwinds
		while (!queue.isEmpty()) {
			String queueEntry = queue.remove();

			// Get this artist's list of collaborators and loop through them
			List<String> collaborators = getCollaboratorList(queueEntry);
			for (String collaborator : collaborators) {
				// Skip any artists the search has already evaluated
				if (visitedArtists.contains(collaborator)) {
					continue;
				}

				// Otherwise, add to the queue,
				queue.add(collaborator);
				// the return list,
				connectedArtists.add(collaborator);
				// and the list of visited artists
				visitedArtists.add(collaborator);
			}
		}

		// Sort the list of all connected artists and return it
		return FXCollections.observableArrayList(sorted(connectedArtists));
	}

}

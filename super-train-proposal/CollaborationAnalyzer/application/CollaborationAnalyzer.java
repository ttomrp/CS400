package application;

import java.io.File;
import java.io.FileNotFoundException;
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Jon
 *
 */
public class CollaborationAnalyzer implements CollaborationAnalyzerInterface {
	private HashMap<String, HashSet<Song>> artistsToSongs; // maps artist names (Strings) to Songs
	private ObservableList<String> artistList; // ObservableList to use for artist selection ComboBoxes
	private HashSet<Song> songSet; // we cache a set of all our songs for performance
	private ObservableList<String> songTitles;

	private static <T> ArrayList<T> sorted(Collection<T> collection) {
		if (collection != null) {
			ArrayList<T> ret = new ArrayList<T>(collection);
			ret.sort(null); // use 'natural' ordering defined by .compareTo()
			return ret;
		} else
			return new ArrayList<T>();
	}

	/**
	 * Construct a new Collaboration Analyzer with no songs/artists
	 */
	public CollaborationAnalyzer() {
		this.artistsToSongs = new HashMap<String, HashSet<Song>>();
		this.songSet = new HashSet<Song>();
		this.artistList = FXCollections.observableArrayList();
		this.songTitles = FXCollections.observableArrayList();
	}

	/**
	 * Add a song to the collaboration analyzer
	 * 
	 * @param s the Song to add
	 * @throws DuplicateSongException if the Song is already present
	 */
	@Override
	public void addSong(Song s) throws DuplicateSongException {
		assert (s != null);
		if (this.songSet.contains(s)) {
			throw new DuplicateSongException();
		}
		this.songSet.add(s);
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
	public void addSong(String title, String artists, String genre)
			throws DuplicateSongException, InvalidSongException {
		// TODO finish implementing this method
		// TODO convert comma delimited list to List<String for song constructor
		ArrayList<String> artistList = new ArrayList<String>(Arrays.asList(artists.split("\\s*,\\s*")));
		Song song = new Song(title, artistList, genre);
		this.addSong(song);
	}

	/**
	 * Removes a song, if it's present
	 * 
	 * @param s the Song to add
	 * @return true if a matching song was found and removed, otherwise false
	 */
	@Override
	public boolean removeSong(Song s) {
		assert (s != null);
		boolean found = this.songSet.remove(s);
		for (String a : s.getArtists()) {
			assert (this.artistsToSongs.containsKey(a));
			this.artistsToSongs.get(a).remove(s);
			// delete artists who aren't involved with any songs anymore
			if (this.artistsToSongs.get(a).isEmpty()) {
				this.artistsToSongs.remove(a);
				this.artistList.remove(a);
			}
		}
		return found;
	}

	public boolean removeSong(String title, String artist) {
		// TODO, get Song object based on title & artists
		Song song = getSongForTitleAndArtist(title, artist);
		return removeSong(song);
	}

	@Override
	public Song getSongForTitleAndArtist(String title, String artist) {
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
	 * @return a sorted list of all artists
	 */
	@Override
	public ObservableList<String> getArtistNames() {
		return this.artistList;
	}

	/**
	 * Gets the size of songSet
	 * 
	 * @return size
	 */
	@Override
	public int getSongSetSize() {
		return songSet.size();
	}

	@Override
	public ObservableList<String> getSongSet() {
		for (Song s : songSet) {
			songTitles.add(s.getTitle());
		}
		return songTitles;
	}

	/**
	 * Gets a sorted (first by title, then by primary artist) list of Songs
	 * associated with an Artist
	 * 
	 * @param artist the artist whose songs to get
	 * @return List of Songs associated with the artist
	 */
	@Override
	public List<Song> getSongList(String artist) {
		return sorted(this.artistsToSongs.get(artist));
	}

	/**
	 * Gets a sorted (first by title, then by primary artist) list of Songs whose
	 * titles loosely match a filter string
	 * 
	 * @param searchStr the filter string
	 * @return the list of Songs that match serachStr
	 */
	@Override
	public List<Song> getSongListMatching(String searchStr) {
		HashSet<Song> matchingSet = new HashSet<Song>();
		for (Song s : this.songSet) {
			if (s.searchMatches(searchStr)) {
				matchingSet.add(s);
			}
		}

		return sorted(matchingSet);
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
	public List<String> getCollaboratorList(String artist) {
		HashSet<String> dedupList = new HashSet<String>();
		for (Song s : this.getSongList(artist)) {
			dedupList.addAll(s.getArtists());
		}
		return sorted(dedupList);
	}

	@Override
	public void importFile(File file) throws FileNotFoundException {
		MusicLibraryFileProcessor fileProcessor = new MusicLibraryFileProcessor();
		HashSet<Song> fileSongSet = null;
		fileSongSet = fileProcessor.importFile(file);
		Iterator<Song> songIterator = fileSongSet.iterator();
		while (songIterator.hasNext()) {
			try {
				addSong(songIterator.next());
			} catch (DuplicateSongException e) {
				// ignore duplicate song exceptions when adding from file
			}
		}
		return;
	}

	@Override
	public void outputFile(String fileName) throws FileNotFoundException {
		File file = new File(fileName);
		MusicLibraryFileProcessor fileProcessor = new MusicLibraryFileProcessor();
		fileProcessor.exportFile(file, songSet);
		return;
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
	public List<Song> getPathBetweenArtists(String artistName1, String artistName2) {
		if (artistName1 == null || artistName2 == null) {
			return null;
		}
		if (artistName1.equals(artistName2) || !artistsToSongs.containsKey(artistName1)
				|| !artistsToSongs.containsKey(artistName2)) {
			return null;
		}

		// Use a class for queue entries so that we know not only the next
		// artist to search, but also store the path of how we got there
		class QueueEntry {
			public String artist;
			public ArrayList<Song> path;

			public QueueEntry(String a, ArrayList<Song> p) {
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
			QueueEntry queueEntry = queue.peek();

			// Loop through this artist's songs
			for (Song song : this.getSongList(queueEntry.artist)) {

				// Loop through other artists on the song
				for (String nextArtist : song.getArtists()) {
					// Skip any artists that are already visited
					// (which would include the one we're currently on)
					if (visitedArtists.contains(nextArtist)) {
						continue;
					}

					// Add the song connecting to this next artist
					// to a shallow copy of the existing path
					ArrayList<Song> newPath = new ArrayList<Song>();
					queueEntry.path.forEach((s) -> {
						newPath.add(s);
					});
					newPath.add(song);

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

			// Done searching this artist, pop off the queue
			queue.remove();
		}

		// If the queue unwound all the way, we didn't find a path from 1 to 2 :(
		return new ArrayList<Song>();
	}

	@Override
	public List<Song> listCollaboratedSongs(String artistName) {
		if (artistName == null) {
			return null;
		}
		List<String> collaborators = getCollaboratorList(artistName);
		collaborators.remove(artistName);
		List<Song> songList = new ArrayList<Song>();
		for (String c : collaborators) {
			List<Song> cSongs = getSongList(c);
			for (Song s : cSongs) {
				if (!s.getArtists().contains(artistName)) {
					songList.add(s);
				}
			}
		}
		return sorted(songList);
	}

}

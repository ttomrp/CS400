package application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Filename: CollaborationAnalyzerTest.java
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
 * JUnit test class for CollaborationAnalyzer.java
 */
class CollaborationAnalyzerTest {
	private CollaborationAnalyzer analyzer;

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		this.analyzer = new CollaborationAnalyzer();
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#CollaborationAnalyzer()}.
	 */
	@Test
	void test01_testCollaborationAnalyzerConstructor() {
		CollaborationAnalyzer blankCA = new CollaborationAnalyzer();
		Assert.assertEquals(0, blankCA.getLibrarySize());
		Assert.assertEquals(0, blankCA.getArtistNames().size());
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#addSong(Song)}.
	 */
	@Test
	void test02_testAddSong() throws Exception {
		this.analyzer.addSong("One Song", "first,second,third", "pop");
		Assert.assertEquals(1, this.analyzer.getLibrarySize());
		Assert.assertEquals(3, this.analyzer.getArtistNames().size());
		Assert.assertEquals(this.analyzer.getSongForTitleAndArtist("One Song", "second"),
				this.analyzer.getSongList("second").get(0));
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#removeSong(Song)}.
	 */
	@Test
	void test03_testRemoveSong1() throws Exception {
		this.analyzer.addSong("Two Song", "fourth,fifth,sixth", "rap");
		this.analyzer.removeSong("Two Song", "fourth");
		// it should be empty again
		Assert.assertEquals(0, this.analyzer.getLibrarySize());
		Assert.assertEquals(0, this.analyzer.getArtistNames().size());
		// and there should be no song with the given artist
		Assert.assertEquals(0, this.analyzer.getSongList("fourth").size());
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#importFile(java.lang.String)}.
	 */
	@Test
	void test04_ImportFile() throws Exception {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#outputFile(java.lang.String)}.
	 */
	@Test
	void test05_OutputFile() throws Exception {
		fail("Not yet implemented");
	}

	/**
	 * Test method for
	 * {@link CollaborationAnalyzer#getPathBetweenArtists(java.lang.String, java.lang.String)}.
	 * Verifies we get the proper path between artists
	 */
	@Test
	void test06_GetPathBetweenArtists() {
		try {
			// Add A-B and verify path of size 1
			//
			// A-B
			//
			Song ab = new Song("ab", List.of("A", "B"), "ab");
			analyzer.addSong(ab);
			assertEquals(1, analyzer.getPathBetweenArtists("A", "B").size());
			assertEquals(List.of(ab), analyzer.getPathBetweenArtists("A", "B"));

			// Try bad inputs
			assertEquals(null, analyzer.getPathBetweenArtists(null, "A")); // null 1
			assertEquals(null, analyzer.getPathBetweenArtists("A", null)); // null 2
			assertEquals(null, analyzer.getPathBetweenArtists("F", "A")); // nonexistent 1
			assertEquals(null, analyzer.getPathBetweenArtists("A", "F")); // nonexistent 2
			assertEquals(null, analyzer.getPathBetweenArtists("A", "A")); // same artist

			// Test a longer chain from A-E
			//
			// A-B-C-D-E
			//
			Song bc = new Song("bc", List.of("B", "C"), "bc");
			Song cd = new Song("cd", List.of("D", "C"), "cd");
			Song de = new Song("de", List.of("D", "E"), "de");
			analyzer.addSong(bc);
			analyzer.addSong(cd);
			analyzer.addSong(de);
			assertEquals(4, analyzer.getPathBetweenArtists("A", "E").size());
			assertEquals(List.of(ab, bc, cd, de), analyzer.getPathBetweenArtists("A", "E"));

			// Test that a connection from B-D makes A-E path shorter
			//
			// A-B-C-D-E
			// | |
			// ---
			//
			Song bd = new Song("bd", List.of("B", "D"), "bd");
			analyzer.addSong(bd);
			assertEquals(3, analyzer.getPathBetweenArtists("A", "E").size());
			assertEquals(List.of(ab, bd, de), analyzer.getPathBetweenArtists("A", "E"));

			// Test that disconnected artists returns an empty list
			//
			// A-B-C-D-E
			// | | F
			// ---
			//
			Song f = new Song("f", List.of("F"), "f");
			analyzer.addSong(f);
			assert (analyzer.getPathBetweenArtists("A", "F").isEmpty());

		} catch (Exception e) {
			fail("Unexpected exception" + e.getClass());
		}
	}

	/**
	 * Test method for
	 * {@link CollaborationAnalyzer#getAllConnectedArtists(java.lang.String)}.
	 * Verifies we get the proper sorted list of connected artists
	 */
	@Test
	void test07_GetAllConnectedArtists() {
		try {
			// Add A-B and verify single collaborator
			//
			// A-B
			//
			Song ab = new Song("ab", List.of("A", "B"), "ab");
			analyzer.addSong(ab);
			assertEquals(1, analyzer.getAllConnectedArtists("A").size());
			assertEquals(List.of("B"), analyzer.getAllConnectedArtists("A"));

			// Try bad inputs
			assertEquals(null, analyzer.getAllConnectedArtists(null)); // null
			assertEquals(null, analyzer.getAllConnectedArtists("C")); // non-existent

			// Add some more songs, including a cycle and disconnected
			//
			// A-B-D-E
			// | |/ G
			// F---C
			//
			Song af = new Song("ac", List.of("A", "F"), "af");
			Song bd = new Song("bd", List.of("B", "D"), "bd");
			Song cf = new Song("cf", List.of("C", "F"), "cf");
			Song dce = new Song("dce", List.of("D", "C", "E"), "dce");
			Song g = new Song("g", List.of("G"), "g");
			analyzer.addSong(af);
			analyzer.addSong(bd);
			analyzer.addSong(cf);
			analyzer.addSong(dce);
			analyzer.addSong(g);
			assertEquals(5, analyzer.getAllConnectedArtists("A").size());
			assertEquals(List.of("B", "C", "D", "E", "F"), analyzer.getAllConnectedArtists("A"));
			assert (analyzer.getAllConnectedArtists("G").isEmpty()); // disconnected

		} catch (Exception e) {
			fail("Unexpected exception" + e.getClass());
		}

	}

	/**
	 * Test method for GetSimilarSongs backend function Verifies we get the proper
	 * list of similar songs and not any extraneous songs
	 */
	@Test
	void test06_GetSimilarSongs() {
		CollaborationAnalyzer localAnalyzer = new CollaborationAnalyzer();
		List<Song> startingSongList = null;
		try {
			startingSongList = List.of(new Song("Song A", List.of("Joe"), "a genre"),
					new Song("Song B", List.of("Joe", "Ellen"), "b genre"),
					new Song("Song C", List.of("Ellen"), "c genre"), new Song("Song D", List.of("Lilly"), "c genre"),
					new Song("Song E", List.of("Joe", "Lilly"), "c genre"));
		} catch (InvalidSongException e) {
			fail("unexpected invalid song exception");
		}
		for (int i = 0; i < 5; i++) {
			try {
				localAnalyzer.addSong(startingSongList.get(i));
			} catch (DuplicateSongException e) {
			}
		}
		List<Song> similarSongList = localAnalyzer.getSimilarSongs("Joe");
		if (similarSongList.size() != 2)
			fail("should have 2 similar song but returned: " + similarSongList.size());
		String songName = similarSongList.get(1).getTitle();
		if (songName.compareTo("Song C") != 0 && songName.compareTo("Song D") != 0)
			fail("Song C & Song E should be similar songs but returned: " + songName);
	}

}
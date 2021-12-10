package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
/**
 * 
 */

/**
 * @author jmcmahon
 *
 */
class CollaborationAnalyzerTest {
	private CollaborationAnalyzer analyzer;
	private List<Song> songList;
	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
		this.analyzer = new CollaborationAnalyzer();
		this.songList = List.of(new Song("Song A", List.of("Joe", "Ellen"), "a genre"),
								new Song("Song B", List.of("Joe", "Ellen"), "b genre"),
								new Song("Song C", List.of("Joe", "Rachel"), "c genre")
							    );
	}


	/**
	 * Test method for {@link CollaborationAnalyzer#CollaborationAnalyzer()}.
	 */
	@Test
	void jmm1_testCollaborationAnalyzerConstructor() {
		new CollaborationAnalyzer();
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#addSong(Song)}.
	 */
	@Test
	void testAddSong() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#removeSong(Song)}.
	 */
	@Test
	void testRemoveSong() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#getArtistNames()}.
	 */
	@Test
	void testGetArtistNames() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#getSongList(java.lang.String)}.
	 */
	@Test
	void testGetSongList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#getSongListMatching(java.lang.String)}.
	 */
	@Test
	void testGetSongListMatching() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#getCollaboratorList(java.lang.String)}.
	 */
	@Test
	void testGetCollaboratorList() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#importFile(java.lang.String)}.
	 */
	@Test
	void testImportFile() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#outputFile(java.lang.String)}.
	 */
	@Test
	void testOutputFile() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#getPathBetweenArtists(java.lang.String, java.lang.String)}.
	 *
	 * A-B
	 * 
	 * A-B-C-D-E
	 * 
	 * A-B-C-D-E
	 *   |   |     F
	 *    ---  
	 */
	@Test
	void testGetPathBetweenArtists() {
		try {
			// Add A-B and verify path of size 1
			Song ab = new Song("ab", List.of("A", "B"), "ab");
			analyzer.addSong(ab);
			assertEquals(1, analyzer.getPathBetweenArtists("A", "B").size());
			assertEquals(List.of(ab), analyzer.getPathBetweenArtists("A", "B"));
			
			// Try bad inputs
			assertEquals(null, analyzer.getPathBetweenArtists(null, "A")); //null 1
			assertEquals(null, analyzer.getPathBetweenArtists("A", null)); //null 2
			assertEquals(null, analyzer.getPathBetweenArtists("F", "A")); //nonexistent 1
			assertEquals(null, analyzer.getPathBetweenArtists("A", "F")); //nonexistent 2
			assertEquals(null, analyzer.getPathBetweenArtists("A", "A")); //same artist
			
			// Test a longer chain from A-E
			Song bc = new Song("bc", List.of("B", "C"), "bc");
			Song cd = new Song("cd", List.of("D", "C"), "cd");
			Song de = new Song("de", List.of("D", "E"), "de");
			analyzer.addSong(bc);
			analyzer.addSong(cd);
			analyzer.addSong(de);
			assertEquals(4, analyzer.getPathBetweenArtists("A", "E").size());
			assertEquals(List.of(ab, bc, cd, de), analyzer.getPathBetweenArtists("A", "E"));
			
			// Test that a connection from B-D makes A-E path shorter
			Song bd = new Song("bd", List.of("B", "D"), "bd");
			analyzer.addSong(bd);
			assertEquals(3, analyzer.getPathBetweenArtists("A", "E").size());
			assertEquals(List.of(ab, bd, de), analyzer.getPathBetweenArtists("A", "E"));
			
			// Test that disconnected artists returns an empty list
			Song f = new Song("f", List.of("F"), "f");
			analyzer.addSong(f);
			assert(analyzer.getPathBetweenArtists("A", "F").isEmpty());
			
		} catch (Exception e) {
			fail("Unexpected exception" + e.getClass());
		}
	}

	/**
	 * Test method for {@link CollaborationAnalyzer#getPath(java.lang.String, java.lang.String)}.
	 */
	@Test
	void testGetPath() {
		fail("Not yet implemented");
	}

}

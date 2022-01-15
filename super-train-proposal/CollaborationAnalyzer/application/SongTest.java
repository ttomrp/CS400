package application;

import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

/**
 * Filename: SongTest.java
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
 * JUnit test class for Song.java
 */

class SongTest {

	/**
	 * Verifies that HashCodes for two equal Songs are equal
	 */
	@Test
	void test01_testHashCode() throws Exception {
		Song song1 = new Song("Song A", List.of("Artist A", "Artist B", "Artist C"), "blues");
		Song song2 = new Song("Song " + "A", List.of("Artist A", "Artist Q"), "hip hop");
		// according to our definition of equality song1 equals song2 (primary artist
		// and title are the same
		Assert.assertEquals("same songs didn't have same hashCode()", song1.hashCode(), song2.hashCode());
	}

	/**
	 * Tests constructing a valid song
	 */
	@Test
	void test02_testValidSongConstructor() {
		try {
			new Song("Song!", List.of("a"), "stuff");
			// pass
		} catch (Exception ex) {
			fail("exception thrown when constructing valid Song");
		}
	}

	/**
	 * Makes sure that null/blank title and genre cause an exception
	 */
	@Test
	void test03_testNullTitleAndGenre() {
		// null title
		Assert.assertThrows("null title didn't throw", InvalidSongException.class, () -> {
			new Song(null, List.of("a"), "stuff");
		});
		// blank title
		Assert.assertThrows("blank title didn't throw", InvalidSongException.class, () -> {
			new Song("", List.of("a"), "stuff");
		});
		// null genre
		Assert.assertThrows("null genre didn't throw", InvalidSongException.class, () -> {
			new Song("x", List.of("a"), null);
		});
		// blank genre
		Assert.assertThrows("blank genre didn't throw", InvalidSongException.class, () -> {
			new Song("x", List.of("a"), "");
		});
	}

	/**
	 * Makes sure that constructing a Song fails if there are duplicate artists
	 */
	@Test
	void test04_testDuplicateArtists() {
		Assert.assertThrows(InvalidSongException.class, () -> {
			new Song("x", List.of("a", "b", "a"), "y");
		});
	}

	/**
	 * Tests that constructing a Song with null artist fails
	 */
	@Test
	void test05_testBlankArtist() {
		Assert.assertThrows(InvalidSongException.class, () -> {
			new Song("x", List.of("a", "b", ""), "y");
		});
	}

	/**
	 * Tests that constructing a Song with no artists fails
	 */
	@Test
	void test06_testNoArtists() {
		Assert.assertThrows(InvalidSongException.class, () -> {
			new Song("x", List.of(), "y");
		});
	}

	/**
	 * Tests getTitle method returns the title element of Song
	 * 
	 * @throws Exception
	 */
	@Test
	void test08_testGetTitle() throws Exception {
		Assert.assertEquals(new Song("a", List.of("b", "c"), "d").getTitle(), "a");
	}

	/**
	 * Tests getGenre method returns the genre element of Song
	 * 
	 * @throws Exception
	 */
	@Test
	void test09_testGetGenre() throws Exception {
		Assert.assertEquals(new Song("a", List.of("b", "c"), "d").getGenre(), "d");
	}

	/**
	 * Test getCommaDelimitedArtistNames returns a comma-delimited string of artists
	 * for Song
	 * 
	 * @throws Exception
	 */
	@Test
	void test10_testGetCommaDelimitedArtistNames() throws Exception {
		Assert.assertEquals("larry, curly, moe",
				new Song("a", List.of("larry", "curly", "moe"), "c").getCommaDelimitedArtistNames());
	}

	/**
	 * Tests getArtists returns a list of artists for Song
	 * 
	 * @throws Exception
	 */
	@Test
	void test11_testGetArtists() throws Exception {
		Assert.assertEquals(List.of("larry", "curly", "moe"),
				new Song("a", List.of("larry", "curly", "moe"), "c").getArtists());
	}

	/**
	 * Tests getPrimaryArtist returns the first artist in in a list of artists for
	 * Song
	 * 
	 * @throws Exception
	 */
	@Test
	void test12_testGetPrimaryArtist() throws Exception {
		Assert.assertEquals("larry", new Song("a", List.of("larry", "curly", "moe"), "c").getPrimaryArtist());
	}

	/**
	 * Tests that equals method can identify songs that share exact qualities
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unlikely-arg-type")
	@Test
	void test13_testEqualsObject() throws Exception {
		Song songA1, songA2, songB1, songB2;
		songA1 = new Song("a", List.of("b", "c"), "d");
		songA2 = new Song("a", List.of("b", "c"), "d");
		songB1 = new Song("b", List.of("q"), "c");
		songB2 = new Song("b", List.of("q", "p"), "others");
		assert (songA1.equals(songA1));
		assert (songB1.equals(songB1));
		assert (songA1.equals(songA2));
		assert (songA2.equals(songA1));
		assert (songB1.equals(songB2));
		assert (songB2.equals(songB1));

		assert (!songA1.equals(songB1));
		assert (!songA1.equals("stuff"));
	}

	/**
	 * Tests compareTo method can correctly compare Song objects
	 * 
	 * @throws Exception
	 */
	@Test
	void test14_testCompareTo() throws Exception {
		Song low, middle, high, equalsmiddle;
		low = new Song("A", List.of("b"), "c");
		middle = new Song("A", List.of("d"), "c");
		high = new Song("D", List.of("a"), "a");
		equalsmiddle = new Song("A", List.of("d", "e"), "q");
		assert (low.compareTo(middle) < 0);
		assert (middle.compareTo(high) < 0);
		assert (high.compareTo(middle) > 0);
		assert (middle.compareTo(low) > 0);
		assert (middle.compareTo(equalsmiddle) == 0);
		assert (low.compareTo(low) == 0);
	}


}

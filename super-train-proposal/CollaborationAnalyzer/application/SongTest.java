package application;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class SongTest {
	
	private static boolean songsAreIdentical(Song song1, Song song2) {
		//NOT quite the same as song1.equals(song2) because it also verifies genre and artist lists are exactly the same
		return song1.getTitle().equals(song2.getTitle()) && song1.getGenre().equals(song2.getGenre()) && song1.getArtists().equals(song2.getArtists());
	}
	
	/**
	 * Verifies that HashCodes for two equal Songs are equal
	 */
	@Test
	void jmm1_testHashCode() throws Exception {
		Song song1 = new Song("Song A", List.of("Artist A", "Artist B", "Artist C"), "blues");
		Song song2 = new Song("Song " + "A", List.of("Artist A", "Artist Q"), "hip hop");
		//according to our definition of equality song1 equals song2 (primary artist and title are the same
		Assert.assertEquals("same songs didn't have same hashCode()", song1.hashCode(), song2.hashCode());
	}

	/**
	 * Tests constructing a valid song
	 */
	@Test
	void jmm2_testValidSongConstructor() {
		try {
			Song test = new Song("Song!", List.of("a"), "stuff");
			//pass
		} catch(Exception ex) {
			fail("exception thrown when constructing valid Song");
		}
	}
	
	/**
	 * Makes sure that null/blank title and genre cause an exception
	 */
	@Test
	void jmm3_testNullTitleAndGenre() {
		//null title
		Assert.assertThrows("null title didn't throw", InvalidSongException.class, () -> {
			new Song(null, List.of("a"), "stuff");
		});
		//blank title
		Assert.assertThrows("blank title didn't throw", InvalidSongException.class, () -> {
			new Song("", List.of("a"), "stuff");
		});
		//null genre
		Assert.assertThrows("null genre didn't throw", InvalidSongException.class, () -> {
			new Song("x", List.of("a"), null);
		});
		//blank genre
		Assert.assertThrows("blank genre didn't throw", InvalidSongException.class, () -> {
			new Song("x", List.of("a"), "");
		});
	}
	
	/**
	 * Makes sure that constructing a Song fails if there are duplicate artists
	 */
	@Test
	void jmm4_testDuplicateArtists() {
		Assert.assertThrows(InvalidSongException.class, () -> {
			new Song("x", List.of("a", "b", "a"), "y");
		});
	}
	
	@Test
	void jmm5_testBlankArtist() {
		Assert.assertThrows(InvalidSongException.class, () -> {
			new Song("x", List.of("a", "b", ""), "y");
		});
	}
	
	@Test
	void jmm6_testNoArtists() {
		Assert.assertThrows(InvalidSongException.class, () -> {
			new Song("x", List.of(), "y");
		});
	}

	@Test
	void jmm7_testWithCommaDelimitedArtists() throws Exception {
		assert(songsAreIdentical(new Song("MyTitle", List.of("java", "c++"), "blues"), Song.withCommaDelimitedArtists("MyTitle", " java, c++ ", "blues")));
	}

	@Test
	void jmm8_testGetTitle() throws Exception {
		Assert.assertEquals(new Song("a", List.of("b", "c"), "d").getTitle(), "a");
	}

	@Test
	void jmm9_testGetGenre() throws Exception {
		Assert.assertEquals(new Song("a", List.of("b", "c"), "d").getGenre(), "d");
	}

	@Test
	void jmm10_testGetCommaDelimitedArtistNames() throws Exception {
		Assert.assertEquals("larry, curly, moe", new Song("a", List.of("larry", "curly", "moe"), "c").getCommaDelimitedArtistNames());
	}

	@Test
	void jmm11_testGetArtists() throws Exception {
		Assert.assertEquals(List.of("larry", "curly", "moe"), new Song("a", List.of("larry", "curly", "moe"), "c").getArtists());
	}

	@Test
	void jmm12_testGetPrimaryArtist() throws Exception {
		Assert.assertEquals("larry", new Song("a", List.of("larry", "curly", "moe"), "c").getPrimaryArtist());
	}

	@Test
	void jmm13_testEqualsObject() throws Exception {
		Song songA1, songA2, songB1, songB2;
		songA1 = new Song("a", List.of("b", "c"), "d");
		songA2 = new Song("a", List.of("b", "c"), "d");
		songB1 = new Song("b", List.of("q"), "c");
		songB2 = new Song("b", List.of("q", "p"), "others");
		assert(songA1.equals(songA1));
		assert(songB1.equals(songB1));
		assert(songA1.equals(songA2));
		assert(songA2.equals(songA1));
		assert(songB1.equals(songB2));
		assert(songB2.equals(songB1));
		
		assert(!songA1.equals(songB1));
		assert(!songA1.equals("stuff"));
	}

	@Test
	void jmm14_testCompareTo() throws Exception {
		Song low, middle, high, equalsmiddle;
		low = new Song("A", List.of("b"), "c");
		middle = new Song("A", List.of("d"), "c");
		high = new Song("D", List.of("a"), "a");
		equalsmiddle = new Song("A", List.of("d", "e"), "q");
		assert(low.compareTo(middle) < 0);
		assert(middle.compareTo(high) < 0);
		assert(high.compareTo(middle) > 0);
		assert(middle.compareTo(low) > 0);
		assert(middle.compareTo(equalsmiddle) == 0);
		assert(low.compareTo(low) == 0);
	}

	@Test
	void jmm15_testSearchMatches() throws Exception {
		Song s = new Song("the quick brown fox", List.of("b", "c", "d"), "e");
		assert(s.searchMatches("e quic"));
		assert(!s.searchMatches("ether"));
		
	}

}

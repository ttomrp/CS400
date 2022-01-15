package application;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

/**
 * Filename: MusicLibraryFileProcessorTest.java
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
 * JUnit test class for MusicLibraryFileProcessor.java Test HashTable class
 * implementation to ensure that required functionality works for all cases.
 */

class MusicLibraryFileProcessorTest {

	static final String SONGSIN = "MusicLibraryTestIn.txt";
	static final String SONGSOUT = "MusicLibraryTestOut.txt";
	static final MusicLibraryFileProcessor PROCESSOR = new MusicLibraryFileProcessor();
	static final CollaborationAnalyzer APP = new CollaborationAnalyzer();

	/**
	 * Tests that file loads properly using the music file processor class
	 */
	@Test
	void test00_loadFile() {
		HashSet<Song> songSet = null;
		try {
			File musicLibraryFile = new File(SONGSIN);
			songSet = PROCESSOR.importFile(musicLibraryFile);
		} catch (FileNotFoundException e) {
			fail("test file not found");
		}
		if (songSet.size() != 19)
			fail("should have loaded 19 songs");
	}

	/**
	 * Tests that file loads properly into app backend
	 */
	@Test
	void test01_addSongsFromFile() {
		try {
			File musicLibraryFile = new File(SONGSIN);
			APP.importFile(musicLibraryFile);
		} catch (FileNotFoundException e) {
			fail("test file not found");
		}
	}

	/**
	 * Tests that blank file saves properly using the music file processor class
	 */
	@Test
	void test02_saveFile() {
		try {
			File outputFile = new File(SONGSOUT);
			PROCESSOR.exportFile(outputFile, null);
		} catch (IOException e) {
			fail("IO Exception");
		} catch (SecurityException e) {
			fail("security exception");
		} catch (Exception e) {
			fail(e.toString());
		}
		return;
	}

	/**
	 * Tests that songs loaded save out to file properly using the app backend
	 */
	@Test
	void test03_saveSongsToFile() {
		try {
			File musicLibraryFile = new File(SONGSIN);
			APP.importFile(musicLibraryFile);
		} catch (FileNotFoundException e) {
			fail("test file not found");
		}
		try {
			APP.outputFile(SONGSOUT);
		} catch (Exception e) {
			fail(e.toString());
		}
		return;
	}
}
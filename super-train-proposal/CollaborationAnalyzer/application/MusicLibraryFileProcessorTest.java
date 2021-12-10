package application;

import org.junit.After;
import java.io.FileNotFoundException;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.File;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/** 
 * Test HashTable class implementation to ensure that required 
 * functionality works for all cases.
 */
class MusicLibraryFileProcessorTest {

    static final String SONGSIN = "MusicLibraryTestIn.txt";
    static final String SONGSOUT = "MusicLibraryTestOut.txt";
    static final MusicLibraryFileProcessor PROCESSOR = new MusicLibraryFileProcessor();
    static final CollaborationAnalyzer APP = new CollaborationAnalyzer();

    /**
     * Tests that file loads properly
     */
    @Test
    void test000_loadFile() {
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
    void test001_addSongsFromFile() {
    	try {
    		File musicLibraryFile = new File(SONGSIN);
    		APP.importFile(musicLibraryFile);
    	} catch (FileNotFoundException e) {
    		fail("test file not found");
    	}
    }
    
    /**
     * Tests that blank file saves properly
     */
    @Test
    void test002_saveFile() {
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
     * Tests that songs loaded save out to file properly
     */
    @Test
    void test003_saveSongsToFile() {
    	HashSet<Song> songSet = null;
    	try {
    		File musicLibraryFile = new File(SONGSIN);
    		songSet = PROCESSOR.importFile(musicLibraryFile);
    	} catch (FileNotFoundException e) {
    		fail("test file not found");
    	}
       	try {
        	File outputFile = new File(SONGSOUT);
    		PROCESSOR.exportFile(outputFile, songSet);
    	} catch (Exception e) {
    		fail(e.toString());
    	}
       	return;
    }
}
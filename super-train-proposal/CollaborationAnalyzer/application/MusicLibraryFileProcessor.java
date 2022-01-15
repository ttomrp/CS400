package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Filename: MusicLibraryFileProcessor.java
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
 * Class to perform music library file operations for collaboration analyzer app
 */

class MusicLibraryFileProcessor {

	/*
	 * Reads in music library file and adds data to collaboration analyzer Expects
	 * first row to be a header and only parses header rows labeled: "Track Title",
	 * "Artist", "Genre" & "Remixer" Row must have track title and at least one
	 * artist or remixer, otherwise row is ignored
	 * 
	 * @param file - File object for a tsv file with song information
	 */
	HashSet<Song> importFile(File file) throws FileNotFoundException {
		HashSet<Song> songSet = new HashSet<Song>();
		Scanner fileScanner = new Scanner(file, "UTF-8");
		Scanner songScanner = null;
		int col = 1;
		int titleCol = 0;
		int artistCol = 0;
		int genreCol = 0;
		int remixerCol = 0;

		// Find which columns the information we need are in based on header
		Scanner headerScanner = new Scanner(fileScanner.nextLine());
		headerScanner.useDelimiter("\t");
		while (headerScanner.hasNext()) {
			String data = headerScanner.next();
			if (data.compareTo("Track Title") == 0)
				titleCol = col;
			if (data.compareTo("Artist") == 0)
				artistCol = col;
			if (data.compareTo("Genre") == 0)
				genreCol = col;
			if (data.compareTo("Remixer") == 0)
				remixerCol = col;
			col++;
		}

		// process if valid data columns found
		if (titleCol != 0 && (artistCol != 0 || remixerCol != 0)) {
			// read in remaining rows, storing valid rows into Song objects to be returned
			while (fileScanner.hasNext()) {
				songScanner = new Scanner(fileScanner.nextLine());
				songScanner.useDelimiter("\t");
				col = 1;
				String title = null;
				ArrayList<String> artistAry = new ArrayList<String>();
				String genre = null;
				while (songScanner.hasNext()) {
					String data = songScanner.next();
					if (col == titleCol)
						title = data;
					if (col == genreCol)
						genre = data;
					if (col == artistCol || col == remixerCol)
						artistAry.addAll(artistParser(data));
					col++;
				}

				if (title != null && artistAry.size() > 0) {
					Song newSong = null;
					try {
						newSong = new Song(title, artistAry, genre);
						songSet.add(newSong);
					} catch (InvalidSongException e) {
					}
				}
			}
		}
		fileScanner.close();
		return songSet;
	}

	/*
	 * Given input artist data string, returns them in array format with leading and
	 * trailing whitespaces removed
	 * 
	 * @param data - comma delimited list of artists
	 */
	private ArrayList<String> artistParser(String data) {
		ArrayList<String> artistList = new ArrayList<String>();
		Scanner artistScanner = new Scanner(data);
		artistScanner.useDelimiter(",");
		while (artistScanner.hasNext()) {
			String artist = artistScanner.next();
			artistList.add(artist.strip());
		}
		artistScanner.close();
		return artistList;
	}

	/*
	 * Creates new output file and outputs list of songs to .tsv file with header
	 * row Output file has columns: "Track Title", "Artist", and "Genre"
	 * 
	 * @param outputFilename - filename of file to create
	 * 
	 * @param songList - array of song objects to output
	 * 
	 * @returns - absolute file path of created file
	 */
	String exportFile(File outputFile, Set<Song> songSet) throws IOException {
		// Grab absolute file path to return
		String absoluteFilePath = outputFile.getAbsolutePath();

		// Create instance of PrintWriter writing out to an "output.txt" file in the
		// current directory
		PrintWriter fileOut = new PrintWriter(outputFile);

		// Add file header
		fileOut.append("Track Title\tArtist\tGenre\r\n");

		if (songSet != null) {
			Iterator<Song> songIterator = songSet.iterator();
			while (songIterator.hasNext()) {
				Song currentSong = songIterator.next();
				List<String> artistList = currentSong.getArtists();
				String artistString = new String();
				if (artistList != null) {
					for (int j = 0; j < artistList.size(); j++) {
						if (j > 0)
							artistString = artistString + ", ";
						artistString = artistString + artistList.get(j);
					}
				}
				String outputString = new String();
				if (currentSong.getTitle() != null)
					outputString = outputString + currentSong.getTitle();
				outputString = outputString + "\t";
				if (artistString != null)
					outputString = outputString + artistString;
				outputString = outputString + "\t";
				if (currentSong.getGenre() != null)
					outputString = outputString + currentSong.getGenre();
				outputString = outputString + "\t";
				fileOut.append(outputString + "\r\n");
			}
		}
		fileOut.close();
		// methods on PrintWriter don't throw exceptions but instead set an error flag
		// this could be because the disk was full, there were permission errors, a USB
		// drive was ejected, etc.
		if (fileOut.checkError()) {
			throw new IOException(String.format("error occurred writing to file %s", absoluteFilePath));
		}
		return absoluteFilePath;
	}
}
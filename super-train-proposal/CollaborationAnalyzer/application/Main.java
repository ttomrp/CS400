package application;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * Filename: Song.java
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
 * Main class that defines the GUI for Collaboration Analyzer.
 */
public class Main extends Application {

	// Get dimensions of the screen so we can open the app in an appropriate size
	Rectangle2D screenBounds = Screen.getPrimary().getBounds();

	// Make the initial width of the app just smaller than the screen
	private final double WINDOW_WIDTH = screenBounds.getMaxX() - 100;

	// Make the initial height of the app just smaller than the screen
	private final double WINDOW_HEIGHT = screenBounds.getMaxY() - 100;

	// Name of the app for the window's title bar
	private final String APP_TITLE = "Music Library";

	// Content of a help tool-tip for the Similar Tracks box
	private String SIMILAR_SONGS_HELP_TOOLTIP = "Select an artist in the dropdown to display songs similar to those of that artist.\n"
			+ "\n"
			+ "Finds other artists the selected artist has collorated with, and displays those other artists' songs.";

	// Content of a help tool-tip for the Degrees of Separation box
	private String DEGREES_SEPARATION_HELP_TOOLTIP = "Select two artists in the dropdown to display their degrees of separation,\n"
			+ "based on their collorations with other artists.\n" + "\n"
			+ "Displays the songs that connect the two artists on the shortest possible path.";

	// Content of a help tool-tip for the Songs For Artist box
	private String SONGS_FOR_ARTIST_HELP_TOOLTIP = "Select an artist in the dropdown to display that artist's songs.";

	// The database for the instance of the app
	private CollaborationAnalyzer db;

	// A list of all artists in the database to use in artist-select drop-downs
	private ObservableList<String> sortedList;

	// A map of each ComboBox to its associated Clear button
	private HashMap<ComboBox<String>, Button> comboBoxClearButtonMap = new HashMap<ComboBox<String>, Button>();

	// List of all ComboBoxes on the main dashboard
	private List<ComboBox<String>> allDashboardComboBoxes;

	// Instance-level references to important UI elements
	private Stage primaryStage;
	private ComboBox<String> similarSongsComboBox;
	private TableView<Song> similarSongsTable;
	private ComboBox<String> songsForArtistComboBox;
	private TableView<Song> songsForArtistTable;
	private ComboBox<String> degreesOfSeparationComboBox1;
	private ComboBox<String> degreesOfSeparationComboBox2;
	private TableView<Song> degreesOfSeparationTable;

	/**
	 * Creates the main application window at the beginning of the program
	 * 
	 * @param pStage the primary stage
	 */
	@Override
	public void start(Stage pStage) throws Exception {

		// Set a class-level reference to this so it can be used in any method
		primaryStage = pStage;

		// Initialize the back-end database
		db = new CollaborationAnalyzer();
		sortedList = db.getArtistNames().sorted();

		// Create toolbar and component boxes for the app's content
		MenuBar toolbar = createToolbar();
		VBox similarSongsBox = createSimilarSongsBox();
		VBox songsVbox = createSongsForArtistBox();
		VBox degreesOfSeparationBox = createDegreesOfSeparationBox();

		// Create list of all combo boxes on the dashboard
		allDashboardComboBoxes = List.of(similarSongsComboBox, songsForArtistComboBox, degreesOfSeparationComboBox1,
				degreesOfSeparationComboBox2);

		VBox toolbarBox = new VBox();
		HBox librarySizeBanner = createLibrarySizeBanner();
		toolbarBox.getChildren().addAll(toolbar, librarySizeBanner);

		// Create root pane and attach the main layout:
		// *top - ToolBar with import, add, remove, and export options
		// *left - find similar songs
		// *right - look at this graph (of collaborators)
		// *bottom - degrees of separation
		BorderPane root = new BorderPane();
		root.setPrefHeight(WINDOW_HEIGHT);
		root.setPrefWidth(WINDOW_WIDTH);
		root.setTop(toolbarBox);
		root.setLeft(similarSongsBox);
		root.setRight(songsVbox);
		root.setBottom(degreesOfSeparationBox);

		// Set the size of the components
		similarSongsBox.setPrefWidth(WINDOW_WIDTH / 2 - 5);
		similarSongsBox.setPrefHeight(WINDOW_HEIGHT / 2 - 50);
		similarSongsBox.setMinHeight(WINDOW_HEIGHT / 4);
		similarSongsBox.setMaxHeight(WINDOW_HEIGHT / 2 - 50);
		songsVbox.setPrefWidth(WINDOW_WIDTH / 2 - 5);
		songsVbox.setPrefHeight(WINDOW_HEIGHT / 2 - 50);
		songsVbox.setMinHeight(WINDOW_HEIGHT / 4);
		songsVbox.setMaxHeight(WINDOW_HEIGHT / 2 - 50);
		degreesOfSeparationBox.setPrefHeight(WINDOW_HEIGHT / 2 - 50);
		degreesOfSeparationBox.setMinHeight(WINDOW_HEIGHT / 4);

		// Confirm user wants to exit when selecting exit button
		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest(e -> {
			ButtonType saveAndExitType = new ButtonType("Save and Exit");
			ButtonType exitType = new ButtonType("Exit Without Saving");
			ButtonType cancelType = new ButtonType("Cancel");
			Alert alt = new Alert(AlertType.CONFIRMATION, "Are you sure you want to exit the app?", exitType,
					saveAndExitType, cancelType);
			alt.initModality(Modality.APPLICATION_MODAL);
			alt.initOwner(primaryStage);
			alt.setContentText("Are you sure you want to exit the app?");

			Optional<ButtonType> altSelection = alt.showAndWait();
			if (altSelection.get().equals(exitType)) {
				Platform.exit();
			} else if (altSelection.get().equals(saveAndExitType)) {
				exportFileWithPicker(true);
				e.consume();
			} else {
				alt.close();
				e.consume();
			}
		});

		// The moment you've been waiting for.
		primaryStage.setTitle(APP_TITLE);
		// Set the stage, add CSS to make the app pretty
		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
		mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		primaryStage.setScene(mainScene);
		// SHOWTIME!
		primaryStage.show();

		// Import a file right away if it was passed from the command line
		List<String> params = getParameters().getRaw();
		if (params.size() > 0) {
			File initialFile = new File(params.get(0));
			importFile(initialFile);
		}
	}

	/**
	 * Creates the label used to display the size of the library
	 * 
	 * @return label of librarySize
	 */
	private HBox createLibrarySizeBanner() {
		HBox librarySizeBanner = new HBox();
		librarySizeBanner.setPadding(new Insets(3, 0, 0, 5));
		Label label = new Label("Library Size: ");
		Label librarySizeLabel = new Label();
		librarySizeLabel.textProperty().bind(Bindings.convert(db.getLibrarySize()));
		librarySizeBanner.getChildren().addAll(label, librarySizeLabel);
		return librarySizeBanner;
	}

	/**
	 * Generate the similar songs component of the dashboard
	 * 
	 * @return the similar songs component as a VBox
	 */
	private VBox createSimilarSongsBox() {
		// Initialize VBox
		VBox similarSongsVbox = new VBox();
		similarSongsVbox.setPadding(new Insets(0, 0, 0, 5));

		// Create and add an HBox for the section's header
		HBox titleBox = new HBox();
		Label title = new Label("Find Similar Songs");
		title.setStyle("-fx-font-size: 20;");
		titleBox.getChildren().add(title);
		titleBox.getChildren().add(createHelpIcon(SIMILAR_SONGS_HELP_TOOLTIP));
		similarSongsVbox.getChildren().add(titleBox);

		// Create a TableView & columns
		similarSongsTable = new TableView<Song>();
		similarSongsTable.setPlaceholder(new Label("Select artist to view similar tracks"));
		similarSongsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<Song, String> titleCol = new TableColumn<Song, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Song, String> artistCol = new TableColumn<Song, String>("Artists");
		artistCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
				cellData.getValue().getArtists().toString().replace("[", "").replace("]", "")));
		TableColumn<Song, String> genreCol = new TableColumn<Song, String>("Genre");
		genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

		// Add the columns to the table
		similarSongsTable.getColumns().add(titleCol);
		similarSongsTable.getColumns().add(artistCol);
		similarSongsTable.getColumns().add(genreCol);

		// set table properties so it resizes nicely
		similarSongsTable.setFixedCellSize(25);
		similarSongsTable.prefHeightProperty().bind(similarSongsTable.fixedCellSizeProperty()
				.multiply(Bindings.size(similarSongsTable.getItems()).add(1.11)));
		similarSongsTable.minHeightProperty().set(50);
		similarSongsTable.maxHeightProperty().set(300);

		// Add a ComboBox to select an artist to the component
		similarSongsComboBox = new ComboBox<String>();
		similarSongsVbox.getChildren().add(createArtistSelectComboBoxContainer(similarSongsComboBox, true, e -> {
			// Update table and clear button as needed when the combo box's value changes
			populateSimilarSongsTable();
			if (similarSongsComboBox.getValue() == null) {
				setDisableForClearButton(similarSongsComboBox, true);
			} else {
				setDisableForClearButton(similarSongsComboBox, false);
			}
		}));
		// Start the Clear button disabled
		setDisableForClearButton(similarSongsComboBox, true);

		// Add the table to the component
		similarSongsVbox.getChildren().add(similarSongsTable);

		return similarSongsVbox;
	}

	/**
	 * Add/refresh data to the similar songs table for the current artist selection
	 */
	private void populateSimilarSongsTable() {
		// Clear the existing table first
		similarSongsTable.getItems().clear();

		// Get a list of songs by the artist's collaborators
		List<Song> collaboratorsSongs = db.getSimilarSongs(similarSongsComboBox.getValue());

		// Add the similar songs to the table
		if ((collaboratorsSongs != null) && (!collaboratorsSongs.isEmpty())) {
			for (Song song : collaboratorsSongs) {
				similarSongsTable.getItems().add(song);
			}
		} else if (similarSongsComboBox.getValue() == null) {
			similarSongsTable.setPlaceholder(new Label("Select artist to view similar tracks"));
		} else {
			similarSongsTable.setPlaceholder(new Label("No similar tracks for artist"));
		}
	}

	/**
	 * Create a component to display a list of songs for an artist
	 * 
	 * @return a component for displaying an artist's songs as a VBox
	 */
	private VBox createSongsForArtistBox() {
		// Initialize VBox
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(0, 5, 0, 0));

		// HBox for the header
		HBox titleBox = new HBox();
		Label title = new Label("Find Songs for Artist");
		title.setStyle("-fx-font-size: 20;");
		titleBox.getChildren().add(title);
		titleBox.getChildren().add(createHelpIcon(SONGS_FOR_ARTIST_HELP_TOOLTIP));
		vbox.getChildren().add(titleBox);

		// Create a TableView & add columns
		songsForArtistTable = new TableView<Song>();
		songsForArtistTable.setPlaceholder(new Label("Select artist to find songs"));
		songsForArtistTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<Song, String> titleCol = new TableColumn<Song, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Song, String> artistCol = new TableColumn<Song, String>("Artists");
		artistCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
				cellData.getValue().getArtists().toString().replace("[", "").replace("]", "")));
		TableColumn<Song, String> genreCol = new TableColumn<Song, String>("Genre");
		genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

		// Add the columns to the table
		songsForArtistTable.getColumns().add(titleCol);
		songsForArtistTable.getColumns().add(artistCol);
		songsForArtistTable.getColumns().add(genreCol);

		// set tale properties so it resizes nicely
		songsForArtistTable.setFixedCellSize(25);
		songsForArtistTable.prefHeightProperty().bind(songsForArtistTable.fixedCellSizeProperty()
				.multiply(Bindings.size(songsForArtistTable.getItems()).add(1.11)));
		songsForArtistTable.minHeightProperty().set(50);
		songsForArtistTable.maxHeightProperty().set(900);

		// Add a ComboBox to select an artist to the component
		songsForArtistComboBox = new ComboBox<String>();
		vbox.getChildren().add(createArtistSelectComboBoxContainer(songsForArtistComboBox, true, e -> { // Update table
																										// and Clear
																										// button as
																										// needed when
																										// the combo
																										// box's value
																										// changes
			populateSongsForArtistTable();
			if (songsForArtistComboBox.getValue() == null) {
				setDisableForClearButton(songsForArtistComboBox, true);
			} else {
				setDisableForClearButton(songsForArtistComboBox, false);
			}
		}));
		// Start the Clear button disabled
		setDisableForClearButton(songsForArtistComboBox, true);

		// Add the table to the component
		vbox.getChildren().add(songsForArtistTable);

		return vbox;
	}

	/**
	 * Add/refresh data to the table for an artist's list of songs
	 */
	private void populateSongsForArtistTable() {
		String artist = songsForArtistComboBox.getValue();
		songsForArtistTable.getItems().clear();
		if (artist != null) {
			List<Song> songs = db.getSongList(artist);
			for (Song song : songs) {
				songsForArtistTable.getItems().add(song);
			}
		}
	}

	/**
	 * Generate the degrees of separation component
	 * 
	 * @return the degrees of separation component as a VBox
	 */
	private VBox createDegreesOfSeparationBox() {
		// VBox for the overall component
		VBox vbox = new VBox();
		vbox.setPadding(new Insets(5, 0, 0, 5));

		// Add an HBox for the header
		HBox headerBox = new HBox();
		Label title = new Label("Degrees Of Separation");
		title.setStyle("-fx-font-size: 20;");
		headerBox.getChildren().add(title);
		headerBox.getChildren().add(createHelpIcon(DEGREES_SEPARATION_HELP_TOOLTIP));
		vbox.getChildren().add(headerBox);

		// Create a TableView to display degrees of separation results
		degreesOfSeparationTable = createDegreesOfSeparationTable();
		degreesOfSeparationTable.setPlaceholder(new Label("Select two artists to view degrees separation"));

		// Create ComboBoxes to pick artists
		// First Artist
		Label artist1Label = new Label("First Artist");
		artist1Label.setPrefWidth(85);
		degreesOfSeparationComboBox1 = new ComboBox<String>();
		// Second Artist
		Label artist2Label = new Label("Second Artist");
		artist2Label.setPrefWidth(85);
		degreesOfSeparationComboBox2 = new ComboBox<String>();

		// Add selection elements to a grid: labels on left, dropdowns on right
		GridPane grid = new GridPane();
		// Artist 1 label and dropdown
		grid.add(artist1Label, 0, 0);
		grid.add(createArtistSelectComboBoxContainer(degreesOfSeparationComboBox1, true,
				e -> onDegreesOfSeparationArtist1Changed()), 1, 0);
		// Start 1st dropdown's Clear button disabled
		setDisableForClearButton(degreesOfSeparationComboBox1, true);
		// Artist 2 label and dropdown
		grid.add(artist2Label, 0, 1);
		grid.add(createArtistSelectComboBoxContainer(degreesOfSeparationComboBox2, false,
				e -> onDegreesOfSeparationArtist2Changed()), 1, 1);
		// Start 2nd dropdown disabled, until artist 1 is selected
		degreesOfSeparationComboBox2.setDisable(true);
		setDisableForClearButton(degreesOfSeparationComboBox2, true);

		// Add some space between the selection grid and the results table
		Pane divider = new Pane();
		divider.setPrefWidth(20);

		// Add an HBox to the component for everything under the header
		HBox hbox = new HBox(5);
		hbox.getChildren().addAll(grid, divider, degreesOfSeparationTable);
		vbox.getChildren().add(hbox);

		return vbox;
	}

	/**
	 * Generate the degrees of separation table
	 * 
	 * @return a table for degrees of separation results as a TableView
	 */
	private TableView<Song> createDegreesOfSeparationTable() {
		// Initialize table & set sizing properties
		TableView<Song> table = new TableView<Song>();
		table.setPrefWidth(WINDOW_WIDTH / 2);
		table.setMaxWidth(WINDOW_WIDTH / 2);
		table.setMinWidth(WINDOW_WIDTH / 4);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setFixedCellSize(25);
		table.prefHeightProperty()
				.bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.01)));
		table.minHeightProperty().set(70);
		table.maxHeightProperty().bind(table.prefHeightProperty());

		// Set columns for table
		TableColumn<Song, Number> numberCol = new TableColumn<Song, Number>("Degrees");
		numberCol.setCellValueFactory(
				cellData -> new ReadOnlyObjectWrapper<Number>(table.getItems().indexOf(cellData.getValue()) + 1));
		TableColumn<Song, String> titleCol = new TableColumn<Song, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Song, String> artistCol = new TableColumn<Song, String>("Artists");
		artistCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
				cellData.getValue().getArtists().toString().replace("[", "").replace("]", "")));
		TableColumn<Song, String> genreCol = new TableColumn<Song, String>("Genre");
		genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

		// Add columns to table
		table.getColumns().add(numberCol);
		table.getColumns().add(titleCol);
		table.getColumns().add(artistCol);
		table.getColumns().add(genreCol);

		return table;
	}

	/**
	 * Add/refresh data to the degrees of separation table.
	 */
	private void populateDegreesOfSeparationTable() {
		String artist1 = degreesOfSeparationComboBox1.getValue();
		String artist2 = degreesOfSeparationComboBox2.getValue();

		// Clear any existing data
		degreesOfSeparationTable.getItems().clear();

		// Verify we actually have 2 artists selected
		if ((artist1 == null) || (artist2 == null)) {
			return;
		}

		// Get list of songs from the database & add to table
		List<Song> sepSongs = db.getPathBetweenArtists(artist1, artist2);
		if ((sepSongs != null) && (sepSongs.size() > 0)) {
			for (Song song : sepSongs) {
				degreesOfSeparationTable.getItems().add(song);
			}
		}
	}

	/**
	 * Handler for when artist 1 changes in the degrees of separation component
	 * Updates the artist 2 dropdown's availability and the results table
	 */
	private void onDegreesOfSeparationArtist1Changed() {
		String artist1 = degreesOfSeparationComboBox1.getValue();
		String artist2 = degreesOfSeparationComboBox2.getValue();

		// Find all connected artists to artist 1
		ObservableList<String> connectedArtists = db.getAllConnectedArtists(artist1);

		// If no artist 1 selected, disable artist 2 dropdown
		if ((artist1 == null)) {
			degreesOfSeparationTable.setPlaceholder(new Label("Select two artists to view degrees of separation"));
			degreesOfSeparationComboBox2.setValue(null);
			degreesOfSeparationComboBox2.setDisable(true);
			setDisableForClearButton(degreesOfSeparationComboBox2, true);
			// Disable artist1Box's clear button
			setDisableForClearButton(degreesOfSeparationComboBox1, true);
		}
		// Also disable artist 2 if there's no one to select
		else if (connectedArtists == null || connectedArtists.isEmpty()) {
			degreesOfSeparationTable.setPlaceholder(new Label("First artist has no collaborators"));
			degreesOfSeparationComboBox2.setValue(null);
			degreesOfSeparationComboBox2.setDisable(true);
			setDisableForClearButton(degreesOfSeparationComboBox2, true);
			// Enable artist1Box's clear button
			setDisableForClearButton(degreesOfSeparationComboBox1, false);
		}
		// Otherwise fill out the available selections in artist 2 dropdown
		else {
			// If there's already an artist 2, clear it if it's not in the new item list
			if (!connectedArtists.contains(artist2)) {
				degreesOfSeparationComboBox2.setValue(null);
			}
			// No artist 2 selected = we can't show results yet
			if (artist2 == null) {
				degreesOfSeparationTable.setPlaceholder(new Label("Select two artists to view degrees of separation"));
			}
			// Enable and set artist2Box values
			degreesOfSeparationComboBox2.setItems(connectedArtists);
			degreesOfSeparationComboBox2.setDisable(false);
			setDisableForClearButton(degreesOfSeparationComboBox2, true);
			// Enable artist1Box's clear button
			setDisableForClearButton(degreesOfSeparationComboBox1, false);
		}

		// Re-populate the results table with current artist selections
		populateDegreesOfSeparationTable();
	}

	/**
	 * Handler for when artist 2 changes in the degrees of separation component
	 * Updates the results table
	 */
	private void onDegreesOfSeparationArtist2Changed() {
		String artist1 = degreesOfSeparationComboBox1.getValue();
		String artist2 = degreesOfSeparationComboBox2.getValue();

		// If artist 1 is null, artist 2 dropdown should have been disabled, but this
		// could get
		// called if artist 1's dropdown handler changes artist 2's value
		if ((artist1 == null)) {
			degreesOfSeparationTable.setPlaceholder(new Label("Select two artists to view degrees separation"));
		} else {
			// Nothing can be selected for artist 2 if artist 1 has no connections
			if (db.getAllConnectedArtists(artist1).isEmpty()) {
				degreesOfSeparationTable.setPlaceholder(new Label("First artist has no collaborators"));
			}
			// Can't calculate degrees of separation if artist 2 is not selected
			else if (artist2 == null) {
				degreesOfSeparationTable.setPlaceholder(new Label("Select two artists to view degrees separation"));
				setDisableForClearButton(degreesOfSeparationComboBox2, true);
			}
			// If 2 artists selected, and the table doesn't get filled out with anything
			else {
				degreesOfSeparationTable.setPlaceholder(new Label("No connection between artists"));
				setDisableForClearButton(degreesOfSeparationComboBox2, false);
			}
		}

		// Re-populate the results table with current artist selections
		populateDegreesOfSeparationTable();
	}

	/**
	 * Create the toolbar component of the application window
	 * 
	 * @return toolbar as MenuBar object
	 */
	private MenuBar createToolbar() {
		// Initialize a MenuBar
		MenuBar menuBar = new MenuBar();

		// Create Menus
		Menu addRemoveMenu = new Menu("Add/Remove Songs");
		Menu aboutMenu = new Menu("About");
		Menu exportMenu = new Menu("Export Library");

		// Generate MenuItems for addRemoveMenu
		MenuItem importItem = fileSelectItem();
		MenuItem entryItem = addSongItem();
		MenuItem deleteItem = removeSongItem();

		// Export (to selected file) button
		MenuItem exportWithPickItem = new MenuItem("Export to file");
		exportWithPickItem.setOnAction(e -> exportFileWithPicker(false));

		// Items for About Menu
		MenuItem aboutItem = aboutMenuItem();
		MenuItem helpItem = helpMenuItem();
		aboutMenu.getItems().addAll(aboutItem, helpItem);

		// Attach all the menu items
		addRemoveMenu.getItems().addAll(importItem, entryItem, deleteItem);
		exportMenu.getItems().add(exportWithPickItem);
		menuBar.getMenus().addAll(aboutMenu, addRemoveMenu, exportMenu);

		return menuBar;
	}

	/**
	 * Gets a new FileChooser set up to find/save the appropriate kind of files
	 * 
	 * @return the FileChooser (to be used in load/save file picking)
	 */
	private static FileChooser getFileChooser() {
		FileChooser fc = new FileChooser();
		FileChooser.ExtensionFilter textFilesFilter = new FileChooser.ExtensionFilter("Text Files", "*.txt");
		FileChooser.ExtensionFilter tsvFilesFilter = new FileChooser.ExtensionFilter("Tab-Delimited Files", "*.tsv");
		FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
		fc.getExtensionFilters().addAll(textFilesFilter, tsvFilesFilter, allFilesFilter);
		return fc;
	}

	/**
	 * Create a MenuItem that allows the user to select a file to import
	 * 
	 * @return a MenuItem object that can be added to a menu
	 */
	private MenuItem fileSelectItem() {
		// Create a FileChooser we'll use to select the file
		final FileChooser fileChooser = getFileChooser();

		// Create the MenuItem
		MenuItem importItem = new MenuItem("Import File of Songs");

		// Open a file selection window when item is clicked
		importItem.setOnAction(e -> {
			File file = fileChooser.showOpenDialog(primaryStage);
			if (file != null) {
				importFile(file);
			}
		});

		return importItem;
	}

	/**
	 * Inner method to import a file to the library, which can be called from the
	 * toolbar item or from start()
	 * 
	 * @param file the file object to import
	 */
	private void importFile(File file) {
		// Try to import the file
		try {
			db.importFile(file);
		}
		// Alert the user if unsuccessful
		catch (IOException exception) {
			Alert alt = new Alert(AlertType.WARNING);
			alt.setContentText("Unable to open read file");
			alt.initOwner(primaryStage);
			alt.showAndWait();
			return;
		}

		// Create a separate thread to update the sorted list of all artists
		Thread thread = new Thread(new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				sortedList = db.getArtistNames().sorted();
				return null;
			}
		});
		thread.setDaemon(true);
		thread.start();

		// Alert the user that the import was successful
		Alert alt = new Alert(AlertType.INFORMATION);
		alt.setContentText("File " + file.getName() + " successfully imported");
		alt.initOwner(primaryStage);
		alt.showAndWait();

		refreshDashboard(); // Library has changed; refresh the dashboard
	}

	/**
	 * Create a menu item that launches a pop-up for manually adding songs to the
	 * database
	 * 
	 * @return an item to launch the Add Songs pop-up as a MenuItem
	 */
	private MenuItem addSongItem() {
		MenuItem item = new MenuItem("Add Song");
		item.setOnAction(e -> showAddScreen());
		return item;
	}

	/**
	 * Create a menu item that launches a pop-up for manually removing songs from
	 * the database
	 * 
	 * @return an item to launch the Remove Songs pop-up as a MenuItem
	 */
	private MenuItem removeSongItem() {
		MenuItem item = new MenuItem("Remove Song");
		item.setOnAction(e -> showDeleteScreen());
		return item;
	}

	/**
	 * Creates a menu item with a handler to launch the About pop-up
	 * 
	 * @return a MenuItem for the About pop-up
	 */
	private MenuItem aboutMenuItem() {
		MenuItem item = new MenuItem("About This App");
		item.setOnAction(e -> {
			showAboutPopup();
		});
		return item;
	}

	/**
	 * Show a pop-up window displaying the app's About info
	 */
	private void showAboutPopup() {
		Stage exportStage = new Stage();
		VBox vbox = new VBox();
		Text aboutText = new Text("Created by Matthias Schmitz, Adam Cook, Felix Lin, Tomas Perez, and Jon McMahon.\n"
				+ "This app has been approved by Doge for outstanding quality.");
		aboutText.setFont(Font.font("Comic Sans MS"));
		Image image = new Image("Doge.png");
		ImageView iv = new ImageView();
		iv.setImage(image);
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> exportStage.close());
		vbox.getChildren().addAll(aboutText, iv);
		Scene scene = new Scene(vbox);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		exportStage.setTitle("About");
		exportStage.setScene(scene);
		exportStage.show();
	}

	/**
	 * Create a menu item that launches a pop-up with information about how to use
	 * the application
	 * 
	 * @return an item to launch the Help pop-up
	 */
	private MenuItem helpMenuItem() {
		MenuItem item = new MenuItem("Help");
		item.setOnAction(e -> helpScreen());
		return item;
	}

	/**
	 * Show a pop-up window displaying help text for using the application
	 */
	private void helpScreen() {
		// Initialize an HBox to contain the text
		HBox hbox = new HBox();

		// Create and add the help text
		Text text = new Text("This app allows you to import and analyze your music library. Import your library as a\n"
				+ "tab-separated values file (.tsv or .txt). The file should include headers, and have columns for at\n"
				+ "least Artist, Track Title, and Genre. You can also manually add and remove songs via the app's add\n"
				+ "& remove forms. When done with your session you can export those changes to a new .tsv file which\n"
				+ "can be re-imported in a later session.\n");
		text.setStyle("-fx-font-size: 14;");
		hbox.getChildren().addAll(text);

		// Create scene to house the HBox
		Scene scene = new Scene(hbox, 640, 100);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		// Create a pop-up stage, add the scene and show it
		Stage popupStage = new Stage();
		popupStage.setTitle("How To Use This Application");
		popupStage.setScene(scene);
		popupStage.show();
	}

	/**
	 * Show a pop-up window to allow manually adding songs to the database
	 */
	private void showAddScreen() {
		// Initialize a stage and grid for the entry form
		Stage popupStage = new Stage();
		GridPane form = new GridPane();
		form.setPadding(new Insets(5, 0, 0, 5));
		form.setHgap(5.0);
		form.setVgap(5.0);

		// Labels & TextFields for form
		Label titleLabel = new Label("Title");
		TextField titleField = new TextField();
		titleField.setPromptText("Enter title");
		titleField.setPrefWidth(200);
		Label artistLabel = new Label("Artist");
		TextField artistField = new TextField();
		artistField.setPromptText("Enter artist(s)");
		Label genreLabel = new Label("Genre");
		TextField genreField = new TextField();
		genreField.setPromptText("Enter genre");

		// Add Button
		Button addButton = new Button();
		addButton.setText("Add");
		addButton.setDisable(true);
		// Add & Stay Button
		Button addAndStayButton = new Button();
		addAndStayButton.setText("Add and Stay");
		addAndStayButton.setDisable(true);

		// Add actions handlers to the buttons
		addButton.setOnAction(e -> {
			onAddButtonClicked(popupStage, false, titleField, artistField, genreField);
		});

		addAndStayButton.setOnAction(e -> {
			onAddButtonClicked(popupStage, true, titleField, artistField, genreField);
			addAndStayButton.setDisable(true);
			addButton.setDisable(true);
		});

		// Add handlers to each entry field to update the action buttons' availability
		List.of(artistField, titleField, genreField).forEach(field -> field.setOnKeyReleased(
				e -> onAddFormFieldChanged(artistField, titleField, genreField, addButton, addAndStayButton)));

		// Create a button to close the pop-up
		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> popupStage.close());

		// Add the buttons to an HBox so they don't have to match width of entry fields
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(addButton, addAndStayButton, closeButton);

		// Add all of the objects to the form
		form.add(titleLabel, 0, 0);
		form.add(titleField, 1, 0);
		form.add(artistLabel, 0, 1);
		form.add(artistField, 1, 1);
		form.add(genreLabel, 0, 2);
		form.add(genreField, 1, 2);
		form.add(hbox, 0, 3, 2, 1);

		// Create a scene for the pop-up, add the content and show it
		Scene entryScene = new Scene(form, 260, 125);
		entryScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		popupStage.setTitle("Add Song to Library");
		popupStage.setScene(entryScene);
		popupStage.show();
	}

	/**
	 * Handler for when entry fields' values are updated on the Add Songs form
	 * 
	 * @param artistField      the TextField for the artist field
	 * @param titleField       the TextField for the title field
	 * @param genreField       the TextField for the genre field
	 * @param addButton        the Add button
	 * @param addAndStayButton the Add And Stay button
	 */
	private void onAddFormFieldChanged(TextField artistField, TextField titleField, TextField genreField,
			Button addButton, Button addAndStayButton) {

		// Enable the Add and Add & Stay buttons if all 3 fields have a value
		if (!artistField.getText().equals("") && !titleField.getText().equals("") && !genreField.getText().equals("")) {
			addButton.setDisable(false);
			addAndStayButton.setDisable(false);
		}
		// Otherwise disable the Add and Add & Stay buttons
		else {
			addButton.setDisable(true);
			addAndStayButton.setDisable(true);
		}
	}

	/**
	 * Handler for when Add or Add & Stay are clicked on the Add Songs form
	 * 
	 * @param popupStage   the pop-up's stage
	 * @param keepFormOpen set to true to keep the pop-up open after entry
	 * @param titleField   the TextField for the title field
	 * @param artistField  the TextField for the artist field
	 * @param genreField   the TextField for the genre field
	 */
	private void onAddButtonClicked(Stage popupStage, boolean keepFormOpen, TextField titleField, TextField artistField,
			TextField genreField) {

		// Retrieve inputs
		String title = titleField.getText();
		String artist = artistField.getText();
		String genre = genreField.getText();

		// Create an alert we'll use to show the user the result of the action
		Alert alt = new Alert(AlertType.INFORMATION);
		alt.initOwner(popupStage);

		// Try to add the song
		try {
			db.addSong(title, artist, genre);
			// Update the sorted artist list in a separate thread for performance
			Thread thread = new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					sortedList = db.getArtistNames().sorted();
					return null;
				}
			});
			thread.setDaemon(true);
			thread.start();

			// No exception = song was added successfully
			alt.setTitle("Add Song");
			alt.setHeaderText("Song Added");
			alt.setContentText("Song " + title + " added to library");
			// Images are fun
			Image image = new Image("Song.jpg");
			ImageView iv = new ImageView();
			iv.setPreserveRatio(true);
			iv.setFitWidth(50);
			iv.setFitHeight(50);
			iv.setImage(image);
			alt.setGraphic(iv);
		}
		// Show a duplicate song alert
		catch (DuplicateSongException e) {
			alt.setAlertType(AlertType.WARNING);
			alt.setHeaderText("Error: Duplicate song could not be added");
			alt.setContentText(e.getMessage());

		}
		// Show alert indicating other general failures
		catch (Exception e) {
			alt.setAlertType(AlertType.WARNING);
			alt.setHeaderText("Error: Song could not be added");
			alt.setContentText(e.getMessage());
		}
		// Show whichever alert we created
		alt.showAndWait();

		refreshDashboard(); // Library has changed; refresh the dashboard

		// Clear the entry fields
		titleField.clear();
		artistField.clear();
		genreField.clear();

		// Close the pop-up if not Add & Stay
		if (!keepFormOpen) {
			popupStage.close();
		}
	}

	/**
	 * Show a pop-up window to allow manually removing songs from the database
	 */
	private void showDeleteScreen() {
		// Initialize a stage and grid for the entry form
		Stage popupStage = new Stage();
		GridPane form = new GridPane();
		form.setPadding(new Insets(5, 0, 0, 5));
		form.setHgap(5.0);
		form.setVgap(5.0);

		// Labels and ComboBoxes for the form

		// Artist selection
		Label artistLabel = new Label("Artist");
		ComboBox<String> artistBox = new ComboBox<String>();
		// Use ObservableList with Listener to dynamically update
		sortedList.addListener((ListChangeListener<String>) e -> {
			artistBox.setItems(sortedList);
		});
		artistBox.setItems(sortedList);
		artistBox.setPrefWidth(200);

		// Title selection
		Label titleLabel = new Label("Title");
		ComboBox<String> titleBox = new ComboBox<String>();
		titleBox.setPrefWidth(200);
		titleBox.setDisable(true);

		// Update the title dropdown element when artist is changed
		artistBox.setOnAction(e -> {
			// Clear the title dropdown
			titleBox.setValue(null);
			titleBox.getItems().clear();
			// Get the artist's song list
			List<Song> songList = db.getSongList(artistBox.getValue());
			// If the artist has songs, put them in the title dropdown's item list
			if (songList != null) {
				for (Song s : songList) {
					titleBox.getItems().add(s.getTitle());
				}
			}
			// No artist selected = disable title dropdown
			if (artistBox.getValue() == null) {
				titleBox.setDisable(true);
			} else { // Artist selected = enable title dropdown
				titleBox.setDisable(false);
			}
		});

		// Create a Remove button
		Button removeButton = new Button();
		removeButton.setText("Remove");
		removeButton.setOnAction(e -> onRemoveButtonClicked(popupStage, false, artistBox, titleBox));
		removeButton.setDisable(true);

		// Create a Remove & Stay button
		Button removeAndStayButton = new Button();
		removeAndStayButton.setText("Remove and Stay");
		removeAndStayButton.setOnAction(e -> {
			onRemoveButtonClicked(popupStage, true, artistBox, titleBox);
			removeButton.setDisable(true);
			removeAndStayButton.setDisable(true);

		});
		removeAndStayButton.setDisable(true);

		titleBox.setOnAction(e -> {
			if ((titleBox.getValue() == null) || (titleBox.getValue().equals(""))) {
				removeButton.setDisable(true);
				removeAndStayButton.setDisable(true);
			} else {
				removeButton.setDisable(false);
				removeAndStayButton.setDisable(false);
			}
		});

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> popupStage.close());

		// Add buttons to an HBox so they don't have to match width of entry fields
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(removeButton, removeAndStayButton, closeButton);

		// add objects to form
		form.add(artistLabel, 0, 0);
		form.add(artistBox, 1, 0);
		form.add(titleLabel, 0, 1);
		form.add(titleBox, 1, 1);

		form.add(hbox, 0, 2, 2, 1);

		Scene entryScene = new Scene(form, 280, 100);
		popupStage.setTitle("Remove Song from Library");
		entryScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		popupStage.setScene(entryScene);
		popupStage.show();
	}

	/**
	 * Handler for when Remove or Remove & Stay are clicked on the Remove Songs form
	 * 
	 * @param popupStage   the pop-up's stage
	 * @param keepFormOpen set to true to keep the pop-up open after entry
	 * @param titleField   the TextField for the title field
	 * @param artistBox    the ComboBox for the artist dropdown
	 * @param titleBox     the ComboBox for the genre dropdown
	 */
	private void onRemoveButtonClicked(Stage popupStage, boolean keepFormOpen, ComboBox<String> artistBox,
			ComboBox<String> titleBox) {

		// Retrieve inputs
		String artist = artistBox.getValue();
		String title = titleBox.getValue();
		int titleBoxItemsSize = titleBox.getItems().size();

		// Create an alert to show the user the eventual result of the action
		Alert alt = new Alert(AlertType.INFORMATION);
		alt.initOwner(popupStage);

		// Cache off any combo boxes currently set to the artist we're removing
		ArrayList<ComboBox<String>> comboBoxesSetToRemovedArtist = new ArrayList<ComboBox<String>>();
		for (ComboBox<String> cbox : allDashboardComboBoxes) {
			if (cbox.getValue() == artist) {
				comboBoxesSetToRemovedArtist.add(cbox);
			}
		}

		// Attempt to remove the song
		if (db.removeSong(title, artist)) {
			// If removal was successful

			// Clear the artist box if we removed their last song
			if (titleBoxItemsSize < 2) {
				if (keepFormOpen) {
					titleBox.getItems().clear();
					artistBox.setValue(null);
					titleBox.setDisable(true);
				}

				// Also clear any artist boxes on dashboard set to this artist
				comboBoxesSetToRemovedArtist.forEach(cbox -> cbox.setValue(null));
			}

			// Use a thread to update artist dropdown for performance
			Thread thread = new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					sortedList = db.getArtistNames().sorted();
					return null;
				}
			});
			thread.setDaemon(true);
			thread.start();

			// Populate the alert to indicate a successful removal
			alt.setTitle("Remove song");
			alt.setHeaderText("Song Removed");
			alt.setContentText("Song " + title + " removed from library");
			// Images are cool
			Image image = new Image("Song.jpg");
			ImageView iv = new ImageView();
			iv.setPreserveRatio(true);
			iv.setFitWidth(50);
			iv.setFitHeight(50);
			iv.setImage(image);
			alt.setGraphic(iv);
		} else {
			// If removal was not successful, alert should indicate that
			alt.setAlertType(AlertType.WARNING);
			alt.setHeaderText("Error song could not be removed");
			alt.setContentText("Song does not exist in library");
		}
		// Show whichever alert we created
		alt.showAndWait();

		refreshDashboard(); // Library has changed; refresh the dashboard

		// Close the pop-up if we weren't Remove & Staying
		if (!keepFormOpen) {
			popupStage.close();
			return;
		}

		// If the artist has song(s) remaining, populate their new song list
		if (artistBox.getValue() == null) {
			return;
		}
		List<Song> songList = db.getSongList(artistBox.getValue());
		titleBox.getItems().clear();
		for (Song s : songList) {
			titleBox.getItems().add(s.getTitle());
		}
		titleBox.setDisable(false);
	}

	/**
	 * Creates Alert to show information about the success/failure of export
	 * 
	 * @param e        Exception that occurred, or null if there was no exception
	 * @param fileName the file name where export was done (not used if Exception is
	 *                 non-null)
	 * @return the new Alert
	 */
	private Alert getExportAlert(Exception e, String fileName) {
		boolean success = e == null;
		Alert messagebox = new Alert(success ? AlertType.INFORMATION : AlertType.WARNING);
		messagebox.setTitle("Export Status");
		messagebox.setHeaderText(success ? "Export successful" : "Export failed");
		messagebox.setContentText(success ? String.format("Library exported to: %s", fileName) : e.getMessage());
		messagebox.initModality(Modality.APPLICATION_MODAL);
		messagebox.initOwner(primaryStage);
		return messagebox;
	}

	/**
	 * Creates pop-up to pick file using OS' native file chooser
	 * @param shouldExit whether or not we should exit the app once the export is complete
	 */
	private void exportFileWithPicker(Boolean shouldExit) {
		FileChooser fileChooser = getFileChooser();
		File file = fileChooser.showSaveDialog(primaryStage);
		Alert alt = new Alert(AlertType.INFORMATION);
		if (file != null) {
			try {
				db.outputFile(file);
				alt = getExportAlert(null, file.getAbsolutePath());
			} catch (IOException ex) {
				alt = getExportAlert(ex, file.getAbsolutePath());
			} finally {
				alt.showAndWait();
				if (shouldExit) Platform.exit();
			}
		}
	}

	/**
	 * Creates a combo box wrapped in an HBox, which puts some padding under the
	 * combo box. Also creates a Clear button associated with the combo box.
	 * 
	 * @param linkedTable the table whose data is linked to this combo box
	 * @return an HBox containing the generated combo box
	 */
	private HBox createArtistSelectComboBoxContainer(ComboBox<String> cbox, boolean useFullArtistList,
			EventHandler<ActionEvent> handler) {
		// Initialize the HBox container, padding on the bottom
		HBox hBox = new HBox();
		hBox.setPadding(new Insets(0, 0, 5, 0));

		// Use an ObservableList with Listener to dynamically update the artist list
		if (useFullArtistList) {
			sortedList.addListener((ListChangeListener<String>) e -> {
				cbox.setItems(sortedList);
			});
			cbox.setItems(sortedList);
		}

		// Add the passed in handler to the ComboBox's action event
		cbox.setOnAction(handler);
		// Give it a width and add the combo box to the HBox container
		cbox.setPrefWidth(200);
		hBox.getChildren().add(cbox);

		// Add a button that can clear the combo box
		VBox vBox = new VBox();
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(e -> cbox.setValue(null));
		vBox.getChildren().add(clearButton);
		vBox.setPadding(new Insets(0, 0, 0, 10));
		hBox.getChildren().add(vBox);

		// Allow us to look up the ComboBox's associated clear button later
		comboBoxClearButtonMap.put(cbox, clearButton);

		return hBox;
	}

	/**
	 * Helper to enable/disable both a combo box's associated clear button
	 * 
	 * @param cbox    the ComboBox to disable
	 * @param disable true = disable, false = enable
	 */
	private void setDisableForClearButton(ComboBox<String> cbox, boolean disable) {
		comboBoxClearButtonMap.get(cbox).setDisable(disable);
	}

	/**
	 * Creates a help icon node with a tool-tip
	 * 
	 * @param content the text content of the help tool-tip
	 * @return a help icon with a tool-tip
	 */
	private HBox createHelpIcon(String content) {
		// Create container node to return
		HBox h = new HBox();

		Image helpIcon = new Image("help.png");
		ImageView iv = new ImageView();
		iv.setImage(helpIcon);
		Tooltip t = new Tooltip(content);
		t.setShowDelay(Duration.seconds(0.1));
		t.setShowDuration(Duration.seconds(60));
		Tooltip.install(iv, t);

		h.getChildren().add(iv);
		h.setAlignment(Pos.CENTER);
		h.setPadding(new Insets(0, 0, 0, 5));
		return h;
	}

	/**
	 * Refresh all tables on the dashboard, presumably after the library has changed
	 */
	private void refreshDashboard() {
		// If artists no longer exist due to a removal, clear them
		allDashboardComboBoxes.forEach(cbox -> {
			if (!sortedList.contains(cbox.getValue())) {
				cbox.setValue(null);
			}
		});

		populateSimilarSongsTable();
		populateSongsForArtistTable();

		// Library changes might change the disabled state of the DoS combo boxes.
		// This updates the combo boxes AND refreshes the table
		degreesOfSeparationComboBox1.fireEvent(new ActionEvent());
	}

	/**
	 * Main entry point for the application
	 * 
	 * @param args args[0] = filepath for initial import file to load on launch
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
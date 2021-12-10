package application;
// A lot of imports

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
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
 * This class defines the GUI for the Collaboration Analyzer. Look at this Graph
 * https://www.youtube.com/watch?v=sIlNIVXpIns
 * 
 * @author Matthias Schmitz
 *
 */

public class CollaborationAnalyzerApp extends Application {
	// get dimensions of screen so we can default to opening in full screen
	Rectangle2D screenBounds = Screen.getPrimary().getBounds();

	// Properties
	private final double WINDOW_WIDTH = screenBounds.getMaxX() - 100;
	private final double WINDOW_HEIGHT = screenBounds.getMaxY() - 100;
	private static final String APP_TITLE = "Music Library";
	private String SIMILAR_TRACKS_HELP_TOOLTIP = "Select an artist in the dropdown to display songs similar to those of that artist.\n"
			+ "\n"
			+ "Finds other artists the selected artist has collorated with, and displays those other artists' songs.";
	private String DEGREES_SEPARATION_HELP_TOOLTIP = "Select two artists in the dropdown to display their degrees of separation,\n"
			+ "based on their collorations with other artists.\n" + "\n"
			+ "Displays the songs that connect the two artists on the shortest possible path.";
	private String SONGS_FOR_ARTIST_HELP_TOOLTIP = "Select an artist in the dropdown to display that artist's songs.";
	private static CollaborationAnalyzer db;
	private static ObservableList<String> sortedList;
	private static ObservableList<String> songTitles;

	/**
	 * Runs the whole enchilada
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		// create back-end DB
		db = new CollaborationAnalyzer();
		sortedList = db.getArtistNames().sorted();
		songTitles = db.getSongSet();
		// Layout
		// *top - ToolBar with import, add, remove, and export options
		// *left - find similar tracks
		// *right - look at this graph (of collaborators)
		// *bottom - degrees of separation

		// Main layout is Border Pane example (top,left,center,right,bottom)
		BorderPane root = new BorderPane();
		VBox stVbox = similarTracks();
		VBox degreesSep = degreesOfSeparation();
		MenuBar toolbar = toolbar(primaryStage);
		VBox songsVbox = songsForArtist();
		Label librarySizeLabel = new Label("Library Size" + songTitles.size());
		HBox sizeBox = new HBox();

		songTitles.addListener((ListChangeListener<String>) e -> {
			librarySizeLabel.getText();
			librarySizeLabel.setText("Library Size" + songTitles.size());
		});
		sizeBox.getChildren().add(librarySizeLabel);
		sizeBox.setAlignment(Pos.CENTER_RIGHT);
		// Add the vertical box to the center of the root pane
		root.setTop(toolbar);
		root.setRight(songsVbox);
		root.setLeft(stVbox);
		root.setBottom(degreesSep);
		root.setCenter(sizeBox);
		root.setPrefHeight(WINDOW_HEIGHT);
		root.setPrefWidth(WINDOW_WIDTH);

		// Spacing
		Insets insets = new Insets(5);
		BorderPane.setMargin(stVbox, insets);
		BorderPane.setMargin(degreesSep, insets);

		// Set size of components
		stVbox.setPrefWidth(WINDOW_WIDTH / 2 - 5);
		stVbox.setPrefHeight(WINDOW_HEIGHT / 2 - 50);
		stVbox.setMaxHeight(WINDOW_HEIGHT / 2 - 50);
		stVbox.setMinHeight(WINDOW_HEIGHT / 4);
		degreesSep.setMaxWidth(WINDOW_WIDTH * 2 / 3);
		degreesSep.setPrefHeight(WINDOW_HEIGHT / 2 - 50);
		songsVbox.setMaxHeight(WINDOW_HEIGHT / 2 - 50);
		songsVbox.setPrefHeight(WINDOW_HEIGHT / 2 - 50);
		songsVbox.setPrefWidth(WINDOW_WIDTH / 2 - 5);
		songsVbox.setMinHeight(WINDOW_HEIGHT / 4);

		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		// Add CSS to make the app pretty
		mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		// Confirm user wants to exit when selecting exit button
		Platform.setImplicitExit(false);
		primaryStage.setOnCloseRequest(e -> {
			Alert alt = new Alert(AlertType.CONFIRMATION);
			alt.initModality(Modality.APPLICATION_MODAL);
			alt.initOwner(primaryStage);
			alt.setContentText("Are you sure you want to exit the app?");
			Optional<ButtonType> altSelection = alt.showAndWait();
			if (ButtonType.OK.equals(altSelection.get())) {
				Platform.exit();
			} else {
				alt.close();
				e.consume();

			}
		});

		// The moment you've been waiting for.
		primaryStage.setTitle(APP_TITLE);
		// Set the stage
		primaryStage.setScene(mainScene);
		// SHOWTIME!
		primaryStage.show();
	}

	/**
	 * Generate the similar tracks component of the dashboard
	 * 
	 * @return
	 */
	private VBox similarTracks() {
		// initialize VBox
		VBox stVbox = new VBox();

		// Create and add an HBox for the section's header
		HBox titleBox = new HBox();
		Label title = new Label("Find Similar Tracks");
		title.setStyle("-fx-font-size: 20;");
		titleBox.getChildren().add(title);
		titleBox.getChildren().add(createHelpIcon(SIMILAR_TRACKS_HELP_TOOLTIP));
		stVbox.getChildren().add(titleBox);

		// Create TableView & columns
		TableView<Song> table = new TableView<Song>();
		table.setPlaceholder(new Label("Select artist to view similar tracks"));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		TableColumn<Song, String> titleCol = new TableColumn<Song, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Song, String> artistCol = new TableColumn<Song, String>("Artists");
		artistCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
		artistCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
				cellData.getValue().getArtists().toString().replace("[", "").replace("]", "")));
		TableColumn<Song, String> genreCol = new TableColumn<Song, String>("Genre");
		genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));

		// add columns to table
		table.getColumns().add(titleCol);
		table.getColumns().add(artistCol);
		table.getColumns().add(genreCol);

		// ComboBox to select artist
		ComboBox<String> cbox = new ComboBox<String>();
		stVbox.getChildren().add(createArtistSelectComboBoxContainer(cbox, e -> {
			populateSimilarTable(table, cbox.getValue());
			if (cbox.getValue() == null)
				table.setPlaceholder(new Label("Select artist to view similar tracks"));
		}));

		// set tale properties so it resizes nicely
		table.setFixedCellSize(25);
		table.prefHeightProperty()
				.bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.11)));
		table.minHeightProperty().set(50);
		table.maxHeightProperty().set(300);

		stVbox.getChildren().add(table);
		return stVbox;
	}

	/**
	 * Add data to the similar tracks table
	 * 
	 * @param table
	 * @param selectedArtist
	 */
	private void populateSimilarTable(TableView<Song> table, String selectedArtist) {
		// TableView to display results
		table.getItems().clear();
		List<Song> collabSongs = db.listCollaboratedSongs(selectedArtist);
		if (collabSongs != null) {
			for (Song song : collabSongs) {
				table.getItems().add(song);
			}
		} else {
			table.setPlaceholder(new Label("No similar tracks for artist"));
		}
	}

	/**
	 * Generate the degrees of separation component
	 * 
	 * @return
	 */
	private VBox degreesOfSeparation() {
		// VBox for the overall component
		VBox vbox = new VBox();

		// HBox with GridPane for the menu to select artists
		HBox hbox = new HBox(5);

		// HBox for the header
		HBox titleBox = new HBox();
		Label title = new Label("Degrees Of Separation");
		title.setStyle("-fx-font-size: 20;");
		titleBox.getChildren().add(title);
		titleBox.getChildren().add(createHelpIcon(DEGREES_SEPARATION_HELP_TOOLTIP));
		vbox.getChildren().add(titleBox);

		// TableView to display degrees of separation
		TableView<Song> table = degreesSepTable();
		table.setPlaceholder(new Label("Select two artists to view degrees separation"));

		// ComobBoxes to pick artists
		// First Artist
		Label artist1Label = new Label("First Artist");
		artist1Label.setPrefWidth(85);
		ComboBox<String> artist1Box = new ComboBox<String>();
		// Second Artist
		Label artist2Label = new Label("Second Artist");
		artist2Label.setPrefWidth(85);
		ComboBox<String> artist2Box = new ComboBox<String>();

		// Add selection elements to a grid
		GridPane grid = new GridPane();
		grid.add(artist1Label, 0, 0);
		grid.add(createArtistSelectComboBoxContainer(artist1Box, e -> {
			populateSepTable(table, artist1Box.getValue(), artist2Box.getValue());
			if ((artist1Box.getValue() == null) || (artist2Box.getValue() == null)) {
				table.setPlaceholder(new Label("Select two artists to view degrees separation"));
			} else {
				table.setPlaceholder(new Label("No connection between artists"));
			}
		}), 1, 0);
		grid.add(artist2Label, 0, 1);
		grid.add(createArtistSelectComboBoxContainer(artist2Box, e -> {
			populateSepTable(table, artist1Box.getValue(), artist2Box.getValue());
			if ((artist1Box.getValue() == null) || (artist2Box.getValue() == null)) {
				table.setPlaceholder(new Label("Select two artists to view degrees separation"));
			} else {
				table.setPlaceholder(new Label("No connection between artists"));
			}
		}), 1, 1);

		Pane divider = new Pane();
		divider.setPrefWidth(20);
		hbox.getChildren().addAll(grid, divider, table);

		vbox.getChildren().add(hbox);
		return vbox;
	}

	/**
	 * Generate the degrees of separation table
	 * 
	 * @return
	 */
	private static TableView<Song> degreesSepTable() {
		// initialize table & set properties
		TableView<Song> table = new TableView<Song>();
		table.setPrefWidth(500);
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setFixedCellSize(25);
		table.prefHeightProperty()
				.bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.01)));
		table.minHeightProperty().set(70);
		table.maxHeightProperty().bind(table.prefHeightProperty());

		// set columns for table
		TableColumn<Song, Number> numberCol = new TableColumn<Song, Number>("Degrees Separation");
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

		// table.prefHeightProperty().bind(table.fixedCellSizeProperty().multiply(Bindings.size(table.getItems()).add(1.11)));
		// table.minHeightProperty().set(50);
		// table.setMaxHeight(200);

		return table;
	}

	/**
	 * Populate the degrees of separation table with data. Calling this "refreshes"
	 * the table
	 * 
	 * @param table
	 * @param name1
	 * @param name2
	 */
	private void populateSepTable(TableView<Song> table, String name1, String name2) {
		// Clear any existing data
		table.getItems().clear();
		if ((name1 == null) || (name2 == null))
			return;
		// Get list of songs from DB & add to table
		List<Song> sepSongs = db.getPathBetweenArtists(name1, name2);
		if ((sepSongs != null) && (sepSongs.size() > 0)) {
			for (Song song : sepSongs) {
				table.getItems().add(song);
			}
//    	} else if (name1.equals("") || name2.equals("")) {
//    		table.setPlaceholder(new Label("Two artists must be selected to view degrees separation"));
//
//    	} else {
//    		table.setPlaceholder(new Label("No connection between artists"));
		}

	}

	/**
	 * Create toolbar component of dashboard
	 * 
	 * @param stage
	 * @return toolbar as MenuBar object
	 */
	private MenuBar toolbar(Stage stage) {
		MenuBar menuBar = new MenuBar();

		// Create Menus
		Menu addRemoveMenu = new Menu("Add/Remove Songs");
		Menu aboutMenu = new Menu("About");
		Menu exportMenu = new Menu("Export Library");

		// Generate MenuItems for addRemoveMenu
		MenuItem importItem = fileSelectItem(stage);
		MenuItem entryItem = addSongItem();
		MenuItem deleteItem = removeSongItem();

		// Export button
		MenuItem exportItem = new MenuItem("Export to file");
		exportItem.setOnAction(e -> exportScreen());

		// Items for About Menu
		MenuItem aboutItem = aboutMenuItem();
		MenuItem helpItem = helpMenuItem();
		aboutMenu.getItems().addAll(aboutItem, helpItem);

		addRemoveMenu.getItems().addAll(importItem, entryItem, deleteItem);
		exportMenu.getItems().add(exportItem);
		menuBar.getMenus().addAll(aboutMenu, addRemoveMenu, exportMenu);

		return menuBar;
	}

	/**
	 * MenuItem to select a file
	 * 
	 * @param stage
	 * @return
	 */
	private static MenuItem fileSelectItem(Stage stage) {
		// create FileChooser we'll use to select the file
		final FileChooser fileChooser = new FileChooser();

		// create the MenuItem
		MenuItem importItem = new MenuItem("Import File of Songs");

		// open the file selection window when item is clicked
		importItem.setOnAction(e -> {
			try {
				File file = fileChooser.showOpenDialog(stage);
				if (file != null) {
					String fileName = file.getName();
					db.importFile(file);
					Thread thread = new Thread(new Task<Void>() {
						@Override
						protected Void call() throws Exception {
							sortedList = db.getArtistNames().sorted();
							return null;
						}
					});
					thread.setDaemon(true);
					thread.start();
					// sortedList = db.getArtistNames().sorted();
					Alert alt = new Alert(AlertType.INFORMATION);
					alt.setContentText("File " + fileName + " successfully imported");
					alt.initOwner(stage);
					alt.showAndWait();
				}
			} catch (IOException exception) {
				Alert alt = new Alert(AlertType.WARNING);
				alt.setContentText("Unable to open read file");
				alt.showAndWait();
			}
		});
		return importItem;

	}

	/**
	 * MenuItem to add song to DB
	 * 
	 * @return
	 */
	private MenuItem addSongItem() {
		MenuItem item = new MenuItem("Add Song");
		item.setOnAction(e -> showAddScreen());
		return item;
	}

	/**
	 * MenuItem to remove song from DB
	 * 
	 * @return
	 */
	private MenuItem removeSongItem() {
		MenuItem item = new MenuItem("Remove Song");
		item.setOnAction(e -> showDeleteScreen());
		return item;
	}

	private MenuItem helpMenuItem() {
		MenuItem item = new MenuItem("Help");
		item.setOnAction(e -> helpScreen());
		return item;
	}

	private void helpScreen() {
		HBox hbox = new HBox();
		Text text = new Text("This app allows you to import and analyze your music library. Import your library as a\n"
				+ "tab-separated values file (.tsv or .txt). The file should include headers, and have columns for at\n"
				+ "least Artist, Track Title, and Genre. You can also manually add and remove songs via the app's add\n"
				+ "& remove forms. When done with your session you can export those changes to a new .tsv file which\n"
				+ "can be re-imported in a later session.\n");
		text.setStyle("-fx-font-size: 14;");

		hbox.getChildren().addAll(text);

		// Create scene
		Scene scene = new Scene(hbox, 640, 100);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());

		// Add title & scene to stage and show
		Stage popupStage = new Stage();
		popupStage.setTitle("How To Use This Application");
		popupStage.setScene(scene);
		popupStage.show();
	}

	/**
	 * Pop-up window to add song to database
	 * 
	 * @param shouldAdd
	 */
	private void showAddScreen() {
		Stage popupStage = new Stage();
		// create entry form using GridPane
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

		// Action Buttons
		Button addButton = new Button();
		addButton.setText("Add");

		Button addAndStayButton = new Button();
		addAndStayButton.setText("Add and Stay");

		// Add actions to buttons
		addButton.setOnAction(e -> {
			addButtonAction(popupStage, true, titleField, artistField, genreField);
		});
		addButton.setDisable(true);

		addAndStayButton.setOnAction(e -> {
			addButtonAction(popupStage, false, titleField, artistField, genreField);
		});
		addAndStayButton.setDisable(true);

		artistField.setOnKeyReleased(e -> {
			if (!artistField.getText().equals("") && !titleField.getText().equals("")
					&& !genreField.getText().equals("")) {
				addButton.setDisable(false);
				addAndStayButton.setDisable(false);
			} else {
				addButton.setDisable(true);
				addAndStayButton.setDisable(true);
			}
		});

		titleField.setOnKeyReleased(e -> {
			if (!artistField.getText().equals("") && !titleField.getText().equals("")
					&& !genreField.getText().equals("")) {
				addButton.setDisable(false);
				addAndStayButton.setDisable(false);
			} else {
				addButton.setDisable(true);
				addAndStayButton.setDisable(true);
			}
		});

		genreField.setOnKeyReleased(e -> {
			if (!artistField.getText().equals("") && !titleField.getText().equals("")
					&& !genreField.getText().equals("")) {
				addButton.setDisable(false);
				addAndStayButton.setDisable(false);
			} else {
				addButton.setDisable(true);
				addAndStayButton.setDisable(true);
			}
		});

		Button closeButton = new Button("Close");
		closeButton.setOnAction(e -> popupStage.close());

		// Add buttons to an HBox so they don't have to match width of entry fields
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(addButton, addAndStayButton, closeButton);

		// add objects to form
		form.add(titleLabel, 0, 0);
		form.add(titleField, 1, 0);
		form.add(artistLabel, 0, 1);
		form.add(artistField, 1, 1);
		form.add(genreLabel, 0, 2);
		form.add(genreField, 1, 2);
		form.add(hbox, 0, 3, 2, 1);

		Scene entryScene = new Scene(form, 260, 125);
		entryScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		popupStage.setTitle("Add Song to Library");
		popupStage.setScene(entryScene);
		popupStage.show();
	}

	private static void addButtonAction(Stage popupStage, boolean isSingleAction, TextField titleField,
			TextField artistField, TextField genreField) {
		// retrieve inputs
		String title = titleField.getText();
		String artist = artistField.getText();
		String genre = genreField.getText();

		// create alert
		Alert alt = new Alert(AlertType.INFORMATION);
		alt.initOwner(popupStage);

		try {
			db.addSong(title, artist, genre);
			Thread thread = new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					sortedList = db.getArtistNames().sorted();
					return null;
				}
			});
			thread.setDaemon(true);
			thread.start();
			alt.setTitle("Add Song");
			alt.setHeaderText("Song Added");
			alt.setContentText("Song " + title + " added to library");
			Image image = new Image("Song.jpg");
			ImageView iv = new ImageView();
			iv.setPreserveRatio(true);
			iv.setFitWidth(50);
			iv.setFitHeight(50);
			iv.setImage(image);
			alt.setGraphic(iv);

		} catch (DuplicateSongException e) {
			alt.setAlertType(AlertType.WARNING);
			alt.setHeaderText("Error: Duplicate song could not be added");
			alt.setContentText(e.getMessage());

		} catch (Exception e) {
			alt.setAlertType(AlertType.WARNING);
			alt.setHeaderText("Error: Song could not be added");
			alt.setContentText(e.getMessage());
		}

		// show the alert
		alt.showAndWait();

		// clear fields to refresh
		titleField.clear();
		artistField.clear();
		genreField.clear();
		if (isSingleAction)
			popupStage.close();
	}

	private void showDeleteScreen() {
		Stage popupStage = new Stage();
		// create entry form using GridPane
		GridPane form = new GridPane();
		form.setPadding(new Insets(5, 0, 0, 5));
		form.setHgap(5.0);
		form.setVgap(5.0);

		// Labels & TextFields for form
		// ComboBox to select artist
		Label artistLabel = new Label("Artist");
		ComboBox<String> artistBox = new ComboBox<String>();
		// User ObservableList with Listener to dynamically update
		sortedList.addListener((ListChangeListener<String>) e -> {
			artistBox.setItems(sortedList);
		});
		artistBox.setItems(sortedList);
		artistBox.setPrefWidth(200);

		Label titleLabel = new Label("Title");
		ComboBox<String> titleBox = new ComboBox<String>();
		titleBox.setPrefWidth(200);
		titleBox.setDisable(true);

		artistBox.setOnAction(e -> {
			titleBox.setValue("");
			titleBox.getItems().clear();
			List<Song> songList = db.getSongList(artistBox.getValue());
			if (songList != null) {
				for (Song s : songList) {
					titleBox.getItems().add(s.getTitle());
				}
			}
			if (artistBox.getValue() == null) {
				titleBox.setDisable(true);
			} else {
				titleBox.setDisable(false);
			}
		});

		Button removeButton = new Button();
		removeButton.setText("Remove");
		removeButton.setOnAction(e -> removeButtonAction(popupStage, true, artistBox, titleBox));
		removeButton.setDisable(true);

		Button removeAndStayButton = new Button();
		removeAndStayButton.setText("Remove and Stay");
		removeAndStayButton.setOnAction(e -> {
			removeButtonAction(popupStage, false, artistBox, titleBox);
			removeButton.setDisable(true);
			removeAndStayButton.setDisable(true);

		});
		removeAndStayButton.setDisable(true);

		titleBox.setOnAction(e -> {
			if (titleBox.getValue() == null) {
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

		Scene entryScene = new Scene(form, 260, 100);
		popupStage.setTitle("Remove Song from Library");
		entryScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		popupStage.setScene(entryScene);
		popupStage.show();
	}

	private static void removeButtonAction(Stage popupStage, boolean isSingleAction, ComboBox<String> artistBox,
			ComboBox<String> titleBox) {
		// retrieve inputs
		String artist = artistBox.getValue();
		String title = titleBox.getValue();
		// create alert
		Alert alt = new Alert(AlertType.INFORMATION);
		alt.initOwner(popupStage);
		Boolean removed = db.removeSong(title, artist);
		if (removed) {
			titleBox.getItems().clear();
			Thread thread = new Thread(new Task<Void>() {
				@Override
				protected Void call() throws Exception {
					sortedList = db.getArtistNames().sorted();
					return null;
				}
			});
			thread.setDaemon(true);
			thread.start();
			alt.setTitle("Remove song");
			alt.setHeaderText("Song Removed");
			alt.setContentText("Song " + title + " removed from library");
			Image image = new Image("Song.jpg");
			ImageView iv = new ImageView();
			iv.setPreserveRatio(true);
			iv.setFitWidth(50);
			iv.setFitHeight(50);
			iv.setImage(image);
			alt.setGraphic(iv);

		} else {
			alt.setAlertType(AlertType.WARNING);
			alt.setHeaderText("Error song could not be removed");
			alt.setContentText("Song does not exist in library");
		}

		// show the alert
		alt.showAndWait();

		// clear fields to refresh
		if (isSingleAction) {
			popupStage.close();
		} else {
			// refresh the title ComboBox
			titleBox.getItems().clear();
			List<Song> songList = db.getSongList(artistBox.getValue());
			if (songList != null & (songList.size() > 0)) {
				for (Song s : songList) {
					titleBox.getItems().add(s.getTitle());
				}
				titleBox.setDisable(false);
			} else {
				artistBox.setValue("");
				titleBox.setDisable(true);
			}
		}
	}

	/**
	 * Pop-up to export library to file
	 */
	private void exportScreen() {
		Stage exportStage = new Stage();

		// create entry form using GridPane
		GridPane form = new GridPane();
		form.setHgap(5.0);
		form.setVgap(5.0);
		form.setPrefWidth(390);
		form.setPadding(new Insets(5, 0, 0, 5));

		// entry field for form
		Label fileLabel = new Label("Filename ");
		TextField fileField = new TextField();
		fileField.setPrefWidth(275);
		fileField.setPromptText("Enter name of file to export library to");

		// Text to warn user if no file name is entered
		Text warning = new Text();

		// Create buttons for form
		Button exportButton = new Button("Export");
		exportButton.setDisable(true);
		exportButton.setOnAction(e -> {
			String fileName = fileField.getText();
			if (!fileName.equals("")) {
				try {
					db.outputFile(fileName);
					Alert alt = new Alert(AlertType.INFORMATION);
					exportStage.close();
					alt.setTitle("Export");
					alt.setHeaderText("Export Successful");
					alt.setContentText("Library exported to " + fileField.getText());
					alt.showAndWait();
				} catch (FileNotFoundException exception) {
					Alert alt = new Alert(AlertType.WARNING);
					alt.setHeaderText("Error: could not expert library");
					alt.setContentText(exception.getMessage());
				}
			} else {
				warning.setText("Error: filename must be entered");
			}
		});
		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> exportStage.close());

		fileField.setOnKeyReleased(e -> {
			if (!fileField.getText().equals("")) {
				exportButton.setDisable(false);
			} else {
				exportButton.setDisable(true);
			}
		});

		// add objects to form
		form.add(fileLabel, 0, 0);
		form.add(fileField, 1, 0);
		form.add(exportButton, 0, 1);
		form.add(cancelButton, 1, 1);
		form.add(warning, 0, 2, 2, 1);

		// generate the scene
		Scene entryScene = new Scene(form, 350, 65);
		entryScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		exportStage.setTitle("Export Library");
		exportStage.setScene(entryScene);
		exportStage.show();

	}

	/**
	 * Create component to display list of songs for artist
	 */
	private VBox songsForArtist() {
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

		// Use TableView to display
		TableView<Song> table = new TableView<Song>();
		table.setPlaceholder(new Label("Select artist to find songs"));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// Create and add columns to TableView
		TableColumn<Song, String> titleCol = new TableColumn<Song, String>("Title");
		titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
		TableColumn<Song, String> artistCol = new TableColumn<Song, String>("Artists");
		// artistCol.setCellValueFactory(new PropertyValueFactory<>("artists"));
		artistCol.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(
				cellData.getValue().getArtists().toString().replace("[", "").replace("]", "")));
		TableColumn<Song, String> genreCol = new TableColumn<Song, String>("Genre");
		genreCol.setCellValueFactory(new PropertyValueFactory<>("genre"));
		table.getColumns().add(titleCol);
		table.getColumns().add(artistCol);
		table.getColumns().add(genreCol);

		// ComboBox to select artists
		ComboBox<String> cbox = new ComboBox<String>();
		vbox.getChildren()
				.add(createArtistSelectComboBoxContainer(cbox, e -> populateSongsTable(table, cbox.getValue())));
		vbox.getChildren().add(table);
		return vbox;
	}

	/**
	 * Creates a combo box wrapped in an HBox, which puts some padding under the
	 * combo box
	 * 
	 * @param linkedTable the table whose data is linked to this combo box
	 * @return an HBox containing the generated combo box
	 */
	private HBox createArtistSelectComboBoxContainer(ComboBox<String> cbox, EventHandler<ActionEvent> handler) {
		HBox hBox = new HBox();

		// User ObservableList with Listener to dynamically update
		sortedList.addListener((ListChangeListener<String>) e -> {
			cbox.setItems(sortedList);
		});
		cbox.setItems(sortedList);
		cbox.setOnAction(handler);
		cbox.setPrefWidth(200);
		hBox.getChildren().add(cbox);

		// Add a button that can clear the combo box
		VBox vBox = new VBox();
		Button clearButton = new Button("Clear");
		clearButton.setOnAction(e -> cbox.setValue(null));
		vBox.getChildren().add(clearButton);
		vBox.setPadding(new Insets(0, 0, 0, 10));
		hBox.getChildren().add(vBox);

		hBox.setPadding(new Insets(0, 0, 5, 0));
		return hBox;
	}

	private void populateSongsTable(TableView<Song> table, String artist) {
		table.getItems().clear();
		if (artist != null) {
			List<Song> songs = db.getSongList(artist);
			for (Song song : songs) {
				table.getItems().add(song);
			}
		}

	}

	private MenuItem aboutMenuItem() {
		MenuItem item = new MenuItem("About This App");
		item.setOnAction(e -> {
			showAboutPopup();
		});
		return item;
	}

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
	 * Creates a help icon node with a tooltip
	 * 
	 * @param content the text content of the help tooltip
	 * @return a help icon with a tooltip
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
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}

# super-train-proposal
Fall 2021 CS400-010 A-Team 4 Final Project:
 Collaboration Analyzer


Team Members:
 Adam Cook - x-Team 4, cook29@wisc.edu
 Felix Lin - x-Team 4, flin45@wisc.edu
 Tomas Perez - x-Team 4, tperez7@wisc.edu
 Matthias Schmitz - x-Team 4, mjschmitz6@wisc.edu
 Jon McMahon - x-Team 5, jmmcmahon4@wisc.edu


Import file info:

 - Import (and export files) should be in .tsv (tab-separated values) format. You'll need columns for Artist, Track Title, Genre, and optionally Remixer for the import to be successful. Each column can be in whatever column position, e.g. Artist doesn't need to be the first column. the import process will find it based on the header name, so you need to have a header and the column header names need to exactly match what the program expects.

 - MainMusicLibraryFile.txt is the full music library intended for use with the app. You can specify it as the 1st command line parameter to load it (or another import file of your choice) when the application starts.

 - MusicLibraryTestIn.txt is a smaller portion of the library that can be easier to test simple operations with.

 - DegreesOfSeparationAnalysis.txt can be copied into Excel. Read it to find good test cases for degrees of separation when using MainMusicLibraryFile.txt imported into the app. It lists the longest shortest path from each artist to another artist. Exact path may vary in the actual app, but the number should match.
 


Reminders for running things in Eclipse:

 - Use the application package for all java and css files

 - Put helper files (import/export/images) in the main project folder

 - Since CollaborationAnalyzer uses JavaFX classes, the Run Configuration for
   CollaborationAnalyzerTest -> Dependencies -> classpath needs the external
   jar for javafx.base added


How to create and run executable.jar
(update to match your filepaths; the run line can be copied from your Run Config's arguments in Eclipse):

>jar -cfm executable.jar manifest.txt .

>java --module-path "<javafx location>\javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar executable.jar


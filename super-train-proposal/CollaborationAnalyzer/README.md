# super-train-proposal
Fall 2021 CS400-010 A-Team 4 Final Project:
 Collaboration Analyzer


Team Members:
 Adam Cook - x-Team 4,
 Felix Lin - x-Team 4,
 Tomas Perez - x-Team 4,
 Matthias Schmitz - x-Team 4,
 Jon McMahon - x-Team 5


Import file info:

 - MainMusicLibraryFile.txt is the full music library intended for use with the app

 - MusicLibraryTestIn.txt is a smaller portion of the library you can test with

 - DegreesOfSeparationAnalysis.txt can be copied into Excel. Read it to find good test cases for degrees of separation. It lists the longest shortest path from each artist to another artist. Exact path may vary in the actual app, but the number should match.
 


Reminders for running things in Eclipse:

 - Use the application package for all java and css files

 - Put helper files (import/export/images) in the main project folder

 - Since CollaborationAnalyzer uses JavaFX classes, the Run Configuration for
   CollaborationAnalyzerTest -> Dependencies -> classpath needs the external
   jar for javafx.base added


How to create and run executable.jar
(update to match your filepaths; the run line can be copied from your Run Config's arguments in Eclipse
):

>jar -cfm executable.jar manifest.txt .

>java --module-path "<javafx location>\javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml -jar executable.jar


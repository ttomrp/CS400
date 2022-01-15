MP = --module-path "C:\Users\acook\openjfx-17.0.1_windows-x64_bin-sdk\javafx-sdk-17.0.1\lib" --add-modules javafx.controls,javafx.fxml

SRC = application/*.java

APP = application.CollaborationAnalyzerApp

ARGS = MainMusicLibraryFile.txt

compile:
	javac $(SRC) 

run:
	java $(MP) $(APP) $(ARGS)

jar:
	jar -cvmf manifest.txt executable.jar .

runjar:
	java $(MP) -jar executable.jar $(ARGS)

runjartest:
	java $(MP) -jar executable.jar MusicLibraryTestIn.txt

zip: 
	zip -r ateam.zip .

clean:
	rm -f application/*.class
	rm -f executable.jar
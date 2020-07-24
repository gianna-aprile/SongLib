// James Litton jkl135 & Gianna Aprile gna11

package view;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class SongLibController {
	@FXML Text selected;
	@FXML Button editSelected;
	@FXML Button deleteSelected;
	@FXML Button addUpdateSong;
	@FXML Button cancelAddUpdateSong;
	@FXML TextField title;
	@FXML TextField artist;
	@FXML TextField album;
	@FXML TextField year;
	@FXML ListView<Song> songList; 
	
	// Global List of recently retrieved songs from CSV file for the session
	ArrayList<Song> songs = new ArrayList<Song>();
	ObservableList<Song> observableList = FXCollections.observableList(songs);
	Boolean editSong = false;
	Boolean editDialogue = false;
	
	

	
	public void initialize() {
		
		
		songList.setItems(observableList);
		
		loadList();
		
		songList.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Song>() {

		    @Override
		    public void changed(ObservableValue<? extends Song> observable, Song oldValue, Song newValue) {
		    	
		    	if(newValue!=null) selected.setText(newValue.getSongInfo());
		    	else selected.setText("    -    -    -    ");
		    }
		});
		
		
		songList.getSelectionModel().select(0);

	}
	
	
	@FXML
	private void editSong(ActionEvent event) {
		
		addUpdateSong.setText("Update Song Info");
		editSong = true;
		Song currSong = songList.getSelectionModel().getSelectedItem();
		title.setText(currSong.getTitle());
		artist.setText(currSong.getArtist());
		album.setText(currSong.getAlbum());
		year.setText(currSong.getYear());
		
	}
	
	@FXML
	private void addUpdate(ActionEvent event)
	{
	    // Check for duplicates in song list
		// Add new or updated song to song list and set flag back
		if(title.getText().isEmpty() || artist.getText().isEmpty())
		{
			Alert noArtistOrTitle = new Alert(AlertType.ERROR);
			noArtistOrTitle.setHeaderText("Input not valid");
			noArtistOrTitle.setContentText("A song requrires both an artist and title.");
			noArtistOrTitle.showAndWait();
			return;
		}
		
		try {
			Integer.parseInt(year.getText());
		}
		catch(Exception e){
			// If year is not null, tell user to enter proper format
			if(!year.getText().isEmpty()){
				Alert yearFormat = new Alert(AlertType.ERROR);
				yearFormat.setHeaderText("Format Error");
				yearFormat.setContentText("The year must be a number.");
				yearFormat.showAndWait();
				return;
			}
		}
		
		if(album.getText().isEmpty()) album.setText("    ");
		if(year.getText().isEmpty()) year.setText("    ");
		
			
		
		Song newSong = new Song(title.getText(),artist.getText(),album.getText(),year.getText());
		if(!observableList.contains(newSong)) {
			observableList.add(newSong);
			observableList.sort(new SortSong());
			
			// Edit song must be set to false prior to deleteSong()
			if(editSong == true) {
				editSong = false;
				editDialogue = true;
				deleteSong();
			}
		}
		else {
			// Duplicate song should not be added
			Alert duplicateSong = new Alert(AlertType.ERROR);
			duplicateSong.setHeaderText("Duplicate Song");
			duplicateSong.setContentText("This song has already been added to the library.");
			duplicateSong.showAndWait();
			return;
			
		}
		
		
		FileWriter writer;
		try {
			writer = new FileWriter("songList.csv",true);
			writer.append(title.getText());
			writer.append(",");
			writer.append(artist.getText());
			writer.append(",");
			writer.append(album.getText());
			writer.append(",");
			writer.append(year.getText());
			writer.append("\n");
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		songList.getSelectionModel().select(observableList.indexOf(newSong));
		title.clear();
		artist.clear();
		album.clear();
		year.clear();
		addUpdateSong.setText("Add Song");
	}
    
	public void loadList() {

		String line;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("songList.csv"));
			while ((line = reader.readLine()) != null) {
			    String[] data = line.split(",");
			    Song newSong = new Song(data[0],data[1],data[2],data[3]);
			    
			    observableList.add(newSong);
			}
			reader.close();
			
		} catch (IOException e) {
			// No songs have been added at this point
		}
		observableList.sort(new SortSong());
	}
	
	@FXML
	public void deleteSong() {
		
		
		// Prevent deletion while a song is being edited
		
		if(editSong)
		{
			Alert deleteWhileEditing = new Alert(AlertType.ERROR);
			deleteWhileEditing.setHeaderText("Can not delete while editing");
			deleteWhileEditing.setContentText("Complete edits of song info prior to deleting.");
			deleteWhileEditing.showAndWait();
			return;
		}	
		
		// Confirm with user if delete should happen, using pop up
		if(!editDialogue)
		{
			Alert confirmDelete = new Alert(AlertType.CONFIRMATION);
			confirmDelete.setTitle("Delete Confirmation");
			confirmDelete.setHeaderText("Are you sure you want to delete this song?");
			confirmDelete.setContentText("All song information will be lost. Continue?");
			confirmDelete.showAndWait();
							
			// Cancel Delete
			if(confirmDelete.getResult()==ButtonType.CANCEL) return;
		}
		
		
		
		Song currSong = songList.getSelectionModel().getSelectedItem();
				
		// Remove song from CSV file
	    
		try {
			File temp = new File("temp.csv");
		    BufferedWriter bw;
			bw = new BufferedWriter(new FileWriter(temp));
			
			 String removeID = currSong.getFileFormat();
			    String currentLine;
			    
			    File f = new File("songList.csv");
			    BufferedReader br = new BufferedReader(new FileReader("songList.csv"));
			    
			    while((currentLine = br.readLine()) != null){
			        String trimmedLine = currentLine.trim();
			        if(!trimmedLine.equals(removeID)){
			        	 bw.write(currentLine + System.getProperty("line.separator"));
			        }
			       

			    }
			    bw.close();
			    br.close();
			    f.delete();
			    temp.renameTo(f);
	
			    // Delete song from observable list
			    int index = observableList.indexOf(currSong);
			    observableList.remove(currSong);
			    observableList.sort(new SortSong());
			    
				// Set Selected to next song, or previous if no next
				
			    songList.getSelectionModel().select(index);
			    editDialogue = false;
			    
			    
		} catch (IOException e) {
			e.printStackTrace();
		}
	   

	}
	
	
	@FXML
	private void cancelAddUpdate(ActionEvent event)
	{
		title.clear();
		artist.clear();
		album.clear();
		year.clear();
		addUpdateSong.setText("Add Song");
		editSong = false;
	}
	
	
}



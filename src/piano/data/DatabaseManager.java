package piano.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import piano.model.Song;
import piano.model.SongNote;
import java.sql.*;
import java.util.ArrayList;


public class DatabaseManager {
    /*
    in our database, we have two tables, one that stores songs' information and the other stores its notes
     */

    private static final String DB_URL = "jdbc:sqlite:songs.db";

    public Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL); //establish link with the database
    }

    public Song loadSongByTitle(String title) {
        /*
        Logic for this method is that we get the ID for the using its title from songs table
        then use that ID (which acts as foreign key for both tables) to get the notes of the song of our choice from song_notes table.
        Then we create an object that contains all the details (bpm, title, and notes).
         */
        try (Connection conn = connect()) { //using try catch so that we don't manually have to close the connection
            //getting song id and bpm from songs table
            String songQuery = "SELECT id, bpm FROM songs WHERE title = ?";
            PreparedStatement ps = conn.prepareStatement(songQuery);
            ps.setString(1, title);
            ResultSet rs = ps.executeQuery();

            //if song not found returns null
            if (!rs.next()) return null;

            int songId = rs.getInt("id"); //extract song id
            int bpm = rs.getInt("bpm"); //extract song bpm

            ArrayList<SongNote> noteList = new ArrayList<>();

            //using song id to get all notes from song_notes table to store in noteList
            String notesQuery = "SELECT note, time_ms FROM song_notes WHERE song_id = ? ORDER BY time_ms ASC";
            PreparedStatement ps2 = conn.prepareStatement(notesQuery);
            ps2.setInt(1, songId);
            ResultSet rs2 = ps2.executeQuery();

            //creating objects of SongNote from the result set
            while (rs2.next()) {
                String note = rs2.getString("note");
                int time = rs2.getInt("time_ms");
                noteList.add(new SongNote(note, time));
            }
            // convert the list to an array and create final song object
            SongNote[] notesArray = noteList.toArray(new SongNote[0]);
            return new Song(title, bpm, notesArray);

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String[] getAllSongTitles() {
        try (Connection conn = connect()) {

            //Getting the total number of songs to create an array of correct size
            Statement countStmt = conn.createStatement();
            ResultSet countRs = countStmt.executeQuery("SELECT COUNT(*) AS total FROM songs");
            int total = countRs.next() ? countRs.getInt("total") : 0;

            // Creating an array to hold all song titles
            String[] titles = new String[total];

            // get all song titles in alphabetical order
            String query = "SELECT title FROM songs ORDER BY title ASC";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            int i = 0;
            // Filling the array with the retrieved titles
            while (rs.next() && i < total) {
                titles[i++] = rs.getString("title");
            }
            //return the array
            return titles;
        } catch (SQLException e) {
            e.printStackTrace();
            //in case error occurs, print the error and return an empty string array
            return new String[0];
        }
    }


}

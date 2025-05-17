package piano.logic;

import piano.model.Song;
import piano.model.SongNote;
import piano.ui.NotePanel;

import javax.swing.*;
import java.util.Arrays;

public class GameEngine {

    /*
    Responsible for timed playback of song's notes.
    receives a Song object that contains the array of SongNote objects
    it starts a timer that 'ticks' every 20 ms which checks what notes should appear based on their timeMs and sends them to the NotePanel
    Once every note has been played it stops
     */

    private final NotePanel notePanel;
    private Timer timer;
    private long startTime;
    private Song currentSong;
    private SongNote[] notes;

    public GameEngine(NotePanel notePanel) {
        this.notePanel = notePanel;
    }

    public void play(Song song) {
        this.currentSong = song;
        //making a copy so that once the notes are played we can null them out
        this.notes = Arrays.copyOf(song.getNotes(), song.getNotes().length);
        this.startTime = System.currentTimeMillis();

        timer = new Timer(20, e -> tick());
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    // called every 20 ms by timer
    private void tick() {
        //i'll be honest, i'm not exactly sure how this works
        long timeNow = System.currentTimeMillis();
        long timeElapsed = timeNow - startTime;

        for (int i = 0; i < notes.length; i++) {
            SongNote note = notes[i];
            if (note != null && timeElapsed >= note.timeMs) {
                notePanel.addNote(note.note);
                notes[i] = null;
            }
        }

        boolean finished = Arrays.stream(notes).allMatch(n -> n == null);
        if (finished && timer != null) {
            timer.stop();
        }
    }
}


package piano.logic;

import piano.model.Song;
import piano.model.SongNote;
import piano.ui.NotePanel;

import javax.swing.*;
import java.util.Arrays;

public class GameEngine {

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
        this.notes = Arrays.copyOf(song.getNotes(), song.getNotes().length);
        this.startTime = System.currentTimeMillis();

        timer = new Timer(20, e -> tick());
        timer.start();
    }

    public void stop() {
        timer.stop();
    }

    private void tick() {
        long now = System.currentTimeMillis();
        long elapsed = now - startTime;

        for (int i = 0; i < notes.length; i++) {
            SongNote note = notes[i];
            if (note != null && elapsed >= note.timeMs) {
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


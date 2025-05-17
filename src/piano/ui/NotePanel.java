package piano.ui;

import piano.model.FallingNote;

import javax.swing.*;
import java.awt.*;

public class NotePanel extends JPanel {

    /*
    PANEL RESPONSIBLE FOR DISPLAYING FALLING NOTES
     */

    private static final int FALL_NOTE_WIDTH = 40;
    private static final int FALL_NOTE_HEIGHT = 30;
    private static final int MAX_NOTES = 12;
    private static final int FALL_SPEED = 5;
    private static final int PANEL_HEIGHT = 530;
    //defining deadzone window
    private static final int HIT_ZONE_TOP = 480; //in pixels
    private static final int HIT_ZONE_BOTTOM = 540; //in pixels

    private final FallingNote[] fallingNotes = new FallingNote[MAX_NOTES]; //maximum number of notes allowed to fall
    private int activeNotes = 0; // tracks notes that are being displayed currently
    private final PianoUI pianoUI;

    public NotePanel(PianoUI pianoUI) {
        this.pianoUI = pianoUI;
        gameStartTimer();
    }

    public void addNote(String note) {
        if (activeNotes < fallingNotes.length) {
            int x = pianoUI.getXForNote(note);
            fallingNotes[activeNotes] = new FallingNote(note, x, FALL_SPEED);
            activeNotes++;
        }
    }
    /*
    Responsible for updating the position of each falling note and removing the notes that have fallen below the panel (missed notes).
     */
    private void updateNotes() {
        for (int i = 0; i < activeNotes; i++) { //iterating only over the current falling notes (activeNotes) and not the entire array of fallingNotes.
            if (fallingNotes[i] != null) { //check if note exists at index i
                fallingNotes[i].updatePosition(); // make the note fall down
                if (fallingNotes[i].getY() > PANEL_HEIGHT) { //if the note's Y goes beyond the screen (missed), then it needs to be removed
                    removeNote(i);
                    i--; //since the note at index i got removed, we decrement i so that next loop iteration does not skip the note that got shifted to index i
                }

            }
        }
    }

    //responsible for drawing the falling notes
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); //to clear the background

        for (int i = 0; i < activeNotes; i++) {
            FallingNote note = fallingNotes[i];

            if (note != null) {
                g.setColor(Color.decode("#3A7AFE"));
                g.fillRect(note.getX(), note.getY(), FALL_NOTE_WIDTH, FALL_NOTE_HEIGHT); //makes a rect at note's xy coords with set dimensions.

            }
        }
    }

    private void gameStartTimer() {
        new javax.swing.Timer(20, e -> {
            updateNotes(); //updates y-positions of the notes
            repaint(); //calls paintComponent() to draw note
        }).start();
    }

    public void stopGame() {
        for (int i = 0; i < activeNotes; i++) { fallingNotes[i] = null; } //setting notes to null
        activeNotes = 0;
        repaint();
    }

    public void checkHit(String noteName) {
        boolean noteMatched = false;

        for (int i = 0; i < activeNotes; i++) {
            FallingNote note = fallingNotes[i];

            if (note != null && note.getNote().equals(noteName)) {
                noteMatched = true;
                int y = note.getY();

                if (y >= HIT_ZONE_TOP && y <= HIT_ZONE_BOTTOM) {
                    System.out.println("Hit: " + noteName);
                    pianoUI.addScore();
                    removeNote(i);
                    return;
                }
            }
        }

        if (!noteMatched) {
            pianoUI.deductScore();
            System.out.println("Miss: " + noteName);
        }
    }

    //implementing popping by replacing note at j with the next note (j + 1) by shifting notes left to avoid empty holes
    private void removeNote(int i) {
        for (int j = i; j < activeNotes - 1; j++) {
            fallingNotes[j] = fallingNotes[j + 1];
        }
        fallingNotes[activeNotes - 1] = null; // replacing the last note with null value
        activeNotes--; //one less note now so decrement the active notes' count
    }

}
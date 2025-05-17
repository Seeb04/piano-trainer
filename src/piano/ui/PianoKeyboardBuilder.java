package piano.ui;

import piano.sound.SoundManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class PianoKeyboardBuilder{

    /*
    CLASS RESPONSIBLE FOR BUILDING THE KEYBOARD AND ADDING ITS CORE FUNCTIONALITIES
     */

    private final PianoUI UI;

    //Defining each key's dimensions in px
    protected final int WHITE_KEY_WIDTH = 60;
    protected final int WHITE_KEY_HEIGHT = 250;
    protected final int BLACK_KEY_WIDTH = 40;
    protected final int BLACK_KEY_HEIGHT = 150;

    //all white notes that can be played
    private final String[] WHITE_NOTES = {
            "C4", "D4", "E4", "F4", "G4", "A4", "B4",
            "C5", "D5", "E5", "F5", "G5", "A5", "B5",
            "C6", "D6", "E6", "F6", "G6", "A6", "B6"
    };
    //all black notes that can be played
    private final String[] BLACK_NOTES = {
            "C#4", "D#4", "F#4", "G#4", "A#4",
            "C#5", "D#5", "F#5", "G#5", "A#5",
            "C#6", "D#6", "F#6", "G#6", "A#6"
    };

    // For white keys
    private final String[] WHITE_KEY_LABELS = {
            "Z", "X", "C", "V", "B", "N", "M",
            "Q", "W", "E", "R", "T", "Y", "U",
            "I", "O", "P", "[", "]", "\\", ""
    };
    // For black keys
    private final String[] BLACK_KEY_LABELS = {
            "S", "D", "G", "H", "J",
            "2", "3", "5", "6", "7",
            "9", "0", "=", "BKS", ""
    };
    //yes
    private final int[] BLACK_OFFSETS = {
            0, 1, 3, 4, 5,
            7, 8, 10, 11, 12,
            14, 15, 17, 18, 19
    };

    //All the possible keyboard inputs that are allowed
    private final int[] KEY_CODES = {
            KeyEvent.VK_Z, KeyEvent.VK_S, KeyEvent.VK_X, KeyEvent.VK_D, KeyEvent.VK_C,
            KeyEvent.VK_V, KeyEvent.VK_G, KeyEvent.VK_B, KeyEvent.VK_H, KeyEvent.VK_N,
            KeyEvent.VK_J, KeyEvent.VK_M, KeyEvent.VK_Q, KeyEvent.VK_2, KeyEvent.VK_W,
            KeyEvent.VK_3, KeyEvent.VK_E, KeyEvent.VK_R, KeyEvent.VK_5, KeyEvent.VK_T,
            KeyEvent.VK_6, KeyEvent.VK_Y, KeyEvent.VK_7, KeyEvent.VK_U, KeyEvent.VK_I,
            KeyEvent.VK_9, KeyEvent.VK_O, KeyEvent.VK_0, KeyEvent.VK_P,
            KeyEvent.VK_OPEN_BRACKET, KeyEvent.VK_EQUALS,
            KeyEvent.VK_CLOSE_BRACKET, KeyEvent.VK_BACK_SPACE, KeyEvent.VK_BACK_SLASH
    };

    //all possible notes that can be played
    private final String[] MAPPED_KEY_NOTES = {
            "C4", "C#4", "D4", "D#4", "E4", "F4", "F#4", "G4", "G#4", "A4", "A#4", "B4",
            "C5", "C#5", "D5", "D#5", "E5", "F5", "F#5", "G5", "G#5", "A5", "A#5", "B5",
            "C6", "C#6", "D6", "D#6", "E6", "F6", "F#6", "G6", "G#6", "A6"
    };

    //used for highlighting purposes
    private final JButton[] whiteKeyButtons = new JButton[21];
    private final JButton[] blackKeyButtons = new JButton[15];
    private JButton lastPressedKey = null;
    private String lastPressedNote = null;

    public PianoKeyboardBuilder(PianoUI UI) {
        this.UI = UI;
    }

    public void buildKeys() {
        setupWhiteKeys();
        setupBlackKeys();
    }

    private void styleKey(JButton key, Color bg, Color fg) {
        key.setBackground(bg);
        key.setForeground(fg);
        key.setOpaque(true);
        key.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        key.setFocusPainted(false);
        key.setContentAreaFilled(true);
    }

    //used for playing sound through event listener
    public String getNoteForKeyCode(int keyCode) {
        for (int i = 0; i < KEY_CODES.length; i++) {
            if (KEY_CODES[i] == keyCode) {
                return MAPPED_KEY_NOTES[i];
            }
        }
        return null;
    }


    private void setupWhiteKeys() {
        for (int i = 0; i < WHITE_NOTES.length; i++) {
            String note = WHITE_NOTES[i];
            int x = UI.startX + (i * WHITE_KEY_WIDTH);

            JButton key = new JButton(WHITE_KEY_LABELS[i]);

            key.setBounds(x, UI.startY, WHITE_KEY_WIDTH, WHITE_KEY_HEIGHT);
            styleKey(key, Color.WHITE, Color.BLACK);
            key.addActionListener(e -> {
                SoundManager.playNote(note);
                UI.requestFocusInWindow();
            });

            whiteKeyButtons[i] = key;
            UI.layeredPane.add(key, JLayeredPane.DEFAULT_LAYER);
        }
    }

    private void setupBlackKeys() {
        for (int i = 0; i < BLACK_NOTES.length; i++) {
            String note = BLACK_NOTES[i];
            int whiteIndex = BLACK_OFFSETS[i];
            int x = UI.startX + (whiteIndex * WHITE_KEY_WIDTH) + (WHITE_KEY_WIDTH - BLACK_KEY_WIDTH / 2);

            JButton key = new JButton(BLACK_KEY_LABELS[i]);

            key.setBounds(x, UI.startY, BLACK_KEY_WIDTH, BLACK_KEY_HEIGHT);
            styleKey(key, Color.decode("#2C2C2C"), Color.WHITE);
            key.addActionListener(e -> {
                SoundManager.playNote(note);
                UI.requestFocusInWindow();
            });

            blackKeyButtons[i] = key;
            UI.layeredPane.add(key, JLayeredPane.PALETTE_LAYER);
        }
    }

    public JButton findKeyButton(String note) {
        for (int i = 0; i < WHITE_NOTES.length; i++) {
            if (note.equals(WHITE_NOTES[i])) {
                return whiteKeyButtons[i];
            }
        }
        for (int i = 0; i < BLACK_NOTES.length; i++) {
            if (note.equals(BLACK_NOTES[i])) {
                return blackKeyButtons[i];
            }
        }
        return null;
    }

    public void highlightKey(String note) {
        JButton target = findKeyButton(note);
        if (target != null) {
            target.setBackground(Color.GRAY);
            lastPressedKey = target;
            lastPressedNote = note;
        }
    }

    public void reset() {
        if (lastPressedKey != null && lastPressedNote != null) {
            lastPressedKey.setBackground(lastPressedNote.contains("#") ? Color.BLACK : Color.WHITE);
            lastPressedKey = null;
            lastPressedNote = null;
        }
    }

}

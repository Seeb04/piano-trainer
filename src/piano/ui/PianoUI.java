package piano.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import piano.sound.SoundManager;
import piano.logic.GameEngine;
import piano.model.Song;
import piano.data.DatabaseManager;

public class PianoUI extends JFrame implements KeyListener {

    protected int OLD_WHITE_KEY_WIDTH = 60;
    /*^^^^^
    SAME FUNCTIONALITY AS THE ONE FOUND IN PianoKeyboardBuilder
    Could not shift this to PianoKeyboardBuilder as everything in that java file was first implemented in this file but
    the code got too messy to deal with, so I created another java file that was responsible for building the keyboard and its core functionalities.
    However, the crucial method getXForNote(note) relies heavily on this constant and this method is called in many other classes through this class' object
    so shifting this constant would be too much of a hassle, so this constant stays here.
     */

    protected int startX;
    protected int startY;

    protected JLayeredPane layeredPane;
    protected NotePanel notePanel;
    protected JPanel scorePanel;


    private final GameEngine GAME_ENGINE;
    private JComboBox<String> songSelectorMenu;

    private final int SCORE_INCREMENT = 10;
    private int currentScore = 0;
    private JLabel scoreLabel;

    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();
    /*
    Initial plan was to make the app have responsive sizing, so to implement that I messed around with different methods manipulate the screensize
    but at the end I couldn't manage to properly implement it. How this works, I don't know.
     */

    PianoKeyboardBuilder keyboardBuilder;

    public PianoUI() {
        layeredPane = new JLayeredPane();
        setupWindow();
        layeredPane.setBounds(0, 0, screenSize.width, screenSize.height);
        startX = screenSize.width / 16;
        startY = screenSize.height / 2;
        setupNotePanel();
        keyboardBuilder = new PianoKeyboardBuilder(this);
        keyboardBuilder.buildKeys();
        setupNoteRangeOverlay();
        GAME_ENGINE = new GameEngine(notePanel);
        setupControlButtons();
        setupScorePanel();

        add(layeredPane);
        setupKeyListener();
        setLayout(null);
        setVisible(true);
    }

    private void playNote(String note) {
        SoundManager.playNote(note);
        keyboardBuilder.highlightKey(note);
    }

    private void setupNoteRangeOverlay() {
        //Adds a dark, almost transparent overlay to indicate possible notes' range
        int numOfPlayableKeys = 21;
        int rangeWidth = OLD_WHITE_KEY_WIDTH * numOfPlayableKeys;

        JPanel overlayPanel = new JPanel();
        overlayPanel.setBounds(startX, 0, rangeWidth, startY);
        overlayPanel.setBackground(new Color(0, 0, 0, 80));
        overlayPanel.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 2));
        overlayPanel.setOpaque(true);

        layeredPane.add(overlayPanel, JLayeredPane.DEFAULT_LAYER);
    }

    //KEY LISTENERS
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        keyboardBuilder.reset();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        String note = keyboardBuilder.getNoteForKeyCode(e.getKeyCode());
        if (note != null) {
            playNote(note);
            notePanel.checkHit(note);
        }
    }


    protected void addScore() {
        currentScore += SCORE_INCREMENT;
        scoreLabel.setText("Score: " + currentScore);
        updateScoreColor();
    }

    protected void deductScore() {
        currentScore -= SCORE_INCREMENT;
        scoreLabel.setText("Score: " + currentScore);
        updateScoreColor();
    }

    protected void resetScore() {
        currentScore = 0;
        scoreLabel.setText("Score: " + currentScore);
        scoreLabel.setForeground(Color.decode("#DDDDDD"));
    }

    protected void updateScoreColor() {
        if (currentScore >= 100 && currentScore < 200) {
            scoreLabel.setForeground(Color.YELLOW);
        } else if (currentScore >= 200) {
            scoreLabel.setForeground(Color.GREEN);
        } else if (currentScore < 0) {
            scoreLabel.setForeground(Color.RED);
        }
    }

    public int getXForNote(String note) {
        int blackKeyOffset = OLD_WHITE_KEY_WIDTH - 20;

        return switch (note) {
            // Octave 4
            case "C4"  -> startX + 0  * OLD_WHITE_KEY_WIDTH;
            case "C#4" -> startX + 0  * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "D4"  -> startX + 1  * OLD_WHITE_KEY_WIDTH;
            case "D#4" -> startX + 1  * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "E4"  -> startX + 2  * OLD_WHITE_KEY_WIDTH;
            case "F4"  -> startX + 3  * OLD_WHITE_KEY_WIDTH;
            case "F#4" -> startX + 3  * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "G4"  -> startX + 4  * OLD_WHITE_KEY_WIDTH;
            case "G#4" -> startX + 4  * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "A4"  -> startX + 5  * OLD_WHITE_KEY_WIDTH;
            case "A#4" -> startX + 5  * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "B4"  -> startX + 6  * OLD_WHITE_KEY_WIDTH;

            // Octave 5
            case "C5"  -> startX + 7  * OLD_WHITE_KEY_WIDTH;
            case "C#5" -> startX + 7  * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "D5"  -> startX + 8  * OLD_WHITE_KEY_WIDTH;
            case "D#5" -> startX + 8  * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "E5"  -> startX + 9  * OLD_WHITE_KEY_WIDTH;
            case "F5"  -> startX + 10 * OLD_WHITE_KEY_WIDTH;
            case "F#5" -> startX + 10 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "G5"  -> startX + 11 * OLD_WHITE_KEY_WIDTH;
            case "G#5" -> startX + 11 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "A5"  -> startX + 12 * OLD_WHITE_KEY_WIDTH;
            case "A#5" -> startX + 12 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "B5"  -> startX + 13 * OLD_WHITE_KEY_WIDTH;

            // Octave 6
            case "C6"  -> startX + 14 * OLD_WHITE_KEY_WIDTH;
            case "C#6" -> startX + 14 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "D6"  -> startX + 15 * OLD_WHITE_KEY_WIDTH;
            case "D#6" -> startX + 15 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "E6"  -> startX + 16 * OLD_WHITE_KEY_WIDTH;
            case "F6"  -> startX + 17 * OLD_WHITE_KEY_WIDTH;
            case "F#6" -> startX + 17 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "G6"  -> startX + 18 * OLD_WHITE_KEY_WIDTH;
            case "G#6" -> startX + 18 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "A6"  -> startX + 19 * OLD_WHITE_KEY_WIDTH;
            case "A#6" -> startX + 19 * OLD_WHITE_KEY_WIDTH + blackKeyOffset;
            case "B6"  -> startX + 20 * OLD_WHITE_KEY_WIDTH;

            default    -> startX;
        };
    }

    // SETUP
    private void setupWindow() {
        setTitle("Piano Trainer - OOP Project – Spring 2025");

        JLabel title = new JLabel("Haseeb Haroon 20241-35751");

        title.setFont(new Font("Segoe UI", Font.BOLD, 22));
        title.setForeground(Color.decode("#DDDDDD"));
        title.setBounds(20, 790, 500, 30);

        this.add(title, BorderLayout.SOUTH);

        // Responsive to physical screen size
        int width = (int)(screenSize.width * 0.8);
        int height = (int)(screenSize.height * 0.8);

        this.setSize(width, height);
        getContentPane().setBackground(Color.decode("#293133"));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
    }

    private void setupNotePanel() {
        notePanel = new NotePanel(this);
        notePanel.setBounds(0, 0, screenSize.width, screenSize.height);
        notePanel.setOpaque(false);

        JPanel noteBackground = new JPanel();
        noteBackground.setBounds(0, 0, screenSize.width, screenSize.height);
        noteBackground.setBackground(new Color(220, 220, 220)); // light gray or styled color
        noteBackground.setOpaque(true);

        add(notePanel);

    }

    private void setupScorePanel() {
        scorePanel = new JPanel();
        scorePanel.setBounds(0,0, 150, 50);
        scoreLabel = new JLabel("Score: " + currentScore);
        scoreLabel.setFont(new Font("Segoe UI", Font.BOLD, 17));
        scoreLabel.setForeground(Color.decode("#DDDDDD"));
        scoreLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        scorePanel.setOpaque(false);
        scorePanel.add(scoreLabel);

        scoreLabel.setVisible(false);
        scorePanel.setVisible(true);
        this.add(scorePanel);

    }

    private void setupControlButtons() {

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);
        buttonPanel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        this.add(buttonPanel, BorderLayout.NORTH);
        buttonPanel.setBounds(getWidth() - 150, 10, 140, 200);

        Font buttonFont = new Font("Segoe UI", Font.PLAIN, 14);

        Color buttonColor = new Color(60, 60, 60);
        Color textColor = Color.WHITE;

        DatabaseManager db = new DatabaseManager();
        String[] songTitles = db.getAllSongTitles();

        // drop down menu for stored songs in db
        songSelectorMenu = new JComboBox<>(songTitles);

        songSelectorMenu.setBounds(650, 360, 120, 30);
        songSelectorMenu.setFont(buttonFont);
        songSelectorMenu.setBackground(buttonColor);
        songSelectorMenu.setForeground(textColor);
        songSelectorMenu.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        songSelectorMenu.setMaximumSize(new Dimension(120, 30));

        buttonPanel.add(songSelectorMenu);

        //setting up start button
        JButton startButton = new JButton("Start Game");

        startButton.setFont(buttonFont);
        startButton.setBounds(650, 320 ,120, 30);
        startButton.setBackground(buttonColor);
        startButton.setForeground(textColor);

        //something happens here
        startButton.addActionListener(e -> {
            String selectedTitle = (String) songSelectorMenu.getSelectedItem();
            DatabaseManager dbSelect = new DatabaseManager();
            Song loadedSong = dbSelect.loadSongByTitle(selectedTitle);
            if (!scoreLabel.isVisible()) {
                resetScore();
                scoreLabel.setVisible(true);
            }

            if (loadedSong != null) {
                GAME_ENGINE.play(loadedSong);
                startButton.setEnabled(false);
            } else {
                System.out.println("Song not found: " + selectedTitle);
            }
            requestFocusInWindow();
        });
        startButton.setMaximumSize(new Dimension(120, 30));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        buttonPanel.add(startButton);

        //setting up stop button
        JButton stopButton = new JButton("Stop Game");

        stopButton.setFont(buttonFont);
        stopButton.setBackground(buttonColor);
        stopButton.setForeground(textColor);
        stopButton.setBounds(650, 280, 120, 30);

        stopButton.addActionListener(e -> {
            notePanel.stopGame();
            GAME_ENGINE.stop();
            resetScore();
            scoreLabel.setVisible(false);
            startButton.setEnabled(true); //enabled to allow restarting
            requestFocusInWindow(); //so the keyboard inputs are recognized
        });
        stopButton.setMaximumSize(new Dimension(120, 30));
        stopButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        buttonPanel.add(stopButton);

        //setting up exit button
        JButton exitButton = new JButton("Exit Game");

        exitButton.setFont(buttonFont);
        exitButton.setBackground(buttonColor);
        exitButton.setForeground(textColor);
        exitButton.setBounds(650, 240, 120, 30);
        exitButton.addActionListener( e -> {
            System.exit(0);
        });

        exitButton.setMaximumSize(new Dimension(120, 30));
        exitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        buttonPanel.add(exitButton);
    }

    private void setupKeyListener() {
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
    }

}
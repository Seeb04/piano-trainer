package piano.model;

public class FallingNote {
    /*
    Helper class for NotePanel. Objects store necessary information for displaying/removing notes
     */
    private final String note;
    private final int x;
    private int y;
    private final int speed;

    public FallingNote(String note, int x, int speed) {
        this.note = note;
        this.x = x;
        this.y = 0;
        this.speed = speed;
    }

    public void updatePosition() {
        y += speed;
    }
    public String getNote() {
        return note;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}

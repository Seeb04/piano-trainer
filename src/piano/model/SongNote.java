package piano.model;

public class SongNote {

    /*
    Helper class for Song. Multiple instances of SongNote are created to store in Song
     */
    public String note;     // e.g. "C", "D#", etc.
    public int timeMs;      // When to play (in milliseconds from start)

    public SongNote(String note, int timeMs) {
        this.note = note;
        this.timeMs = timeMs;
    }
}

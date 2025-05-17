package piano.model;

public class Song {
    private final String title;
    private final int bpm;
    private final SongNote[] notes;

    public Song(String title, int bpm, SongNote[] notes) {
        this.title = title;
        this.bpm = bpm;
        this.notes = notes;
    }

    public String getTitle() { return title; }
    public int getBpm() { return bpm; }
    public SongNote[] getNotes() { return notes; }

}
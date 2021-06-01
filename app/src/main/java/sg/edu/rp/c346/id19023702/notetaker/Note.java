package sg.edu.rp.c346.id19023702.notetaker;

import java.util.Date;

public class Note {

    private long key;
    private String title;
    private String description;
    private String time;

    public Note(long key, String title, String description, String time) {
        this.key = key;
        this.title = title;
        this.description = description;
        this.time = time;
    }

    public long getKey() {
        return key;
    }

    public void setKey(long key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }
}
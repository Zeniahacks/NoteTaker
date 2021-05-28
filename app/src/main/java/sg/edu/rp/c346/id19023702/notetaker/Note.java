package sg.edu.rp.c346.id19023702.notetaker;

public class Note {

    private long key;
    private String title;
    private String description;

    public Note(long key, String title, String description) {
        this.key = key;
        this.title = title;
        this.description = description;
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
}
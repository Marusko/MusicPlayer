import javafx.util.Duration;

public class Song {
    private final String name;
    private String playlist;
    private final Duration length;
    private final String path;
    private final String author;
    private final String year;
    private boolean checked = false;

    public Song(String name, String playlist, Duration length, String path, String author, String year) {
        this.name = name;
        this.playlist = playlist;
        this.length = length;
        this.path = path;
        this.author = author;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Duration getLength() {
        return this.length;
    }

    public String getYear() {
        return year;
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public String getPath() {
        return path;
    }

    public boolean getChecked() {
        return this.checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

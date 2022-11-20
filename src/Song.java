import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Song {
    private String name;
    private String playlist;
    private Duration length;
    private String path;
    private String author;
    private boolean checked = false;
    private Media m;
    private MediaPlayer mp;

    public Song(String path, Media m, String playlist) {
        this.path = path;
        this.m = m;
        this.playlist = playlist;
    }

    public void setUp(MainWindow mw) {
        mp = new MediaPlayer(m);
        mp.setOnReady(() -> {
            name = (String) m.getMetadata().get("title");
            length = (Duration) m.getMetadata().get("duration");
            author = (String) m.getMetadata().get("artist");
            mw.refresh();
        });
    }
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public double getLength() {
        return this.length.toMinutes();
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

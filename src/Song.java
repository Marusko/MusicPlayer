import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Song {
    private String name;
    private String playlist;
    private Duration length;
    private final String path;
    private String author;
    private boolean checked = false;
    private final Media m;

    public Song(String path, Media m, String playlist) {
        this.path = path;
        this.m = m;
        this.playlist = playlist;
    }

    public void setUp(MainWindow mw) {
        MediaPlayer mp = new MediaPlayer(m);
        mp.setOnReady(() -> {
            name = (String) m.getMetadata().get("title");
            length = m.getDuration();
            author = (String) m.getMetadata().get("artist");
            if (name == null) {
                String source = m.getSource();
                int slashIndex = source.lastIndexOf("/");
                int dotIndex = source.lastIndexOf(".");
                String fileName = source.substring(slashIndex + 1, dotIndex);
                name = fileName.replace("_", " ").replaceAll("%20", " ").replaceAll("%5B", "[").replaceAll("%5D", "]");
            }
            mw.refresh();
        });
    }
    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getLength() {
        String length ="";
        if (this.length != null) {
            double min = Math.floor(this.length.toMinutes());
            double sec = ((this.length.toMinutes()) - min) * 60;
            length = String.format("%1$.0f:%2$02.0f", min, sec);
        }
        return length;
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

//TODO dispose of initializing media player

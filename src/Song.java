import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Song {
    private String name;
    private Duration length;
    private final String path;
    private String author;
    private final Media m;

    public Song(String path, Media m) {
        this.path = path;
        this.m = m;
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
            mp.dispose();
        });
    }
    public String getName() {
        return name;
    }
    public String getAuthor() {
        return author;
    }
    public String getLength() {
        String length = "";
        if (this.length != null) {
            double min = Math.floor(this.length.toMinutes());
            double sec = ((this.length.toMinutes()) - min) * 60;
            length = String.format("%1$.0f:%2$02.0f", min, sec);
        }
        return length;
    }
    public Duration getDuration() {
        return this.length;
    }
    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return this.name;
    }
}

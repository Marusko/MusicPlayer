import java.util.LinkedList;

public class MainLogic {
    private final LinkedList<Song> songQueue;
    private Song actualSong = null;
    private final MainWindow mw;

    public MainLogic(MainWindow mw) {
        this.songQueue = new LinkedList<>();
        this.mw = mw;
    }

}

import java.util.ArrayList;
import java.util.LinkedList;

public class MainLogic {
    private final LinkedList<Song> songQueue;
    private final ArrayList<Song> allSongs;
    private final ArrayList<Playlist> allPlaylists;
    private Song actualSong = null;
    private final MainWindow mw;

    public MainLogic(MainWindow mw) {
        this.songQueue = new LinkedList<>();
        this.allSongs = new ArrayList<>();
        this.allPlaylists = new ArrayList<>();
        this.mw = mw;
    }

    public void createPlaylist(String name) {
        Playlist p = new Playlist(name, null);
        this.allPlaylists.add(p);
    }
    public ArrayList<Playlist> getAllPlaylists() {
        return this.allPlaylists;
    }

    public Playlist getPlaylist(String name) {
        Playlist playlist = null;
        for (Playlist p : this.allPlaylists) {
            if (p.getName().equals(name)) {
                playlist = p;
            }
        }
        return playlist;
    }
}

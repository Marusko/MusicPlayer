import javafx.scene.media.Media;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainLogic {
    private final LinkedList<Song> songQueue;
    private final ArrayList<Song> allSongs;
    private final ArrayList<Playlist> allPlaylists;
    private final ArrayList<Song> selectedSongs;

    private Song actualSong = null;
    private final MainWindow mw;

    public MainLogic(MainWindow mw) {
        this.songQueue = new LinkedList<>();
        this.allSongs = new ArrayList<>();
        this.allPlaylists = new ArrayList<>();
        this.selectedSongs = new ArrayList<>();
        this.mw = mw;
    }

    public void selectSong(Song s) {
        this.selectedSongs.add(s);
    }
    public ArrayList<Song> getSelectedSongs() {
        return this.selectedSongs;
    }

    public ArrayList<Song> getAllSongs() {
        return this.allSongs;
    }

    public void addSong(Stage stage) {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(stage);
        String path = f.getPath();
        String newPath = path.replace('\\', '/');
        path = newPath.replace('C', 'e');
        path = "fil" + path;
        Media m = new Media(path);
        Song song = new Song(f.getName(), null, m.getDuration(), f.getAbsolutePath(), "Author", "year");
        this.allSongs.add(song);
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

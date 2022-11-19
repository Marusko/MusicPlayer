import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainLogic {
    private MediaPlayer mp;
    private final LinkedList<Song> songQueue;
    private final ArrayList<Song> allSongs;
    private final ArrayList<Playlist> allPlaylists;
    private final ArrayList<Song> selectedSongs;

    private boolean pla = true;
    private boolean rep = false;
    private boolean shuf = false;

    private Song actualSong = null;
    private final MainWindow mw;

    public MainLogic(MainWindow mw) {
        this.songQueue = new LinkedList<>();
        this.allSongs = new ArrayList<>();
        this.allPlaylists = new ArrayList<>();
        this.selectedSongs = new ArrayList<>();
        this.mw = mw;
    }

    public void playSong(Song s) {
        this.actualSong = s;
        Media m = new Media(this.actualSong.getPath());
        mp = new MediaPlayer(m);
        mp.play();
    }

    public void playPauseSong() {
        if (this.pla) {
            this.pla = false;
            this.mp.pause();
        } else {
            this.pla = true;
            this.mp.play();
        }
    }
    public void changeVolume(double v) {
        mp.setVolume(v);
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
        String path = "" + f.toURI();
        Media m = new Media(path);
        Song song = new Song(f.getName(), null, m.getDuration(), path, "Author", "year");
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

    public boolean isPla() {
        return pla;
    }

    public boolean isRep() {
        return rep;
    }

    public boolean isShuf() {
        return shuf;
    }

    public void setPla(boolean pla) {
        this.pla = pla;
    }

    public void setRep(boolean rep) {
        this.rep = rep;
    }

    public void setShuf(boolean shuf) {
        this.shuf = shuf;
    }
}

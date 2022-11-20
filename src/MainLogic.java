import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class MainLogic {
    private MediaPlayer mp;
    private final LinkedList<Song> songQueue;
    private final ArrayList<Song> allSongs;
    private final ArrayList<Playlist> allPlaylists;
    private final ArrayList<Song> selectedSongs;

    private double volume = 0.3;
    private boolean pla = false;
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
        try {
            this.loadSongs();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public MediaPlayer getMp() {
        return mp;
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

    public ArrayList<Song> getAllSongs() {
        return this.allSongs;
    }

    public void playSong(Song s) {
        if (this.pla || this.mp != null) {
            this.mp.stop();
        }

        this.pla = true;
        this.actualSong = s;
        Media m = new Media(this.actualSong.getPath());
        mp = new MediaPlayer(m);
        this.mw.setSongName(s.getName());
        this.mp.setVolume(this.volume);
        this.mp.setOnReady(() -> mw.setSongSliderLength(mp.getTotalDuration().toSeconds()));
        mp.play();
        mp.setOnEndOfMedia(this::playNext);
        this.mw.setPlayButtonImage(!this.pla);
    }

    public void playNext() {
        if (this.songQueue.contains(this.actualSong)) {
            int index = this.songQueue.indexOf(this.actualSong);
            index++;
            if (index < this.songQueue.size()) {
                Song next = this.songQueue.get(index);
                this.playSong(next);
                this.actualSong = next;
            } else if (!this.rep){
                Song next = this.songQueue.getFirst();
                this.playSong(next);
                this.mp.pause();
                this.pla = false;
                this.mw.setPlayButtonImage(true);
            } else if (this.rep){
                Song next = this.songQueue.getFirst();
                this.playSong(next);
                this.mw.setPlayButtonImage(false);
            }
        }
    }
    public void playPrev() {
        if (this.songQueue.contains(this.actualSong)) {
            int index = this.songQueue.indexOf(this.actualSong);
            index--;
            if (index > -1) {
                Song prev = this.songQueue.get(index);
                this.playSong(prev);
                this.actualSong = prev;
            }
        }
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

    public void addSong(Stage stage) throws Exception {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(stage);
        String path = "" + f.toURI();
        Media m = new Media(path);
        Song song = new Song(path, m, null);
        song.setUp(this.mw);
        this.allSongs.add(song);
        this.songQueue.add(song);
        //File allSongsFile = new File(Objects.requireNonNull(getClass().getResource("data/allSongs.txt")).toURI());
        File allSongsFile = new File("all.txt"); //Test
        BufferedReader br = new BufferedReader(new FileReader(allSongsFile));
        StringBuilder lines = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            lines.append(line);
            lines.append("\n");
        }
        br.close();
        lines.append(path);
        PrintWriter pw = new PrintWriter(allSongsFile);
        pw.println(lines);
        pw.flush();
        pw.close();
        this.mw.refresh();
    }

    private void loadSongs() throws Exception {
        File allSongsFile = new File("all.txt"); //Test
        BufferedReader br = new BufferedReader(new FileReader(allSongsFile));
        String line;
        while ((line = br.readLine()) != null) {
            Media m = new Media(line);
            Song song = new Song(line, m, null);
            song.setUp(this.mw);
            this.allSongs.add(song);
            this.songQueue.add(song);
        }
        br.close();
    }

    public void changeVolume(double v) {
        this.volume = v;
        mp.setVolume(v);
    }


    public void selectSong(Song s) {
        this.selectedSongs.add(s);
    }
    public ArrayList<Song> getSelectedSongs() {
        return this.selectedSongs;
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

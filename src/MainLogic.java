import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MainLogic {
    public final static int REPEAT_OFF = 0;
    public final static int REPEAT_ALL = 1;
    public final static int REPEAT_ONE = 2;
    private MediaPlayer mp;
    private LinkedList<Song> songQueue;
    private LinkedList<Song> backUpQueue;
    private final ArrayList<Song> allSongs;
    private final ArrayList<Playlist> allPlaylists;
    private final ArrayList<Song> selectedSongs;

    private double volume = 0.3;
    private boolean pla = false;
    private int rep = MainLogic.REPEAT_OFF;
    private boolean shuf = false;
    private boolean updatingStateOfSong = true;
    private Song actualSong = null;
    private final MainWindow mw;

    public MainLogic(MainWindow mw) {
        this.songQueue = new LinkedList<>();
        this.backUpQueue = new LinkedList<>();
        this.allSongs = new ArrayList<>();
        this.allPlaylists = new ArrayList<>();
        this.selectedSongs = new ArrayList<>();
        this.mw = mw;
        try {
            this.loadSongs();
            this.loadPlaylists();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.backUpQueue = this.songQueue;
    }

    public void setRep() {
        this.rep++;
        this.rep = this.rep > 2 ? 0 : this.rep;
    }
    public void setUpdatingStateOfSong(boolean updatingStateOfSong) {
        this.updatingStateOfSong = updatingStateOfSong;
    }

    public boolean isPla() {
        return pla;
    }
    public int isRep() {
        return rep;
    }
    public boolean isShuf() {
        return shuf;
    }
    public Song getActualSong() {
        return this.actualSong;
    }
    public MediaPlayer getMp() {
        return mp;
    }
    public boolean isUpdatingStateOfSong() {
        return this.updatingStateOfSong;
    }
    public ArrayList<Song> getAllSongs() {
        return this.allSongs;
    }

    public void playSong(Song s) {
        if (this.pla || this.mp != null) {
            this.mp.stop();
            this.mp.dispose();
        }

        this.pla = true;
        this.actualSong = s;
        Media m = new Media(this.actualSong.getPath());
        mp = new MediaPlayer(m);
        this.mw.setSongName();
        this.mw.setSongLength();
        this.mp.setVolume(this.volume);
        this.mp.setOnReady(() -> mw.setSongSliderLength(mp));
        this.mp.currentTimeProperty().addListener(e -> {
            if (updatingStateOfSong) {
                mw.setActualSongTime(mp);
                mw.setSongSlider(mp);
            }
        });
        mp.play();
        mp.setOnEndOfMedia(this::playNext);
        this.mw.setPlayButtonImage(!this.pla);
        this.mw.setTitle(s.getName());
    }
    public void playNext() {
        if (this.songQueue.contains(this.actualSong)) {
            this.mp.setCycleCount(1);
            int index = this.songQueue.indexOf(this.actualSong);
            index++;
            if (this.rep == MainLogic.REPEAT_ONE){
                this.mp.setCycleCount(MediaPlayer.INDEFINITE);
                Song next = this.actualSong;
                this.playSong(next);
                this.mw.setPlayButtonImage(false);
            } else if (index < this.songQueue.size() && this.rep == MainLogic.REPEAT_OFF || index < this.songQueue.size() && this.rep == MainLogic.REPEAT_ALL) {
                Song next = this.songQueue.get(index);
                this.playSong(next);
                this.actualSong = next;
            } else if (index == this.songQueue.size() && this.rep == MainLogic.REPEAT_OFF){
                Song next = this.songQueue.getFirst();
                this.playSong(next);
                this.mp.pause();
                this.pla = false;
                this.mw.setPlayButtonImage(true);
            } else if (index == this.songQueue.size() && this.rep == MainLogic.REPEAT_ALL){
                Song next = this.songQueue.getFirst();
                this.playSong(next);
                this.mw.setPlayButtonImage(false);
            }
        }
    }
    public void playPrev() {
        if (this.songQueue.contains(this.actualSong)) {
            double seconds = this.mp.getCurrentTime().toSeconds();
            if (seconds < 11) {
                int index = this.songQueue.indexOf(this.actualSong);
                index--;
                if (index > -1) {
                    Song prev = this.songQueue.get(index);
                    this.playSong(prev);
                    this.actualSong = prev;
                } else if (index == -1 && this.rep == MainLogic.REPEAT_ALL) {
                    Song prev = this.songQueue.getLast();
                    this.playSong(prev);
                    this.actualSong = prev;
                    this.mw.setPlayButtonImage(false);
                }
            } else {
                this.mp.seek(this.mp.getStartTime());
                this.mw.setPlayButtonImage(false);
                this.pla = true;
                this.mp.play();
            }
        }
    }
    public void playPauseSong() {
        if (this.pla) {
            this.pla = false;
            this.mp.pause();
            this.mw.setTitle(null);
        } else {
            this.pla = true;
            this.mp.play();
            this.mw.setTitle(this.actualSong.getName());
        }
    }

    public void setAndShuffle() {
        if (this.shuf) {
            this.shuf = false;
            this.songQueue = this.backUpQueue;
        } else {
            this.shuf = true;
            shuffle();
        }
    }
    private void shuffle() {
        int[] tmp = new int[this.songQueue.size()];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = i;
        }
        Random r = new Random();
        for (int i = 0; i < tmp.length; i++) {
            int n = r.nextInt(0, tmp.length);
            int x = tmp[n];
            tmp[n] = tmp[i];
            tmp[i] = x;
        }
        LinkedList<Song> shuffledQueue = new LinkedList<>();
        shuffledQueue.add(this.actualSong);
        for (int index : tmp) {
            if (index != this.songQueue.indexOf(this.actualSong)) {
                shuffledQueue.add(this.songQueue.get(index));
            }
        }
        this.songQueue = shuffledQueue;
    }

    public void setPlaylistSongs(Playlist playlist) {
        if (playlist != null) {
            this.songQueue = new LinkedList<>(playlist.getSongs());
        } else {
            this.songQueue.clear();
            this.songQueue.addAll(this.allSongs);
        }
        this.backUpQueue = this.songQueue;
        this.shuf = false;
        this.mw.resetShuffleColor();
    }

    public void addSong(Stage stage) throws Exception {
        FileChooser fc = new FileChooser();
        File f = fc.showOpenDialog(stage);
        String path = "" + f.toURI();
        Media m = new Media(path);
        Song song = new Song(path, m);
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
            Song song = new Song(line, m);
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
    public void deSelectSong(Song s) {
        this.selectedSongs.remove(s);
    }
    public ArrayList<Song> getSelectedSongs() {
        return this.selectedSongs;
    }

    public void createPlaylist(String name) {
        Playlist p = new Playlist(name);
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
    public void deletePlaylist(Playlist p) {
        p.deleteFile();
        this.allPlaylists.remove(p);
        this.savePlaylists();
    }
    public void savePlaylists() {
        for (Playlist p : this.allPlaylists) {
            try {
                p.savePlaylist();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
    public void loadPlaylists() throws Exception {
        File folder = new File("playlists/");
        File[] playlists = folder.listFiles();
        if (playlists != null) {
            for (File playlist : playlists) {
                int index = playlist.getName().lastIndexOf(".");
                String name = playlist.getName().substring(0, index);
                this.createPlaylist(name);
                BufferedReader br;
                for (Song s : this.allSongs) {
                    br = new BufferedReader(new FileReader(playlist));
                    String line;
                    while ((line = br.readLine()) != null) {
                        if (s.getPath().equals(line.trim())) {
                            this.getPlaylist(name).addSong(s);
                        }
                    }
                    br.close();
                }
            }
        }
    }
}

//TODO when song is not in the location then don't load it and remove it from all.txt
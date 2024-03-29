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
    private final ArrayList<String> songPaths;
    private final ArrayList<Playlist> allPlaylists;
    private final ArrayList<Song> selectedSongs;

    public static String PATH = "";
    private double volume = 0.3;
    private boolean pla = false;
    private int rep = MainLogic.REPEAT_OFF;
    private boolean shuf = false;
    private boolean updatingStateOfSong = true;
    private Song actualSong = null;
    private final MainWindow mw;
    private String theme = "";
    private boolean firstUseBool;
    private Playlist selectedPlaylist = null;

    public MainLogic(MainWindow mw) {
        this.songQueue = new LinkedList<>();
        this.backUpQueue = new LinkedList<>();
        this.allSongs = new ArrayList<>();
        this.songPaths = new ArrayList<>();
        this.allPlaylists = new ArrayList<>();
        this.selectedSongs = new ArrayList<>();
        this.mw = mw;
    }

    public void setFirstUseBool(boolean firstUseBool) {
        this.firstUseBool = firstUseBool;
    }

    public boolean isFirstUseBool() {
        return firstUseBool;
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
    public ArrayList<String> getSongPaths() {
        return this.songPaths;
    }
    public LinkedList<Song> getSongQueue() {
        return this.songQueue;
    }
    public void equalsQueue() {
        this.backUpQueue = this.songQueue;
    }
    public void setSelectedPlaylist(Playlist p) {
        this.selectedPlaylist = p;
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

    public void setPlaylistSongs() {
        if (this.selectedPlaylist != null) {
            this.songQueue = new LinkedList<>(this.selectedPlaylist.getSongs());
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
        song.setUp();
        this.allSongs.add(song);
        this.songQueue.add(song);
        File allSongsFile = new File(PATH + "/all.txt");
        BufferedReader br = new BufferedReader(new FileReader(allSongsFile));
        String line = br.readLine();
        br.close();
        line = line + path + ";";
        PrintWriter pw = new PrintWriter(allSongsFile);
        pw.print(line);
        pw.flush();
        pw.close();
        this.mw.refresh(true);
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
        try {
            p.deleteFile();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    private String loadAndSplitConfig() throws Exception {
        File config = new File("config.cf");
        BufferedReader br = new BufferedReader(new FileReader(config));
        StringBuilder lines = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            lines.append(line);
        }
        br.close();

        return lines.toString();
    }
    public void setPath(String p) throws Exception {
        PATH = p;

        File config = new File("config.cf");
        String lines = this.loadAndSplitConfig();
        String[] sepLines = lines.split(";");
        for (String s : sepLines) {
            String[] parts = s.split("=");
            String name = parts[0].replace("[", "").replaceAll("]", "");
            if (name.equals("Path")) {
                lines = lines.replace(parts[1], PATH);
                BufferedWriter w = new BufferedWriter(new FileWriter(config));
                w.write(lines);
                w.close();
            }
        }
    }
    public void setTheme(String t, boolean b) throws Exception {
        this.theme = t;
        if (b) {
            File config = new File("config.cf");
            String lines = this.loadAndSplitConfig();
            String[] sepLines = lines.split(";");
            for (String s : sepLines) {
                String[] parts = s.split("=");
                String name = parts[0].replace("[", "").replaceAll("]", "");
                if (name.equals("Style")) {
                    lines = lines.replaceAll(parts[1], t);
                    BufferedWriter w = new BufferedWriter(new FileWriter(config));
                    w.write(lines);
                    w.close();
                }
            }
        }
    }
    public String getTheme() {
        return theme;
    }
}
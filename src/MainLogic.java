import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.FileSystemException;
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

    public MainLogic(MainWindow mw) {
        this.songQueue = new LinkedList<>();
        this.backUpQueue = new LinkedList<>();
        this.allSongs = new ArrayList<>();
        this.songPaths = new ArrayList<>();
        this.allPlaylists = new ArrayList<>();
        this.selectedSongs = new ArrayList<>();
        this.mw = mw;
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
        File allSongsFile = new File(PATH + "/all.txt");
        BufferedReader br = new BufferedReader(new FileReader(allSongsFile));
        String line = br.readLine();
        br.close();
        line = line + path + ";";
        PrintWriter pw = new PrintWriter(allSongsFile);
        pw.print(line);
        pw.flush();
        pw.close();
        this.mw.refresh();
    }
    private void loadSongs() throws Exception {
        File allSongsFile = new File(PATH + "/all.txt");
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(allSongsFile));
        } catch (FileNotFoundException e) {
           boolean songs = allSongsFile.createNewFile();
           if (!songs) {
               throw new FileSystemException("Can't create file for songs");
           }
            br = new BufferedReader(new FileReader(allSongsFile));
        }
        String lines;
        lines = br.readLine();
        br.close();
        String[] sepLines = lines.split(";");
        for (String s : sepLines) {
            Media m;
            try {
                m = new Media(s);
                this.songPaths.add(s);
            } catch (MediaException e) {
                lines = lines.replace(s, "");
                continue;
            }
            Song song = new Song(s, m);
            song.setUp(this.mw);
            this.allSongs.add(song);
            this.songQueue.add(song);
        }
        lines = lines.replace(";;", ";");
        BufferedWriter w = new BufferedWriter(new FileWriter(allSongsFile));
        w.write(lines);
        w.close();
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
    public void loadPlaylists() throws Exception {
        File folder = new File(PATH + "/playlists/");

        if (folder.exists()) {
            File[] playlists = folder.listFiles();
            if (playlists != null) {
                for (File playlist : playlists) {
                    int index = playlist.getName().lastIndexOf(".");
                    String name = playlist.getName().substring(0, index);
                    this.createPlaylist(name);
                    BufferedReader br = new BufferedReader(new FileReader(playlist));
                    String lines = br.readLine();
                    br.close();
                    String[] sepLines = lines.split(";");

                    for (String s : sepLines) {
                        int songIndex = this.songPaths.indexOf(s);
                        if (songIndex != -1) {
                            this.getPlaylist(name).addSong(this.allSongs.get(songIndex));
                        } else {
                            lines = lines.replace(s, "");
                        }
                    }
                    lines = lines.replace(";;", ";");
                    BufferedWriter w = new BufferedWriter(new FileWriter(playlist));
                    w.write(lines);
                    w.close();
                }
            }
        } else {
            boolean dir = folder.mkdir();
            if (!dir) {
                throw new FileSystemException("Can't create directory");
            }
        }
        this.songPaths.clear();
    }

    public void load() {
        try {
            this.loadSongs();
            this.loadPlaylists();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.backUpQueue = this.songQueue;
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
    public void setTheme(String t) throws Exception {
        this.theme = t;
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
    public String getTheme() {
        return theme;
    }

    public boolean loadConfig() throws Exception {
        boolean first = true;
        File config = new File("config.cf");
        String lines = this.loadAndSplitConfig();
        String[] sepLines = lines.split(";");
        for (String s : sepLines) {
            String[] parts = s.split("=");
            String name = parts[0].replace("[", "").replaceAll("]", "");
            switch (name) {
                case "First" -> {
                    if (parts[1].equals("false")) {
                        first = false;
                    } else {
                        lines = lines.replaceAll("true", "false");
                        BufferedWriter w = new BufferedWriter(new FileWriter(config));
                        w.write(lines);
                        w.close();
                    }
                }
                case "Style" -> {
                    switch (parts[1]) {
                        case "Orange" -> {this.mw.setTheme("Orange"); this.theme = "Orange";}
                        case "Blue" -> {this.mw.setTheme("Blue"); this.theme = "Blue";}
                        case "Green" -> {this.mw.setTheme("Green"); this.theme = "Green";}
                    }
                }
                case "Path" -> {
                    if (!parts[1].equals("null")) {
                        PATH = parts[1];
                    }
                }
            }
        }
        return first;
    }
}
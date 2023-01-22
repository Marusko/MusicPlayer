import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;

import java.io.*;
import java.nio.file.FileSystemException;

public class Loader extends Thread {
    private MainWindow mw;
    private MainLogic ml;

    public void setup(MainWindow mw, MainLogic ml) {
        this.mw = mw;
        this.ml = ml;
    }

    @Override
    public void run() {
        try {
            this.loadAndSplitConfig();
            this.loadConfig();
            Platform.runLater(() -> this.mw.firstUI());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            this.load();
            try {
                Thread.sleep(5000);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            Platform.runLater(() -> this.mw.continueUI());
        }
    }

    private String loadAndSplitConfig() throws Exception {
        File config = new File("src/config.cf");
        BufferedReader br = new BufferedReader(new FileReader(config));
        StringBuilder lines = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            lines.append(line);
        }
        br.close();

        return lines.toString();
    }

    private void loadConfig() throws Exception {
        boolean first = true;
        File config = new File("src/config.cf");
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
                        case "Orange" -> {this.mw.setTheme("Orange"); this.ml.setTheme("Orange", false);}
                        case "Blue" -> {this.mw.setTheme("Blue"); this.ml.setTheme("Blue", false);}
                        case "Green" -> {this.mw.setTheme("Green"); this.ml.setTheme("Green", false);}
                    }
                }
                case "Path" -> {
                    if (!parts[1].equals("null")) {
                        MainLogic.PATH = parts[1];
                    }
                }
            }
        }
        this.ml.setFirstUseBool(first);
    }

    private void load() {
        try {
            this.loadSongs();
            this.loadPlaylists();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void loadSongs() throws Exception {
        File allSongsFile = new File(MainLogic.PATH + "/all.txt");
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
                this.ml.getSongPaths().add(s);
            } catch (MediaException e) {
                lines = lines.replace(s, "");
                continue;
            }
            Song song = new Song(s, m);
            song.setUp();
            this.ml.getAllSongs().add(song);
            this.ml.getSongQueue().add(song);
        }
        lines = lines.replace(";;", ";");
        BufferedWriter w = new BufferedWriter(new FileWriter(allSongsFile));
        w.write(lines);
        w.close();
        this.ml.equalsQueue();
    }
    private void loadPlaylists() throws Exception {
        File folder = new File(MainLogic.PATH + "/playlists/");

        if (folder.exists()) {
            File[] playlists = folder.listFiles();
            if (playlists != null) {
                for (File playlist : playlists) {
                    int index = playlist.getName().lastIndexOf(".");
                    String name = playlist.getName().substring(0, index);
                    this.ml.createPlaylist(name);
                    BufferedReader br = new BufferedReader(new FileReader(playlist));
                    String lines = br.readLine();
                    br.close();
                    String[] sepLines = lines.split(";");

                    for (String s : sepLines) {
                        int songIndex = this.ml.getSongPaths().indexOf(s);
                        if (songIndex != -1) {
                            this.ml.getPlaylist(name).addSong(this.ml.getAllSongs().get(songIndex));
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
        this.ml.getSongPaths().clear();
    }
}

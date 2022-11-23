import java.io.*;
import java.util.ArrayList;

public class Playlist {
    private final ArrayList<Song> songs;
    private final String name;

    public Playlist(String name) {
        this.songs = new ArrayList<>();
        this.name = name;
    }

    public void savePlaylist() throws Exception {
        File playlistFileOld = new File("playlists/" + name + ".txt");
        File playlistFile;
        if (playlistFileOld.delete()) {
            playlistFile = new File("playlists/" + name + ".txt");
        } else {
            playlistFile = playlistFileOld;
        }

        PrintWriter pw = new PrintWriter(playlistFile);
        StringBuilder sb = new StringBuilder();
        for (Song s : this.songs) {
            sb.append(s.getPath()).append("\n");
        }
        pw.println(sb);
        pw.flush();
        pw.close();
    }
    public void deleteFile() {
        File playlistFile = new File("playlists/" + name + ".txt");
        playlistFile.delete();
    }
    public String getName() {
        return name;
    }
    public String getTotalLength() {
        double allSeconds = 0;
        for (Song s : this.songs) {
            if (s.getDuration() != null) {
                allSeconds += s.getDuration().toSeconds();
            }
        }

        String length;
        if (allSeconds > 0) {
            double min = Math.floor(allSeconds / 60);
            double sec = ((allSeconds / 60) - min) * 60;
            length = String.format("%1$.0f:%2$02.0f", min, sec);
        }  else {
            length = "Empty";
        }
        return length;
    }
    public int getNumberOfSongs() {
        return this.songs.size();
    }
    public ArrayList<Song> getSongs() {
        return songs;
    }
    public void removeSongs(ArrayList<Song> r) {
        this.songs.removeAll(r);
        try {
            this.savePlaylist();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void addSongs(ArrayList<Song> songs) {
        this.songs.addAll(songs);
        try {
            this.savePlaylist();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void addSong(Song s) {
        this.songs.add(s);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
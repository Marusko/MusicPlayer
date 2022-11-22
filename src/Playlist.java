import java.util.ArrayList;

public class Playlist {
    private final ArrayList<Song> songs;
    private final String name;
    private final String path;

    public Playlist(String name, String path) {
        this.songs = new ArrayList<>();
        this.name = name;
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public String getTotalLength() {
        double allSeconds = 0;
        for (Song s : this.songs) {
            allSeconds += s.getDuration().toSeconds();
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
    }

    public void addSongs(ArrayList<Song> songs) {
        this.songs.addAll(songs);
        for (Song s : this.songs) {
            s.setPlaylist(this);
        }
    }

    @Override
    public String toString() {
        return this.name;
    }
}

//TODO save playlists
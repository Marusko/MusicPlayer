import java.util.ArrayList;

public class Playlist {
    private final ArrayList<Song> songs;
    private String name;
    private String totalLength;
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

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalLength() {
        return totalLength;
    }

    public int getNumberOfSongs() {
        return this.songs.size();
    }

    public ArrayList<Song> getSongs() {
        return songs;
    }
    public Song getSong(String name) {
        Song song = null;
        for (Song s : songs) {
            if (s.getName().equals(name)) {
                song = s;
                break;
            }
        }
        return song;
    }
    public void addSong(Song s) {
        this.songs.add(s);
    }
    public void addSongs(ArrayList<Song> songs) {
        this.songs.addAll(songs);
        for (Song s : this.songs) {
            s.setPlaylist(this);
        }
    }

    /*private void calculateTotalLength() {
        ZATIAL NEPOUZITE
    }*/

    @Override
    public String toString() {
        return this.name;
    }
}

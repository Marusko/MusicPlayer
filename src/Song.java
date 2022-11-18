public class Song {
    private final String name;
    private String playlist;
    private final String length;
    private final String path;
    private final String author;
    private final String year;

    public Song(String name, String playlist, String length, String path, String author,String year) {
        this.name = name;
        this.playlist = playlist;
        this.length = length;
        this.path = path;
        this.author = author;
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getLength() {
        return length;
    }

    public String getYear() {
        return year;
    }

    public String getPlaylist() {
        return playlist;
    }

    public void setPlaylist(String playlist) {
        this.playlist = playlist;
    }

    public String getPath() {
        return path;
    }
}

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Objects;


public class MainWindow extends Application {
    private final String autor = "Matúš Suský";
    private final String version = "0.0.2";
    //Testovanie
    private static final int ADD = 0;
    private static final int ALL = 1;
    private static final int PLAYLISTS = 2;
    private static final int SETTINGS = 3;
    private int selected = MainWindow.ALL;
    //Testovanie
    private boolean pla = true;
    private boolean rep = false;//Testovanie
    private boolean shuf = false;

    //UI elements which are updated based on other elements
    private final Label nameOnScreen = new Label();
    private final Label songName = new Label("Song name");
    private final VBox mainListsVbox = new VBox();
    private final VBox settings = new VBox();
    private final VBox add = new VBox();
    private final ScrollPane songsScroll = new ScrollPane();
    private final ScrollPane playlistsScroll = new ScrollPane();

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: #5c5c5c");
        bp.setBottom(this.songControls());
        bp.setLeft(this.menu());
        bp.setCenter(this.content());
        Scene mainScene = new Scene(bp, 1500, 900);
        stage.setTitle("Music player");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        stage.setScene(mainScene);
        stage.show();
        //Testovanie UI
        this.settingsPage();
        this.songsPage();
        this.playlistsPage();
        this.addPage();
        this.setContent(this.songsScroll);
    }
    //Testovanie
    private void setRep(boolean v) {
        this.rep = v;
    }
    private void setPla(boolean v) {
        this.pla = v;
    }
    private void setShuf(boolean v) {
        this.shuf = v;
    }

    public void setNameOnScreen(String s) {
        this.nameOnScreen.setText(s);
    }
    public void setSongName(String s) {
        this.songName.setText(s);
    }

    public static void main(String[] args) {
        launch(args);
    }

    private VBox songControls() {
        VBox mainSongControls = new VBox();
        mainSongControls.setSpacing(10);
        mainSongControls.setAlignment(Pos.TOP_CENTER);

        Button play = new Button();
        ImageView playI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/play.png")).toExternalForm()));
        playI.setFitHeight(20);
        playI.setPreserveRatio(true);
        play.setGraphic(playI);
        ImageView pauseI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/pause.png")).toExternalForm()));
        pauseI.setFitHeight(20);
        pauseI.setPreserveRatio(true);
        play.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/musicControls.css")).toExternalForm());
        play.setOnMouseEntered(e -> play.setStyle("button-color: mouse-on-color"));
        play.setOnMouseExited(e -> play.setStyle("button-color: default-button-color"));
        //Testovanie
        play.setOnAction(e -> {
            if (this.pla) {
                play.setGraphic(pauseI);
                this.setPla(false);
            } else {
                play.setGraphic(playI);
                this.setPla(true);
            }
        });

        Button prev = new Button();
        ImageView prevI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/back.png")).toExternalForm()));
        prevNext(prev, prevI);
        Button next = new Button();
        ImageView nextI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/next.png")).toExternalForm()));
        prevNext(next, nextI);
        Button repeat = new Button();
        ImageView repeatI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/repeat.png")).toExternalForm()));
        repeatI.setFitHeight(20);
        repeatI.setPreserveRatio(true);
        repeat.setGraphic(repeatI);
        repeat.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/musicControls.css")).toExternalForm());
        repeat.setOnMouseEntered(e -> repeat.setStyle("button-color: mouse-on-color"));
        repeat.setOnMouseExited(e -> {
            //testovanie
            if (!this.rep) {
                repeat.setStyle("button-color: default-button-color");
            } else {
                repeat.setStyle("button-color: default-action-button-color");
            }
        });
        //testovanie
        repeat.setOnAction(e -> {
            if (this.rep) {
                this.setRep(false);
                repeat.setStyle("button-color: default-button-color");
            } else {
                this.setRep(true);
                repeat.setStyle("button-color: default-action-button-color");
            }
        });

        Button shuffle = new Button();
        ImageView shuffleI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/shuffle.png")).toExternalForm()));
        shuffleI.setFitHeight(20);
        shuffleI.setPreserveRatio(true);
        shuffle.setGraphic(shuffleI);
        shuffle.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/musicControls.css")).toExternalForm());
        shuffle.setOnMouseEntered(e -> shuffle.setStyle("button-color: mouse-on-color"));
        shuffle.setOnMouseExited(e -> {
            //testovanie
            if (!this.shuf) {
                shuffle.setStyle("button-color: default-button-color");
            } else {
                shuffle.setStyle("button-color: default-action-button-color");
            }
        });
        //testovanie
        shuffle.setOnAction(e -> {
            if (this.shuf) {
                this.setShuf(false);
                shuffle.setStyle("button-color: default-button-color");
            } else {
                this.setShuf(true);
                shuffle.setStyle("button-color: default-action-button-color");
            }
        });

        this.songName.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        Label actualTime = new Label("0:00");
        actualTime.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/labelSmall.css")).toExternalForm());
        Label songLength = new Label("5:00");
        songLength.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/labelSmall.css")).toExternalForm());

        HBox controls = new HBox();
        controls.getChildren().addAll(shuffle, prev, play, next, repeat);
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setSpacing(10);

        HBox songTimeAligment = new HBox();
        songTimeAligment.getChildren().addAll(actualTime, this.hybridSlider(700), songLength);
        songTimeAligment.setAlignment(Pos.TOP_CENTER);
        songTimeAligment.setSpacing(10);

        HBox songNameAligment = new HBox();
        songNameAligment.getChildren().add(this.songName);
        songNameAligment.setAlignment(Pos.TOP_CENTER);

        mainSongControls.getChildren().addAll(songNameAligment, songTimeAligment, controls);
        mainSongControls.setStyle("-fx-background-color: #4d4d4d; -fx-padding: 15px; -fx-background-radius: 10");
        return mainSongControls;
    }

    private void prevNext(Button b, ImageView i) {
        i.setFitHeight(20);
        i.setPreserveRatio(true);
        b.setGraphic(i);
        b.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/musicControls.css")).toExternalForm());
        b.setOnMouseEntered(e -> b.setStyle("button-color: mouse-on-color"));
        b.setOnMouseExited(e -> b.setStyle("button-color: default-button-color"));
    }

    private VBox menu() {
        VBox mainMenu = new VBox();

        Button add = new Button("Add");
        ImageView addI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/add-list.png")).toExternalForm()));
        addI.setFitHeight(20);
        addI.setPreserveRatio(true);
        add.setGraphic(addI);
        add.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        add.setOnMouseEntered(e -> add.setStyle("button-color: mouse-on-color"));
        add.setOnMouseExited(e -> {
            if (this.selected == MainWindow.ADD) {
                add.setStyle("button-color: default-action-button-color");
            } else {
                add.setStyle("button-color: default-button-color");
            }
        });

        Button all = new Button("All songs");
        ImageView allI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/music-file.png")).toExternalForm()));
        allI.setFitHeight(20);
        allI.setPreserveRatio(true);
        all.setGraphic(allI);
        all.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        all.setStyle("button-color: default-action-button-color");
        this.setNameOnScreen("All songs");
        all.setOnMouseEntered(e -> all.setStyle("button-color: mouse-on-color"));
        all.setOnMouseExited(e -> {
            if (this.selected == MainWindow.ALL) {
                all.setStyle("button-color: default-action-button-color");
            } else {
                all.setStyle("button-color: default-button-color");
            }
        });

        Button playlists = new Button("Playlists");
        ImageView playlistI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/playlist.png")).toExternalForm()));
        playlistI.setFitHeight(20);
        playlistI.setPreserveRatio(true);
        playlists.setGraphic(playlistI);
        playlists.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        playlists.setOnMouseEntered(e -> playlists.setStyle("button-color: mouse-on-color"));
        playlists.setOnMouseExited(e -> {
            if (this.selected == MainWindow.PLAYLISTS) {
                playlists.setStyle("button-color: default-action-button-color");
            } else {
                playlists.setStyle("button-color: default-button-color");
            }
        });

        Button settings = new Button("Settings");
        ImageView settingsI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/equalizer.png")).toExternalForm()));
        settingsI.setFitHeight(20);
        settingsI.setPreserveRatio(true);
        settings.setGraphic(settingsI);
        settings.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        settings.setOnMouseEntered(e -> settings.setStyle("button-color: mouse-on-color"));
        settings.setOnMouseExited(e -> {
            if (this.selected == MainWindow.SETTINGS) {
                settings.setStyle("button-color: default-action-button-color");
            } else {
                settings.setStyle("button-color: default-button-color");
            }
        });

        add.setOnAction(e -> {
            this.selected = MainWindow.ADD;
            this.setContent(this.add);
            this.setNameOnScreen("Add new song or playlist");
            add.setStyle("button-color: default-action-button-color");
            all.setStyle("button-color: default-button-color");
            playlists.setStyle("button-color: default-button-color");
            settings.setStyle("button-color: default-button-color");
        });
        all.setOnAction(e -> {
            this.selected = MainWindow.ALL;
            this.setContent(this.songsScroll);
            this.setNameOnScreen("All songs");
            all.setStyle("button-color: default-action-button-color");
            add.setStyle("button-color: default-button-color");
            playlists.setStyle("button-color: default-button-color");
            settings.setStyle("button-color: default-button-color");
        });
        playlists.setOnAction(e -> {
            this.selected = MainWindow.PLAYLISTS;
            this.setContent(this.playlistsScroll);
            this.setNameOnScreen("Your playlists");
            playlists.setStyle("button-color: default-action-button-color");
            all.setStyle("button-color: default-button-color");
            add.setStyle("button-color: default-button-color");
            settings.setStyle("button-color: default-button-color");
        });
        settings.setOnAction(e -> {
            this.selected = MainWindow.SETTINGS;
            this.setContent(this.settings);
            this.setNameOnScreen("Settings");
            settings.setStyle("button-color: default-action-button-color");
            all.setStyle("button-color: default-button-color");
            add.setStyle("button-color: default-button-color");
            playlists.setStyle("button-color: default-button-color");
        });

        Label volume = new Label("Volume");
        volume.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        ImageView volumeI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/volume.png")).toExternalForm()));
        HBox volumeLabelBox = new HBox(volumeI, volume);
        volumeLabelBox.setSpacing(10);
        volumeLabelBox.setAlignment(Pos.CENTER);
        VBox volumeBox = new VBox(volumeLabelBox, hybridSlider(150));
        volumeBox.setSpacing(5);
        volumeBox.setAlignment(Pos.TOP_CENTER);

        mainMenu.getChildren().addAll(add, all, playlists, settings, volumeBox);
        mainMenu.setSpacing(10);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setStyle("-fx-padding: 10px; -fx-background-color: #404040; -fx-background-radius: 10");

        return mainMenu;
    }

    private VBox content() {
        this.nameOnScreen.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/labelLarge.css")).toExternalForm());
        this.mainListsVbox.getChildren().add(this.nameOnScreen);
        this.mainListsVbox.setAlignment(Pos.TOP_LEFT);
        this.mainListsVbox.getChildren().addAll(this.playlistsPage());
        return mainListsVbox;
    }

    private StackPane hybridSlider(int sliderWidth) {

        Slider slider = new Slider();
        slider.setMaxWidth(sliderWidth);
        slider.setMinWidth(sliderWidth);
        slider.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/slider.css")).toExternalForm());
        slider.setOnMouseEntered(e -> slider.setStyle("thumb-transparent: thumb-show"));
        slider.setOnMouseExited(e -> slider.setStyle("thumb-transparent: thumb-hide"));

        ProgressBar pb = new ProgressBar(0);
        pb.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/progressbar.css")).toExternalForm());
        pb.setStyle("track-color:transparent");
        pb.setMaxWidth(sliderWidth);
        pb.setMinWidth(sliderWidth);
        pb.setMaxHeight(13);
        pb.setMinHeight(13);

        ProgressBar pb2 = new ProgressBar(0);
        pb2.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/progressbar.css")).toExternalForm());
        pb2.setMaxWidth(sliderWidth - 6);
        pb2.setMinWidth(sliderWidth - 6);
        pb2.setMaxHeight(3);
        pb2.setMinHeight(3);

        slider.valueProperty().addListener(e -> pb.setProgress(slider.getValue() / 100));

        StackPane hybrid = new StackPane();

        hybrid.getChildren().addAll(pb2, pb, slider);
        return hybrid;
    }

    private void setContent(Node n) {
        this.mainListsVbox.getChildren().remove(1);
        this.mainListsVbox.getChildren().add(n);
    }

    private VBox settingsPage() {
        VBox colorBox = new VBox();
        Label color = new Label("Theme");
        color.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        ComboBox<String> colorPicker = new ComboBox<>();
        colorPicker.getItems().addAll("Orange", "Green", "Blue");
        colorBox.getChildren().addAll(color, colorPicker);
        colorBox.setSpacing(10);
        colorPicker.getSelectionModel().selectFirst();
        colorPicker.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/combobox.css")).toExternalForm());
        colorBox.setOnMouseEntered(e -> colorPicker.setStyle("combo-color: mouse-on-color"));
        colorBox.setOnMouseExited(e -> colorPicker.setStyle("combo-color: default-combo-color"));

        VBox infoBox = new VBox();
        Label info = new Label("Made by: " + this.autor);
        info.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/labelSmall.css")).toExternalForm());
        Label infoVersion = new Label("Version: " + this.version);
        infoVersion.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/labelSmall.css")).toExternalForm());
        infoBox.getChildren().addAll(info, infoVersion);
        infoBox.setSpacing(10);

        this.settings.getChildren().addAll(colorBox, infoBox);
        this.settings.setSpacing(300);
        this.settings.setPadding(new Insets(100));

        return settings;
    }

    private VBox addPage() {
        Label addSong = new Label("Add song");
        addSong.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        Button songButton = new Button("Add");
        songButton.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        songButton.setOnMouseEntered(e -> songButton.setStyle("button-color: mouse-on-color"));
        songButton.setOnMouseExited(e -> songButton.setStyle("button-color: default-button-color"));
        VBox songBox = new VBox(addSong, songButton);
        songBox.setSpacing(10);

        Label createPlaylist = new Label("Create playlist");
        createPlaylist.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        TextField playlistName = new TextField();
        playlistName.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/textfield.css")).toExternalForm());
        playlistName.setOnMouseEntered(e -> playlistName.setStyle("field-color: mouse-on-color"));
        playlistName.setOnMouseExited(e -> playlistName.setStyle("field-color: default-field-color"));
        Button playlistButton = new Button("Create");
        playlistButton.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        playlistButton.setOnMouseEntered(e -> playlistButton.setStyle("button-color: mouse-on-color"));
        playlistButton.setOnMouseExited(e -> playlistButton.setStyle("button-color: default-button-color"));
        VBox playlistBox = new VBox(createPlaylist, playlistName, playlistButton);
        playlistBox.setSpacing(10);

        this.add.getChildren().addAll(songBox, playlistBox);
        this.add.setSpacing(50);
        this.add.setPadding(new Insets(100));
        return this.add;
    }

    private HBox songUI() {
        //CheckBox selected = new CheckBox(); //TO DO
        //selected.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/checkbox.css")).toExternalForm());
        Button playSong = new Button();
        ImageView playSongI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/play.png")).toExternalForm()));
        playSongI.setPreserveRatio(true);
        playSongI.setFitHeight(10);
        playSong.setGraphic(playSongI);
        playSong.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        playSong.setStyle("-fx-pref-width: 10px; -fx-pref-height: 10px");
        playSong.setOnMouseEntered(e -> playSong.setStyle("button-color: mouse-on-color; -fx-pref-width: 10px; -fx-pref-height: 10px"));
        playSong.setOnMouseExited(e -> playSong.setStyle("button-color: default-button-color; -fx-pref-width: 10px; -fx-pref-height: 10px"));
        HBox controlsBox = new HBox(/*selected, */playSong);
        controlsBox.setSpacing(10);
        controlsBox.setAlignment(Pos.CENTER);

        Label songName = new Label("Song name");
        songName.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        Label author = new Label("Author");
        author.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        Label year = new Label("Year");
        year.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        Label length = new Label("5:00");
        length.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());

        HBox song = new HBox(controlsBox, songName, author, year, length);
        song.setSpacing(200);
        song.setPrefHeight(40);
        song.setPrefWidth(1200);
        song.setAlignment(Pos.CENTER);
        song.setStyle(" -fx-background-color: #404040; -fx-background-radius: 10");
        return song;
    }

    private ScrollPane songsPage() {
        VBox songs = new VBox();
        songs.setPadding(new Insets(10));
        songs.setSpacing(10);
        for (int i = 0; i < 8; i++) {
            songs.getChildren().add(this.songUI());
        }
        this.songsScroll.setContent(songs);
        this.songsScroll.setOnMouseEntered(e -> this.songsScroll.setStyle("bar-width: bar-fat"));
        this.songsScroll.setOnMouseExited(e -> this.songsScroll.setStyle("bar-width: bar-skinny"));
        this.songsScroll.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/scrollpane.css")).toExternalForm());
        return this.songsScroll;
    }

    private VBox playlist() {
        ImageView playlistI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        Label name = new Label("Playlist");
        name.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        Button open = new Button("Open");
        open.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/button.css")).toExternalForm());
        open.setOnMouseEntered(e -> open.setStyle("button-color: mouse-on-color"));
        open.setOnMouseExited(e -> open.setStyle("button-color: default-button-color"));
        VBox playlist = new VBox(playlistI, name, open);
        playlist.setSpacing(10);
        playlist.setAlignment(Pos.CENTER);
        playlist.setPadding(new Insets(10));
        playlist.setPrefHeight(250);
        playlist.setPrefWidth(200);
        playlist.setMaxWidth(200);
        playlist.setStyle("-fx-background-color: #404040; -fx-background-radius: 5");
        return playlist;
    }

    private ScrollPane playlistsPage() {
        GridPane playlists = new GridPane();
        playlists.setPadding(new Insets(0, 0, 100, 170));
        playlists.setVgap(40);
        playlists.setHgap(40);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 4; j++) {
                playlists.add(this.playlist(), j, i);
            }
        }
        this.playlistsScroll.setContent(playlists);
        this.playlistsScroll.setOnMouseEntered(e -> this.playlistsScroll.setStyle("bar-width: bar-fat"));
        this.playlistsScroll.setOnMouseExited(e -> this.playlistsScroll.setStyle("bar-width: bar-skinny"));
        this.playlistsScroll.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/scrollpane.css")).toExternalForm());
        return this.playlistsScroll;
    }
}

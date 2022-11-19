import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;


public class MainWindow extends Application {
    private final static String autor = "Matúš Suský";
    private final static String version = "0.0.2";
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

    private MainLogic ml;

    //UI elements which are updated based on other elements
    private Scene mainScene;
    private final Label nameOnScreen = new Label();
    private final Label songName = new Label("Song name");
    private final VBox mainListsVbox = new VBox();
    private final VBox settings = new VBox();
    private final VBox add = new VBox();
    private final ScrollPane songsScroll = new ScrollPane();
    private final ScrollPane playlistsScroll = new ScrollPane();

    @Override
    public void start(Stage stage) throws Exception {
        this.ml = new MainLogic(this);
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: #5c5c5c");
        bp.setBottom(this.songControls());
        bp.setLeft(this.menu());
        bp.setCenter(this.content());
        this.mainScene = new Scene(bp, 1500, 900);
        this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styleSheets/orangeTheme.css")).toExternalForm());
        stage.setTitle("Music player");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        stage.setScene(mainScene);
        stage.show();
        //Testovanie UI
        this.refresh(stage);
        this.switchMenu(this.selected);
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
    private void setTheme(String t) {
        switch (t) {
            case "Orange" -> {
                this.mainScene.getStylesheets().remove(0);
                this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styleSheets/orangeTheme.css")).toExternalForm());
            }
            case "Green" -> {
                this.mainScene.getStylesheets().remove(0);
                this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styleSheets/greenTheme.css")).toExternalForm());
            }
            case "Blue" -> {
                this.mainScene.getStylesheets().remove(0);
                this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("styleSheets/blueTheme.css")).toExternalForm());
            }
        }
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
        play.getStyleClass().add("my-music-control-button");
        play.setOnMouseEntered(e -> play.setStyle("button-color: mouse-color"));
        play.setOnMouseExited(e -> play.setStyle("button-color: default-color"));
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
        repeat.getStyleClass().add("my-music-control-button");
        repeat.setOnMouseEntered(e -> repeat.setStyle("button-color: mouse-color"));
        repeat.setOnMouseExited(e -> {
            //testovanie
            if (!this.rep) {
                repeat.setStyle("button-color: default-color");
            } else {
                repeat.setStyle("button-color: default-action-color");
            }
        });
        //testovanie
        repeat.setOnAction(e -> {
            if (this.rep) {
                this.setRep(false);
                repeat.setStyle("button-color: default-color");
            } else {
                this.setRep(true);
                repeat.setStyle("button-color: default-action-color");
            }
        });

        Button shuffle = new Button();
        ImageView shuffleI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/shuffle.png")).toExternalForm()));
        shuffleI.setFitHeight(20);
        shuffleI.setPreserveRatio(true);
        shuffle.setGraphic(shuffleI);
        shuffle.getStyleClass().add("my-music-control-button");
        shuffle.setOnMouseEntered(e -> shuffle.setStyle("button-color: mouse-color"));
        shuffle.setOnMouseExited(e -> {
            //testovanie
            if (!this.shuf) {
                shuffle.setStyle("button-color: default-color");
            } else {
                shuffle.setStyle("button-color: default-action-color");
            }
        });
        //testovanie
        shuffle.setOnAction(e -> {
            if (this.shuf) {
                this.setShuf(false);
                shuffle.setStyle("button-color: default-color");
            } else {
                this.setShuf(true);
                shuffle.setStyle("button-color: default-action-color");
            }
        });

        Label actualTime = new Label("0:00");
        actualTime.getStyleClass().add("label-small");
        Label songLength = new Label("5:00");
        songLength.getStyleClass().add("label-small");

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
        b.getStyleClass().add("my-music-control-button");
        b.setOnMouseEntered(e -> b.setStyle("button-color: mouse-color"));
        b.setOnMouseExited(e -> b.setStyle("button-color: default-color"));
    }

    private void refresh(Stage stage) {
        this.settingsPage(stage);
        this.songsPage(this.ml.getAllSongs(), stage);
        this.playlistsPage(this.ml.getAllPlaylists(), stage);
        this.addPage(stage);
    }

    private VBox menu() {
        VBox mainMenu = new VBox();

        Button add = new Button("Add");
        ImageView addI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/add-list.png")).toExternalForm()));
        addI.setFitHeight(20);
        addI.setPreserveRatio(true);
        add.setGraphic(addI);
        add.getStyleClass().add("my-menu-button");
        add.setOnMouseEntered(e -> add.setStyle("button-color: mouse-color"));
        add.setOnMouseExited(e -> {
            if (this.selected == MainWindow.ADD) {
                add.setStyle("button-color: default-action-color");
            } else {
                add.setStyle("button-color: default-color");
            }
        });

        Button all = new Button("All songs");
        ImageView allI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/music-file.png")).toExternalForm()));
        allI.setFitHeight(20);
        allI.setPreserveRatio(true);
        all.setGraphic(allI);
        all.getStyleClass().add("my-menu-button");
        all.setStyle("button-color: default-action-color");
        all.setOnMouseEntered(e -> all.setStyle("button-color: mouse-color"));
        all.setOnMouseExited(e -> {
            if (this.selected == MainWindow.ALL) {
                all.setStyle("button-color: default-action-color");
            } else {
                all.setStyle("button-color: default-color");
            }
        });

        Button playlists = new Button("Playlists");
        ImageView playlistI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/playlist.png")).toExternalForm()));
        playlistI.setFitHeight(20);
        playlistI.setPreserveRatio(true);
        playlists.setGraphic(playlistI);
        playlists.getStyleClass().add("my-menu-button");
        playlists.setOnMouseEntered(e -> playlists.setStyle("button-color: mouse-color"));
        playlists.setOnMouseExited(e -> {
            if (this.selected == MainWindow.PLAYLISTS) {
                playlists.setStyle("button-color: default-action-color");
            } else {
                playlists.setStyle("button-color: default-color");
            }
        });

        Button settings = new Button("Settings");
        ImageView settingsI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/equalizer.png")).toExternalForm()));
        settingsI.setFitHeight(20);
        settingsI.setPreserveRatio(true);
        settings.setGraphic(settingsI);
        settings.getStyleClass().add("my-menu-button");
        settings.setOnMouseEntered(e -> settings.setStyle("button-color: mouse-color"));
        settings.setOnMouseExited(e -> {
            if (this.selected == MainWindow.SETTINGS) {
                settings.setStyle("button-color: default-action-color");
            } else {
                settings.setStyle("button-color: default-color");
            }
        });

        add.setOnAction(e -> {
            this.selected = MainWindow.ADD;
            this.switchMenu(this.selected);
            add.setStyle("button-color: default-action-color");
            all.setStyle("button-color: default-color");
            playlists.setStyle("button-color: default-color");
            settings.setStyle("button-color: default-color");
        });
        all.setOnAction(e -> {
            this.selected = MainWindow.ALL;
            this.switchMenu(this.selected);
            all.setStyle("button-color: default-action-color");
            add.setStyle("button-color: default-color");
            playlists.setStyle("button-color: default-color");
            settings.setStyle("button-color: default-color");
        });
        playlists.setOnAction(e -> {
            this.selected = MainWindow.PLAYLISTS;
            this.switchMenu(this.selected);
            playlists.setStyle("button-color: default-action-color");
            all.setStyle("button-color: default-color");
            add.setStyle("button-color: default-color");
            settings.setStyle("button-color: default-color");
        });
        settings.setOnAction(e -> {
            this.selected = MainWindow.SETTINGS;
            this.switchMenu(this.selected);
            settings.setStyle("button-color: default-action-color");
            all.setStyle("button-color: default-color");
            add.setStyle("button-color: default-color");
            playlists.setStyle("button-color: default-color");
        });

        Label volume = new Label("Volume");
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

    private void switchMenu(int menu) {
        switch (menu) {
            case 0 -> {
                this.setContent(this.add);
                this.setNameOnScreen("Add new song or playlist");
            }
            case 1 -> {
                this.setContent(this.songsScroll);
                this.setNameOnScreen("All songs");
            }
            case 2 -> {
                this.setContent(this.playlistsScroll);
                this.setNameOnScreen("Your playlists");
            }
            case 3 -> {
                this.setContent(this.settings);
                this.setNameOnScreen("Settings");
            }
        }
    }

    private VBox content() {
        this.nameOnScreen.getStyleClass().add("label-large");
        this.mainListsVbox.getChildren().add(this.nameOnScreen);
        this.mainListsVbox.setAlignment(Pos.TOP_LEFT);
        return mainListsVbox;
    }

    private StackPane hybridSlider(int sliderWidth) {

        Slider slider = new Slider();
        slider.setMaxWidth(sliderWidth);
        slider.setMinWidth(sliderWidth);
        slider.setOnMouseEntered(e -> slider.setStyle("thumb-transparent: thumb-show"));
        slider.setOnMouseExited(e -> slider.setStyle("thumb-transparent: thumb-hide"));

        ProgressBar pb = new ProgressBar(0);
        pb.setStyle("track-color:transparent");
        pb.setMaxWidth(sliderWidth);
        pb.setMinWidth(sliderWidth);
        pb.setMaxHeight(13);
        pb.setMinHeight(13);

        ProgressBar pb2 = new ProgressBar(0);
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
        if (this.mainListsVbox.getChildren().size() > 1) {
            this.mainListsVbox.getChildren().remove(1);
            this.mainListsVbox.getChildren().add(n);
        } else {
            this.mainListsVbox.getChildren().add(n);
        }
    }

    private void settingsPage(Stage stage) {
        this.settings.getChildren().clear();
        Label color = new Label("Theme");
        ComboBox<String> colorPicker = new ComboBox<>();
        colorPicker.getItems().addAll("Orange", "Green", "Blue");
        colorPicker.getSelectionModel().selectFirst();
        colorPicker.setOnMouseEntered(e -> colorPicker.setStyle("combo-color: mouse-color"));
        colorPicker.setOnMouseExited(e -> colorPicker.setStyle("combo-color: default-color"));
        colorPicker.valueProperty().addListener(e -> this.setTheme(colorPicker.getValue()));
        VBox colorBox = new VBox();
        colorBox.getChildren().addAll(color, colorPicker);
        colorBox.setSpacing(10);

        VBox infoBox = new VBox();
        Label info = new Label("Made by: " + MainWindow.autor);
        info.getStyleClass().add("label-small");
        Label infoVersion = new Label("Version: " + MainWindow.version);
        infoVersion.getStyleClass().add("label-small");
        infoBox.getChildren().addAll(info, infoVersion);
        infoBox.setSpacing(10);

        this.settings.getChildren().addAll(colorBox, infoBox);
        this.settings.setSpacing(300);
        this.settings.setPadding(new Insets(100));
    }

    private void addPage(Stage stage) {
        this.add.getChildren().clear();
        Label addSong = new Label("Add song");
        Button songButton = new Button("Add");
        songButton.getStyleClass().add("my-menu-button");
        songButton.setOnMouseEntered(e -> songButton.setStyle("button-color: mouse-color"));
        songButton.setOnMouseExited(e -> songButton.setStyle("button-color: default-color"));
        songButton.setOnAction(e -> {
            this.ml.addSong(stage);
            this.refresh(stage);
        });
        VBox songBox = new VBox(addSong, songButton);
        songBox.setSpacing(10);

        Label createPlaylist = new Label("Create playlist");
        TextField playlistName = new TextField();
        playlistName.setOnMouseEntered(e -> playlistName.setStyle("field-color: mouse-color"));
        playlistName.setOnMouseExited(e -> playlistName.setStyle("field-color: default-color"));
        Button playlistButton = new Button("Create");
        playlistButton.getStyleClass().add("my-menu-button");
        playlistButton.setOnMouseEntered(e -> playlistButton.setStyle("button-color: mouse-color"));
        playlistButton.setOnMouseExited(e -> playlistButton.setStyle("button-color: default-color"));
        playlistButton.setOnAction(e -> {
            this.ml.createPlaylist(playlistName.getText());
            playlistName.setText("");
            this.refresh(stage);
        });
        VBox playlistBox = new VBox(createPlaylist, playlistName, playlistButton);
        playlistBox.setSpacing(10);

        Label addToPlaylist = new Label("Or add choosen songs to your existing playlist");
        ComboBox<Playlist> choosePlaylist = new ComboBox<>();
        choosePlaylist.getItems().addAll(this.ml.getAllPlaylists());
        choosePlaylist.setOnMouseEntered(e -> choosePlaylist.setStyle("combo-color: mouse-color"));
        choosePlaylist.setOnMouseExited(e -> choosePlaylist.setStyle("combo-color: default-color"));
        Button addToButton = new Button("Add");
        addToButton.getStyleClass().add("my-menu-button");
        addToButton.setOnMouseEntered(e -> addToButton.setStyle("button-color: mouse-color"));
        addToButton.setOnMouseExited(e -> addToButton.setStyle("button-color: default-color"));
        VBox addToBox = new VBox(addToPlaylist, choosePlaylist, addToButton);
        addToBox.setSpacing(10);

        this.add.getChildren().addAll(songBox, playlistBox, addToBox);
        this.add.setSpacing(50);
        this.add.setPadding(new Insets(100));
    }

    private HBox songUI(String name, String auth, String yer, String len, Song s) {
        CheckBox selected = new CheckBox();
        selected.setOnAction(e -> {
            if (selected.isSelected()) {
                this.ml.selectSong(s);
            }
        });
        Button playSong = new Button();
        ImageView playSongI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/play.png")).toExternalForm()));
        playSongI.setPreserveRatio(true);
        playSongI.setFitHeight(10);
        playSong.setGraphic(playSongI);
        playSong.getStyleClass().add("my-play-button");
        playSong.setOnMouseEntered(e -> playSong.setStyle("button-color: mouse-color"));
        playSong.setOnMouseExited(e -> playSong.setStyle("button-color: default-color"));
        HBox controlsBox = new HBox(selected, playSong);
        controlsBox.setSpacing(10);
        controlsBox.setAlignment(Pos.CENTER);

        Label songName = new Label(name);
        Label author = new Label(auth);
        Label year = new Label(yer);
        Label length = new Label(len);

        HBox song = new HBox(controlsBox, songName, author, year, length);
        song.setSpacing(200);
        song.setPrefHeight(40);
        song.setPrefWidth(1200);
        song.setAlignment(Pos.CENTER);
        song.setStyle(" -fx-background-color: #404040; -fx-background-radius: 10");
        return song;
    }

    private void songsPage(ArrayList<Song> songsA, Stage stage) {
        this.songsScroll.setContent(null);
        VBox songs = new VBox();
        songs.setPadding(new Insets(10));
        songs.setSpacing(10);
        for (Song s : songsA) {
            songs.getChildren().add(this.songUI(s.getName(), s.getAuthor(), s.getYear(), "0:00", s));
        }
        this.songsScroll.setContent(songs);
        this.songsScroll.setOnMouseEntered(e -> this.songsScroll.lookup(".scroll-bar").setStyle("bar-width: bar-fat"));
        this.songsScroll.setOnMouseExited(e -> this.songsScroll.lookup(".scroll-bar").setStyle("bar-width: bar-skinny"));
    }

    private VBox playlist(String namePlaylist) {
        ImageView playlistI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        Label name = new Label(namePlaylist);
        Button open = new Button("Open");
        open.getStyleClass().add("my-menu-button");
        open.setOnMouseEntered(e -> open.setStyle("button-color: mouse-color"));
        open.setOnMouseExited(e -> open.setStyle("button-color: default-color"));
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

    private void playlistsPage(ArrayList<Playlist> all, Stage stage) {
        this.playlistsScroll.setContent(null);
        FlowPane playlists = new FlowPane();
        playlists.setPrefWidth(1200);
        playlists.setPadding(new Insets(0, 0, 100, 170));
        playlists.setVgap(40);
        playlists.setHgap(40);
        for (Playlist p : all) {
            playlists.getChildren().add(this.playlist(p.getName()));
        }
        this.playlistsScroll.setContent(playlists);
        this.playlistsScroll.setOnMouseEntered(e -> this.playlistsScroll.lookup(".scroll-bar").setStyle("bar-width: bar-fat"));
        this.playlistsScroll.setOnMouseExited(e -> this.playlistsScroll.lookup(".scroll-bar").setStyle("bar-width: bar-skinny"));
    }
}

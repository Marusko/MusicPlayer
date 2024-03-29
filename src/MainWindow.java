import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.MediaPlayer;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;


public class MainWindow extends Application {
    private final static String autor = "Matúš Suský";
    private final static String version = "0.2.7";
    private final Hyperlink playerIcon = new Hyperlink("-Main Icon");
    private final Hyperlink otherIcon = new Hyperlink("-Other icons");
    private final Hyperlink loadingAni = new Hyperlink("-Loading animation");

    private static final int ADD = 0;
    private static final int ALL = 1;
    private static final int PLAYLISTS = 2;
    private static final int SETTINGS = 3;
    private static final int REMOVE = 4;
    private int selected = MainWindow.ALL;
    private MainLogic ml;
    private LoadingWindow lw;
    private Loader l;

    //UI elements which are updated based on other elements
    private BorderPane bp;
    private Stage mainStage;
    private Scene mainScene;
    private final Label nameOnScreen = new Label();
    private final Label songName = new Label("Song name");
    private final VBox mainListsVbox = new VBox();
    private final VBox settings = new VBox();
    private final VBox add = new VBox();
    private final VBox remove = new VBox();
    private final ScrollPane songsScroll = new ScrollPane();
    private final ScrollPane playlistsScroll = new ScrollPane();
    private Button playButton;
    private Button shuffle;
    private ImageView playI;
    private ImageView pauseI;
    private Label songLength;
    private Slider songSlider;
    private ProgressBar songProgress;
    private Label actualTime;
    private Slider volumeSlider;
    private VBox songControl;
    private VBox volumeBox;

    public void start(Stage stage) {
        this.ml = new MainLogic(this);
        this.mainStage = stage;
        bp = new BorderPane();
        this.mainScene = new Scene(bp, 1500, 900);
        this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/orangeTheme.css")).toExternalForm());
        l = new Loader();
        l.setup(this, this.ml);
        l.start();
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
           throw new RuntimeException(e);
        }
    }

    public void loadingUI() {
        lw = new LoadingWindow();
        lw.setStyle(this.ml.getTheme());
        lw.loading();
    }
    public void firstUI() {
        if (this.ml.isFirstUseBool()) {
            FirstUseWindow fuw = new FirstUseWindow();
            fuw.start(new Stage(), this.ml);
        }
        l.interrupt();
        this.loadingUI();
    }

    public void continueUI() {
        playI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/play.png")).toExternalForm()));
        playI.setFitHeight(20);
        playI.setPreserveRatio(true);
        pauseI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/pause.png")).toExternalForm()));
        pauseI.setFitHeight(20);
        pauseI.setPreserveRatio(true);
        this.playButton = new Button();
        bp.setStyle("-fx-background-color: #5c5c5c");
        songControl = this.songControls();
        bp.setBottom(songControl);
        bp.setLeft(this.menu());
        bp.setCenter(this.content());
        this.mainStage.setTitle("Music player");
        this.mainStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        this.mainStage.setScene(mainScene);

        playerIcon.setOnAction(iconEvent -> getHostServices().showDocument("https://www.flaticon.com/free-icon/music_2402461?term=music&page=1&position=9&origin=tag&related_id=2402461"));
        playerIcon.getStyleClass().add("label-small");
        otherIcon.setOnAction(iconEvent -> getHostServices().showDocument("https://www.flaticon.com/packs/multimedia-171"));
        otherIcon.getStyleClass().add("label-small");
        loadingAni.setOnAction(iconEvent -> getHostServices().showDocument("https://github.com/JFXtras/jfxtras-styles#readme"));
        loadingAni.getStyleClass().add("label-small");

        this.refresh(true);
        volumeBox.setDisable(true);
        songControl.setDisable(true);
        this.switchMenu(this.selected);
        this.mainStage.show();
        lw.close();
    }
    public void setTitle(String s) {
        if (s != null) {
            this.mainStage.setTitle("Playing - " + s);
        } else {
            this.mainStage.setTitle("Music player");
        }
    }
    public void setTheme(String t) {
        switch (t) {
            case "Orange" -> {
                this.mainScene.getStylesheets().remove(0);
                this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/orangeTheme.css")).toExternalForm());
                try {
                    this.ml.setTheme("Orange", true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "Green" -> {
                this.mainScene.getStylesheets().remove(0);
                this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/greenTheme.css")).toExternalForm());
                try {
                    this.ml.setTheme("Green", true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "Blue" -> {
                this.mainScene.getStylesheets().remove(0);
                this.mainScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/blueTheme.css")).toExternalForm());
                try {
                    this.ml.setTheme("Blue", true);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    public void setNameOnScreen(String s) {
        this.nameOnScreen.setText(s);
    }
    public void setSongName() {
        this.songName.setText(this.ml.getActualSong().getName());
    }
    public void setSongLength() {
        this.songLength.setText(this.ml.getActualSong().getLength());
    }
    public void setActualSongTime(MediaPlayer mp) {
        String actual = "";
        if (mp != null) {
            Duration d = mp.getCurrentTime();
            double min = Math.floor(d.toMinutes());
            double sec = ((d.toMinutes()) - min) * 60;
            actual = String.format("%1$.0f:%2$02.0f", min, sec);
        }
        this.actualTime.setText(actual);
    }
    public void setSongSlider(MediaPlayer mp) {
        this.songSlider.setValue(mp.getCurrentTime().toSeconds());
    }
    public void setSongSliderLength(MediaPlayer mp) {
        songSlider.maxProperty().bind(Bindings.createDoubleBinding(
                () -> mp.getTotalDuration().toSeconds(),
                mp.totalDurationProperty()));
    }
    public void setPlayButtonImage(boolean b) {
        if (b) {
            this.playButton.setGraphic(playI);
        } else {
            this.playButton.setGraphic(pauseI);
        }
    }
    public void resetShuffleColor() {
        shuffle.setStyle("button-color: default-color");
    }

    public static void main(String[] args) {
        launch(args);
    }

    private VBox songControls() {
        VBox mainSongControls = new VBox();
        mainSongControls.setSpacing(10);
        mainSongControls.setAlignment(Pos.TOP_CENTER);

        playButton.getStyleClass().add("my-music-control-button");
        playButton.setOnMouseEntered(e -> playButton.setStyle("button-color: mouse-color"));
        playButton.setOnMouseExited(e -> playButton.setStyle("button-color: default-color"));
        this.setPlayButtonImage(false);
        playButton.setOnAction(e -> {
            this.setPlayButtonImage(this.ml.isPla());
            this.ml.playPauseSong();
        });

        Button prev = new Button();
        ImageView prevI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/back.png")).toExternalForm()));
        prevNext(prev, prevI);
        prev.setOnAction(e -> this.ml.playPrev());
        Button next = new Button();
        ImageView nextI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/next.png")).toExternalForm()));
        prevNext(next, nextI);
        next.setOnAction(e -> this.ml.playNext());
        Button repeat = new Button();
        ImageView repeatAllI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/repeat.png")).toExternalForm()));
        repeatAllI.setFitHeight(20);
        repeatAllI.setPreserveRatio(true);
        ImageView repeatOneI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/repeat-once.png")).toExternalForm()));
        repeatOneI.setFitHeight(20);
        repeatOneI.setPreserveRatio(true);
        repeat.setGraphic(repeatAllI);
        repeat.getStyleClass().add("my-music-control-button");
        repeat.setOnMouseEntered(e -> repeat.setStyle("button-color: mouse-color"));
        repeat.setOnMouseExited(e -> {
            if (this.ml.isRep() == 0) {
                repeat.setStyle("button-color: default-color");
            } else {
                repeat.setStyle("button-color: default-action-color");
            }
        });
        repeat.setOnAction(e -> {
            if (this.ml.isRep() == 2) {
                this.ml.setRep();
                repeat.setStyle("button-color: default-color");
                repeat.setGraphic(repeatAllI);
            } else if (this.ml.isRep() == 0) {
                this.ml.setRep();
                repeat.setStyle("button-color: default-action-color");
            } else if (this.ml.isRep() == 1) {
                this.ml.setRep();
                repeat.setStyle("button-color: default-action-color");
                repeat.setGraphic(repeatOneI);
            }
        });

        shuffle = new Button();
        ImageView shuffleI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/shuffle.png")).toExternalForm()));
        shuffleI.setFitHeight(20);
        shuffleI.setPreserveRatio(true);
        shuffle.setGraphic(shuffleI);
        shuffle.getStyleClass().add("my-music-control-button");
        shuffle.setOnMouseEntered(e -> shuffle.setStyle("button-color: mouse-color"));
        shuffle.setOnMouseExited(e -> {
            if (!this.ml.isShuf()) {
                shuffle.setStyle("button-color: default-color");
            } else {
                shuffle.setStyle("button-color: default-action-color");
            }
        });
        shuffle.setOnAction(e -> {
            if (this.ml.isShuf()) {
                this.ml.setAndShuffle();
                shuffle.setStyle("button-color: default-color");
            } else {
                this.ml.setAndShuffle();
                shuffle.setStyle("button-color: default-action-color");
            }
        });

        actualTime = new Label("0:00");
        actualTime.getStyleClass().add("label-small");
        songLength = new Label("0:00");
        songLength.getStyleClass().add("label-small");

        HBox controls = new HBox();
        controls.getChildren().addAll(shuffle, prev, playButton, next, repeat);
        controls.setAlignment(Pos.TOP_CENTER);
        controls.setSpacing(10);

        HBox songTimeAlignment = new HBox();
        StackPane sp = this.hybridSlider(700);
        songSlider = (Slider)sp.getChildren().get(2);
        songSlider.valueProperty().addListener(e -> {
            songSlider.setOnMousePressed(ev -> this.ml.setUpdatingStateOfSong(false));
            songSlider.setOnMouseReleased(eve -> {
                this.ml.getMp().seek(new Duration(songSlider.getValue() * 1000));
                this.ml.setUpdatingStateOfSong(true);
                this.ml.getMp().currentTimeProperty().addListener(ev -> {
                    if (ml.isUpdatingStateOfSong()) {
                        this.setActualSongTime(this.ml.getMp());
                        this.setSongSlider(this.ml.getMp());
                    }
                });
            });
            songProgress.setProgress(songSlider.getValue() / ml.getMp().getTotalDuration().toSeconds());
        });
        songProgress = (ProgressBar) sp.getChildren().get(1);
        songTimeAlignment.getChildren().addAll(actualTime, sp, songLength);
        songTimeAlignment.setAlignment(Pos.TOP_CENTER);
        songTimeAlignment.setSpacing(10);

        HBox songNameAlignment = new HBox();
        songNameAlignment.getChildren().add(this.songName);
        songNameAlignment.setAlignment(Pos.TOP_CENTER);

        mainSongControls.getChildren().addAll(songNameAlignment, songTimeAlignment, controls);
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

    public void refresh(boolean b) {
        if (b) {
            this.songsPage(this.ml.getAllSongs());
        }
        this.settingsPage();
        this.playlistsPage(this.ml.getAllPlaylists());
        this.addPage();
        this.removePage();
        this.bp.setLeft(this.menu());
        if (this.ml.getMp() != null) {
            this.volumeSlider.setValue(this.ml.getMp().getVolume() * 100);
        }
    }

    private VBox menu() {
        VBox mainMenu = new VBox();

        Button add = new Button("Add");
        ImageView addI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/plus.png")).toExternalForm()));
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

        Button remove = new Button("Remove");
        ImageView removeI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/minus.png")).toExternalForm()));
        removeI.setFitHeight(20);
        removeI.setPreserveRatio(true);
        remove.setGraphic(removeI);
        remove.getStyleClass().add("my-menu-button");
        remove.setOnMouseEntered(e -> remove.setStyle("button-color: mouse-color"));
        remove.setOnMouseExited(e -> {
            if (this.selected == MainWindow.REMOVE) {
                remove.setStyle("button-color: default-action-color");
            } else {
                remove.setStyle("button-color: default-color");
            }
        });

        Button all = new Button("All songs");
        ImageView allI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/music-file.png")).toExternalForm()));
        allI.setFitHeight(20);
        allI.setPreserveRatio(true);
        all.setGraphic(allI);
        all.getStyleClass().add("my-menu-button");
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
            remove.setStyle("button-color: default-color");
        });
        all.setOnAction(e -> {
            this.selected = MainWindow.ALL;
            this.songsPage(this.ml.getAllSongs());
            this.ml.setSelectedPlaylist(null);
            this.switchMenu(this.selected);
            all.setStyle("button-color: default-action-color");
            add.setStyle("button-color: default-color");
            playlists.setStyle("button-color: default-color");
            settings.setStyle("button-color: default-color");
            remove.setStyle("button-color: default-color");
        });
        playlists.setOnAction(e -> {
            this.selected = MainWindow.PLAYLISTS;
            this.switchMenu(this.selected);
            playlists.setStyle("button-color: default-action-color");
            all.setStyle("button-color: default-color");
            add.setStyle("button-color: default-color");
            settings.setStyle("button-color: default-color");
            remove.setStyle("button-color: default-color");
        });
        settings.setOnAction(e -> {
            this.selected = MainWindow.SETTINGS;
            this.switchMenu(this.selected);
            settings.setStyle("button-color: default-action-color");
            all.setStyle("button-color: default-color");
            add.setStyle("button-color: default-color");
            playlists.setStyle("button-color: default-color");
            remove.setStyle("button-color: default-color");
        });

        remove.setOnAction(e -> {
            this.selected = MainWindow.REMOVE;
            this.switchMenu(this.selected);
            remove.setStyle("button-color: default-action-color");
            all.setStyle("button-color: default-color");
            add.setStyle("button-color: default-color");
            playlists.setStyle("button-color: default-color");
            settings.setStyle("button-color: default-color");
        });

        Label volume = new Label("Volume");
        ImageView volumeI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/volume.png")).toExternalForm()));
        HBox volumeLabelBox = new HBox(volumeI, volume);
        volumeLabelBox.setSpacing(10);
        volumeLabelBox.setAlignment(Pos.CENTER);
        StackPane sp = this.hybridSlider(150);
        volumeSlider = (Slider)sp.getChildren().get(2);
        volumeSlider.setValue(30);
        volumeSlider.valueProperty().addListener(e -> this.ml.changeVolume(volumeSlider.getValue() / 100));
        volumeBox = new VBox(volumeLabelBox, sp);
        volumeBox.setSpacing(5);
        volumeBox.setAlignment(Pos.TOP_CENTER);
        if (this.ml.getMp() == null) {
            volumeBox.setDisable(true);
        }

        mainMenu.getChildren().addAll(add);
        if (!this.ml.getAllPlaylists().isEmpty()) {
            mainMenu.getChildren().add(remove);
        }
        mainMenu.getChildren().addAll(all, playlists, settings, volumeBox);
        mainMenu.setSpacing(10);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setStyle("-fx-padding: 10px; -fx-background-color: #404040; -fx-background-radius: 10");

        switch (this.selected) {
            case 0 -> add.setStyle("button-color: default-action-color");
            case 1 -> all.setStyle("button-color: default-action-color");
            case 2 -> playlists.setStyle("button-color: default-action-color");
            case 3 -> settings.setStyle("button-color: default-action-color");
            case 4 -> remove.setStyle("button-color: default-action-color");
        }

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
            case 4 -> {
                this.setContent(this.remove);
                this.setNameOnScreen("Remove songs");
            }
        }
    }
    private VBox content() {
        this.nameOnScreen.getStyleClass().add("label-large");
        this.mainListsVbox.getChildren().add(this.nameOnScreen);
        this.mainListsVbox.setAlignment(Pos.TOP_LEFT);
        return mainListsVbox;
    }
    private void setContent(Node n) {
        if (this.mainListsVbox.getChildren().size() > 1) {
            this.mainListsVbox.getChildren().remove(1);
            this.mainListsVbox.getChildren().add(n);
        } else {
            this.mainListsVbox.getChildren().add(n);
        }
    }

    private void removePage() {
        this.remove.getChildren().clear();
        Label removeFromPlaylist = new Label("Remove chosen songs from your existing playlist");
        ComboBox<Playlist> choosePlaylist = new ComboBox<>();
        choosePlaylist.getItems().addAll(this.ml.getAllPlaylists());
        choosePlaylist.setOnMouseEntered(e -> choosePlaylist.setStyle("combo-color: mouse-color"));
        choosePlaylist.setOnMouseExited(e -> choosePlaylist.setStyle("combo-color: default-color"));
        Button deleteButton = new Button("Delete");
        deleteButton.getStyleClass().add("my-menu-button");
        deleteButton.setOnMouseEntered(e -> deleteButton.setStyle("button-color: mouse-color"));
        deleteButton.setOnMouseExited(e -> deleteButton.setStyle("button-color: default-color"));
        deleteButton.setOnAction(e -> {
            choosePlaylist.getValue().removeSongs(this.ml.getSelectedSongs());
            this.ml.getSelectedSongs().clear();
            this.refresh(false);
        });
        VBox removeFromBox = new VBox(removeFromPlaylist, choosePlaylist, deleteButton);
        removeFromBox.setSpacing(10);

        Label removePlaylist = new Label("Remove your existing playlist");
        ComboBox<Playlist> choosePlaylistToDelete = new ComboBox<>();
        choosePlaylistToDelete.getItems().addAll(this.ml.getAllPlaylists());
        choosePlaylistToDelete.setOnMouseEntered(e -> choosePlaylistToDelete.setStyle("combo-color: mouse-color"));
        choosePlaylistToDelete.setOnMouseExited(e -> choosePlaylistToDelete.setStyle("combo-color: default-color"));
        Button deletePlaylistButton = new Button("Delete");
        deletePlaylistButton.getStyleClass().add("my-menu-button");
        deletePlaylistButton.setOnMouseEntered(e -> deletePlaylistButton.setStyle("button-color: mouse-color"));
        deletePlaylistButton.setOnMouseExited(e -> deletePlaylistButton.setStyle("button-color: default-color"));
        deletePlaylistButton.setOnAction(e -> {
            this.ml.deletePlaylist(choosePlaylistToDelete.getValue());
            this.refresh(false);
        });
        VBox removeBox = new VBox(removePlaylist, choosePlaylistToDelete, deletePlaylistButton);
        removeBox.setSpacing(10);

        this.remove.getChildren().addAll(removeFromBox, removeBox);
        this.remove.setSpacing(50);
        this.remove.setPadding(new Insets(100));
    }

    private void settingsPage() {
        this.settings.getChildren().clear();
        Label color = new Label("Theme");
        ComboBox<String> colorPicker = new ComboBox<>();
        colorPicker.getItems().addAll("Orange", "Green", "Blue");
        colorPicker.getSelectionModel().select(this.ml.getTheme());
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
        Label copy = new Label("Copyright: ");
        copy.getStyleClass().add("label-small");
        VBox copyBox = new VBox(copy, loadingAni, playerIcon, otherIcon);
        infoBox.getChildren().addAll(info, infoVersion, copyBox);
        infoBox.setSpacing(10);

        Label setPathLabel = new Label("Active folder for player data: " + MainLogic.PATH), orLabel = new Label("Set new folder");

        Button searchButton = new Button("Search PC");
        searchButton.getStyleClass().add("my-menu-button");
        searchButton.setOnMouseEntered(e -> searchButton.setStyle("button-color: mouse-color"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("button-color: default-color"));

        searchButton.setOnAction(e -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Choose folder");
            File selected = dc.showDialog(this.mainStage);
            try {
                this.ml.setPath(selected.getAbsolutePath());
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            setPathLabel.setText("Active folder for player data: " + selected.getAbsolutePath());
        });

        VBox pathBox = new VBox(setPathLabel, orLabel, searchButton);
        pathBox.setSpacing(10);

        this.settings.getChildren().addAll(colorBox, pathBox, infoBox);
        this.settings.setSpacing(50);
        this.settings.setPadding(new Insets(100));
    }

    private void addPage() {
        this.add.getChildren().clear();
        Label addSong = new Label("Add song");
        Button songButton = new Button("Add");
        songButton.getStyleClass().add("my-menu-button");
        songButton.setOnMouseEntered(e -> songButton.setStyle("button-color: mouse-color"));
        songButton.setOnMouseExited(e -> songButton.setStyle("button-color: default-color"));
        songButton.setOnAction(e -> {
            try {
                this.ml.addSong(this.mainStage);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
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
            this.refresh(false);
        });
        VBox playlistBox = new VBox(createPlaylist, playlistName, playlistButton);
        playlistBox.setSpacing(10);

        Label addToPlaylist = new Label("Or add chosen songs to your existing playlist");
        ComboBox<Playlist> choosePlaylist = new ComboBox<>();
        choosePlaylist.getItems().addAll(this.ml.getAllPlaylists());
        choosePlaylist.setOnMouseEntered(e -> choosePlaylist.setStyle("combo-color: mouse-color"));
        choosePlaylist.setOnMouseExited(e -> choosePlaylist.setStyle("combo-color: default-color"));
        Button addToButton = new Button("Add");
        addToButton.getStyleClass().add("my-menu-button");
        addToButton.setOnMouseEntered(e -> addToButton.setStyle("button-color: mouse-color"));
        addToButton.setOnMouseExited(e -> addToButton.setStyle("button-color: default-color"));
        addToButton.setOnAction(e -> {
            choosePlaylist.getValue().addSongs(this.ml.getSelectedSongs());
            this.ml.getSelectedSongs().clear();
            this.refresh(false);
        });
        VBox addToBox = new VBox();
        addToBox.setSpacing(10);
        if (!this.ml.getAllPlaylists().isEmpty()) {
            addToBox.getChildren().addAll(addToPlaylist, choosePlaylist, addToButton);
        }

        this.add.getChildren().addAll(songBox, playlistBox, addToBox);
        this.add.setSpacing(50);
        this.add.setPadding(new Insets(100));
    }

    private HBox songUI(String name, String auth, String len, Song s) {
        CheckBox selected = new CheckBox();
        if (this.ml.getSelectedSongs().contains(s)) {
            selected.setSelected(true);
        }
        selected.setOnAction(e -> {
            if (selected.isSelected()) {
                this.ml.selectSong(s);
            } else if (!selected.isSelected()) {
                this.ml.deSelectSong(s);
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
        playSong.setOnAction(e -> {
            this.ml.setPlaylistSongs();
            this.ml.playSong(s);
            this.volumeBox.setDisable(false);
            this.songControl.setDisable(false);
        });
        HBox controlsBox = new HBox(selected, playSong);
        controlsBox.setSpacing(10);
        controlsBox.setAlignment(Pos.CENTER);

        Label songName = new Label(name);
        songName.setPrefWidth(600);
        Label author = new Label(auth);
        author.setPrefWidth(225);
        Label length = new Label(len);

        HBox song = new HBox(controlsBox, songName, author, length);
        song.setSpacing(75);
        song.setPrefHeight(40);
        song.setPrefWidth(1200);
        song.setPadding(new Insets(10));
        song.setAlignment(Pos.CENTER_LEFT);
        song.setStyle(" -fx-background-color: #404040; -fx-background-radius: 10");
        return song;
    }
    private void songsPage(ArrayList<Song> songsA) {
        this.songsScroll.setContent(null);
        VBox songs = new VBox();
        songs.setPadding(new Insets(10));
        songs.setSpacing(10);
        for (Song s : songsA) {
            songs.getChildren().add(this.songUI(s.getName(), s.getAuthor(), s.getLength(), s));
        }
        this.songsScroll.setContent(songs);
        this.songsScroll.setOnMouseEntered(e -> this.songsScroll.lookup(".scroll-bar").setStyle("bar-width: bar-fat"));
        this.songsScroll.setOnMouseExited(e -> this.songsScroll.lookup(".scroll-bar").setStyle("bar-width: bar-skinny"));
    }

    private VBox playlist(String namePlaylist) {
        ImageView playlistI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/playlist_big.png")).toExternalForm()));
        Label name = new Label(namePlaylist);

        Label songs = new Label("Empty");
        songs.getStyleClass().add("label-small");
        songs.setText(this.ml.getPlaylist(namePlaylist).getNumberOfSongs() + " songs");
        Label length = new Label("Empty");
        length.getStyleClass().add("label-small");
        length.setText(this.ml.getPlaylist(namePlaylist).getTotalLength());
        VBox infoBox = new VBox(songs, length);
        infoBox.setAlignment(Pos.CENTER);

        Button open = new Button("Open");
        open.getStyleClass().add("my-menu-button");
        open.setOnMouseEntered(e -> open.setStyle("button-color: mouse-color"));
        open.setOnMouseExited(e -> open.setStyle("button-color: default-color"));
        open.setOnAction(e -> {
            this.songsPage(this.ml.getPlaylist(namePlaylist).getSongs());
            this.ml.setSelectedPlaylist(this.ml.getPlaylist(namePlaylist));
            this.switchMenu(MainWindow.ALL);
            this.setNameOnScreen(namePlaylist);
        });
        VBox playlist = new VBox(playlistI, name, infoBox, open);
        playlist.setSpacing(10);
        playlist.setAlignment(Pos.CENTER);
        playlist.setPadding(new Insets(10));
        playlist.setPrefHeight(250);
        playlist.setPrefWidth(200);
        playlist.setMaxWidth(200);
        playlist.setStyle("-fx-background-color: #404040; -fx-background-radius: 5");
        return playlist;
    }
    private void playlistsPage(ArrayList<Playlist> all) {
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
}

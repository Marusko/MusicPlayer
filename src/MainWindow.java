import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;


public class MainWindow extends Application {
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

    @Override
    public void start(Stage stage) throws Exception {
        BorderPane bp = new BorderPane();
        bp.setStyle("-fx-background-color: #5c5c5c");
        bp.setBottom(this.songControls());
        bp.setLeft(this.menu());
        bp.setCenter(this.lists());
        Scene mainScene = new Scene(bp, 1500, 900);
        stage.setTitle("Music player");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        stage.setScene(mainScene);
        stage.show();
    }

    public void setNameOnScreen(String s) {
        this.nameOnScreen.setText(s);
    }
    public void setSongName(String s) {
        this.songName.setText(s);
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
        prevI.setFitHeight(20);
        prevI.setPreserveRatio(true);
        prev.setGraphic(prevI);
        prev.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/musicControls.css")).toExternalForm());
        prev.setOnMouseEntered(e -> prev.setStyle("button-color: mouse-on-color"));
        prev.setOnMouseExited(e -> prev.setStyle("button-color: default-button-color"));

        Button next = new Button();
        ImageView nextI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/next.png")).toExternalForm()));
        nextI.setFitHeight(20);
        nextI.setPreserveRatio(true);
        next.setGraphic(nextI);
        next.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/musicControls.css")).toExternalForm());
        next.setOnMouseEntered(e -> next.setStyle("button-color: mouse-on-color"));
        next.setOnMouseExited(e -> next.setStyle("button-color: default-button-color"));

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
            this.setNameOnScreen("Add new song or playlist");
            add.setStyle("button-color: default-action-button-color");
            all.setStyle("button-color: default-button-color");
            playlists.setStyle("button-color: default-button-color");
            settings.setStyle("button-color: default-button-color");
        });
        all.setOnAction(e -> {
            this.selected = MainWindow.ALL;
            this.setNameOnScreen("All songs");
            all.setStyle("button-color: default-action-button-color");
            add.setStyle("button-color: default-button-color");
            playlists.setStyle("button-color: default-button-color");
            settings.setStyle("button-color: default-button-color");
        });
        playlists.setOnAction(e -> {
            this.selected = MainWindow.PLAYLISTS;
            this.setNameOnScreen("Your playlists");
            playlists.setStyle("button-color: default-action-button-color");
            all.setStyle("button-color: default-button-color");
            add.setStyle("button-color: default-button-color");
            settings.setStyle("button-color: default-button-color");
        });
        settings.setOnAction(e -> {
            this.selected = MainWindow.SETTINGS;
            this.setNameOnScreen("Settings");
            settings.setStyle("button-color: default-action-button-color");
            all.setStyle("button-color: default-button-color");
            add.setStyle("button-color: default-button-color");
            playlists.setStyle("button-color: default-button-color");
        });

        Label volume = new Label("Volume");
        volume.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/label.css")).toExternalForm());
        ImageView volumeI = new ImageView(new Image(Objects.requireNonNull(getClass().getResource("icons/volume.png")).toExternalForm()));
        HBox volumeHbox = new HBox(volumeI, volume);
        volumeHbox.setSpacing(10);
        volumeHbox.setAlignment(Pos.CENTER);
        VBox volumeBox = new VBox(volumeHbox, hybridSlider(150));
        volumeBox.setSpacing(5);
        volumeBox.setAlignment(Pos.TOP_CENTER);

        mainMenu.getChildren().addAll(add, all, playlists, settings, volumeBox);
        mainMenu.setSpacing(10);
        mainMenu.setAlignment(Pos.CENTER);
        mainMenu.setStyle("-fx-padding: 10px; -fx-background-color: #404040; -fx-background-radius: 10");

        return mainMenu;
    }

    private VBox lists() {
        VBox mainListsVbox = new VBox();
        this.nameOnScreen.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/labelLarge.css")).toExternalForm());
        mainListsVbox.getChildren().add(this.nameOnScreen);
        mainListsVbox.setAlignment(Pos.TOP_LEFT);
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
}

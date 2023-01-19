import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;

public class FirstUseWindow {

    private BorderPane bp;
    private String path = "";
    private MainLogic m;
    private Stage stage;

    public void start(Stage stage, MainLogic m) {
        this.stage = stage;
        this.m = m;
        this.bp = new BorderPane();
        this.bp.setStyle("-fx-background-color: #5c5c5c");
        this.setUpWindow(stage);

        Scene firstUseScene = new Scene(bp, 600, 350);
        firstUseScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/orangeTheme.css")).toExternalForm());
        this.stage.setTitle("Welcome");
        this.stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        this.stage.setScene(firstUseScene);
        this.stage.showAndWait();
    }

    private void setUpWindow(Stage stage) {
        Label welcomeLabel = new Label("Welcome"), setPathLabel = new Label("Active folder for player data:"), changeLaterLabel = new Label("You can change the folder later in settings"), orLabel = new Label("Set new folder");
        welcomeLabel.getStyleClass().add("label-large");
        welcomeLabel.setStyle("-fx-padding: 30");
        changeLaterLabel.getStyleClass().add("label-small");

        Button okButton = new Button("OK");
        okButton.getStyleClass().add("my-menu-button");
        okButton.setOnMouseEntered(e -> okButton.setStyle("button-color: mouse-color"));
        okButton.setOnMouseExited(e -> okButton.setStyle("button-color: default-color"));

        Button searchButton = new Button("Search PC");
        searchButton.getStyleClass().add("my-menu-button");
        searchButton.setOnMouseEntered(e -> searchButton.setStyle("button-color: mouse-color"));
        searchButton.setOnMouseExited(e -> searchButton.setStyle("button-color: default-color"));

        Label folderPathText = new Label();
        if (!MainLogic.PATH.isEmpty()) {
            folderPathText.setText(MainLogic.PATH);
        }

        okButton.setOnAction(e -> {
            this.path = folderPathText.getText().replace("\\", "/" );
            this.forwardPath();
            this.stage.close();
        });

        searchButton.setOnAction(e -> {
            DirectoryChooser dc = new DirectoryChooser();
            dc.setTitle("Choose folder");
            File selected = dc.showDialog(stage);
            folderPathText.setText(selected.getAbsolutePath());
        });

        HBox welcomeBox = new HBox(welcomeLabel);
        welcomeBox.setAlignment(Pos.CENTER);
        HBox okBox = new HBox(okButton);
        okBox.setAlignment(Pos.CENTER);
        okBox.setPadding(new Insets(10));

        VBox pathBox = new VBox(setPathLabel, folderPathText, orLabel, searchButton, changeLaterLabel);
        pathBox.setAlignment(Pos.CENTER);
        pathBox.setSpacing(10);
        pathBox.setPadding(new Insets(10));

        this.bp.setTop(welcomeBox);
        this.bp.setCenter(pathBox);
        this.bp.setBottom(okBox);
    }
    public void forwardPath() {
        try {
            this.m.setPath(this.path);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

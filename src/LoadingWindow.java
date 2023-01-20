import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.Objects;

public class LoadingWindow {
    private final Stage loadingStage = new Stage();
    private String style;

    public void setStyle(String style) {
        this.style = style;
    }

    public void loading() {
        this.loadingStage.setTitle("Loading");
        this.loadingStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/music.png")).toExternalForm()));
        this.loadingStage.setAlwaysOnTop(true);

        Label loadingLabel = new Label("Loading");
        loadingLabel.getStyleClass().add("label-large");
        loadingLabel.setStyle("-fx-padding: 30");

        ProgressBar pb = new ProgressBar();
        pb.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);

        VBox loadingBox = new VBox(loadingLabel, pb);
        loadingBox.setSpacing(10);
        loadingBox.setPadding(new Insets(10));
        loadingBox.setStyle("-fx-background-color: #5c5c5c");
        loadingBox.setAlignment(Pos.TOP_CENTER);

        Scene loadingScene = new Scene(loadingBox, 400, 200);

        JMetro jMetro = new JMetro(Style.DARK);
        jMetro.setScene(loadingScene);

        switch (style) {
            case "Orange" -> loadingScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/orangeTheme.css")).toExternalForm());
            case "Blue" -> loadingScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/blueTheme.css")).toExternalForm());
            case "Green" -> loadingScene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("stylesheets/greenTheme.css")).toExternalForm());
        }

        this.loadingStage.setScene(loadingScene);
        this.loadingStage.show();
    }

    public void close() {
        this.loadingStage.close();
    }
}

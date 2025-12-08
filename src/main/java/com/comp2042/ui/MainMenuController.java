package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * FXML Controller for MainMenu.
 * Acts as a mediator between the FXML view and navigation logic.
 */
public class MainMenuController {

    @FXML
    private javafx.scene.control.Button playButton;

    @FXML
    private javafx.scene.control.Button controlsButton;

    @FXML
    private javafx.scene.control.Button highScoresButton;

    @FXML
    private javafx.scene.control.Button optionsButton;

    @FXML
    private javafx.scene.control.Button quitButton;
    
    @FXML
    private ImageView backgroundImage;
    
    @FXML
    private StackPane rootStackPane;

    private Stage stage;
    private Runnable onPlayCallback;
    private Runnable onControlsCallback;
    private Runnable onHighScoresCallback;
    private Runnable onOptionsCallback;
    private Runnable onQuitCallback;

    @FXML
    private void initialize() {
        // Bind background image to fill the StackPane
        if (backgroundImage != null && rootStackPane != null) {
            backgroundImage.fitWidthProperty().bind(rootStackPane.widthProperty());
            backgroundImage.fitHeightProperty().bind(rootStackPane.heightProperty());
        }
    }

    @FXML
    private void play() {
        if (onPlayCallback != null) {
            onPlayCallback.run();
        }
    }

    @FXML
    private void controls() {
        if (onControlsCallback != null) {
            onControlsCallback.run();
        }
    }

    @FXML
    private void highScores() {
        if (onHighScoresCallback != null) {
            onHighScoresCallback.run();
        }
    }

    @FXML
    private void options() {
        if (onOptionsCallback != null) {
            onOptionsCallback.run();
        }
    }

    @FXML
    private void quit() {
        if (onQuitCallback != null) {
            onQuitCallback.run();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setOnPlayCallback(Runnable callback) {
        this.onPlayCallback = callback;
    }

    public void setOnControlsCallback(Runnable callback) {
        this.onControlsCallback = callback;
    }

    public void setOnHighScoresCallback(Runnable callback) {
        this.onHighScoresCallback = callback;
    }

    public void setOnOptionsCallback(Runnable callback) {
        this.onOptionsCallback = callback;
    }

    public void setOnQuitCallback(Runnable callback) {
        this.onQuitCallback = callback;
    }
}


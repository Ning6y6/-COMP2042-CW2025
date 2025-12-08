package com.comp2042.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Scene manager for Mode Selection view.
 * Loads FXML and sets up the scene for mode selection.
 * All UI creation and styling is handled by FXML and CSS.
 */
public class ModeSelectionView {

    private ModeSelectionFXController fxController;
    private Runnable onEndlessModeCallback;
    private Runnable onChallengeModeCallback;
    private Runnable onBackCallback;

    public ModeSelectionView() {
    }

    /**
     * Sets callbacks for mode selection actions.
     */
    public void setCallbacks(Runnable onEndlessMode, Runnable onChallengeMode, Runnable onBack) {
        this.onEndlessModeCallback = onEndlessMode;
        this.onChallengeModeCallback = onChallengeMode;
        this.onBackCallback = onBack;
    }

    /**
     * Creates and returns the Mode Selection scene.
     * Loads FXML, injects dependencies, and sets up callbacks.
     */
    public Scene createScene(Stage primaryStage) {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ModeSelectionView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("ModeSelectionView.fxml not found at /fxml/ModeSelectionView.fxml");
            }
            Parent root = loader.load();

            // Get controller from loader
            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("ModeSelectionFXController not found in FXML");
            }

            // Set callbacks
            fxController.setOnEndlessModeCallback(onEndlessModeCallback);
            fxController.setOnChallengeModeCallback(onChallengeModeCallback);
            fxController.setOnBackCallback(onBackCallback);

            // Create Scene
            Scene scene = new Scene(root, com.comp2042.GameConstants.WINDOW_WIDTH, com.comp2042.GameConstants.WINDPW_HEIGHT);
            return scene;

        } catch (IOException e) {
            System.err.println("Failed to load ModeSelectionView.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load ModeSelectionView.fxml", e);
        } catch (Exception e) {
            System.err.println("Error creating Mode Selection scene: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to create Mode Selection scene", e);
        }
    }
}


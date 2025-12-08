package com.comp2042.ui;

import com.comp2042.GameController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsView {

    private final GameController controller;
    private Stage stage;
    private SettingsFXController fxController;

    public SettingsView(GameController controller) {
        this.controller = controller;
    }

    /**
     * Shows the Settings window as a modal dialog.
     * Loads FXML, injects dependencies, and sets up window close handler.
     */
    public void show() {
        try {
            // Load FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsView.fxml"));
            if (loader.getLocation() == null) {
                throw new IOException("SettingsView.fxml not found at /fxml/SettingsView.fxml");
            }
            Parent root = loader.load();
            
            // Get controller from loader
            fxController = loader.getController();
            if (fxController == null) {
                throw new IllegalStateException("SettingsFXController not found in FXML");
            }
            
            // Inject GameController dependency
            fxController.setGameController(controller);
            
            // Create and configure Stage
            stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Settings");
            
            // Create Scene and set it on Stage first (needed for setStage to work)
            Scene scene = new Scene(root, 300, 450);
            stage.setScene(scene);
            
            // Set Stage reference in controller (for window close handling)
            // This must be done after Scene is set
            fxController.setStage(stage);
            
            // Set up window close handler to automatically resume game
            stage.setOnCloseRequest(e -> {
                controller.resumeGame();
            });
            
            // Show the Stage
            stage.show();
            stage.toFront(); // Ensure it's on top
            
        } catch (IOException e) {
            System.err.println("Failed to load SettingsView.fxml: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to load SettingsView.fxml", e);
        } catch (Exception e) {
            System.err.println("Error showing Settings window: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to show Settings window", e);
        }
    }

    public void close() {
        if (stage != null) {
            stage.close();
        }
    }
}
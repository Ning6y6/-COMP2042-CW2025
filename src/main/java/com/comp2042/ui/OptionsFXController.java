package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * FXML Controller for OptionsView.
 * Handles volume adjustment for background music.
 */
public class OptionsFXController {

    @FXML
    private Slider volumeSlider;

    @FXML
    private Label volumeValueLabel;

    @FXML
    private Button closeButton;

    private Stage stage;

    @FXML
    private void initialize() {
        try {
            // Get current volume from AudioManager
            double currentVolume = AudioManager.getInstance().getVolume();
            
            // Set initial slider value
            volumeSlider.setValue(currentVolume);
            updateVolumeLabel(currentVolume);
            
            // Bind volume changes to AudioManager
            volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
                double volume = newVal.doubleValue();
                AudioManager.getInstance().setVolume(volume);
                updateVolumeLabel(volume);
            });
        } catch (Exception e) {
            System.err.println("Error initializing volume slider: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Updates the volume label to show current percentage.
     */
    private void updateVolumeLabel(double volume) {
        int percentage = (int) Math.round(volume * 100);
        volumeValueLabel.setText(percentage + "%");
    }

    /**
     * Sets the Stage reference for window close handling.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}


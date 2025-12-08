package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * OptionsFXController acts as a controller, coordinating between the game model and the view.
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
    /**
     * Performs the initialize operation.
     */
    private void initialize() {
        try {

            double currentVolume = AudioManager.getInstance().getVolume();

            volumeSlider.setValue(currentVolume);
            updateVolumeLabel(currentVolume);

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
     * Updates the volume label.
     *
     * @param volume the volume
     */
    private void updateVolumeLabel(double volume) {
        int percentage = (int) Math.round(volume * 100);
        volumeValueLabel.setText(percentage + "%");
    }

    /**
     * Sets the stage.
     *
     * @param stage the stage
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    /**
     * Handles the close.
     */
    private void handleClose() {
        if (stage != null) {
            stage.close();
        }
    }
}
package com.comp2042.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

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

    private void updateVolumeLabel(double volume) {
        int percentage = (int) Math.round(volume * 100);
        volumeValueLabel.setText(percentage + "%");
    }

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

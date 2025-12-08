package com.comp2042.ui;

import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * NotificationPanel is part of the ui package and encapsulates behaviour related to notification panel.
 */
public class NotificationPanel extends StackPane {

    private static final Duration FADE_IN_TIME = Duration.millis(150);
    private static final Duration VISIBLE_TIME = Duration.millis(350);
    private static final Duration FLOAT_TIME = Duration.millis(600);
    private static final Duration FADE_OUT_TIME = Duration.millis(300);
    private static final double FLOAT_DISTANCE = 40;

    public NotificationPanel(String text) {
        getStyleClass().add("notification-panel");
        setPickOnBounds(false);
        setAlignment(Pos.CENTER);

        Label scoreLabel = new Label(text);
        scoreLabel.getStyleClass().add("notification-label");

        getChildren().add(scoreLabel);
        setOpacity(1);
        setScaleX(0.6);
        setScaleY(0.6);
    }

    /**
     * Performs the play animation operation.
     */
    public void playAnimation() {
        FadeTransition fadeIn = new FadeTransition(FADE_IN_TIME, this);
        fadeIn.setToValue(1.0);

        ScaleTransition popIn = new ScaleTransition(FADE_IN_TIME, this);
        popIn.setToX(1.1);
        popIn.setToY(1.1);

        TranslateTransition floatUp = new TranslateTransition(FLOAT_TIME, this);
        floatUp.setByY(-FLOAT_DISTANCE);

        FadeTransition fadeOut = new FadeTransition(FADE_OUT_TIME, this);
        fadeOut.setToValue(0);

        ScaleTransition shrinkOut = new ScaleTransition(FADE_OUT_TIME, this);
        shrinkOut.setToX(0.8);
        shrinkOut.setToY(0.8);

        SequentialTransition sequence = new SequentialTransition(
                new ParallelTransition(fadeIn, popIn),
                new PauseTransition(VISIBLE_TIME),
                new ParallelTransition(floatUp, fadeOut, shrinkOut)
        );
        sequence.setOnFinished(e -> removeFromParent());
        sequence.play();
    }

    /**
     * Performs the remove from parent operation.
     */
    private void removeFromParent() {
        Parent parent = getParent();
        if (parent instanceof Pane pane) {
            pane.getChildren().remove(this);
        }
    }
}
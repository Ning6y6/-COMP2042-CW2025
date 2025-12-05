package com.comp2042.ui;
import com.comp2042.GameController;
import javafx.animation.*;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class NotificationPanel extends StackPane {

    private static final Duration FADE_IN_TIME = Duration.millis(150);
    private static final Duration VISIBLE_TIME = Duration.millis(350);
    private static final Duration FLOAT_TIME = Duration.millis(600);
    private static final Duration FADE_OUT_TIME = Duration.millis(300);
    private static final double FLOAT_DISTANCE = 40;

    public NotificationPanel(String text) {
        setPickOnBounds(false);
        setAlignment(Pos.CENTER);

        Label scoreLabel = new Label(text);
        scoreLabel.setTextFill(Color.WHITE);
        scoreLabel.setFont(Font.font("Verdana", 28));
        scoreLabel.setStyle("-fx-font-weight: bold; "
                + "-fx-effect: dropshadow(gaussian, black, 6, 0.5, 0, 1);");

        getChildren().add(scoreLabel);
        setOpacity(1);
        setScaleX(0.6);
        setScaleY(0.6);
        playAnimation();

    }

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

        ParallelTransition appear = new ParallelTransition(fadeIn, popIn);
        ParallelTransition disappear = new ParallelTransition(fadeOut, shrinkOut);

        SequentialTransition sequence = new SequentialTransition(
                new ParallelTransition(fadeIn, popIn),
                new PauseTransition(VISIBLE_TIME),
                new ParallelTransition(floatUp, fadeOut, shrinkOut)
        );
        sequence.setOnFinished(e -> removeFromParent());
        sequence.play();
    }

    private void removeFromParent() {
        Parent parent = getParent();
        if (parent instanceof Pane pane) {
            pane.getChildren().remove(this);
        }
    }
}

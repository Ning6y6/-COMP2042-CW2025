package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.GameLoop;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * JavaFXGameLoop is part of the ui package and encapsulates behaviour related to java fx game loop.
 */
public class JavaFXGameLoop implements GameLoop {

    private final Timeline timeline;
    private Runnable tickAction;

    public JavaFXGameLoop() {
        this.timeline = new Timeline();
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        configureTimeline();
    }

    /**
     * Performs the configure timeline operation.
     */
    private void configureTimeline() {

        KeyFrame frame = new KeyFrame(
                Duration.millis(GameConstants.GAME_SPEED_MILLIS),
                e -> {
                    if (tickAction != null) {
                        tickAction.run();
                    }
                }
        );
        this.timeline.getKeyFrames().setAll(frame);
    }

    @Override
    /**
     * Performs the start operation.
     */
    public void start() {
        timeline.play();
    }

    @Override
    /**
     * Performs the stop operation.
     */
    public void stop() {
        timeline.stop();
    }

    @Override
    /**
     * Performs the pause operation.
     */
    public void pause() {
        timeline.pause();
    }

    @Override
    /**
     * Returns whether the running is true.
     *
     * @return the result of the operation
     */
    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }

    @Override
    /**
     * Sets the tick action.
     *
     * @param action the action
     */
    public void setTickAction(Runnable action) {
        this.tickAction = action;
    }

    @Override
    /**
     * Sets the speed.
     *
     * @param millis the millis
     */
    public void setSpeed(double millis) {
        javafx.animation.Timeline.Status previousStatus = timeline.getStatus();

        timeline.stop();
        KeyFrame frame = new KeyFrame(
                Duration.millis(millis),
                e -> {
                    if (tickAction != null) {
                        tickAction.run();
                    }
                }
        );
        this.timeline.getKeyFrames().setAll(frame);

        if (previousStatus == javafx.animation.Timeline.Status.RUNNING ||
                previousStatus == javafx.animation.Timeline.Status.PAUSED) {
            timeline.playFromStart();
        }
    }
}
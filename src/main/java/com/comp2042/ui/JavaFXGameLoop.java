package com.comp2042.ui;

import com.comp2042.GameConstants;
import com.comp2042.GameLoop;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class JavaFXGameLoop implements GameLoop {

    private final Timeline timeline;
    private Runnable tickAction;

    public JavaFXGameLoop() {
        this.timeline = new Timeline();
        this.timeline.setCycleCount(Timeline.INDEFINITE);
        configureTimeline();
    }

    private void configureTimeline() {
        // We set up the keyframe, but the action runs the stored 'tickAction'
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
    public void start() {
        timeline.play();
    }

    @Override
    public void stop() {
        timeline.stop();
    }

    @Override
    public void pause() {
        timeline.pause();
    }

    @Override
    public boolean isRunning() {
        return timeline.getStatus() == Timeline.Status.RUNNING;
    }

    @Override
    public void setTickAction(Runnable action) {
        this.tickAction = action;
    }
    
    @Override
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
        
        // Restart timeline if it was running or paused
        // Reset to beginning of cycle to ensure consistent timing
        if (previousStatus == javafx.animation.Timeline.Status.RUNNING || 
            previousStatus == javafx.animation.Timeline.Status.PAUSED) {
            timeline.playFromStart();
        }
    }
}
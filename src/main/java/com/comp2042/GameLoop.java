package com.comp2042;

public interface GameLoop {
    void start();
    void stop();
    void pause();
    boolean isRunning();

    void setTickAction(Runnable action);
    
    /**
     * Sets the game loop speed in milliseconds.
     * @param millis The duration between ticks in milliseconds.
     */
    void setSpeed(double millis);
}
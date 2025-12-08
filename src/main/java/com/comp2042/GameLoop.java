package com.comp2042;

public interface GameLoop {
    void start();
    void stop();
    void pause();
    boolean isRunning();

    void setTickAction(Runnable action);

    void setSpeed(double millis);
}
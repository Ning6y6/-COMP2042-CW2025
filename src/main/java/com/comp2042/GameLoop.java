package com.comp2042;

/**
 * GameLoop is part of the comp2042 package and encapsulates behaviour related to game loop.
 */
public interface GameLoop {
    /**
     * Performs the start operation.
     */
    void start();
    /**
     * Performs the stop operation.
     */
    void stop();
    /**
     * Performs the pause operation.
     */
    void pause();
    /**
     * Returns whether the running is true.
     *
     * @return the result of the operation
     */
    boolean isRunning();

    /**
     * Sets the tick action.
     *
     * @param action the action
     */
    void setTickAction(Runnable action);

    /**
     * Sets the speed.
     *
     * @param millis the millis
     */
    void setSpeed(double millis);
}
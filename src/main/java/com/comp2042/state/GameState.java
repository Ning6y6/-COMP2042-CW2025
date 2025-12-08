package com.comp2042.state;

import com.comp2042.GameController;

/**
 * GameState defines a state in the game state machine.
 */
public interface GameState {
    /**
     * Handles the input.
     *
     * @param context the context
     * @param action the action
     */
    void handleInput(GameController context, Runnable action);
    /**
     * Updates the state.
     *
     * @param context the context
     */
    void update(GameController context);
    /**
     * Handles the toggle pause.
     *
     * @param context the context
     */
    void handleTogglePause(GameController context);
}
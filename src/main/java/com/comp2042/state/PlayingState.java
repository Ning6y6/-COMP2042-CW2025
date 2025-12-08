package com.comp2042.state;

import com.comp2042.GameController;

/**
 * PlayingState defines a state in the game state machine.
 */
public class PlayingState implements GameState {
    @Override
    /**
     * Handles the input.
     *
     * @param context the context
     * @param action the action
     */
    public void handleInput(GameController context, Runnable action) {
        action.run();
    }

    @Override
    /**
     * Updates the state.
     *
     * @param context the context
     */
    public void update(GameController context) {
        context.performGameLoopStep();
    }

    @Override
    /**
     * Handles the toggle pause.
     *
     * @param context the context
     */
    public void handleTogglePause(GameController context) {
        context.pauseGame();
    }
}
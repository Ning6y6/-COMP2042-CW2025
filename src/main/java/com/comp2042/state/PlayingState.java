package com.comp2042.state;

import com.comp2042.GameController;


public class PlayingState implements GameState {
    @Override
    public void handleInput(GameController context, Runnable action) {
        action.run();
    }

    @Override
    public void update(GameController context) {
        context.performGameLoopStep();
    }

    @Override
    public void handleTogglePause(GameController context) {
        context.pauseGame();
    }
}

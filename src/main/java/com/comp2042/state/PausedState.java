package com.comp2042.state;

import com.comp2042.GameController;

public class PausedState implements GameState {
    @Override
    public void handleInput(GameController context, Runnable action) {
    }

    @Override
    public void update(GameController context) {
    }

    public void handleTogglePause(GameController context) {
        context.resumeGame();
    }
}
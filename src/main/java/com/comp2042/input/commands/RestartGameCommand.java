package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;

public class RestartGameCommand implements GameCommand {

    @Override
    public void execute(GameController controller) {
        controller.restartGame();
    }

    @Override
    public String getName() {
        return "Restart Game";
    }
}
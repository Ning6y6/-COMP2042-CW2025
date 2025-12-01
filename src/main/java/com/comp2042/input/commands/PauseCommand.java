package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;

public class PauseCommand implements GameCommand {
    @Override
    public void execute(GameController controller) {
        controller.pauseGame();
    }
    @Override
    public String getName() {
        return "Pause Game";
    }
}

package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;

public class HardDropCommand implements GameCommand {
    @Override
    public void execute(GameController controller) {
        controller.hardDrop();
    }

    @Override
    public String getName() {
        return "Hard Drop";
    }
}
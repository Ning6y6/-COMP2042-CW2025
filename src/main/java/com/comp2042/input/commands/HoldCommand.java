package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;

public class HoldCommand implements GameCommand {
    @Override
    public void execute(GameController controller) {
        controller.hold();
    }

    @Override
    public String getName() {
        return "Hold Brick";
    }
}
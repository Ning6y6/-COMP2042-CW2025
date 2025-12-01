package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;

public class SoftDropCommand implements GameCommand {
    @Override
    public void execute(GameController controller) {
        controller.softDrop();
    }
    @Override
    public String getName() {
        return "Soft Drop";
    }
}
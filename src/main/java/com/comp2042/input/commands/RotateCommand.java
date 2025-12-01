package com.comp2042.input.commands;

import com.comp2042.GameController;
import com.comp2042.input.GameCommand;

public class RotateCommand implements GameCommand {
    @Override
    public void execute(GameController controller) {
        controller.rotate();
    }
    @Override
    public String getName() {
        return "Rotate";
    }
}
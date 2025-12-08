package com.comp2042.view.theme;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class FontApplier {
    

    public static void applyMainFontToNode(Node node) {
        if (node == null) {
            return;
        }
        
        // Apply font to text-based nodes
        if (node instanceof Label label) {
            // Only apply if not already using a custom font
            Font currentFont = label.getFont();
            if (currentFont == null || currentFont.getFamily().equals("System") || 
                currentFont.getFamily().equals("Verdana") || currentFont.getFamily().equals("Arial")) {
                Font mainFont = FontLoader.getMainFont(currentFont != null ? currentFont.getSize() : 14);
                label.setFont(mainFont);
            }
        } else if (node instanceof Button button) {
            Font currentFont = button.getFont();
            if (currentFont == null || currentFont.getFamily().equals("System") || 
                currentFont.getFamily().equals("Verdana") || currentFont.getFamily().equals("Arial")) {
                Font mainFont = FontLoader.getMainFont(currentFont != null ? currentFont.getSize() : 14);
                button.setFont(mainFont);
            }
        } else if (node instanceof Text text) {
            Font currentFont = text.getFont();
            if (currentFont == null || currentFont.getFamily().equals("System") || 
                currentFont.getFamily().equals("Verdana") || currentFont.getFamily().equals("Arial")) {
                Font mainFont = FontLoader.getMainFont(currentFont != null ? currentFont.getSize() : 14);
                text.setFont(mainFont);
            }
        }
        
        // Recursively apply to children
        if (node instanceof Parent parent) {
            for (Node child : parent.getChildrenUnmodifiable()) {
                applyMainFontToNode(child);
            }
        }
    }
}


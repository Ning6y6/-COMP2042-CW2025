package com.comp2042.view.theme;

import javafx.scene.text.Font;

import java.net.URL;

/**
 * Loads custom fonts for the Gothic theme.
 * Fonts must be loaded programmatically before CSS can reference them.
 */
public class FontLoader {
    
    private static boolean fontsLoaded = false;
    private static String tetrisFontFamily = null;
    private static String mainFontFamily = null;
    private static URL tetrisFontUrl = null;
    private static URL mainFontUrl = null;
    
    /**
     * Loads all custom fonts. Should be called at application startup.
     */
    public static void loadFonts() {
        if (fontsLoaded) {
            return; // Already loaded
        }
        
        // Get font URLs - Using Tetris.ttf for title, main.otf for body text
        tetrisFontUrl = FontLoader.class.getResource("/assets/fonts/Tetris.ttf");
        mainFontUrl = FontLoader.class.getResource("/assets/fonts/main.otf");
        
        // Load Tetris font (for title)
        if (tetrisFontUrl != null) {
            Font tetrisFont = Font.loadFont(tetrisFontUrl.toExternalForm(), 48);
            if (tetrisFont != null) {
                tetrisFontFamily = tetrisFont.getFamily();
                System.out.println("✓ Loaded Tetris font (Tetris.ttf) - Family name: '" + tetrisFontFamily + "'");
            } else {
                System.err.println("✗ Failed to load Tetris.ttf - Font.loadFont returned null");
            }
        } else {
            System.err.println("✗ Tetris.ttf not found at /assets/fonts/Tetris.ttf");
        }
        
        // Load Main font (for body text)
        if (mainFontUrl != null) {
            Font mainFont = Font.loadFont(mainFontUrl.toExternalForm(), 16);
            if (mainFont != null) {
                mainFontFamily = mainFont.getFamily();
                System.out.println("✓ Loaded Main font (main.otf) - Family name: '" + mainFontFamily + "'");
            } else {
                System.err.println("✗ Failed to load main.otf - Font.loadFont returned null");
            }
        } else {
            System.err.println("✗ main.otf not found at /assets/fonts/main.otf");
        }
        
        fontsLoaded = true;
    }
    
    /**
     * Gets the actual font family name for Tetris font.
     */
    public static String getTetrisFontFamily() {
        if (!fontsLoaded) {
            loadFonts();
        }
        return tetrisFontFamily != null ? tetrisFontFamily : "Arial";
    }
    
    /**
     * Gets the actual font family name for Main font.
     */
    public static String getMainFontFamily() {
        if (!fontsLoaded) {
            loadFonts();
        }
        return mainFontFamily != null ? mainFontFamily : "Arial";
    }
    
    /**
     * Gets a Font object for Tetris font at specified size.
     * Uses the loaded font URL to create a new Font instance.
     */
    public static Font getTetrisFont(double size) {
        if (!fontsLoaded) {
            loadFonts();
        }
        
        if (tetrisFontUrl != null) {
            Font font = Font.loadFont(tetrisFontUrl.toExternalForm(), size);
            if (font != null) {
                System.out.println("✓ Created Tetris font at size " + size + " - Family: '" + font.getFamily() + "'");
                return font;
            } else {
                System.err.println("✗ Failed to create Tetris font at size " + size);
            }
        } else {
            System.err.println("✗ Tetris font URL is null");
        }
        
        return Font.font("Arial", size); // Fallback
    }
    
    /**
     * Gets a Font object for Main font at specified size.
     * Uses the loaded font URL to create a new Font instance.
     */
    public static Font getMainFont(double size) {
        if (!fontsLoaded) {
            loadFonts();
        }
        
        if (mainFontUrl != null) {
            Font font = Font.loadFont(mainFontUrl.toExternalForm(), size);
            if (font != null) {
                return font;
            } else {
                System.err.println("✗ Failed to create Main font at size " + size);
            }
        } else {
            System.err.println("✗ Main font URL is null");
        }
        
        return Font.font("Arial", size); // Fallback
    }
}


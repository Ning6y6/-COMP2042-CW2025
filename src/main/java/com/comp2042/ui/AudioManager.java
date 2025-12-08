package com.comp2042.ui;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * Manages background music for the game.
 * Handles loading and playing BGM throughout the application.
 */
public class AudioManager {
    
    private MediaPlayer bgmPlayer;
    private static AudioManager instance;
    private double currentVolume = 0.5; // Default volume (50%)
    
    private AudioManager() {
        // Singleton pattern
    }
    
    public static AudioManager getInstance() {
        if (instance == null) {
            instance = new AudioManager();
        }
        return instance;
    }
    
    /**
     * Starts playing the background music.
     * Loops continuously throughout the game.
     */
    public void playBGM() {
        try {
            // Stop existing player if playing
            stopBGM();
            
            // Load and play BGM
            String bgmPath = getClass().getResource("/assets/audio/bgm.mp3").toExternalForm();
            Media bgmMedia = new Media(bgmPath);
            bgmPlayer = new MediaPlayer(bgmMedia);
            bgmPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            bgmPlayer.setVolume(currentVolume); // Use stored volume
            bgmPlayer.play();
        } catch (Exception e) {
            System.err.println("Failed to load BGM: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stops the background music.
     */
    public void stopBGM() {
        if (bgmPlayer != null) {
            bgmPlayer.stop();
            bgmPlayer.dispose();
            bgmPlayer = null;
        }
    }
    
    /**
     * Pauses the background music.
     */
    public void pauseBGM() {
        if (bgmPlayer != null && bgmPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
            bgmPlayer.pause();
        }
    }
    
    /**
     * Resumes the background music.
     * If the player doesn't exist or is stopped, starts playing from the beginning.
     */
    public void resumeBGM() {
        if (bgmPlayer == null) {
            // Player doesn't exist, start fresh
            playBGM();
        } else {
            MediaPlayer.Status status = bgmPlayer.getStatus();
            if (status == MediaPlayer.Status.PAUSED || status == MediaPlayer.Status.STOPPED) {
                bgmPlayer.play();
            } else if (status == MediaPlayer.Status.READY) {
                // Player is ready but not playing, start it
                bgmPlayer.play();
            }
        }
    }
    
    /**
     * Sets the volume (0.0 to 1.0).
     */
    public void setVolume(double volume) {
        currentVolume = Math.max(0.0, Math.min(1.0, volume));
        if (bgmPlayer != null) {
            bgmPlayer.setVolume(currentVolume);
        }
    }
    
    /**
     * Gets the current volume (0.0 to 1.0).
     */
    public double getVolume() {
        return currentVolume;
    }
}

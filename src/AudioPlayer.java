/**
 * The AudioPlayer class is responsible for managing and playing audio files in the game.
 *
 * This class uses javax.sound.sampled library to handle audio operations.
 *
 * @author Aditya Bankoti, Ekam Singh
 * @version January 2, 2024
 */

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;

public class AudioPlayer {
    private String dir;
    private HashMap<String, Clip> sounds;

    /**
     * Constructs an AudioPlayer with the specified directory containing audio files.
     *
     * @param s The directory path containing audio files.
     */
    public AudioPlayer(String s) {
        dir = s;
        sounds = new HashMap<>();
        for (File f : Utils.getFiles(dir)) {
            try {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(f.toURI().toURL());

                Clip c = AudioSystem.getClip();
                c.open(audioIn);
                String name = f.getName();
                sounds.put(name.substring(0, name.lastIndexOf(".")), c);
            } catch (Exception e) {
                System.out.println("Error reading file: " + e + " " + f);
            }
        }
    }

    /**
     * Plays the specified sound file.
     *
     * @param fileName The name of the sound file to be played.
     */
    public void playSound(String fileName) {
        Clip sound = getSound(fileName);
        if (sound.isRunning()) return;
        if (isSoundDone(fileName) || sound.getMicrosecondPosition() == 0) {
            sound.setMicrosecondPosition(0);
            sound.start();
        }
    }

    /**
     * Checks if the specified sound has finished playing.
     *
     * @param soundName The name of the sound to check.
     * @return true if the sound has finished playing, false otherwise.
     */
    public boolean isSoundDone(String soundName) {
        Clip sound = getSound(soundName);
        return sound.getMicrosecondLength() == sound.getMicrosecondPosition();
    }

    /**
     * Checks if the specified sound is at the start position.
     *
     * @param sound The name of the sound to check.
     * @return true if the sound is at the start position, false otherwise.
     */
    public boolean isSoundStart(String sound) {
        return getSound(sound).getMicrosecondPosition() == 0;
    }

    private Clip getSound(String name) {
        return sounds.get(name);
    }
}

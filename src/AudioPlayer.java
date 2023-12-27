import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.HashMap;

public class AudioPlayer {
    private String dir;
    private HashMap<String, Clip> sounds;
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

    public void playSound(String fileName) {
        Clip sound = getSound(fileName);
        if (isSoundDone(fileName) || sound.getMicrosecondPosition() == 0) {
            sound.setMicrosecondPosition(0);
            sound.start();
        }
    }

    public boolean isSoundDone(String soundName) {
        Clip sound = getSound(soundName);
        return sound.getMicrosecondLength() == sound.getMicrosecondPosition();
    }

    private Clip getSound(String name) {
        return sounds.get(name);
    }
}

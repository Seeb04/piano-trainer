package piano.sound;

import javax.sound.sampled.*;
/*
Responsible for all the samples played from the sounds folder
 */

public class SoundManager {

    public static void playNote(String noteName) {
        try {
            String path = "/sounds/" + noteName + ".wav";
            var inputStream = SoundManager.class.getResourceAsStream(path);

            if (inputStream == null) {
                System.err.println("Sound file not found: " + path);
                return;
            }

            var audioStream = AudioSystem.getAudioInputStream(inputStream);
            var clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

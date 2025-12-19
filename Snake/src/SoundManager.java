import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class SoundManager {

    private Clip backgroundClip;
    private Clip movementClip;

    // Background Music (Looping)
    public void playBackgroundMusic(String resourcePath) {
        try {
            // Stop background sebelumnya jika ada
            if (backgroundClip != null && backgroundClip.isRunning()) {
                backgroundClip.stop();
            }

            URL audioUrl = getClass().getResource(resourcePath);
            if (audioUrl == null) {
                System.err.println("Audio file not found: " + resourcePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioUrl);
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundClip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
        }
    }

    // STOP MOVEMENT SOUND
    public void stopMovementSound() {
        if (movementClip != null) {
            if (movementClip.isRunning()) {
                movementClip.stop();
            }
            movementClip.close();
            movementClip = null;
        }
    }

    // Sound Effect Sekali Main
    public void playSFX(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) return;

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
        } catch (Exception e) {
            // Ignore small errors
        }
    }
}

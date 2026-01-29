package app.display.common.sound;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import app.Configuration;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;

/**
 * Manages loading and caching of sound effects and music.
 * <p>
 * This class provides static methods to retrieve audio clips, ensuring each
 * asset is loaded only once and reused throughout the game. Unless specified,
 * uses the default volumes as defined in {@link Configuration}.
 * 
 * @see Configuration
 */
public class AudioManager {

    private static final String SOUNDS_DIRECTORY = "data/audio/sfx/";
    private static final String MUSIC_DIRECTORY = "data/audio/music/";

    private static HashMap<String, AudioClip> soundEffects = new HashMap<>();
    private static HashMap<String, Media> musicTracks = new HashMap<>();
    private static MediaPlayer musicPlayer;

    /**
     * Prevent instantiation, as this class is intended to be static.
     */
    private AudioManager() {
    }

    /**
     * Plays a sound effect given a file name, at a volume of
     * {@value Configuration#DEFAULT_SOUND_VOLUME}.
     * 
     * @param filename name of the sound including file extension.
     */
    public static void playSoundEffect(String filename) {
        playSoundEffect(filename, Configuration.DEFAULT_SOUND_VOLUME);
    }

    /**
     * Plays a sound effect given a file name and volume between 0 and 1.
     * 
     * @param filename name of the sound including file extension.
     * @param volume   how loud the sound should be between 0 and 1.
     */
    public static void playSoundEffect(String filename, double volume) {
        if (volume <= 0) {
            return;
        }
        try {
            if (!soundEffects.containsKey(filename)) {
                String soundFile = SOUNDS_DIRECTORY + filename;
                if (!new File(soundFile).exists()) {
                    throw new FileNotFoundException();
                }
                AudioClip clip = new AudioClip(new File(soundFile).toURI().toString());
                clip.setVolume(volume);
                soundEffects.put(filename, clip);
                clip.play();
            } else {
                soundEffects.get(filename).play();
            }
        } catch (MediaException | FileNotFoundException mediaException) {
            System.err.println("Error when playing sound: The sound [" + filename + "] does not exist!");
        }
    }

    /**
     * Plays a music track given a file name, at a volume of
     * {@value Configuration#DEFAULT_MUSIC_VOLUME}.
     * 
     * @param filename name of the track including file extension.
     */
    public static void playMusic(String filename) {
        playMusic(filename, Configuration.DEFAULT_MUSIC_VOLUME);
    }

    /**
     * Plays a music track given a file name and a volume between 0 and 1.
     * 
     * @param filename name of the track including file extension.
     * @param volume   how loud the music should be between 0 and 1.
     */
    public static void playMusic(String filename, double volume) {
        if (volume <= 0) {
            return;
        }
        try {
            Media newMusic;
            if (musicTracks.containsKey(filename)) {
                newMusic = musicTracks.get(filename);
            } else {
                String soundFile = MUSIC_DIRECTORY + filename;
                if (!new File(soundFile).exists()) {
                    throw new FileNotFoundException();
                }
                newMusic = new Media(new File(soundFile).toURI().toString());
                musicTracks.put(filename, newMusic);
            }

            if (musicPlayer != null) {
                // Avoid restarting the track
                if (musicPlayer.getMedia().equals(newMusic))
                    return;

                musicPlayer.stop();
                musicPlayer.dispose();
            }

            musicPlayer = new MediaPlayer(newMusic);
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.setVolume(volume);
            musicPlayer.play();
        } catch (MediaException | FileNotFoundException mediaException) {
            System.err.println("Error when playing music: The music [" + filename + "] does not exist!");
        }
    }

    /**
     * Continues a music track if it is currently stopped or paused.
     */
    public static void playMusic() {
        if (musicPlayer != null) {
            musicPlayer.play();
        }
    }

    /**
     * Restarts the music if a track is currently playing.
     */
    public static void restartMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.play();
        }
    }

    /**
     * Stops the music if a track is currently playing. Replaying the music starts
     * from the beginning.
     */
    public static void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    /**
     * Pauses music if a track is currently playing. Calling {@link playMusic}
     * unpauses the music.
     */
    public static void pauseMusic() {
        if (musicPlayer != null) {
            musicPlayer.pause();
        }
    }

    /**
     * Changes the music volume if a track is currently playing.
     * 
     * @param volume how loud the music should be between 0 and 1.
     */
    public static void setMusicVolume(double volume) {
        if (musicPlayer != null) {
            musicPlayer.setVolume(volume);
        }
    }

}

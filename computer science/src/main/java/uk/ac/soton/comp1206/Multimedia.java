package uk.ac.soton.comp1206;

import javafx.application.Platform;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Multimedia {
    private static final Logger logger = LogManager.getLogger(Multimedia.class);
    private static boolean audioEnabled = true;
    private static boolean musicEnabled = true;
    /**
     * getting the resources
     */
    /**
     * the mediaPlayer for background music
     */
    private static MediaPlayer musicPlayer ;
    /**
     * the mediaPlayer for audio
     */
    private static MediaPlayer audioPlayer;

    /**
     * method playing the music
     * @param file the music file
     */
    public static void playMusic(String file){
        if (!musicEnabled){
            return;
        }
        String ToPlay = Multimedia.class.getResource("/music/"+ file).toExternalForm();
        logger.info("start playing music " + file);
        try {
            Platform.runLater( () -> {
                Media media = new Media(ToPlay);
                musicPlayer = new MediaPlayer(media);
                musicPlayer.setAutoPlay(true);
                musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);

                musicPlayer.setOnEndOfMedia(() ->{
                    musicPlayer.play();
                });
            });

        }
        catch (Exception e){
            musicEnabled = false;
            e.printStackTrace();
            logger.error("Unable to play music");
        }
    }
    /**
     * method playing the audio
     * @param file the audio file
     */
    public static void playAudio(String file){
        if (!audioEnabled){
            return;
        }
        String ToPlay = Multimedia.class.getResource("/sounds/"+ file).toExternalForm();
        logger.info("start playing sounds " + file);
        try {
            Platform.runLater(() -> {
                Media media = new Media(ToPlay);
                audioPlayer = new MediaPlayer(media);
                audioPlayer.play();
            });

        }
        catch (Exception e){
            audioEnabled = false;
            e.printStackTrace();
            logger.error("unable to play audio");
        }
    }
}

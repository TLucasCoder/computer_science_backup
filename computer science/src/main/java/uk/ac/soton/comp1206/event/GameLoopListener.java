package uk.ac.soton.comp1206.event;

/**
 * listener listening to the game loop
 */
public interface GameLoopListener {
    /**
     * handling the timer when an event occurred
     * @param duration the duration of the timer
     */
    void gameLoop(int duration);
}

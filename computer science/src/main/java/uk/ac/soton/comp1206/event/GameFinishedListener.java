package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.game.Game;

/**
 * listener for the finish of game
 */
public interface GameFinishedListener {
    /**
     * handling the action after the finish of game
     */

    void gameFinished();
}

package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.component.GameBlockCoordinate;

/**
 * line Cleared Listener listens to the event when line is cleared
 */
public interface LineClearedListener {
    /**
     * handling the event when line is cleared
     * @param gameBlockCoordinates the coordinates of the block going to be cleared
     */
    public void clearingLine(GameBlockCoordinate [] gameBlockCoordinates);
}

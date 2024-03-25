package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.component.GameBlock;

/**
 * listen to the events of the clicking for rotation
 */
public interface RotationClickedListener {
    /**
     * handling the event when a block is clicked for rotation
     */
    void clickForRotation();
}

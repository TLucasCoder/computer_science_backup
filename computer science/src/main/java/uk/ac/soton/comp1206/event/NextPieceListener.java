package uk.ac.soton.comp1206.event;

import uk.ac.soton.comp1206.game.GamePiece;

/**
 * Next Piece listener is used to handle the event when a new piece appeared.
 * It passes the next game piece.
 */
public interface NextPieceListener {
    /**
     * Handling the next game piece
     * @param gamePiece the next game piece
     */
    void nextPieceHandler (GamePiece gamePiece);

}

package uk.ac.soton.comp1206.component;

import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.game.Grid;

/**
 * used to display an upcoming piece in a 3x3 grid
 */
public class PieceBoard extends GameBoard {
    /**
     *  piece board constructor
     * @param cols number of columns
     * @param rows number of rows
     * @param width width of the piece board
     * @param height height of the piece board
     */
    public PieceBoard(int cols, int rows, double width, double height) {
        super(cols, rows, width, height);
    }

    /**
     * setting a piece to display
     * @return
     */
    public void setToDisplay(GamePiece gamePiece){
        for (int i = 0; i< 3 ; i++){
            for (int j = 0; j < 3 ; j++){
                grid.set(i,j,0);
            }
        }
        grid.playPiece(gamePiece,1,1);
        //drawCircle();
        //return blocks;
    }

    /**
     * the method used to draw circle at the pivotal point of the game piece
     */
    public void drawCircle(){
        blocks[1][1].painCircle();
    }



}

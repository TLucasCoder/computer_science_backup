package uk.ac.soton.comp1206.component;

import javafx.animation.AnimationTimer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.canvas.Canvas;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The Visual User Interface component representing a single block in the grid.
 *
 * Extends Canvas and is responsible for drawing itself.
 *
 * Displays an empty square (when the value is 0) or a coloured square depending on value.
 *
 * The GameBlock value should be bound to a corresponding block in the Grid model.
 */
public class GameBlock extends Canvas {

    private static final Logger logger = LogManager.getLogger(GameBlock.class);

    /**
     * The set of colours for different pieces
     */
    public static final Color[] COLOURS = {
            Color.TRANSPARENT,
            Color.DEEPPINK,
            Color.RED,
            Color.ORANGE,
            Color.YELLOW,
            Color.YELLOWGREEN,
            Color.LIME,
            Color.GREEN,
            Color.DARKGREEN,
            Color.DARKTURQUOISE,
            Color.DEEPSKYBLUE,
            Color.AQUA,
            Color.AQUAMARINE,
            Color.BLUE,
            Color.MEDIUMPURPLE,
            Color.PURPLE
    };

    private final GameBoard gameBoard;

    private final double width;
    private final double height;

    /**
     * The column this block exists as in the grid
     */
    private final int x;

    /**
     * The row this block exists as in the grid
     */
    private final int y;
    /**
     * the opacity
     */
    double op;
    /**
     * The value of this block (0 = empty, otherwise specifies the colour to render as)
     */
    private final IntegerProperty value = new SimpleIntegerProperty(0);

    /**
     * Create a new single Game Block
     * @param gameBoard the board this block belongs to
     * @param x the column the block exists in
     * @param y the row the block exists in
     * @param width the width of the canvas to render
     * @param height the height of the canvas to render
     */
    public GameBlock(GameBoard gameBoard, int x, int y, double width, double height) {
        this.gameBoard = gameBoard;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;

        //A canvas needs a fixed width and height
        setWidth(width);
        setHeight(height);

        //Do an initial paint
        paint();

        //When the value property is updated, call the internal updateValue method
        value.addListener(this::updateValue);
    }

    /**
     * making the selected block highlighted when hovering
     */
    public void hightlightWhenHover(){
        var gc = getGraphicsContext2D();
        gc.setFill(Color.web(Color.WHITE.toString(),0.2));
        gc.fillRect(0,0, width, height);
    }
    /**
     * making the selected block changes back to original
     */
    public void returnWhenExit(){
        paint();
    }

    /**
     * pain the center circle of the current piece
     */
    public void painCircle(){
        var gc = getGraphicsContext2D();
        double radius = width/4;
        gc.setFill(Color.LIGHTGRAY);

        gc.fillOval(width /4, height /4, radius * 2, radius * 2);
        //gc.fillArc(width/2, height/2, radius, 0, Math.PI * 2, 1);
    }

    /**
     * When the value of this block is updated,
     * @param observable what was updated
     * @param oldValue the old value
     * @param newValue the new value
     */
    private void updateValue(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        paint();
    }

    /**
     * Handle painting of the block canvas
     */
    public void paint() {
        //If the block is empty, paint as empty
        if(value.get() == 0) {
            paintEmpty();
        } else {
            //If the block is not empty, paint with the colour represented by the value
            paintColor(COLOURS[value.get()]);
        }
    }

    /**
     * Paint this canvas empty
     */
    private void paintEmpty() {
        var gc = getGraphicsContext2D();
        //Clear
        gc.clearRect(0,0,width,height);
        //Fill
        //gc.setFill(Color.web(Color.BLACK.toString(),0.01));
        Color fill = Color.BLACK.deriveColor(0,Color.BLACK.getSaturation(),Color.BLACK.getBrightness(),0.5);
        gc.setFill(fill);
        gc.fillRect(0,0, width, height);

        //Border
        gc.setStroke(Color.GRAY);
        gc.strokeRect(0,0,width,height);
    }

    /**
     * Paint this canvas with the given colour
     * @param colour the colour to paint
     */
    private void paintColor(Paint colour) {
        var gc = getGraphicsContext2D();

        //Clear
        gc.clearRect(0,0,width,height);
        //gc.beginPath();
        //Colour fill


        gc.setFill(colour);
        gc.fillRect(0,0, width, height);
        //gc.setEffect(reflection);

        //Border
        gc.setStroke(Color.BLACK);
        gc.strokeRect(0,0,width,height);
        gc.strokeRect(1,1,width-5,height-5);

        gc.setFill(Color.web(Color.WHITE.toString(),0.5));
        gc.beginPath();
        gc.moveTo(0,0);
        gc.lineTo(0,width);
        gc.lineTo(height,0);
        gc.fill();


        gc.setStroke(Color.LIGHTGRAY);
        gc.strokeLine(1,1,1,height);
        gc.strokeLine(1,1,width,1);
        gc.strokeLine(2,2,2,height);
        gc.strokeLine(2,2,width,2);


        //painCircle();

    }

    /**
     * Get the column of this block
     * @return column number
     */
    public int getX() {
        return x;
    }

    /**
     * Get the row of this block
     * @return row number
     */
    public int getY() {
        return y;
    }

    /**
     * Get the current value held by this block, representing it's colour
     * @return value
     */
    public int getValue() {
        return this.value.get();
    }

    /**
     * Bind the value of this block to another property. Used to link the visual block to a corresponding block in the Grid.
     * @param input property to bind the value to
     */
    public void bind(ObservableValue<? extends Number> input) {
        value.bind(input);
    }

    /**
     * the effect that makes cleared row/column(s) fading out
     */
    public void fadeOut(){
        paintEmpty();
        var gc = getGraphicsContext2D();
        op = 0.7;
        gc.setFill(Color.web(Color.GREENYELLOW.toString(),op));
        gc.fillRect(0,0, width, height);
        AnimationTimer animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                op -= 0.01;
                gc.clearRect(0,0, width, height);
                if (op>=0) {
                    gc.setFill(Color.web(Color.GREENYELLOW.toString(), op));
                    gc.fillRect(0,0, width, height);
                }

                if (op <= 0){
                    paintEmpty();
                    stop();
                }
            }
        };
        animationTimer.start();
    }

}

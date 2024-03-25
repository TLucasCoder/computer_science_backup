package uk.ac.soton.comp1206.game;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;
import uk.ac.soton.comp1206.event.*;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.*;

/**
 * The Game class handles the main logic, state and properties of the TetrECS game. Methods to manipulate the game state
 * and to handle actions made by the player should take place inside this class.
 */
public class Game {

    private static final Logger logger = LogManager.getLogger(Game.class);

    /**
     * Number of rows
     */
    protected final int rows;

    /**
     * Number of columns
     */
    protected final int cols;

    /**
     * The grid model linked to the game
     */
    protected final Grid grid;

    /**
     * keep track of the current piece
     */
    protected GamePiece currentPiece;
    /**
     * keep track of the following piece
     */
    protected GamePiece followingPiece;
    /**
     * Score bindable property
     */
    protected SimpleIntegerProperty score = new SimpleIntegerProperty(0);
    /**
     * level bindable property
     */
    protected SimpleIntegerProperty level = new SimpleIntegerProperty(0);
    /**
     * lives bindable property
     */
    protected SimpleIntegerProperty lives = new SimpleIntegerProperty(3);
    /**
     * multiplier bindable property
     */
    protected SimpleIntegerProperty multiplier = new SimpleIntegerProperty(1);

    protected NextPieceListener nextPieceListener;
    protected LineClearedListener lineClearedListener;
    protected GameLoopListener gameLoopListener;
    protected SuccessPlayListener successPlayListener;
    protected GameFinishedListener gameFinishedListener;
    protected boolean successfulBlockPlaced = false;
    Timer timer;


    /**
     * Create a new game with the specified rows and columns. Creates a corresponding grid model.
     * @param cols number of columns
     * @param rows number of rows
     */
    public Game(int cols, int rows) {
        this.cols = cols;
        this.rows = rows;

        //Create a new grid model to represent the game state
        this.grid = new Grid(cols,rows);
    }

    /**
     * Start the game
     */
    public void start() {
        logger.info("Starting game");
        initialiseGame();

    }

    /**
     * Initialise a new game and set up anything that needs to be done at the start
     */
    public void initialiseGame() {
        logger.info("Initialising game");


        // When the game is initialised, spawn a new GamePiece and set it as the currentPiece
        this.followingPiece = spawnPiece();
        nextPieceHandler(this.followingPiece);
        nextPiece();



            //Timer timer1 =new Timer();

            setTimer();


            while (successfulBlockPlaced ){
                successfulBlockPlaced = false;
                timer.cancel();
            }
    }

    /**
     * set a timer for the game loop
     */
    private void setTimer(){
        timer = new Timer();
        gameLoopListener.gameLoop(getTimeDelay(level.get()));
        var timerTask = new TimerTask(){
            public void run(){
                Platform.runLater(() -> {
                    gameLoopListener.gameLoop(getTimeDelay(level.get()));
                    gameLoop();
                    if (lives.get()<0){
                        stopTimer();
                        timer.cancel();
                        // exit game
                        gameFinished();
                    }
                });

            }
        };
        timer.schedule(timerTask,getTimeDelay(level.get()),getTimeDelay(level.get()));

        setSuccessPlayListener(this::successPlay);
    }

    /**
     * function dealing with the timer a success play occurred.
     */
    private void successPlay(){
        stopTimer();
        setTimer();
    }

    /**
     * stop the timer
     */
    private void stopTimer(){
        timer.cancel();
    }

    /**
     * method when no piece is placed during time period
     */
    public void gameLoop(){
        setLives(lives.getValue() - 1);
        multiplier.set(1);
        nextPiece();
    }

    /**
     * call the method in the game finished listener
     */
    public void gameFinished(){
        if(gameFinishedListener!=null){
            gameFinishedListener.gameFinished();
        }
    }

    /**
     * set the game finished listener
     * @param listener game finished listener
     */
    public void setGameFinishedListener(GameFinishedListener listener){
        gameFinishedListener = listener;
    }
    /**
     * set the game loop listener
     * @param gameLoopListener game loop listener
     */
    public void setGameLoopListener(GameLoopListener gameLoopListener) {
        this.gameLoopListener = gameLoopListener;
    }

    /**
     * set the success play listener
     * @param listener success play listener
     */
    public void setSuccessPlayListener(SuccessPlayListener listener){
        successPlayListener = listener;
    }


    /**
     * Handle what should happen when a particular block is clicked
     * @param gameBlock the block that was clicked
     */
    public void blockClicked(GameBlock gameBlock) {
        //Get the position of this block
        int x = gameBlock.getX();
        int y = gameBlock.getY();
        // check if the piece can be played
        logger.info("current piece is: "+currentPiece.getValue());
        var check = grid.playPiece(currentPiece,x,y);
        if (check==1){
            logger.info("success!");
            successPlayListener.successPlay();
            successfulBlockPlaced = true;
            afterPiece();
            nextPiece();
        }
    }

    /**
     * Get the grid model inside this game representing the game state of the board
     * @return game grid model
     */
    public Grid getGrid() {
        return grid;
    }

    /**
     * Get the number of columns in this game
     * @return number of columns
     */
    public int getCols() {
        return cols;
    }

    /**
     * Get the number of rows in this game
     * @return number of rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * A method to create a random piece
     * @return a newly created gamePiece of random type
     */
    public GamePiece spawnPiece(){
        logger.info("new gamePiece spawned");
        Random random = new Random();
        int temp = random.nextInt(15);
        return GamePiece.createPiece(temp);
    }

    /**
     * Replace the current piece with a new piece
     */
    public void nextPiece(){
        logger.info("next piece");
        logger.info("current piece: " + this.currentPiece);
        this.currentPiece = this.followingPiece;
        this.followingPiece = spawnPiece();
        logger.info("Next piece: " + this.followingPiece);

        nextPieceHandler(this.followingPiece);


    }

    /**
     * logic to handle the clearance of lines
     *  clear any full vertical/horizontal lines that have been made
     */
    public void afterPiece()  {
        var numberOfBlockCleared = 0;
        var numberOfLineCLeared = 0;
        ArrayList<GameBlockCoordinate> coordinates = new ArrayList<>();

        ArrayList<Integer> Col_to_Clear = new ArrayList<>();
        ArrayList<Integer> Row_to_Clear = new ArrayList<>();
        // finding which column to be cleared.
        for (int i = 0 ; i<cols;i++){
            var temp = 0;
            for (int j = 0; j < rows;j++){
                if (grid.get(i,j)!=0){
                    temp++;
                }
            }
            if (temp==5){
                Col_to_Clear.add(i);
                numberOfLineCLeared++;
            }
        }
        // finding which row to be cleared.
        for (int i = 0 ; i<cols;i++){
            var temp = 0;
            for (int j = 0; j < rows;j++){
                if (grid.get(j,i)!=0){
                    temp++;

                }
            }
            if (temp==5){
                Row_to_Clear.add(i);
                numberOfLineCLeared++;
            }
        }

        if (numberOfLineCLeared>0) {
            Multimedia.playAudio("clear.wav");
            // clearing columns
            for (int itr : Col_to_Clear) {
                for (int i = 0; i < rows; i++) {
                    if (grid.get(itr, i) != 0) {
                        numberOfBlockCleared++;
                    }
                    grid.set(itr, i, 0);
                    coordinates.add(new GameBlockCoordinate(itr,i));
                }
            }
            // clearing rows
            for (int itr : Row_to_Clear) {
                for (int i = 0; i < cols; i++) {
                    if (grid.get(i, itr) != 0) {
                        numberOfBlockCleared++;
                    }
                    grid.set(i, itr, 0);
                    coordinates.add(new GameBlockCoordinate(i,itr));
                }
            }
        }
        // call the score method
        score(numberOfLineCLeared,numberOfBlockCleared);
        level();
        multiplier(multiplier.get(), numberOfLineCLeared>0);
        clearingLine(coordinates.toArray(new GameBlockCoordinate[coordinates.size()] ));

    }

    /**
     * set the score property
     * @param score the score input
     */
    public void setScore(int score) {
        this.score.set(score);
    }

    /**
     * get the score property
     * @return score
     */
    public SimpleIntegerProperty getScore(){
        return score;
    }
    /**
     * set the level property
     * @param level the score input
     */
    public void setLevel(int level) {
        this.level.set(level);
    }

    /**
     * get the level property
     * @return level
     */
    public SimpleIntegerProperty getLevel(){
        return level;
    }

    /**
     * set the number of lives
     * @param lives the number of lives input
     */
    public void setLives(int lives) {
        this.lives.set(lives);
    }
    /**
     * get the lives property
     * @return lives
     */
    public SimpleIntegerProperty getLives(){
        return lives;
    }
    /**
     * set the times of multiplier
     * @param multiplier the times of multiplier
     */
    public void setMultiplier(int multiplier) {
        this.multiplier.set(multiplier);
    }
    /**
     * get the times of multiplier
     * @return multiplier
     */
    public SimpleIntegerProperty getMultiplier(){
        return multiplier;
    }

    /**
     * calculate the score
     * @param numberOfLines number of line cleared
     * @param numberOfBlocks number of block cleared
     */
    public void score(int numberOfLines, int numberOfBlocks){
        int tempScore = numberOfLines * numberOfBlocks *10 * multiplier.get() + score.get();
        setScore(tempScore);
        logger.info("the score has been set to: " + getScore());
    }

    /**
     * calculate the multiplier
     * @param multi the orginal multiplier
     * @param lineClearedCheck see if there is line cleared
     */
    public void multiplier (int multi, boolean lineClearedCheck){
        if (lineClearedCheck){
            setMultiplier(multi+1);
        }
        else{
            setMultiplier(1);
        }
    }

    /**
     * calculate the level
     */
    public void level(){
        setLevel(score.get() / 1000);
    }

    /**
     * Set the listener to handle an event when next block is generated
     * @param listener listener to add
     */
    public void setNextPieceListener(NextPieceListener listener){
        logger.info("setNextPieceListener triggered");
        this.nextPieceListener = listener;
    }

    /**
     * calling method in the next piece listener
     * @param gamePiece game piece
     */
    public void nextPieceHandler(GamePiece gamePiece){
        if (nextPieceListener!=null){
            nextPieceListener.nextPieceHandler(gamePiece);
        }
    }

    /**
     * set the line cleared listener
     * @param listener line cleared listener
     */
    public void setLineClearedListener(LineClearedListener listener) {
        this.lineClearedListener = listener;
    }

    /**
     * call the method in the line cleared listener
     * @param gameBlockCoordinates the collection of game block coordinates
     */
    public void clearingLine(GameBlockCoordinate[] gameBlockCoordinates){
        if (lineClearedListener!=null){
            lineClearedListener.clearingLine(gameBlockCoordinates);
        }
    };

    /**
     * rotate the current game piece
     * @param piece the game piece to be rotated
     */
    public void rotateCurrentPiece(GamePiece piece){
        Multimedia.playAudio("rotate.wav");
        piece.rotate();
    }

    /**
     * method to swap the current and following piece
     */
    public void swapCurrentPiece(){
        Multimedia.playAudio("transition.wav");
        var temp = this.followingPiece;
        this.followingPiece = this.currentPiece;
        this.currentPiece = temp;

    }

    /**
     * Calculate the delay at the maximum of either 2500 milliseconds or 12000 - 500 * the current level
     */
    public int getTimeDelay(int currentLevel){
        int delay = 12000 - 500 * currentLevel;
        if (delay > 2500){
            return delay;
            //for testing only
            //return 500;
        }
        // 2500 millisecond is the lowerest delay possible
        delay = 2500;
        return delay;
    }

}

package uk.ac.soton.comp1206.scene;

import javafx.animation.FillTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
// need to change dependencies
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.component.GameBlock;
import uk.ac.soton.comp1206.component.GameBlockCoordinate;
import uk.ac.soton.comp1206.component.GameBoard;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.event.NextPieceListener;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * The Single Player challenge scene. Holds the UI for the single player challenge mode in the game.
 */
public class ChallengeScene extends BaseScene {

    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    protected Game game;
    protected PieceBoard piece = new PieceBoard(3,3,gameWindow.getWidth()/8,gameWindow.getWidth()/8); ;
    protected PieceBoard piece_2 = new PieceBoard(3,3,gameWindow.getWidth()/10,gameWindow.getWidth()/10);
    GameBoard board ;
    protected BorderPane mainPane = new BorderPane();
    protected GamePiece currentGamePiece ;
    protected GamePiece nextGamePiece ;
    /**
     * coordinate_x is the row coordinate
     */
    protected int coordinate_x = -1;
    /**
     * coordinate_y is the column coordinate
     */
    protected int coordinate_y = -1;
    protected SimpleIntegerProperty highScore = new SimpleIntegerProperty();




    /**
     * Create a new Single Player challenge scene
     * @param gameWindow the Game Window
     */
    public ChallengeScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Challenge Scene");
    }

    /**
     * Build the Challenge window
     */
    @Override
    public void build()  {
        logger.info("Building " + this.getClass().getName());

        setupGame();
        try {
            getHighScore();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var challengePane = new StackPane();
        challengePane.setMaxWidth(gameWindow.getWidth());
        challengePane.setMaxHeight(gameWindow.getHeight());
        challengePane.getStyleClass().add("challenge-background");
        root.getChildren().add(challengePane);


        challengePane.getChildren().add(mainPane);

        board = new GameBoard(game.getGrid(),gameWindow.getWidth()/2,gameWindow.getWidth()/2);
        mainPane.setCenter(board);


        var score = new Text();
        score.textProperty().bind(game.getScore().asString());

        mainPane.getChildren().add(score);
        score.getStyleClass().add("score");

        var lives = new Text();
        lives.textProperty().bind(game.getLives().asString());
        lives.getStyleClass().add("lives");


        var level = new Text();
        level.textProperty().bind(game.getLevel().asString());
        level.getStyleClass().add("level");

        var multiplier = new Text();
        multiplier.textProperty().bind(game.getMultiplier().asString());
        multiplier.getStyleClass().add("multiplier");
        //multiplier.setAlignment(TextAlignment.CENTER);

        var pane_top = new Pane();
        mainPane.setTop(pane_top);


        var pane_test = new VBox();
        mainPane.setRight(pane_test);


        // score
        var pane_score = new VBox();
        var score_title = new Text("Score");
        score_title.getStyleClass().add("title");
        pane_score.getChildren().addAll(score_title,score);
        pane_score.setSpacing(10);
        pane_score.setAlignment(Pos.CENTER);
        pane_top.getChildren().add(pane_score);
        pane_score.setLayoutX(10);
        pane_score.setLayoutY(10);

        // highscore
        var highScore_pane = new VBox();
        var highScore_display = new Text();
        highScore_display.textProperty().bind(highScore.asString());
        highScore_display.getStyleClass().add("score");
        var highScore_title = new Text("High Score");
        highScore_title.getStyleClass().add("title");
        highScore_pane.setSpacing(10);
        highScore_pane.setAlignment(Pos.CENTER);
        highScore_pane.getChildren().addAll(highScore_title,highScore_display);
        pane_top.getChildren().add(highScore_pane);
        highScore_pane.setLayoutX(570);
        highScore_pane.setLayoutY(10);

        // title
        var title_Scene = new Text("Challenge mode");
        pane_top.getChildren().add(title_Scene);
        title_Scene.setLayoutX(gameWindow.getWidth()/4);
        title_Scene.setLayoutY(50);
        title_Scene.getStyleClass().add("title");

        // lives
        var lives_pane = new VBox();
        var lives_title = new Text("Lives");
        lives_title.getStyleClass().add("title");
        lives_pane.getChildren().addAll(lives_title,lives);
        lives_pane.setSpacing(10);
        lives_pane.setAlignment(Pos.CENTER);

        //level
        var level_pane = new VBox();
        var level_title = new Text("Level");
        level_title.getStyleClass().add("title");
        level_pane.getChildren().addAll(level_title,level);
        level_pane.setSpacing(10);
        level_pane.setAlignment(Pos.CENTER);




        var pane_word = new VBox();
        pane_word.getChildren().addAll(lives_pane,level_pane);

        pane_test.getChildren().addAll(pane_word,piece,piece_2);
        pane_test.setAlignment(Pos.CENTER);
        pane_test.setSpacing(20);
        pane_test.setPadding(new Insets(10,10,10,10));

        //Handle block on gameboard grid being clicked
        board.setOnBlockClick(this::blockClicked);
        board.setOnClickedForRotation(this::clickForRotation);
        piece.setOnClickedForRotation(this::clickForRotation);
        //Handle the display the next piece
        game.setNextPieceListener(this::nextPieceHandler);
        game.setLineClearedListener(this::clearingLine);
        game.setGameLoopListener(this::gameLoop);
        game.setGameFinishedListener(this::gameFinished);
        //mainPane.setAlignment(pane_test,Pos.TOP_LEFT);

    }

    /**
     * Handle when a block is clicked
     * @param gameBlock the Game Block that was clicked
     */
    private void blockClicked(GameBlock gameBlock) {
        game.blockClicked(gameBlock);
    }

    private void clickForRotation(){
        game.rotateCurrentPiece(currentGamePiece);
        this.piece.setToDisplay(currentGamePiece);
        piece.drawCircle();

    }

    /**
     * Setup the game object and model
     */
    public void setupGame() {
        logger.info("Starting a new challenge");

        //Start new game
        game = new Game(5, 5);
    }

    /**
     * Initialise the scene and start the game
     */
    @Override
    public void initialise() {
        logger.info("Initialising Challenge");
        Multimedia.playMusic("game.wav");
        board.setOnMouseEntered( e -> {
            if (coordinate_x>-1 ||coordinate_y > -1 ) {
                board.getBlock(coordinate_x, coordinate_y).returnWhenExit();
                coordinate_x = -1;
                coordinate_y = -1;
            }
        });
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case ESCAPE :
                    // return
                    gameWindow.startMenu();
                    break;
                case R,SPACE:
                    // swap
                    Multimedia.playAudio("rotate.wav");
                    game.swapCurrentPiece();
                    var temp = currentGamePiece;
                    currentGamePiece = nextGamePiece;
                    nextGamePiece = temp;
                    this.piece.setToDisplay(currentGamePiece);
                    piece.drawCircle();
                    this.piece_2.setToDisplay(nextGamePiece);
                    break;
                case A, LEFT, S, DOWN ,W, UP, D, RIGHT:

                        // control
                        if (coordinate_x == -1 || coordinate_y == -1 ){
                            coordinate_x = 0;
                            coordinate_y = 0;
                        }
                        else {
                            board.getBlock(coordinate_x,coordinate_y).returnWhenExit();
                            switch (keyEvent.getCode()) {
                                case A, LEFT:
                                    if (coordinate_x > 0){
                                        coordinate_x = (coordinate_x - 1);
                                    }
                                    break;
                                case S, DOWN:
                                    if (coordinate_y < game.getRows()-1){
                                        coordinate_y = (coordinate_y + 1) ;
                                    }
                                    break;
                                case W, UP:

                                    if (coordinate_y > 0){
                                        coordinate_y = (coordinate_y - 1) ;
                                    }
                                    break;
                                case D, RIGHT:
                                    if(coordinate_x < game.getCols()-1){
                                        coordinate_x = (coordinate_x + 1);
                                    }
                                    break;
                            }
                        }
                        board.getBlock(coordinate_x,coordinate_y).hightlightWhenHover();
                        logger.info("coordinate x: " + coordinate_x + ", coordinate y: " + coordinate_y) ;


                    break;
                case ENTER, X:

                    if (coordinate_x!=-1 && coordinate_y!=-1){
                        game.blockClicked(board.getBlock(coordinate_x,coordinate_y));
                    }
                    break;
                case OPEN_BRACKET, Q, Z:
                    game.rotateCurrentPiece(currentGamePiece);
                    game.rotateCurrentPiece(currentGamePiece);
                    game.rotateCurrentPiece(currentGamePiece);
                    this.piece.setToDisplay(currentGamePiece);
                    piece.drawCircle();
                    break;
                case CLOSE_BRACKET, E, C:
                    game.rotateCurrentPiece(currentGamePiece);
                    this.piece.setToDisplay(currentGamePiece);
                    piece.drawCircle();
                };
            }
        );
        game.start();
    }

    /**
     * handling the next piece
     * @param gamePiece the gamePiece to display
     */
    public void nextPieceHandler(GamePiece gamePiece){
        if (nextGamePiece==null){
            this.piece_2.setToDisplay(gamePiece);
            nextGamePiece = gamePiece;
        }
        else {
            //this.piece = this.piece_2;
            this.piece.setToDisplay(nextGamePiece);
            piece.drawCircle();
            this.piece_2.setToDisplay(gamePiece);
            currentGamePiece = nextGamePiece;
            nextGamePiece = gamePiece;
        }

    }

    /**
     * method to produce the clearing line animation
     * @param gameBlockCoordinates collection of game block coordinates
     */
    public void clearingLine(GameBlockCoordinate [] gameBlockCoordinates){
        logger.info("clearing line animation");
        board.fadeOut(gameBlockCoordinates);
    }

    /**
     * animation for game loop
     */
    public void gameLoop(int duration){
        /*
        Timeline timeline = new Timeline();
        Rectangle rectBasedTimeLine = new Rectangle(600,50);
        timeline.getKeyFrames().add(Duration.millis(duration),new keyValue());

         */
        final Rectangle rectBasicTimeline = new Rectangle(gameWindow.getWidth(),20);

        rectBasicTimeline.setFill(Color.RED);
        mainPane.setBottom(rectBasicTimeline);

        final Timeline timeline = new Timeline();
        //timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        final KeyValue kv = new KeyValue(rectBasicTimeline.widthProperty(), 0);
        final KeyFrame kf = new KeyFrame(Duration.millis(duration), kv);
        FillTransition ft_1 = new FillTransition(Duration.millis(duration/2), rectBasicTimeline, Color.GREEN, Color.YELLOW);
        FillTransition ft_2 = new FillTransition(Duration.millis(duration/2), rectBasicTimeline, Color.YELLOW, Color.RED);
        ft_1.setOnFinished(actionEvent -> ft_2.play());
        timeline.getKeyFrames().add(kf);
        timeline.play();
        ft_1.play();
    }
    /**
     * method listen to the game finished listener
     */
    public void gameFinished(){
        gameWindow.startScores(game);
    }

    /**
     * method displaying the high score
     * @throws Exception the URI may produce NullPointerException
     */
    private void getHighScore() throws Exception {
        File file = new File(ScoresScene.class.getResource("/TextFile/score.txt").toURI());
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String captured;
        ArrayList<Pair<String,Integer>> temp_List = new ArrayList<>();
        while ((captured = bf.readLine())!= null){
            String nameFromFile = captured.substring(0,captured.indexOf(":"));
            String scoreFromFile = captured.substring(captured.indexOf(":")+1);
            scoreFromFile = scoreFromFile.replace(" ", "");
            temp_List.add(new Pair<>(nameFromFile,Integer.parseInt(scoreFromFile)));
        }
        Collections.sort(temp_List, Comparator.comparing(p -> -p.getValue()));
        highScore.set(temp_List.get(0).getValue());
    }


}

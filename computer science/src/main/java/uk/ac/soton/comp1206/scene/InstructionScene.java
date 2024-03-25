package uk.ac.soton.comp1206.scene;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.game.GamePiece;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

public class InstructionScene extends BaseScene{

    private static final Logger logger = LogManager.getLogger(InstructionScene.class);
    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public InstructionScene(GameWindow gameWindow) {
        super(gameWindow);
        logger.info("Creating Instruction Scene");
    }
    /**
     * initialise the instruction scene
     */
    @Override
    public void initialise() {
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case ESCAPE -> gameWindow.startMenu();
            }
        });
    }

    /**
     * build the instruction scene
     */
    @Override
    public void build() {
        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());
        var stackPane = new StackPane();
        stackPane.getStyleClass().add("instructions-background");
        root.getChildren().add(stackPane);
        var borderPane = new BorderPane();
        stackPane.getChildren().add(borderPane);

        var image = new ImageView(new Image(this.getClass().getResource("/images/Instructions.png").toExternalForm()));
        var pane = new Pane();
        image.setPreserveRatio(true);
        image.setFitWidth(gameWindow.getWidth()/(1.85));
        image.setFitHeight(gameWindow.getHeight()/(1.85));

        borderPane.setCenter(pane);
        pane.getChildren().add(image);
        image.setX(180);
        image.setY(100);
        var grid_pane = new GridPane();
        int count = 0;
        for (int i = 0; i < 15; i++){
            count = i / 5;
            PieceBoard pieceBoard = new PieceBoard(3,3,gameWindow.getWidth()/12,gameWindow.getHeight()/12);
            pieceBoard.setToDisplay(GamePiece.createPiece(i));
            /*
            for (int j=0; j< 2 ;j++ ){
                pieceBoard.getColumnConstraints().add(new ColumnConstraints(50));
                pieceBoard.getRowConstraints().add(new RowConstraints(50));
            }

             */
            grid_pane.add(pieceBoard,i - 5 * count,count);
        }

        grid_pane.setHgap(10); //horizontal gap in pixels => that's what you are asking for
        grid_pane.setVgap(10); //vertical gap in pixels
        var pane_2 = new Pane();


        borderPane.setBottom(pane_2);
        pane_2.getChildren().add(grid_pane);
        //grid_pane.setMaxSize(gameWindow.getWidth()/3,gameWindow.getHeight()/3);
        /*
        for (int i = 0; i< 5;i++){
            grid_pane.getColumnConstraints().add(new ColumnConstraints(150));
        }
        for (int i = 0; i< 3;i++){
            grid_pane.getRowConstraints().add(new RowConstraints(150));
        }

         */
        grid_pane.setPrefWidth(200);
        grid_pane.setPrefHeight(200);

        grid_pane.setLayoutX(210);
        grid_pane.setLayoutY(400);





    }
}

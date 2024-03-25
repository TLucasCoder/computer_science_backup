package uk.ac.soton.comp1206.scene;

import javafx.animation.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.App;
import uk.ac.soton.comp1206.Multimedia;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import static javafx.scene.input.KeyCode.*;

/**
 * The main menu of the game. Provides a gateway to the rest of the game.
 */
public class MenuScene extends BaseScene {
    boolean animate;
    private static final Logger logger = LogManager.getLogger(MenuScene.class);

    /**
     * Create a new menu scene
     * @param gameWindow the Game Window this will be displayed in
     */
    public MenuScene(GameWindow gameWindow,int check) {
        super(gameWindow);
        logger.info("check = "+check);
        if (check ==1){
            animate = true;
        }
        else {
            animate = false;
        }
        logger.info("Creating Menu Scene");
    }

    /**
     * Build the menu layout
     */
    @Override
    public void build()  {

        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        if(animate){
            var startPane = new StackPane();
            var image_0 = new ImageView(new Image(this.getClass().getResource("/images/ECSGames.png").toExternalForm()));
            startPane.getChildren().add(image_0);
            startPane.getStyleClass().add("intro");
            image_0.setPreserveRatio(true);
            image_0.setFitWidth(gameWindow.getWidth()/2);
            root.getChildren().add(startPane);
            FadeTransition ft = new FadeTransition(Duration.millis(3000), startPane);
            ft.setFromValue(0);
            ft.setToValue(1);
            ft.setCycleCount(Animation.INDEFINITE);
            ft.setAutoReverse(true);

            ft.play();
            //root.getChildren().remove(startPane);
        }
        try {
            Thread.sleep(2000);
        }
        catch (Exception e){
            e.printStackTrace();
        }



        //root.getChildren().add(startPane);


        var menuPane = new StackPane();
        menuPane.setMaxWidth(gameWindow.getWidth());
        menuPane.setMaxHeight(gameWindow.getHeight());
        //menuPane.getChildren().add(image_0);
        root.getChildren().add(menuPane);

        menuPane.getStyleClass().add("menu-background");

        var mainPane = new BorderPane();
        menuPane.getChildren().add(mainPane);
        /*
        //Awful title
        var title = new Text("TetrECS");
        title.getStyleClass().add("title");
        mainPane.setTop(title);
*/


        var image = new ImageView(new Image(this.getClass().getResource("/images/TetrECS.png").toExternalForm()));
        var pane = new Pane();
        image.setPreserveRatio(true);
        image.setFitWidth(gameWindow.getWidth()/2);
        mainPane.setTop(pane);
        pane.getChildren().add(image);
        image.setX(gameWindow.getWidth()/4);
        image.setY(100);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.ZERO, new KeyValue(image.opacityProperty(), 1.0)),
                new KeyFrame(Duration.seconds(2), new KeyValue(image.opacityProperty(), 0.0))
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.setAutoReverse(true);
        timeline.play();

        //For now, let us just add a button that starts the game. I'm sure you'll do something way better.
        var vbox = new VBox();

        var button_1 = new Button("Play Single Player");
        var button_2 = new Button("Play Multiplayer");
        var button_3 = new Button("Instructions");
        var button_4 = new Button("Exit");
        vbox.getChildren().addAll(button_1,button_2,button_3,button_4);
        mainPane.setBottom(vbox);

        button_1.getStyleClass().add("menuItem");
        button_2.getStyleClass().add("menuItem");
        button_3.getStyleClass().add("menuItem");
        button_4.getStyleClass().add("menuItem");




        vbox.getStyleClass().add("menu");

        vbox.setOnMouseEntered(e -> {
            vbox.getStyleClass().add("menuItem:hover");
        });


        //mainPane.setCenter(button_1);

        //Bind the button action to the startGame method in the menu
        button_1.setOnAction(this::startGame);
        button_2.setOnAction(this::goMultiplayer);
        button_3.setOnAction(this::goInstruction);
        button_4.setOnAction(this::goExit);


    }

    /**
     * Initialise the menu
     */
    @Override
    public void initialise() {
        // play menu Music
        Multimedia.playMusic("menu.mp3");
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()){
                case ESCAPE -> App.getInstance().shutdown();
            }
        });

    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    private void startGame(ActionEvent event) {
        gameWindow.startChallenge();
    }

    /**
     * Handle when the Start Game button is pressed
     * @param event event
     */
    public void goInstruction(ActionEvent event){
        gameWindow.startInstruction();
    }

    public void dofade(StackPane image){
        logger.info("doing Transition");
        FadeTransition fadeTransition = new FadeTransition(new Duration(2000),image);
        fadeTransition.setFromValue(1);
        fadeTransition.setToValue(0);
        fadeTransition.play();

    }
    public void goExit(ActionEvent event){
        App.getInstance().shutdown();
    }

    public void goMultiplayer(ActionEvent event){
        gameWindow.startMulti();
    }

}

package uk.ac.soton.comp1206.scene;

import javafx.application.Platform;
import javafx.application.Preloader;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.event.CommunicationsListener;
import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import java.nio.channels.Channels;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class LobbyScene extends BaseScene {
    private static final Logger logger = LogManager.getLogger(MenuScene.class);
    private Communicator communicator;
    private ArrayList<String> channel_list = new ArrayList<>();
    private ArrayList<Button> channel_button = new ArrayList<>();
    private ArrayList<CommunicationsListener> communicationsListeners = new ArrayList<>();

    private VBox vbox_1 = new VBox();
    private VBox vbox_2 = new VBox();
    private VBox gameBox = new VBox();
    TextField createGame = new TextField();
    Text title_game = new Text("hi");
    boolean check_quit = false;
    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public LobbyScene(GameWindow gameWindow) {
        super(gameWindow);
    }

    /**
     * initialise the lobby scene
     */

    @Override
    public void initialise() {
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case ESCAPE:
                    // return
                    gameWindow.startMenu();
                    check_quit = true;
                    break;
            }
        });
    }
    /**
     * build the lobby scene
     */
    @Override
    public void build() {
        communicator = gameWindow.getCommunicator();
        logger.info("Building " + this.getClass().getName());

        root = new GamePane(gameWindow.getWidth(),gameWindow.getHeight());

        var lobbyPane = new StackPane();
        lobbyPane.setMaxWidth(gameWindow.getWidth());
        lobbyPane.setMaxHeight(gameWindow.getHeight());
        lobbyPane.getStyleClass().add("challenge-background");
        var borderPane = new BorderPane();
        lobbyPane.getChildren().add(borderPane);
        root.getChildren().add(lobbyPane);

        var pane = new Pane();
        borderPane.setTop(pane);
        var title = new Text("Multi-player");
        title.getStyleClass().add("title");
        pane.getChildren().add(title);
        title.setLayoutX(250);
        title.setLayoutY(50);
        VBox vbox = new VBox();;
        pane.getChildren().add(vbox);
        vbox.setLayoutX(10);
        vbox.setLayoutY(50);

        pane.getChildren().add(gameBox);

        //create window for chat
        gameBox.setLayoutX(300);
        gameBox.setLayoutY(100);
        gameBox.setMinWidth(500);
        gameBox.setMaxWidth(500);
        gameBox.setMinHeight(600);
        gameBox.setMaxHeight(600);
        gameBox.setVisible(false);

        title_game.getStyleClass().add("heading");
        gameBox.getChildren().add(title_game);
        var pane_2 = new Pane();
        pane_2.getStyleClass().add("gameBox");
        pane_2.setMinHeight(400);
        //pane_2.setMaxSize(300,300);
        //pane_2.setLayoutX(400);
        //pane_2.setLayoutY(100);
        gameBox.getChildren().add(pane_2);
        var leaveButton = new Button("Leave game");
        //leaveButton.getStyleClass().add("channelItem");
        gameBox.getChildren().add(leaveButton);
        leaveButton.setOnMouseClicked(e ->{
            communicator.send("PART");
            gameBox.setVisible(false);
        });
        //leaveButton.setLayoutX(600);
        //leaveButton.setLayoutY(300);




        // the vbox for creation of game or join game

        vbox_1.setSpacing(10);
        vbox_2.setSpacing(10);
        var currentGame = new Text("Current Game");
        currentGame.getStyleClass().add("heading");
        vbox.getChildren().addAll(currentGame,vbox_1,vbox_2);
        vbox.setSpacing(30);
        var hostGame = new Button("Host new game");

        createGame.visibleProperty().set(false);

        vbox_1.getChildren().addAll(hostGame,createGame);

        hostGame.getStyleClass().add("channelItem");


        // host game clicked
        hostGame.setOnMouseClicked(e ->{
            createGame.visibleProperty().set(true);
        });
        // keypressed
        vbox.setOnKeyPressed(e -> {
            if (createGame.visibleProperty().get()) {
                communicator.send("CREATE "+createGame.getText());
            }
        });
    vbox.setOnMouseEntered(e ->{
        for (Button button : channel_button) {
            button.setOnMouseClicked(ev -> {
                logger.info("joining "+ button.getText());
                communicator.send("JOIN "+button.getText());
            });
        }
    });


        // timer requesting for channels
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                communicator.send("LIST");
                if (check_quit){
                    timer.cancel();
                }
            }
        };
        timer.schedule(timerTask,0,5000);
        communicator.addListener((message) -> {


            String starting;
            if(message.contains(" ")){
                starting = message.substring(0,message.indexOf(" "));
                message = message.substring(message.indexOf(" ")+1) + "\n";

            }
            else{
                starting = message;
            }
            String finalMessage = message;
            switch (starting){
                case "CHANNELS":
                    message = message.replace(" ", "");
                    Platform.runLater(() -> this.channel(finalMessage));
                    break;
                case "JOIN":
                    message = message.replace(" ", "");
                    Platform.runLater(() -> this.join(finalMessage));
                    break;
                case "ERROR":
                    Platform.runLater(() -> this.error(finalMessage));
                    break;
                case "PARTED":
                    message = message.replace(" ", "");
                    Platform.runLater(() -> this.part(finalMessage));

            }
        });



    }

    /**
     * method interpreting channels from the message
     * @param message message from server
     */
    private void channel(String message){
        logger.info("searching channels");
        var k = channel_list.size();
        for (int i = 0; i< k;i++){
            vbox_2.getChildren().remove(0);
            channel_list.remove(0);
            channel_button.remove(0);
        }

        while (!message.equals("\n")){
            var temp = "";
            temp = message.substring(0,message.indexOf("\n"));
            message = message.substring(message.indexOf("\n")+1);
            channel_list.add(temp);
        }

            for (int i = 0; i< channel_list.size();i++){
                channel_button.add(new Button(channel_list.get(i)));
                channel_button.get(i).getStyleClass().add("channelItem");
                vbox_2.getChildren().add(channel_button.get(i));
            }

    }
    /**
     * method use for joining games
     * @param message message from server
     */
    private void join(String message){
        logger.info("joining game");
        gameBox.setVisible(true);
        title_game.setText(message);
        createGame.setText("");
        createGame.setVisible(false);
    }

    /**
     * method use for exiting the game
     * @param message message from server

     */
    private void part(String message){
        logger.info("leave game");
        gameBox.setVisible(false);
    }

    /**
     * method used for dealing with errors
     * @param message message from server
     */
    private void error(String message){
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("Error");
        errorAlert.setHeaderText("Error");
        errorAlert.setContentText(message);
        errorAlert.showAndWait();
    }
}

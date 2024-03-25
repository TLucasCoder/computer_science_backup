package uk.ac.soton.comp1206.scene;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.ac.soton.comp1206.component.PieceBoard;
import uk.ac.soton.comp1206.component.ScoresList;
import uk.ac.soton.comp1206.event.CommunicationsListener;
import uk.ac.soton.comp1206.game.Game;
import uk.ac.soton.comp1206.network.Communicator;
import uk.ac.soton.comp1206.ui.GamePane;
import uk.ac.soton.comp1206.ui.GameWindow;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * scene displaying scores after the game
 */
public class ScoresScene extends BaseScene {
    private static final Logger logger = LogManager.getLogger(ScoresScene.class);
    private Game game;
    private SimpleListProperty<Pair<String, Integer>> remoteScores = new SimpleListProperty<>(FXCollections.observableArrayList());
    private SimpleListProperty<Pair<String, Integer>> localScores = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ArrayList<CommunicationsListener> communicationsListeners;
    ScoresList scoresList = new ScoresList();
    Communicator communicator;
    private StringProperty name = new SimpleStringProperty();
    private ArrayList<Pair<String, Integer>> temp = new ArrayList<>();

    /**
     * Create a new scene, passing in the GameWindow the scene will be displayed in
     *
     * @param gameWindow the game window
     */
    public ScoresScene(GameWindow gameWindow) {
        super(gameWindow);

    }

    /**
     * initialise the scene
     */
    @Override
    public void initialise() {
        scene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case ESCAPE:
                    // return
                    gameWindow.startMenu();
                    break;
            }
        });

    }
    /**
     * build the scene
     */
    @Override
    public void build() {
        scoresList.bind(localScores);


        try {
            loadScene(localScores);
        } catch (Exception e) {
            e.printStackTrace();
        }


        logger.info("Building " + this.getClass().getName());
        root = new GamePane(gameWindow.getWidth(), gameWindow.getHeight());
        var scoreScene = new StackPane();
        scoreScene.getStyleClass().add("score-background");
        root.getChildren().add(scoreScene);
        var borderpane = new BorderPane();
        scoreScene.getChildren().add(borderpane);
        var pane = new Pane();
        var lose = new Text("You Lose");
        lose.getStyleClass().add("bigtitle");
        pane.getChildren().add(lose);
        lose.setLayoutX(200);
        lose.setLayoutY(100);


        if (game.getScore().get() > localScores.get(localScores.size() - 1).getValue()) {
            var pane_1 = new VBox();
            var text = new Text("Your Score is");
            text.getStyleClass().add("title");
            var text_1 = new Text(Integer.toString(game.getScore().get()));
            text_1.getStyleClass().add("score");
            var text_2 = new Text("Please enter your name:");
            text_2.getStyleClass().add("title");
            var textField = new TextField("Please enter your name");
            textField.textProperty().bindBidirectional(this.name);
            textField.setAlignment(Pos.CENTER);
            pane_1.getChildren().addAll(text, text_1, text_2, textField);
            pane_1.setAlignment(Pos.CENTER);
            borderpane.setCenter(pane_1);

            pane_1.setOnKeyPressed(e -> {
                if (e.getCode().equals(KeyCode.ENTER)) {
                    ArrayList<Pair<String, Integer>> arr = new ArrayList<>(localScores);
                    arr.add(new Pair(textField.getText(), game.getScore().get()));
                    arr.sort(Comparator.comparing(p -> -p.getValue()));
                    localScores.clear();
                    localScores.addAll(arr);
                    borderpane.getChildren().remove(pane_1);
                    borderpane.setCenter(pane);
                    setDisplayLocal(pane);
                }
            });

        } else {
            borderpane.setCenter(pane);
            setDisplayLocal(pane);
        }
        // display local scores

    }

    /**
     * passing the game result to the scene
     * @param game the last game played
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * function use for loading scene from file
     * @param localScoresS the simple List property storing the players and their scores
     * @throws Exception
     */
    public void loadScene(SimpleListProperty<Pair<String, Integer>> localScoresS) throws Exception {
        File file = new File(ScoresScene.class.getResource("/TextFile/score.txt").toURI());
        BufferedReader bf = new BufferedReader(new FileReader(file));
        String captured;
        ArrayList<Pair<String, Integer>> temp_List = new ArrayList<>();
        while ((captured = bf.readLine()) != null) {
            temp_List.add(readInfo(captured));
        }
        temp_List.sort(Comparator.comparing(p -> -p.getValue()));
        localScoresS.addAll(temp_List);
    }

    /**
     * writing score back to file
     */
    public void writeScore() {
        try {
            File file = new File(ScoresScene.class.getResource("/TextFile/score.txt").toURI());
            Writer writer = new FileWriter(file);

            for (Pair<String, Integer> itr : localScores) {
                writer.write(itr.getKey() + ":" + itr.getValue().toString());
                writer.write("\n");
            }
            writer.close();
        } catch (Exception e) {
            Path newFilePath = Paths.get("TextFile/score.txt");
            try {
                Files.createFile(newFilePath);
                File file = new File(ScoresScene.class.getResource("/TextFile/score.txt").toURI());
                Writer writer = new FileWriter(file);
                for (int i = 0; i < 10; i++) {
                    int temp = 2000 * (10 - i);
                    writer.write("Lucas:" + temp);
                }
                writer.close();
            } catch (Exception ea) {
                e.printStackTrace();
            }
        }
    }

    /**
     * displaying the local scores
     * @param pane the pane for display
     */
    public void setDisplayLocal(Pane pane) {
        loadOnlineScores();

            var localScoreDisplay = new VBox();
            pane.getChildren().add(localScoreDisplay);
            var displayTitle = new Text("Local Scores");
            displayTitle.getStyleClass().add("title");
            localScoreDisplay.getChildren().add(displayTitle);
            localScoreDisplay.setLayoutX(100);
            localScoreDisplay.setLayoutY(200);
            localScoreDisplay.setAlignment(Pos.CENTER);
            ArrayList<Text> score = new ArrayList<>();
            for (int i = 0; i < localScores.size(); i++) {
                score.add(new Text((localScores.get(i).getKey() + ":" + localScores.get(i).getValue())));
                score.get(i).getStyleClass().add("scorelist");
                localScoreDisplay.getChildren().add(score.get(i));
                if (i == 10) {
                    break;
                }
            }
            writeScore();

            Platform.runLater(()->{
                loadOnlineScores();
                var RemoteScoreDisplay = new VBox();
                pane.getChildren().add(RemoteScoreDisplay);
                var displayRemoteTitle = new Text("Remote Scores");
                displayRemoteTitle.getStyleClass().add("title");
                RemoteScoreDisplay.getChildren().add(displayRemoteTitle);
                RemoteScoreDisplay.setLayoutX(500);
                RemoteScoreDisplay.setLayoutY(200);
                RemoteScoreDisplay.setAlignment(Pos.CENTER);
                ArrayList<Text> scoreRemote = new ArrayList<>();
                for (int i = 0; i< remoteScores.size();i++) {
                    scoreRemote.add(new Text((remoteScores.get(i).getKey()+":"+remoteScores.get(i).getValue())));
                    scoreRemote.get(i).getStyleClass().add("scorelist");
                    RemoteScoreDisplay.getChildren().add(scoreRemote.get(i));
                    if (i == 10){
                        break;
                    }
                }
             });



    }

    /**
     * method used to load the online scores from server
     */
    public void loadOnlineScores() {
        communicator = gameWindow.getCommunicator();
        communicator.send("HISCORES");
        communicator.addListener((message) -> {
            message = message.substring(message.indexOf(" ") + 1);
            message = message.replace(" ", "");
            message = message + "\n";

            while (!message.equals("")) {
                if (message.equals("\n")) {
                    break;
                }

                String readText = message.substring(0, message.indexOf("\n"));
                message = message.substring(message.indexOf("\n") + 1);
                remoteScores.removeAll(temp);
                temp.add(readInfo(readText));
                temp.sort(Comparator.comparing(p -> -p.getValue()));
                remoteScores.addAll(temp);


                if (message.equals("\n")) {
                    temp.sort(Comparator.comparing(p -> -p.getValue()));
                    remoteScores.addAll(temp);

                }

            }
        });
    }

    /**
     * writing result to server
     */
    private void writeOnlineScore() {
        if (game.getScore().get() > remoteScores.get(remoteScores.size() - 1).getValue()) {
            communicator.send("NEWSCORE " + this.name.get() + ":" + game.getScore().get());
        }
    }


    /**
     * function identify the name and score
     *
     * @param toBeParsed message to be identified
     * @return pair containing the name of player and their respective score
     */
    private Pair<String, Integer> readInfo(String toBeParsed) {
        String nameFromFile = toBeParsed.substring(0, toBeParsed.indexOf(":"));
        String scoreFromFile = toBeParsed.substring(toBeParsed.indexOf(":") + 1);
        scoreFromFile = scoreFromFile.replace(" ", "");

        return new Pair<>(nameFromFile, Integer.parseInt(scoreFromFile));
    }
}


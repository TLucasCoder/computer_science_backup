package uk.ac.soton.comp1206.component;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Transition;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import javafx.util.Pair;
import uk.ac.soton.comp1206.scene.ScoresScene;

import java.util.ArrayList;
import java.util.Properties;

/**
 *  hold and display a list of names and associated scores
 */
public class ScoresList{
    SimpleListProperty<Pair<String,Integer>> scores = new SimpleListProperty<>(FXCollections.observableArrayList());

    /**
     * function binding the local scores from scores scene to scoresList
     */
    public void bind(SimpleListProperty<Pair<String,Integer>> input) {
        scores.bind(input);
    }

    /**
     * animation for revealing the scoreboard
     */
    public void reveal(VBox vbox){
        FadeTransition fadeIn = new FadeTransition(Duration.millis(2000), vbox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

}

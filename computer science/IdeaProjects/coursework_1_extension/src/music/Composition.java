package music;

import java.util.List;

public interface Composition {
    //made up of several music scores for different instruments
    // The Conductor will coordinate the play of a composition
    String getName();
    //Return the name of the composition
    void addScore(String instrumentName, List<String> notes, boolean soft);
    //Add a score to the composition
    //The score is represented by the name of the instrument instrumentName,
    //the String representation of the notes in the music score (notes), and whether
    //the music should be played softly (soft).
    MusicScore[] getScores();
    // return the array of music scores in the composition
    int getLength();
    //get the length of the composition(how many notes is to be played)
    int getNoteLength(int index);
    // get the length (in ms) for a note. This dictates the tempo of the
    //composition.
}

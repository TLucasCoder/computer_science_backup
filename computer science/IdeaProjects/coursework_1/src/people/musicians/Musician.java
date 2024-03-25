package people.musicians;
// musicians interface is common for all types of musicians
public interface Musician {
    void setSeat(int seat);
    //to tell the musician what seat they are sitting in. The input seat is assumed to be between 0 and 15.
    void readScore(int [] notes, boolean soft);
    // to read the music score represented by an array of music notes (parameter notes)
    // including whether the musician will softly or loudly (parameter soft)
    void playNextNote();
    // after setting the position of the musician  (using setSeat) and the musician reads the score
    //(using readScore), playNextNote will play the next note
    //(and only the next node) in the music score. This method
    //will do nothing if there are no more notes to play.
    int getSeat();
    String getName();
    // get instrument id
    int getID();
    void clear_note();
}

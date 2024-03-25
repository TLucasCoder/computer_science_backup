package music;

public class MusicScore {
    // keeps the information about a piece of music for an individual instrument
    // Information stored include information about the instrument, the notes that
    //represent the music score, and whether the music should be played softly
    private String instrumentName = "";
    private int [] notes;
    private boolean soft;
    public MusicScore(String instrumentName, int[] notes, boolean soft){
        this.instrumentName = instrumentName;
        this.notes = notes;
        this.soft = soft;
    }


    public int getInstrumentID(){
        //return the instrument ID for the music score
        if (instrumentName.equals("Piano")){
            return 1;
        }
        else if (instrumentName.equals("Cello")){
            return 43;
        }
        else{
            return 41;
        }
    }
    public int[] getNotes(){
        //return the array of notes representing the music score
        return this.notes;
    }
    public boolean isSoft(){
        //return whether the music should be played softly
        return this.soft;
    }


}

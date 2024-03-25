package people.musicians;

import people.Person;
import people.musicians.Musician;
import utils.SoundSystem;
import javax.sound.midi.MidiUnavailableException;

public class Pianist extends Person implements Musician {
    public Pianist(String name, SoundSystem soundSystem) throws MidiUnavailableException{
        super(name);
        this.instrumentID = 1;
        this.soundSystem = soundSystem;
    }
    public Pianist(String name, SoundSystem soundSystem, int seat) throws MidiUnavailableException{
        super(name);
        this.instrumentID = 1;
        this.seat = seat;
        this.soundSystem = soundSystem;
    }
    // constructor of the Pianist object
    public void setSeat(int seat){
        this.seat = seat;
    }
    public void readScore(int [] notes, boolean soft){
        for (int i = 0; i< notes.length; i++){
            this.notes.add(notes[i]);
        }
        if (soft){
            loudness = 75;
        }
        else{
            this.loudness = 150;
        }
        this.nextNote = this.notes.iterator();
    }


    public void playNextNote(){
        soundSystem.setInstrument(this.seat,this.instrumentID);
        if(nextNote.hasNext()){
            soundSystem.playNote(this.seat,nextNote.next(),this.loudness);
        }
        else{
            soundSystem.init();
        }
    }
    public int getSeat(){
        return this.seat;
    }
    public String getName() {
        return this.name;
    }
    public int getID(){
        return this.instrumentID;
    }
    public void clear_note(){
        notes.clear();
    }
}

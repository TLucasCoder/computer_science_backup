package people.musicians;

import people.Person;
import people.musicians.Musician;
import utils.SoundSystem;

import javax.sound.midi.MidiUnavailableException;

public class Violinist extends Person implements Musician {
    public Violinist(String name, SoundSystem soundSystem) throws MidiUnavailableException {
        super(name);
        instrumentID = 41;
        this.soundSystem = soundSystem;

    }
    public Violinist(String name, SoundSystem soundSystem, int seat) throws MidiUnavailableException{
        super(name);
        this.instrumentID = 41;
        this.seat = seat;
        this.soundSystem = soundSystem;
    }
    // constructor of the Violinist object
    public void setSeat(int seat){
        this.seat = seat;
    }
    public void readScore(int [] notes, boolean soft){
        for (int i = 0; i< notes.length; i++){
            this.notes.add(notes[i]);
        }
        if (soft){
            loudness = 50;
        }
        else{
            this.loudness = 100;
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

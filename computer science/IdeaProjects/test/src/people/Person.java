package people;
import java.util.*;
import utils.SoundSystem;

import javax.sound.midi.MidiUnavailableException;

public class Person {
    protected String name;
    protected int instrumentID;
    // need to be corrected later
    protected List<Integer> notes = new ArrayList<>();

    protected Iterator<Integer> nextNote;
    protected SoundSystem soundSystem = new SoundSystem();
    protected int seat;
    protected int loudness;

    public Person(String name) throws MidiUnavailableException {
        this.name = name;
    }
    public String getName(){
        return name;
    }



}

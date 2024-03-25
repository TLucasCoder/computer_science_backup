package people.conductors;

import music.MusicScore;
import music.Composition;
import orchestra.Orchestra;
import people.Person;
import utils.SoundSystem;
import people.musicians.Musician;
import javax.sound.midi.MidiUnavailableException;
import java.util.ArrayList;
import java.util.Arrays;


public class Conductor extends Person {
    //the class Conductor is responsible for putting the orchestra together,
    //reading the music in and then conducting the orchestra in playing it.

    protected ArrayList<Musician> musician = new ArrayList<>();
    protected  Orchestra orchestra = new Orchestra();
    public Conductor(String name, SoundSystem soundSystem) throws MidiUnavailableException {
        super(name);
        this.soundSystem = soundSystem;


    }
    // to register the musician into the band
    public void registerMusician(Musician musician){
    if(!orchestra.isSeated(musician)) {
            this.musician.add(musician);
        }
    }
    public void remove_musician(Musician musician){
        try {
            orchestra.standUp(musician);
            for (int i = 0; i < this.musician.size(); i++) {
                if (musician.equals(this.musician.get(i))) {
                    this.musician.remove(i);
                }
            }
        }
        catch (Exception e){
            System.out.println("error occurred in removing musician");
        }
    }
    public int get_number_musician(){
        return musician.size();
    }


    @SuppressWarnings("BusyWait")
    public void playComposition(Composition composition){
        //Get the scores from the composition
        MusicScore [] musicScore = composition.getScores();
        // assign the scores to musician
        try {
            for (int i = 0; i < musician.size(); i++) {
                for (int j = 0; j < musicScore.length; j++) {
                    if (musician.get(i).getID() == musicScore[j].getInstrumentID()) {
                        musician.get(i).clear_note();
                        musician.get(i).readScore(musicScore[j].getNotes(), musicScore[j].isSoft());
                        break;
                    }
                }
            }
        }
        catch (Exception e){
            System.out.println("error occurred in assigning the scores to musicians");
        }
       // making the musician sit down in an orchestra

        for (int i = 0;i< musician.size();i++){
            int response;
           response = orchestra.sitDown(musician.get(i));
           if (response==1){
               System.out.println("Orchestra is full");
           }
           else if (response ==2){
               System.out.println("Musician has been inputted");
           }
        }
        // the execution of the music
        try {
            int count = 0;
            while (count+1<= composition.getLength()){
                count++;


                try {
                    orchestra.playNextNote();
                }
                catch (Exception e){
                    System.out.println("Musician not found or music sheet not found");
                    break;
                }



                Thread.sleep(composition.getNoteLength());
                if (count == composition.getLength()){
                    soundSystem.init();
                }
            }
            Thread.sleep(2000);
        }
        catch (InterruptedException e ){
            System.out.println("wrong");

        }

    }
}

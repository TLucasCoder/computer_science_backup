import music.Composition;
import music.MusicScore;
import music.MusicSheet;
import people.conductors.Conductor;
import people.musicians.Cellist;
import people.musicians.Musician;
import people.musicians.Pianist;
import people.musicians.Violinist;
import utils.SoundSystem;

import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.random.RandomGenerator;
public class EcsBandAid {
    //simulate a group of musicians playing some music.
//As time passes, existing musicians leave the group and new members join to play the new
//compositions
    protected SoundSystem soundSystem;
    protected ArrayList<Musician> musicians = new ArrayList<>();
    protected ArrayList<Composition> compositions = new ArrayList<>();
    protected Conductor conductor__;

    protected HashMap<Integer,ArrayList<Musician>> invited_musician = new HashMap();
    public EcsBandAid(SoundSystem soundSystem, Musician [] musician, Composition [] compositions){
        Collections.addAll(this.musicians, musician);
        Collections.addAll(this.compositions, compositions);
        this.soundSystem = soundSystem;
        // organise them into different instrument
        for (int i = 0; i< 3 ; i++){
            invited_musician.put(i,new ArrayList<Musician>());
        }

    }

    // temp. getting conductor
    public void getConductor(Conductor conductor){
        this.conductor__ = conductor;
    }

    public void performForAYear(){
        // randomly choosing 3 compositions
        ArrayList<Composition> compositions_chosen = new ArrayList<>();

        int [][] number_instrument = {{0,0,0},{0,0,0},{0,0,0}};
        System.out.println("Compositions chosen: ");
        for (int i = 0; i< 3 ;i++){
            // this type of random may lead to repetition of song
            Random random = new Random();
            //int int_random = random.nextInt(compositions.size());
            int int_random = random.nextInt(compositions.size());
            compositions_chosen.add(compositions.get(int_random));
            System.out.println( (i+1) + ": " + compositions_chosen.get(i).getName());
            // finding the number of violinist, pianist and cellist needed in below
            // instrument_inc = the scores in one composition
            MusicScore[] instrument_inc = compositions_chosen.get(i).getScores();
            // the array storing the number of each musician needed in each instrument.
            for (MusicScore musicScore : instrument_inc) {
                if (musicScore.getInstrumentID() == 1) {
                    number_instrument[i][0]++;
                } else if (musicScore.getInstrumentID() == 41) {
                    number_instrument[i][1]++;
                } else if (musicScore.getInstrumentID() == 43){
                    number_instrument[i][2]++;
                }
            }
        }

        // the aim of the following code is to find out
        // the minimum total value of musicians needed for the 3 compositions and organise them into the conductor
        // for every composition so the musician in the band will be cleared every time after the composition and
        // will be reorganised.
        // temp: storing the number of max value of musicians needed
        int [] temp = {0,0,0};
        for (int i = 0; i< 3;i++){
            if (number_instrument[i][0]>temp[0]){
                temp[0] = number_instrument[i][0];
            }
            if (number_instrument[i][1]>temp[1]){
                temp[1] = number_instrument[i][1];
            }
            if (number_instrument[i][2]>temp[2]){
                temp[2] = number_instrument[i][2];
            }
        }
        for (int k = 0; k< 3 ; k++) {
            int i = invited_musician.get(k).size();
            while (i< temp[k]) {
                i++;
                for (int j = 0; j < musicians.size(); j++) {
                    if (k == 0 && musicians.get(j).getID() == 1 && !invited_musician.get(k).contains(musicians.get(j))) {
                        invited_musician.get(k).add(musicians.get(j));
                        break;
                    }
                    if (k == 1 && musicians.get(j).getID() == 41 && !invited_musician.get(k).contains(musicians.get(j))) {
                        invited_musician.get(k).add(musicians.get(j));
                        break;
                    }
                    if (k == 2 && musicians.get(j).getID() == 43 && !invited_musician.get(k).contains(musicians.get(j))) {
                        invited_musician.get(k).add(musicians.get(j));
                        break;
                    }
                }
            }
        }

        //display
        System.out.println("Invited musician: ");
        for (int ii= 0; ii < 3 ; ii++) {
            for (int i = 0; i< invited_musician.get(ii).size(); i++){

                System.out.print(invited_musician.get(ii).get(i).getName() + " ");
            }
        }
        System.out.println();
        System.out.println();
        // register the musician for each composition into the conductor

        for (int i = 0; i< 3 ; i++){
            try {
                for (int k = 0; k < 3; k++) {
                    if (number_instrument[i][k] > 0) {
                        for (int j = 0; j < number_instrument[i][k]; j++) {
                            conductor__.registerMusician(invited_musician.get(k).get(j));
                        }
                    }
                }
            }
            catch (Exception e){
                System.out.println("not enough musicians");
            }

            System.out.println("Composition performing at the moment: " + compositions_chosen.get(i).getName());
            conductor__.playComposition(compositions_chosen.get(i));
            for (int ip = 0; ip < 3 ; ip++) {
                for (int id = 0 ; id < invited_musician.get(ip).size(); id ++) {
                    conductor__.remove_musician(invited_musician.get(ip).get(id));
                }
            }
            System.out.println();
        }
        Random leaving = new Random();
        ArrayList<Musician> leaving_list = new ArrayList<>();

        for (int im = 0; im < 3 ; im ++) {

            for (int it = 0; it < invited_musician.get(im).size(); it++) {
                int index = leaving.nextInt(2);
                if (index == 0) {
                    leaving_list.add(invited_musician.get(im).get(it));
                }
            }
        }
        System.out.println("List of leaving musician: ");
        for (Musician musician : leaving_list) {
            System.out.print(musician.getName() + " ");
        }
        System.out.println();

    }

    public static void main(String [] args) throws MidiUnavailableException{
        SoundSystem k = new SoundSystem();
        readingFile rF = new readingFile();
        ArrayList<Musician> musicians = new ArrayList<>();
        Conductor conductor = new Conductor("Luke",k);
        ArrayList<MusicSheet> musicSheets = new ArrayList<>();
        musicians = rF.readMusician(args[0]);
        musicSheets = rF.reading_composition(args[1]);
        Musician [] to_1 = musicians.toArray(new Musician[musicians.size()-1]);
        Composition[] to_2 = musicSheets.toArray(new Composition[musicSheets.size()-1]);
        EcsBandAid eba = new EcsBandAid(k,to_1,to_2);
        eba.getConductor(conductor);
        // execution of ecs band
        for (int i = 0; i< Integer.parseInt(args[2]); i++) {
            eba.performForAYear();
        }
    }


}

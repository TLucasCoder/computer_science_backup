package orchestra;

import people.musicians.Musician;

import java.util.HashMap;
import java.util.Map.Entry;

public class Orchestra {
    HashMap<Integer, Musician> seating;

    public Orchestra() {
        this.seating = new HashMap<Integer, Musician>();

    }
    // function for making the musician sit down in the sit
    public int sitDown(Musician musician) {
        int check_seat = 0;
        int k = -1;
        for (Entry<Integer, Musician> entry : seating.entrySet()){
            if (entry.getValue()==musician){
                return 2;
            }
            check_seat++;
        }
        if (check_seat!=16) {
            if (!seating.containsKey(musician.getSeat())) {
                this.seating.put(musician.getSeat(), musician);
            }
            else{
                k = 0;
                while (seating.containsKey(k)){
                    k++;
                }
                musician.setSeat(k);
                this.seating.put(musician.getSeat(), musician);
            }
            return 0;
        }
        else {
            return 1;
        }

    }
    // check if the seat is occupied
    public boolean isSeated(Musician musician){
        return seating.containsValue(musician);
    }
    // function making the musician away from their sits
    public void standUp (Musician musician){
        for (Entry<Integer, Musician> entry: seating.entrySet()){
            if (entry.getValue().equals(musician)){
                seating.remove(entry.getKey());
                break;
            }
        }
    }
    // instruct the musician to play the next note
    public void playNextNote(){
       for (int i = 0; i<15; i++){
           if (seating.containsKey(i)){
               seating.get(i).playNextNote();
           }
       }
    }

}

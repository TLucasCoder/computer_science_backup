package orchestra;

import people.musicians.Musician;

import java.util.HashMap;
import java.util.Map.Entry;

public class Orchestra {
    HashMap<Integer, Musician> seating;

    public Orchestra() {
        this.seating = new HashMap<Integer, Musician>();

    }

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
    public boolean isSeated(Musician musician){
        return seating.containsValue(musician);
    }
    public void standUp (Musician musician){
        for (Entry<Integer, Musician> entry: seating.entrySet()){
            if (entry.getValue().equals(musician)){
                seating.remove(entry.getKey());
                break;
            }
        }
    }
    public void playNextNote() throws InterruptedException {
       for (int i = 0; i<15; i++){
           if (seating.containsKey(i)){
               seating.get(i).playNextNote();
           }
       }
    }

}

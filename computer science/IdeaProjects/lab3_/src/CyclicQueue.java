import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

import static java.lang.Math.abs;

public class CyclicQueue implements NumberQueue {

    int size_queue;
    int head =  0;
    int tail = -1;
    int size = 0;
    int [] cyc_arr;
    public CyclicQueue(int capacity){
        size_queue = capacity;
        cyc_arr = new int[size_queue];
    }

    public int k(){
        return cyc_arr.length;
    }
    @Override
    public void enqueue(int i) throws IndexOutOfBoundsException {
        //System.out.println("length "+cyc_arr.length);
        //System.out.println("head " +head);
        //System.out.println("tail " + tail);

        System.out.println();
        if (size < cyc_arr.length) {

            tail = (tail + 1) % cyc_arr.length;
            cyc_arr[tail] = i;
            size++;
        }
        else{

           throw new IndexOutOfBoundsException();
        }


    }

    @Override
    public int dequeue() throws IndexOutOfBoundsException {

        int temp = cyc_arr[head];

        if (size!=0) {
            cyc_arr[head] = -1;
            //head++;
            head = (head + 1) % cyc_arr.length;
            size--;
        }
        else{
            throw new IndexOutOfBoundsException();
        }

        return temp;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }
}

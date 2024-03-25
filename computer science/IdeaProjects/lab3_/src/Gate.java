public class Gate implements Runnable{
    int nog;
    Counter counter;
    // nog = number of guest
    public Gate(Counter counter,int nog ){
        this.nog = nog;
        this.counter = counter;

    }
    @Override
    public void run() {
        for (int i = 0; i< nog; i++){
            counter.addOne();
        }
    }
}

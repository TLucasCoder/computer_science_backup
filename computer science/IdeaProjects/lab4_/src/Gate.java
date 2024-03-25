public class Gate implements Runnable{
    int nog;
    Counter counter;
    // nog = number of guest
    public Gate(Counter counter,int nog ){
        this.nog = nog;
        this.counter = counter;

    }
    @Override
    public synchronized void run() {
        synchronized (this) {
            //System.out.println("counter " + counter.getCounter());
            for (int i = 0; i < nog; i++) {

                counter.addOne();

            }
            //System.out.println("Thread:" + Thread.currentThread().getName() + " counter: " + counter.getCounter());
        }
    }
}

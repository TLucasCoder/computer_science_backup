import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.Seat;

import java.util.concurrent.locks.ReentrantLock;

public class seatTypeOne implements Seat {
    private ReentrantLock fork1;
    private ReentrantLock fork2;
    private volatile boolean flag = false;

    @Override
    public  void askFork1() {

        fork1.lock();
    }

    @Override
    public  void askFork2() {
        fork2.lock();
    }

    @Override
    public synchronized void assignForks(ReentrantLock reentrantLock, ReentrantLock reentrantLock1) {
        if (flag){
            try{
                wait();
                fork1 = null;
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }

        }
        flag = true;
        notify();
        fork1 = reentrantLock;
        fork2 = reentrantLock1;
    }
}

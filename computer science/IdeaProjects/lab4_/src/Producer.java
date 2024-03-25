import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.FactoryWorker;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

import java.util.Random;

public class Producer extends FactoryWorker {
    private int id;
    private volatile boolean flag = true;
    NumberQueue belt_;
    public Producer (int i, NumberQueue numberQueue){
        super("Producer",i,numberQueue);
        belt_ = numberQueue;
        id = i;


    }
    @Override
    public void message(int i) {
        System.out.println("Producer "+ id + "picked " + i + "from the belt" );
    }

    @Override
    public synchronized int action() {
        //message(30);
        while (!flag){
            try {
                this.wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        flag = false;
        this.notify();
        Random random = new Random();
        int temp =  random.nextInt();
        belt_.enqueue(temp);

        return temp;

    }

    @Override
    public synchronized void run()  {
        while (!flag){
            try {
                this.wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        flag = false;
        this.notify();

        while(!Thread.currentThread().isInterrupted()){

            action();
        }
        messageError();
    }
}

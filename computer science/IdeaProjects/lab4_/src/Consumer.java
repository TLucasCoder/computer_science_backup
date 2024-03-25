import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.FactoryWorker;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

public class Consumer extends FactoryWorker {
    private int id;
    private volatile boolean flag = false;
    NumberQueue belt_;
    public Consumer (int i, NumberQueue numberQueue){
        super("Consumer",i,numberQueue);
        belt_ = numberQueue;
        id = i;


    }
    @Override
    public void message(int i) {
        System.out.println("Consumer "+ id + "picked " + i + "from the belt" );
    }

    @Override
    public synchronized int action() {
        while (flag){
            try {
                this.wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        flag = true;
        this.notify();
        return belt_.dequeue();
    }

    @Override
    public synchronized void run() {
        while (flag){
            try {
                this.wait();
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        flag = true;
        this.notify();

        while(!Thread.currentThread().isInterrupted()){
            action();
        }
        messageError();
    }
}

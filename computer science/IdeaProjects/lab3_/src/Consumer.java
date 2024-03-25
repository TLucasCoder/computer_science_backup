import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.FactoryWorker;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

import java.util.Random;

public class Consumer extends FactoryWorker {
    private int id;
    public Consumer (int i, NumberQueue numberQueue){
        super("Consumer",i,numberQueue);
        id = i;


    }
    @Override
    public void message(int i) {
        System.out.println("Consumer "+ id + "picked " + i + "from the belt" );
    }

    @Override
    public int action() {

        return belt.dequeue();
    }

    @Override
    public void run()  {
        while(!Thread.currentThread().isInterrupted()){
            action();
        }
        messageError();
    }
}

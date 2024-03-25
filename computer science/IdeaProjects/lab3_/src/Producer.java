import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.FactoryWorker;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.threading.NumberQueue;

import java.util.Random;

public class Producer extends FactoryWorker {
    private int id;
    public Producer (int i, NumberQueue numberQueue){
        super("Producer",i,numberQueue);
        id = i;


    }
    @Override
    public void message(int i) {
        System.out.println("Producer "+ id + "picked " + i + "from the belt" );
    }

    @Override
    public int action() {
        Random random = new Random();
        int temp =  random.nextInt();
        belt.enqueue(temp);
        return temp;
    }

    @Override
    public void run()  {
        while(!Thread.currentThread().isInterrupted()){
            action();
        }
        messageError();
    }
}

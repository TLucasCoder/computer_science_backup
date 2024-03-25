public class Zoo {

    public static void  main(String [] args ) throws InterruptedException{
        Counter counter = new Counter();
        Runnable gate1 = new Gate(counter,1000);
        Runnable gate2 = new Gate(counter,1000);
        Runnable gate3 = new Gate(counter,1000);
        Runnable gate4 = new Gate(counter,1000);
        Runnable gate5 = new Gate(counter,1000);
        Runnable gate6 = new Gate(counter,1000);
        //Runnable gate7 = new Gate(counter,1000);
        //Runnable gate8 = new Gate(counter,1000);
        Thread t1 = new Thread(gate1);
        Thread t2 = new Thread(gate2);
        Thread t3 = new Thread(gate3);
        Thread t4 = new Thread(gate4);
        Thread t5 = new Thread(gate5);
        Thread t6 = new Thread(gate6);

/*
        t1.start();
        t1.join();
        t2.start();
        t2.join();
        t3.start();
        t3.join();
        t4.start();
        t4.join();
        t5.start();
        t5.join();
        t6.start();
        t6.join();

 */

        //Thread t7 = new Thread(gate7);
        //Thread t8 = new Thread(gate8);

        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();
        t6.start();
        //t7.start();
        //t8.start();

        t1.join();
        t2.join();
        t3.join();
        t4.join();
        t5.join();
        t6.join();
        //t7.join();
        //t8.join();



        System.out.println(counter.getCounter());



    }

}

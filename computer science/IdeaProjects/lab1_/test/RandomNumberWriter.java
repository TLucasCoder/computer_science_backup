import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.io.RandomIO;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Random;


public class RandomNumberWriter implements RandomIO {
    Random rand;
    long number = 0;
    public RandomNumberWriter(long seed){
        rand = new Random(seed);
        number = seed;
        //this.number = rand.nextLong(arg+1);

    }
    @Override
    public void writeRandomChars(String s) throws IOException{
        Random rand_1 = new Random(number);

        File file_ = new File(s);
        Writer out_w = new FileWriter(file_);
        for (int i = 0; i< 10000 ; i++){
            Integer random_num = rand_1.nextInt(100000);
            //System.out.println(random_num);
            String k = Integer.toString(random_num);
            out_w.write(k + "\n");
        }
        out_w.close();
    }

    @Override
    public void writeRandomByte(String s) throws IOException {
        Random rand_1 = new Random(number);
        ByteBuffer bf =  ByteBuffer.allocate(4);

        FileOutputStream file_ = new FileOutputStream(s);
        DataOutputStream out_s = new DataOutputStream(file_);
        /*
        File file_ = new File(s);
        OutputStream out_s = new FileOutputStream(file_);

         */
        for (int i = 0; i< 10000 ; i++){
            int random_num = rand_1.nextInt(100000);
            out_s.writeInt(random_num);
            /*
            System.out.println(bf);
            WritableByteChannel channel = Channels.newChannel(out_s);
            channel.write(bf);

             */
        }
        out_s.close();
    }
}

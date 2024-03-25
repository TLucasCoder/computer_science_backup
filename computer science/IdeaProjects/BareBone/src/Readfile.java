import java.io.*;
import java.util.*;
public class Readfile {
    private String data;
    public void reading() {
        try {
            File r_file = new File("C:\\Users\\TLucas\\IdeaProjects\\BareBone\\src\\barebone.txt");
            Scanner myScanner = new Scanner(r_file);
            data = "";
            while (myScanner.hasNextLine()) {
                String temp = myScanner.nextLine();
                data = data + temp;
                if (!temp.contains(";")){
                    data+= ";";
                }

                //System.out.println(data);
            }

            myScanner.close();
        }
        catch(FileNotFoundException e){
            System.out.println("Error occurred.");
            e.printStackTrace();
        }
    }
    public String Getdata(){
        return data;
    }

}

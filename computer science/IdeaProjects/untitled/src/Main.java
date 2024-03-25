import java.io.*;
import java.util.*;

public class Main {
    Hashtable<String, Integer> variables = new Hashtable<String, Integer>();
    ArrayList<String> Keys = new ArrayList<String>();
    public Main()throws Exception{
        System.out.println("Please enter your BareBones file:");
        BufferedReader file = new BufferedReader(new InputStreamReader(System.in));
        File bare = new File(file.readLine());
        read(bare);
    }
    public static void main(String[] args)throws Exception{
        Main run = new Main();
    }
    public void read(File file)throws Exception{
        Scanner eachLine = new Scanner(file);
        while (eachLine.hasNextLine()) {
            String line = eachLine.nextLine();
            String[] words = line.split(" ");
            List<String> list = new ArrayList<String>(Arrays.asList(words));
            list.removeAll(Arrays.asList("", null));
            words = list.toArray(new String[0]);
            if (words[0].equals("end;")){
                break;
            }
            //The first word in any line is an operator, and the second is a variable to be used.
            //So only the first two words need to be looked at.
            String var = words[1];

            while (var.endsWith(";")) {
                var = var.substring(0, var.length() - 1);
            }
            varCheck(var);
            int currentVal = variables.get(var);
            switch (words[0]) {
                case "incr":
                    variables.put(var, currentVal + 1);
                    printTable();
                    break;

                case "clear":
                    System.out.println("cleared");
                    variables.put(var, 0);
                    System.out.print(var);
                    printTable();
                    break;

                case "decr":
                    if (currentVal > 0) {
                        variables.put(var, currentVal - 1);
                        printTable();
                    }
                    break;

                //For each while loop, a temporary file is created which stores all the text contained in the while loop.
                //Then, the read method is called on the temporary file over and over until the conditions of the while loop have been met.

                case "while":
                    File tempFile = File.createTempFile("temp", ".txt");
                    FileWriter fr = new FileWriter(tempFile);
                    boolean found = false;
                    int count = 1;

                    while (!found){
                        if (eachLine.hasNext("while")) {
                            count++;
                        }

                        if (eachLine.hasNext("end;")){
                            count--;
                        }

                        fr.write(eachLine.nextLine());
                        fr.write("\n");
                        if (count == 0){
                            found = true;
                        }
                    }
                    fr.close();

                    while (variables.get(var) > 0) {
                        read(tempFile);
                    }
                    break;
            }
        }
    }
    public void varCheck(String var){       //This method checks if a variable has been made and if not, adds it to the hashtable
        if (variables.get(var) == null){
            variables.put(var, 0);
            Keys.add(var);
        }
    }
    public void printTable(){       //This method prints a table of all variables that have been instantiated
        System.out.println("--------------------");
        for (String var : Keys){
            System.out.println(var + " : " + variables.get(var));
        }
    }
}
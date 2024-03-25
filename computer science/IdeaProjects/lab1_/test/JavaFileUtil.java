import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.io.ConcatenateJavaFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaFileUtil implements ConcatenateJavaFiles {
    @Override
    public void concatenateJavaFiles(String dirName, String fileName) throws IOException {
        try{
            File file = new File(dirName + File.separator +fileName);
            //File file = new File("C:\\Users\\TLucas\\IdeaProjects\\lab1_\\"+ dirName + "\\" +fileName);
            //System.out.println( "C:\\Users\\TLucas\\IdeaProjects\\lab1_\\"+ dirName + "\\" +fileName);

            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

            //dirName = "C:\\Users\\TLucas\\IdeaProjects\\lab1_\\"+ dirName ;


            System.out.println(dirName);
            List<String> content = new ArrayList<>();
            File dir = new File(dirName);
            String [] fn = dir.list();
            System.out.println("LIst: ");
            for (String s : fn) {
                System.out.println(s);
            }
            // reading files
            for(String itr : fn) {

                if (itr.contains(".java")){
                    //System.out.println("hi");
                    File f = new File(dir, itr);
                    BufferedReader bf = new BufferedReader(new FileReader(f));
                    String reading = bf.readLine();
                    while (reading != null) {
                        content.add(reading);
                        reading = bf.readLine();
                        //System.out.println(content.get(content.size()-1));
                    }
                    bf.close();
                }
            }
            Writer wr = new FileWriter(file);
            for (String temp : content){
                System.out.println(temp);
                wr.write(temp + "\n");

            }
            wr.close();
            System.out.println("bye");
        }


        catch (Exception e){
            throw new IllegalArgumentException("idk");

        }

    }
}

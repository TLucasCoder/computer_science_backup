import org.jetbrains.annotations.NotNull;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.io.ConcatenateJavaFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JavaFileUtil implements ConcatenateJavaFiles {
    @Override
    public void concatenateJavaFiles(String dirName, String fileName) throws IOException {
        try{
            File file = new File(fileName);
/*
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }

 */

            dirName = "C:\\Users\\TLucas\\IdeaProjects\\lab1_\\" + dirName ;
            System.out.println(dirName);
            List<String> content = new ArrayList<>();
            File dir = new File(dirName);
            String [] fn = dir.list();
            System.out.println("LIst: ");
            for (String s : fn) {
                System.out.println(s);
            }
            for(String itr : fn) {

                if (itr.contains(".java")){
                    //System.out.println("hi");
                    File f = new File(dir, itr);
                    BufferedReader bf = new BufferedReader(new FileReader(f));
                    String reading = bf.readLine();
                    content.add(reading);
                    while (reading != null) {
                        reading = bf.readLine();
                        content.add(reading);
                    }
                }
            }
            Writer wr = new FileWriter(file);
            for (String temp : content){
                //System.out.println(temp);
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

import music.Composition;
import music.MusicSheet;
import people.musicians.Cellist;
import people.musicians.Musician;
import people.musicians.Pianist;
import people.musicians.Violinist;
import utils.SoundSystem;

import javax.sound.midi.MidiUnavailableException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class readingFile {
    protected SoundSystem soundSystem;

    public ArrayList<Musician> readMusician(String file_name){
        ArrayList<Musician> aMusic= new ArrayList<Musician>();
        try {
            File file_ = new File(file_name);
            Scanner sc = new Scanner(file_);
            while (sc.hasNextLine()){
                String reading = sc.nextLine();
                String musician_name;
                String type;
                for (int i = 0; i< reading.length(); i++){
                    if (reading.toCharArray()[i]=='('){
                        musician_name = reading.substring(0, i);
                        musician_name = musician_name.trim();
                        type = reading.substring(i+1,reading.length()-1);
                        type = type.trim();
                        soundSystem = new SoundSystem();
                        switch (type) {
                            case "Piano" -> aMusic.add(new Pianist(musician_name, soundSystem));
                            case "Violin" -> aMusic.add(new Violinist(musician_name, soundSystem));
                            case "Cello" -> aMusic.add(new Cellist(musician_name, soundSystem));
                        }
                        break;
                    }
                }
            }

            return aMusic;
        }
        catch(FileNotFoundException | MidiUnavailableException e){
            System.out.println("file not found or sth else");
        }
        return null;
    }

    public ArrayList<MusicSheet> reading_composition (String composition_name){
        ArrayList<MusicSheet> temp_compo = new ArrayList<>();
        try{
            File file_ = new File(composition_name);
            Scanner sc = new Scanner(file_);

            int count = -1;
            String compo_name = "";
            String tempo = "";
            int length_ = 0;
            while (sc.hasNextLine()){
                String temp_read = sc.nextLine();
                if (temp_read.indexOf("Name")==0){
                    count = 0;
                }
                count ++;
                if (count <=3){
                    int index_ = temp_read.indexOf(":");
                    String type = temp_read.substring(0,index_);
                    String content = temp_read.substring(index_+1);

                    type = type.trim();
                    content = content.trim();
                    switch (type){
                        case "Name" -> compo_name = content;
                        case "Tempo" -> tempo = content;
                        case "Length" -> length_ = Integer.parseInt(content);
                    }
                    if (count == 3){
                        temp_compo.add(new MusicSheet(compo_name,tempo,length_));
                    }
                }
                else{
                    String ins_type = "";
                    Boolean soft_c = false;
                    List<String> sheet;
                    int checking = 0;
                    while (checking <3){
                        String part = "";
                        if (checking !=2) {
                            part = temp_read.substring(0, temp_read.indexOf(","));
                            temp_read = temp_read.substring(temp_read.indexOf(",") + 1);
                            temp_read = temp_read.trim();
                        }
                        else{
                            part = temp_read;
                        }
                        if (checking == 0){
                            // getting the instrument type
                            ins_type = part;
                        }
                        else if (checking ==1){
                            // getting if it is soft
                            if (part.equals("soft")){
                                soft_c = true;
                            }
                            else{
                                soft_c = false;
                            }
                        }
                        else{
                            // reading the notes

                            part = part.substring(1,part.length()-1);
                            String [] note = part.split(",");
                            for (String no : note){
                                no = no.trim();
                            }
                            // from array to arraylist
                            sheet =  Arrays.asList(note);
                            ins_type = ins_type.trim();
                            temp_compo.get(temp_compo.size()-1).addScore(ins_type,sheet,soft_c);
                        }
                        checking++;

                    }

                }

            }
            return temp_compo;
        }
        catch (FileNotFoundException e){
            System.out.println("file not found");
            return null;
        }
    }


}

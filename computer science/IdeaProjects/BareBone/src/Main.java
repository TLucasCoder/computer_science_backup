import java.io.*;
import java.lang.reflect.Array;
import java.util.*;
public class Main {
    HashMap<String, Integer> variable = new HashMap<String, Integer>();
    HashMap<Integer, String> comment = new LinkedHashMap<>();
    // check the if else statement
    Stack<Integer> if_check = new Stack<>();
    ArrayList<String> varName = new ArrayList<String>();
    List<List> while_loop = new ArrayList<List>();
    Stack<Integer> point = new Stack<Integer>();


    int endCheck = 0;
    int ac = 0;

    public void addition_s(String key_1, Integer value_1){
        variable.put(key_1, variable.get(key_1) + value_1);
    }
    public void subtraction(String key_1, Integer value_1){
        variable.put(key_1, variable.get(key_1) - value_1);

    }
    public void multiplication(String key_1, Integer value_1){
        variable.put(key_1, variable.get(key_1) * value_1 );

    }
    public void division(String key_1, Integer value_1){
        variable.put(key_1, variable.get(key_1) / value_1 );

    }

    public void trans(String a, int line) {

        String[] as = a.split(" ");
        ArrayList<String> acc = new ArrayList<>();
        int lenCheck = 0;
        while (lenCheck< as.length){
            acc.add(as[lenCheck]);
            lenCheck++;
        }
        //System.out.println(as.length);
        try {
            /*
            System.out.print("code to be exe: ");
            for (int i = 0; i < as.length; i++) {
                System.out.print(as[i]+ " ");
            }
            System.out.println();


             */
            if (if_check.size()>0 ){
                //System.out.println("if_check: "+if_check.peek());
                if (if_check.peek()==0 && !acc.get(0).contains("if") && !acc.get(0).equals("else")){
                    return;
                }
            }
            // interpreting different syntax and operations
            switch (acc.get(0)) {
                case "clear":
                    variable.put(acc.get(1), 0);
                    break;
                case "incr":

                    variable.put((acc.get(1)), variable.get(acc.get(1)) + 1);

                    break;
                case "decr":
                    variable.put((acc.get(1)), variable.get(acc.get(1)) - 1);
                    break;
                case "while":
                    if (acc.get(1) != acc.get(3)) {
                        varName.add(acc.get(1));
                        List list1 = new ArrayList<Integer>();
                        list1.add(variable.get(acc.get(1)));
                        //System.out.println(list1.add(variable.get(acc[1])));
                        list1.add(Integer.parseInt(acc.get(3)));
                        while_loop.add(list1);
                        point.add(line + 1);
                    }

                    break;
                case "endif":
                    if_check.pop();
                    break;
            }

            if(acc.size()>1 || acc.get(0).equals("else")) {
                if (acc.get(0).equals("if") || acc.get(0).equals("else")) {
                    if (acc.get(0).equals("if")){
                        if_check.add(0);
                    }
                    int it = 1;
                    int temp = 0;
                    if (acc.size()>1) {
                        if (acc.get(1).equals("if")) {
                            it++;
                        }
                    }
                    else{
                        if (acc.get(0).equals("else")){
                            if_check.add(1);

                        }
                    }
                    if (acc.size()>1) {
                        while (!acc.get(it).contains("=") && !acc.get(it).contains(">") && !acc.get(it).contains("<")) {
                            if (variable.containsKey(acc.get(it))) {
                                temp = variable.get(acc.get(it));
                            } else {
                                temp = Integer.parseInt(acc.get(it));
                            }
                            if (acc.get(it).equals("+") || acc.get(it).equals("-") || acc.get(it).equals("*") || acc.get(it).equals("/")) {
                                if (variable.containsKey(acc.get(it + 1))) {
                                    switch (acc.get(it)) {
                                        case "+":
                                            temp += variable.get(it + 1);
                                            break;
                                        case "-":
                                            temp -= variable.get(it + 1);
                                            break;
                                        case "*":
                                            temp *= variable.get(it + 1);
                                            break;
                                        case "/":
                                            temp /= variable.get(it + 1);
                                            break;
                                    }
                                } else {
                                    switch (acc.get(it)) {
                                        case "+":
                                            temp += Integer.parseInt(acc.get(it + 1));
                                            break;
                                        case "-":
                                            temp -= Integer.parseInt(acc.get(it + 1));
                                            break;
                                        case "*":
                                            temp *= Integer.parseInt(acc.get(it + 1));
                                            break;
                                        case "/":
                                            temp /= Integer.parseInt(acc.get(it + 1));
                                            break;
                                    }
                                }
                            }
                            it++;

                        }

                        // checking if the ' if else ' statement is valid

                        switch (acc.get(it)) {
                            case "==":

                                if (temp == Integer.parseInt(acc.get(it + 1))) {
                                    if_check.pop();
                                    if_check.add(1);
                                }
                                break;
                            case ">":
                                if (temp > Integer.parseInt(acc.get(it + 1))) {
                                    if_check.pop();
                                    if_check.add(1);
                                }
                                break;
                            case "<":
                                if (temp < Integer.parseInt(acc.get(it + 1))) {
                                    if_check.pop();
                                    if_check.add(1);
                                }
                                break;
                            case "<=":
                                if (temp <= Integer.parseInt(acc.get(it + 1))) {
                                    if_check.pop();
                                    if_check.add(1);
                                }
                                break;
                            case ">=":
                                if (temp >= Integer.parseInt(acc.get(it + 1))) {
                                    if_check.pop();
                                    if_check.add(1);
                                }
                                break;
                            case "!=":
                                if (temp != Integer.parseInt(acc.get(it + 1))) {
                                    if_check.pop();
                                    if_check.add(1);
                                }
                                break;
                        }
                    }

                }
                else if (acc.get(1).contains("=")) {

                    switch (acc.get(3)) {
                        case "+":
                            if (variable.containsKey(acc.get(4))) {
                                addition_s(acc.get(2), variable.get(acc.get(4)));
                            }
                            else{
                                addition_s(acc.get(2), Integer.parseInt(acc.get(4)));
                            }
                            break;
                        case "-":
                            if (variable.containsKey(acc.get(4))) {
                                subtraction(acc.get(2), variable.get(acc.get(4)));
                            }
                            else{
                                subtraction(acc.get(2), Integer.parseInt(acc.get(4)));
                            }
                            break;
                        case "*":
                            if (variable.containsKey(acc.get(4))) {
                                multiplication(acc.get(2), variable.get(acc.get(4)));
                            }
                            else{
                                multiplication(acc.get(2), Integer.parseInt(acc.get(4)));
                            }
                            break;
                        case "/":
                            if (variable.containsKey(acc.get(4))) {
                                division(acc.get(2), variable.get(acc.get(4)));
                            }
                            else{
                                division(acc.get(2), Integer.parseInt(acc.get(4)));
                            }
                            break;

                    }


                }
            }


            for (String ii : variable.keySet()) {
                System.out.println("Value of " + ii + " is: " + variable.get(ii));
            }
            System.out.println();

            // update the value of variables in while_loop
            if (varName.size() > 0) {
                //System.out.println(varName);
                for (int y = 0; y < varName.size(); y++) {
                    int k = variable.get(varName.get(y));
                    while_loop.get(y).set(0, k);
                }
            }
        }
        catch(Exception e){
            System.out.println("error found");
        }
    }
    // finding end for recursion
    public void checkEnd(String dataa, String[] data, int b) {
        if (dataa.equals("end")) {
           // int m = 10;
            while (  while_loop.get(while_loop.size() - 1).get(0) != while_loop.get(while_loop.size() - 1).get(1)) {

                for (int seek = point.peek(); seek < b; seek++) {
                        trans(data[seek], seek);
                        if (data[seek].equals("end")) {
                            checkEnd(dataa, data, seek);
                        }

                }
            }
            point.pop();
            while_loop.remove(while_loop.size() - 1);
            varName.remove(varName.size() - 1);
        }
    }
// function for running the program
    public void execution(){
        Readfile rf = new Readfile();
        rf.reading();
        String data_1 = rf.Getdata();
        String[] data = data_1.split(";");
        for( int i =0; i < data.length ;i++) {
            while(data[i].startsWith(" ")){
                data[i] = data[i].substring(1);
                //System.out.println(data[i]);
            }
            if(data[i].contains("//")){
                comment.put(i,data[i]);
                data[i]= "";
            }
        }
        int b = 0;

        for (int c= 0; c< data.length; c++) {
            trans(data[c], c);
            checkEnd(data[c],data, c);
        }
        System.out.println(comment.size() + " of comment found.");
        for (Integer ii : comment.keySet()){
            System.out.println("Comment on line " + ii + ": " + comment.values());
        }

        // search for result

    }
    // main program
    public static void main(String[] args) {
        Main exe = new Main();
        exe.execution();
    }
}
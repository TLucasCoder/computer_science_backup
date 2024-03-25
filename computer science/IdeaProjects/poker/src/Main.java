import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void display(ArrayList<user_> user_s){
        for(int j = 0; j< user_s.size();j++){
            System.out.print("Player " + (j+1) + ": ");
            System.out.print(user_s.get(j).getValue());
            System.out.print("  ");
        }
        System.out.println();

    }
    public static void main(String[] args) {
        final int constant_ = 20;
        ArrayList<user_> User = new ArrayList<>();
        Scanner reader_ = new Scanner(System.in);
        System.out.print("enter the number of player: ");
        int answer = Integer.parseInt(reader_.nextLine());
        System.out.print("Manual input?(y/n) ");
        String anss = reader_.nextLine();
        if (anss.equals("y")){
            for (int i = 0; i< answer ; i++){
                System.out.print("Input value "+ (i+1) + ": ");
                int inputTT = Integer.parseInt(reader_.nextLine());
                User.add(new user_(inputTT));
            }
        }
        else {
            for (int i = 0; i < answer; i++) {
                User.add(new user_(constant_));
            }
        }
        boolean game_end = false;
        while (game_end == false){
            int pool = 0;
            System.out.println("====================================================");
            System.out.println("Round "+ (1));
            System.out.println("====================================================");
            display(User);
            for (int k=0;k< User.size();k++){
                    User.get(k).change_value(-1);
                    pool+=1;


            }

            for (int round = 1; round < 4; round++){
                System.out.println("====================================================");
                System.out.println("Round "+ (round+1));
                System.out.println("====================================================");
                display(User);
                System.out.print("Input bet:(input integer) ");
                int answer__ = Integer.parseInt(reader_.nextLine());
                for (int k=0;k< User.size();k++){
                    System.out.print("Player "+ (k+1) +" bet?(y/n) ");
                    String ans = reader_.nextLine();
                    if(ans.equals("y")){
                        User.get(k).change_value(-answer__);
                        pool+=answer__;
                    }

                }
                System.out.println("The pool is: " + pool);


            }
            System.out.print("Winner: ");
            answer = Integer.parseInt(reader_.nextLine());
            User.get(answer-1).change_value(pool);
            System.out.println("====================================================");
            display(User);
            System.out.println("====================================================");
            System.out.print("Next round?(y/n) ");
            String responses = reader_.nextLine();
            if (responses.equals("n")){
                game_end = true;
                System.out.println("====================================================");
                System.out.println("====================================================");
                System.out.println("Final Standing: ");
                display(User);
                System.out.println("====================================================");
                break;
            }
            for (int k = 0; k< User.size();k++){
                if (User.get(k).getValue()>0){
                    continue;
                }
                else if (User.get(k).getValue()<1){
                    System.out.print("Player "+ (k+1) +" Borrow?(y/n) ");
                    String response = reader_.nextLine();
                    if (response.equals("y")){
                        User.get(k).change_value(10);
                        User.get(k).borrowing(10);
                    }

                }
            }
        }

    }
}
//package lab3part3.lab3part2;


public class ATM {
    public Toolbox myToolbox = new Toolbox();
    public int balance;
    public boolean accountSetUp = false;

    public void go() {

        while (true) {

            if (!accountSetUp) {
                System.out.println("Welcome to online ATM banking");
                System.out.println("How much do you want in your account?");

                // update balance
                int newBalance = myToolbox.readIntegerFromCmd();
                if (newBalance >= 0) {
                    balance = newBalance;
                    accountSetUp = true;
                }


            } else {
                // give user options
                System.out.println("What do you want to do?");
                System.out.println("1 : Withdraw");
                System.out.println("2 : Deposit");
                System.out.println("3 : Inquire");
                System.out.println("4 : Quit");

                int choice = myToolbox.readIntegerFromCmd();
                if (choice == 1) {
                    withdraw();
                } else if (choice == 2) {
                    deposit();
                } else if (choice == 3) {
                    inquire();
                } else {
                    quit();
                }
            }
        }
    }

    public void title(String title_name) {
        System.out.println("*****************************************");
        System.out.println(title_name);
        System.out.println("*****************************************");
    }

    public void withdraw() {
        title("Withdrawal");
        System.out.println("How much would you like to withdraw?");
        boolean valid = false;
        while (!valid){
            int amount = myToolbox.readIntegerFromCmd();
            if (amount >= 0 && balance - amount >= 0) {
                balance -= amount;
                title("Your new balance is " + balance);
                valid = true;
            }
        }
    }

    public void deposit() {
        title("Deposit");
        System.out.println("How much would you like to deposit?");
        boolean valid = false;
        while (!valid){
            int amount = myToolbox.readIntegerFromCmd();
            if (amount >= 0) {
                balance += amount;
                title("Your new balance is " + balance);
                valid = true;
            }
        }
    }
    public void inquire() {
        title("Your balance is " + balance);
    }
    public  void quit() {
        title("GoodBye!");
        System.exit(0);
    }


    public static void main(String[] args) {
        ATM myATM = new ATM();
        myATM.go();
    }
}
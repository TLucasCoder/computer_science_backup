public class user_ {
    protected int balance;
    protected int borrow;
    public user_ (int input_){
        this.balance = input_;
    }


    public void borrowing(int input__){
        this.borrow += input__;
    }

    public void change_value(int input_){
        this.balance = this.balance + input_;
    }
    public int getValue(){
        return this.balance;
    }
}

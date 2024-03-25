import org.checkerframework.checker.units.qual.K;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.recursion.PalindromeChecker;

public class KPalindrome implements PalindromeChecker {
/*
    public static void main(String[] args){
        String k = "ABCDEFCBA";
        KPalindrome acc = new KPalindrome();
        System.out.println("The result is: "+ acc.isKPalindrome(k,2));
    }

 */
    @Override
    public boolean isKPalindrome(String s, int i) {
        //System.out.println("s: " + s);
        // all pairs are removed

        // can't form a palindrome in removing at most k character
        if (i < 0){
            return false;
        }
        if (s.equals("")|| s.length()==1){
            return true;
        }
        String [] temp = {"",""} ;
        char [] car = s.toCharArray();
        if (car[0]!= car[car.length-1]){
            temp[0] = s.substring(1);
            temp[1] = s.substring(0,s.length()-1);
            //System.out.println("temp[0]: "+temp[0]);
            //System.out.println("temp[1]: "+temp[1]);
            return (isKPalindrome(temp[0],i-1)|| isKPalindrome(temp[1],i-1));
        }
        temp[0] = s.substring(1,s.length()-1);
        //System.out.println("temp[0]: "+temp[0]);
        return (isKPalindrome(temp[0],i));
    }
}

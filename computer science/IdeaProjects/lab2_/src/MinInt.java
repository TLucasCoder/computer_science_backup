import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.recursion.MinimumInArray;

import java.util.Arrays;

public class MinInt implements MinimumInArray {

    public static void main(String[] numbers){
        int[] arr = {24,52,74,9,34,23,64,34,8};
        MinInt minInt = new MinInt();
        System.out.println("Minimum is: " + minInt.findMin(arr));
    }
    public int min(int first, int second){
        if (first < second){
            return first;
        }
        return second;
    }


    public int findMin(int[] array){
        int n = array.length;
		if (n == 2){
            return min(array[0],array[1]);
        }
        if (n == 1){
            return array[0];
        }
        int [] fir_arr = Arrays.copyOfRange(array,0, (n+1)/2);
        int [] sec_arr = Arrays.copyOfRange(array, (n+1)/2 , n );

        //int [] fir_arr = new int[(n+1)/2];
        //int [] sec_arr = new int[n-fir_arr.length];

        return min(findMin(fir_arr),findMin(sec_arr));
    }

}
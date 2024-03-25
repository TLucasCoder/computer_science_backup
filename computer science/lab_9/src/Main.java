import static org.junit.jupiter.api.Assertions.assertTrue;

public class Main {
    public static void main(String [] args) throws InvalidNodeException, CycleDetectedException {
        int [][] testGraph =  {{},null};
        int [] sorted = DAGSort.sortDAG(testGraph);
        for (int i = 0; i< testGraph.length; i++){
            for (int j : testGraph[i]){
                int pos_upper = -1;
                int pos_lower = -1;
                for (int k = 0; k < sorted.length;k++){
                    if (sorted[k] == i){
                        pos_upper = k;
                    }
                    else if (sorted[k] == j){
                        pos_lower = k;
                    }

                    if (pos_lower!=-1 && pos_upper!=-1){
                        break;
                    }


                }
                System.out.println("pos_upper:" + pos_upper);
                System.out.println("pos_lower:" + pos_lower);
            }
        }
        for (int i : sorted){
            System.out.println(i);
        }

    }
}
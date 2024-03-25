import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DAGSortTest {
    @Test
    void nullPointerExceptionTest() throws RuntimeException{
        int [][] testGraph = {null};
        NullPointerException npe = assertThrows(NullPointerException.class, () -> {
            DAGSort.sortDAG(testGraph);
        },"the list should not be null");
    }

    @Test
    void cycleDetectedExceptionTest()throws RuntimeException{
        int [][] testGraph = {{0},{0}};
        int [][] testGraph_2 = {{1,2},{2},{0}};
        CycleDetectedException npe = assertThrows(CycleDetectedException.class, () -> {
            DAGSort.sortDAG(testGraph);
        },"Cycle should not appear");
        CycleDetectedException npec = assertThrows(CycleDetectedException.class, () -> {
            DAGSort.sortDAG(testGraph_2);
        },"Cycle should not appear");
    }

    @Test
    void invalidNodeExceptionTest()throws RuntimeException{
        int [][] testGraph = {{2}};
        int[][] testGraph_1 = {{-1}};
        InvalidNodeException npe = assertThrows(InvalidNodeException.class, () -> {
            DAGSort.sortDAG(testGraph);
        },"Node index bigger than the number of nodes should be thrown");
        InvalidNodeException npec = assertThrows(InvalidNodeException.class, () -> {
            DAGSort.sortDAG(testGraph_1);
        },"Node index below 0 should be thrown");

    }
    @Test
    void correctLengthTest() throws InvalidNodeException, CycleDetectedException,RuntimeException {
        int [][] testGraph = {{3},{3,4},{4,7},{5,6,7},{6},{},{},{}};
        int [] sorted = DAGSort.sortDAG(testGraph);
        assertEquals(testGraph.length,sorted.length,"The size of the input and output is different");
    }

    @Test
    void correctOrderTest() throws InvalidNodeException, CycleDetectedException,RuntimeException {
        int [][] testGraph = {{3},{3,4},{4,7},{5,6,7},{6},{},{},{}};
        //int [][] testGraph = {{},{2},{3},{}};
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
                if (pos_lower!=-1 && pos_upper!=-1){
                    assertTrue(pos_upper<pos_lower,"the order of the sorted result is not correct ");
                } else {
                    // check if missing any nodes in the method
                    fail("missing required node(s)");
                }


            }
        }

    }

    @Test
    void duplicationNodeTest() throws InvalidNodeException, CycleDetectedException, RuntimeException {
        int [][] testGraph = {{3,3,3,3},{3,4},{4,7,7,7,7},{5,6,6,6,7},{6},{},{},{}};
        int [] sorted = DAGSort.sortDAG(testGraph);

        int [][] testGraph_1 = {{3},{3,4},{4,7},{5,6,7},{6},{},{},{}};
        int [] sorted_1 = DAGSort.sortDAG(testGraph_1);

        for (int i = 0; i < sorted.length; i++){
            int j = i+1;
            while (j < sorted.length){
                if (sorted[i]==sorted[j]){
                    assertTrue(sorted[i]!=sorted[j],"there should be no duplication in the list");
                    break;
                }
                j++;
            }
        }
        for (int i = 0; i < sorted_1.length; i++){
            int j = i+1;
            while (j < sorted_1.length){
                if (sorted_1[i]==sorted_1[j]){
                    assertTrue(sorted_1[i]!=sorted_1[j],"there should be no duplication in the list");
                    break;
                }
                j++;
            }
        }

    }
}

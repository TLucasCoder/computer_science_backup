import uk.ac.soton.ecs.comp1206.labtestlibrary.datastructure.Tree;
import uk.ac.soton.ecs.comp1206.labtestlibrary.interfaces.recursion.MinimumInTree;

public class MinTree implements MinimumInTree {
final int con = 2147483647;
    public static void main(String[] args){
        Tree tree = new Tree( 1,
                        new Tree( 45, null ,
                                new Tree(8, null , null) ) ,
                        new Tree ( 8,
                                new Tree (4 , null , null ) ,
                                null ) );
        MinTree minTree = new MinTree();
        System.out.println("Minimum is: " + minTree.findMin(tree));
    }

    public int min(int first, int second){
        if (first < second){
            return first;
        }
        return second;
    }

    public int findMin(Tree tree){
        int temp_0,temp_1 = 0;
        if (tree == null){
            return con;
        }
        if (tree.left()==(null) && tree.right()==(null)){
            return tree.getVal();
        }

        //return min(min(tree.getVal(),tree.left().getVal()),min(tree.getVal(),tree.right().getVal()));
        return min(tree.getVal(), min(findMin(tree.left()),findMin(tree.right())));
    }

}
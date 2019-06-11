import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;

/**
 * Created by Bradley Pham on 11/17/2017.
 */
public class ShortestCommonAncestor {


    private Digraph graph; //to make it immutable
//    private int root; //to store the root of the graph

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null)
            throw new NullPointerException();
        graph = new Digraph(G);
        //checking the graph if it has cycle
        DirectedCycle graphCycle = new DirectedCycle(graph);
        if (graphCycle.hasCycle())
            throw new IllegalArgumentException();
        //checking if the graph has only 1 root
        int numOfRoot = 0;
        for (int i = 0; i < graph.V(); i++) {
            if (graph.outdegree(i) == 0) {
                numOfRoot++;
            }
        }
        if (numOfRoot != 1)
            throw new IllegalArgumentException();
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (!(isValid(v) || isValid(w)))
            throw new IndexOutOfBoundsException();
        DeluxeBFS findingLength = new DeluxeBFS(graph);
        return findingLength.findShortestLength(v, w);
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (!(isValid(v) || isValid(w)))
            throw new IndexOutOfBoundsException();

        DeluxeBFS findingAncestor = new DeluxeBFS(graph);
        return findingAncestor.findShortestAncestor(v, w);
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int length(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (!isValid(subsetA, subsetB))
            throw new IndexOutOfBoundsException();
        DeluxeBFS findingLength = new DeluxeBFS(graph);
        return findingLength.findingShortestLength(subsetA, subsetB);

    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (!isValid(subsetA, subsetB))
            throw new IndexOutOfBoundsException();

        DeluxeBFS findingAncestor = new DeluxeBFS(graph);
        return findingAncestor.findShortestAncestor(subsetA, subsetB);

    }

    private boolean isValid(int v) {
        return !(v < 0 || v > graph.V());
    }
    private boolean isValid(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new NullPointerException();
        Queue<Integer> setA = new Queue<>();
        Queue<Integer> setB = new Queue<>();
        for (int v : subsetA) {
            setA.enqueue(v);
            if (v < 0 || v > graph.V())
                return false;
        }
        for (int w : subsetB) {
            setB.enqueue(w);
            if (w < 0 || w > graph.V())
                return false;
        }
        if (setA.isEmpty() || setB.isEmpty())
            throw new IllegalArgumentException();
        return true;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);
        /*while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();

            int length = sca.length(v, w);

            double start = System.nanoTime();
            int ancestor = sca.ancestor(v, w);
            double end = System.nanoTime();
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
            StdOut.printf("Time taken: %2f\n", end-start);
        }*/
        while (!StdIn.isEmpty()) {
            Queue<Integer> subsetA = new Queue<>();
            Queue<Integer> subsetB = new Queue<>();
            int input = StdIn.readInt();
            while (!(input == -1)) {
                subsetA.enqueue(input);
                input = StdIn.readInt();
            }
            input = StdIn.readInt();
            while (!(input == -1)) {
                subsetB.enqueue(input);
                input = StdIn.readInt();
            }
            int length = sca.length(subsetA, subsetB);
            int ancestor = sca.ancestor(subsetA, subsetB);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;

/**
 * Created by Bradley Pham on 11/18/2017.
 */
/* This class is to find path from only a specific source @param s to all reachable vertices.
   The graph passed in must be Rooted DAG */
public class DeluxeBFS {

    private Digraph graph; //to make it immutable
    private int[] distanceTo1; //to have the length from the sca to one source
    private int[] distanceTo2; //to have the length from the sca to the other source

    public DeluxeBFS(Digraph G) {
        graph = new Digraph(G);
        distanceTo1 = new int[graph.V()];
        distanceTo2 = new int[graph.V()];
        for (int i = 0; i < graph.V(); i++) {
            distanceTo1[i] = Integer.MAX_VALUE;
            distanceTo2[i] = Integer.MAX_VALUE;
        }
    }

    //get path from source v and w using bfs algorithm, return the shortest common ancestor whenever two paths have a hit
    //The graph shouldn't have any bi-directional relationship
    private int findAncestor(int v, int w) {

        boolean[] fromFirstSource = new boolean[graph.V()]; //to differentiate the source
        boolean[] isMarked = new boolean[graph.V()];
        Queue<Integer> checked = new Queue<>();
        //start with the first point
        checked.enqueue(v);
        isMarked[v] = true;
        fromFirstSource[v] = true;
        distanceTo1[v] = 0;
        //start with the second point
        checked.enqueue(w);
        isMarked[w] = true;
        distanceTo2[w] = 0;

        while(!checked.isEmpty()) {
            int consideredV = checked.dequeue();
            for (int neighbour : graph.adj(consideredV)) {
                if (neighbour == w || neighbour == v) { //there is direct path from v -> w with distance 1
                    if (neighbour == w) {
                        distanceTo1[neighbour] = distanceTo1[consideredV] + 1;
                    } else {
                        distanceTo2[neighbour] = distanceTo2[consideredV] + 1;
                    }
                    return neighbour;
                }
                if (fromFirstSource[consideredV]) {
                    if (distanceTo1[neighbour] > distanceTo1[consideredV] + 1)
                        distanceTo1[neighbour] = distanceTo1[consideredV] + 1;
                    if (!isMarked[neighbour])
                        fromFirstSource[neighbour] = true;
                } else {
                    if (distanceTo2[neighbour] > distanceTo2[consideredV] + 1)
                        distanceTo2[neighbour] = distanceTo2[consideredV] + 1;
                }
                if (!isMarked[neighbour]) {
                    isMarked[neighbour] = true;
                    checked.enqueue(neighbour);
                } else { //if we have a hit
                    if ((fromFirstSource[consideredV] && !fromFirstSource[neighbour])
                        || (fromFirstSource[neighbour] && !fromFirstSource[consideredV])) {
                        for (int n : graph.adj(consideredV)) {//if its neighbour list contains the other point, prioritise that one
                            if (n == w || n == v)
                                return (n == w) ? w : v;
                        }
                        return neighbour;
                    }
                }
            }
        }
        return 0;
    }

    //algorithm: run bfs for one point up to the root. Then start checking the other point. Whenever the other point's path reaches
    //a marked point, that is the shortest common ancestor and we stop searching
    public int findShortestAncestor(int v, int w) {
        if (v == w)
            return v;
        return findAncestor(v, w);
    }

    public int findShortestLength(int v, int w) {
        if (v == w)
            return 0;
        //find the shortest common ancestor
        int sca = findShortestAncestor(v, w);
        return distanceTo1[sca] + distanceTo2[sca];
    }

    //Algorithm: Similar to considering 2 points only, we run bfs for subsetA first and then check all the points in subset B
    //if we have any hit, we have to check if that is the shorter one than the current one
    public int findShortestAncestor(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {

        //run bfs for whole subsetA, but we have to update if there is shorter path to the marked points
        boolean[] isMarked = new boolean[graph.V()];
        int oldDistanceSA = -1;
        Queue<Integer> checkedA = new Queue<>();
        for (int sources : subsetA) {
            checkedA.enqueue(sources);
            isMarked[sources] = true;
            distanceTo1[sources] = 0;
        }
        while(!checkedA.isEmpty()) {
            int consideredV = checkedA.dequeue();
            for (int neighbour : graph.adj(consideredV)) {
                if (!isMarked[neighbour]) {
                    isMarked[neighbour] = true;
                    distanceTo1[neighbour] = distanceTo1[consideredV] + 1;
                    checkedA.enqueue(neighbour);
                } else {
                    //update the distance to a considered point if there is shorter distance from one of the subset's points
                    if (distanceTo1[neighbour] <= oldDistanceSA) {
                        distanceTo1[neighbour] = distanceTo1[consideredV] + 1;
                        oldDistanceSA = distanceTo1[consideredV] + 1;
                    }
                }
            }
        }

        //Now running through all the subset B
        boolean[] isMarkedBPath = new boolean[graph.V()]; //not to repeat an already reached point
        int sca = -1; //to store possible sca
        int oldLength = Integer.MAX_VALUE; //to store the distance to current sca from two sources
        Queue<Integer> checkedB = new Queue<>();
        for (int v : subsetB) {
            distanceTo2[v] = 0;
            if (isMarked[v]) {//if subsetB contains at least one similar point as A
                if (sca == -1) {
                    sca = v;
                    oldLength = distanceTo1[v] + distanceTo2[v];
                } else {
                    if (distanceTo1[v] + distanceTo2[v] < oldLength) {
                        sca = v;
                        oldLength = distanceTo1[v] + distanceTo2[v];
                    }
                }
                isMarkedBPath[v] = true;
                checkedB.enqueue(v);
            } else if (!isMarkedBPath[v]) {
                isMarkedBPath[v] = true;
                checkedB.enqueue(v);
            }
        }

        while(!checkedB.isEmpty()) {
            int consideredV = checkedB.dequeue();
            for (int neighbour : graph.adj(consideredV)) {
                if (!isMarkedBPath[neighbour]) {
                    isMarkedBPath[neighbour] = true;
                    distanceTo2[neighbour] = distanceTo2[consideredV] + 1;
                    if (isMarked[neighbour]) { //found a hit between 2 subsets
                        //check if the distance of this point in subsetB is shorter than current shortest length
                        if (distanceTo1[neighbour] + distanceTo2[neighbour] < oldLength) {
                            oldLength = distanceTo1[neighbour] + distanceTo2[neighbour];
                            sca = neighbour;
                        }
                    } else {
                        checkedB.enqueue(neighbour);
                    }
                } else { //meet a point already marked on path from B, update the shortest path from B to that point
                    if (distanceTo2[neighbour] > distanceTo2[consideredV] + 1) {
                        distanceTo2[neighbour] = distanceTo2[consideredV] + 1;

                    }
                }
            }
        }
        return sca;
    }

    public int findingShortestLength(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        int sca = findShortestAncestor(subsetA, subsetB);
        int distanceFromA = distanceTo1[sca];
        int distanceFromB = distanceTo2[sca];
        return distanceFromA + distanceFromB;
    }

}

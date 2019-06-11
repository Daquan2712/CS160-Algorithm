import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Hashtable;
import java.util.Scanner;

/**
 * Created by Bradley Pham on 11/4/2017.
 */
public class Solver {

    private class SearchNode implements Comparable<SearchNode> {
        private int currentMoves;
        private Board board;
        private int priorityInQueue;
        private SearchNode previous;

        private SearchNode(Board currentBoard, SearchNode previousNode) {
            board = currentBoard;
            if (previousNode != null)
                currentMoves = previousNode.currentMoves + 1;
            else
                currentMoves = 0;
            priorityInQueue = currentMoves + board.manhattan();
            previous = previousNode;
        }
        private int priority() {
            return priorityInQueue;
        }
        public int compareTo(SearchNode s) {
            if (this.priorityInQueue == s.priority())
                return 0;
            else if (this.priorityInQueue < s.priority())
                return -1;
            else
                return 1;
        }
    }

    //instance variables
    private Board initialBoard;
    private MinPQ<SearchNode> gameTree = new MinPQ<>(); //to build the game tree and use A* algorithm
    private Stack<Board> solution = new Stack<>(); //to trace back the shortest path to solution
    private int numOfMoves = 0; //the number of moves needed to lead to goal board
    private Hashtable<Board, Integer> seenBoard = new Hashtable<>(); //to store seen boards

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException();
        if (!(initial.isSolvable())) {
            throw new IllegalArgumentException("Board is unsolvable");
        }

        //to enqueue the initial board
        SearchNode initialNode = new SearchNode(initial, null);
        gameTree.insert(initialNode);
        seenBoard.put(initialNode.board, 0);
        boolean finished = false;

        //keep on doing until we dequeue the goal board
        while(!finished) {
            SearchNode current = gameTree.delMin();

            numOfMoves = current.currentMoves;
            if (current.board.isGoal()) {
                finished = true;
                SearchNode trace = current;
                //to build the path to the solution
                while (trace != null) {
                    solution.push(trace.board);
                    trace = trace.previous;
                }
            }

            //to enqueue the neighbours which haven't been seen
            for (Board w : current.board.neighbors()) {
                SearchNode temp = new SearchNode(w, current);
                if (!seenBoard.containsKey((Board)w)) {
                    gameTree.insert(temp);
                    seenBoard.put(w, temp.currentMoves);
                }
            }
        }

    }

    // min number of moves to solve initial board
    public int moves() {
        return numOfMoves;
    }
    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return solution;
    }
    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        int size = input.nextInt();
        int[][] tiles = new int[size][size];

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                tiles[row][col] = input.nextInt();
            }
        }

        long startTime = System.currentTimeMillis();
        Board testBoard = new Board(tiles);
        Solver solving = new Solver(testBoard);
        System.out.println("Minimum number of moves: " + solving.moves());
        for(Board w : solving.solution()) {
            System.out.println(w.toString());
        }
        long endTime = System.currentTimeMillis();
        System.out.printf("%2f", (endTime - startTime)/1000.0);

    } // unit testing
}

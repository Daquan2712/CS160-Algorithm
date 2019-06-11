import edu.princeton.cs.algs4.Queue;

import java.util.Arrays;
import java.util.Scanner;

/**
 * Created by Bradley Pham on 11/4/2017.
 */
public class Board {

    private int[][] boardTiles; //to make the data type immutable
    private int boardSize;
    private int blankRow, blankCol; //to store blank space position
    private int hamming = -1; //to cache
    private int manhattan = -1; //to cache
    private int inversion = -1; //to cache
    private boolean isSolvable; //to cache
    private int hashCode = -1; //to cache

    // construct a board from an N-by-N array of tiles
    // (where tiles[i][j] = tile at row i, column j)
    public Board(int[][] tiles) {
        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        boardSize = tiles.length;
        boardTiles = new int[boardSize][boardSize];
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (tiles[row][col] < 0)
                    throw new IllegalArgumentException();
                if (tiles[row][col] == 0) {
                    blankRow = row;
                    blankCol = col;
                }
                boardTiles[row][col] = tiles[row][col];
            }
        }
    }

    // return tile at row i, column j (or 0 if blank)
    public int tileAt(int i, int j) {
        if (i >= boardSize || j >= boardSize)
            throw new IndexOutOfBoundsException();
        return boardTiles[i][j];
    }

    // board size N
    public int size() {
        return boardSize;
    }

    // number of tiles out of place
    public int hamming() {
        if (hamming != -1)
            return hamming;
        //reset hamming;
        hamming = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardTiles[row][col] == 0)
                    continue;
                if (boardTiles[row][col] != boardSize*row + col + 1)
                    hamming++;
            }
        }
        return hamming;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        if (manhattan != -1) {
            return manhattan;
        }
        //reset manhattan;
        manhattan = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardTiles[row][col] == boardSize*row + 1 + col)
                    continue;
                if (boardTiles[row][col] == 0)
                    continue;

                //check if it is in the right row
                int supposedRow;
                //in case the number is the max col number of any row, as our rows start as 0
                if (boardTiles[row][col] % boardSize == 0) {
                    supposedRow = (boardTiles[row][col] / boardSize) - 1;
                } else {
                    supposedRow = boardTiles[row][col] / boardSize;
                }
                manhattan += Math.abs(supposedRow - row);
                //check if it is in the right col
                int supposedCol = boardTiles[row][col] - (boardSize*supposedRow + 1);
                manhattan += Math.abs(supposedCol - col);
            }
        }
        return manhattan;
    }
    // is this board the goal board?
    public boolean isGoal() {
        //in case either of them has already been called
        if ((hamming != -1) || (manhattan != -1))
            return (hamming == 0) || (manhattan == 0);
        else {
            return (this.hamming() == 0);
        }
    }

    // is this board solvable?
    public boolean isSolvable() {
        if (inversion != -1)
            return isSolvable;

        //to transform the matrix to array
        int[] flattenMatrix = new int[boardSize*boardSize - 1]; //exclude 0
        int arrayIndex = 0;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (boardTiles[row][col] == 0)
                    continue;
                flattenMatrix[arrayIndex] = boardTiles[row][col];
                arrayIndex++;
            }
        }
        //to find inversion
        inversion = findInversion(flattenMatrix, 0, boardSize*boardSize - 1 - 1);
        if (boardSize % 2 == 0) {
            if ((inversion + blankRow) % 2 != 0) {
                isSolvable = true;
            } else {
                isSolvable = false;
            }
        } else {
            if (inversion % 2 == 0) {
                isSolvable = true;
            } else {
                isSolvable = false;
            }
        }
        return isSolvable;
    }

    private int findInversion(int[] a, int lo, int hi) {
        //base case - an array with only 2 elements
        if (hi <= lo + 1) {
            if (a[lo] <= a[hi]) {
                return 0;
            }
            else {
                return 1;
            }
        }
        int mid = lo + (hi - lo)/2 ;
        //recursively find number of inversion left and right
        int numOfInversion = findInversion(a, lo, mid) + findInversion(a, mid + 1, hi);
        //update the number of inversion with the complete array
        Arrays.sort(a, lo, (mid + 1));
        Arrays.sort(a, (mid + 1), (hi + 1));
        //Now we have 2 partially sorted array
        int i = lo;
        int j = mid + 1;
        while (i < mid + 1 && j <= hi) {
            if ( a[i] < a[j]) {
                i++;
            }
            else {
                numOfInversion += mid + 1 - i;
                j++;
            }
        }
        return numOfInversion;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board other = (Board) y;
        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                if (((Board) y).boardTiles[row][col] != this.boardTiles[row][col])
                    return false;
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<>();

        if (blankRow == 0) {
            if (blankCol == 0) { //2 possible moves
                swap(boardTiles, 0,0, 0, 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, 0, 0,0,1);

                swap(boardTiles, 0, 0, 1, 0);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, 1, 0, 0,0);
                return neighbors;
            } else if (blankCol == boardSize - 1) { //2 possible moves
                swap(boardTiles, 0,boardSize - 1, 0, boardSize - 2);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, 0,boardSize - 1, 0, boardSize - 2);

                swap(boardTiles, 0, boardSize - 1, 1, boardSize - 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, 0, boardSize - 1, 1, boardSize - 1);
                return neighbors;
            } else { //3 possible moves
                swap(boardTiles, 0, blankCol, 1, blankCol);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, 0, blankCol, 1, blankCol);

                swap(boardTiles, 0, blankCol, 0, blankCol - 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, 0, blankCol, 0, blankCol - 1);

                swap(boardTiles, 0, blankCol, 0, blankCol + 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, 0, blankCol, 0, blankCol + 1);

                return neighbors;
            }
        } else if (blankRow == boardSize -1) {
            if (blankCol == 0) { //2 possible moves
                swap(boardTiles, boardSize - 1,0, boardSize - 2, 0);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, boardSize - 1,0, boardSize - 2, 0);

                swap(boardTiles, boardSize - 1, 0, boardSize - 1, 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, boardSize - 1, 0, boardSize - 1, 1);

                return neighbors;
            } else if (blankCol == boardSize - 1) { //2 possible moves
                swap(boardTiles, boardSize - 1,boardSize - 1, boardSize - 1, boardSize - 2);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, boardSize - 1,boardSize - 1, boardSize - 1, boardSize - 2);

                swap(boardTiles, boardSize - 1, boardSize - 1, boardSize - 2, boardSize - 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, boardSize - 1, boardSize - 1, boardSize - 2, boardSize - 1);

                return neighbors;
            } else { //3 possible moves
                swap(boardTiles, boardSize - 1, blankCol, boardSize - 1, blankCol - 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, boardSize - 1, blankCol, boardSize - 1, blankCol - 1);

                swap(boardTiles, boardSize - 1, blankCol, boardSize - 1, blankCol + 1);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, boardSize - 1, blankCol, boardSize - 1, blankCol + 1);

                swap(boardTiles, boardSize - 1, blankCol, boardSize - 2, blankCol);
                neighbors.enqueue(new Board(boardTiles));
                swap(boardTiles, boardSize - 1, blankCol, boardSize - 2, blankCol);

                return neighbors;
            }
        } else if (blankCol == 0) { // 3 possible moves
            swap(boardTiles, blankRow, 0, blankRow, 1);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, 0, blankRow, 1);

            swap(boardTiles, blankRow, 0, blankRow - 1, 0);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, 0, blankRow - 1, 0);

            swap(boardTiles, blankRow, 0, blankRow + 1, 0);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, 0, blankRow + 1, 0);
            return neighbors;
        } else if (blankCol == boardSize - 1) { //3 possible moves
            swap(boardTiles, blankRow, boardSize - 1, blankRow, boardSize - 2);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, boardSize - 1, blankRow, boardSize - 2);

            swap(boardTiles, blankRow, boardSize - 1, blankRow - 1, boardSize - 1);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, boardSize - 1, blankRow - 1, boardSize - 1);

            swap(boardTiles, blankRow, boardSize - 1, blankRow + 1, boardSize - 1);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, boardSize - 1, blankRow + 1, boardSize - 1);
            return neighbors;
        } else { // 4 possible moves
            swap(boardTiles, blankRow,  blankCol, blankRow - 1, blankCol);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow,  blankCol, blankRow - 1, blankCol);

            swap(boardTiles, blankRow, blankCol, blankRow + 1, blankCol);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, blankCol, blankRow + 1, blankCol);

            swap(boardTiles, blankRow, blankCol, blankRow, blankCol - 1);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, blankCol, blankRow, blankCol - 1);

            swap(boardTiles, blankRow, blankCol, blankRow, blankCol + 1);
            neighbors.enqueue(new Board(boardTiles));
            swap(boardTiles, blankRow, blankCol, blankRow, blankCol + 1);
            return neighbors;
        }
    }

    //to swap and create neighbours
    private void swap(int[][] array, int row1, int col1, int row2, int col2) {
        int temp = array[row1][col1];
        array[row1][col1] = array[row2][col2];
        array[row2][col2] = temp;
    }

    // string representation of this board (in the output format specified below)
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(boardSize + "\n");
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                s.append(String.format("%2d ", tileAt(i, j)));
            }
            s.append("\n");
        }
        return s.toString();
    }

    //returns a hash code for this board
    public int hashCode() {
        if (hashCode != -1)
            return hashCode;
        for (int row = 0; row < boardSize; row++) {
            hashCode += Arrays.hashCode(boardTiles[row]);
        }
        return hashCode;
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

        Board testBoard = new Board(tiles);
        System.out.println(testBoard.hamming());
        boolean isSolvable = testBoard.isSolvable();
        System.out.println(isSolvable);
        System.out.println(testBoard.isGoal());

//        int[][] testBoard = {{2, 0}, {1,3}} ;
//        Board testInversion = new Board(testBoard);
//        System.out.println(testInversion.manhattan());
//        int[] arrayInversion = {8, 7, 6, 5, 4, 3, 2, 1};
//        System.out.println(testInversion.findInversion(arrayInversion, 0,7));
    } // unit testing (required)
}

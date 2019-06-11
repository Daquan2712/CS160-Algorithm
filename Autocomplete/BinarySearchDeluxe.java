import java.util.Comparator;
import edu.princeton.cs.algs4.*;

/**
 * Created by Bradley Pham on 10/14/2017.
 */
public class BinarySearchDeluxe {

    public BinarySearchDeluxe() {

    }
    // Returns the index of the first key in a[] that equals the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        if (a == null || key == null || comparator == null) {
            throw new NullPointerException();
        }
        return firstIndexOf(a, key, 0, a.length, comparator);
    }

    //to remember the possible first position before the found index
    private static int indexBeforeHit;
    private static <Key> int firstIndexOf(Key[] a, Key key, int lo, int hi, Comparator<Key> comparator) {
        if (hi <= lo) {
            return -1;
        }
        int mid = lo + ((hi - lo) / 2);
//        System.out.println(a[mid]);
        int cmp = comparator.compare(key, a[mid]);
        if (cmp < 0) {
//            System.out.println("It's smaller");
            return firstIndexOf(a, key, lo, mid, comparator);
        }
        else if (cmp > 0) {
//            System.out.println("It's bigger");
            return firstIndexOf(a, key, mid + 1, hi, comparator);
        }
        else {
//            System.out.println("Is it first?");
            indexBeforeHit = mid;
            if (firstIndexOf(a, key, lo, mid, comparator) == -1) {
                //If the part of the array before the current hit doesn't have any other hit, return the current one
                return mid;
            } else {
                //return the one that made the condition wrong
                return indexBeforeHit;
            }
        }
    }

    // Returns the index of the last key in a[] that equals the search key, or -1 if no such key.
     public static <Key> int lastIndexOf(Key[] a, Key key, Comparator<Key> comparator) {
        return lastIndexOf(a, key, 0, a.length, comparator);

     }

    //to store position after the found index
    private static int indexAfterHit;
    private static <Key> int lastIndexOf(Key[] a, Key key, int lo, int hi, Comparator<Key> comparator) {
        if (hi <= lo) {
            return -1;
        }
        int mid = lo + ((hi - lo) / 2);
        int cmp = comparator.compare(key, a[mid]);
        if (cmp < 0) {
//            System.out.println("It's smaller");
            return lastIndexOf(a, key, lo, mid, comparator);
        }
        else if (cmp > 0) {
//            System.out.println("It's bigger");
            return lastIndexOf(a, key, mid + 1, hi, comparator);
        }
        else {
//            System.out.println("Checking max");
            indexAfterHit = mid;
            if (lastIndexOf(a, key, mid + 1, hi, comparator) == -1) {
                //If the part of the array after the hit doesn't have another hit, return the current hit index
                return mid;
            } else {
                //return the one that made the condition wrong
                return indexAfterHit;
            }
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
//        Integer[] testArray = {10, 10, 30, 30, 30, 30, 40, 50, 50, 50, 70, 70, 70};
//        //create intComp class implementing comparator<Integer>
//        IntComp intComp = new IntComp();
//        System.out.println("First index: " + BinarySearchDeluxe.firstIndexOf(testArray, 90, intComp));
//        System.out.println("Last index: " + BinarySearchDeluxe.lastIndexOf(testArray, 90, intComp));
//
//        Integer[] testArray2 = {70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70, 70};
//        System.out.println("First index: " + BinarySearchDeluxe.firstIndexOf(testArray2, 70, intComp));
//        System.out.println("Last index: " + BinarySearchDeluxe.lastIndexOf(testArray2, 70, intComp));
//        String a = "8244    Nassau Hall";
//        String[] grab = a.split("    ");
//        for (int i = 0; i < grab.length; i++) {
//            System.out.println("Print " + i + " time");
//            System.out.println(grab[i]);
//        }
    }
}

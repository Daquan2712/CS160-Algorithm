
import edu.princeton.cs.algs4.Insertion;
import edu.princeton.cs.algs4.Quick;
import java.util.Arrays;

/**
 * Created by Bradley Pham on 10/14/2017.
 */
public class Autocomplete {

    private Term[] data; //to copy the data from users
    private int dataSize;
    private String usedPrefix; //to cache
    private Term[] usedResult; //to cache
    private int usedNumberMatches = -10; //to cache

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        if (terms == null)
             throw new NullPointerException("The data is null!");
        dataSize = terms.length;
        data = new Term[dataSize];
        for (int count = 0; count < dataSize; count++) {
            if (terms[count] == null)
                throw new NullPointerException("There is one null term!");
            data[count] = terms[count];
        }
        //to sort the terms in lexicographic order
        Arrays.sort(data);
    }

    // Returns all terms that start with the given prefix, in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix cannot be null");
        }
        //to cache if called multiple times
        if (prefix.equals(usedPrefix) && usedResult != null) {
//            System.out.println("The same!");
            return usedResult;
        }

        usedPrefix = prefix;
        Term query = new Term(prefix, 0);
        int firstMatchIndex = BinarySearchDeluxe.firstIndexOf(data, query, Term.byPrefixOrder(prefix.length()));
        int lastMatchIndex = BinarySearchDeluxe.lastIndexOf(data, query, Term.byPrefixOrder(prefix.length()));

        //to return an array with 0 element
        if (firstMatchIndex == -1 || lastMatchIndex == -1) {
            //an array with 0 element is different from a "null" array
            usedResult = new Term[0];
            return usedResult;
        }

        int numberOfMatches = lastMatchIndex - firstMatchIndex + 1;
        usedResult = new Term[numberOfMatches];
        for (int count = lastMatchIndex, i = 0 ; count >= firstMatchIndex ; count--, i++) {
//            System.out.println(count + " ");
            usedResult[i] = data[count];
        }
//        System.out.println("Finish finding");
        //to rearrange the result depending on weight
        Arrays.sort(usedResult, Term.byReverseWeightOrder());

        return usedResult;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("The prefix can't be null");
        }
        //to return immediately in case called multiple times in a row
        if (prefix.equals(usedPrefix) && usedNumberMatches != -10) {
            return usedNumberMatches;
        }

        usedPrefix = prefix;
        Term query = new Term(prefix, 0);
        int firstMatchIndex = BinarySearchDeluxe.firstIndexOf(data, query, Term.byPrefixOrder(prefix.length()));

        //in case there is no match
        //fixed
        if(firstMatchIndex == -1) {
            usedNumberMatches = 0;
            return usedNumberMatches;
        }

        int lastMatchIndex = BinarySearchDeluxe.lastIndexOf(data, query, Term.byPrefixOrder(prefix.length()));
        usedNumberMatches = lastMatchIndex - firstMatchIndex + 1;
        return usedNumberMatches;
    }

//    public void printData() {
//        for (int i = 0; i< data.length; i++) {
//            System.out.println(data[i].toString());
//        }
//    }

    // unit testing (required)
    public static void main(String[] args) {

//        //to build data
//        In file = new In(args[0]);
//        int arrayLength = file.readInt();
//        Term[] testArray = new Term[arrayLength];
//        System.out.println("There are: " + arrayLength + " items in the array");
//        for (int i = 0; i < testArray.length; i++) {
//            Long weight = file.readLong();
//            String query = file.readLine().trim();
//            testArray[i] = new Term(query, weight);
//        }
////        for (int i = 0; i < testArray.length; i++) {
////            System.out.println(testArray[i].toString());
////        }
//
//        Autocomplete testAuto = new Autocomplete(testArray);
////        System.out.println("Data in Autocomplete: ");
////        testAuto.printData();
//        String query = args[1];
//        int length = testAuto.numberOfMatches(query);
//        Term[] allMatches = testAuto.allMatches(query);
//        System.out.println("Number of matches: " + length);
//        System.out.println("All matches: ");
//        for (int i = 0; i<allMatches.length; i++) {
//            System.out.println(allMatches[i].toString());
//        }

//          Term[] testArray = new Term[5];
//        testArray[4] = new Term("company", 1331590);
//        testArray[2] = new Term("complete", 7803980);
//        testArray[1] = new Term("companion", 6038490);
//        testArray[3] = new Term("completely", 5205030);
//        testArray[0] = new Term("comply", 4481770);
//
//        Autocomplete autoTest = new Autocomplete(testArray);
//        int numMatches = autoTest.numberOfMatches("com");
//        System.out.println(numMatches);
//        Term[] allMatches = autoTest.allMatches("com");
//        System.out.println("All matches: ");
//        for (int i = 0; i<allMatches.length; i++) {
//            System.out.println(allMatches[i].toString());
//        }
//        Term[] allMatches2 = autoTest.allMatches("com");
    }
}


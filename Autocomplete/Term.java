import edu.princeton.cs.algs4.*;

import java.util.Comparator;

/**
 * Created by Bradley Pham on 10/14/2017.
 */
public class Term implements Comparable<Term> {

    private String queryContent;
    private long queryWeight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null)
            throw new NullPointerException("Query cannot be empty!");
        if (weight < 0)
            throw new IllegalArgumentException("Weight cannot be negative!");
        queryContent = query;
        queryWeight = weight;
    }

    private static class WeightComparison implements Comparator<Term> {
        public int compare(Term t1, Term t2) {
            if (t1.queryWeight < t2.queryWeight)
                return 1;
            else if (t1.queryWeight == t2.queryWeight)
                return 0;
            else
                return -1;
        }
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        Comparator<Term> cmp = new WeightComparison();
        return cmp;
    }

    private static class PrefixComparison implements Comparator<Term> {
        private int prefix;
        public PrefixComparison(int r) {
            prefix = r;
        }
        public int compare(Term t1, Term t2) {
           int lengthOfT1 = t1.queryContent.length();
           int lengthOfT2 = t2.queryContent.length();

            if (prefix >= Math.max(lengthOfT1, lengthOfT2)) {
                return t1.queryContent.compareTo(t2.queryContent);
            } else if (prefix <= Math.min(lengthOfT1, lengthOfT2)) {
                String subString1 = t1.queryContent.substring(0, prefix);
                String subString2 = t2.queryContent.substring(0, prefix);
                return subString1.compareTo(subString2);
            } else {
                if (prefix > lengthOfT1) {
                    String subString2 = t2.queryContent.substring(0, prefix);
                    return t1.queryContent.compareTo(subString2);
                } else {
                    String subString1 = t1.queryContent.substring(0, prefix);
                    return subString1.compareTo(t2.queryContent);
                }
            }
        }
    }

    // Compares the two terms in lexicographic order but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0)
            throw new IllegalArgumentException("Prefix length can't be empty!");
        Comparator<Term> cmp = new PrefixComparison(r);
        return cmp;
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        return queryContent.compareTo(that.queryContent);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        //to use AutocompleteGUI
        //fixed
        return new String(queryWeight + "\t" + queryContent);
//        return new String(queryWeight + queryContent);
    }

    // unit testing (required)
    public static void main(String[] args) {
//        Term[] testArray = new Term[5];
//        testArray[4] = new Term("company", 13315900);
//        testArray[2] = new Term("complete", 7803980);
//        testArray[1] = new Term("companion", 6038490);
//        testArray[3] = new Term("completely", 5205030);
//        testArray[0] = new Term("comply", 4481770);
//
//        //test sort by Weight
//        Insertion.sort(testArray, Term.byReverseWeightOrder());
//        System.out.println("Sort by Weight reversely:");
//        for (int i = 0; i< testArray.length; i++)
//            System.out.println(testArray[i].toString());
//
//        //test sort by prefix with r = 4
//        Insertion.sort(testArray, Term.byPrefixOrder(5));
//        System.out.println("Sort by prefix:");
//        for (int i = 0; i< testArray.length; i++) {
//            System.out.println(testArray[i].toString());
//        }
//
//        String string1 = "";
//        String string2 = "";
//        System.out.println(string1.compareTo(string2));

//        Term[] testArray2 = new Term[2];
//        testArray2[0] = new Term("Chrisyopher", 1);
//        testArray2[1] = new Term("Christ",1);
////       testArray2[2] = new Term("Chris", 1);
//        Insertion.sort(testArray2, Term.byPrefixOrder(6));
//        for (int i = 0; i< testArray2.length; i++) {
//            System.out.println(testArray2[i].toString());
//        }

    }
}
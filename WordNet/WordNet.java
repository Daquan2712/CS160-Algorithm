import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;

/**
 * Created by Bradley Pham on 11/18/2017.
 */
public class WordNet {

    private RedBlackBST<String, Queue<Integer>> synsetN; //to store ST: nouns -> id and have O(logN) property when searching
    private RedBlackBST<Integer, String> synsetID; //to store ST: id -> nouns
    private Digraph hypernymsIn;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        if (synsets == null || hypernyms == null)
            throw new NullPointerException();
        //store synsets
        synsetN = new RedBlackBST<>();
        synsetID = new RedBlackBST<>();
        In synsetsIn = new In(synsets);
        int numOfSynset = 0;
        while(synsetsIn.hasNextLine()) {
            numOfSynset++;
            //take the whole line and break it down spliting the ","
            String[] line = synsetsIn.readLine().split(",");
            //take out the nouns only
            String[] nouns = line[1].split(" ");
            //if the tree already has that nouns, update its value queues
            for (int i = 0; i < nouns.length; i++) {
                if (synsetN.contains(nouns[i])) {
                    synsetN.get(nouns[i]).enqueue(Integer.parseInt(line[0]));
                } else {
                    Queue<Integer> id = new Queue<>();
                    id.enqueue(Integer.parseInt(line[0]));
                    synsetN.put(nouns[i], id);
                }
            }
            synsetID.put(Integer.parseInt(line[0]), line[1] );
        }
        //store hypernyms
        hypernymsIn = new Digraph(numOfSynset);
        In inHypernyms = new In(hypernyms);
        while (inHypernyms.hasNextLine()) {
            String[] line = inHypernyms.readLine().split(",");
            for (int i = 1; i < line.length; i++)
                hypernymsIn.addEdge(Integer.parseInt(line[0]), Integer.parseInt(line[i]));
        }
    }

    // all WordNet nouns
    public Iterable<String> nouns() {
        Iterable<String> nouns;
        nouns = synsetN.keys();
        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new NullPointerException();
        return synsetN.contains(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        if (noun1 == null || noun2 == null)
            throw new NullPointerException();
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
        Iterable<Integer> subsetA = synsetN.get(noun1);
        Iterable<Integer> subsetB = synsetN.get(noun2);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(hypernymsIn);
        int ancestor = sca.ancestor(subsetA, subsetB);
        return synsetID.get(ancestor);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        if (noun1 == null || noun2 == null)
            throw new NullPointerException();
        if (!isNoun(noun1) || !isNoun(noun2)) {
            throw new IllegalArgumentException();
        }
        Iterable<Integer> subsetA = synsetN.get(noun1);
        Iterable<Integer> subsetB = synsetN.get(noun2);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(hypernymsIn);
        int ancestralLength = sca.length(subsetA, subsetB);
        return ancestralLength;
    }

    // do unit testing of this class
    public static void main(String[] args) {

//        In input = new In();
        String synset = args[0];
        String hypernym = args[1];
        WordNet testing = new WordNet(synset, hypernym);
        /*for (String word : testing.nouns()) {
            System.out.println(word);
        }*/
        In input = new In();
        while (input.hasNextLine()) {
            String query1 = input.readString();
            String query2 = input.readString();
            if (testing.isNoun(query1) && testing.isNoun(query2)) {
                String sca = testing.sca(query1, query2);
                int length = testing.distance(query1, query2);
                System.out.printf("SCA: %s, Length: %d\n", sca, length);
            } else {
                System.out.println("There aren't those nouns in dictionary");
            }

        }
    }
}

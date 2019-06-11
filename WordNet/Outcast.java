/**
 * Created by Bradley Pham on 11/18/2017.
 */
public class Outcast {
    private WordNet wn; //to make it immutable
    public Outcast(WordNet wordnet){
        wn = wordnet;
    }         // constructor takes a WordNet object
    public String outcast(String[] nouns) {
        int maxLength = 0;
        int outcastIndex = -1;
        for (int i = 0; i < nouns.length; i++) {
            int length = 0;
            for (int j = 0; j < nouns.length; j++) {
                length += wn.distance(nouns[i], nouns[j]);
            }
            if (length > maxLength) {
                maxLength = length;
                outcastIndex = i;
            }
        }
        return nouns[outcastIndex];
    }   // given an array of WordNet nouns, return an outcast
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }


    } // see test client below
}

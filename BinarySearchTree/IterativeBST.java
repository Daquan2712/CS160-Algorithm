import java.util.NoSuchElementException;

public class IterativeBST<Key extends Comparable<Key>, Value> extends BasicBST<Key, Value> {
    // Returns the smallest key in the symbol table
    public Key min(){
        Node x = root;
        if (x == null)
            throw new NoSuchElementException("There is nothing in the tree.");
        else {
            while(x.left != null){
                x = x.left;
            }
            return x.key;
        }
    }

    // Returns the largest key in the symbol table
    public Key max() {
        Node x = root;
        if (x == null)
            throw new NoSuchElementException("There is nothing in the tree.");
        else {
            while (x.right != null) {
                x = x.right;
            }
            return x.key;
        }
    }

    // Returns the largest key in the symbol table less than or equal to key
    public Key floor(Key key){
        Node x = root;
        if (x == null)
            throw new NoSuchElementException("The tree is empty.");
        else if (key == null){
            throw new IllegalArgumentException("Cannot accept a null key.");
        } else {
            Key floor = null;
            while (x != null) {
                int cmp = key.compareTo(x.key);
                if (cmp == 0)
                    return x.key;
                //key is larger than key of x
                else if (cmp > 0) {
                    floor = x.key;
                    x = x.right;

                } else {//key is smaller than key of x
                    x = x.left;
                }
            }
            return floor;
        }
    }


    // Returns the smallest key in the symbol table greater than or equal to key
    public Key ceiling(Key key){
        Node x = root;
        if (x == null)
            throw new NoSuchElementException("The tree is empty.");
        else if (key == null) {
            throw new IllegalArgumentException("Cannot accept a null key.");
        } else {
            Key ceil = null;
            while (x != null) {
                int cmp = key.compareTo(x.key);
                if (cmp == 0)
                    return x.key;
                else if (cmp > 0) {
                        x = x.right;
                } else {
                    ceil = x.key;
                    x = x.left;
                }
            }
            return ceil;
        }
    }

    // Returns the kth smallest key in the symbol table
    public Key select(int k){
        Node x = root;
        if (k < 0 || k > x.size - 1){
            throw new IllegalArgumentException("Out of range!");
        } else {
            Key select = null;
            while (!(k < 0)) {
                if (k > size(x.left)) {
                    select = x.key;
                    k = k - size(x.left) - 1;
                    x = x.right;
                } else if (k < size(x.left)) {
                    select = x.key;
                    x = x.left;
                } else {
                    //StdOut.println("touch");
                    return x.key;
                }
            }
            return select;
        }
    }

    // Returns the number of keys in the symbol table strictly less than key
   public int rank(Key key){
        Node x = root;
        if (key == null){
            throw new IllegalArgumentException("Invalid argument!");
        } else {
            int rank = 0;
            while (x != null) {
                int cmp = key.compareTo(x.key);
                if (cmp > 0) {
                    rank = rank + size(x.left) + 1;
                    x = x.right;
                } else if (cmp < 0){
                    x = x.left;
                } else {
                    return rank + size(x.left);
                }
            }
            return rank;
        }
    }

    public static void main(String args[]) {
        IterativeBST<Integer, Integer> test;
        test = new IterativeBST<Integer, Integer>();
        test.put(15, 15);
        test.put(10, 10);
        test.put(17, 17);
        test.put(8, 8);
        test.put(12, 12);
        test.put(11,11);
        test.put(13, 13);
        test.put(25, 25);
        test.put(20, 20);
        test.put(28, 28);
        test.put(26, 26);
        test.put(18, 18);
        test.put(19, 19);

        System.out.println("Size of tree: " + test.size());
        System.out.println("The rank: " + test.rank(25));

        Integer a = new Integer(5);
        Integer  b = new Integer(2);
        a = b;
        System.out.println(a);

        String s = new String("hello");
        String p = new String("hello");
        //s = p;
        System.out.println(s == p);
        System.out.println(s.equals(p));
    }
}

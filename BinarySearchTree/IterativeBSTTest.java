import junit.framework.TestCase;


/**
 * Created by Bradley Pham on 10/18/2017.
 */
public class IterativeBSTTest extends TestCase{

    IterativeBST<Integer, Integer> test = new IterativeBST<Integer, Integer>();
    public void IterativeBSTTest() {

    }

    protected void setUp() {
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
    }
    public void testMin() {
        assertEquals(8, (int)test.min());
    }

    public void testMax() {
        assertEquals(28, (int)test.max());
    }

    public void testFloor() {
        assertEquals(10, Math.toIntExact(test.floor(10)));
        assertEquals(13, Math.toIntExact(test.floor(13)));
        assertEquals(null, test.floor(7));
        assertEquals(15, Math.toIntExact(test.floor(15)));
        assertEquals(8, Math.toIntExact(test.floor(9)));
        assertEquals(28, Math.toIntExact(test.floor(30)));
    }

    public void testCeiling() {
        assertEquals(28, Math.toIntExact(test.ceiling(28)));
        assertEquals(null, test.ceiling(30));
        assertEquals(8, Math.toIntExact(test.ceiling(7)));
        assertEquals(10, Math.toIntExact(test.ceiling(9)));
    }

    public void testSelect() {
        assertEquals(13, Math.toIntExact(test.select(4)));
        assertEquals(25, Math.toIntExact(test.select(8)));
        assertEquals(8, Math.toIntExact(test.select(0)));
    }

    public void testRank() {
        assertEquals(11, test.rank(30));
        assertEquals(2, test.rank(11));
        assertEquals(9, test.rank(26));
        assertEquals(0, test.rank(8));
    }
}
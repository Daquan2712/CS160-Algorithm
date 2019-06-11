
import edu.princeton.cs.algs4.*;

import java.awt.*;

public class PointST<Value> {

    private RedBlackBST<Point2D, Value> tree = new RedBlackBST<Point2D, Value>();;

    // construct an empty symbol table of points
    public PointST() {
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    // number of points
    public int size() {
        return tree.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new java.lang.NullPointerException("Illegal input!");
        tree.put(p, val);
    }
    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new java.lang.NullPointerException("Illegal input!");
        return tree.get(p);
    }

    // does the symbol table contain point p?
    public           boolean contains(Point2D p){
        if (p == null)
            throw new java.lang.NullPointerException("Illegal input!");
        return tree.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return tree.keys();
    }

    // all points that are inside the rectangle
    //time constraint: N
    public Iterable<Point2D> range(RectHV rect) {
        Queue<Point2D> pointsInside = new Queue<>();
        //list out all the points to check if they are in the rectangle
        Queue<Point2D> allPoints = new Queue<>();
        allPoints = (Queue<Point2D>) tree.keys();
        //checking
        for (Point2D checkingPoint : allPoints) {
            if (rect.contains(checkingPoint)) {
                pointsInside.enqueue(checkingPoint);
            }
        }
        return pointsInside;
    }

    // a nearest neighbor to point p; null if the symbol table is empty
    //time constraint: N
    public           Point2D nearest(Point2D p) {

        //list out all the points to check if they are in the rectangle
        Queue<Point2D> allPoints = new Queue<>();
        allPoints = (Queue<Point2D>) tree.keys();
        //comparing
        Point2D nearestPoint = allPoints.dequeue();
        double nearestDistance = p.distanceSquaredTo(nearestPoint);
        //To go through all the points in the queue
        for(Point2D checkingPoint : allPoints){
            if(p.distanceSquaredTo(checkingPoint) < nearestDistance){
                nearestDistance = p.distanceSquaredTo(checkingPoint);
                nearestPoint = checkingPoint;
            }
        }
        return nearestPoint;
    }
    // unit testing (required)
    public static void main(String[] args) {

        PointST<Integer> testingTree = new PointST<>();

        //testing empty tree
        System.out.println(testingTree.size());
        System.out.println("Is the tree empty? " + testingTree.isEmpty());

        //testing input
        Point2D tp1 = new Point2D(5.0, 3.0);
        Point2D tp2 = new Point2D(1.0, 1.0);
        Point2D tp3 = new Point2D(2.0, 3.0);
        Point2D tp4 = new Point2D(2.0, 2.0);
        Point2D tp5 = new Point2D(4.0, 3.0);
        Point2D tp6 = new Point2D(6.0, 5.0);
        //to check when input same value twice
        testingTree.put(tp1, 5);
        testingTree.put(tp1, 5);
        testingTree.put(tp2, 1);
        testingTree.put(tp3, 2);
        testingTree.put(tp4, 2);
        testingTree.put(tp5, 4);

        System.out.println("Size: " + testingTree.size());
        System.out.println("Value of (2,3): " + testingTree.get(tp3));
        System.out.println("Tree contains (6,5): " + testingTree.contains(tp6));

        Queue<Point2D> checkingQueue = new Queue<>();
        checkingQueue = (Queue<Point2D>)testingTree.points();
        for(Point2D n : checkingQueue){
            System.out.println("Points in the tree: ");
            System.out.println("(" + n.x() + "," + n.y() + ")");
        }

        RectHV testingRec = new RectHV(2, 2, 4,5);
        Queue<Point2D> pointInside = new Queue<>();
        pointInside = (Queue<Point2D>)testingTree.range(testingRec);
        System.out.print("Points in rectangle (2,2,4,5): ");
        for(Point2D checkingPoint : pointInside){
            System.out.print("(" + checkingPoint.x() + "," + checkingPoint.y() + ") ");
        }
        System.out.println();

        Point2D tp7 = new Point2D(3, 4);
        System.out.println("The existing nearest point to (3,4): " + testingTree.nearest(tp7));
    }

}

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.*;

public class KdTreeST<Value> {

    private class Node {
        private Point2D p;      // the point
        private Value value;    // the symbol table maps the point to this value
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree
        private boolean isHorizontal; //horizontal or vertical

        public Node(Point2D key, Value val, RectHV boundingBox, boolean horizontal) {
            p = key;
            value = val;
            rect = boundingBox;
            isHorizontal = horizontal;
        }
    }
    private Node root;
    // construct an empty symbol table of points
    public         KdTreeST() {
    }

    // is the symbol table empty?
    public           boolean isEmpty(){
        return (root == null);
    }
    // number of points
    public               int size(){
        return size(root);
    }
    private int size(Node x) {
        if (x == null) return 0;
        else {
            return size(x.lb) + size(x.rt) + 1;
        }
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new NullPointerException("The key or val cannot be empty.");
        else
            root = put(root, p, val);

    }

    //to remember direction to assign for a new node
    private boolean isCurrentHorizontal = false;
    //to remember the bounding box for a new node
    private Double currentBoxMinX = Double.NEGATIVE_INFINITY;
    private Double currentBoxMaxX = Double.POSITIVE_INFINITY;
    private Double currentBoxMinY = Double.NEGATIVE_INFINITY;
    private Double currentBoxMaxY = Double.POSITIVE_INFINITY;

    private Node put(Node currentNode, Point2D p, Value val) {
        if(currentNode == null) {
            RectHV boundingBox = new RectHV(currentBoxMinX, currentBoxMinY, currentBoxMaxX, currentBoxMaxY);
            //not to remember the past value, in case leads to invalid rectangle
            currentBoxMinX = Double.NEGATIVE_INFINITY;
            currentBoxMaxX = Double.POSITIVE_INFINITY;
            currentBoxMinY = Double.NEGATIVE_INFINITY;
            currentBoxMaxY = Double.POSITIVE_INFINITY;
            return new Node(p, val, boundingBox, isCurrentHorizontal);
        }
        if ((currentNode.p.x() == p.x() && (currentNode.p.y() == p.y()))) {
            currentNode.value = val;
            //this one is changed after the test was done!
            //fixed to return; not continue adding points
            return currentNode;
        }
        //to save the current Node's direction
        isCurrentHorizontal = !(currentNode.isHorizontal);
        //to check which one to compare
        //update the bounding box
        if(currentNode.isHorizontal) {
            if (currentNode.p.y() > p.y()) {
                currentBoxMaxY = currentNode.p.y();
                currentNode.lb = put(currentNode.lb, p, val);
            } else {
                currentBoxMinY = currentNode.p.y();
                currentNode.rt = put(currentNode.rt, p, val);
            }

        } else {
            if (currentNode.p.x() > p.x()) {
                currentBoxMaxX = currentNode.p.x();
                currentNode.lb = put(currentNode.lb, p, val);

            } else {
                currentBoxMinX = currentNode.p.x();
                currentNode.rt = put(currentNode.rt, p, val);
            }
        }
        return currentNode;
    }

    //print bounding box
    private void printBox() {
        printBox(root);
    }
    private void printBox(Node x) {
        if (x == null)
            return;
        System.out.println("(" + x.rect.xmin() + "," + x.rect.ymin() + "," + x.rect.xmax() + "," + x.rect.ymax() + ")");
        printBox(x.lb);
        printBox(x.rt);

    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null)
            throw new NullPointerException("Key cannot be null");
        return get(root, p);
    }
    private Value get(Node currentNode, Point2D p) {
        if (currentNode == null) {
            return null;
        }
        if (currentNode.p.equals(p)) {
            return currentNode.value;
        }
        if (currentNode.isHorizontal) {
            if (currentNode.p.y() > p.y()) {
                return get(currentNode.lb, p);
            } else {
                return get(currentNode.rt, p);
            }
        } else {
            if (currentNode.p.x() > p.x()) {
                return get(currentNode.lb, p);
            } else {
                return get(currentNode.rt, p);
            }
        }
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new NullPointerException("Key cannot be empty");
        if (this.get(p) != null) {
            return true;
        }
        return false;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Point2D> points = new Queue<>();
        Queue<Node> nodes = new Queue<>();
        nodes.enqueue(root);
        while (!nodes.isEmpty()){
            Node temp = nodes.dequeue();
            if (temp == null)
                continue;
            points.enqueue(temp.p);
            nodes.enqueue(temp.lb);
            nodes.enqueue(temp.rt);
        }
        return points;
    }

    // all points that are inside the rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new NullPointerException("Rectangle cannot be empty");
        Queue<Point2D> pointsInside = new Queue<>();
        range(root, rect, pointsInside);
        return pointsInside;
    }

    //to contain all points in the rectangle
    private void range(Node currentNode, RectHV rect, Queue<Point2D> pointsInside) {
        if (currentNode == null || !(currentNode.rect.intersects(rect)))
            return;
        if (rect.contains(currentNode.p)) {
            pointsInside.enqueue(currentNode.p);
        }
        range(currentNode.lb, rect, pointsInside);
        range(currentNode.rt, rect, pointsInside);
    }

    // a nearest neighbor to point p; null if the symbol table is empty
    public Point2D nearest(Point2D p){
        if (p == null)
            throw new NullPointerException("Invalid P");
        if (this.isEmpty())
            return null;
        if (this.contains(p))
            return p;
        Point2D nearestPoint = root.p;
        nearestPoint = nearest(root, p, nearestPoint);
        return nearestPoint;
    }

    private Point2D nearest(Node currentNode, Point2D p, Point2D nearestPoint) {

        if (currentNode == null) {
            return nearestPoint;
        }
        if (currentNode.p.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p)) {
            nearestPoint = currentNode.p;
        }

        boolean areInSameBox = currentNode.rect.contains(p);
        boolean hasNearerPoint = currentNode.rect.distanceSquaredTo(p) < nearestPoint.distanceSquaredTo(p);

        if (areInSameBox || hasNearerPoint) {
            if (currentNode.isHorizontal) {
                if (currentNode.p.y() > p.y()) {
                    nearestPoint = nearest(currentNode.lb, p, nearestPoint);
                    nearestPoint = nearest(currentNode.rt, p, nearestPoint);
                }
                 else {
                    nearestPoint = nearest(currentNode.rt, p, nearestPoint);
                    nearestPoint = nearest(currentNode.lb, p, nearestPoint);
                }
            } else {
                if (currentNode.p.x() > p.x()) {
                        nearestPoint = nearest(currentNode.lb, p, nearestPoint);
                        nearestPoint = nearest(currentNode.rt, p, nearestPoint);
                    }
                 else {
                        nearestPoint = nearest(currentNode.rt, p, nearestPoint);
                        nearestPoint = nearest(currentNode.lb, p, nearestPoint);
                    }
            }
        }
        return nearestPoint;
    }

    // unit testing (required)
    public static void main(String[] args) {

        KdTreeST<Integer> testTree = new KdTreeST<>();
//        In input = new In();
//        while (!input.isEmpty()) {
//            Double x = input.readDouble();
//        }

        System.out.println("The tree is empty: " + testTree.isEmpty());
        System.out.println("The size is: " + testTree.size());
//
        Point2D p1 = new Point2D(2,3);
        Point2D p2 = new Point2D(1,5);
        Point2D p3 = new Point2D(4,2);
        Point2D p4 = new Point2D(4,8);
        Point2D p5 = new Point2D(4,8);
        Point2D p6 = new Point2D(4,5);
        Point2D p7 = new Point2D(1, 1);
        Point2D p8 = new Point2D(6, 6);
        Point2D p9 = new Point2D(4, 3);
//        Point2D p10 = new Point2D(9, 8);
//
        testTree.put(p1 ,1);
        testTree.put(p2 ,2);
        testTree.put(p3 ,3);
        testTree.put(p4 ,4);
        testTree.put(p5 ,5);
        testTree.put(p6 ,6);
        testTree.put(p7, 7);
        testTree.put(p8, 8);
        testTree.put(p9, 9);
//
        System.out.println("The tree is empty: " + testTree.isEmpty());
        System.out.println("The size is: " + testTree.size());
         // testTree.printBox();
        System.out.println(testTree.get(p6));
//        System.out.println(testTree.get(p10));
//        System.out.println(testTree.contains(p8));
//        System.out.println(testTree.contains(p10));
        Queue<Point2D> testingQ = new Queue<>();
        testingQ = (Queue<Point2D>)testTree.points();
        if (testingQ.isEmpty())
            System.out.println("There is no point");
        else
            for (Point2D check : testingQ) {
                System.out.println(check.toString());
            }
//
//        Queue<Point2D> pointsInRect = new Queue<>();
//        RectHV testRec = new RectHV(4, 2, 6, 6);
//        pointsInRect = (Queue<Point2D>)testTree.range(testRec);
//
//        if (pointsInRect.isEmpty())
//            System.out.println("The tree is empty");
//        else
//            for (Point2D point : pointsInRect) {
//                System.out.println(point.toString());
//            }
//        Point2D p11 = new Point2D(4, 4.5);
//        Point2D nearestPoint = testTree.nearest(p11);
//        if (nearestPoint != null)
//        System.out.println("The nearest point in tree to " + p11.toString() + ": " + nearestPoint.toString());
    }
}

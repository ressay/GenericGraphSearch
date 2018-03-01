package GenericGraphSearch;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
abstract public class Node
{
    private Node parent = null;
    private int depth = 0;
    private double g=0,h=0;

    public Node(Node parent) {
        this.parent = parent;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public LinkedList<Node> getNodesToRoot()
    {
        LinkedList<Node> nodes = new LinkedList<>();
        Node node = this;
        for (;node != null;node = node.getParent())
            nodes.addLast(node);
        return nodes;
    }

    public abstract LinkedList<Node> getSuccessors();

    // returns cost function
    public double getG()
    {
        return g;
    }

    // returns heuristic function
    public double getH()
    {
        return h;
    }

    public void setG(double g) {
        this.g = g;
    }

    public void setH(double h) {
        this.h = h;
    }

    public double getGH()
    {
        return g+h;
    }
}
package GenericGraphSearch;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
abstract public class Node
{
    // parent node
    private Node parent = null;
    // depth of the node
    private int depth = 0;
    // g stores cost value to reach node, h stores heuristic value of node
    private double g=0,h=0;

    public Node(Node parent) {
        this.parent = parent;
    }

    /*
    Method to be overridden that generates successors of this node
     */
    public abstract LinkedList<Node> getSuccessors();

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

    /*
    returns LinkedList with all nodes from current to root
     */
    public LinkedList<Node> getNodesToRoot()
    {
        LinkedList<Node> nodes = new LinkedList<>();
        Node node = this;
        for (;node != null;node = node.getParent())
            nodes.addFirst(node);
        return nodes;
    }



    // returns cost value
    public double getG()
    {
        return g;
    }

    // returns heuristic value
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
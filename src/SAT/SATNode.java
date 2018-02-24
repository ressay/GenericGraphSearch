package SAT;

import GenericGraphSearch.Node;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class SATNode extends Node
{
    private int value;
    public SATNode(Node parent) {
        super(parent);
    }

    @Override
    public LinkedList<Node> getSuccessors() {
        LinkedList<Node> successors = new LinkedList<>();
        SATNode n1 = new SATNode(this);
        SATNode n2 = new SATNode(this);
        n1.setValue(1);
        n2.setValue(0);
        successors.add(n1);
        successors.add(n2);
        return successors;
    }

    public int getValue() {
        return value;
    }

    private void setValue(int value) {
        this.value = value;
    }
}

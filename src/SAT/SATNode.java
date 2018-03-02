package SAT;

import GenericGraphSearch.Node;

import java.util.BitSet;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class SATNode extends Node
{
    private int value;
    private int numberOfClausesSatisfied;
    private BitSet bitSet;
    public SATNode(Node parent) {
        super(parent);
        bitSet = new BitSet();

    }

    @Override
    public LinkedList<Node> getSuccessors() {
        LinkedList<Node> successors = new LinkedList<>();
        SATNode n1 = new SATNode(this);
        SATNode n2 = new SATNode(this);
        n1.setValue(1);
        n2.setValue(-1);
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

    public int getNumberOfClausesSatisfied() {
        return numberOfClausesSatisfied;
    }

    public void setNumberOfClausesSatisfied(int numberOfClausesSatisfied) {
        this.numberOfClausesSatisfied = numberOfClausesSatisfied;
    }

    public int getNumberOfClausesSatisfiedByThisNode()
    {
        return getNumberOfClausesSatisfied() - ((SATNode)getParent()).getNumberOfClausesSatisfied();
    }

    public BitSet getBitSet() {
        return bitSet;
    }

    public void setBitSet(BitSet bitSet) {
        this.bitSet = new BitSet();
        this.bitSet.or(bitSet);
    }
}

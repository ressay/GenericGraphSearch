package SAT;

import GenericGraphSearch.HeuristicEstimator;
import GenericGraphSearch.Node;

import java.util.LinkedList;

/**
 * Created by ressay on 01/03/18.
 */
public class SATHeuristicEstimator extends HeuristicEstimator
{
    SATEvaluator evaluator;

    public SATHeuristicEstimator(SATEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public double estimate(Node node) {
        SATNode satNode = (SATNode) node;
        int numSatisfied = estimateBitSet(satNode);
        satNode.setNumberOfClausesSatisfied(numSatisfied);
        node.setH(evaluator.getNumberOfClauses() - numSatisfied);
        return 0;
    }

    public int estimateBitSet(SATNode satNode)
    {
        satNode.setBitSet(evaluator.getBitSetOf(satNode));
        satNode.getBitSet().or(((SATNode)satNode.getParent()).getBitSet());
        return satNode.getBitSet().cardinality();
    }

    public int estimateMatrix(Node node)
    {
        LinkedList<Node> nodes = node.getNodesToRoot();
        nodes.removeFirst();
        return evaluator.getNumberOfSatisfiedClauses(nodes);
    }
}

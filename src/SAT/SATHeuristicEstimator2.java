package SAT;

import GenericGraphSearch.HeuristicEstimator;
import GenericGraphSearch.Node;

import java.util.LinkedList;

/**
 * Created by ressay on 02/03/18.
 */
public class SATHeuristicEstimator2 extends HeuristicEstimator
{
    SATEvaluator evaluator;

    public SATHeuristicEstimator2(SATEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public double estimate(Node node) {
        SATNode satNode = (SATNode) node;
        int numSatisfied = estimateBitSet(satNode);
        satNode.setNumberOfClausesSatisfied(numSatisfied);
        int diff2 = evaluator.getNumberOfAppearanceOfNodeDepth(node.getDepth()) - ((SATNode) node).getNumberOfClausesSatisfiedByThisNode();
        satNode.setG(((SATNode)node.getParent()).getG()+diff2/evaluator.getNumberOfVariables());
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

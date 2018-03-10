package SAT;

import GenericGraphSearch.HeuristicEstimator;
import GenericGraphSearch.Node;

import java.util.LinkedList;

/**
 * Created by ressay on 02/03/18.
 */
public class SATHeuristicEstimatorGreedy extends HeuristicEstimator
{
    SATEvaluator evaluator;

    public SATHeuristicEstimatorGreedy(SATEvaluator evaluator) {
        this.evaluator = evaluator;
    }

    @Override
    public double estimate(Node node) {
        SATNode satNode = (SATNode) node;
        int numSatisfied = estimateBitSet(satNode);
        satNode.setNumberOfClausesSatisfied(numSatisfied);
        int diff2 = evaluator.getBitSetOf(satNode).cardinality() - ((SATNode) node).getNumberOfClausesSatisfiedByThisNode();
//        satNode.setG(((SATNode)node.getParent()).getG()+ratio);
        satNode.setG(((SATNode)node.getParent()).getG()+diff2);
        node.setH(evaluator.getNumberOfClauses() - numSatisfied);
        return 0;
    }

    public int estimateBitSet(SATNode satNode)
    {
        satNode.setBitSet(evaluator.getBitSetOf(satNode));
        satNode.getBitSet().or(((SATNode)satNode.getParent()).getBitSet());
        return satNode.getBitSet().cardinality();
    }

}

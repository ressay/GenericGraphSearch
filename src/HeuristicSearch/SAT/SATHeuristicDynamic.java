package HeuristicSearch.SAT;

import HeuristicSearch.GenericGraphSearch.HeuristicEstimator;
import HeuristicSearch.GenericGraphSearch.Node;

/**
 * Created by ressay on 09/03/18.
 */
public class SATHeuristicDynamic extends HeuristicEstimator
{
    SATEvaluatorDynamic evaluator;

    public SATHeuristicDynamic(SATEvaluatorDynamic evaluator) {
        this.evaluator = evaluator;
    }



    @Override
    public double estimate(Node node) {
        SATNode satNode = (SATNode) node;
        int numSatisfied = estimateBitSet(satNode);
        satNode.setNumberOfClausesSatisfied(numSatisfied);
//        satNode.setG(((SATNode)node.getParent()).getG()+ratio);
        node.setH(evaluator.getEvaluationOfBitset(((SATNode)node).getBitSet()));
        node.setG(0);
        return 0;
    }

    public int estimateBitSet(SATNode satNode)
    {
        satNode.setBitSet(evaluator.getBitSetOf(satNode));
        satNode.getBitSet().or(((SATNode)satNode.getParent()).getBitSet());
        return satNode.getBitSet().cardinality();
    }
}

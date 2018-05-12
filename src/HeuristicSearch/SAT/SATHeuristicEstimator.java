package HeuristicSearch.SAT;

import HeuristicSearch.GenericGraphSearch.HeuristicEstimator;
import HeuristicSearch.GenericGraphSearch.Node;

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
        int numSatisfied = estimateMatrix(satNode);
        satNode.setNumberOfClausesSatisfied(numSatisfied);
//        double ratio = (double)(evaluator.getNumberOfClauses())/((double)evaluator.getNumberOfVariables());
//        int diff = evaluator.getNumberOfAppearanceOfNode((SATNode) node);
//        satNode.setG(((SATNode)node.getParent()).getG()+diff);
//        satNode.setG(((SATNode)node.getParent()).getG()+diff/evaluator.getNumberOfVariables());
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

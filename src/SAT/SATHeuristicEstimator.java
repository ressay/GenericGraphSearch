package SAT;

import GenericGraphSearch.HeuristicEstimator;
import GenericGraphSearch.HeuristicEvaluator;
import GenericGraphSearch.Node;
import mainPackage.TextDisplayer;

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
        int numSatisfied = satNode.getNumberOfClausesSatisfied();
        node.setH(evaluator.getNumberOfClauses() - numSatisfied);
        return 0;
    }
}

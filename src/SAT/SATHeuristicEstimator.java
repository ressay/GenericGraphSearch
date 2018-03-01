package SAT;

import GenericGraphSearch.HeuristicEstimator;
import GenericGraphSearch.HeuristicEvaluator;
import GenericGraphSearch.Node;

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
        //TODO count here how many times this node appears in clauses
        return 0;
    }
}

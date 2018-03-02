package GenericGraphSearch;

/**
 * Created by ressay on 01/03/18.
 */
abstract public class HeuristicEvaluator extends Evaluator
{
    private HeuristicEstimator estimator = null;

    public HeuristicEstimator getEstimator() {
        return estimator;
    }

    public void setEstimator(HeuristicEstimator estimator) {
        this.estimator = estimator;
    }

    public void evaluateF(Node node)
    {
        if(getEstimator() != null)
            getEstimator().estimate(node);
        else
            node.setH(0);
    }
}

package GenericGraphSearch;

/**
 * Created by ressay on 01/03/18.
 */
abstract public class HeuristicEvaluator extends Evaluator
{
    /*
    Estimates heuristic value of a given node
     */
    private HeuristicEstimator estimator = null;

    /*
    Override evaluateF from evaluating cost only to evaluating cost and heuristic.
    Cost can be evaluated in addingNodePreEvaluation or addingNodePostEvaluation.
     */
    @Override
    void evaluateF(Node node)
    {
        addingNodePreEvaluation(node);
        // estimate heuristic.
        if(getEstimator() != null)
            getEstimator().estimate(node);
        else
            node.setH(0);
        addingNodePostEvaluation(node);
    }

    /*
    Methods to be Overridden if necessary.
     */
    protected void addingNodePostEvaluation(Node node)
    {

    }

    protected void addingNodePreEvaluation(Node node)
    {

    }

    public HeuristicEstimator getEstimator() {
        return estimator;
    }

    public void setEstimator(HeuristicEstimator estimator) {
        this.estimator = estimator;
    }




}

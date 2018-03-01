package GenericGraphSearch;

/**
 * Created by ressay on 01/03/18.
 */
abstract public class CostEvaluator extends Evaluator
{
    abstract protected void evaluateG(Node node);

    public void evaluateF(Node node)
    {
        evaluateG(node);
    }
}

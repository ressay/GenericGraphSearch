package GenericGraphSearch;

/**
 * Created by ressay on 24/02/18.
 */
abstract public class Evaluator
{
    /*
    if node is goal return true, else return false
     */
    abstract public boolean isGoal(Node node);
    /*
    evaluates cost to reach node
     */
    abstract void evaluateF(Node node);
}
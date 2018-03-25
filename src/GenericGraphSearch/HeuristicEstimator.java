package GenericGraphSearch;

/**
 * Created by ressay on 01/03/18.
 */
abstract public class HeuristicEstimator
{
    /*
    Method to estimate node's heuristic value
     */
    abstract public double estimate(Node node);
}

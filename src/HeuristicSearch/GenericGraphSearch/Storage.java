package HeuristicSearch.GenericGraphSearch;

/**
 * Created by ressay on 24/02/18.
 */
abstract public class Storage
{
    public abstract void add(Node node);
    public abstract boolean isEmpty();
    public abstract Node getNext();
}

package GenericGraphSearch;

/**
 * Storage stores nodes under a given politic used by both open and closed
 * Created by ressay on 24/02/18.
 */
abstract public class Storage
{
    // adds node to the storage
    public abstract void add(Node node);
    // checks if Storage is empty
    public abstract boolean isEmpty();

}

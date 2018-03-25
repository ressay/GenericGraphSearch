package GenericGraphSearch;

/**
 * Created by ressay on 25/03/18.
 */
public abstract class ClosedStorage extends Storage
{
    /*
    returns true if node is already evaluated, else returns false
     */
    public abstract boolean contains(Node node);
}

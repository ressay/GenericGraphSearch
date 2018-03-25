package GenericGraphSearch;

/**
 * Created by ressay on 25/03/18.
 */
public abstract class OpenStorage extends Storage
{
    /*
    Method that selects next node to be selected.
     */
    public abstract Node getNext();
}

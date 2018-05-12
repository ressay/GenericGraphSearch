package HeuristicSearch.Storages;

import HeuristicSearch.GenericGraphSearch.Node;

/**
 * Created by ressay on 01/03/18.
 */
public class AStarStorage extends HeapStorage
{

    public AStarStorage() {
    }

    public AStarStorage(int size) {
        super(size);
    }

    @Override
    public int compare(Node n1, Node n2) {
        if(n1.getGH() > n2.getGH()) return 1;
        if(n1.getGH() < n2.getGH()) return -1;
        return 0;
    }
}

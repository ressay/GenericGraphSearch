package HeuristicSearch.Storages;

import HeuristicSearch.GenericGraphSearch.Node;
import HeuristicSearch.GenericGraphSearch.Storage;

import java.util.ArrayList;

/**
 * Created by ressay on 24/02/18.
 */
public class DepthStorage extends Storage
{
    ArrayList<Node> list = new ArrayList<>();
    @Override
    public void add(Node node) {
        list.add(node);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Node getNext() {
        return list.remove(list.size()-1);
    }
}

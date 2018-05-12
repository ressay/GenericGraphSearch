package HeuristicSearch.Storages;

import HeuristicSearch.GenericGraphSearch.Node;
import HeuristicSearch.GenericGraphSearch.Storage;

import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class BreadthStorage extends Storage
{
    LinkedList<Node> list = new LinkedList<>();
    @Override
    public void add(Node node) {
        list.addFirst(node);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Node getNext() {
        return list.removeLast();
    }
}

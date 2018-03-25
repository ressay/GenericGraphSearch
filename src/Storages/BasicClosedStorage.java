package Storages;

import GenericGraphSearch.ClosedStorage;
import GenericGraphSearch.Node;

import java.util.LinkedList;

/**
 * A basic example of closed storage that checks if reference to node already exists
 * Created by ressay on 25/03/18.
 */
public class BasicClosedStorage extends ClosedStorage
{
    private LinkedList<Node> list = new LinkedList<>();
    @Override
    public void add(Node node) {
        list.add(node);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /*
    Checks if reference to node already exists.
    Override this method to check different attributes of node to decide if it is to be evaluated or not
     */
    @Override
    public boolean contains(Node node) {
        return list.contains(node);
    }
}

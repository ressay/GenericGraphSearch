package Storages;

import GenericGraphSearch.Node;
import GenericGraphSearch.OpenStorage;

import java.util.LinkedList;

/**
 * manages open storage as FIFO for breadth first search
 * Created by ressay on 24/02/18.
 */
public class BreadthStorage extends OpenStorage
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

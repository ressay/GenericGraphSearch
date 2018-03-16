package Storages;

import GenericGraphSearch.Node;
import GenericGraphSearch.Storage;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class BreadthStorage extends Storage
{
    LinkedList<Node> list = new LinkedList<>();
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
        return list.removeFirst();
    }
}

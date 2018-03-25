package Storages;

import GenericGraphSearch.Node;
import GenericGraphSearch.OpenStorage;

import java.util.ArrayList;

/**
 * manages open storage as LIFO for depth first search
 * Created by ressay on 24/02/18.
 */
public class DepthStorage extends OpenStorage
{
    private ArrayList<Node> list = new ArrayList<>();

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

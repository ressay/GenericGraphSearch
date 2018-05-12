package HeuristicSearch.Storages;

import HeuristicSearch.GenericGraphSearch.Node;
import HeuristicSearch.GenericGraphSearch.Storage;

import java.util.ArrayList;

/**
 * Created by ressay on 09/03/18.
 */
public class SelectMinStorage extends Storage
{
    ArrayList<Node> list = new ArrayList<>();
    @Override
    public void add(Node node)
    {
        list.add(node);
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public Node getNext()
    {
        Node min = null;
        for (Node node: list)
        {
            if(min == null || node.getGH() < min.getGH())
                min = node;
        }
        list.remove(min);
        return min;
    }
}

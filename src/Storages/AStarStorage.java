package Storages;

import GenericGraphSearch.Node;

/**
 * used for A* algorithm, manages open as heap.
 * Created by ressay on 01/03/18.
 */
public class AStarStorage extends HeapStorage
{

    public AStarStorage() {
    }

    public AStarStorage(int size) {
        super(size);
    }

    // compares nodes based on their cost+heuristic value, keeps node with minimum value as heap's root
    // HeapStorage selects root each iteration
    @Override
    public int compare(Node n1, Node n2) {
        if(n1.getGH() > n2.getGH()) return 1;
        if(n1.getGH() < n2.getGH()) return -1;
        return 0;
    }
}

package Storages;

import GenericGraphSearch.Node;

/**
 * Created by ressay on 01/03/18.
 */
public class UniformStorage extends HeapStorage
{

    public UniformStorage() {
    }

    public UniformStorage(int size) {
        super(size);
    }

    @Override
    public int compare(Node n1, Node n2) {
        if(n1.getG() > n2.getG()) return 1;
        if(n1.getG() < n2.getG()) return -1;
        return 0;
    }
}

package mainPackage;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Node;
import SAT.SATEvaluator;
import SAT.SATNode;
import Storages.BreadthStorage;
import Storages.DepthStorage;

public class Main {

    public static void main(String[] args)
    {
        GraphSearch searcher = new GraphSearch(new DepthStorage(),new SATEvaluator(),5);
        searcher.search(new SATNode(null));
    }
}

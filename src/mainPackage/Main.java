package mainPackage;

import GenericGraphSearch.GraphSearch;
import SAT.SATEvaluator;
import SAT.SATNode;
import Storages.DepthStorage;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String file = "/home/wiss/CODES/TP-MetaHeuristic/GenericGraphSearch/test.cnf";
        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
        GraphSearch searcher = new GraphSearch(new DepthStorage(),satEvaluator,75);
        SATNode n = (SATNode) searcher.search(new SATNode(null));

    }
}

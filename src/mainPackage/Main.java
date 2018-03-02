package mainPackage;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Storage;
import SAT.SATEvaluator;
import SAT.SATHeuristicEstimator;
import SAT.SATNode;
import Storages.*;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String file = "/home/wiss/CODES/TP-MetaHeuristic/GenericGraphSearch/" +
                "ai/hoos/Shortcuts/UF75.325.100/uf75-02.cnf";
        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
        satEvaluator.setEstimator(new SATHeuristicEstimator(satEvaluator));
        Storage method = new UniformStorage();
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        SATNode n = (SATNode) searcher.search(new SATNode(null));
        System.out.println("result is:");
        if (n != null)
            for (; n.getParent() != null; n = (SATNode) n.getParent()) {
                System.out.print(n.getValue() + " ");
            }
        else
            System.out.println("not satisfiable");
    }
}


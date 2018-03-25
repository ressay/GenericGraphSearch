package mainPackage;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Node;
import GenericGraphSearch.OpenStorage;
import SAT.*;
import Storages.*;

import java.io.IOException;
import java.util.LinkedList;

public class Main {


    public static void main(String[] args) throws IOException {
        String file = "test.cnf";
        SATNode n = executeSATDepth(file);
//            SATNode n = executeSATDepth(file);

        if (n != null) {
            LinkedList<Node> nodes = n.getNodesToRoot();
            nodes.removeFirst();
            for (Node no : nodes) {
                System.out.print(no + " ");
            }
            System.out.println();
        }
    }


    public static SATNode executeSAT(String file, OpenStorage method) throws IOException {

        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
        satEvaluator.setEstimator(new SATHeuristicEstimator(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        return (SATNode) searcher.search(new SATNode(null), 5);
    }

    public static SATNode executeSATAStar(String file) throws IOException {
        OpenStorage method = new AStarStorage();
        return executeSAT(file, method);
        //return null;
    }

    public static SATNode executeSATDepth(String file) throws IOException {

        OpenStorage method = new DepthStorage();
        return executeSAT(file, method);
        //return null;
    }

    public static SATNode executeSATBreadth(String file) throws IOException {

        OpenStorage method = new BreadthStorage();
        return executeSAT(file, method);
    }

}

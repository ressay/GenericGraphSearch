package mainPackage;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Node;
import GenericGraphSearch.Storage;
import SAT.SATEvaluator;
import SAT.SATHeuristicEstimator;
import SAT.SATNode;
import Storages.AStarStorage;
import Storages.BreadthStorage;
import Storages.DepthStorage;

import java.io.IOException;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws IOException {
        String file = "test.cnf";
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 10; j++) {
                String file2 = "uuf75-0" + i + ".cnf";
                System.out.println("FILE : " + file2 +"\nATTEMPT : "+(j+1));
                SATNode n = executeSATDepth("UUF75.325.100/" + file2);
//            SATNode n = executeSATDepth("test.cnf");

                if (n != null) {
                    LinkedList<Node> nodes = n.getNodesToRoot();
                    nodes.removeFirst();
                    for (Node no : nodes) {
                        System.out.print(no + " ");
                    }
                    System.out.println();
                }
            }
        }


    }

    public static SATNode executeSAT(String file, Storage method) throws IOException {

        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
        satEvaluator.setEstimator(new SATHeuristicEstimator(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        long t1 = System.currentTimeMillis();
        SATNode n = (SATNode) searcher.search(new SATNode(null), 600);
        long diff = System.currentTimeMillis() - t1;
        long seconds = diff / 1000;
        System.out.println("RESULT FOUND IN " + seconds + " IS:");
        if (n == null)
        {
            int maxNumberOfClausesSatisfied = satEvaluator.getMaxSat();
            int numberOfClause = satEvaluator.getNumberOfClauses();
            double percent = (double) 100*maxNumberOfClausesSatisfied/ numberOfClause;
            System.out.println("SATISFIED "+maxNumberOfClausesSatisfied+"/"+numberOfClause+
                    "  ("+percent+"%) \n");

        }
        return n;
    }

    public static SATNode executeSATAStar(String file) throws IOException {
        Storage method = new AStarStorage();
        return executeSAT(file, method);
    }

    public static SATNode executeSATDepth(String file) throws IOException {

        Storage method = new DepthStorage();
        return executeSAT(file, method);
    }

    public static SATNode executeSATBreadth(String file) throws IOException {

        Storage method = new BreadthStorage();
        return executeSAT(file, method);
    }
}

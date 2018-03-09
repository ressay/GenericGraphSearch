package mainPackage;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Node;
import GenericGraphSearch.Storage;
import PlotPackage.BarPlotter;
import PlotPackage.Plotter;
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
        for (int i = 1; i < 11; i++) {
            String file2 = "uuf75-0" + i + ".cnf";

            //Add the plotter here and set it up.
            //XYPlotter dataBarPlotter = new XYPlotter(file2, "Attempt", "Percentage");
            BarPlotter dataBarPlotter = new BarPlotter(file2,"Attempt","Percentage");
            dataBarPlotter.setUpAndShow();

            System.out.println("FILE : " + file2 + "\n");
            for (int j = 0; j < 10; j++) {
                System.out.println("ATTEMPT : " + (j + 1));
                SATNode n = executeSATAStar("UUF75.325.100/" + file2, dataBarPlotter, j+1
                );
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

    public static SATNode executeSAT(String file, Storage method, Plotter dataBarPlotter, int attempt) throws IOException {

        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
        satEvaluator.setEstimator(new SATHeuristicEstimator(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        long t1 = System.currentTimeMillis();
        SATNode n = (SATNode) searcher.search(new SATNode(null), 2);
        long diff = System.currentTimeMillis() - t1;
        long seconds = diff / 1000;
        System.out.println("RESULT FOUND IN " + seconds + " IS:");
        if (n == null) {
            int maxNumberOfClausesSatisfied = satEvaluator.getMaxSat();
            int numberOfClause = satEvaluator.getNumberOfClauses();
            double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
            if(dataBarPlotter != null)
                dataBarPlotter.addData(attempt, percent);
            System.out.println("SATISFIED " + maxNumberOfClausesSatisfied + "/" + numberOfClause +
                    "  (" + percent + "%) \n");

        }
        return n;
    }

    public static SATNode executeSATAStar(String file, Plotter dataBarPlotter, int attempt) throws IOException {
        Storage method = new AStarStorage();
        return executeSAT(file, method, dataBarPlotter, attempt);
        //return null;
    }

    public static SATNode executeSATDepth(String file, Plotter dataBarPlotter, int attempt) throws IOException {

        Storage method = new DepthStorage();
        return executeSAT(file, method, dataBarPlotter, attempt);
        //return null;
    }

    public static SATNode executeSATBreadth(String file, Plotter dataPlotter, int attempt) throws IOException {

        Storage method = new BreadthStorage();
        return executeSAT(file, method, dataPlotter, attempt);
    }
}

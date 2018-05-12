package mainPackage;

import HeuristicSearch.GenericGraphSearch.GraphSearch;
import HeuristicSearch.GenericGraphSearch.Node;
import HeuristicSearch.GenericGraphSearch.Storage;
import PlotPackage.BarPlotter;
import PlotPackage.Plotter;
import HeuristicSearch.SAT.*;
import HeuristicSearch.Storages.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class Main {

    public static class IntTab
    {
        int[] tab;

        public IntTab(int... tab) {
            this.tab = tab;
        }

        @Override
        public boolean equals(Object tab)
        {
            IntTab t = (IntTab) tab;
            return Arrays.equals(this.tab,t.tab);
        }
    }

    public static void main(String[] args) throws IOException {
//        String file = "test.cnf";
//        for (int i = 1; i < 11; i++) {
//            String file2 = "uf75-0" + i + ".cnf";
//
//            //Add the plotter here and set it up.
//            //XYPlotter dataBarPlotter = new XYPlotter(file2, "Attempt", "Percentage");
////            BarPlotter dataBarPlotter = new BarPlotter(file2,"Attempt","Percentage");
////            dataBarPlotter.setUpAndShow();
//
//            System.out.println("FILE : " + file2 + "\n");
//            for (int j = 0; j < 1; j++) {
//                System.out.println("ATTEMPT : " + (j + 1));
//                SATNode n = executeSATDepth("UF75.325.100/" + file2, null, j+1);
////            SATNode n = executeSATDepth("test.cnf");
//
//                if (n != null) {
//                    LinkedList<Node> nodes = n.getNodesToRoot();
//                    nodes.removeFirst();
//                    for (Node no : nodes) {
//                        System.out.print(no + " ");
//                    }
//                    System.out.println();
//                }
//            }
//        }



        HashMap<IntTab,String> map = new HashMap<>();
        map.put(new IntTab(1,2,3),"A");
        IntTab a = new IntTab(1,2);
        IntTab b = new IntTab(1,2,3);
        map.put(b,"C");
        if(map.containsKey(a))
            System.out.println("found: "+map.get(a));
        if(map.containsKey(b))
            System.out.println("found: "+map.get(b));
        if(a.equals(b))
            System.out.println("equal!!");
        else
            System.out.println("not equal!!");

    }


    public static SATNode executeSAT(String file, Storage method, Plotter dataBarPlotter, int attempt) throws IOException {

        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
        satEvaluator.setEstimator(new SATHeuristicEstimator(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        long t1 = System.currentTimeMillis();
        SATNode n = (SATNode) searcher.search(new SATNode(null), 5);
        long diff = System.currentTimeMillis() - t1;
        long seconds = diff / 1000;
        System.out.println("RESULT FOUND IN " + seconds+ " AFTER "
                + satEvaluator.numberOfEvaluation + " EVALUATIONS IS:");
        if (n == null) {
            int maxNumberOfClausesSatisfied = satEvaluator.getMaxSat();
            int numberOfClause = satEvaluator.getNumberOfClauses();
            double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
            if(dataBarPlotter != null)
                dataBarPlotter.addData(attempt, percent);
            System.out.println("SATISFIED " + maxNumberOfClausesSatisfied + "/" + numberOfClause +
                    "  (" + percent + "%) \n");
            System.out.println("number of evaluations: " + satEvaluator.numberOfEvaluation);
//            drawClausesFrequencies(satEvaluator.getClausesFrequencies());

        }
        return n;
    }


    public static SATNode executeSATDynamic(String file, Storage method, Plotter dataBarPlotter, int attempt) throws IOException {

        SATEvaluatorDynamic satEvaluator = SATEvaluatorDynamic.loadClausesFromDimacsD(file);
        satEvaluator.setHeapStorage((HeapStorage)method);
        satEvaluator.setEstimator(new SATHeuristicDynamic(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        long t1 = System.currentTimeMillis();
        SATNode n = (SATNode) searcher.search(new SATNode(null), 5);
        long diff = System.currentTimeMillis() - t1;
        long seconds = diff / 1000;
        System.out.println("RESULT FOUND IN " + seconds+ " AFTER "
                + satEvaluator.numberOfEvaluation + " EVALUATIONS IS:");
        if (n == null) {
            int maxNumberOfClausesSatisfied = satEvaluator.getMaxSat();
            int numberOfClause = satEvaluator.getNumberOfClauses();
            double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
            if(dataBarPlotter != null)
                dataBarPlotter.addData(attempt, percent);
            System.out.println("SATISFIED " + maxNumberOfClausesSatisfied + "/" + numberOfClause +
                    "  (" + percent + "%) \n");
            drawClausesFrequencies(satEvaluator.getClausesFrequencies());

        }
        return n;
    }

    static private void drawClausesFrequencies(int[] frequencies)
    {
        double maxFrequencies = 0;
        double avg = 0;
        for (int i = 0; i < frequencies.length; i++) {
            if(frequencies[i] > maxFrequencies)
                maxFrequencies = frequencies[i];
            avg += frequencies[i];
        }
        avg /= frequencies.length;
        avg = 100 * avg / maxFrequencies/4;
        Arrays.sort(frequencies);
        int cpt = 0;
        BarPlotter dataBarPlotter = new BarPlotter("frequencies","clauses","frequency percentage");
        for (int i = 0; i < frequencies.length; i++) {
            double percent = (double) 100 * frequencies[i] / maxFrequencies;
            if(percent < avg) {
                dataBarPlotter.addData(i, percent, "clauses");
                cpt++;
            }
            else
                dataBarPlotter.addData(i,percent,"clauses");
        }
        System.out.println("average is : "+avg);
        System.out.println("number of clauses frequency under the average is: " + cpt);
        dataBarPlotter.setUpAndShow();
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

    public static SATNode executeSATAStarWithMinSelection(String file, Plotter dataPlotter, int attempt) throws IOException {

        Storage method = new SelectMinStorage();
        return executeSAT(file, method, dataPlotter, attempt);
    }
}

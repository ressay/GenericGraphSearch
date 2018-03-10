package SATUI;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Storage;
import PlotPackage.BarPlotter;
import PlotPackage.Plotter;
import SAT.SATEvaluator;
import SAT.SATEvaluatorDynamic;
import SAT.SATHeuristicDynamic;
import SAT.SATNode;
import Storages.*;

import java.io.IOException;
import java.util.Arrays;

/**
 * Created by ressay on 10/03/18.
 */
public class SATToUI
{
    SATEvaluator satEvaluator;
    Controller controller;

    public SATToUI(SATEvaluator satEvaluator, Controller controller) {
        this.satEvaluator = satEvaluator;
        this.controller = controller;
    }

    public SATNode executeSAT(String file, Storage method, Plotter dataBarPlotter, int attempt) throws IOException {

        SATEvaluatorDynamic satEvaluator = SATEvaluatorDynamic.loadClausesFromDimacsD(file);
        satEvaluator.setHeapStorage((HeapStorage)method);
        satEvaluator.setEstimator(new SATHeuristicDynamic(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        long t1 = System.currentTimeMillis();
        SATNode n = (SATNode) searcher.search(new SATNode(null), 5);
        long diff = System.currentTimeMillis() - t1;
        long seconds = diff / 1000;
        String output = "RESULT FOUND IN " + seconds+ " AFTER "
                + satEvaluator.numberOfEvaluation + " EVALUATIONS IS:\n";
        if (n == null) {
            int maxNumberOfClausesSatisfied = satEvaluator.getMaxSat();
            int numberOfClause = satEvaluator.getNumberOfClauses();
            double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
            if(dataBarPlotter != null)
                dataBarPlotter.addData(attempt, percent);
            output += "SATISFIED " + maxNumberOfClausesSatisfied + "/" + numberOfClause +
                    "  (" + percent + "%) \n\n";
            drawClausesFrequencies(satEvaluator.getClausesFrequencies());

        }
        return n;
    }

    private void drawClausesFrequencies(int[] frequencies)
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

        }
        System.out.println("average is : "+avg);
        System.out.println("number of clauses frequency under the average is: " + cpt);
        dataBarPlotter.setUpAndShow();
    }

    public SATNode executeSATAStar(String file, Plotter dataBarPlotter, int attempt) throws IOException {
        Storage method = new AStarStorage();
        return executeSAT(file, method, dataBarPlotter, attempt);
        //return null;
    }

    public SATNode executeSATDepth(String file, Plotter dataBarPlotter, int attempt) throws IOException {

        Storage method = new DepthStorage();
        return executeSAT(file, method, dataBarPlotter, attempt);
        //return null;
    }

    public SATNode executeSATBreadth(String file, Plotter dataPlotter, int attempt) throws IOException {

        Storage method = new BreadthStorage();
        return executeSAT(file, method, dataPlotter, attempt);
    }

    public SATNode executeSATAStarWithMinSelection(String file, Plotter dataPlotter, int attempt) throws IOException {

        Storage method = new SelectMinStorage();
        return executeSAT(file, method, dataPlotter, attempt);
    }
}

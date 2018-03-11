package SATUI;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Node;
import GenericGraphSearch.Storage;
import PlotPackage.BarPlotter;
import PlotPackage.Plotter;
import SAT.SATEvaluator;
import SAT.SATEvaluatorDynamic;
import SAT.SATHeuristicDynamic;
import SAT.SATNode;
import Storages.*;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * Created by ressay on 10/03/18.
 */
public class SATToUI
{
    SATEvaluator satEvaluator;
    Controller controller;
    String output;
    String headOutPut;
    String solution = "";

    public SATToUI(Controller controller) {
        this.controller = controller;
    }

    public void startSolver() throws IOException {
        if(controller.listOfFiles == null || controller.listOfFiles.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No file selected");
            alert.showAndWait();
            return;
        }
        controller.informationsArea.clear();
        for(File file : controller.listOfFiles)
        {
            for (int i = 0; i < controller.getNumberOfAttempts(); i++) {
                headOutPut = "FILE : " + file.getName() + "\n";
                headOutPut = "ATTEMPT : " + (i + 1) + "\n";
                SATNode n = executeSATAStar(file, null, i+1);
                solution = "";
                if (n != null) {
                    LinkedList<Node> nodes = n.getNodesToRoot();
                    nodes.removeFirst();
                    for (Node no : nodes) {
                        System.out.print(no + " ");
                        solution += no + " ";
                    }
                    System.out.println("*");
                    solution += "\n";
                }

                controller.informationsArea.appendText(headOutPut);
                controller.informationsArea.appendText(output);
                controller.informationsArea.appendText(solution);

            }

        }
    }

    public SATNode executeSAT(File file, Storage method, Plotter dataBarPlotter, int attempt) throws IOException {

        SATEvaluatorDynamic satEvaluator = SATEvaluatorDynamic.loadClausesFromDimacsD(file.getAbsolutePath());
        satEvaluator.setHeapStorage((HeapStorage)method);
        satEvaluator.setEstimator(new SATHeuristicDynamic(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        long t1 = System.currentTimeMillis();
        SATNode n = (SATNode) searcher.search(new SATNode(null), controller.getTimeLimit());
        long diff = System.currentTimeMillis() - t1;
        long seconds = diff / 1000;
        output = "RESULT FOUND IN " + seconds+ " AFTER "
                + satEvaluator.numberOfEvaluation + " EVALUATIONS IS:\n";
        if (n == null) {
            int maxNumberOfClausesSatisfied = satEvaluator.getMaxSat();
            int numberOfClause = satEvaluator.getNumberOfClauses();
            double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
            if(dataBarPlotter != null)
                dataBarPlotter.addData(attempt, percent);
            output += "SATISFIED " + maxNumberOfClausesSatisfied + "/" + numberOfClause +
                    "  (" + percent + "%) \n\n";
//            drawClausesFrequencies(satEvaluator.getClausesFrequencies());

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

    public SATNode executeSATAStar(File file, Plotter dataBarPlotter, int attempt) throws IOException {
        Storage method = new AStarStorage();
        return executeSAT(file, method, dataBarPlotter, attempt);
        //return null;
    }

    public SATNode executeSATDepth(File file, Plotter dataBarPlotter, int attempt) throws IOException {

        Storage method = new DepthStorage();
        return executeSAT(file, method, dataBarPlotter, attempt);
        //return null;
    }

    public SATNode executeSATBreadth(File file, Plotter dataPlotter, int attempt) throws IOException {

        Storage method = new BreadthStorage();
        return executeSAT(file, method, dataPlotter, attempt);
    }

    public SATNode executeSATAStarWithMinSelection(File file, Plotter dataPlotter, int attempt) throws IOException {

        Storage method = new SelectMinStorage();
        return executeSAT(file, method, dataPlotter, attempt);
    }
}

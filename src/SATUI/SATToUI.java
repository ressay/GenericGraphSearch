package SATUI;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.HeuristicEstimator;
import GenericGraphSearch.Node;
import GenericGraphSearch.Storage;
import PlotPackage.BarPlotter;
import PlotPackage.Plotter;
import SAT.*;
import Storages.*;
import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;

/**
 * Created by ressay on 10/03/18.
 */
public class SATToUI implements Updatable
{
    SATEvaluator satEvaluator;
    Controller controller;
    String output;
    String headOutPut;
    String solution = "";
    private long startTime;
    private long timeLimit = 500;
    private boolean isSolving = false;
    SATNode satNode = null;
    long progTime = 0;

    String[] heuristics = {"Greedy","heuristic 1","heuristic 2"};


    public SATToUI(Controller controller) {
        this.controller = controller;
    }

    public void runSolver()
    {
        if(controller.listOfFiles == null || controller.listOfFiles.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No file selected");
            alert.showAndWait();
            return;
        }

        controller.runButton.setDisable(true);
        isSolving = true;
        startThreadSolver();
        System.out.println("out");
        controller.runButton.setDisable(false);
    }

    public void startThreadSolver()
    {
        System.out.println("yeah");
        new Thread(() -> {
            try {
                startSolver();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        System.out.println("done");
    }


    public void startSolver() throws IOException {
        isSolving = true;
        controller.informationsArea.clear();
        for(File file : controller.listOfFiles)
        {
            for (int i = 0; i < controller.getNumberOfAttempts(); i++) {
                setStartTime(System.currentTimeMillis());
                progTime = 0;
                controller.progress.setProgress(0);
                controller.initLineChart();
                headOutPut = "FILE : " + file.getName() + "\n";
                headOutPut = "ATTEMPT : " + (i + 1) + "\n";
                SATNode n = executeMethod(file,i+1);
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
        isSolving = false;
    }

    public SATNode executeSAT(File file, Storage method, int attempt) throws IOException {

        satEvaluator = SATEvaluatorDynamic.loadClausesFromDimacs(file.getAbsolutePath());
        satEvaluator.setEstimator(getHeuristic());
        satEvaluator.updatable = this;
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
    }

    public SATNode executeSATAStar(File file,int attempt) throws IOException {
        Storage method = new AStarStorage();
        return executeSAT(file, method, attempt);
        //return null;
    }

    public SATNode executeMethod(File file, int attempt) throws IOException {
        if(controller.getMethod().equals("Depth search"))
            return executeSATDepth(file,attempt);
        else if(controller.getMethod().equals("Breadth search"))
            return executeSATBreadth(file,attempt);
        else
            return executeSATAStar(file,attempt);
    }

    HeuristicEstimator getHeuristic()
    {
        if(controller.getHeuristic().equals(heuristics[0]))
            return new SATHeuristicEstimatorGreedy(satEvaluator);
        else if(controller.getHeuristic().equals(heuristics[1]))
            return new SATHeuristicEstimator(satEvaluator);
        else return new SATHeuristicEstimator2(satEvaluator);
    }

    public SATNode executeSATDepth(File file, int attempt) throws IOException {

        Storage method = new DepthStorage();
        return executeSAT(file, method, attempt);
        //return null;
    }

    public SATNode executeSATBreadth(File file, int attempt) throws IOException {

        Storage method = new BreadthStorage();
        return executeSAT(file, method, attempt);
    }

    public SATNode executeSATAStarWithMinSelection(File file, int attempt) throws IOException {

        Storage method = new SelectMinStorage();
        return executeSAT(file, method, attempt);
    }

    @Override
    public void update(SATNode node) {
        satNode = node;
        satEvaluator.updateClausesFrequencies(satNode.getBitSet());
        if(getTimeSinceStart() > timeLimit) {
            progTime += getTimeSinceStart();
            setStartTime(System.currentTimeMillis());
            Platform.runLater(() -> {

                double prog = (double) progTime / (double) controller.getTimeLimit()/1000;
                System.out.println("updating!! "+prog );
                controller.progress.setProgress(prog);
                innerUpdate();
            });
        }
    }

    public void innerUpdate()
    {
        if(controller.isLive()) {
//            if(getTimeSinceStart() > timeLimit)
            {
                updateXYChart();
            }
        }
    }

    public void updateXYChart()
    {
        if(satEvaluator != null)
            controller.addLineData(progTime/timeLimit,satEvaluator.getMaxSat());
    }

    public void updateAttemptPlot()
    {

    }

    public void updateClausesPlot()
    {

    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTimeSinceStart()
    {
        return System.currentTimeMillis() - startTime;
    }

}

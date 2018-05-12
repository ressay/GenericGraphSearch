package SATUI;

import ACOSAT.ACSSAT;
import HeuristicSearch.GenericGraphSearch.GraphSearch;
import HeuristicSearch.GenericGraphSearch.HeuristicEstimator;
import HeuristicSearch.GenericGraphSearch.Node;
import HeuristicSearch.GenericGraphSearch.Storage;
import HeuristicSearch.SAT.*;
import HeuristicSearch.Storages.*;
import SATDpendencies.SATInstance;
import SATII.BSOSat;
import SATII.BSOSatDynamic;
import SATII.BeeSAT;
import SATII.SATSolution;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollBar;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;

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
    private long timeLimit = 800;
    SATNode satNode = null;
    long progTime = 0;
    int iterationsUpdate = 0;
    BSOSatDynamic bsoSat;

    String[] heuristics = {"Greedy","heuristic 1","heuristic 2"};


    public SATToUI(Controller controller) {
        this.controller = controller;
    }

    public void runSolver()
    {
        iterationsUpdate = 0;
        if(controller.listOfFiles == null || controller.listOfFiles.isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No file selected");
            alert.showAndWait();
            return;
        }

        controller.runButton.setDisable(true);
        startThreadSolver();
        controller.runButton.setDisable(false);
        satEvaluator = null;
    }

    public void startThreadSolver()
    {
        controller.initAttemptChart();
        new Thread(() -> {
            try {
                startSolver();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }


    public void startSolver() throws IOException {
        controller.informationsArea.clear();
        for(File file : controller.listOfFiles)
        {
            for (int i = 0; i < controller.getNumberOfAttempts(); i++) {
                setStartTime(System.currentTimeMillis());
                progTime = 0;
                controller.progress.setProgress(0);
                Platform.runLater(() -> controller.initLineChart());
                Platform.runLater(() -> controller.addLineData(0,0));
                Platform.runLater(() -> controller.initClausesChart());
                headOutPut = "FILE : " + file.getName() + "\n";
                headOutPut = "ATTEMPT : " + (i + 1) + "\n";
                SATNode n = null;
                int maxSat = 0;
                if(controller.getMethod().equals("BSO"))
                {
                    SATII.SATInstance instance = SATII.SATInstance.loadClausesFromDimacs(file.getAbsolutePath());
                    double[] params = controller.getBsoParams();
                    int numberOfBees = (int) params[0];
                    int maxChances = (int) params[1];
                    int flip = (int) params[2];
                    int maxIterations = (int) params[3];
                    int maxLocalSearch = (int) params[4];
                    System.out.println("max iterations is:" + maxIterations);
                    SATSolution sol = BSOSat.searchBSOSAT(instance, maxIterations, flip, numberOfBees,
                            maxChances, maxLocalSearch, SATSolution.generateRandomSolution(instance));
                    solution = ""+sol;
                    maxSat = instance.getNumberOfClausesSatisfied(sol);
                    int maxNumberOfClausesSatisfied = maxSat;
                    int numberOfClause = instance.getNumberOfClauses();
                    double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
                    output = "RESULT :";
                    output += "SATISFIED " + maxSat + "/" + instance.getNumberOfClauses() +
                            "  (" + percent + "%) \n\n";
                } else if(controller.getMethod().equals("BSODynamic"))
                {
                    SATII.SATInstance instance = SATII.SATInstance.loadClausesFromDimacs(file.getAbsolutePath());
                    SATSolution start = SATSolution.generateRandomSolution(instance);
                    double heat = instance.getNumberOfVariables()/2;
                    double localSearch = instance.getNumberOfVariables()/1.8;
                    int beeSize = instance.getNumberOfVariables()/2;
                    bsoSat = BSOSatDynamic.searchBSOSATDynamic(instance, 400, beeSize, 5,
                            (int) (localSearch), heat, 40, start);
                    SATSolution sol = bsoSat.searchMultiThread(new BeeSAT(instance, start,
                            (int)localSearch));
                    solution = ""+sol+"\n\n";
                    maxSat = instance.getNumberOfClausesSatisfied(sol);
                    int maxNumberOfClausesSatisfied = maxSat;
                    int numberOfClause = instance.getNumberOfClauses();
                    double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
                    output = "RESULT :";
                    output += "SATISFIED " + maxSat + "/" + instance.getNumberOfClauses() +
                            "  (" + percent + "%) \n\n";
                }
                else if(controller.getMethod().equals("ACO"))
                {
                    SATInstance instance = SATInstance.loadClausesFromDimacs(file.getAbsolutePath());
                    double[] params = controller.getAcoParams();
                    int numberOfAnts = (int) params[0];
                    double initValue =  params[1];
                    double ro =  params[2];
                    double alpha = params[3];
                    double beta = params[4];
                    double q = params[5];
                    int maxIterations = (int) params[6];
                    SATDpendencies.SATSolution sol = launchACS(numberOfAnts, initValue, maxIterations,
                            ro, alpha, beta, q, instance);
                    solution = ""+sol;
                    maxSat = instance.getNumberOfClausesSatisfied(sol);
                    int maxNumberOfClausesSatisfied = maxSat;
                    int numberOfClause = instance.getNumberOfClauses();
                    double percent = (double) 100 * maxNumberOfClausesSatisfied / numberOfClause;
                    output = "RESULT :";
                    output += "SATISFIED " + maxSat + "/" + instance.getNumberOfClauses() +
                            "  (" + percent + "%) \n\n";

                } else
                {
                n = executeMethod(file,i+1);
                solution = "";
                if (n != null)
                {
                    LinkedList<Node> nodes = n.getNodesToRoot();
                    nodes.removeFirst();
                    for (Node no : nodes)
                    {
                        System.out.print(no + " ");
                        solution += no + " ";
                    }
                    System.out.println("*");
                    solution += "\n";
                }
                maxSat = satEvaluator.getMaxSat();
                }
                final int mSat = maxSat;
                controller.informationsArea.appendText(headOutPut);
                controller.informationsArea.appendText(output);
                controller.informationsArea.appendText(solution);
                Platform.runLater(() -> controller.progress.setProgress(1));
                if(satEvaluator != null)
                Platform.runLater(() -> drawClausesFrequencies(satEvaluator.getClausesFrequencies()));
                final int attempt = i+1;
                Platform.runLater(() -> controller.addAttemptData(""+attempt,mSat));
                if(!controller.isLive()) {

                    Platform.runLater(() -> controller.showLineKeptData());
                }

            }

        }
    }

    public static SATDpendencies.SATSolution launchACS(int numberOfAnts, double initValue, int MAXITTER, double ro, double alpha, double beta, double q, SATInstance instance)
    {
        ACSSAT acssat = null;
        try
        {
            acssat = new ACSSAT(numberOfAnts, initValue, MAXITTER, ro, alpha, beta, q, instance);
            return acssat.startResearch();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
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
//

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
        for (int i = 0; i < frequencies.length; i++) {
            double percent = (double) 100 * frequencies[i] / maxFrequencies;
            controller.addClauseData(i+"",percent);
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
            double prog = (double) progTime / (double) controller.getTimeLimit()/1000;
            Platform.runLater(() -> controller.progress.setProgress(prog));
            progTime += getTimeSinceStart();
            setStartTime(System.currentTimeMillis());
            controller.keepLineData(progTime/timeLimit,satEvaluator.getMaxSat());
            Platform.runLater(this::innerUpdate);
        }
    }

    public void innerUpdate()
    {
        if(controller.isLive()) {
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

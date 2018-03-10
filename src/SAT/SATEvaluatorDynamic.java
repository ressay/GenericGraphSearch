package SAT;

import GenericGraphSearch.Node;
import Storages.HeapStorage;
import mainPackage.TextDisplayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;

/**
 * Created by ressay on 09/03/18.
 */
public class SATEvaluatorDynamic extends SATEvaluator
{
    double[] dynamicVector;
    boolean first = true;
    long startTime;
    long timeLimit = 1;
    HeapStorage heapStorage;
    double maxVal = 0;
    public SATEvaluatorDynamic(int numberOfVariables, int numberOfClauses) {
        super(numberOfVariables, numberOfClauses);
        dynamicVector = new double[numberOfClauses];
        for (int i = 0; i < numberOfClauses; i++)
        {
            dynamicVector[i] = 1;
        }
        maxVal = numberOfClauses;
    }

    public static SATEvaluatorDynamic loadClausesFromDimacsD(String pathToCnfFile) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(pathToCnfFile));
        String line = reader.readLine();
        while (line.charAt(0) != 'p') {
            line = reader.readLine();
        }

        String first[] = line.split("\\s+");
        SATEvaluatorDynamic se = new SATEvaluatorDynamic(Integer.parseInt(first[2]),Integer.parseInt(first[3]));
        se.clauses = new int[se.getNumberOfClauses()][se.getNumberOfVariables()];
        se.variablesBitSet = new BitSet[2][se.getNumberOfVariables()];
        for (int i = 0; i < se.getNumberOfVariables(); i++) {
            se.variablesBitSet[0][i] = new BitSet(se.getNumberOfClauses());
            se.variablesBitSet[1][i] = new BitSet(se.getNumberOfClauses());
        }
        int i = 0;
        while ((line = reader.readLine()) != null && i < se.getNumberOfClauses()) {
            if(line.length()>0 && line.charAt(0) == ' ')
                line = line.substring(1);
            String sLine[] = line.split("\\s+");
            for (int j = 0; j < sLine.length - 1; j++) {
                int i1 = Integer.parseInt(sLine[j]);
                se.clauses[i][Math.abs(i1)-1]
                        = i1 / Math.abs(i1);
                int index = (i1 > 0)?1:0;
                se.variablesBitSet[index][Math.abs(i1)-1].set(i);
            }
            i++;
        }
        se.generateRandomMap();
//        se.generateMapByNumberOfAppearance();
//        se.generateMapByNumberOfAppearanceReversed();
        reader.close();
        return se;
    }

    @Override
    public boolean isGoal(Node node) {
        if(first)
        {
            first = false;
            setStartTime(System.currentTimeMillis());
        }
        SATNode satNode = (SATNode) node;
        int numSatisfied = satNode.getNumberOfClausesSatisfied();
        if (numSatisfied > maxSat) {
            maxSat = numSatisfied;
            TextDisplayer.getInstance().showText("Depth : " + satNode.getDepth() + "|" + maxSat,
                    TextDisplayer.MOREINFORMATIONS);
        }
        numberOfEvaluation++;
        updateClausesFrequencies(satNode.getBitSet());
        if(getTimeSinceStart() > timeLimit)
        {
            setStartTime(System.currentTimeMillis());
            updateDynamicVector();
            updateNodesValues(heapStorage.getHeap(),heapStorage.getCurrentSize());
//            System.out.println("appearances: ");
//            for (int i = 0; i < numberOfClauses; i++) {
//                System.out.println(clausesFrequencies[i] + " vs " + dynamicVector[i]);
//            }
        }
        return satNode.getNumberOfClausesSatisfied() == getNumberOfClauses();
    }

    protected void updateDynamicVector()
    {
        int[] frequencies = getClausesFrequencies();
        double maxFrequencies = 0;
        for (int i = 0; i < frequencies.length; i++) {
            if(frequencies[i] > maxFrequencies)
                maxFrequencies = frequencies[i];
        }
        maxVal = 0;
        for (int i = 0; i < frequencies.length; i++) {
            double percent = (double) 100 * frequencies[i] / maxFrequencies;
            dynamicVector[i] = (percent>0)?(100/percent):100;
            maxVal += dynamicVector[i];
        }
    }

    protected double getEvaluationOfBitset(BitSet bitSet)
    {
        double sum = 0;
        for (int i = 0; i < numberOfClauses; i++) {
            sum += (bitSet.get(i))?dynamicVector[i]:0;
        }
        return maxVal-sum;
    }

    protected void updateNodesValues(Node[] nodes,int length)
    {
        for (int i = 0; i < length; i++) {
            SATNode satNode = (SATNode) nodes[i];
            nodes[i].setH(getEvaluationOfBitset(satNode.getBitSet()));
            nodes[i].setG(0);
        }
    }

    public HeapStorage getHeapStorage() {
        return heapStorage;
    }

    public void setHeapStorage(HeapStorage heapStorage) {
        this.heapStorage = heapStorage;
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

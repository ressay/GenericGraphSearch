package SAT;

import GenericGraphSearch.HeuristicEvaluator;
import GenericGraphSearch.Node;
import mainPackage.TextDisplayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by ressay on 24/02/18.
 */
public class SATEvaluator extends HeuristicEvaluator {

    protected int[][] clauses;
    protected int[] clausesFrequencies;
    protected BitSet[][] variablesBitSet;
    protected int numberOfVariables, numberOfClauses;
    protected int tauxSat = 0;
    protected int maxSat = 0;
    protected int maxDepth = 0;
    protected int[] map;
    public long numberOfEvaluation = 0;

    public SATEvaluator(int numberOfVariables, int numberOfClauses) {
        this.numberOfVariables = numberOfVariables;
        this.numberOfClauses = numberOfClauses;
        map = new int[getNumberOfVariables()];
        for (int i = 0; i < getNumberOfVariables(); i++) {
            map[i] = i;
        }
        clausesFrequencies = new int[numberOfClauses];
        for (int i = 0; i < numberOfClauses; i++) {
            clausesFrequencies[i] = 0;
        }
    }

    public static SATEvaluator loadClausesFromDimacs(String pathToCnfFile) throws IOException {

        BufferedReader reader = new BufferedReader(new FileReader(pathToCnfFile));
        String line = reader.readLine();
        while (line.charAt(0) != 'p') {
            line = reader.readLine();
        }

        String first[] = line.split("\\s+");
        SATEvaluator se = new SATEvaluator(Integer.parseInt(first[2]),Integer.parseInt(first[3]));
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

    protected void generateRandomMap()
    {
        map = new int[getNumberOfVariables()];
        ArrayList<Integer> ints = new ArrayList<>();
        for (int i = 0; i < getNumberOfVariables(); i++) {
            ints.add(i);
        }
        for (int i = 0; i < getNumberOfVariables(); i++) {
            Random random = new Random();
            int randomInt = random.nextInt(ints.size())%ints.size();
            map[i] = ints.get(randomInt);
            ints.remove(randomInt);
        }
    }

    protected void generateMapByNumberOfAppearance()
    {
        int[] appearances = new int[getNumberOfVariables()];
        for (int i = 0; i < getNumberOfVariables(); i++) {
            appearances[i] = getNumberOfAppearanceOfNodeDepth(i+1);
        }
        for (int i = 0; i < getNumberOfVariables(); i++) {
            int max = appearances[i];
            int maxMap = map[i];
            int k = i;
            for (int j = i+1; j < getNumberOfVariables(); j++) {
                if(appearances[j] > max)
                {
                    max = appearances[j];
                    maxMap = map[j];
                    k = j;
                }
            }
            appearances[k] = appearances[i];
            appearances[i] = max;
            map[k] = map[i];
            map[i] = maxMap;
        }
    }

    protected void generateMapByNumberOfAppearanceReversed()
    {
        generateMapByNumberOfAppearance();
        int[] copy = new int[getNumberOfVariables()];
        int k = getNumberOfVariables()-1;
        for (int i = 0; i < getNumberOfVariables(); i++) {
            copy[k--] = map[i];
        }
        k=0;
        for (int i = 0; i < getNumberOfVariables(); i++) {
            map[i] = copy[k++];
        }
    }

    public int getDepth() {
        return getNumberOfVariables();
    }


    @Override
    protected void addingNodePreEvaluation(Node node)
    {
        SATNode satNode = (SATNode) node;
        satNode.setIndex(map[node.getDepth()-1]);
    }

    @Override
    public boolean isGoal(Node node) {
        SATNode satNode = (SATNode) node;
        int numSatisfied = satNode.getNumberOfClausesSatisfied();
        if (numSatisfied > maxSat) {
            maxSat = numSatisfied;
            TextDisplayer.getInstance().showText("Depth : " + satNode.getDepth() + "|" + maxSat,
                    TextDisplayer.MOREINFORMATIONS);
        }
        numberOfEvaluation++;
//        updateClausesFrequencies(satNode.getBitSet());
        return satNode.getNumberOfClausesSatisfied() == getNumberOfClauses();
    }


    public int getNumberOfSatisfiedClauses(LinkedList<Node> nodes)
    {
        int cpt = 0;
        int j;
        for (int i = 0;i < getNumberOfClauses();i++) {
            j = 0;
            boolean SATc = false;
            for (Node node :  nodes) {
                if (clauses[i][getVarFromDepth(j+1)] * ((SATNode) node).getValue() > 0) {
                    SATc = true;
                    break;
                }
                j++;
            }
            if (SATc)
                cpt++;
        }
        return cpt;
    }

    public int getNumberOfVariables() {
        return numberOfVariables;
    }

    public void setNumberOfVariables(int numberOfVariables) {
        this.numberOfVariables = numberOfVariables;
    }

    public int getNumberOfClauses() {
        return numberOfClauses;
    }

    public void setNumberOfClauses(int numberOfClauses) {
        this.numberOfClauses = numberOfClauses;
    }

    public int getNumberOfAppearanceOfNode(SATNode node)
    {
        int index = (node.getValue() == -1)?0:1;
        int var = getVarFromDepth(node.getDepth());
        return variablesBitSet[index][var].cardinality();
    }

    public int getNumberOfAppearanceOfNodeDepth(int depth)
    {
        int var = getVarFromDepth(depth);
        return variablesBitSet[0][var].cardinality()+variablesBitSet[1][var].cardinality();
    }

    public BitSet getBitSetOf(SATNode node)
    {
        int index = (node.getValue() == -1)?0:1;
        int var = getVarFromDepth(node.getDepth());
        return variablesBitSet[index][var];
    }

    private int getVarFromDepth(int depth)
    {
        return map[depth-1];
    }

    public int getMaxSat() {
        return maxSat;
    }

    public void updateClausesFrequencies(BitSet bitset)
    {
        for (int i = 0; i < numberOfClauses; i++) {
            if(bitset.get(i))
                clausesFrequencies[i]++;
        }
    }

    public int[] getClausesFrequencies()
    {
        return clausesFrequencies;
    }

    public int getClauseFrequency(int i)
    {
        return clausesFrequencies[i];
    }

}

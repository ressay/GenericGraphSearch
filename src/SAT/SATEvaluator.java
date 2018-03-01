package SAT;

import GenericGraphSearch.Evaluator;
import GenericGraphSearch.HeuristicEvaluator;
import GenericGraphSearch.MaximumDepthExceededException;
import GenericGraphSearch.Node;
import mainPackage.TextDisplayer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class SATEvaluator extends HeuristicEvaluator {

    private int[][] clauses;
    private int numberOfVariables, numberOfClauses;
    private int tauxSat = 0;
    private int maxSat = 0;

    public static SATEvaluator loadClausesFromDimacs(String pathToCnfFile) throws IOException {
        SATEvaluator se = new SATEvaluator();

        BufferedReader reader = new BufferedReader(new FileReader(pathToCnfFile));
        String line = reader.readLine();
        while (line.charAt(0) != 'p') {
            line = reader.readLine();
        }

        String first[] = line.split("\\s+");
        se.setNumberOfVariables(Integer.parseInt(first[2]));
        se.setNumberOfClauses(Integer.parseInt(first[3]));
        se.clauses = new int[se.getNumberOfClauses()][se.getNumberOfVariables()];

        int i = 0;
        while ((line = reader.readLine()) != null) {
            String sLine[] = line.split("\\s+");
            for (int j = 0; j < sLine.length - 1; j++) {
                se.clauses[i][Math.abs(Integer.parseInt(sLine[j])) - 1]
                        = Integer.parseInt(sLine[j]) / Math.abs(Integer.parseInt(sLine[j]));
            }
            i++;
        }

        reader.close();
        return se;
    }

    public int getDepth() {
        return getNumberOfVariables();
    }


    @Override
    public boolean isGoal(Node node) {
        return false;
    }

    @Override
    protected void evaluateG(Node node) {

        LinkedList<Node> nodes = node.getNodesToRoot();
        nodes.removeLast();

        if (nodes.size() > clauses[0].length) try {
            throw new MaximumDepthExceededException();
        } catch (MaximumDepthExceededException e) {
            e.printStackTrace();
        }

        TextDisplayer.getInstance().showText("nodes size: " + nodes.size(), TextDisplayer.RANDOMCOMMENTS);
            int numSatisfied = getNumberOfSatisfiedClauses(nodes);
            if (numSatisfied > maxSat) {
                maxSat = numSatisfied;
                TextDisplayer.getInstance().showText("Depth : " + nodes.size() + "|" + maxSat,
                        TextDisplayer.MOREINFORMATIONS);
            }
            node.setG(getNumberOfClauses() - numSatisfied);

    }

    private int getNumberOfSatisfiedClauses(LinkedList<Node> nodes)
    {
        int cpt = 0;
        int j;
        for (int i = 0;i < getNumberOfClauses();i++) {
            j = 0;
            boolean SATc = false;
            for (Node node :  nodes) {
                if (clauses[i][j] * ((SATNode) node).getValue() > 0) {
                    SATc = true;
                    break;
                }
                j++;
            }
            if (SATc)
                cpt++;
        }
        if (cpt == getNumberOfClauses()) {
            for (j = 0; j < nodes.size(); j++) {
                System.out.print(((SATNode) nodes.get(j)).getValue() + " ");
            }
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
}

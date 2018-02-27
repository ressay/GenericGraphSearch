package SAT;

import GenericGraphSearch.Evaluator;
import GenericGraphSearch.Node;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class SATEvaluator extends Evaluator {

    private int[][] clauses;
    private int vars, nbrClause;
    private int tauxSat = 0;

    public static SATEvaluator loadClausesFromDimacs(String pathToCnfFile) throws IOException {
        SATEvaluator se = new SATEvaluator();

        BufferedReader reader = new BufferedReader(new FileReader(pathToCnfFile));
        String line = reader.readLine();
        while (line.charAt(0)!='p'){
            line=reader.readLine();
        }

        String first[] = line.split("\\s+");
        se.vars = Integer.parseInt(first[2]);
        se.nbrClause = Integer.parseInt(first[3]);
        se.clauses = new int[se.nbrClause][se.vars];

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


    @Override
    public boolean isGoal(Node node) {


        LinkedList<Node> nodes = node.getNodesToRoot();
        nodes.removeLast();
        if (!nodes.isEmpty()) {
            Boolean SAT = true;
            int i = 0;
            int j ;
            while (i < nbrClause && SAT) {
                j = 0;
                boolean SATc = false;
                while (!SATc && j < nodes.size()) {
                    if (clauses[i][j] * ((SATNode) nodes.get(j)).getValue()>0) {
                        //System.out.println(i+","+j);
                        SATc = true;
                    } else j++;
                }
                if (SATc)
                    i++;
                else
                    SAT = false;
            }
            if(i>tauxSat)
            {
                tauxSat=i;
                System.out.println("Depth : "+ nodes.size() + "|" + tauxSat);
            }

            if(SAT) {
                for (j = 0; j < nodes.size(); j++) {
                    System.out.print(((SATNode) nodes.get(j)).getValue() + " ");
                }
            }
            return SAT;
        }
        return false;
    }
}

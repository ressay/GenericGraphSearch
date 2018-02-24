package SAT;

import GenericGraphSearch.Evaluator;
import GenericGraphSearch.Node;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class SATEvaluator extends Evaluator
{

    @Override
    public boolean isGoal(Node node) {
        LinkedList<Node> nodes = node.getNodesToRoot();
        for(Node n : nodes)
        {
            SATNode sNode = (SATNode) n;
            System.out.print(sNode.getValue()+" ");
        }
        System.out.println();
        return false;
    }
}

package GenericGraphSearch;

import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class GraphSearch
{

    private Storage open;
    private LinkedList<Node> closed = new LinkedList<>();
    private Evaluator evaluator;
    private int maxDepth = 1000000;
    private String pathToInputFile;

    public GraphSearch(Storage open, Evaluator evaluator) {
        this.open = open;
        this.evaluator = evaluator;
    }

    public GraphSearch(Storage open, Evaluator evaluator, int maxDepth) {
        this.open = open;
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }
    public Node search(Node start) {
        LinkedList<Node> successors;
        Node current;
        open.add(start);
        while (!open.isEmpty())
        {
            current = open.getNext();
            if(evaluator.isGoal(current))
            {
                return current;
            }
            if(current.getDepth() == maxDepth) continue;
            successors = current.getSuccessors();
            for (Node successor : successors)
                if(!closed.contains(successor))
                {

                    successor.setParent(current);
                    successor.setDepth(current.getDepth()+1);
                    if(evaluator instanceof CostEvaluator)
                        ((CostEvaluator) evaluator).evaluateF(successor);
                    open.add(successor);
                }
            closed.add(current);
        }
        return null;
    }

}

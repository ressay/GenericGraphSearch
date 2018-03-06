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
    private long startTime;

    public GraphSearch(Storage open, Evaluator evaluator) {
        this.open = open;
        this.evaluator = evaluator;
    }

    public GraphSearch(Storage open, Evaluator evaluator, int maxDepth) {
        this.open = open;
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

    public Node search(Node start)
    {
        return search(start,100000000);
    }
    public Node search(Node start, int timeLimit) {
        setStartTime(System.currentTimeMillis()/1000);
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
//                if(!closed.contains(successor))
                {
                    successor.setParent(current);
                    successor.setDepth(current.getDepth()+1);
//                    long t1 = System.nanoTime();
                    if(evaluator instanceof HeuristicEvaluator)
                        ((HeuristicEvaluator) evaluator).evaluateF(successor);
//                    System.out.println(System.nanoTime()-t1);
                    open.add(successor);
                }
//            closed.add(current);
            if(getTimeSinceStart() > timeLimit)
                return null;
        }
        return null;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTimeSinceStart()
    {
        return System.currentTimeMillis()/1000 - startTime;
    }
}

package GenericGraphSearch;

import Storages.BasicClosedStorage;

import java.util.LinkedList;

/**
 * Created by ressay on 24/02/18.
 */
public class GraphSearch
{
    /**
     * OpenStorage manages nodes to be visited.
     * selects next node to be visited.
     */
    private OpenStorage open;
    /**
     * ClosedStorage stores already visited nodes.
     */
    private ClosedStorage closed = new BasicClosedStorage();
    /**
     * Evaluator depends of the problem.
     * tells the program when goal is reached.
     */
    private Evaluator evaluator;
    /**
     * maximum depth possible.
     */
    private int maxDepth = 1000000;
    /**
     * stores starting time in order to control the search time limit.
     */
    private long startTime;

    public GraphSearch(OpenStorage open, Evaluator evaluator) {
        this.open = open;
        this.evaluator = evaluator;
    }

    public GraphSearch(OpenStorage open, Evaluator evaluator, int maxDepth) {
        this.open = open;
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

    public Node search(Node start)
    {
        return search(start,100000000);
    }

    public Node search(Node start, int timeLimit) {
        // starting time in seconds
        setStartTime(System.currentTimeMillis()/1000);
        LinkedList<Node> successors;
        Node current;

        open.add(start);
        while (!open.isEmpty()) // iterate while there are nodes to be explored
        {
            current = open.getNext(); // open selects next node to be evaluated

            if(evaluator.isGoal(current)) // evaluator checks if current node is goal
            {
                return current;
            }
            // if current's depth is maximum depth his successors are skipped
            if(current.getDepth() == maxDepth) continue;
            // node generates its successors
            successors = current.getSuccessors();

            for (Node successor : successors)
                if(!closed.contains(successor)) // if we didn't explore node yet
                {
                    successor.setParent(current);
                    successor.setDepth(current.getDepth()+1);
                    // evaluate cost to node and eventually heuristic value.
                    evaluator.evaluateF(successor);
                    // open stores nodes to be visited
                    open.add(successor);
                }
            // we add current to closed as an already visited node
            closed.add(current);
            // if time limit reached we stop algorithm and return failure
            if(getTimeSinceStart() > timeLimit)
                return null;
        }
        // if goal is not reached and there are no nodes to explore return failure
        return null;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTimeSinceStart()
    {
        return System.currentTimeMillis()/1000 - startTime;
    }

    // to change closed management of nodes
    public void setClosed(ClosedStorage closed) {
        this.closed = closed;
    }
}

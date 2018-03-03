package mainPackage;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Node;
import GenericGraphSearch.Storage;
import SAT.*;
import Storages.*;

import java.io.IOException;
import java.util.BitSet;
import java.util.LinkedList;

public class Main {

    public static void main(String[] args) throws IOException {
        String file = "test.cnf";
        for (int i = 1; i < 21; i++) {

            String file2 = "uf75-0"+1+".cnf";
            System.out.println("file: "+file2);
//            SATNode n = executeSATAStar("UF75.325.100/"+file2);
            SATNode n = executeSATAStar("test.cnf");
            int count = 1;


            if (n != null) {
                LinkedList<Node> nodes = n.getNodesToRoot();
                nodes.removeFirst();
                for (Node no : nodes) {
                    System.out.print(((SATNode) no).getValue() * count + " ");
                    count++;
                }
            }
        }



    }

    public static SATNode executeSAT(String file,Storage method) throws IOException {

        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
        satEvaluator.setEstimator(new SATHeuristicEstimator(satEvaluator));
        GraphSearch searcher = new GraphSearch(method, satEvaluator, satEvaluator.getDepth());
        long t1 = System.currentTimeMillis();
        SATNode n = (SATNode) searcher.search(new SATNode(null),7);
        long diff = System.currentTimeMillis() - t1;
        long seconds = diff/1000;
        System.out.println("result found in "+seconds+" is:");
        if(n==null)
        System.out.println("didn't find solution max satisfied: "+satEvaluator.getMaxSat());
        return n;
    }

    public static SATNode executeSATAStar(String file) throws IOException {
        Storage method = new AStarStorage();
        return executeSAT(file,method);
    }

    public static SATNode executeSATDepth(String file) throws IOException {

        Storage method = new DepthStorage();
        return executeSAT(file,method);
    }

    public static SATNode executeSATBreadth(String file) throws IOException {

        Storage method = new BreadthStorage();
        return executeSAT(file,method);
    }
}

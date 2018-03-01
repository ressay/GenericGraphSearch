package mainPackage;

import GenericGraphSearch.GraphSearch;
import GenericGraphSearch.Storage;
import SAT.SATEvaluator;
import SAT.SATNode;
import Storages.BreadthStorage;
import Storages.DepthStorage;
import Storages.HeapStorage;
import Storages.UniformStorage;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        String file = "uf75-01.cnf";
        SATEvaluator satEvaluator = SATEvaluator.loadClausesFromDimacs(file);
//        Storage method = new UniformStorage(HeapStorage.BIG);
        Storage method = new UniformStorage();
        GraphSearch searcher = new GraphSearch(method,satEvaluator,satEvaluator.getDepth());
//        SATNode n = (SATNode) searcher.search(new SATNode(null));

    }

    static int numberOfSetBits(int i)
    {
        // Java: use >>> instead of >>
        // C or C++: use uint32_t
        i = i - ((i >> 1) & 0x55555555);
        i = (i & 0x33333333) + ((i >>> 2) & 0x33333333);
        return (((i + (i >> 4)) & 0x0F0F0F0F) * 0x01010101) >> 24;
    }
}

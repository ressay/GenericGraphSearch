# GenericGraphSearch
This is a Java implementation of graph search algorithms independent of the problem to solve.
The [GraphSearch](https://github.com/ressay/GenericGraphSearch/blob/master/src/GenericGraphSearch/GraphSearch.java) class is an implementation of graph search algorithm. To use it extend classes from [GenericGraphSearch](https://github.com/ressay/GenericGraphSearch/tree/master/src/GenericGraphSearch) package to define the problem you want to solve using this algorithm.
Classes you want to know about:
## OpenStorage
Since graph search algorithms differs in their way of handling *open*, redefining `OpenStorage` changes the search algorithm. 
The [Storages](https://github.com/ressay/GenericGraphSearch/tree/master/src/Storages) package contains implementation of these Storages:

| OpenStorage   | Algorithm     |
| ------------- | ------------- |
| [DepthStorage](https://github.com/ressay/GenericGraphSearch/blob/master/src/Storages/DepthStorage.java)  | Depth First Search  |
| [BreadthStorage](https://github.com/ressay/GenericGraphSearch/blob/master/src/Storages/BreadthStorage.java)  | Breadth First Search  |
| [UniformStorage](https://github.com/ressay/GenericGraphSearch/blob/master/src/Storages/UniformStorage.java)  | Uniform Cost Search  |
| [AStarStorage](https://github.com/ressay/GenericGraphSearch/blob/master/src/Storages/AStarStorage.java)  | A* Algorithm  |

## Node
Extend this class to define your problem's nodes and how successors are generated. See [SATNode](https://github.com/ressay/GenericGraphSearch/blob/master/src/SAT/SATNode.java) for an example of SAT problem `Node` implementation.

## Evaluator
This class should contain your problem's inputs. Redefine `isGoal(Node node)` method to test if node is goal. See [SATEvaluator](https://github.com/ressay/GenericGraphSearch/blob/master/src/SAT/SATEvaluator.java) for an example of SAT problem `Evaluator` implementation.
`HeuristicEvaluator` is an `Evaluator` which uses a `HeuristicEstimator` to estimate heuristic value of nodes.

## HeuristicEstimator
Extend this class to create a new heuristic for your problem and set it on your `HeuristicEvaluator`. See [SATHeuristicEstimator](https://github.com/ressay/GenericGraphSearch/blob/master/src/SAT/SATHeuristicEstimator.java) for an example of SAT problem `HeuristicEstimator` implementation.

# Test
The [mainPackage](https://github.com/ressay/GenericGraphSearch/tree/master/src/mainPackage) contains a [Main](https://github.com/ressay/GenericGraphSearch/blob/master/src/mainPackage/Main.java) class that tests the graph search on problem SAT. The package [SAT](https://github.com/ressay/GenericGraphSearch/tree/master/src/SAT) is an example implementation of classes mentioned above. 



# GenericGraphSearch
Java implementation to graph search algorithms

This is a generic implementation of graph search algorithms independent of the problem to solve.
The [GenericGraphSearch](https://github.com/ressay/GenericGraphSearch/tree/master/src/GenericGraphSearch) package contains classes
that implements these search algorithms.
Classes you want to know about:
## OpenStorage
Since graph search algorithms differs in their way of handling *open*, redefining `OpenStorage` changes the search algorithm. 
The [Storages](https://github.com/ressay/GenericGraphSearch/tree/master/src/Storages) package contains implementation of these Storages:
| OpenStorage   | Algorithm     |

| ------------- | ------------- |

| DepthStorage  | Depth First Search  |

| BreadthStorage  | Breadth First Search  |

| UniformStorage  | Uniform Cost Search  |

| AStarStorage  | A* Algorithm  |


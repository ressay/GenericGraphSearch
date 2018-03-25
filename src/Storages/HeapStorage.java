package Storages;

import GenericGraphSearch.Node;
import GenericGraphSearch.OpenStorage;
import GenericGraphSearch.Storage;

/**
 * A storage that selects a node with a minimum value.
 * Heap storage inserts in log(n) and remove in log(n).
 * Created by ressay on 01/03/18.
 */
abstract public class HeapStorage extends OpenStorage
{
    // size of heap
    public static final int SMALL = 1<<10;
    public static final int MEDIUM = 1<<15;
    public static final int BIG = 1<<20;

    private Node[] heap;
    private int currentSize = 0;

    public HeapStorage()
    {
        heap = new Node[MEDIUM];
    }

    public HeapStorage(int size)
    {
        heap = new Node[size];
    }

    /**
     * Compares nodes to order heap
     * @param n1
     * @param n2
     * @return 1 if n1 > n2 -1 if n1 < n2 and 0 if n1 == n2
     */
    abstract public int compare(Node n1, Node n2);

    /*
    adds node to heap
     */
    @Override
    public void add(Node node)
    {
        // if maximum size reached, double the size
        if(currentSize == heap.length) {
            doubleSize();
        }
        heap[currentSize++] = node;

        for (int i = currentSize-1; i != 0 && compare(heap[i],heap[parent(i)]) < 0; i=parent(i))
            swap(i,parent(i));

    }

    @Override
    public boolean isEmpty() {
        return currentSize == 0;
    }

    // returns root node and removes it from heap
    @Override
    public Node getNext() {
        if(currentSize == 0)
        return null;
        if (currentSize == 1)
            return heap[--currentSize];
        Node node = heap[0];
        heap[0] = heap[--currentSize];
        heapify(0);
        return node;
    }

    // put node[i] in its place in heap
    private void heapify(int i)
    {
        int l = left(i),r = right(i);
        int smallest = i;
        if(l < currentSize && compare(heap[l],heap[i]) < 0) // heap[l] < heap[i]
            smallest = l;
        if(r < currentSize && compare(heap[r],heap[smallest]) < 0) // heap[r] < heap[smallest]
            r = smallest;
        if(smallest != i)
        {
            swap(i,smallest);
            heapify(smallest);
        }
    }

    /*
    returns parent index of node i
     */
    private int parent(int i)
    {
        return (i-1)/2;
    }

    /*
    returns left child index of node i
     */
    private int left(int i)
    {
        return 2*i+1;
    }

    /*
    returns right child index of node i
     */
    private int right(int i)
    {
        return 2*i+2;
    }

    /*
    swaps nodes i and j in heap
     */
    private void swap(int i,int j)
    {
        Node node = heap[i];
        heap[i] = heap[j];
        heap[j] = node;
    }

    /*
    doubles the size of heap
     */
    private void doubleSize()
    {
        Node[] newHeap = new Node[heap.length*2];
        for (int i = 0; i < currentSize; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

    public Node[] getHeap() {
        return heap;
    }

    public int getCurrentSize() {
        return currentSize;
    }
}

package Storages;

import GenericGraphSearch.Node;
import GenericGraphSearch.Storage;
import mainPackage.TextDisplayer;

/**
 * Created by ressay on 01/03/18.
 */
abstract public class HeapStorage extends Storage
{
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
     *
     * @param n1
     * @param n2
     * @return 1 if n1 > n2 -1 if n1 < n2 and 0 if n1 == n2
     */
    abstract public int compare(Node n1, Node n2);

    @Override
    public void add(Node node)
    {
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



    private int parent(int i)
    {
        return (i-1)/2;
    }

    private int left(int i)
    {
        return 2*i+1;
    }

    private int right(int i)
    {
        return 2*i+2;
    }

    private void swap(int i,int j)
    {
        Node node = heap[i];
        heap[i] = heap[j];
        heap[j] = node;
    }

    private void doubleSize()
    {
        Node[] newHeap = new Node[heap.length*2];
        for (int i = 0; i < currentSize; i++) {
            newHeap[i] = heap[i];
        }
        heap = newHeap;
    }

}

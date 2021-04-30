public class MinPQ
{
    //Variables needed to initialize a MinPQ object.
	//The minimum priority queue will be made of nodes.
    private Node[] arr;
    private int size;

    //Constructor for a MinPQ object, with the capacity of the queue as a parameter.
    protected MinPQ(int capacity)
    {
        arr = new Node[capacity+1];
        for(int i = 0; i < arr.length; i++)
        {
            arr[i] = null;
        }
        size = 0;
    }

    
    /*-------------MINIMUM PRIORITY QUEUE HELPER METHODS--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**
     * Insert a distance and vertex into the minimum priority queue.
     * @param: The distance and vertex we want to add to the queue.
     * @return: void.
     */
    protected void insert(double d, int v)
    {
        if(++size >= arr.length) 
        {
        	extendArray();
        }
        arr[size] = new Node(d, v);
        swim(size);
    }

    /**
     * Get the next vertex in the priority queue.
     * @param: null.
     * @return: An integer representing the next vertex.
     */
    protected int nextVertex()
    {
        int nextV = arr[1].vertex;
        arr[1] = arr[size--];
        arr[size+1] = null;
        sink(1);
        if(size <= arr.length/4 && arr.length > 4) 
        {
        	shrinkArray();
        }
        return nextV;
    }

    /**
     * Checks if the priority queue is empty.
     * @param: null.
     * @return: A boolean representing if the minimum priority queue is empty or not.
     */
    protected boolean isEmpty()
    { 
    	return size == 0;
    }

    
    /**
     * Swim method for the priority queue.
     * @param: A given integer K.
     * @return: void.
     */
    private void swim(int k)
    {
        Node temp;
        while(k > 1)
        {
            if(arr[k/2].dist < arr[k].dist) 
            {
            	break;
            }
            temp = arr[k/2];
            arr[k/2] = arr[k];
            arr[k] = temp;
            k = k/2;
        }
    }

    /**
     * Sink method for the priority queue.
     * @param: A given integer K.
     * @return: void.
     */
    private void sink(int k)
    {
        Node temp;
        while(2*k <= size)
        {
            int j = 2*k;
            if(j < size && arr[j].dist > arr[j+1].dist) 
            {
            	j++;
            }
            if(arr[k].dist < arr[j].dist) {
            	break;
            }
            temp = arr[j];
            arr[j] = arr[k];
            arr[k] = temp;
            k = j;
        }
    }

    /**
     * Extend the array for the priority queue.
     * @param: null.
     * @return: void.
     */
    private void extendArray()
    {
        Node[] newArr = new Node[arr.length*2];
        for(int i = 0; i < newArr.length; i++)
        {
            if(i < arr.length) 
            {
            	newArr[i] = arr[i];
            }
            else 
            {
            	newArr[i] = null;
            }
        }
        arr = newArr;
    }

    /**
     * Shrink the array for the priority queue.
     * @param: null.
     * @return: void.
     */
    private void shrinkArray()
    {
        Node[] newArr = new Node[arr.length/2];
        for(int i = 0; i < newArr.length; i++)
        {
            newArr[i] = arr[i];
        }
        arr = newArr;
    }
}
import java.util.ArrayList;

public class DijkstraShortestPaths
{
	//Variables we need to find the shortest paths between two bus stop
    private BusStopMap g;
    private Double[] distTo;
    private Integer[] edgeTo;

    //Constructor for a DijkstraShortestPaths object with parameters being the graph in question, and an integer s.
    protected DijkstraShortestPaths(BusStopMap graph, int s)
    {
        g = graph;

        //Initialize array of visited arrays to all elements false.
        boolean[] visited = new boolean[g.V()];
        for(int i = 0; i < g.V(); i++)
        {
            visited[i] = false;
        }

        //Initialize all distances to infinite.
        distTo = new Double[g.V()];
        for(int i = 0; i < g.V(); i++)
        {
            distTo[i] = null;
        }

        //Initialize all edges to null.
        edgeTo = new Integer[g.V()];
        for(int i = 0; i < g.V(); i++)
        {
            edgeTo[i] = null;
        }

        //Create a Queue for the vertices to be visited.
        MinPQ verticesToRelax = new MinPQ(3);
        distTo[s] = 0.0;
        verticesToRelax.insert(distTo[s], s);
        int currentVertex;

        //Relax all unvisited vertices.
        while(!verticesToRelax.isEmpty())
        {
            currentVertex = verticesToRelax.nextVertex();
            if(!visited[currentVertex])
            {
                visited[currentVertex] = true;
                for(Object o : g.adjacent(currentVertex))
                {
                    WeightedEdge e = (WeightedEdge) o;
                    //If distTo vertex is not infinite, compare it to current path. 
                    //If it is infinite, update distTo.
                    if(distTo[e.to()] != null)
                    {
                        if((e.weight() + distTo[currentVertex]) < distTo[e.to()])
                        {
                            distTo[e.to()] = e.weight() + distTo[currentVertex];
                            edgeTo[e.to()] = currentVertex;
                        }
                    }
                    else
                    {
                        distTo[e.to()] = e.weight() + distTo[currentVertex];
                        edgeTo[e.to()] = currentVertex;
                    }
                    //Add the unvisited vertex to the Queue.
                    verticesToRelax.insert(distTo[e.to()], e.to());
                }
            }
        }
    }
    
    /*-------------DIJKSTRA HELPER METHODS-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    /**
     * Find the distance to a certain vertex.
     * @param: The vertex we want to find the distance to.
     * @return: A double representing the distance to the vertex given as parameter.
     */
    protected Double distTo(int v)
    { return distTo[v]; }

    /**
     * Gets the path to a vertex.
     * @param: The vertex we want to get the path for.
     * @return: An Array-list of integers in reverse order representing the path with the destination vertex first and source last.
     */
    protected ArrayList<Integer> getPath(int v)
    {
        ArrayList<Integer> result = new ArrayList<Integer>();
        int vertex = v;
        result.add(vertex);
        while(edgeTo[vertex] != null)
        {
            result.add(edgeTo[vertex]);
            vertex = edgeTo[vertex];
        }
        return result;
    }
}

import java.util.ArrayList;

public class DijkstraShortestPaths
{
    private BusStopMap g;
    private Double[] distTo;
    private Integer[] edgeTo;

    public DijkstraShortestPaths(BusStopMap graph, int s)
    {
        g = graph;

        // Initialise array of visited arrays to all elements false
        boolean[] visited = new boolean[g.V()];
        for(int i = 0; i < g.V(); i++)
        {
            visited[i] = false;
        }

        // Initialise all distances to infinite
        distTo = new Double[g.V()];
        for(int i = 0; i < g.V(); i++)
        {
            distTo[i] = null;
        }

        // Initialise all edges to null
        edgeTo = new Integer[g.V()];
        for(int i = 0; i < g.V(); i++)
        {
            edgeTo[i] = null;
        }

        // Create a Queue for the vertices to be visited
        MinPQ verticesToRelax = new MinPQ(3);
        distTo[s] = 0.0;
        verticesToRelax.insert(distTo[s], s);

        int currentVertex;

        // Relax all unvisited vertices
        while(!verticesToRelax.isEmpty())
        {
            currentVertex = verticesToRelax.nextVertex();
            if(!visited[currentVertex])
            {
                visited[currentVertex] = true;
                for(Object o : g.adjacent(currentVertex))
                {
                    WeightedEdge e = (WeightedEdge) o;
                    // If distTo vertex is not infinite, compare it to current path. If it is infinite, update distTo.
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
                    // Add the unvisited vertex to the Queue
                    verticesToRelax.insert(distTo[e.to()], e.to());
                }
            }
        }
    }

    public Double distTo(int v)
    { return distTo[v]; }

    public ArrayList<Integer> getPath(int v)
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

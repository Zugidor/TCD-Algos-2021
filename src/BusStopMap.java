import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class BusStopMap
{
    // Holds key value pairs of the stops name and their associated ID
    private Map<String, Integer> stops;

    // Holds key value pairs of a stops ID and its associated index in the adjacency list
    private Map<Integer, Integer> stopIndexes;

    // Holds key value pairs of an index in the array and its associated stop name. Necessary for getting the correct stops from the graph
    private Map<Integer, String> indexToName;

    // Adjacency list representing the graph
    private ArrayList<WeightedEdge>[] adj;

    private int V;

    private DijkstraShortestPaths sps;

    public BusStopMap(String stopsFile, String stopTimesFile, String transfersFile) throws IOException
    {
        stops = new HashMap<String, Integer>();
        stopIndexes = new HashMap<Integer, Integer>();
        indexToName = new HashMap<Integer, String>();

        // Get the number of stops/vertices for the graph. Get names of stops and associated IDs.
        // Give each ID an associated index in the adjacency list
        BufferedReader reader = new BufferedReader(new FileReader(stopsFile));
        reader.readLine(); // Ignore the first line
        String line = null;
        String[] splitLine = null;
        int vertex = 0;
        int ID = 0;
        String name = null;
        while((line = reader.readLine()) != null)
        {
            splitLine = line.split(",");
            name = splitLine[2];
            ID = Integer.parseInt(splitLine[0]);
            stops.put(name, ID);
            stopIndexes.put(ID, vertex);
            indexToName.put(vertex, name);
            vertex++;
        }
        reader.close();
        V = vertex;

        adj = new ArrayList[V];
        for(int i = 0; i < V; i++) adj[i] = new ArrayList<WeightedEdge>();

        // Read in edges
        int fromID = 0;
        int toID = 0;
        int fromIndex = 0;
        int toIndex = 0;
        double weight = 0;

        // Read in edges from transfers
        reader = new BufferedReader(new FileReader(transfersFile));
        reader.readLine(); // Ignore the first line
        while((line = reader.readLine()) != null)
        {
            splitLine = line.split(",");
            fromID = Integer.parseInt(splitLine[0]);
            toID = Integer.parseInt(splitLine[1]);
            fromIndex = stopIndexes.get(fromID);
            toIndex = stopIndexes.get(toID);
            weight = (Integer.parseInt(splitLine[2]) == 0) ? 2.0 : Double.parseDouble(splitLine[3])/100;
            adj[fromIndex].add(new WeightedEdge(fromIndex, toIndex, weight));
        }
        reader.close();

        // Read in Edges from stop times
        reader = new BufferedReader(new FileReader(stopTimesFile));
        reader.readLine(); // Ignore the first line
        int lastTripID = -1;
        int currentTripID = 0;
        while((line = reader.readLine()) != null)
        {
            splitLine = line.split(",");

            // TODO get rid of entries with invalid times

            currentTripID = Integer.parseInt(splitLine[0]);
            toID = Integer.parseInt(splitLine[3]);
            if(currentTripID == lastTripID)
            {
                fromIndex = stopIndexes.get(fromID);
                toIndex = stopIndexes.get(toID);
                adj[fromIndex].add(new WeightedEdge(fromIndex,toIndex, 1.0));
            }
            lastTripID = currentTripID;
            fromID = toID;
        }
        reader.close();
    }

    public Double getCost(int toID)
    {
        if(stopIndexes.containsKey(toID)) return sps.distTo(stopIndexes.get(toID));
        else throw new IllegalArgumentException();
    }

    public ArrayList<String> getStops(int toID)
    {
        if(stopIndexes.containsKey(toID))
        {
            ArrayList<Integer> vertexPath = sps.getPath(stopIndexes.get(toID));
            if (vertexPath.size() > 0)
            {
                ArrayList<String> stopPath = new ArrayList<String>();
                int vertex = 0;
                String name = null;
                // getPath returns the list in reverse order, with the destination vertex first and source last.
                // Reverse the order for the String arraylist
                for (int i = vertexPath.size() - 1; i >= 0; i--)
                {
                    vertex = vertexPath.get(i);
                    name = indexToName.get(vertex);
                    stopPath.add(name);
                }
                return stopPath;
            } else return null;
        }
        else throw new IllegalArgumentException();
    }

    public void makePaths(int fromID)
    {
        if(stopIndexes.containsKey(fromID)) sps = new DijkstraShortestPaths(this, stopIndexes.get(fromID));
        else throw new IllegalArgumentException();
    }

    public int V()
    { return V; }

    public ArrayList<WeightedEdge> adjacent(int vertex)
    { return adj[vertex]; }
}

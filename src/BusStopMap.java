import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BusStopMap
{
	//Below are variables we need to initialize a BusStopMap object.
    //This Map will hold key value pairs of the stops name and their associated ID.
    private Map<String, Integer> stops;
    //This Map will holds key value pairs of a stops ID and its associated index in the adjacency list.
    private Map<Integer, Integer> stopIndexes;
    //This Map will hold key value pairs of an index in the array and its associated stop name (necessary for getting the correct stops from the graph).
    private Map<Integer, String> indexToName;
    //The Array-list below will be an adjacency list for representing the graph.
    private ArrayList<WeightedEdge>[] adj;
    private int V;
    private DijkstraShortestPaths sps;
    
    //Constructor for a BusStopMap object, with parameters being paths to all three input files.
    public BusStopMap(String stopsFile, String stopTimesFile, String transfersFile) throws IOException
    {
        stops = new HashMap<String, Integer>();
        stopIndexes = new HashMap<Integer, Integer>();
        indexToName = new HashMap<Integer, String>();
        
        int counterForProgressBar = 0;
        long numberOfLinesToRead = ((progressBar.numberOfLinesInAFile(stopsFile)-1) + (progressBar.numberOfLinesInAFile(stopTimesFile)-1) + (progressBar.numberOfLinesInAFile(transfersFile)-1));
        System.out.println("Reading in " + numberOfLinesToRead + " entries from stops.txt, stop_times.txt, and transfers.txt.");
        
        //Get the number of stops/vertices for the graph. Get names of stops and associated IDs.
        //Give each ID an associated index in the adjacency list
        BufferedReader reader = new BufferedReader(new FileReader(stopsFile));
        //The first line in the file simply contains the order of the data, which we can ignore.
        reader.readLine();
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
            counterForProgressBar++;
            //We only want to update the progress bar for every 1% of progress, so the function remains speedy.
            //If we updated the progress bar after every line, the I/0 required would slow down the function.
            if (counterForProgressBar % ((int)numberOfLinesToRead/100) == 0)
            {
                    progressBar.updateProgressBar(counterForProgressBar, numberOfLinesToRead);
            }
        }
        reader.close();
        V = vertex;

        adj = new ArrayList[V];
        for(int i = 0; i < V; i++) adj[i] = new ArrayList<WeightedEdge>();

        //Read in edges.
        int fromID = 0;
        int toID = 0;
        int fromIndex = 0;
        int toIndex = 0;
        double weight = 0;

        //Read in edges from transfers.
        reader = new BufferedReader(new FileReader(transfersFile));
        //The first line in the file simply contains the order of the data, which we can ignore.
        reader.readLine();
        while((line = reader.readLine()) != null)
        {
            splitLine = line.split(",");
            fromID = Integer.parseInt(splitLine[0]);
            toID = Integer.parseInt(splitLine[1]);
            fromIndex = stopIndexes.get(fromID);
            toIndex = stopIndexes.get(toID);
            weight = (Integer.parseInt(splitLine[2]) == 0) ? 2.0 : Double.parseDouble(splitLine[3])/100;
            adj[fromIndex].add(new WeightedEdge(fromIndex, toIndex, weight));
            counterForProgressBar++;
            //We only want to update the progress bar for every 1% of progress, so the function remains speedy.
            //If we updated the progress bar after every line, the I/0 required would slow down the function.
            if (counterForProgressBar % ((int)numberOfLinesToRead/100) == 0)
            {
                    progressBar.updateProgressBar(counterForProgressBar, numberOfLinesToRead);
            }
        }
        reader.close();

        //Read in edges from stop times.
        reader = new BufferedReader(new FileReader(stopTimesFile));
        //The first line in the file simply contains the order of the data, which we can ignore.
        reader.readLine();
        int lastTripID = -1;
        int currentTripID = 0;
        while((line = reader.readLine()) != null)
        {
            splitLine = line.split(",");

            //According to the assignment specification, we should not consider all invalid times.
            try
            {
                //We can try to parse the strings as LocalTime objects
            	//If there is any white-space in the string, it means the time is in the format " h:mm:ss", and we can change this to "hh:mm:ss" for convenience.
                LocalTime.parse(splitLine[1].replaceAll("\\s+", "0"), DateTimeFormatter.ofPattern("H:mm:ss"));
                LocalTime.parse(splitLine[2].replaceAll("\\s+", "0"), DateTimeFormatter.ofPattern("H:mm:ss"));
            }
            catch (Exception e) //If any exceptions arise from doing so, that means the times are invalid.
            {
                //We simply skip this iteration of the loop (i.e. this line), and don't include this entry with an invalid time.
            	counterForProgressBar++;
            	continue;
            }
            
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
            counterForProgressBar++;
            //We only want to update the progress bar for every 1% of progress, so the function remains speedy.
            //If we updated the progress bar after every line, the I/0 required would slow down the function.
            if (counterForProgressBar % ((int)numberOfLinesToRead/100) == 0)
            {
                    progressBar.updateProgressBar(counterForProgressBar, numberOfLinesToRead);
            }
        }
        //Once we have successfully read all lines in the file, we have to update the progress bar to represent this.
        progressBar.updateProgressBar((int)numberOfLinesToRead, numberOfLinesToRead);
        reader.close();
    }

    /*-------------BUS STOP MAP HELPER METHODS-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**
     * Gets the cost of going to a connecting bus stop.
     * @param: The name of the connecting bus stop.
     * @return: A double representing the cost to the bus stop.
     */
    public Double getCost(String destinationStop)
    {
        if(stops.containsKey(destinationStop))
        {
            int stopID = stops.get(destinationStop);
            return sps.distTo(stopIndexes.get(stopID));
        }
        else throw new IllegalArgumentException();
    }

    /**
     * Prints out all the stops in the shortest path between two bus stops.
     * @param: The ID of the connecting bus stop, and the cost.
     * @return: void.
     */
    public void getStops(String destinationStop, double cost)
    {
        if(stops.containsKey(destinationStop))
        {
            int stopID = stops.get(destinationStop);
            ArrayList<Integer> vertexPath = sps.getPath(stopIndexes.get(stopID));
            if (vertexPath.size() > 0)
            {
                ArrayList<String> stopPath = new ArrayList<String>();
                int vertex = 0;
                String name = null;
                //getPath returns the list in reverse order, with the destination vertex first and source last.
                //So we must reverse the order for the String Array-list.
                for (int i = vertexPath.size() - 1; i >= 0; i--)
                {
                    vertex = vertexPath.get(i);
                    name = indexToName.get(vertex);
                    stopPath.add(name);
                }
                System.out.println("\n" + String.join("", Collections.nCopies(2," ")) + "With a cost of " + cost  + ", the shortest route between those two bus stops is as follows:" + String.join("", Collections.nCopies(2," ")) + "\n");
                System.out.println(String.join("", Collections.nCopies(35,"*")) + " SHORTEST-ROUTE " + String.join("", Collections.nCopies(35,"*")));
                for(int i = 0; i < stopPath.size(); i++)
                {
                	System.out.println("["+ (i+1) + "] " + stopPath.get(i));
                }
                System.out.println(String.join("", Collections.nCopies(86,"*")));
            }
        }
        else throw new IllegalArgumentException();
    }

    /**
     * Uses Dijkstra's algorithm to find the shortest paths to every other bus stop on the map from a source bus stop.
     * @param: The name of the source bus stop.
     * @return: void.
     */
    public void makePaths(String sourceStop)
    {
        if(stops.containsKey(sourceStop))
        {
            int stopID = stops.get(sourceStop);
            sps = new DijkstraShortestPaths(this, stopIndexes.get(stopID));
        }
        else throw new IllegalArgumentException();
    }
    
    /**
     * Get method for the integer V
     * @param: null
     * @return: An integer V.
     */
    public int V()
    { return V; }

    /**
     * Finds the edge adjacent to a vertex, which is a bus stop.
     * @param: The source vertex we want to find an adjacent vertex to.
     * @return: An Array-list of weighted edges which represent the adjacent vertices.
     */
    public ArrayList<WeightedEdge> adjacent(int vertex)
    { return adj[vertex]; }
}

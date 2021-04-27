import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FloydWarshallMap
{
    // Array of costs between different nodes
    Double[][] spsCosts;

    // Array with a list of stops along shortest paths
    // Each list contains the first stop, intermediate stops, and the last stop
    // Every entry with indexes [i][i] contains the name of the stop itself
    ArrayList<String>[][] spsStopLists;

    // ID indexed array giving the stops index in the above two arrays
    Integer[] stopIndexes;

    int V;

    public FloydWarshallMap(String stopsFile, String stopTimesFile, String transfersFile) throws IOException
    {
        // Get the number of stops/vertices for the graph. Get names of stops and associated IDs.
        ArrayList<Integer> IDs = new ArrayList<Integer>();
        ArrayList<String> names = new ArrayList<String>();
        BufferedReader reader = new BufferedReader(new FileReader(stopsFile));
        int largestID = 0;
        reader.readLine(); // Ignore the first line
        String line = null;
        String[] splitLine = null;
        int ID = 0;
        int vertexCount = 0;
        while((line = reader.readLine()) != null)
        {
            splitLine = line.split(",");
            ID = Integer.parseInt(splitLine[0]);
            if(ID > largestID) largestID = ID;
            IDs.add(ID);
            names.add(splitLine[2]);
            vertexCount++;
        }
        reader.close();
        V = vertexCount;

        // Give each stop an index, storing it in the ID indexed array
        stopIndexes = new Integer[largestID+1];
        for(int i = 0; i < largestID; i++) stopIndexes[i] = null;
        for(int i = 0; i < V; i++) stopIndexes[IDs.get(i)] = i;

        // Initialise the cost array
        spsCosts = new Double[V][V];
        for(int i = 0; i < V; i++)
        {
            for(int j = 0; j < V; j++) spsCosts[i][j] = null;
            spsCosts[i][i] = 0.0;
        }

        // Initialise the stop lists
        spsStopLists = new ArrayList[V][V];
        for(int i = 0; i < V; i++)
        {
            for(int j = 0; j < V; j++) spsStopLists[i][j] = new ArrayList<String>();
        }
        for(int i = 0; i < IDs.size(); i++)
            spsStopLists[stopIndexes[IDs.get(i)]][stopIndexes[IDs.get(i)]].add(names.get(i));

        // Read in edges
        int fromID = 0;
        int toID = 0;
        int fromIndex = 0;
        int toIndex = 0;

        // Read in edges from transfers
        reader = new BufferedReader(new FileReader(transfersFile));
        reader.readLine(); // Ignore the first line
        while((line = reader.readLine()) != null)
        {
            splitLine = line.split(",");
            fromID = Integer.parseInt(splitLine[0]);
            toID = Integer.parseInt(splitLine[1]);
            fromIndex = stopIndexes[fromID];
            toIndex = stopIndexes[toID];
            spsCosts[fromIndex][toIndex] = (Integer.parseInt(splitLine[2]) == 0) ? 2.0 : Double.parseDouble(splitLine[3])/100;
            spsStopLists[fromIndex][toIndex].add(spsStopLists[fromIndex][fromIndex].get(0));
            spsStopLists[fromIndex][toIndex].add(spsStopLists[toIndex][toIndex].get(0));
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
                fromIndex = stopIndexes[fromID];
                toIndex = stopIndexes[toID];
                spsCosts[fromIndex][toIndex] = 1.0;
                spsStopLists[fromIndex][toIndex].add(spsStopLists[fromIndex][fromIndex].get(0));
                spsStopLists[fromIndex][toIndex].add(spsStopLists[toIndex][toIndex].get(0));
            }
            lastTripID = currentTripID;
            fromID = toID;
        }

        //runFloydWarshall();
    }

    public double getCost(int fromID, int toID)
    {
        if(stopIndexes[fromID] != null && stopIndexes[toID] != null)
            if(spsCosts[stopIndexes[fromID]][stopIndexes[toID]] != null)
                return spsCosts[stopIndexes[fromID]][stopIndexes[toID]];
        return -1;
    }

    public ArrayList<String> getStops(int fromID, int toID)
    {
        if(stopIndexes[fromID] != null && stopIndexes[toID] != null)
            return spsStopLists[stopIndexes[fromID]][stopIndexes[toID]];
        else return null;
    }

    /*
    private void runFloydWarshall()
    {
        for(int k = 0; k < V; k++)
        {
            for(int i = 0; i < V; i++)
            {
                for(int j = 0; j < V; j++)
                {
                    // If the shortest path between either i and k or k and j is infinite, then the path from i to j
                    // through k is not shorter than the currently known shortest path between i and j
                    if(spsCosts[i][k] != null && spsCosts[k][j] != null)
                    {
                        // If the currently known shortest path between i and j is not infinite, we must compare the
                        // path through k to it. If it is infinite and the path through k is not, replace the cost of
                        // the currently known path with the cost of the path through k
                        if (spsCosts[i][j] != null)
                        {
                            if ((spsCosts[i][k] + spsCosts[k][j]) < spsCosts[i][j])
                            {
                                spsCosts[i][j] = spsCosts[i][k] + spsCosts[k][j];
                                spsStopLists[i][j].clear();
                                //for(int n = 0; n < spsStopLists[i][k].size(); n++) spsStopLists[i][j].add(spsStopLists[i][k].get(n));
                                //for(int n = 1; n < spsStopLists[k][j].size(); n++) spsStopLists[i][j].add(spsStopLists[k][j].get(n));
                            }
                        }
                        else
                        {
                            spsCosts[i][j] = spsCosts[i][k] + spsCosts[k][j];
                            spsStopLists[i][j].clear();
                            //for(int n = 0; n < spsStopLists[i][k].size(); n++) spsStopLists[i][j].add(spsStopLists[i][k].get(n));
                            //for(int n = 1; n < spsStopLists[k][j].size(); n++) spsStopLists[i][j].add(spsStopLists[k][j].get(n));
                        }
                    }
                }
            }
        }
    }
     */
}

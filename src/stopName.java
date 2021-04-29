import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.*;

public class stopName
{
    TST ourTST;
    String namesFile;
    
    stopName(String filename) throws IOException
    {
        ourTST = new TST(); // make our Ternary Search Tree
        namesFile = filename;

        int lineID = 2; // Start at the second line to ignore the definitions line in stops.txt
        TST.allNames.clear();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine(); //This will read the first line and skip it.
        String thisLine; //Starting loop from 2nd line.
        while ((thisLine = reader.readLine()) != null)
        {
            String[] tokens = thisLine.split(",");
            String stopNameUnformatted = tokens[2];
            String[] test = stopNameUnformatted.split(" ");
            List<String> t = Arrays.asList(test);
            LinkedList<String> temp = new LinkedList<>(t);
            while (temp.get(0).equals("NB") || temp.get(0).equals("SB") ||
                    temp.get(0).equals("WB") || temp.get(0).equals("EB") ||
                    temp.get(0).equals("FLAGSTOP"))
            {
                String s = temp.remove(0);
                temp.add(s);
            }
            String stopNameFormatted = temp.toString();
            stopNameFormatted = stopNameFormatted.replaceAll("\\p{P}", ""); //Remove all punctuation added by toString().
            ourTST.put(stopNameFormatted, lineID); //Puts stop name into TST along with line number.
            lineID++;
        }
    }

    /**
     * Gets a list of stops that match or whose first few letters match the input
     * @param: The string we are looking for
     * @return: An ArrayList containing the names of all matches, or null if no matches
     */
    public ArrayList<String> queryNameWithReturn(String query) throws IOException
    {
        int returnValue = ourTST.get(query);
        if(returnValue >= 0)
        {
            ArrayList<String> results = new ArrayList<>();
            for (int i = 0; i <= TST.allNames.size() - 1; i++)
            {
                String output;
                int lineNumber = TST.allNames.get(i);
                try (Stream<String> lines = Files.lines(Paths.get(namesFile)))
                {
                    output = lines.skip(lineNumber - 1).findFirst().get(); //Line number -1 as we skipped the first line when inputting data.
                    String[] outTokens = output.split(",");
                    results.add(outTokens[2]);
                }
            }
            TST.allNames.clear();
            return results;
        }
        else return null;
    }
}
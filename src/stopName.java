import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.*;

public class stopName
{
    TST ourTST;
    String namesFile;

    stopName(String filename, String Query) throws IOException
    {
        //TST ourTST = new TST(); // make our Ternary Search Tree

        ourTST = new TST(); // make our Ternary Search Tree
        namesFile = filename;

        int lineID = 2; // Start at the second line to ignore the definitions line in stops.txt
        TST.allNames.clear();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        reader.readLine(); // this will read the first line and skip it
        String thisLine; //starting loop from 2nd line
        while ((thisLine = reader.readLine()) != null)
        {
            String[] tokens = thisLine.split(",");
            String stopNameUnformatted = tokens[2];
            String[] test = stopNameUnformatted.split(" ");
            List<String> t = Arrays.asList(test);
            ArrayList<String> temp = new ArrayList<>(t);
            while (temp.get(0).equals("NB") || temp.get(0).equals("SB") ||
                    temp.get(0).equals("WB") || temp.get(0).equals("EB") ||
                    temp.get(0).equals("FLAGSTOP"))
            {
                String s = temp.remove(0);
                temp.add(s);
            }
            String stopNameFormatted = temp.toString();
            stopNameFormatted = stopNameFormatted.replaceAll("\\p{P}", ""); // remove all punctuation added by toString()
            ourTST.put(stopNameFormatted, lineID); //puts stop name into TST along with line #
            lineID++;
        }
        int returnValue = ourTST.get(Query);
        if (returnValue > 0) //if 1 result found
        {
            String output;
            try (Stream<String> lines = Files.lines(Paths.get(filename)))
            {
                output = lines.skip(returnValue - 1).findFirst().get();
                String[] outTokens = output.split(",");
                System.out.println(String.join("", Collections.nCopies(35,"*")) + " SEARCH-RESULTS " + String.join("", Collections.nCopies(35,"*")));
                System.out.println(String.join("", Collections.nCopies(35," ")) + "Matching stop #1");
                System.out.println("[+] ID: " + outTokens[0]);
                System.out.println("[+] Code: " + outTokens[1]);
                System.out.println("[+] Name: " + outTokens[2]);
                System.out.println("[+] Description: " + outTokens[3]);
                System.out.println("[+] Latitude: " + outTokens[4]);
                System.out.println("[+] Longitude: " + outTokens[5]);
                System.out.println("[+] Zone ID: " + outTokens[6]);
                System.out.println("[+] URL: " + outTokens[7]);
                System.out.println("[+] Location Type: " + outTokens[8]);
                System.out.println("[+] Parent Station: " + outTokens[9]);
                System.out.println(String.join("", Collections.nCopies(86,"*")));
            }
        }
        else if (returnValue == 0) //if multiple results found
        {
            System.out.println(String.join("", Collections.nCopies(35,"*")) + " SEARCH-RESULTS " + String.join("", Collections.nCopies(35,"*")));
            for (int i = 0; i <= TST.allNames.size() - 1; i++)
            {
                String output;
                int lineNumber = TST.allNames.get(i);
                if(i != 0)
                {
                    System.out.println(String.join("", Collections.nCopies(86,"*")));
                }
                System.out.println(String.join("", Collections.nCopies(35," ")) + "Matching stop #" + (i+1));
                try (Stream<String> lines = Files.lines(Paths.get(filename)))
                {
                    output = lines.skip(lineNumber - 1).findFirst().get(); //line number -1 as we skipped the first line when inputting data
                    String[] outTokens = output.split(",");
                    System.out.println("[+] ID: " + outTokens[0]);
                    System.out.println("[+] Code: " + outTokens[1]);
                    System.out.println("[+] Name: " + outTokens[2]);
                    System.out.println("[+] Description: " + outTokens[3]);
                    System.out.println("[+] Latitude: " + outTokens[4]);
                    System.out.println("[+] Longitude: " + outTokens[5]);
                    System.out.println("[+] Zone ID: " + outTokens[6]);
                    System.out.println("[+] URL: " + outTokens[7]);
                    System.out.println("[+] Location Type: " + outTokens[8]);
                    if (outTokens.length > 9)
                    {
                        System.out.println("[+] Parent Station: " + outTokens[9]);
                    }
                    else
                    {
                        System.out.println("[+] Parent Station: ");
                    }
                }
            }
            System.out.println(String.join("", Collections.nCopies(86,"*")));
        }
        else
        {
            System.out.println("No search result found, please try again");
        }
    }

    // Second constructor for creating the TST without a query. Used for part 1 of the project for selecting stops
    stopName(String filename) throws IOException
    {
        //TST ourTST = new TST(); // make our Ternary Search Tree

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
            ArrayList<String> temp = new ArrayList<>(t);
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
            ourTST.clearAllNames();
            return results;
        }
        else return null;
    }
}
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

    // Second constructor for simply creating the TST. Used for Query 1 for selecting stops
    stopName(String filename) throws IOException
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
    }

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
                    output = lines.skip(lineNumber - 1).findFirst().get(); //line number -1 as we skipped the first line when inputting data
                    String[] outTokens = output.split(",");
                    results.add(outTokens[2]);
                }
            }
            ourTST.clearAllNames();
            return results;
        }
        else return null;
    }
    
    // Ternary Search Tree subclass
    protected static class TST
    {
        public static ArrayList<Integer> allNames = new ArrayList<>();
        ArrayList<Integer> values; // dynamic array to track all values in the TST to avoid duplicate values
        TSTNode root;
        
        TST() // new empty TST
        {
            values = new ArrayList<>();
            root = null;
        }
        
        static class TSTNode
        {
            char data;
            Integer value;
            TSTNode left, mid, right;
            
            TSTNode(char data, Integer value) // new node with null links
            {
                this.value = value;
                this.data = data;
                left = null;
                mid = null;
                right = null;
            }
        }

        void clearAllNames()
        { allNames.clear(); }
        
        /**
         * @param key String we are looking for
         * @return value associated with the key if found, -1 if not found
         */
        int get(String key)
        {
            if (key.isEmpty())
            {
                return -1;
            }
            key = key.toUpperCase(); // TST is in uppercase
            return get(root, key);
        }
        
        /**
         * recursive version of get()
         *
         * @param node TST node to continue on from
         * @param key  String we are searching for
         * @return as above
         */
        int get(TSTNode node, String key)
        {
            char c = key.charAt(0);
            if (node == null)
            {   // search miss
                return -1;
            }
            else if (key.length() > 1)
            {
                if (c == node.data)
                {   // continue down
                    return get(node.mid, key.substring(1));
                }
                else if (c > node.data)
                {   // continue right
                    return get(node.right, key);
                }
                else //if (c < node.data)
                {   // continue left
                    return get(node.left, key);
                }
            }
            else
            {   // last node
                if (c == node.data)
                {   // search hit
                    if (node.value != null)
                    {   // one result
                        return node.value;
                    }
                    else
                    {   // multiple results
                        traverse(node, "");
                        return 0;
                    }
                }
                else
                {   // search miss
                    return -1;
                }
            }
        }
    
        /**
         *
         * @param node
         * @param string
         */
        private void traverse(TSTNode node, String string)
        {
            if (node != null)
            {
                traverse(node.left, string);
                string = string + node.data;
                if (node.value != null)
                {   //if last node then add Line ID to allNames ArrayList
                    allNames.add(node.value);
                }
                traverse(node.mid, string);
                string = string.substring(0, string.length() - 1);
                traverse(node.right, string);
            }
        }
        
        /**
         * @param key   String to add to TST
         * @param value Number to associate with string
         */
        void put(String key, int value)
        {
            if (key.isEmpty() || values.contains(value))
            {
                System.out.println("invalid input to TST.put()!");
                return; // don't do anything with invalid input
            }
            key = key.toUpperCase(); // TST is in uppercase
            root = put(root, key, value);
        }
        
        /**
         * recursive version of put()
         *
         * @param node  TST node to continue on from
         * @param key   substring to continue adding
         * @param value as above
         */
        TSTNode put(TSTNode node, String key, int value)
        {
            char c = key.charAt(0);
            if (key.length() > 1)
            {
                if (node == null)
                {   // fully new string, make new intermediate node and continue down
                    node = new TSTNode(c, null);
                    node.mid = put(node.mid, key.substring(1), value);
                    return node;
                }
                else if (c == node.data)
                {   // identical substring, continue down
                    node.mid = put(node.mid, key.substring(1), value);
                    return node;
                }
                else if (c < node.data)
                {   // mismatch, continue tree left
                    node.left = put(node.left, key, value);
                    return node;
                }
                else //if (c > node.data)
                {   // mismatch, continue tree right
                    node.right = put(node.right, key, value);
                    return node;
                }
            }
            else // on last node to add
            {
                if (node == null) // new string ends here
                {
                    values.add(value);
                    return new TSTNode(c, value);
                }
                else //if (c == node.data)
                {   // duplicate string, leave unchanged
                    return node;
                }
            }
            // return node;
        }
    }
}


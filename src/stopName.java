import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.*;

public class stopName
{
    stopName(String filename, String Query) throws IOException
    {
        TST ourTST = new TST();
        int lineID = 2;
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
                System.out.println("1 Result found:");
                System.out.println(output);
            }
        }
        else if (returnValue == 0) //if multiple results found
        {
            System.out.println(TST.allNames.size() + " Results found:");
            for (int i = 0; i <= TST.allNames.size() - 1; i++)
            {
                String output;
                int lineNumber = TST.allNames.get(i);
                try (Stream<String> lines = Files.lines(Paths.get(filename)))
                {
                    output = lines.skip(lineNumber - 1).findFirst().get(); //line number -1 as we skipped the first line when inputting data
                    System.out.println(i + 1 + ": " + output);
                }
            }
        }
        else
        {
            System.out.println("No search result found, please try again");
        }
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
                    {
                        return node.value;
                    }
                    else
                    {
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
        
        private void traverse(TSTNode node, String string)
        {
            if (node != null)
            {
                traverse(node.left, string);
                string = string + node.data;
                if (node.value != null)
                {   //if last node then add Line ID to Arraylist
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


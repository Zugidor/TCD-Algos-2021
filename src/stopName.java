import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class stopName
{
    stopName(String filename) throws IOException {
        TST ourTST = new TST();
        int lineID = 1;
        long totalNumberOfLinesInFile = stopTime.numberOfLinesInAFile(filename) -1;
        System.out.println("Reading in " + totalNumberOfLinesInFile + " entries from stopNames.txt");
            BufferedReader reader = new BufferedReader(new FileReader(filename));
                reader.readLine(); // this will read the first line and skip it
            String thisLine; //starting loop from 2nd line
            while ((thisLine = reader.readLine()) != null) {
                String[] tokens = thisLine.split(",");
                String stopNameUnformatted = tokens[2];
                String[] test = stopNameUnformatted.split(" ");
                String stopNameFormatted = switch (test[0]) {//testing for keywords
                    case "NB" -> stopNameUnformatted.substring(3) + " NB";
                    case "SB" -> stopNameUnformatted.substring(3) + " SB";
                    case "WB" -> stopNameUnformatted.substring(3) + " WB";
                    case "EB" -> stopNameUnformatted.substring(3) + " EB";
                    case "FLAGSTOP" -> switch (test[1]) {
                        case "NB" -> stopNameUnformatted.substring(12) + " FLAGSTOP NB";
                        case "SB" -> stopNameUnformatted.substring(12) + " FLAGSTOP SB";
                        case "WB" -> stopNameUnformatted.substring(12) + " FLAGSTOP WB";
                        case "EB" -> stopNameUnformatted.substring(12) + " FLAGSTOP EB";
                        default -> stopNameUnformatted.substring(10) + " FLAGSTOP";
                    };
                    default -> stopNameUnformatted;
                };

                ourTST.put(stopNameFormatted, lineID); //puts stop name into TST along with line #
                lineID++;
            }

           int idOfWater = ourTST.get("Water");
           System.out.println("line id of water: " + idOfWater);


            //TODO: Read file, store names of stops, store modified names into TST as per spec sheet with line number as value...
            //TODO: ...search names in TST, get line numbers, display info from line numbers.

    }

    
    // Ternary Search Tree subclass
    protected static class TST
    {
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
         * @param key: String we are looking for
         * @return value associated with the key if found, -1 if not found
         */

        int get(String key)
        {
            if (key.isEmpty())
            {
                return -1;
            }
            key = key.toLowerCase();
            System.out.println("root: " + root);
            return get(root, key);
        }
    
        /**
         * recursive version of get()
         * @param node: TST node to continue on from
         * @param key: String we are searching for
         * @return: as above
         */

        int get(TSTNode node, String key)
        {
            char c = key.charAt(0);
            if (node == null)
            {   // search miss
                System.out.println("Search missed due to node being null");

                return -1;
            }
            if (key.length() > 1)
            {
                System.out.println("Node data: " + node.data);
                if (c == node.data)
                {   // continue down
                    return get(node.mid, key.substring(1));
                }
                else if (c > node.data)
                {   // continue right
                    return get(node.right, key.substring(1));
                }
                else //if (c < node.data)
                {   // continue left
                    return get(node.left, key.substring(1));
                }
            }
            else // last node
            {
                if (c == node.data)
                {   // search hit
                    System.out.println("search hit");

                    return node.value;
                }
                else
                {   // search miss
                    return -1;
                }
            }
        }



        /**
         * @param key: String to add to TST
         * @param value: Number to associate with string
         */

          TSTNode put(String key, int value)
        {
            if (key.isEmpty() || values.contains(value))
            {
                return null; // invalid input
            }
            key = key.toLowerCase(); // TST is in lowercase
            System.out.println(root);
            return root = put(root, key, value);

        }

        /**
         * recursive version of put()
         * @param node: TST node to continue on from
         * @param key: substring to continue adding
         * @param value: as above
         */

          TSTNode put(TSTNode node, String key, int value)
        {
            char c = key.charAt(0);
            //System.out.println(key);

            if (key.length() > 1)
            {
                if (node == null)
                {   // fully new string, make new intermediate node and continue down
                    node = new TSTNode(c, null);
                    return node.mid = put(node.mid, key.substring(1), value);
                }
                else if (c == node.data)
                {   // identical substring, continue down
                    return node.mid = put(node.mid, key.substring(1), value);
                }
                else if (c < node.data)
                {   // mismatch, continue tree left
                    return node.left = put(node.left, key, value);
                }
                else //if (c > node.data)
                {   // mismatch, continue tree right
                    return node.right = put(node.right, key, value);
                }
            }
            else // on last node to add
            {
                if (node == null)
                {   // new string ends here
                    values.add(value);
                    return new TSTNode(c, value);

                }
                else //if (c == node.data)
                {   // duplicate string
                    return node;
                }
            }
        }
    }
}

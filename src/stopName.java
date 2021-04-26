import java.util.ArrayList;

public class stopName
{
    
    private stopName(String filename)
    {
        //TODO: Read file, store names of stops, store modified names into TST as per spec sheet with line number as value...
        //TODO: ...search names in TST, get line numbers, display info from line numbers.
    }
    
    // Ternary Search Tree subclass
    private static class TST
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
                return -1;
            }
            if (key.length() > 1)
            {
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

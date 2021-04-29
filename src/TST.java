import java.util.ArrayList;

public class TST
{
    public static ArrayList<Integer> allNames = new ArrayList<>();
    ArrayList<Integer> values; //Dynamic array to track all values in the TST to avoid duplicate values.
    TSTNode root;

    TST() //New empty TST.
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
    /*-------------TERNARY SEARCH TREE HELPER METHODS---------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**
     * Clears all names in the ternary search tree.
     * @return null.
     */
    void clearAllNames()
    {
        allNames.clear();
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
        key = key.toUpperCase(); //TST is in uppercase.
        return get(root, key);
    }

    /**
     * recursive version of get()
     * @param node TST node to continue on from
     * @param key  String we are searching for
     * @return as above
     */
    int get(TSTNode node, String key)
    {
        char c = key.charAt(0);
        if (node == null)
        {   //Search miss.
            return -1;
        }
        else if (key.length() > 1)
        {
            if (c == node.data)
            {   //Continue down.
                return get(node.mid, key.substring(1));
            }
            else if (c > node.data)
            {   //Continue right.
                return get(node.right, key);
            }
            else
            {   //Continue left.
                return get(node.left, key);
            }
        }
        else
        {   //Last node.
            if (c == node.data)
            {   //Search hit.
                if (node.value != null)
                {   //One result.
                    return node.value;
                }
                else
                {   //Multiple results.
                    traverse(node, "");
                    return 0;
                }
            }
            else
            {   //Search miss.
                return -1;
            }
        }
    }

    /**
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
            {   //If last node then add Line ID to allNames ArrayList.
                allNames.add(node.value);
            }
            traverse(node.mid, string);
            string = string.substring(0, string.length() - 1);
            traverse(node.right, string);
        }
    }

    /**
     * @param key String to add to TST
     * @param value Number to associate with string
     */
    void put(String key, int value)
    {
        if (key.isEmpty() || values.contains(value))
        {
            System.out.println("invalid input to TST.put()!");
            return; //Don't do anything with invalid input.
        }
        key = key.toUpperCase(); //TST is in uppercase.
        root = put(root, key, value);
    }

    /**
     * recursive version of put()
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
            {   //Fully new string, make new intermediate node and continue down.
                node = new TSTNode(c, null);
                node.mid = put(node.mid, key.substring(1), value);
                return node;
            }
            else if (c == node.data)
            {   //Identical substring, continue down.
                node.mid = put(node.mid, key.substring(1), value);
                return node;
            }
            else if (c < node.data)
            {   //Mismatch, continue tree left.
                node.left = put(node.left, key, value);
                return node;
            }
            else
            {   //Mismatch, continue tree right.
                node.right = put(node.right, key, value);
                return node;
            }
        }
        else //On last node to add.
        {
            if (node == null) //New string ends here.
            {
                values.add(value);
                return new TSTNode(c, value);
            }
            else
            {   //Duplicate string, leave unchanged.
                return node;
            }
        }
    }
}
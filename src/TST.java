import java.util.ArrayList;

public class TST
{
	//Variables which we need for our Ternary Search Tree.
    public static ArrayList<Integer> allNames = new ArrayList<>(); //Track LineIDs while you traverse().
    TSTNode root;

    //The constructor for a new empty TST object.
    protected TST()
    {
        root = null;
    }

    /*-------------TERNARY SEARCH TREE HELPER METHODS---------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/

    /**
     * @param: String we are looking for.
     * @return: Associated with the key if found, -1 if not found.
     */
    protected int get(String key)
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
     * @param: TST node to continue on from.
     * @param: String we are searching for.
     * @return: Associated with the key if found, -1 if not found.
     */
    protected int get(TSTNode node, String key)
    {
        char c = key.charAt(0);
        if (node == null)
        {   
        	//Search miss.
            return -1;
        }
        else if (key.length() > 1)
        {
            if (c == node.data)
            {   
            	//Continue down.
                return get(node.mid, key.substring(1));
            }
            else if (c > node.data)
            {   
            	//Continue right.
                return get(node.right, key);
            }
            else
            {   //Continue left.
                return get(node.left, key);
            }
        }
        else
        {   
        	//Last node.
            if (c == node.data)
            {   
            	//Search hit.
                if (node.value != null)
                {   
                	//One result.
                    return node.value;
                }
                else
                {   
                	//Multiple results.
                    traverse(node, "");
                    return 0;
                }
            }
            else if (c > node.data)
            {
                if (node.value == null)
                {   
                	//Continue right.
                    return get(node.right, key);
                }
                //Search miss
                return -1; 
            }
            else
            {
                if (node.value == null)
                {   
                	//Continue left.
                    return get(node.left, key);
                }
                //Search miss
                return -1;
            }
        }
    }

    /**
     * @param: Node we want to traverse from.
     * @param: String we want to search from.
     * @return: void.
     */
    private void traverse(TSTNode node, String string)
    {
        if (node != null)
        {
            traverse(node.left, string);
            string = string + node.data;
            if (node.value != null)
            {   
            	//If last node then add Line ID to allNames ArrayList.
                allNames.add(node.value);
            }
            traverse(node.mid, string);
            string = string.substring(0, string.length() - 1);
            traverse(node.right, string);
        }
    }

    /**
     * @param: String to add to TST.
     * @param: Value Number to associate with string.
     * @return: void.
     */
    protected void put(String key, int value)
    {
        if (key.isEmpty())
        {
        	//Don't do anything with invalid input.
            System.out.println("invalid input to TST.put()!");
            return;
        }
        //TST is in upper-case.
        key = key.toUpperCase();
        root = put(root, key, value);
    }

    /**
     * recursive version of put()
     * @param: TST node to continue on from.
     * @param: Substring to continue adding.
     * @param: Value Number to associate with string.
     * @return: A TSTNode.
     */
    protected TSTNode put(TSTNode node, String key, int value)
    {
        char c = key.charAt(0);
        if (key.length() > 1)
        {
            if (node == null)
            {   
            	//Fully new string, make new intermediate node and continue down.
                node = new TSTNode(c, null);
                node.mid = put(node.mid, key.substring(1), value);
                return node;
            }
            else if (c == node.data)
            {   
            	//Identical substring, continue down.
                node.mid = put(node.mid, key.substring(1), value);
                return node;
            }
            else if (c < node.data)
            {   
            	//Mismatch, continue tree left.
                node.left = put(node.left, key, value);
                return node;
            }
            else
            {   
            	//Mismatch, continue tree right.
                node.right = put(node.right, key, value);
                return node;
            }
        }
        //On last node to add.
        else
        {
        	//New string ends here.
            if (node == null)
            {
                return new TSTNode(c, value);
            }
            else
            {   
            	//Duplicate string, leave unchanged.
                return node;
            }
        }
    }
}
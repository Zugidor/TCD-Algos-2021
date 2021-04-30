public class WeightedEdge
{
	//Variables we need in order to initialize a WeightedEdge object.
    private int v;
    private int w;
    private double weight;

    //Constructor for a WeightedEdge object, with parameters being the source vertex, the destination vertex, and the weight between them.
    protected WeightedEdge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    /*-------------WEIGHTED EDGE HELPER METHODS------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------*/
    /**
     * Get method for the weight
     * @param: null.
     * @return: The weight of the weighted edge.
     */
    protected double weight()
    {
        return weight;
    }

    /**
     * Get method for the source vertex
     * @param: null.
     * @return: The source vertex in the weighted edge.
     */
    protected int from()
    { 
    	return v; 
    }

    /**
     * Get method for the destination vertex
     * @param: null.
     * @return: The destination vertex in the weighted edge.
     */
    protected int to()
    { 
    	return w; 
    }
}

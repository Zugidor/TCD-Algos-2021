public class WeightedEdge
{
    private int v;
    private int w;
    private double weight;

    WeightedEdge(int v, int w, double weight)
    {
        this.v = v;
        this.w = w;
        this.weight = weight;
    }

    double weight()
    {
        return weight;
    }

    int from()
    { return v; }

    int to()
    { return w; }
}

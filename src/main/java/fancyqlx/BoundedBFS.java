package fancyqlx;

public class BoundedBFS{

    private Graph g;
    public BoundedBFS(Graph g){
        this.g = g;
    }

    public static void main(String[] args){
        String path = "graphData/data.in";
        int B = 1;
        // Defining a new graph
        Graph g = new Graph(B);
        // Constructing graph
        ConstructGraph constructor = new ConstructGraph(path,g);
        constructor.construct();
        // Printing graph
        g.printVertices();
    }

}


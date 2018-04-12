package fancyqlx;

public class Algorithm implements GraphAlgorithm {

    private Graph g;
    public Algorithm(Graph g){
        this.g = g;
    }

    public static void main(String[] args){
        String path = "graphData/data.in";
        int B = 1;
        // Defining a new graph
        Graph g = new Graph(B);
        // Defining a new algorithm
        GraphAlgorithm alg = new Algorithm(g);
        // Constructing graph
        ConstructGraph constructor = new ConstructGraph(path,g);
        constructor.construct();
        // Printing graph
        g.printVertices();
    }

}

package fancyqlx;

public class RunAlgorithm implements Runnable{

    private String algName;
    private String filePath;
    private Graph g;
    private String graphType = "";
    private int N = 0;

    public RunAlgorithm(String algName, String filePath, String graphType){
        this.algName = algName;
        this.filePath = filePath;
        this.graphType = graphType;
        g = new Graph(1);
        ConstructGraph constructGraph = new ConstructGraph(filePath,g);
        constructGraph.construct();
    }

    public Algorithm CreateAlg(String algName){
        if(algName.equals("Betweenness")){
            return new Betweenness(g);
        }else if(algName.equals("BellmanFord")){
            return new BellmanFord(g);
        }
        return null;
    }

    public void setN(int n){
        N = n;
    }

    @Override
    public void run() {
        Algorithm alg = CreateAlg(algName);
        if(alg!=null){
            System.out.println("Start "+algName+" of "+ N + " nodes with "+Thread.currentThread());
            alg.run();
        }
        else return;
        System.out.printf("round = %d\n",alg.getRound());
        String resultFile = "results/"+algName+"-"+graphType+"-"+Integer.toString(N);
        alg.writeResult(resultFile);
    }
}

package fancyqlx;

import java.lang.Math;

public class Girth {
    private int t;
    private int alpha;
    private int beta;
    private int phase;
    private Graph g;
    private int gmin;
    private int girth;

    public Girth(Graph g){
        this.t = 1;
        this.alpha = 3;
        this.g = g;
        this.beta = g.getN()*g.getW();
        this.phase = 0;
        this.gmin = 2*g.getN()*g.getW();
        this.girth = 0;
    }

    public void run(){
        while(beta - alpha > 2){
            // update t
            if(beta < g.getN()*g.getW()) {
                if(t==(alpha+beta)/4){
                    t++;
                }else{
                    t=(alpha+beta)/4;
                }
            }else{
                t = (int) Math.pow(2,phase);
            }

            // perform bounded BFS
            BoundedBFS alg = new BoundedBFS(g,t);
            alg.run();
            this.gmin = alg.getGmin();

            // update alpha and beta
            if(alg.getCondition1()){
                beta = Math.min(2*t,gmin);
            }else if(alg.getCondition2()){
                if(gmin==2*g.getN()*g.getW() || gmin>2*t){
                    alpha = 2*t;
                }else{
                    beta = Math.min(beta,gmin);
                }
            }
            phase++;
        }
        girth = beta;
    }

    public int getGirth(){
        return girth;
    }

    public int getPhase(){
        return phase;
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
        Girth alg = new Girth(g);
        alg.run();
        System.out.printf("girth = %d\n",alg.getGirth());
        System.out.printf("phase = %d\n",alg.getPhase());
    }
}

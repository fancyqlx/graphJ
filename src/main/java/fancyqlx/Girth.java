package fancyqlx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.Math;

public class Girth {
    private int t; // distance constraint
    private int alpha; // lower bound
    private int beta; // upper bound
    private int phase = 0; // phase counter
    private Graph g;
    private int gmin; // min{gv}
    private int girth = 0; // the value of girth
    private int round = 0; // the round complexity counter

    public Girth(Graph g){
        this.t = 1;
        this.alpha = 3;
        this.g = g;
        this.beta = g.getN()*g.getW();
        this.gmin = 2*g.getN()*g.getW();
    }

    public void run(){
        while(beta - alpha > 2){
            phase++;
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
            this.round += alg.getRound();

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
            //System.out.printf("%d,%d,%d,%d\n", phase,t,alpha,beta);
        }
        girth = beta;
    }

    public int getGirth(){
        return girth;
    }

    public int getPhase(){
        return phase;
    }

    public int getRound() {
        return round;
    }

    public void writeResult(String filepath){
        try{
            BufferedWriter writer = new BufferedWriter((new FileWriter(filepath,true)));
            String s = Integer.toString(g.getN()) + " " + Integer.toString(g.getM()) +
                    " " + Integer.toString(girth) + " " + Integer.toString(phase) +
                    " " + Integer.toString(round) +'\n';
            writer.write(s);
            writer.close();
        }catch (IOException e){

        }
    }


    public static void main(String[] args){
        int n = 100;
        int m = (int)(1.2 * n);
        int w = n;
        for(int i=0;i<20;i++){
            m = (int)(1.2 * n);
            String path = "graphData/graph-"+Integer.toString(n)+
                    "-"+Integer.toString(m)+"-"+Integer.toString(w);
            int B = 1;
            // Defining a new graph
            Graph g = new Graph(B);
            // Constructing graph
            ConstructGraph constructor = new ConstructGraph(path,g);
            if(constructor.construct()){
                // Printing graph
                Girth alg = new Girth(g);
                alg.run();
                System.out.printf("girth = %d\n",alg.getGirth());
                System.out.printf("phase = %d\n",alg.getPhase());
                System.out.printf("round = %d\n",alg.getRound());
                String resultFile = "results/Girth-"+Integer.toString(n)+
                        "-"+Integer.toString(m)+"-"+Integer.toString(w);
                alg.writeResult(resultFile);
                n = n + 100;
                w = n;
            }else{
                break;
            }
        }

        /*n = 100;
        m = (int)(1.2 * n);
        for(int i=0;i<9;i++){
            w = w + 10;
            String path = "graphData/graph-"+Integer.toString(n)+
                    "-"+Integer.toString(m)+"-"+Integer.toString(w);
            int B = 1;
            // Defining a new graph
            Graph g = new Graph(B);
            // Constructing graph
            ConstructGraph constructor = new ConstructGraph(path,g);
            if(constructor.construct()){
                // Printing graph
                Girth alg = new Girth(g);
                alg.run();
                System.out.printf("girth = %d\n",alg.getGirth());
                System.out.printf("phase = %d\n",alg.getPhase());
                System.out.printf("round = %d\n",alg.getRound());
                String resultFile = "results/Girth-"+Integer.toString(n)+
                        "-"+Integer.toString(m)+"-"+Integer.toString(w);
                alg.writeResult(resultFile);
            }else{
                break;
            }
        }*/
    }
}

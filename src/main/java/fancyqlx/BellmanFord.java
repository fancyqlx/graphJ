package fancyqlx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class BellmanFord implements Algorithm{

    private Graph g;
    // a map representing a queue for sending messages
    private Map<Integer, Queue<BellmanFordMessage>> outMsg;
    private int round; // round complexity counter

    public class BellmanFordMessage extends Message implements Comparable<BellmanFordMessage>{
        Integer ID;
        Integer dist;
        public BellmanFordMessage(Integer ID, Integer dist){
            this.ID = ID;
            this.dist = dist;
        }

        public int compareTo(BellmanFordMessage msg){
            return Integer.compare(ID,msg.ID);
        }
    }


    public BellmanFord(Graph g){
        this.g = g;
        outMsg = new HashMap<>();
        this.round = 0;
    }

    public void run(){
        Integer n = g.getN();
        Integer m = g.getM();
        Integer W = g.getW();
        // Initialize messages for each vertex to send
        for(Integer i : g.getVertexIDs()){
            Vertex v = g.getVertex(i);
            for(Integer j: g.getVertexIDs()){
                v.updateDistance(j,m*W);
            }
            v.updateDistance(i,0);
            outMsg.put(i,new LinkedList<>());
            outMsg.get(i).add(new BellmanFordMessage(i,0));
        }

        int num = n;
        while(num>0){
            num = n;
            // send messages to all of neighbors
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                Queue<BellmanFordMessage> q = outMsg.get(ID);
                if(!q.isEmpty()){
                    BellmanFordMessage msg = q.poll();
                    v.broadcast(msg);
                }
            }

            // receive messages from neighbors and update distance
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                for(Integer j: v.getNeighbors()){
                    Message msg = v.extractMsg(j);
                    BellmanFordMessage bMsg = null;
                    if(msg instanceof BellmanFordMessage){
                        bMsg = (BellmanFordMessage) msg;
                        int weight = v.getWeight(j);
                        // update distance and add new message
                        if(bMsg.dist+weight<v.getDistance(bMsg.ID)){
                            v.updateDistance(bMsg.ID,bMsg.dist+weight);
                            outMsg.get(ID).add(new BellmanFordMessage(bMsg.ID,bMsg.dist+weight));
                        }
                    }

                }
                if(outMsg.get(ID).isEmpty()) num--;
            }
            this.round++;
        }

    }

    public int getRound(){
        return round;
    }

    public void printDistance(){
        for(Vertex v: g.getVertices()){
            System.out.printf("%d :", v.getID());
            for(Integer i: g.getVertexIDs()){
                System.out.printf("{%d,%d} ", i, v.getDistance(i));
            }
            System.out.println();
        }
    }

    public void writeResult(String filepath){
        try{
            BufferedWriter writer = new BufferedWriter((new FileWriter(filepath,true)));
            String s = Integer.toString(g.getN()) + " " + Integer.toString(g.getM()) +
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
                BellmanFord alg = new BellmanFord(g);
                alg.run();
                System.out.printf("round = %d\n",alg.getRound());
                String resultFile = "results/BellmanFord-"+Integer.toString(n)+
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
                BellmanFord alg = new BellmanFord(g);
                alg.run();
                System.out.printf("round = %d\n",alg.getRound());
                String resultFile = "results/BellmanFord-"+Integer.toString(n)+
                        "-"+Integer.toString(m)+"-"+Integer.toString(w);
                alg.writeResult(resultFile);
            }else{
                break;
            }
        }*/
    }

}

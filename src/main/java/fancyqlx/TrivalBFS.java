package fancyqlx;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * TrivalBFS is an algorithm for computing unweighted girth
 * by using simple BFS process. Note that in order to unify the
 * definition of our graph, we let the weight of unweighted graph
 * be 1 for each edge.
 */
public class TrivalBFS {

    private Graph g;
    private int maxDist; // maximum distance in the graph
    private int gmin;
    private int round; // round complexity counter

    // a map representing a queue for sending messages
    private Map<Integer, PriorityQueue<BFSMessage>> outMsg;

    public class BFSMessage extends Message implements Comparable<BFSMessage>{
        Integer src;
        Integer dist;
        public BFSMessage(Integer src, Integer dist){
            this.src = src;
            this.dist = dist;
        }

        public int compareTo(BFSMessage msg){
            return Integer.compare(dist, msg.dist);
        }
    }

    public TrivalBFS(Graph g){
        this.g = g;
        this.maxDist = g.getN()*g.getW();
        outMsg = new HashMap<>();
        this.gmin = 2*maxDist;
        this.round = 0;
    }

    public void run(){
        // Initialize messages for each vertex to send
        for(Integer i : g.getVertexIDs()){
            Vertex v = g.getVertex(i);
            for(Integer j: g.getVertexIDs()){
                v.updateDistance(j, maxDist);
            }
            v.updateDistance(i,0);
            v.updatePre(i, i);
            outMsg.put(i,new PriorityQueue<>());
            outMsg.get(i).add(new BFSMessage(i,0));
        }

        int num = g.getN();
        while(num>0){
            num = g.getN();
            // send messages to all of neighbors
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                PriorityQueue<BFSMessage> q = outMsg.get(ID);
                if(!q.isEmpty()){
                    BFSMessage msg = q.poll();
                    for(Integer j: v.getNeighbors()){
                        if((v.getPre(msg.src)==null || !j.equals(v.getPre(msg.src)))){
                            v.send(j,msg);
                        }
                    }
                }
            }

            // receive messages from neighbors and update distance
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                for(Integer j: v.getNeighbors()){
                    Message msg = v.extractMsg(j);
                    BFSMessage bMsg = null;
                    // if the message is a BFS message, then we handle it
                    if(msg instanceof BFSMessage){
                        bMsg = (BFSMessage) msg;
                        int weight = 1;
                        // update distance and add new message
                        if(v.getDistance(bMsg.src)!=maxDist){
                            if((v.getPre(bMsg.src)==null || !j.equals(v.getPre(bMsg.src)))){
                                gmin = Math.min(gmin,v.getDistance(bMsg.src)+bMsg.dist+1);
                            }
                        }
                        else if(bMsg.dist+weight<v.getDistance(bMsg.src)){
                            v.updateDistance(bMsg.src,bMsg.dist+weight);
                            v.updatePre(bMsg.src, j);
                            // add a new message to vertex's queue
                            outMsg.get(ID).add(new BFSMessage(bMsg.src,bMsg.dist+weight));


                        }
                    }
                }
                if(outMsg.get(ID).isEmpty()) num--;
            }
            this.round++;
        }
    }

    public int getGrith(){
        return gmin;
    }

    public int getRound() {
        return round;
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
        int w = 10;
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
                TrivalBFS alg = new TrivalBFS(g);
                alg.run();
                System.out.printf("round = %d\n",alg.getRound());
                String resultFile = "results/TrivalBFS-"+Integer.toString(n)+
                        "-"+Integer.toString(m)+"-"+Integer.toString(w);
                alg.writeResult(resultFile);
                n = n + 100;
            }else{
                break;
            }
        }

        n = 100;
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
                TrivalBFS alg = new TrivalBFS(g);
                alg.run();
                System.out.printf("round = %d\n",alg.getRound());
                String resultFile = "results/TrivalBFS-"+Integer.toString(n)+
                        "-"+Integer.toString(m)+"-"+Integer.toString(w);
                alg.writeResult(resultFile);
            }else{
                break;
            }
        }
    }

}

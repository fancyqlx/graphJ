package fancyqlx;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BoundedBFS{

    private Graph g;
    private int maxDist; // maximum distance in the graph
    private int t; // distance constraint
    private boolean condition1 = false;
    private boolean condition2 = false;
    private int gmin;
    private int round; // round complexity counter

    // a map representing a queue for sending messages
    private Map<Integer, PriorityQueue<BFSMessage>> outMsg;

    public class BFSMessage extends Message implements Comparable<BFSMessage>{
        Integer hop;
        Integer src;
        Integer dist;
        public BFSMessage(Integer hop, Integer src, Integer dist){
            this.hop = hop;
            this.src = src;
            this.dist = dist;
        }

        public int compareTo(BFSMessage msg){
            return Integer.compare(hop, msg.hop);
        }
    }

    public BoundedBFS(Graph g, Integer t){
        this.g = g;
        this.maxDist = g.getN()*g.getW();
        outMsg = new HashMap<>();
        this.t = t;
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
            outMsg.get(i).add(new BFSMessage(0, i,0));
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
                        if((v.getPre(msg.src)==null || !j.equals(v.getPre(msg.src))) && msg.dist+v.getWeight(j)<=t){
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
                        int weight = v.getWeight(j);
                        // update distance and add new message
                        if(v.getDistance(bMsg.src)!=maxDist){
                            condition1 = true;
                            computeGmin();
                            return;
                        }
                        else if(bMsg.dist+weight<v.getDistance(bMsg.src)){
                            v.updateDistance(bMsg.src,bMsg.dist+weight);
                            v.updatePre(bMsg.src, j);
                            // add a new message to vertex's queue
                            if(bMsg.dist+weight<=t){
                                outMsg.get(ID).add(new BFSMessage(bMsg.hop+1,bMsg.src,bMsg.dist+weight));
                            }

                        }
                    }
                }
                if(outMsg.get(ID).isEmpty()) num--;
            }
            this.round++;
        }

        condition2 = true;
        computeGmin();
    }

    /**
     * gv is the possible cycle that each vertex can compute in local.
     * computeGmin is the function computing gv
     */
    private void computeGmin(){
        for(Vertex v: g.getVertices()){
            int gv = gmin;
            for(int i: g.getVertexIDs()){
                for(int j: v.getNeighbors()){
                    Vertex u = g.getVertex(j);
                    if((v.getPre(i)==null || j!=v.getPre(i)) && (u.getPre(i)==null || !v.getID().equals(u.getPre(i)))){
                        if(v.getDistance(i)+u.getDistance(i)+v.getWeight(j)<gv){
                            gv = v.getDistance(i)+u.getDistance(i)+v.getWeight(j);
                        }
                    }
                }
            }
            if(gv<gmin) gmin = gv;
        }
    }

    public boolean getCondition1(){
        return condition1;
    }

    public boolean getCondition2(){
        return condition2;
    }

    public int getGmin(){
        return gmin;
    }

    public int getRound() {
        return round;
    }

    public static void main(String[] args){
        String path = "graphData/graph-100-120-100";
        int B = 1;
        // Defining a new graph
        Graph g = new Graph(B);
        // Constructing graph
        ConstructGraph constructor = new ConstructGraph(path,g);
        constructor.construct();
        // Printing graph
        BoundedBFS alg = new BoundedBFS(g, 5);
        alg.run();
        if(alg.getCondition1()){
            System.out.println("Condition1");
        }else{
            System.out.println("Condition2");
        }
        System.out.printf("gmin = %d\n",alg.getGmin());
        System.out.printf("rounds = %d\n", alg.getRound());
    }

}


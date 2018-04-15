package fancyqlx;

import java.util.*;

public class BellmanFord {

    private Graph g;
    // a map for storing distance from neighbors
    private Map<Integer, Queue<BellmanFordMessage>> outMsg;

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
                    v.boardcast(msg);
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

        }

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

    public static void main(String[] args){
        String path = "graphData/data.in";
        int B = 1;
        // Defining a new graph
        Graph g = new Graph(B);
        // Constructing graph
        ConstructGraph constructor = new ConstructGraph(path,g);
        constructor.construct();
        BellmanFord alg = new BellmanFord(g);
        alg.run();
        alg.printDistance();
    }

}

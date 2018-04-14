package fancyqlx;

import java.util.*;

public class Diameter {

    private Graph g;
    // A map for storing hop from
    private Map<Integer, Queue<DiameterMessage>> outMsg;
    private Integer D;

    public class DiameterMessage extends Message implements Comparable<DiameterMessage>{
        Integer ID;
        Integer hop;
        public DiameterMessage(Integer ID, Integer hop){
            this.ID = ID;
            this.hop = hop;
        }

        public int compareTo(DiameterMessage msg){
            return Integer.compare(ID,msg.ID);
        }
    }


    public Diameter(Graph g){
        this.g = g;
        outMsg = new HashMap<>();
        D = 0;
    }

    public void run(){
        Integer n = g.getN();
        Integer m = g.getM();
        // Initialize messages for each vertex to send
        for(Integer i : g.getVertexIDs()){
            Vertex v = g.getVertex(i);
            for(Integer j: g.getVertexIDs()){
                v.updateHop(j,m);
            }
            v.updateHop(i,0);
            outMsg.put(i,new LinkedList<>());
            outMsg.get(i).add(new DiameterMessage(i,0));
        }

        int num = n;
        while(num>0){
            num = n;
            // send messages to all of neighbors
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                Queue<DiameterMessage> q = outMsg.get(ID);
                if(!q.isEmpty()){
                    DiameterMessage msg = q.poll();
                    v.boardcast(msg);
                }
            }

            // receive messages from neighbors and update hops
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                for(Integer j: v.getNeighbors()){
                    Message msg = v.extractMsg(j);
                    DiameterMessage bMsg = null;
                    if(msg instanceof DiameterMessage){
                        bMsg = (DiameterMessage) msg;

                        // update hops and add new message
                        if(bMsg.hop+1<v.getHop(bMsg.ID)){
                            v.updateHop(bMsg.ID,bMsg.hop+1);
                            outMsg.get(ID).add(new DiameterMessage(bMsg.ID,bMsg.hop+1));
                        }
                    }

                }
                if(outMsg.get(ID).isEmpty()) num--;
            }

        }

        for(Vertex v: g.getVertices()){
            for(Integer i: g.getVertexIDs()){
                if(v.getHop(i)>D){
                    D = v.getHop(i);
                }
            }
        }

    }

    public Integer getDiameter(){
        return D;
    }

    public void printHops(){
        for(Vertex v: g.getVertices()){
            System.out.printf("%d :", v.getID());
            for(Integer i: g.getVertexIDs()){
                System.out.printf("{%d,%d} ", i, v.getHop(i));
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
        Diameter alg = new Diameter(g);
        alg.run();
        alg.printHops();
        System.out.printf("Diameter is %d\n", alg.getDiameter());
    }

}

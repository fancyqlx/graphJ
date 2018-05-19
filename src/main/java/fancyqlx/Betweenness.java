package fancyqlx;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Betweenness implements Algorithm{
    private Graph g;
    // A map for storing hop from neighbors
    private Map<Integer, Queue<Betweenness.BFSMessage>> outMsg;
    private int D;
    private int round = 0; // round complexity counter

    public class BFSMessage extends Message implements Comparable<Betweenness.BFSMessage>{
        Integer ID;
        Integer hop;
        public BFSMessage(Integer ID, Integer hop){
            this.ID = ID;
            this.hop = hop;
        }

        public int compareTo(Betweenness.BFSMessage msg){
            return Integer.compare(ID,msg.ID);
        }
    }


    public Betweenness(Graph g){
        this.g = g;
        outMsg = new HashMap<>();
        D = 0;
    }

    /**
     * Betweenness algorithm needs to schedule vertices by a
     * DFS process. In each time a vertex is visited, it starts a BFS
     * process.
     */
    public void run(){
        Integer n = g.getN();
        Integer m = g.getM();
        // construct a BFS tree
        int startnode = 0;
        for(;startnode<g.getN();startnode++){
            if(g.getVertex(startnode)!=null)
                break;
        }
        BFSTree tree = new BFSTree(g,g.getVertex(startnode));
        //tree.printBFSTree();
        TreeNode root = tree.getRoot();

        // Initialize hops for each vertex
        for(Integer i : g.getVertexIDs()){
            Vertex v = g.getVertex(i);
            for(Integer j: g.getVertexIDs()){
                v.updateHop(j,m);
            }
            v.updateHop(i,0);
            outMsg.put(i,new LinkedList<>());
        }

        // Start delivering BFS messages when a vertex is visited by DFS process
        // Use stack to perform DFS process
        Stack<TreeNode> stack = new Stack<>();
        stack.push(root);
        // The loop is terminated when there is no BFS messages
        // Note that the empty stack is not a terminated condition
        int num = n;
        while(num>0){
            num = n;

            // DFS visits a vertex
            TreeNode node = null;
            if(!stack.empty()) {
                node = stack.pop();
                for (TreeNode cnode : node.children) {
                    stack.push(cnode);
                }
            }

            // send messages to all of neighbors
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                Queue<Betweenness.BFSMessage> q = outMsg.get(ID);
                if(!stack.empty() && node.vertex.getID() == ID){
                    q.add(new Betweenness.BFSMessage(ID,0));
                }
                if(!q.isEmpty()){
                    Betweenness.BFSMessage msg = q.poll();
                    v.broadcast(msg);
                }
            }

            // receive messages from neighbors and update hops
            for(Vertex v: g.getVertices()){
                Integer ID = v.getID();
                for(Integer j: v.getNeighbors()){
                    Message msg = v.extractMsg(j);
                    Betweenness.BFSMessage bMsg = null;
                    if(msg instanceof Betweenness.BFSMessage){
                        bMsg = (Betweenness.BFSMessage) msg;

                        // update hops and add new message
                        if(bMsg.hop+1<v.getHop(bMsg.ID)){
                            v.updateHop(bMsg.ID,bMsg.hop+1);
                            outMsg.get(ID).add(new Betweenness.BFSMessage(bMsg.ID,bMsg.hop+1));
                        }
                    }

                }
                if(outMsg.get(ID).isEmpty()) num--;
            }
            this.round++;
        }

        // simulate the rounds of computing Betweenness
        this.round += n;

    }

    public int getRound(){
        return round;
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

    public static void readGraphs(String graphType){
        int n = 1000;
        for(int i=0;i<20;i++){
            System.gc();
            long startTime = System.currentTimeMillis();
            String path = "graphData/"+graphType+"-"+Integer.toString(n);
            RunAlgorithm runAlgorithm = new RunAlgorithm("Betweenness",path,graphType);
            runAlgorithm.setN(n);
            runAlgorithm.run();
            long endTime = System.currentTimeMillis();
            System.out.println("Running time: "+(endTime-startTime)+" ms");
            n = n + 500;

        }
    }

    public static void main(String[] args){
        Betweenness.readGraphs("random");
    }
}

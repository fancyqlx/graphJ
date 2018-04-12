package fancyqlx;

import java.util.*;
import java.lang.*;

/**
 * Graph is a class describing properties of the graph and
 * storing the structure of vertices.
 */
public class Graph{
    private Map<Integer, Vertex> vertices; // a map for storing vertices
    private int n = 0; // the number of vertices
    private int m = 0; // the number of edges
    private int B = 0; // bandwidth
    private int W = 0; // maximum edge weight

    public Graph(int B){
        this.B = B;
        vertices = new HashMap<>();
    }

    public Vertex getVertex(Integer ID){
        return vertices.get(ID);
    }

    public int getN(){
        return n;
    }
    public int getM(){
        return m;
    }
    public int getB(){
        return B;
    }
    public void addN(){
        n++;
    }
    public void reduceN(){
        n--;
    }
    public void addM(){
        m++;
    }
    public void reduceM(){
        m--;
    }
    public void updateW(int W){
        this.W = W;
    }
    public int getW(){
        return W;
    }

    public boolean addVertex(Integer ID, Vertex v){
        if(vertices.putIfAbsent(ID,v)!= null){
            return true;
        }
        return false;
    }

    public boolean removeVertex(Integer ID){
        if(vertices.remove(ID) != null){
            return true;
        }
        return false;
    }

    public boolean removeVertex(Vertex v){
        if(vertices.remove(v.getID()) != null){
            return true;
        }
        return false;
    }

    public void printVertices(){
        System.out.printf("Graph: n=%d, m=%d\n", n, m);
        for(Map.Entry<Integer,Vertex> entry: vertices.entrySet()){
            Integer ID = entry.getKey();
            Vertex v = entry.getValue();
            System.out.print(ID);
            System.out.print(":");
            for(Integer i: v.getNeighbors()){
                System.out.printf("{%d,%d} ", i, v.getWeight(i));
            }
            System.out.println();
        }
    }
}

/**
 * Message is a class representing the message delivered
 * in the process of any algorithm in the graph.
 */
class Message {

}

/**
 * Vertex is a class representing the entity of a
 * vertex in the graph.
 */
class Vertex {
    private Integer ID; // the identity of the vertex
    private Map<Integer, Vertex> neighbors; // a map for storing neighbors of vertex
    private Map<Integer, Message> receiver; // a map for storing received messages for each neighbor
    private Map<Integer, Integer> weights; // a map for storing edge weights for each neighbor


    public Vertex(Integer ID){
        this.ID = ID;
        neighbors = new HashMap<>();
        receiver = new HashMap<>();
        weights = new HashMap<>();
    }

    public Integer getID(){
        return ID;
    }

    public void setID(Integer ID){
        this.ID = ID;
    }

    public boolean addNeighbor(Vertex v, Integer w){
        if(neighbors.putIfAbsent(v.getID(),v)!= null){
            return false;
        }
        weights.putIfAbsent(v.getID(),w);
        return true;
    }

    public boolean removeNeghbor(Integer ID){
        if(neighbors.remove(ID) != null){
            weights.remove(ID);
            return true;
        }
        return false;
    }

    public boolean removeNeghbor(Vertex v){
        if(neighbors.remove(v.getID()) != null){
            weights.remove(ID);
            return true;
        }
        return false;
    }

    public Set<Integer> getNeighbors(){
        return neighbors.keySet();
    }

    public Integer getWeight(Integer ID){
        return weights.get(ID);
    }

    public boolean setWeight(Integer ID, Integer w){
        if(weights.putIfAbsent(ID,w) != null){
            return false;
        }
        return true;
    }
}

interface LocalComputation {

    boolean sendMsg(Integer ID, Message msg);

}

interface GraphAlgorithm {

}
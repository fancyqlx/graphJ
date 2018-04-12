package fancyqlx;

import java.lang.*;
import java.io.*;
import java.util.Scanner;

/**
 * ConstructGraph is class for reading graph data from datafiles.
 */
public class ConstructGraph {
    private String filePath;
    private Graph g;

    public ConstructGraph(String path, Graph g){
        this.filePath = path;
        this.g = g;
    }

    public boolean construct(){
        File f = new File(filePath);
        Scanner in = null;
        try{
            in = new Scanner(f);
        }catch (IOException e){
            e.printStackTrace();
        }
        if(in != null){
            weightGraph(in);
            in.close();
            return true;
        }
        return false;
    }

    private void addItem(Integer i, Integer j, Integer w){
        Vertex u = g.getVertex(i);
        Vertex v = g.getVertex(j);
        if(u == null){
            u = new Vertex(i);
            g.addVertex(i,u);
            g.addN();
        }
        if(v == null){
            v = new Vertex(j);
            g.addVertex(j,v);
            g.addN();
        }
        if(u.addNeighbor(v,w) && v.addNeighbor(u,w)) g.addM();
        if(w>g.getW()){
            g.updateW(w);
        }
    }

    private void weightGraph(Scanner in){
        while(in.hasNextInt()){
            Integer i = in.nextInt();
            Integer j = in.nextInt();
            Integer w = in.nextInt();
            addItem(i,j,w);
        }
    }

    private void unweightGraph(Scanner in){
        while(in.hasNextLine()){
            Integer i = in.nextInt();
            Integer j = in.nextInt();
            Integer w = 1;
            addItem(i,j,w);
        }
    }
}

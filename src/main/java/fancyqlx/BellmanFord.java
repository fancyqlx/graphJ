package fancyqlx;


public class BellmanFord {

    private Graph g;

    public class BellmanFordMessage extends Message{
        int ID;
        int hop;
        public BellmanFordMessage(int ID, int hop){
            this.ID = ID;
            this.hop = hop;
        }
    }


    public BellmanFord(Graph g){
        this.g = g;
    }
}

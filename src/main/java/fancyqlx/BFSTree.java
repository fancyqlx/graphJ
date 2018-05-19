package fancyqlx;

import java.util.*;

class TreeNode{
    Vertex vertex;
    TreeNode parent;
    ArrayList<TreeNode> children;
    public TreeNode(Vertex vertex){
        this.vertex = vertex;
        parent = null;
        children = new ArrayList<>();
    }
}


public class BFSTree {
    private TreeNode root;
    private Graph g;
    private Set<Integer> treeNodeSet = new HashSet<>();

    public BFSTree(Graph g, Vertex vertex){
        this.g = g;
        this.root = new TreeNode(vertex);
        treeNodeSet.add(root.vertex.getID());
        constructTree();
    }

    private void constructTree(){
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            TreeNode node = queue.poll();
            for(Integer id: node.vertex.getNeighbors()){
                if(!treeNodeSet.contains(id)){
                    Vertex vertex = g.getVertex(id);
                    TreeNode cnode = new TreeNode(vertex);
                    cnode.parent = node;
                    node.children.add(cnode);
                    treeNodeSet.add(id);
                    queue.add(cnode);
                }
            }
        }
    }

    public TreeNode getRoot(){
        return root;
    }

    public void printBFSTree(){
        Queue<TreeNode> queue = new LinkedList<>();
        queue.add(root);
        while(!queue.isEmpty()){
            TreeNode node = queue.poll();
            System.out.print(node.vertex.getID()+": ");
            for(TreeNode cnode: node.children){
                queue.add(cnode);
                System.out.print(cnode.vertex.getID()+",");
            }
            System.out.println();
        }
    }
}

import random
import networkx as nx


def generateRandomGraph(n,p,w):
    filepath = "graphData/random-"+str(n)
    with open(filepath,"w+") as f:
        g = nx.fast_gnp_random_graph(n,p)
        m = g.number_of_edges()
        print "Generating graph with %d nodes %d edges" % (n,m)
        # write data into files
        for (u,v) in g.edges:
            lines = str(u) + ' ' + str(v) + ' ' + str(w) + '\n'
            f.writelines(lines)

if __name__ == "__main__":

    n = 1000
    p = 0.002
    w = 1
    for i in range(0,20):
        generateRandomGraph(n,p,w)
        n = n + 500
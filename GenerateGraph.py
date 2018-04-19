import random
import networkx as nx

def generateGraph(n,m,w):
    filepath = "graphData/graph-"+str(n)+"-"+str(m)+"-"+str(w)
    with open(filepath,"a+") as f:
        g = nx.gnm_random_graph(n,m)
        # assign weights
        for (u,v) in g.edges():
            g[u][v]['weight'] = random.randint(1,w)
        # write data into files
        for (u,v,w) in g.edges(data='weight'):
            lines = str(u) + ' ' + str(v) + ' ' + str(w) + '\n'
            f.writelines(lines)

if __name__ == "__main__":

    n = 100
    w = n
    for i in range(0,20):
        m = int(1.2*n)
        generateGraph(n,m,w)
        n = n + 100
        w = n

    # n = 100
    # m = int(1.2*n)
    # for i in range(0,9):
    #     w = w + 10
    #     generateGraph(n,m,w)
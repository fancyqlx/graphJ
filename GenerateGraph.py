import random
import networkx as nx
import sys

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
    ''' if len(sys.argv) != 3:
        print "input two arguments [n,w]"
        exit()
    n = int(sys.argv[1])
    w = int(sys.argv[2]) '''
    n = 50
    w = 10
    for i in range(0,10):
        m = int(1.2*n)
        generateGraph(n,m,w)
        n = n + 50

    n = 50
    m = int(1.2*n)
    for i in range(0,9):
        w = w + 10
        generateGraph(n,m,w)
        
import os
import matplotlib.pyplot as plt

def average(f):
    n = 0 # the number of vertices
    m = 0 # the number of edges
    r = 0 # the number of rounds
    lines = f.readlines()
    num = 0
    for line in lines:
        a,b,c = map(int,line.split())
        n += a
        m += b
        r += c
        num += 1
    if num == 0:
        return n,m,r
    else:
        return n/num,m/num,r/num

def travelFiles(algName):
    dirPath = "results/"
    xn = [] # x-axis of n
    xw = [] # x-axis of w
    ynr = [] # y-axis of r when x-axis is n
    ywr = [] # y-axis of r when x-axis is w
    files = os.listdir(dirPath)
    N = 1000
    # read files that n is changed
    for i in xrange(0,20):
        filename = algName+"-"+str(N)
        for fn in files:
            if(not os.path.isdir(fn)) and fn == filename:
                with open(dirPath+"/"+filename,"r") as f:
                    n,m,r = average(f)
                    xn.append(n)
                    ynr.append(r)
        N += 500
    return xn,xw,ynr,ywr

# generate pictures
def drawFic(x,y,labelx,labely,filename):
    plt.plot(x,y,'k-')
    plt.xlabel(labelx)
    plt.ylabel(labely)
    plt.subplots_adjust(left=0.15,right=0.95)
    plt.savefig("resultPics/"+filename+".png", format='png')
    #plt.show()
    plt.close()

if __name__ == "__main__":
    Betweenness = "Betweenness"

    b_xn,b_xw,b_ynr,b_ywr = travelFiles(Betweenness)
    # four pictures for girth


    # two pictures for comparision
    drawFic(b_xn,b_ynr,"n","round complexity","bc_n_r")
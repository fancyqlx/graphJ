import matplotlib.pyplot as plt
import os

def averageGirth(f):
    n = 0 # the number of vertices
    m = 0 # the number of edges
    g = 0 # girth value
    p = 0 # the number of phases
    r = 0 # the number of rounds
    lines = f.readlines()
    num = 0
    for line in lines:
        a,b,c,d,e = map(int,line.split())
        n += a
        m += b
        g += c
        p += d
        r += e
        num += 1
    if num == 0:
        return n,m,g,p,r
    else:
        return n/num,m/num,g/num,p/num,r/num

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



def travelGirthFiles():
    dirPath = "results/"
    xn = [] # x-axis of n
    xw = [] # x-axis of w
    ynp = [] # y-axis of p when x-axis is n
    ynr = [] # y-axis of r when x-axis is n
    ywp = [] # y-axis of p when x-axis is w
    ywr = [] # y-axis of r when x-axis is w
    files = os.listdir(dirPath)
    N = 50
    M = int(1.2*N)
    W = 10
    # read files that n is changed
    for i in xrange(0,10):
        filename = "Girth-"+str(N)+"-"+str(M)+"-"+str(W)
        for fn in files:
            if(not os.path.isdir(fn)) and fn == filename:
                with open(dirPath+"/"+filename,"r") as f:
                    n,m,g,p,r = averageGirth(f)
                    xn.append(n)
                    ynp.append(p)
                    ynr.append(r)
        N += 50
        M = int(1.2*N)

    # read files that w is changed
    N = 50
    M = int(1.2*N)
    for i in xrange(0,10):
        filename = "Girth-"+str(N)+"-"+str(M)+"-"+str(W)
        for fn in files:
            if(not os.path.isdir(fn)) and fn == filename:
                with open(dirPath+"/"+filename,"r") as f:
                    n,m,g,p,r = averageGirth(f)
                    xw.append(W)
                    ywp.append(p)
                    ywr.append(r)
        W += 10
    return xn,xw,ynp,ynr,ywp,ywr


def travelFiles(algName):
    dirPath = "results/"
    xn = [] # x-axis of n
    xw = [] # x-axis of w
    ynr = [] # y-axis of r when x-axis is n
    ywr = [] # y-axis of r when x-axis is w
    files = os.listdir(dirPath)
    N = 50
    M = int(1.2*N)
    W = 10
    # read files that n is changed
    for i in xrange(0,10):
        filename = algName+"-"+str(N)+"-"+str(M)+"-"+str(W)
        for fn in files:
            if(not os.path.isdir(fn)) and fn == filename:
                with open(dirPath+"/"+filename,"r") as f:
                    n,m,r = average(f)
                    xn.append(n)
                    ynr.append(r)
        N += 50
        M = int(1.2*N)

    # read files that w is changed
    N = 50
    M = int(1.2*N)
    for i in xrange(0,10):
        filename = algName+"-"+str(N)+"-"+str(M)+"-"+str(W)
        for fn in files:
            if(not os.path.isdir(fn)) and fn == filename:
                with open(dirPath+"/"+filename,"r") as f:
                    n,m,r = average(f)
                    xw.append(W)
                    ywr.append(r)
        W += 10
    return xn,xw,ynr,ywr

# generate pictures
def drawFicGirth(x,y,labelx,labely,filename):
    plt.plot(x,y,'k-')
    plt.xlabel(labelx)
    plt.ylabel(labely)
    plt.subplots_adjust(left=0.15,right=0.95)
    plt.savefig("resultPics/"+filename+".png", format='png')
    plt.show()

# generate pictures
def drawFic(gx,gy,bx,by,tx,ty,labelx,labely,filename):
    plt.plot(gx,gy,"k-")
    plt.plot(bx,by,"k--")
    plt.plot(tx,ty,"k:")
    plt.xlabel(labelx)
    plt.ylabel(labely)
    plt.subplots_adjust(left=0.15,right=0.95)
    plt.savefig("resultPics/"+filename+".png", format='png')
    plt.show()

if __name__ == "__main__":
    BellmanFord = "BellmanFord"
    TrivalBFS = "TrivalBFS"
    g_xn,g_xw,g_ynp,g_ynr,g_ywp,g_ywr = travelGirthFiles()
    b_xn,b_xw,b_ynr,b_ywr = travelFiles(BellmanFord)
    t_xn,t_xw,t_ynr,t_ywr = travelFiles(TrivalBFS)
    # four pictures for girth
    drawFicGirth(g_xn,g_ynr,"n","round compleixty","girth_n_r")
    drawFicGirth(g_xn,g_ynp,"n","the number of phases","girth_n_p")
    drawFicGirth(g_xw,g_ywr,"w","round compleixty","girth_w_r")
    drawFicGirth(g_xw,g_ywp,"w","the number of phases","girth_w_p")
    # two pictures for comparision
    drawFic(g_xn,g_ynr,b_xn,b_ynr,t_xn,t_ynr,"n","round complexity","cmp_n_r")
    drawFic(g_xw,g_ywr,b_xw,b_ywr,t_xw,t_ywr,"w","round complexity","cmp_n_r")

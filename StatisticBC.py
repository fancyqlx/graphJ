import os
import matplotlib.pyplot as plt

def average(f):
    n = 0 # the number of vertices
    m = 0 # the number of edges
    r = 0 # the number of rounds
    msg = 0 # the number of messages
    lines = f.readlines()
    num = 0
    for line in lines:
        a, b, c, d= map(int,line.split())
        n += a
        m += b
        r += c
        msg += d
        num += 1
    if num == 0:
        return n,m,r,msg
    else:
        return n/num,m/num,r/num,msg/num

def travelFiles(algName):
    dirPath = "results/"
    xn = [] # x-axis of n
    ynr = [] # y-axis of r when x-axis is n
    ynmsg = [] # y-axis of msg when x-axis is n
    ynn = []
    ynmn = []
    files = os.listdir(dirPath)
    N = 100
    # read files that n is changed
    for i in xrange(0,10):
        filename = algName+"-"+"random"+"-"+str(N)
        for fn in files:
            if(not os.path.isdir(fn)) and fn == filename:
                with open(dirPath+"/"+filename,"r") as f:
                    n,m,r,msg = average(f)
                    xn.append(n)
                    ynr.append(r)
                    ynmsg.append(msg)
                    ynn.append(n)
                    ynmn.append(m*n)
        N += 100
    return xn,ynr,ynmsg,ynn,ynmn

# generate pictures
def drawFic1(x,y,labelx,labely,filename):
    plt.plot(x,y,'k-')
    plt.xlabel(labelx)
    plt.ylabel(labely)
    plt.subplots_adjust(left=0.15,right=0.95)
    plt.savefig("resultPics/"+filename+".pdf", format='pdf')
    #plt.show()
    plt.close()

# generate pictures
def drawFic2(ax,ay,bx,by,labelx,labely,filename):
    l1, = plt.plot(ax,ay,"k-")
    l2, = plt.plot(bx,by,"k--")
    plt.xlabel(labelx)
    plt.ylabel(labely)
    plt.legend(handles=[l1,l2], labels=['Experiment','Theory'], loc='best')
    plt.subplots_adjust(left=0.15,right=0.95)
    plt.savefig("resultPics/"+filename+".pdf", format='pdf')
    #plt.show()
    plt.close()

if __name__ == "__main__":
    Betweenness = "Betweenness"

    b_xn,b_ynr,b_ynmsg,b_ynn,b_ynmn = travelFiles(Betweenness)
    # four pictures for girth


    # two pictures for comparision
    drawFic1(b_xn,b_ynr,"n","round complexity","bc_n_r")
    drawFic1(b_xn,b_ynmsg,"n","message complexity","bc_n_msg")

    #drawFic2(b_xn,b_ynr,b_xn,b_ynn,"n","round complexity","bc_n_r")
    #drawFic2(b_xn,b_ynmsg,b_xn,b_ynmn,"n","message complexity","bc_n_msg")
import os

def generateRandomGraph():
    os.system("rm -rf graphData/random*")
    os.system("python GenerateBCGraph.py")

def compileJavaSource():
    os.system("javac -d target/classes/ -cp target/classes/ -s src/main/java/fancyqlx/ src/main/java/fancyqlx/*.java")


def runBetweenness():
    os.system("java -Xmx16384m -cp target/classes fancyqlx/Betweenness")

def runStatistic():
    os.system("python StatisticBC.py")


def runBC():
    for i in xrange(0,5):
        print "run %d-th experiment" % i
        generateRandomGraph()
        runBetweenness()

    runStatistic()

if __name__ == "__main__":
    compileJavaSource()
    runBC()


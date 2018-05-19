import os

def generateGraph():
    os.system("rm -r graphData/*")
    os.system("python GenerateGraph.py")

def generateRandomGraph():
    os.system("rm -r graphData/random*")
    os.system("python GenerateGraph.py")

def compileJavaSource():
    os.system("javac -d target/classes/ -cp target/classes/ -s src/main/java/fancyqlx/ src/main/java/fancyqlx/*.java")

def runGirth():
    os.system("java -cp target/classes fancyqlx/Girth")

def runTrivalBFS():
    os.system("java -cp target/classes fancyqlx/TrivalBFS")

def runBellmanFord():
    os.system("java -cp target/classes fancyqlx/BellmanFord")

def runBetweenness():
    os.system("java -cp target/classes fancyqlx/Betweenness")

def runStatistic():
    os.system("python Statistic.py")

def run():
    for i in xrange(0,100):
        print "run %d-th experiment" % i
        generateGraph()
        runGirth()
        runTrivalBFS()
        runBellmanFord()

    runStatistic()

def runBC():
    for i in xrange(0,10):
        print "run %d-th experiment" % i
        generateRandomGraph()
        runBetweenness()

    runStatistic()

if __name__ == "__main__":
    compileJavaSource()
    run()


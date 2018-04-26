import os

def generateGraph():
    os.system("rm -r graphData/*")
    os.system("python GenerateGraph.py")

def compileJavaSource():
    os.system("javac -d target/classes/ -cp target/classes/ -s src/main/java/fancyqlx/ src/main/java/fancyqlx/*.java")

def runGirth():
    os.system("java -cp target/classes fancyqlx/Girth")

def runTrivalBFS():
    os.system("java -cp target/classes fancyqlx/TrivalBFS")

def runBellmanFord():
    os.system("java -cp target/classes fancyqlx/BellmanFord")

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

if __name__ == "__main__":
    compileJavaSource()
    run()


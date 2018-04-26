# graphJ
graphJ是一个模拟分布式图算法的程序，它模拟了多个分布式算法并使用随机图作为数据集进行了若干算法的实验。
程序包含若干经典的分布式算法并可以继续设计其它的图算法，本文档提供了详细的设计和使用说明。

## 程序结构
graphJ的程序结构如下所示：
    
    graphJ--
          |-graphData
          |-resultPics
          |-results
          |-src--
               |-main--
                     |-java--
                            |-fancyqlx--
                                       |-BellmanFord.java
                                       |-BoundedBFS.java
                                       |-ConstructGraph.java
                                       |-Diameter.java
                                       |-Girth.java
                                       |-Graph.java
                                       |-TrivalBFS.java
               |-test
          |-target--
          |-GenerateGraph.py
          |-Statistic.py
          |-runExp.py

上图表示的是graphJ中重要的程序，其它文件为IntelliJ IDEA的环境文件。其中，graphData存储的为每一次
运行实验时的图数据，这些数据都是由GenerateGraph.py文件生成。resultPics保存的为最终实验运行的结果
的对比图片，这些图片由Statistic.py生成。results存储的是若干次实验生成的数据，这些数据由各个图
算法写入。src保存的是程序的源代码，其中，Bellman-Ford.java为分布式的Bellman-Ford算法；
BoundedBFS.java为分布式的有限距离的宽度优先搜索；ConstructGraph.java用于构造一个图的实例，
实例的类为Graph.java文件。Girth.java为使用BoundedBFS算法计算有权图最小环的算法。
TrivalBFS.java为使用分布式n源BFS计算无权图最小环的算法。Diameter为分布式计算图直径的算法。
runExp.py是运行整个实验的脚本，它会编译所有的java文件并运行GenegateGraph.py和Statistic.py来
生成随机图和统计实验数据。

## 设计说明
#### Graph.java
Graph.java包含三个java类，分别是Graph，Message和Vertex。

1. Message：
Message是一个抽象类，用于表示一个空的消息体，不同的算法需要为这个消息体添加不同的内容。

2. Vertex：
Vertex表示一个节点，节点中存储自己的ID，邻居节点，邻接边的权值，到其它点的跳数和距离以及节点在每个
BFS树上的前驱。Vertex中包含的主要数据结构和方法列举如下。
    - ID：Integer类型，表示节点ID
    - neighbors: Map<Integer, Vertex>类型，键表示节点ID，值表示Vertex实例，表示邻居节点
    - receiver： Map<Integer, Message>类型，键表示节点ID，值表示Message实例，存储从每个邻居接收到的消息
    - weights： Map<Integer, Integer>类型，键表示节点ID，值表示权值，表示边的权值
    - hops： Map<Integer, Integer>类型，键表示节点ID，值表示跳数，表示到节点ID的跳数
    - distance： Map<Integer, Integer>类型，键表示节点ID，值表示距离，表示到节点ID的距离
    - pre： Map<Integer, Integer>类型，键表示BFS源点节点ID，值表示前驱
    - send(Integer ID, Message msg): 方法返回boolean，参数ID表示目的节点的ID，msg表示发送的消息体
    - broadcast(Message msg): 方法表示将msg发送给所有的邻居，返回boolean，参数msg表示发送的消息
    
3. Graph:
Graph表示一个图的实例，它存储图中所有的节点，Graph中重要的数据结构和方法列举如下。
    - vertices: Map<Integer,Vertex>类型，键表示节点ID，值表示对应的Vertex实例，用于存储所有节点的信息
    - n：int型，表示图中节点的个数
    - m：int型，表示图中边的个数
    - B：int型，表示图的带宽
    - W：int型，表示图中边的最大权值

#### ConstructGraph.java
ConstructGraph.java用于读取图数据文件，将数据转化为一个Graph实例，该文件只包含一个ConstructGraph类，
主要的数据结构和方法列举如下。

- filepath：String类型，表示读取图数据文件的路径
- g：Graph类型，表示Graph实例
- construct()：返回boolean类型，true表示图构造成功，false表示失败
- addItem(Integer i, Integer j, Integer w)：返回void，参数i，j表示一条边的两个顶点，w表示边的权值，方法用于将一条边加入图中


#### BellmanFord.java
BellmanFord.java是分布式的Bellman-Ford算法的代码，它只包含BellmanFord一个类，主要的数据结构和方法列举如下。

- g：Graph类型，表示一个图的实例
- outMsg： Map<Integer, Queue<BellmanFordMessage>>类型，键为节点ID，值为一个存储有消息体的队列，表示每一个点的待发送消息队列
- round: int类型，用于计算分布式算法运行的轮数
- run()：返回void，启动分布式算法的方法，该方法模拟Bellman-Ford算法
- writeResult(String filepath): 返回void，参数filepath表示文件路径，该方法将算法的运行结果写入文件中


#### Diameter.java
Diameter.java是模拟分布式计算Diameter的算法，它使用的是分布式Bellman-Ford算法，文件只包含Diameter类，主要的数据结构和方法如下。

- g：Graph类型，表示一个图的实例
- D：int类型，表示图的直径
- outMsg： Map<Integer, Queue<DiameterMessage>>类型，键为节点ID，值为一个存储有消息体的队列，表示每一个点的待发送消息队列
- round: int类型，用于计算分布式算法运行的轮数
- run()：返回void，启动分布式算法的方法，该方法模拟Bellman-Ford算法

#### TrivalBFS.java
TrivalBFS.java模拟分布式n源BFS算法，文件包含类TrivalBFS类，主要数据结构和方法如下。

- g：Graph类型，表示一个图的实例
- maxDist：int类型，表示图中两点之间可能的最大距离
- gmin：int类型，表示最小环的值
- outMsg： Map<Integer, PriorityQueue<BFSMessage>>类型，键为节点ID，值为一个存储有消息体的队列，表示每一个点的待发送消息队列
- round: int类型，用于计算分布式算法运行的轮数
- run()：返回void，启动分布式算法的方法，该方法模拟分布式n源BFS算法，最终计算出最小环的值
- writeResult(String filepath): 返回void，参数filepath表示文件路径，该方法将算法的运行结果写入文件中

#### BoundedBFS.java
BounedBFS.java是模拟分布式有限距离的宽度优先搜索算法，BoundedBFS算法中的有限距离需要设置，即BoundedBFS算法会根据这个
设置的有限距离运行，距离的设置在BoundedBFS的构造函数中，一般由其它外部调用程序设置。

- g：Graph类型，表示一个图的实例
- maxDist：int类型，表示图中两点之间可能的最大距离
- t：int类型，表示BoundedBFS中的有限距离
- condition1：boolean型，表示条件1是否发生，true表示发生
- condition2：boolean型，表示条件2是否发生，true表示发生
- gmin：int类型，表示最小环的值
- outMsg： Map<Integer, PriorityQueue<BFSMessage>>类型，键为节点ID，值为一个存储有消息体的队列，表示每一个点的待发送消息队列
- round: int类型，用于计算分布式算法运行的轮数
- run()：返回void，启动分布式算法的方法，该方法模拟分布式n源BoundedBFS算法
- computeGmin()： 返回void，该计算出每个点所在的环的最小值
- writeResult(String filepath): 返回void，参数filepath表示文件路径，该方法将算法的运行结果写入文件中 

#### Girth.java
Girth.java中包含唯一的类Girth，Girth用二分法逐渐逼近最小环的值，二分法的每一步都运行BoundedBFS，主要的数据结构和方法如下。

- g：Graph类型，表示一个图的实例
- alpha：int类型，表示最小环的下界
- beta：int类型，表示最小环的上界
- phase：int类型，表示算法运行的阶段数
- t：int类型，表示BoundedBFS中的有限距离
- girth：int类型，表示最小环的值
- round: int类型，用于计算分布式算法运行的轮数
- run()：返回void，启动分布式算法的方法，该方法通过二分来计算alpha和beta，每次二分运行分布式n源BoundedBFS算法
- writeResult(String filepath): 返回void，参数filepath表示文件路径，该方法将算法的运行结果写入文件中 

## 运行说明
graphJ的目的是模拟多个分布式算法，然后比较它们的性能，因此需要在不同的图数据集上多次实验，在runExp.py中，
我们设置只运行了BellmanFord，TrivalBFS和Girth三个类，可以在这里自定义需要运行的算法。

GenerateGraph是生成随机图的脚本，它使用networkx生成随机图，然后将图数据写入文本文件中供其它算法读取，直接运行GenerateGraph.py
就可以替换graphData中的数据，在GenerateGraph.py中，可以自定义图的各个参数。

Statistic.py是统计实验结果和画图的代码，通过自定义这个文件里的代码可以选择生成想要的结果和对比图。
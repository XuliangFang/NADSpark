## Progress Records

**Project Link:** https://github.com/XuliangFang/NADSpark

#### March 29th, 2017

* 实现K-means算法的简单案例，包括数据加载，训练模型，模型优化（选取合适的K值）等过程；
* 考虑木马检测的特征向量选取，以及针对具体特征的数据预处理（Filter、Normalize...etc.），然后编写model class与mainrun.scala；
* 测试数据采用了UCI的数据。UCI 是一个关于机器学习测试数据的下载中心站点，里面包含了适用于做聚类，分群，回归等各种机器学习问题的数据集。Link: http://archive.ics.uci.edu/ml/datasets/Wholesale+customers

> Note: build.sbt 注意library dependency问题（编译时遇到mllib is not a member of org.apache.spark）
>
> ```shell
> $spark-submit --class "KMeansClustering" ./target/scala-2.11/kmeans-project_2.11-1.0.jar /user/kmeans/kmeans_Training.txt /user/kmeans/kmeans_Test.txt 8 30 3
> ```

##### 思路整理

> **目前难点**：木马检测的特征选择是否有效不用再考虑，最大的难处在于训练数据的获得（数据预处理），其次是编写代码完成模型训练、优化与检测。关于代码，模型训练基本没问题（基于Spark MLlib），模型优化暂时决定使用K Array的方法，逐一计算cost，但也不能一味选择cost小对应的K值，有待思考。检测即通过labeled cluster进行predict，得到空间向量距离最近的cluster的label。
>
> **工作安排**：先假设已经可以得到训练数据，完成训练、优化和检测的代码；然后考虑训练数据的获得。读完相关paper，看看有没有启发再说。
>
> **今日主要工作**：编写、编译k-means测试代码，完成k-means算法案例测试。完成木马特征选择。

#### March 30th, 2017

* 木马检测所需的结构化数据
* 实验结果分析
* Slides for Group Meeting

今日工作主要是完善木马异常流量检测Kmeans算法的代码。木马检测所需要的结构化数据来源问题未完全解决。组会slides未完成。

明天主要工作：测试木马检测聚类算法的代码。实验结果分析报告。组会。

今日收获：

```scala
val a = List(1,2,3,4)
val b = (5 /: a)(_+_)
def /:[B](z:B)(op: (B,A) => B):B
return op(...op(op(z, x_1), x_2), ..., x_n)
```

#### March 31th, 2017

##### NCC-Group Meeting Tasks

* 谢老师提出：可先针对几个服务器的IP进行检测，与广泛检测可以结合起来考虑工作；考虑攻击者的IP可能动态变化的问题。
* 薛老师提出：与宇飞、人杰一起解决数据源的问题；与罗盛杰一起work on分布式规则引擎的开发。
* 下次组会准备Slides介绍异常检测的背景、现有方法与我采用的算法。介绍算法测试结果。
* 本次组会后撰写文档：数据源需求文档；检测算法原理文档。

##### 今日工作安排

* 撰写算法原理与数据源需求文档，详细介绍K-Means聚类与Spark MLlib中相关的算法支持。
* 编译、打包nadsTrojan.scala，测试程序功能。
* 思考数据源问题的解决方法。

#### April 3rd, 2017

##### Today's work arrangements

> sorted by priority as follows

* Docs & Slides 
* Test nadsTrojan.scala
* thinking of final year project Mid-term Check...
* Think about Thesis of Final Year Project...

Not Good Today...read some code of Shengjie's work. Distributed rules engine.

Link: https://github.com/ShengjieLuo/DBNS

#### April 4th, 2017

##### Today's work arrangements

> MUST FINISH THESE WORK TODAY

* 完成技术文档(.docx)，介绍当前常用木马检测方法以及机器学习聚类算法，然后描述聚类算法检测木马的原理。
* 完善代码，思考数据源问题解决方法，与学长讨论方法。
* 初步Slides for group meeting
* 毕业设计文档准备

##### 今日工作完成情况

> Note: 21:35 self-evaluation

* task1: FINISHED
* task2: Worked but not finished.
* task3: NO
* task4: NO

#### April 5th, 2017

##### Today's work arrangements

* review code
* 思考木马的下一步
* 数据源！！！
* 抓紧测试代码，考虑系统的实现

##### 今日工作完成

* code review完成，代码测试完成，但功能测试未完成（数据源问题未解决）。
* 与金人杰讨论了数据源的问题，需要宇飞帮忙。
* 木马的下一步：测试完成后，需要考虑从Flow层面检测网络异常。

#### April 6th, 2017

##### Work Arrangements

> Sorted by priority

* 组会Slides：介绍木马检测的方案及数据源要求；基于罗盛杰架构的规则、服务；介绍下周工作（除了木马检测的数据和测试外，其他异常的检测对策以及可视化）
* 数据源问题解决方案（Depicted in Work.1）
* ShengjieLuo的代码，架构以及Drools
* Thinking of 毕设系统（哪些异常的检测？）展示方案

##### 收获

#### April 7th, 2017

##### GroupMeeting

> Records

* 整理数据，尽快做出demo
* 考虑其他异常的检测与毕设系统展示
* 中期检查的要求
* 论文——

#### April 10th, 2017    Monday

##### Project overview

目前，“基于大数据的网络异常分析”项目工作完成度不高，后期主要工作还有平台迁移，环境安装，代码测试与文档撰写。

##### Work on this week 

> sort by priority

**No.1 木马检测的数据源问题，代码的优化测试、完善**

**No.2 研究Hogzilla的flow实现 & Review code of DBNS** 

**No.3 考虑DDoS攻击、DNS攻击、BotNet、Combination with Distributed Rules Engine**

**No.4 论文应于5月初开始撰写**

##### Today's Work Arrangements

* 阅读Hogzilla源码，DBNS源码
* 进一步学习Spark平台技术（Hive/SparkSQL/Scala/Others）
* 编写代码（DDoS攻击、DNS攻击）

##### Self-evaluation

完成了：

* 数据源初步得到，总共一个小时内的每个IP的记录，总共1955949条记录；

* 对数据源进行清洗，过滤掉只有上传或只有下行流量的记录（后续再仔细考虑是否妥当），剩下一共107247条记录；

* 测试代码，得到一个当前数据源下的Kmeans训练模型。异常记录如下：

  | IP地址        | DNS次数 | 上下行包数比例 | 上下行字节比例 | SYN包占比 | PSH包占比 |
  | ----------- | ----- | ------- | ------- | ------ | ------ |
  | 106.75.76.5 | 0     | 0.2647  | 0.7799  | 0      | 1.0    |

#### April 11th, 2017 Tuesday

##### Today's work

* processData.py, 得到统计数据

* 测试得到聚类结果

  ```shell
  #when k=2
  How many clusters? Clusters Number: 2
  Center Point of Cluster 0:
  [8.713611696473528,3.6201762799550132,8.047831717687123,0.2509824209559019,1.0,0.7422143969675418]
  Center Point of Cluster 1:
  [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
  #when k=4
  How many clusters? Clusters Number: 4
  Center Point of Cluster 0:
  [3.0460462513987316,3.6202481006706315,8.048165156842792,0.2509675008644095,1.0,0.7422330293087946]
  Center Point of Cluster 1:
  [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
  Center Point of Cluster 2:
  [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
  Center Point of Cluster 3:
  [88157.8,2.553380659281,2.267114526237,0.5030642221058,1.0,0.454463656992]
  ```

* 合理展示聚类结果

  ```shell
  The threshold is 536.235
  Selecting anomalous cluster...
  Cluster:2 Number:1
  Cluster:1 Number:1
  Cluster:3 Number:5
  How many clusters? Clusters Number: 4
  Center Point of Cluster 0:
  [3.0460462513987316,3.6202481006706315,8.048165156842792,0.2509675008644095,1.0,0.7422330293087946]
  Center Point of Cluster 1:
  [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
  Center Point of Cluster 2:
  [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
  Center Point of Cluster 3:
  [88157.8,2.553380659281,2.267114526237,0.5030642221058,1.0,0.454463656992]
  Got anomalous clusters...---------->>>>>>>>
  =======================================
  The index of anomalous cluster is 2
  =======================================
  The index of anomalous cluster is 1
  =======================================
  The index of anomalous cluster is 3
  ```

  ```
  The threshold is 536.235
  Selecting anomalous cluster...
  Cluster:2 Number:2
  Cluster:5 Number:1
  Cluster:4 Number:3
  Cluster:7 Number:10
  Cluster:1 Number:1
  Cluster:3 Number:2
  Cluster:6 Number:1
  How many clusters? Clusters Number: 8
  Center Point of Cluster 0:
  [2.6885858972087253,3.4186631900510625,5.915501870962927,0.2509974893184599,1.0,0.7423186994505182]
  Center Point of Cluster 1:
  [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
  Center Point of Cluster 2:
  [70524.0,0.5620078120075,0.509714296379,0.37705667276050003,1.0,0.6245875121319999]
  Center Point of Cluster 3:
  [0.0,2443.214285715,50647.82709995,1.038062074965E-4,1.0,7.813799059054999E-4]
  Center Point of Cluster 4:
  [99913.66666666666,3.880962557463333,3.4387146794756664,0.5870692550026666,1.0,0.34104775356533334]
  Center Point of Cluster 5:
  [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
  Center Point of Cluster 6:
  [38369.0,0.954158302147,0.0888580369825,0.0397530693172,1.0,0.335750914202]
  Center Point of Cluster 7:
  [0.0,1677.5025706581,12748.796924414,7.044867047482931E-4,1.0,0.012556342043293001]
  Got anomalous clusters...---------->>>>>>>>
  =======================================
  The index of anomalous cluster is 2
  =======================================
  The index of anomalous cluster is 5
  =======================================
  The index of anomalous cluster is 4
  =======================================
  The index of anomalous cluster is 7
  =======================================
  The index of anomalous cluster is 1
  =======================================
  The index of anomalous cluster is 3
  =======================================
  The index of anomalous cluster is 6
  ```

* hogzilla结构，思考系统架构

##### 收获

* scala中map与foreach操作选择
* scala Vectors.dense()
* 测试代码

##### 问题

* 直观展示IP地址或者输出数据向量到文件中再做进一步处理
* 系统架构尚待思考

#### April 12th, 2017

> Wednesday

##### TODO Arrangements

* 完善测试代码的展示部分，准备测试报告for group meeting slides
* 参考hogzilla的架构，构建系统架构
* 考虑从flow的角度，选取参量维度，实现其他情况的检测
* 阅读ShengjieLuo/DBNS的源码，进一步熟悉分布式规则引擎的架构

##### Work

```scala
//scala grammar
//To print a 1-D and 2-D Array
scala> val a = Array.range(1,10)  
a: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)  
scala> println(a.mkString(" "))  
1 2 3 4 5 6 7 8 9  
scala> val array = Array.fill(2,2)(0)
array: Array[Array[Int]] = Array(Array(0, 0), Array(0, 0))
scala> println(array.deep.mkString("\n"))
Array(0, 0)
Array(0, 0)

//得到带标签的向量
def create_label_point(line:String):LabeledPoint = {
    //字符串去空格，以逗号分隔转为数组
    val linearr = line.trim().split(" ")
    val linedoublearr = linearr.map(x=>x.toDouble)
    //定长数组转可变数组
    val linearrbuff = linedoublearr.toBuffer
    //移除label元素（将linedoublearr的第一个元素作为标签）
    linearrbuff.remove(0)
    //将剩下的元素转为向量
    val vectorarr = linearrbuff.toArray
    val vector = Vectors.dense(vectorarr)
    //返回标签向量
    LabeledPoint(linedoublearr(0),vector)
  }
```

Problem：java.lang.NumberFormatException: multiple points 非线程安全？

Solution：println("[debug] " + line(0) + "Number of dot: " + dotNumber(line(0)))

```scala
        val linebuff = line.toBuffer
        linebuff.remove(0)
        val linearr = linebuff.toArray.map(_.toDouble) --错写成--> val linearr = line.toArray.map(...)
        val vecData = Vectors.dense(linearr)
        (line(0), vecData)
```
##### 评价

* 完成了完整的展示功能，下一步将检测结果写入数据库，展示到前端
* Read Guidance to SparkSQL, DataFrames and DataSets
* 明天可以玩玩Hive与SparkSQL，以及构建一个小型的Flask应用*。

#### April 13th, 2017 Thursday 

##### TODO Arrangements

昨天完成了完整信息的展示，今天要做的主要是考虑将检测结果写入数据库，更重要的是，除了木马的检测，其他网络异常的检测方法（DDoS，DNS相关攻击，参见http://ids-hogzilla.org/）。确定好做哪些攻击检测以后，检测方法-->>分布式规则引擎的Service服务（参见DBNS https://github.com/ShengjieLuo/DBNS/tree/cluster）。实现攻击检测的后台算法与前端展示。 

> Sorted by priority

* HBase, Hive, SparkSQL, DataFrames.
* 检测哪些攻击？方法？参量维度？
* 进一步测试，形成组会Slides（回顾方法、参量，测试数据，测试参数与结果，下一步工作）
* 若有时间，了解DBNS的最新进展情况

>*[LEARN] 在实际编程中，我们经常需要把RDD中的元素打印输出到屏幕上（标准输出stdout），一般会采用语句rdd.foreach(println)或者rdd.map(println)。当采用本地模式（local）在单机上执行时，这些语句会打印出一个RDD中的所有元素。但是，当采用集群模式执行时，在worker节点上执行打印语句是输出到worker节点的stdout中，而不是输出到任务控制节点Driver Program中，因此，任务控制节点Driver Program中的stdout是不会显示打印语句的这些输出内容的。为了能够把所有worker节点上的打印输出信息也显示到Driver Program中，可以使用collect()方法，比如，rdd.collect().foreach(println)，但是，由于collect()方法会把各个worker节点上的所有RDD元素都抓取到Driver Program中，因此，这可能会导致内存溢出。因此，当你只需要打印RDD的部分元素时，可以采用语句rdd.take(100).foreach(println)。*


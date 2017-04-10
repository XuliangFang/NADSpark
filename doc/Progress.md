## Progress Records

Project Link: https://github.com/XuliangFang/NADSpark

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

* 对数据源进行清洗，过滤只有上传或只有下行流量的记录（后续再仔细考虑是否妥当）；

* 测试代码，得到一个当前数据源下的Kmeans训练模型。异常记录如下：

  | IP地址           | DNS次数 | 上下行流量比例 | 上下行包数比例 | SYN包占比 | PSH包占比 | 小数据包占比 |
  | -------------- | ----- | ------- | ------- | ------ | ------ | ------ |
  | 202.120.37.116 |       |         |         |        |        |        |
  |                |       |         |         |        |        |        |
  |                |       |         |         |        |        |        |
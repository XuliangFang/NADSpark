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
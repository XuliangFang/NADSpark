## Progress Records

#### March 29th, 2017

* 实现K-means算法的简单案例，包括数据加载，训练模型，模型优化（选取合适的K值）等过程；
* 考虑木马检测的特征向量选取，以及针对具体特征的数据预处理（Filter、Normalize...etc.），然后编写model class与mainrun.scala；
* 测试数据采用了UCI的数据。UCI 是一个关于机器学习测试数据的下载中心站点，里面包含了适用于做聚类，分群，回归等各种机器学习问题的数据集。Link: http://archive.ics.uci.edu/ml/datasets/Wholesale+customers

> Note: build.sbt 注意library dependency问题（编译时遇到mllib is not a member of org.apache.spark）
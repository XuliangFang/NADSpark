### Progress Records

#### April 14th, 2017 Friday

##### Group Meeting

* 数据预处理有问题：未归一化处理，导致权重不均
* 数据清洗过程是否欠妥：195万条数据--->>>10万7千条数据
* 展示需要进一步改进：哪些维度导致其被分类在异常类？（需要思考）
* K值的优化（寻找cost拐点）
* Kibana查询优化后聚类结果，并将结果及时发布到群里

##### Today's work

完成以上问题的处理。

##### 数据归一化--Normalization

数据标准化（归一化）处理是数据挖掘的一项基础工作，不同评价指标往往具有不同的量纲和量纲单位，这样的情况会影响到数据分析的结果，为了消除指标之间的量纲影响，需要进行数据标准化处理，以解决数据指标之间的可比性。原始数据经过数据标准化处理后，各指标处于同一数量级，适合进行综合对比评价。以下是两种常用的归一化方法：

* Min-Max Normalization

也称为离差标准化，是对原始数据的线性变换，使结果值映射到[0 - 1]之间。转换函数如下：

![min-max_normalization](../pics/min-max_normalization.gif)

* Z-score Normalization

基于原始数据的均值（mean）和标准差（Standard deviation）进行数据的标准化。经过处理的数据符合标准正态分布，即均值为0，标准差为1，转化函数为：

![z-score_normalization](../pics/z-score_normalization.gif)




# NADSpark

NADSpark is a network anomaly  detection system using MLlib on spark.

We detect anomalous network traffic in real time network environment.

### Feature Selection

木马的通信行为分为三个阶段：建立连接、通信、保持连接。建立连接阶段会发出DNS请求以获得客户端（即控制端）的IP。通信阶段，服务端通信表现出上行流量比下行流量多的特征；此外，SYN、PSH标志位为1的数据包占总数据包的比例较正常情况大。最后，在保持连接阶段，木马服务端会不断发出心跳包（数据包小，常常1字节；时间间隔异于正常）以表征自身的存活性。

- DNS Behavior
- upload/download traffic
- SYN/PSH packages proportion (among all packages)
- Packages interval time
- Size of packages

### Machine Learning Algorithm

#### K-Means聚类算法

K值的选取：通过实验确定（10-100, 考察准确率与误报率）

特征向量：<f1, f2, f3, f4, f5>

### Implementation in Scala

A example of using MLlib (K-Means)

```scala
import org.apache.spark.{SparkConf, SparkContext}
// $example on$
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
// $example off$
object KMeansExample {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("KMeansExample")
    val sc = new SparkContext(conf)
    // $example on$
    // Load and parse the data
    val data = sc.textFile("data/mllib/kmeans_data.txt")
    val parsedData = data.map(s => Vectors.dense(s.split(' ').map(_.toDouble))).cache()
    // Cluster the data into two classes using KMeans
    val numClusters = 2
    val numIterations = 20
    val clusters = KMeans.train(parsedData, numClusters, numIterations)

    // Evaluate clustering by computing Within Set Sum of Squared Errors
    val WSSSE = clusters.computeCost(parsedData)
    println("Within Set Sum of Squared Errors = " + WSSSE)

    // Save and load model
    clusters.save(sc, "target/org/apache/spark/KMeansExample/KMeansModel")
    val sameModel = KMeansModel.load(sc, "target/org/apache/spark/KMeansExample/KMeansModel")
    // $example off$

    sc.stop()
  }
}
```

通过机器学习聚类的方法，可以将异常数据与正常数据分离开来。
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

### Implementation on Spark Using Scala

待续...
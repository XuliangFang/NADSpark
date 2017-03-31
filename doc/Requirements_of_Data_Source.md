### Trojan Detecion Algorithm

#### Principal

木马的通信行为分为三个阶段：建立连接、通信、保持连接。建立连接阶段会发出DNS请求以获得客户端（即控制端）的IP。通信阶段，服务端通信表现出上行流量比下行流量多的特征；此外，SYN、PSH标志位为1的数据包占总数据包的比例较正常情况大。最后，在保持连接阶段，木马服务端会不断发出心跳包（数据包小，常常1字节；时间间隔异于正常）以表征自身的存活性。

#### K-Means Clustering

k-means 算法接受输入量 k ；然后将n个数据对象划分为 k个聚类以便使得所获得的聚类满足：同一聚类中的对象相似度较高；而不同聚类中的对象相似度较小。聚类相似度是利用各聚类中对象的均值所获得一个“中心对象”（引力中心）来进行计算的。

![450px-Iris_Flowers_Clustering_kMeans.svg](C:\Users\Jerry\Desktop\450px-Iris_Flowers_Clustering_kMeans.svg.png)

#### Feature Selection

* DNS行为
* 上下行流量比例
* SYN、PSH包数量占比
* 报文间隔时间
* 报文大小

算法参数均是针对特定IP的数据。
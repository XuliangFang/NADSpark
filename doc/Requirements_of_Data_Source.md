### Trojan Detecion Algorithm

#### Principal

木马的通信行为分为三个阶段：建立连接、通信、保持连接。建立连接阶段会不断发出DNS请求以获得客户端（即控制端）的IP直到连接建立完成。通信阶段，服务端通信表现出上行流量比下行流量多的特征；此外，SYN、PSH标志位为1的数据包占总数据包的比例较正常情况大。最后，在保持连接阶段，木马服务端会不断发出心跳包（数据包小，常常1字节；时间间隔异于正常）以表征自身的存活性。

#### K-Means Clustering

k-means 算法接受输入量 k ；然后将n个数据对象划分为 k个聚类以便使得所获得的聚类满足：同一聚类中的对象相似度较高；而不同聚类中的对象相似度较小。聚类相似度是利用各聚类中对象的均值所获得一个“中心对象”（引力中心）来进行计算的。对象即一个特征向量，由若干预先定义好的特征组成。

#### Feature Selection

* **TCP会话时间长度 (Session Duration)**:  时间窗口内，第一个IP-pair session会话长度（从SYN到FIN）
* **DNS行为** **(DNS behavior)**: 时间窗口内，请求DNS次数
* **上下行包比例 (number of packages)**: 时间窗口内，IP->out包个数除以IP<-out包个数
* **上下行字节比例(Bytes of packages)**: 时间窗口内，IP->out字节数除以IP<-out字节数
* **SYN包数量占比 (Proportion of SYN packages)**: IP->out SYN packages占总发送包数量比例
* **PSH包数量占比 (Proportion of PSH packages)**: IP<-out PSH packages占总接收包数量比例
* **报文平均间隔时间 (Mean Interval Time)**: 时间窗口内，第一个IP-pair Session的报文平均间隔时间
* **size <= 62字节的报文数量占比 (Proportion of small packages)**: 时间窗口内小数据包占总数据包个数比例

针对每个IP（或需要重点关注的IP地址），统计时间窗口大小为15分钟内的数据，获得以上结构化流量特征。
### Requirements on Data Source for Various Anomaly Detection

#### Basic Info. of Anomaly

* 异常时间
* 异常相关的主机IP
* 异常说明

#### DDoS

> 功能：检测可能遭受DDoS攻击的Server. 检测依据：短时间内连接数超过阈值

数据要求（一个TCP流，IP1->IP2）：

* IP1，Port1，IP2，Port2，上行流量(size)， 上行流量(package number)，下行流量（size），下行流量(package number)，总共通信的包数量(上下行package number加起来)，开始时间，结束时间

#### Port Scan


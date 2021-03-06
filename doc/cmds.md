#### HDFS

```shell
#Upload file
$ hdfs dfs -put <localfile> <hdfs path>
u928@master:~/fl/anomalyDetection$ hdfs dfs -put ./tcp_session_5min.txt /tcpSession/tcp_session_5min.txt
#list file
$ hdfs dfs -ls [hdfspath]
#download file
$ hdfs dfs -get /tcpheader/KmeansModel ./getModel
```

#### Spark

```shell
#newest cmds for test
u928@master:~/fl/kmeansTest$ spark-submit --driver-class-path ./lib/mysql-connector-java-5.1.41-bin.jar --class "trojanDetection" ./trojan-kmeans_2.11-1.0.jar /tcpheader/normalizeDataSet_v2.txt /tcpheader/trainSet_v2.txt 15 100 5 0.01
# K-Means algorithm test
$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/trainSet.txt /tcpheader/tcp_test.txt 3 30 3
```

#### Scala

```shell
$ sbt package
```

#### Kafka

```shell
# host: u928@10.255.0.10 hadoop928
u928@slaver1:~$ kafka-console-consumer.sh --bootstrap-server master:9092  --topic tcp-stream > ./fl/tcpsession.txt

```

#### MySQL

```shell
$ service mysql start
### password:hadoop928
$ mysql -u root -p
####建表记录
For server side help, type 'help contents'
###创建数据库
mysql> create database anomaly;
Query OK, 1 row affected (0.11 sec)
###建木马检测结果表
mysql> use anomaly
Database changed
mysql> create table trojan(
     id int(4) primary key not null auto_increment,
     ipaddr varchar(16),
     intervalTime varchar(16),
     dnsTimes varchar(16),
     upDownNumber varchar(16),
     upDownSize varchar(16),
     synProportion varchar(16),
     pshProportion varchar(16),
     smallProportion varchar(16),
     troTime varchar(20),
     );
Query OK, 0 rows affected (0.72 sec)

mysql> 

###建立异常事件表
create table events(
	id bigint primary key not null auto_increment,
	ipaddr varchar(16),
	category varchar(16),
	shortContent varchar(128),
	status boolean default false,
	event_time varchar(20)
	);
	insert into events (ipaddr,category,shortContent,status,event_time) values ("89.248.167.131","木马","疑似木马，请查看详情",false,"2017-04-28 17:24:42");
  ####修改表格编码方式，以插入中文
  alter table ttname CONVERT TO CHARACTER SET utf8;
```

#### 写入数据库

```scala
import java.util.Properties
import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.types.{StringType, IntegerType, StructField, StructType}
val sqlContext = new SQLContext(sc)
//下面我们设置两条数据表示两个学生信息
val studentRDD = sc.parallelize(Array("3 Rongcheng M 26","4 Guanhua M 27")).map(_.split(" "))
//下面要设置模式信息
val schema = StructType(List(StructField("id", IntegerType, true),StructField("name", StringType, true),StructField("gender", StringType, true),StructField("age", IntegerType, true)))
//下面创建Row对象，每个Row对象都是rowRDD中的一行
val rowRDD = studentRDD.map(p => Row(p(0).toInt, p(1).trim, p(2).trim, p(3).toInt))
//建立起Row对象和模式之间的对应关系，也就是把数据和模式对应起来
val studentDataFrame = sqlContext.createDataFrame(rowRDD, schema)
//下面创建一个prop变量用来保存JDBC连接参数
val prop = new Properties()
prop.put("user", "root") //表示用户名是root
prop.put("password", "hadoop") //表示密码是hadoop
prop.put("driver","com.mysql.jdbc.Driver") //表示驱动程序是com.mysql.jdbc.Driver
//下面就可以连接数据库，采用append模式，表示追加记录到数据库spark的student表中
studentDataFrame.write.mode("append").jdbc("jdbc:mysql://localhost:3306/spark", "spark.student", prop)
```

```
create table onePercentTrojan(
     id int(4) primary key not null auto_increment,
     intervalTime varchar(16),
     dnsTimes varchar(16),
     upDownNumber varchar(16),
     upDownSize varchar(16),
     synProportion varchar(16),
     pshProportion varchar(16),
     smallProportion varchar(16),
     troTime varchar(20)
     );
```


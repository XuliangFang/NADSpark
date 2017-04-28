//package com.jerryfang.ml

import java.util.Properties
import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.mutable.HashSet
import scala.collection.mutable.Set
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SQLContext, Row}
import org.apache.spark.sql.types._
import java.util.Date 
import java.text.SimpleDateFormat

object trojanDetection {
    
    def main(args: Array[String]){

        //check parameters
        if(args.length < 6){
            println("Usage: nadsTrojan trainingDataFilePath testDataFilePath numClusters numIterations runTimes maxAnomalousClusterProportion")
            sys.exit(1)
        }

        val conf = new SparkConf().setAppName("K-means Clustering Detect Trojan")
        val sc = new SparkContext(conf)
        //for writing in database
        val sqlContext = new SQLContext(sc)
        val schema = StructType(List(StructField("ipaddr", StringType, true), 
                                    StructField("intervalTime", StringType, true), 
                                    StructField("dnsTimes", StringType, true),
                                    StructField("upDownNumber", StringType, true),
                                    StructField("upDownSize", StringType, true),
                                    StructField("synProportion", StringType, true),
                                    StructField("pshProportion", StringType, true),
                                    StructField("smallProportion", StringType, true),
                                    StructField("troTime", StringType, true)
                                    ))

        //get raw training data
        val rawTrainingData = sc.textFile(args(0))
        val parsedTrainingData = rawTrainingData.filter(!isColumnNameLine(_)).map(line => {
            Vectors.dense(line.split(" ").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
        }).cache()

        //get IP + IP's Data file
        val rawFullData = sc.textFile(args(1))
        val parsedFullData = rawFullData.map(line => {
            line.split(" ").map(_.trim).filter(!"".equals(_))
        })
        //parsedFullData.foreach(line => println("[debug] " + line.mkString(" ")))
        val fullData = parsedFullData.map(line => {
            //val ipAddr = line(0)
            val linebuff = line.toBuffer
            linebuff.remove(0)
            val linearr = linebuff.toArray.map(_.toDouble)
            val vecData = Vectors.dense(linearr)
            (line(0), vecData)
        })

        //Normalize data 
        //This part is added on Firday 14th April 2017
        println("[debug] Calculating some variables to normalize data...")
        val arrayParsedTrainingData = rawTrainingData.filter(!isColumnNameLine(_)).map(line => {
            line.split(" ").map(_.trim).filter(!"".equals(_)).map(_.toDouble)
        })
        val n = arrayParsedTrainingData.count()
        val numCols = arrayParsedTrainingData.first.length
        val sums = arrayParsedTrainingData.reduce((a,b) => a.zip(b).map(t => t._1 + t._2))
        val sumSquares = arrayParsedTrainingData.fold(
            new Array[Double](numCols)
        )(
            (a,b) => a.zip(b).map(t => t._1 + t._2*t._2)
        )
              
        val stdevs = sumSquares.zip(sums).map{
            case(sumSq,sum) => math.sqrt(n*sumSq - sum*sum)/n
        }
            
        val means = sums.map(_/n)
              
        def normalize(vector: Vector):Vector = {
            val normArray = (vector.toArray, means, stdevs).zipped.map(
                (value,mean,std) =>
                  if(std<=0) (value-mean) else (value-mean)/std)
            return Vectors.dense(normArray)
        }   

        println("[debug] Normalizing data...")
        val labelAndData = fullData.map({ 
            case (ip, vecData) => 
            (ip, normalize(vecData))
        })

        //for display raw data in anomalous clusters 
        val ipRawNorData = fullData.map({
            case (ip, vecData) =>
            (ip, vecData, normalize(vecData))
        })

        println("[debug] Morlization Finished!")

        //get training data for K-Means algorithm
        val trainData = labelAndData.values.cache()

        //cluster the data into classes using Kmeans 
        val numClusters = args(2).toInt
        val numIterations = args(3).toInt
        val runTimes = args(4).toInt
        var clusterIndex:Int = 0
        val clusters:KMeansModel = KMeans.train(trainData, numClusters, numIterations, runTimes)
        //save kmeans model to HDFS: /fangliang/KmeansModel
        clusters.save(sc, "/fangliang/KmeansModel")
        
        //when need to load
        //val sameModel = KMeansModel.load(sc, "/tcpheader/KMeansModel")
        //added on 18th April
        //val clustersSummaryCol = clusters.summary.featuresCol
        //println("[debug] ------------------ $clustersSummaryCol" + "------------------>>>>>>>>>>> "+ clustersSummaryCol)
/*
        //labeling and raise event(normal or abnormal)
        val clusterLabel = parsedTestData.map(data => {
            val clusterPredict = clusters.predict(data)
            (clusterPredict, data)
        })
*/
        //new labeling 2017-4-12 P.M.
        val newClusterLabel = labelAndData.map({
            case (ipAddr, data) =>
            val clusterPrediction = clusters.predict(data)
            (clusterPrediction, ipAddr, data)
        })

        //for display
        val displayClusterLabel = ipRawNorData.map({
            case (ipaddr, rawdata, normaldata) =>
            val clusterPredict = clusters.predict(normaldata)
            (clusterPredict, ipaddr, rawdata)
        })

        //statistic
        /*val clusterLabelCount = clusterLabel.map({
            case (clusterPredict, data) =>
            val mapData: Map[(Int, String), (Double, Int)] = new HashMap[(Int, String), (Double, Int)]
            mapData.put((clusterPredict, "Trojan"), (1.toDouble, 1))
            mapData
        }).reduceByKey((a, b) => {
            b._2
        })*/
        val clusterLabelCount = newClusterLabel.map({
            case (clusterPredict, ipAddr, data) =>
            val mapData: Map[Int, Int] = new HashMap[Int, Int]
            mapData.put(clusterPredict, 1)
            mapData
        }).reduce((a, b) => {
            b.keys.map{
                case key:(Int)=>
                if(a.contains(key)){
                    a.put(key, a.apply(key)+b.apply(key))
                }
                else{
                    a.put(key, b.apply(key))
                }
            }
            a
        })
        //compute proportion of every cluster
        val maxAnomalousClusterProportion = args(5).toDouble
        val minDirtyProportion = 0.001
        //test data size
        val testDataSize = labelAndData.count()
        val threshold = maxAnomalousClusterProportion * testDataSize
        val minProportion = minDirtyProportion * testDataSize
        println("[debug] The threshold is " + threshold)

        clusterLabelCount.map({ t =>
            println("[debug] Cluster:"+ t._1 + " Number:" + t._2)
        })
        val anomalyIpNumberPair = clusterLabelCount.filter({
                kv => (kv._2.toDouble < threshold && kv._2.toDouble > minProportion)
        })
        var anomalyIpCount = 0
        if(!anomalyIpNumberPair.isEmpty){
            anomalyIpCount = anomalyIpNumberPair.reduce((x, y) => (x._1, x._2+y._2))._2
        }

        println("[debug] Selecting anomalous cluster...")

        val anomalousArray = clusterLabelCount.filter({
            kv => (kv._2.toDouble < threshold  && kv._2.toDouble > minProportion)  
        }).map({
            case(cluster, number) =>
            cluster
        })

        //show results

        println("[debug] How many clusters? Clusters Number: "+ clusters.clusterCenters.length)
        clusters.clusterCenters.foreach(x => {
            println("[debug] Center Point of Cluster " + clusterIndex + ":")
            println("[debug] "+ x)
            clusterIndex += 1
        })

        val prop = new Properties()
        prop.put("user", "root")
        prop.put("password", "hadoop928")
        prop.put("driver", "com.mysql.jdbc.Driver")
        val dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

        //Save anomalous cluster index array into database
        if(!anomalousArray.isEmpty){
            val anomalousRDD = sc.parallelize(anomalousArray.toList)
            val clusterTime:String = dateFormat.format(new Date())
            val indexRowRDD = anomalousRDD.map({
                t => 
                Row(t.toString.trim, clusterTime.trim)
            })
            val indexSchema = StructType(List(StructField("clusterIndex", StringType, true), 
                                    StructField("clusterTime", StringType, true)
                                    ))
            val indexDataFrame = sqlContext.createDataFrame(indexRowRDD, indexSchema)
            indexDataFrame.write.mode("append").jdbc("jdbc:mysql://localhost:3306/anomaly", "anomaly.kmeansIndex", prop)
            println("[debug] Saved anomalous cluster index into database(anomaly.kmeansIndex)...")
        } 

        //Save dataset means and stdevs for ShengjieLuo
        val statisticTime:String = dateFormat.format(new Date())
        val meansRDD = sc.parallelize(means) //convert to RDD
        val meansRowRDD = meansRDD.map(t => Row(t.toString.trim, statisticTime.trim))
        val stdevsRDD = sc.parallelize(stdevs)
        val stdRowRDD = stdevsRDD.map(t => Row(t.toString.trim, statisticTime.trim))
        val meansSchema = StructType(List(StructField("means", StringType, true),
                                    StructField("meansTime", StringType, true)
                                    ))
        val stdSchema = StructType(List(StructField("stdevs", StringType, true),
                                    StructField("stdevsTime", StringType, true)
                                    ))
        val meansDataFrame = sqlContext.createDataFrame(meansRowRDD, meansSchema)
        val stdDataFrame = sqlContext.createDataFrame(stdRowRDD, stdSchema)
        meansDataFrame.write.mode("append").jdbc("jdbc:mysql://localhost:3306/anomaly", "anomaly.means", prop)
        stdDataFrame.write.mode("append").jdbc("jdbc:mysql://localhost:3306/anomaly", "anomaly.stdevs", prop)
        println("[debug] Saved dataset means and stdevs into database(anomaly.means and anomaly.stdevs)...")

        if(anomalousArray.isEmpty){
            println("[debug] There is no anomalous cluster...")
        }
        else{
            println("[debug] Got anomalous clusters...---------->>>>>>>>")
            println("[debug] =======================================")
            var anomalyClusterCount:Int = 0
            anomalousArray.map({
                clt => 
                println("[debug] The index of anomalous cluster is " + clt.toString)
                println("[debug] The points in this cluster are as follows: ")
                val trojanRDD = displayClusterLabel
                .filter({
                    case(predict, ip, data) => predict==clt & data.apply(1) != 0.0
                })
                .map({
                    case(predict, ip, data) =>
                    println("[debug] In cluster "+ clt + ": " + ip)
                    println("[debug] ******* Details ******* (data -->> means)")
                    var tmpCount:Int = 1
                    val featuresArray = Array("IntervalTime", "DNS Query Times", "Up/Down Packages Number", "Up/Down Packages Size", "SYN Packages proportion", "PSH Packages proportion", "Small Packages proportion")
                    means.foreach({t => 
                        println("[debug] " + featuresArray(tmpCount-1) + "\t " + data.apply(tmpCount-1) + "\t-->>\t"+t)
                        tmpCount += 1
                    })
                    println("[debug] ")
                    (ip, data)
                })
                //木马检测结果写入数据库
                val trojanTime:String = dateFormat.format(new Date())
                //val trojanTime = dateFormat.parse(trojanTimeStr)
                val rowRDD = trojanRDD.map({ case(ip, p) => Row(ip.trim, p(0).toString.trim, p(1).toString.trim, p(2).toString.trim, p(3).toString.trim,
                                                    p(4).toString.trim, p(5).toString.trim, p(6).toString.trim, trojanTime.trim)
                                        })
                val trojanDataFrame = sqlContext.createDataFrame(rowRDD, schema)
                trojanDataFrame.write.mode("append").jdbc("jdbc:mysql://localhost:3306/anomaly", "anomaly.trojan", prop)
                println("[debug] Saved into database(anomaly.trojan)...")

                /*displayClusterLabel.foreach({
                    case (pred, ip, data) => {
                        if(pred == clt)
                        {
                            //剔除DNS行为数为0的记录
                            if(data.apply(1) != 0.0)
                            {
                                println("[debug] In cluster "+ clt + ": " + ip)
                                println("[debug] ******* Details ******* (data -->> means)")
                                var tmpCount:Int = 1
                                val featuresArray = Array("IntervalTime", "DNS Query Times", "Up/Down Packages Number", "Up/Down Packages Size", "SYN Packages proportion", "PSH Packages proportion", "Small Packages proportion")
                                means.foreach({t => 
                                    println("[debug] " + featuresArray(tmpCount-1) + "\t " + data.apply(tmpCount-1) + "\t-->>\t"+t)
                                    tmpCount += 1
                                })
                                println("[debug] ")
                                //meaningfulAnomalyPointsCount += 1
                            }
                            //println("[debug] Mean data: "+ means)
                            //println("[debug] In cluster "+ clt + ": " + ip + " " +data)
                        }
                    }
                })*/
                println("[debug] =======================================")
                anomalyClusterCount += 1
            })
            //println("[debug] [Info] " + anomalyDataSet.size)
            println("[debug] [Info] There are " + anomalyIpCount + " anomalous IPs among " + anomalyClusterCount + " clusters.")
            //println("[debug] [Info] There are " + meaningfulAnomalyPointsCount + " anomalous IPs among " + anomalyClusterCount + " clusters.")
        }
        
        
        println("[debug] Begin to optimize K-means model by choosing a K value that takes minimum cost.")

        val ks:Array[Int] = Array(8,9,10,11,12,13,14,15,16,17,18,19,20)
        ks.foreach(clusterNum => {
            val model:KMeansModel = KMeans.train(trainData, clusterNum, 50, 1)
            val cost = model.computeCost(trainData)
            println("[debug] Sum of squared distances of points to their nearest center when K=" + clusterNum + " --->>> " + cost)
        })
        
    }

    private def isColumnNameLine(line:String):Boolean = {
        if(line != null && line.contains("dns")) true
        else false
    }

}
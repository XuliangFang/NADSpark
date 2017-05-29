package com.jerryfang.ml

import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.mutable.HashSet
import org.apache.spark.{SparkContext, SparkConf}
//import org.apache.spark.mllib.clustering.ClusteringSummary
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.mllib.linalg.Vector
import org.apache.spark.rdd.RDD
//import java.io.PrintWriter

object nadsTrojan {
    
    def main(args: Array[String]){

        //check parameters
        if(args.length < 5){
            println("Usage: nadsTrojan trainingDataFilePath testDataFilePath numClusters numIterations runTimes")
            sys.exit(1)
        }

        val conf = new SparkConf().setAppName("K-means Clustering Detect Trojan")
        val sc = new SparkContext(conf)

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
            //println("[debug] " + line(0) + "Number of dot: " + dotNumber(line(0)))
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
        //added on 18th April
        //val clustersSummaryCol = clusters.summary.featuresCol
        //println("[debug] ------------------ $clustersSummaryCol" + "------------------>>>>>>>>>>> "+ clustersSummaryCol)

        /*
        println("How many clusters? Clusters Number: "+ clusters.clusterCenters.length)
        clusters.clusterCenters.foreach(x => {
            println("Center Point of Cluster " + clusterIndex + ":")
            println(x)
            clusterIndex += 1
        })*/


        //predict which cluster each point in test data set belongs to
        /***********
        val rawTestData = sc.textFile(args(0))
        val parsedTestData = rawTestData.map(line => {
            Vectors.dense(line.split(" ").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
        })


        //Maybe need modification here
        val testDataSize = parsedTestData.count()
        //val testDataSize = rawTestData.collect().count()

        parsedTestData.collect().foreach(testDataLine => {
            val predictionClusterIndex: Int = clusters.predict(testDataLine)
            println("The point "+ testDataLine.toString + "belongs to cluser " + predictionClusterIndex)
        })

        println("--------------------------------------->>>>>>>>>>>>>")
        println("--------------------------------------->>>>>>>>>>>>>")
        println("--------------------------------------->>>>>>>>>>>>>")
        println("Simple Prediction finished.")
        ***********/

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
        val maxAnomalousClusterProportion = 0.01
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
        }).map(_._1)

        //show results

        println("[debug] How many clusters? Clusters Number: "+ clusters.clusterCenters.length)
        clusters.clusterCenters.foreach(x => {
            println("[debug] Center Point of Cluster " + clusterIndex + ":")
            println("[debug] "+ x)
            clusterIndex += 1
        })

        if(anomalousArray.isEmpty){
            println("[debug] There is no anomalous cluster...")
        }
        else{
            println("[debug] Got anomalous clusters...---------->>>>>>>>")
            println("[debug] =======================================")
            var anomalyClusterCount = 0
            anomalousArray.map({
                clt => 
                println("[debug] The index of anomalous cluster is " + clt.toString)
                println("[debug] The points in this cluster are as follows: ")
                displayClusterLabel.foreach({
                    case (pred, ip, data) => {
                        if(pred == clt)
                        {
                            //剔除DNS行为数为0的记录
                            if(data.apply(1) != 0.0)
                            {
                                println("[debug] In cluster "+ clt + ": " + ip)
                                println("[debug] ******* Details ******* (data -->> means)")
                                var tmpCount:Int = 1
                                val featuresArray = Array("IntervalTime", "DNS", "Up/Down Packages Number", "Up/Down Packages Size", "SYN proportion", "PSH proportion", "Small Packages proportion")
                                means.foreach({t => 
                                    println("[debug] " + featuresArray(tmpCount-1) + "\t " + data.apply(tmpCount-1) + "\t-->>\t"+t)
                                    tmpCount += 1
                                })
                                println("[debug] ")
                            }
                            //println("[debug] Mean data: "+ means)
                            //println("[debug] In cluster "+ clt + ": " + ip + " " +data)
                        }
                    }
                })
                println("[debug] =======================================")
                anomalyClusterCount += 1
            })
            println("[debug] [Info] There are " + anomalyIpCount + " anomalous IPs among " + anomalyClusterCount + " clusters.")
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

    private def dotNumber(line:String):Int = {
        var cnt:Int = 0
        line.foreach( ch => 
            if(ch == '.')
                cnt += 1
        )
        cnt
    }
}
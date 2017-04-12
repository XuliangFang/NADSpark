package com.jerryfang.ml

import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.mutable.HashSet
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
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

        //cluster the data into classes using Kmeans 
        val numClusters = args(2).toInt
        val numIterations = args(3).toInt
        val runTimes = args(4).toInt
        var clusterIndex:Int = 0
        val clusters:KMeansModel = KMeans.train(parsedTrainingData, numClusters, numIterations, runTimes)

        /*
        println("How many clusters? Clusters Number: "+ clusters.clusterCenters.length)
        clusters.clusterCenters.foreach(x => {
            println("Center Point of Cluster " + clusterIndex + ":")
            println(x)
            clusterIndex += 1
        })*/


        //predict which cluster each point in test data set belongs to
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

/*
        //labeling and raise event(normal or abnormal)
        val clusterLabel = parsedTestData.map(data => {
            val clusterPredict = clusters.predict(data)
            (clusterPredict, data)
        })
*/
        //new labeling 2017-4-12 P.M.
        val newClusterLabel = fullData.map({
            case (ipAddr, data) =>
            val clusterPrediction = clusters.predict(data)
            (clusterPrediction, ipAddr, data)
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
        val maxAnomalousClusterProportion = 0.005
        val minDirtyProportion = 0.001
        val threshold = maxAnomalousClusterProportion * testDataSize
        println("[debug] The threshold is " + threshold)

        clusterLabelCount.map({ t =>
            println("[debug] Cluster:"+ t._1 + " Number:" + t._2)
        })
        val anomalyIpCount = clusterLabelCount.filter({
                kv => kv._2.toDouble < threshold
            }).reduce((x, y) => (x._1, x._2+y._2))

        println("[debug] Selecting anomalous cluster...")

        val anomalousArray = clusterLabelCount.filter({
            kv => kv._2.toDouble < threshold    
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
                newClusterLabel.foreach({
                    case (pred, ip, data) => {
                        if(pred == clt)
                        {
                            println("[debug] In cluster "+ clt + ": " + ip + " " +data)
                        }
                    }
                })
                println("[debug] =======================================")
                anomalyClusterCount += 1
            })
            println("[debug] [Info] There are " + anomalyIpCount._2 + " anomalous IPs among " + anomalyClusterCount + " clusters.")
        }
        
        /*
        println("Begin to optimize K-means model by choosing a K value that takes minimum cost.")

        val ks:Array[Int] = Array(3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)
        ks.foreach(clusterNum => {
            val model:KMeansModel = KMeans.train(parsedTrainingData, clusterNum, 30, 1)
            val cost = model.computeCost(parsedTrainingData)
            println("Sum of squared distances of points to their nearest center when K=" + clusterNum + " --->>> " + cost)
        })
        */
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
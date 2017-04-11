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
        /*val parsedFullData = rawFullData.map(line => {
            Vectors.dense(line.split(" ").map(_.trim).filter(!"".equals(_))
        })
        parsedFullData.foreach(line => println(line._1))*/

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

        //labeling and raise event(normal or abnormal)
        val clusterLabel = parsedTestData.map(data => {
            val clusterPredict = clusters.predict(data)
            (clusterPredict, data)
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
        val clusterLabelCount = clusterLabel.map({
            case (clusterPredict, data) =>
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
        println("The threshold is " + threshold)

        clusterLabelCount.map({ t =>
            println("Cluster:"+ t._1 + " Number:" + t._2)
        })

        println("Selecting anomalous cluster...")

        val anomalousArray = clusterLabelCount.filter({
            kv => kv._2.toDouble < threshold    
        }).map(_._1)

        //show results

        println("How many clusters? Clusters Number: "+ clusters.clusterCenters.length)
        clusters.clusterCenters.foreach(x => {
            println("Center Point of Cluster " + clusterIndex + ":")
            println(x)
            clusterIndex += 1
        })

        if(anomalousArray.isEmpty){
            println("There is no anomalous cluster...")
        }
        else{

            println("Got anomalous clusters...---------->>>>>>>>")
            anomalousArray.map({
                clt => 
                println("The index of anomalous cluster is " + clt.toString)
                println("The points in this cluster are as follows: ")
                clusterLabel.foreach({
                    case (pred, data) => {
                        if(pred == clt)
                        {
                            println(data)
                        }
                    }
                })
                println("=======================================")
            })
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
}
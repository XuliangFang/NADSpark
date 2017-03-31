package com.jerryfang.ml

import scala.collection.mutable.HashMap
import scala.collection.mutable.Map
import scala.collection.mutable.HashSet
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.mllib.clustering.{KMeans, KMeansModel}
import org.apache.spark.mllib.linalg.Vectors
import org.apache.spark.rdd.RDD

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
            Vectors.dense(line.split("\t").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
        }).cache()

        //cluster the data into classes using Kmeans 
        val numClusters = args(2).toInt
        val numIterations = args(3).toInt
        val runTimes = args(4).toInt
        var clusterIndex:Int = 0
        val clusters:KMeansModel = Kmeans.train(parsedTrainingData, numClusters, numIterations, runTimes)

        println("How many clusters? Clusters Number: "+ clusters.clusterCenters.length)
        clusters.clusterCenters.foreach(x => {
            println("Center Point of Cluster " + clusterIndex + ":")
            println(x)
            clusterIndex += 1
        })

        //predict which cluster each point in test data set belongs to
        val rawTestData = sc.textFile(args(1))
        val parsedTestData = rawTestData.map(line => {
            Vectors.dense(line.split("\t").map(_.trim).filter(!"".equals(_)).map(_.toDouble))
        })
        //Maybe need modification here
        val testDataSize = parsedTestData.collect().count()
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
            val mapData: Map[Int, Int] = new Map[Int, Int]
            mapData.put(clusterPredict, 1))
            mapData
        }).reduce((a, b) => {
            b.keySet().toArray().map{
                case key:(Int)=>
                if(a.containsKey(key)){
                    a.put(key, a.get(key)+b.get(key))
                }
                else{
                    a.put(key, b.get(key))
                }
            }
            a
        })
        //compute proportion of every cluster
        val maxAnomalousClusterProportion = 0.05
        val minDirtyProportion = 0.001
        val threshold = maxAnomalousClusterProportion * testDataSize

        println("Selecting anomalous cluster...")

        val anomalousArray = clusterLabelCount.filter({
            kv => kv._2.toDouble < threshold    
        }).map(_._1)

        if(anomalousArray.isEmpty){
            println("There is no anomalous cluster...")
        }
        else{
            println("Got anomalous clusters...---------->>>>>>>>")
            anomalousArray.map({
                clt => 
                println("=======================================")
                println("The index of anomalous cluster is " + clt.toString)
            })
        }
        

        println("Begin to optimize K-means model by choosing a K value that takes minimum cost.")

        val ks:Array[Int] = (3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20)
        ks.foreach(clusterNum => {
            val model:KMeansModel = KMeans.train(parsedTrainingData, clusterNum, 30, 1)
            val cost = model.computeCost(parsedTrainingData)
            println("Sum of squared distances of points to their nearest center when K=" + clusterNum + " --->>> " + cost)
        })
    }

    private def isColumnNameLine(line:String):Boolean = {
        if(line != null && line.contains("dns")) true
        else false
    }
}
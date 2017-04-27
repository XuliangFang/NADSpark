package com.anomaly

//import org.apache.hadoop.hbase.client.Scan
//import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.{SparkContext, SparkConf}
import org.apache.spark.rdd.PairRDDFunctions
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

class nadsFlow(spark:SparkContext, filePath:String) {

    val file:String = filePath
    val sc:SparkContext = spark
    val rawData = sc.textFile(file)
    val parsedRDD = rawData.map(line => {
        line.split("\t").map(_.trim).filter(!"".equals(_))
    })
    //var nadsRDD

    def getRDD():RDD[(String,String,String,String,String,Long,Long,Long,Long,Long)] = {
        val nadsRDD = parsedRDD.map(t => {
            (t(0), t(1), t(2), t(3), t(4), t(5).toLong, t(6).toLong, t(7).toLong, t(8).toLong, t(9).toLong)
        })
        nadsRDD
    }
}
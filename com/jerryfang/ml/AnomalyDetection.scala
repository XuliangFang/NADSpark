package com.jerryfang.ml

import org.apache.spark._
import org.apache.spark.mllib.clustering.KMeans
import org.apache.spark.mllib.linalg._
import org.apache.spark.mllib.regression.LabeledPoint
import org.apache.spark.mllib.stat.{MultivariateStatisticalSummary, Statistics}
import org.apache.spark.rdd.RDD


class AnomalyDetection {

	def run(NadsRDD: RDD[], spark: SparkContext)
	{
		kmeans(NadsRDD)
	}

	def kmeans(NadsRDD: RDD[])
	{
		val features = Array("dnsBehavior",
												 "Out-In-Packages",
												 "SYN-PSH-Packages",
												 "packagesIntervalTime",
												 "packagesSize")

		println("Filtering NadsRDD...")
		val NadsRDD = NadsRDD.map{
			case(id, result) => {
				val map: Map[String, String] = new HashMap[String, String]
			}
		}

		println("Counting NadsRDD...")
		val rddTotalSize = NadsRDD.count()
		println("NadsRDD has"+ rddTotalSize +" rows!")
		if(rddTotalSize == 0)
			return

		println("Calculating some variables to normalize data...")

	}

}
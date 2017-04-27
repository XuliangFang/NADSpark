import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.anomaly.ddosDetection
import com.anomaly.nadsFlow
import com.anomaly.nadsEvent

object runAnomalyDetection {

	def main(args: Array[String]){
		//check parameters
		if(args.length<3 || args.length>3)
		{
			println("[debug] Usage: runAnomalyDetection filePath ddosMinConnectionsThreshold ddosMinPairsThreshold")
			return
		}

		val sparkConf = new SparkConf()
                          .setAppName("NADSpark")
                          //.set("spark.executor.memory", "1g")
                          //.set("spark.default.parallelism", "160") // 160
                          
	    val spark = new SparkContext(sparkConf)
	    val filePath = args(0)
	    val ddosMinConnectionsThreshold = args(1).toInt
    	val ddosMinPairsThreshold = args(2).toInt
	    val flow = new nadsFlow(spark, filePath)
	    val nadsRDD = flow.getRDD
	    ddosDetection.run(nadsRDD, spark, ddosMinConnectionsThreshold, ddosMinPairsThreshold)
	}
    
}
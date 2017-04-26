import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import com.anomaly.ddosDetection
import com.anomaly.nadsFlow
import com.anomaly.nadsEvent

object runAnomalyDetection {
    val sparkConf = new SparkConf()
                          .setAppName("NADSpark")
                          .set("spark.executor.memory", "1g")
                          .set("spark.default.parallelism", "160") // 160
                          
    val spark = new SparkContext(sparkConf)
}
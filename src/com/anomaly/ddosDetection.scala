package com.anomaly

import com.anomaly.nadsEvent
import com.anomaly.nadsFlow

import java.net.InetAddress
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import scala.collection.mutable.Map
import scala.math.floor
import scala.math.log
import org.apache.hadoop.hbase.client.Scan
import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.SparkContext
import org.apache.spark.rdd.PairRDDFunctions
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

object ddosDetection extends Any {

    val FlowListLimit = 1000 //maximum of flow number in a list
    val ddosMinConnectionThreshold = 80 //need to be optimized later
    val ddosMinPairsThreshold = 35 //need to be optimized later
    //consideration....
    val ddosExceptionAlienPorts:Set[String] = Set("80","443","587","465","993","995")

    println("[debug] ---------------------------")
    println("[debug] Detecting server under DDoS attack!")
    //get flow data source
    val flow = new nadsFlow()
    val nadsFlowSummary = flow.getFlowSummary()

    val ddosCollection: PairRDDFunctions[(String,String), (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Long,Long)],Long)] = 
    nadsFlowSummary
    .filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime)) 
                =>  //!isMyIP(alienIP, myNets) &
                    !ddosExceptionAlienPorts.contains(alienPort) //&
                    //direction: alienIP -->> myIP
    })
    .map({
      case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime)) =>
         val flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Long,Long)] = new HashSet()
         flowSet.add((myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,beginTime,endTime))
        ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,1L))
    })
  
    val ddosCollectionFinal=ddosCollection
    .reduceByKey({
    case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberFlowsA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberFlowsB)) =>
      (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberFlowsA+numberFlowsB)
    })
    .cache
  
    val ddosStats = ddosCollectionFinal
    .map({case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,numberFlows)) =>
        numberFlows
    })
    .stats()
  
  
    ddosCollectionFinal
    .filter({case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,numberFlows)) =>
        numberFlows > ddosMinConnectionsThreshold &
        !p2pTalkers.contains(myIP) & // Avoid P2P talkers
        {
            val orderedFlowSet=
            flowSet
            .map({case (myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,beginTime,endTime) => beginTime})
            .toArray
            .sortBy { x => x }
          
            val orderedFlowSetSize = orderedFlowSet.size
              
            //TODO: Review. You should count the number of equals beginTime and discover why its generating error.
            if(orderedFlowSetSize>6)
            {  
                val flowSetMean=
                (orderedFlowSet.slice(1, orderedFlowSetSize)
                .zip(orderedFlowSet.slice(0, orderedFlowSetSize-1))
                .map({case (a,b) => a-b})
                .toArray
                .sortBy { x => x }
                .slice(0,orderedFlowSetSize-4)
                .sum)/(orderedFlowSetSize-4)
              
                if(flowSetMean<60)
                    true
                else
                    false
            }
            else {
                false
            }
        }
    })
    .map({case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,numberFlows)) =>
        
        (alienIP,(bytesUp,bytesDown,numberPkts,flowSet,numberFlows,1L))
    })
    .reduceByKey({
    case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberFlowsA,pairsA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberFlowsB,pairsB)) =>
        
        (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberFlowsA+numberFlowsB,pairsA+pairsB)
    })
    .filter{case  (alienIP,(bytesUp,bytesDown,numberPkts,flowSet,numberFlows,pairs)) => 
        pairs > ddosMinPairsThreshold 
    }
    .foreach{case  (alienIP,(bytesUp,bytesDown,numberPkts,flowSet,numberFlows,pairs)) => 
    
        println("IP: "+alienIP+ " - DDoS Attack: "+numberFlows+" Pairs: "+pairs.toString)
        
        val flowMap: Map[String,String] = new HashMap[String,String]
        flowMap.put("flow:id",System.currentTimeMillis.toString)
        val event = new nadsEvent(new nadsFlow(flowMap,myIP,"255.255.255.255"))
        event.data.put("numberFlows",numberFlows.toString)
        event.data.put("numberOfAttackers",pairs.toString)
        event.data.put("under attack IP", alienIP)
        event.data.put("bytesUp",   (bytesUp*sampleRate).toString)
        event.data.put("bytesDown", (bytesDown*sampleRate).toString)
        event.data.put("numberPkts", numberPkts.toString)
        //here
        //here
        //here set flows 2 string need to be implemented
        event.data.put("stringFlows", flowSet2String(flowSet))
        event.data.put("flowsMean", ddosStats.mean.round.toString)
        event.data.put("flowsStdev", ddosStats.stdev.round.toString)
        
        // TODO: 
        raiseDDoSEvent(event).alert()       
    }

    //Function: Raise DDoS attack events(write into database and display on front-end)
    def raiseDDoSEvent(event:nadsEvent):nadsEvent = {
        
    }

    //translate flow set to easy-read string
    def flowSet2String(flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Long,Long)]):String =
    {
        flowSet.toList.sortBy({case (srcIP1,srcPort1,dstIP1,dstPort1,proto1,bytesUP,bytesDOWN,numberPkts1,beginTime,endTime) =>  bytesUP+bytesDOWN })
            .reverse
            .take(FlowListLimit)
            ./:("")({ case (c,(srcIP1,srcPort1,dstIP1,dstPort1,proto1,bytesUP,bytesDOWN,numberPkts1,beginTime,endTime)) 
                        => 
                           c+"\n"+
                           //srcIP1+":"+srcPort1+" => "+dstIP1+":"+dstPort1+" "+statusInd+" ("+proto1+", Up: "+humanBytes(bytesUP*sampleRate)+", Down: "
                           //+humanBytes(bytesDOWN*sampleRate)+","+numberPkts1+" pkts, duration: "+(endTime-beginTime)+"s, sampling: 1/"+sampleRate+")"
                           srcIP1+":"+srcPort1+" => "+dstIP1+":"+dstPort1+" ("+proto1+", Up: "+humanBytes(bytesUP)+", Down: "
                           +humanBytes(bytesDOWN)+","+numberPkts1+" pkts, duration: "+(endTime-beginTime)+"s)"       
                    })
    }

    //translate bytes to easy-read format
    def humanBytes(b:Any):String =
    {
        val bytes=b.toString().toLong
        val unit = 1024L
        if (bytes < unit) return bytes + " B";
        val exp = (log(bytes) / log(unit)).toInt;
        val pre = "KMGTPE".charAt(exp-1)
        "%.1f%sB".format(bytes / math.pow(unit, exp), pre);
    }
}

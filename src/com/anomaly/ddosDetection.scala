package com.anomaly

import com.anomaly.nadsEvent
import com.anomaly.nadsFlow

import java.net.InetAddress
import scala.collection.mutable.HashMap
import scala.collection.mutable.HashSet
import scala.collection.mutable.Map
import scala.math.floor
import scala.math.log
//import org.apache.hadoop.hbase.client.Scan
//import org.apache.hadoop.hbase.util.Bytes
import org.apache.spark.SparkContext
import org.apache.spark.rdd.PairRDDFunctions
import org.apache.spark.rdd.RDD
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions

object ddosDetection {

    val FlowListLimit = 1000 //maximum of flow number in a list
    var ddosMinConnectionsThreshold = 6 //need to be optimized later
    var ddosMinPairsThreshold = 5 //need to be optimized later
    //consideration....
    val ddosExceptionAlienPorts:Set[String] = Set("80","443","587","465","993","995")

    println("[debug] -----------------------------------")
    println("[debug] Detecting server under DDoS attack!")
    //get flow data source
    //val flow = new nadsFlow()
    //val nadsFlowSummary = flow.getFlowSummary()

    //run detection algorithm
    def run(nadsRDD:RDD[(String,String,String,String,String,Long,Long,Long,Long,Long)], sc:SparkContext, ddosMinConnections:Int, ddosMinPairs:Int)
    {
        detect(nadsRDD, sc, ddosMinConnections, ddosMinPairs)
    }

    def detect(nadsRDD:RDD[(String,String,String,String,String,Long,Long,Long,Long,Long)], sc:SparkContext, ddosMinConnections:Int, ddosMinPairs:Int)
    {
        ddosMinConnectionsThreshold = ddosMinConnections
        ddosMinPairsThreshold = ddosMinPairs
        val nadsFlowSummary = nadsRDD.map({
            case (myIP, myPort, alienIP, alienPort, proto, bytesUp, bytesDown, numberPkts, beginTime, endTime) =>
            ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime))
        })
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
            //!p2pTalkers.contains(myIP) & // Avoid P2P talkers
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
        .foreach{case (alienIP,(bytesUp,bytesDown,numberPkts,flowSet,numberFlows,pairs)) => 
        
            println("[debug] IP: "+alienIP+ " - DDoS Attack: "+numberFlows+" Pairs: "+pairs.toString)
            
            //val flowMap: Map[String,String] = new HashMap[String,String]
            //flowMap.put("flow:id",System.currentTimeMillis.toString)
            //val eventdata:Map[String, String] = new HashMap()
            val event:nadsEvent = new nadsEvent()
            event.data.put("numberFlows",numberFlows.toString)
            event.data.put("numberOfAttackers",pairs.toString)
            event.data.put("underAttackIP", alienIP)
            event.data.put("bytesUp",   bytesUp.toString)
            event.data.put("bytesDown", bytesDown.toString)
            event.data.put("numberPkts", numberPkts.toString)
            event.data.put("stringFlows", flowSet2String(flowSet))
            event.data.put("flowsMean", ddosStats.mean.round.toString)
            event.data.put("flowsStdev", ddosStats.stdev.round.toString)
            /*eventdata.put("numberFlows",numberFlows.toString)
            eventdata.put("numberOfAttackers",pairs.toString)
            eventdata.put("underAttackIP", alienIP)
            eventdata.put("bytesUp",   bytesUp.toString)
            eventdata.put("bytesDown", bytesDown.toString)
            eventdata.put("numberPkts", numberPkts.toString)
            eventdata.put("stringFlows", flowSet2String(flowSet))
            eventdata.put("flowsMean", ddosStats.mean.round.toString)
            eventdata.put("flowsStdev", ddosStats.stdev.round.toString)*/
            
            // DO: (write into database and display on front-end)
            raiseDDoSEvent(event).alert()
        }
    }

    //Function: Raise DDoS attack events
    def raiseDDoSEvent(event:nadsEvent):nadsEvent = {

        val numberFlows:String = event.data.get("numberFlows")
        val numberOfAttackers:String = event.data.get("numberOfAttackers")
        val alienIP:String = event.data.get("underAttackIP")
        val bytesUp:String = event.data.get("bytesUp")
        val bytesDown:String = event.data.get("bytesDown")
        val numberPkts:String = event.data.get("numberPkts")
        val stringFlows:String = event.data.get("stringFlows")
        val flowsMean:String = event.data.get("flowsMean")
        val flowsStdev:String = event.data.get("flowsStdev")

        event.title = "DDoS Detection Event"
    
        event.text = "This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.\n"+
                      "Abnormal behaviour: Host possibly under DDoS attack.\n"+
                      "IP: "+alienIP+"\n"+
                      "Number of Attackers: "+numberOfAttackers+"\n"+
                      "Number of flows: "+numberFlows+"\n"+
                      "Mean/Stddev of flows per AlienIP (all flows for this IP): "+flowsMean+"/"+flowsStdev+"\n"+
                      "Bytes Up: "+humanBytes(bytesUp)+"\n"+
                      "Bytes Down: "+humanBytes(bytesDown)+"\n"+
                      "Packets: "+numberPkts+"\n"+
                      "Flows: "+stringFlows
                      
        //event.signature_id = signature._16.signature_id  
        event
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
                           srcIP1+":"+srcPort1+" => "+dstIP1+":"+dstPort1+" ("+proto1+", Up: "+humanBytes(bytesUP)+", Down: "+humanBytes(bytesDOWN)+","+numberPkts1+" pkts, duration: "+(endTime-beginTime)+"s)"       
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

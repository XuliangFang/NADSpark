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

object abusedSMTP {
    
    var abusedSMTPBytesThreshold = 50000000L // about 50M
    var abusedSMTPConnectionsThreshold = 50 

    def run(nadsRDD:RDD[(String,String,String,String,String,Long,Long,Long,Long,Long)], sc:SparkContext)
    {
        println("")
        println("Abused SMTP Server")
        val abusedSMTPCollection: PairRDDFunctions[(String, String), (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)],Long,Long)] = sflowSummary
        .filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime)) 
                      =>  
                          ( myPort.equals("465") | //SMTPS protocol 465-->>SSL  587 -->> TLS
                            myPort.equals("587")
                          ) &
                          proto.equals("TCP")  //&
                          //!isMyIP(alienIP,myNets) 
               })
        .map({
              case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime)) =>
                   val flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Long,Long)] = new HashSet()
                   flowSet.add((myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,beginTime,endTime))
                   ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,1L))
            })
      
      
        abusedSMTPCollection
        .reduceByKey({
                       case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,connectionsA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,connectionsB)) =>
                            (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, connectionsA+connectionsB)
                    })
        .filter({ case  ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections)) => 
                        connections>50 &
                        bytesDown > abusedSMTPBytesThreshold
               })
        .sortBy({ 
                  case   ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections)) =>    bytesDown  
                }, false, 15
               )
        .take(100)
        .foreach{ case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections)) => 
                        println("("+myIP+","+alienIP+","+bytesUp+")" ) 
                        val flowMap: Map[String,String] = new HashMap[String,String]
                        flowMap.put("flow:id",System.currentTimeMillis.toString)
                        val event = new NadsEvent(new HogFlow(flowMap,myIP,alienIP))
                        
                        event.data.put("hostname", myIP)
                        event.data.put("bytesUp",   (bytesUp*sampleRate).toString)
                        event.data.put("bytesDown", (bytesDown*sampleRate).toString)
                        event.data.put("numberPkts", numberPkts.toString)
                        event.data.put("connections", connections.toString)
                        event.data.put("stringFlows", setFlows2String(flowSet))
                        
                        raiseAbusedSMTPEvent(event).alert()
        }
    }

    def raiseAbusedSMTPEvent(event:nadsEvent) = {
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
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
    
    val FlowListLimit = 100
    var abusedSMTPBytesThreshold = 5000000L // about 50M
    var abusedSMTPConnectionsThreshold = 10 

    def run(nadsRDD:RDD[(String,String,String,String,String,Long,Long,Long,Long,Long)], sc:SparkContext)
    {
        println("")
        println("Detecting Abused SMTP Server")
        val nadsFlowSummary = nadsRDD.map({
            case (myIP, myPort, alienIP, alienPort, proto, bytesUp, bytesDown, numberPkts, beginTime, endTime) =>
            ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime))
        })
        val abusedSMTPCollection: PairRDDFunctions[(String, String), (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Long,Long)],Long)] = 
        nadsFlowSummary.filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime)) 
                      =>  
                          ( alienPort.equals("465") | //SMTPS protocol 465-->>SSL  587 -->> TLS
                            alienPort.equals("587")
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
      
        //输出中间结果
        println("[debug] All IP->SMTP_Server flow size(count by key): " + abusedSMTPCollection.countByKey().toString)

        abusedSMTPCollection
        .reduceByKey({
                       case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,connectionsA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,connectionsB)) =>
                            (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, connectionsA+connectionsB)
                    })
        .filter({ case  ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections)) => 
                        connections>abusedSMTPConnectionsThreshold &
                        bytesUp > abusedSMTPBytesThreshold
               })
        .sortBy({ 
                  case   ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections)) =>    bytesUp  
                }, false, 15
               )
        .take(100)
        .foreach{ case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections)) => 
                        println("("+myIP+","+alienIP+","+bytesUp+")" ) 
                        //val flowMap: Map[String,String] = new HashMap[String,String]
                        //flowMap.put("flow:id",System.currentTimeMillis.toString)
                        val event = new nadsEvent()
                        event.data.put("hostname", alienIP)
                        event.data.put("bytesUp",   bytesUp.toString)
                        event.data.put("bytesDown", bytesDown.toString)
                        event.data.put("numberPkts", numberPkts.toString)
                        event.data.put("connections", connections.toString)
                        event.data.put("stringFlows", flowSet2String(flowSet))
                        
                        raiseAbusedSMTPEvent(event).alert()
        }
    }

    def raiseAbusedSMTPEvent(event:nadsEvent) = {
        
        val hostname:String = event.data.get("hostname")
        val bytesUp:String = event.data.get("bytesUp")
        val bytesDown:String = event.data.get("bytesDown")
        val numberPkts:String = event.data.get("numberPkts")
        val stringFlows:String = event.data.get("stringFlows")
        val connections:String = event.data.get("connections")
        
        event.text = "This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.\n"+
                      "Abnormal behaviour: Host is receiving too many e-mail submissions. May be an abused SMTP server. \n"+
                      "IP: "+hostname+"\n"+
                      "Bytes Up: "+humanBytes(bytesUp)+"\n"+
                      "Bytes Down: "+humanBytes(bytesDown)+"\n"+
                      "Packets: "+numberPkts+"\n"+
                      "Connections: "+connections+"\n"+
                      "Flows"+stringFlows 
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
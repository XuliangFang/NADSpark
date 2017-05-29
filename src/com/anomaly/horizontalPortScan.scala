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

object horizontalPortScan {
    
    val FlowListLimit = 100
    val hPortScanMinFlowsThreshold = 100
    val hPortScanExceptionPorts = Set("80","443","53")
    val hPortScanExceptionInternalPorts = Set("123")

    def run(nadsRDD:RDD[(String,String,String,String,String,Long,Long,Long,Long,Long)], sc:SparkContext)
    {
        println("")
        println("Detecting Horizontal Port Scan")

        val nadsFlowSummary = nadsRDD.map({
            case (myIP, myPort, alienIP, alienPort, proto, bytesUp, bytesDown, numberPkts, beginTime, endTime) =>
            ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime))
        })
        
        val hPortScanCollection: PairRDDFunctions[(String,String,String), (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Long,Long)],Long,Long)] = 
        nadsFlowSummary
        .filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime)) 
                  => !hPortScanExceptionPorts.contains(alienPort)  & // Avoid common ports
                     (   
                        //!isMyIP(alienIP, myNets) ||
                        !hPortScanExceptionInternalPorts.contains(alienPort) ) &
                        numberPkts < 5 // few pkts per flow
        })
        .map({
        case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,beginTime,endTime)) =>
            val flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Long,Long)] = new HashSet()
            flowSet.add((myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,beginTime,endTime))
            ((myIP,alienIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,1L,1L))
        })

        val hPortScanCollectionFinal=
        hPortScanCollection
        .reduceByKey({
        case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,numberOffPairsA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,numberOffPairsB)) =>
          (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB,1L)
        })
        .map({
            case ((myIP,alienIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOffPairsPort)) =>
            ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOffPairsPort))
        })
        .reduceByKey({
            case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,numberOffPairsA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,numberOffPairsB)) =>
            (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB,numberOffPairsA+numberOffPairsB)
        })
        .cache
      
        val hPortScanStats = 
        hPortScanCollectionFinal
        .map({case ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOffPairsPort)) =>
            numberOffPairsPort
        }).stats()

        hPortScanCollectionFinal
        .filter({case ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort)) =>
            numberOfPairsPort > hPortScanMinFlowsThreshold
        })
        .map({
            case ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort)) =>
        
            val histogram: Map[String,Double] = new HashMap()
            histogram.put(alienPort,numberOfPairsPort)
             
            (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,histogram))
        })
        .reduceByKey({
            case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,numberOfPairsPortA,histogramA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,numberOfPairsPortB,histogramB)) =>
             
            histogramB./:(0){case  (c,(key,qtdH))=> val qtdH2 = {if(histogramA.get(key).isEmpty) 0D else histogramA.get(key).get }
                                                            histogramA.put(key,  qtdH2 + qtdH) 
                                                            0
                            }
            (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB,numberOfPairsPortA+numberOfPairsPortB, histogramA)
        })
        /*.filter{case (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,numPorts,sampleRate)) =>
                       !p2pTalkers.contains(myIP)// Avoid P2P talkers
        }*/
        .foreach{
            case  (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,histogram)) => 

                if(numberOfPairsPort > 100)
                {
                    val atypical = histogram.filter({ case (port, numPairsPort) => 
                        !port.equals("25") //avoid SMTP
                    })

                    if(atypical.size>0)
                    {
                        println("IP: "+myIP+ "  (N:1,Pairs(IP->aline Port) Size:"+numberOfPairsPort+") - Horizontal PortScan: "+numberOfflows+", Ports: "+atypical.toString)
                            
                            val event = new nadsEvent()
                            event.data.put("numberOfFlows",numberOfflows.toString)
                            event.data.put("numberOfFlowsAlienPort",numberOfPairsPort.toString)
                            event.data.put("numberOfFlowsPerPort",atypical.map({case (port,number) => port+"="+number}).mkString("[",", ","]"))
                            event.data.put("myIP", myIP)
                            event.data.put("bytesUp",   bytesUp.toString)
                            event.data.put("bytesDown", bytesDown.toString)
                            event.data.put("numberPkts", numberPkts.toString)
                            event.data.put("stringFlows", flowSet2String(flowSet.filter({p => atypical.keySet.contains(p._4)})))
                            event.data.put("flowsMean", hPortScanStats.mean.round.toString)
                            event.data.put("flowsStdev", hPortScanStats.stdev.round.toString)
                            
                            event.data.put("ports",flowSet
                                            .map({case (myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,beginTime,endTime) =>
                                                (proto,alienPort)
                                            }).toArray
                                            .distinct
                                            .map({case (proto,alienPort) => proto+"/"+alienPort})
                                            .mkString(", ")
                                        )
                            
                            raiseHorizontalPortScanEvent(event).alert()
                    }
        
                /*val savedHistogram=HogHBaseHistogram.getHistogram("HIST07-"+myIP)
                
                val hogHistogramOpenPorts=HogHBaseHistogram.getHistogram("HIST01-"+myIP)
               
                if(savedHistogram.histSize< 100)
                {
                  HogHBaseHistogram.saveHistogram(Histograms.mergeMax(savedHistogram, new HogHistogram("",numberOfPairsPort,histogram)))
                }else
                {
                    val atypical   = histogram.filter({ case (port,numPairsPort) =>
                        
                        if(port.equals("25") && Histograms.isTypicalEvent(hogHistogramOpenPorts.histMap, "25"))
                        {
                            false // Avoid FP with SMTP servers
                        }
                            
                        if(savedHistogram.histMap.get(port).isEmpty)
                        {
                            true // This MyIP never accessed so much distinct Aliens in the same port
                        }
                        else{
                            if(savedHistogram.histMap.get(port).get.toLong < numPairsPort)
                              true // This MyIP never accessed so much distinct Aliens in the same port
                            else
                              false // Is typical
                        }
                    })*/
                }
                else{
                    println("[debug] NADSpark[Horizontal Port Scan]: No result..")
                }
        }
    }

    def raiseHorizontalPortScanEvent(event:nadsEvent) = {
        
        val numberOfFlows:String = event.data.get("numberOfFlows")
        val numberOfFlowsPerPort:String = event.data.get("numberOfFlowsPerPort")
        val myIP:String = event.data.get("myIP")
        val bytesUp:String = event.data.get("bytesUp")
        val bytesDown:String = event.data.get("bytesDown")
        val numberPkts:String = event.data.get("numberPkts")
        val stringFlows:String = event.data.get("stringFlows")
        val flowsMean:String = event.data.get("flowsMean")
        val flowsStdev:String = event.data.get("flowsStdev")
        val numberOfFlowsAlienPort:String = event.data.get("numberOfFlowsAlienPort")
        val ports:String = event.data.get("ports")

        event.title = "NADSpark: Horizontal scan on ports "+ports
        
        event.ports = "Ports: "+ports

        
        event.text = "This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.\n"+
                      "Abnormal behaviour: Horizontal Port Scan \n"+
                      "IP: "+myIP+"\n"+
                      "Number of flows: "+numberOfFlows+"\n"+
                      "Number of flows per AlienPort: "+numberOfFlowsPerPort+"\n"+
                      "Number of flows per distinct AlienIP/AlienPort: "+numberOfFlowsAlienPort+"\n"+
                      "Mean/Stddev of flows per AlienPort (all flows for this IP): "+flowsMean+"/"+flowsStdev+"\n"+
                      "Bytes Up: "+humanBytes(bytesUp)+"\n"+
                      "Bytes Down: "+humanBytes(bytesDown)+"\n"+
                      "Packets: "+numberPkts+"\n"+
                      "Flows"+stringFlows
                      
        //event.signature_id = signature._14.signature_id  
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
                           srcIP1+":"+srcPort1+" => "+dstIP1+":"+dstPort1+" ("+proto1+", Up: "+humanBytes(bytesUP)+", Down: "+humanBytes(bytesDOWN)+","+numberPkts1+" pkts, duration: "+(endTime.toDouble - beginTime.toDouble)+"s)"       
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
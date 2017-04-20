package com.jerryfang.ml

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

object AnomalyDetection {
    
    val nadsSignature = 

    val alienThreshold = 20
    val topTalkersThreshold:Long = 21474836480L // (20*1024*1024*1024 = 20G)
    val SMTPTalkersThreshold:Long = 20971520L // (20*1024*1024 = 20M)
    val atypicalTCPPort:Set[String] = Set("80","443","587","465","993","995")
    val atypicalPairsThresholdMIN = 300
    val atypicalAmountDataThresholdMIN = 5737418240L // (10*1024*1024*1024 = 5G) 
    val p2pPairsThreshold = 5
    val p2pMyPortsThreshold = 4
    val abusedSMTPBytesThreshold = 50000000L // ~50 MB
    val p2pBytes2ndMethodThreshold = 10000000L // ~10 MB
    val p2pPairs2ndMethodThreshold = 10
    val p2pDistinctPorts2ndMethodThreshold = 10
    val mediaClientCommunicationDurationThreshold = 300 // 5min (300s)
    val mediaClientCommunicationDurationMAXThreshold = 7200 // 2h
    val mediaClientPairsThreshold = p2pPairs2ndMethodThreshold
    val mediaClientUploadThreshold = 10000000L // ~10MB
    //val mediaClientDownloadThreshold = 10000000L // ~10MB
    val mediaClientDownloadThreshold = 1000000L // 1MB
    val dnsTunnelThreshold = 50000000L // ~50 MB
    val bigProviderThreshold = 1073741824L // (1*1024*1024*1024 = 1G)
    val icmpTunnelThreshold = 200 // 200b
    val icmpTotalTunnelThreshold = 100000000L // ~100MB
    val hPortScanMinFlowsThreshold = 100
    val hPortScanExceptionPorts = Set("80","443","53")
    val hPortScanExceptionInternalPorts = Set("123")
    val vPortScanMinPortsThreshold = 3
    val vPortScanPortIntervalThreshold = 1024 // 1 to 1023
    val ddosMinConnectionsThreshold = 50 // Over this, can be considered
    val ddosMinPairsThreshold = 20
    val ddosExceptionAlienPorts:Set[String] = Set("80","443","587","465","993","995")
    val FlowListLimit = 1000

    def populateAlienAccessingManyHosts(event:NadsEvent):NadsEvent =
    {
        val numberOfPairs:String = event.data.get("numberOfPairs")
        val alienIP:String = event.data.get("alienIP")
        val bytesUp:String = event.data.get("bytesUp")
        val bytesDown:String = event.data.get("bytesDown")
        val numberPkts:String = event.data.get("numberPkts")
        val stringFlows:String = event.data.get("stringFlows")
        val ports:String = event.data.get("ports")

        event.title = "HZ: Horizontal scan on ports "+ports
        
        event.ports = "Ports: "+ports
        
        event.text = "This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.\n"+
                      "Abnormal behaviour: Alien accessing too much hosts ("+numberOfPairs+"). Possibly a horizontal port scan.\n"+
                      "AlienIP: "+alienIP+"\n"+
                      "Bytes Up: "+humanBytes(bytesUp)+"\n"+
                      "Bytes Down: "+humanBytes(bytesDown)+"\n"+
                      "Packets: "+numberPkts+"\n"+
                      "Number of pairs: "+numberOfPairs+"\n"+
                      "Flows"+stringFlows
                      
        event.signature_id = signature._7.signature_id       
        event
    }

    def populateUDPAmplifier(event:NadsEvent):NadsEvent =
    {
        val hostname:String = event.data.get("hostname")
        val bytesUp:String = event.data.get("bytesUp")
        val bytesDown:String = event.data.get("bytesDown")
        val numberPkts:String = event.data.get("numberPkts")
        val stringFlows:String = event.data.get("stringFlows")
        val connections:String = event.data.get("connections")
        
        event.text = "This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.\n"+
                      "Abnormal behaviour: Host is sending too many big UDP packets. May be a DDoS.\n"+
                      "IP: "+hostname+"\n"+
                      "Bytes Up: "+humanBytes(bytesUp)+"\n"+
                      "Bytes Down: "+humanBytes(bytesDown)+"\n"+
                      "Packets: "+numberPkts+"\n"+
                      "Connections: "+connections+"\n"+
                      "Flows"+stringFlows
                      
        event.signature_id = signature._9.signature_id       
        event
    }

    def populateAbusedSMTP(event:NadsEvent):NadsEvent =
    {
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
                      
        event.signature_id = signature._10.signature_id       
        event
    }

    def populateICMPTunnel(event:NadsEvent):NadsEvent =
    {
        val hostname:String = event.data.get("hostname")
        val bytesUp:String = event.data.get("bytesUp")
        val bytesDown:String = event.data.get("bytesDown")
        val numberPkts:String = event.data.get("numberPkts")
        val stringFlows:String = event.data.get("stringFlows")
        val connections:String = event.data.get("connections")
        
        event.text = "This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.\n"+
                      "Abnormal behaviour: Host has DNS communication with large amount of data. \n"+
                      "IP: "+hostname+"\n"+
                      "Bytes Up: "+humanBytes(bytesUp)+"\n"+
                      "Bytes Down: "+humanBytes(bytesDown)+"\n"+
                      "Packets: "+numberPkts+"\n"+
                      "Connections: "+connections+"\n"+
                      "Flows"+stringFlows
                      
        event.signature_id = signature._13.signature_id       
        event
    }

    def populateHorizontalPortScan(event:NadsEvent):NadsEvent =
    {
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

        event.title = "HZ: Horizontal scan on ports "+ports
        
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
                      
        event.signature_id = signature._14.signature_id  
        event
    }

    def populateDDoSAttack(event:NadsEvent):NadsEvent =
    {
        val numberOfFlows:String = event.data.get("numberOfFlows")
        val numberOfAttackers:String = event.data.get("numberOfAttackers")
        val myIP:String = event.data.get("myIP")
        val bytesUp:String = event.data.get("bytesUp")
        val bytesDown:String = event.data.get("bytesDown")
        val numberPkts:String = event.data.get("numberPkts")
        val stringFlows:String = event.data.get("stringFlows")
        val flowsMean:String = event.data.get("flowsMean")
        val flowsStdev:String = event.data.get("flowsStdev")
        
        
        event.text = "This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.\n"+
                      "Abnormal behaviour: Host possibly under DDoS attack.\n"+
                      "IP: "+myIP+"\n"+
                      "Number of Attackers: "+numberOfAttackers+"\n"+
                      "Number of flows: "+numberOfFlows+"\n"+
                      "Mean/Stddev of flows per AlienIP (all flows for this IP): "+flowsMean+"/"+flowsStdev+"\n"+
                      "Bytes Up: "+humanBytes(bytesUp)+"\n"+
                      "Bytes Down: "+humanBytes(bytesDown)+"\n"+
                      "Packets: "+numberPkts+"\n"+
                      "Flows"+stringFlows
                      
        event.signature_id = signature._16.signature_id  
        event
    }

/*
 * 
 * Server under DDoS attack
 * 
 * 
 */

    
  println("")
  println("Server under DDoS attack")
 
  val ddosCollection: PairRDDFunctions[(String,String), (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)],Long,Long)] = 
    sflowSummary
    .filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) 
                  => !isMyIP(alienIP, myNets) &
                     !ddosExceptionAlienPorts.contains(alienPort) &
                     direction < 1
           })
    .map({
      case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) =>
         val flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)] = new HashSet()
         flowSet.add((myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status))
        ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,1L,sampleRate))
        })
  
  val ddosCollectionFinal=
  ddosCollection
  .reduceByKey({
    case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,sampleRateA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,sampleRateB)) =>
      (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB, (sampleRateA+sampleRateB)/2)
  })
  .cache
  
  val ddosStats = 
  ddosCollectionFinal
  .map({case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,sampleRate)) =>
        numberOfflows
      }).stats()
  
  
  ddosCollectionFinal
  .filter({case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,sampleRate)) =>
        numberOfflows > ddosMinConnectionsThreshold &
        !p2pTalkers.contains(myIP) & // Avoid P2P talkers
        {
          val orderedFlowSet=
          flowSet
          .map({case (myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status) => beginTime})
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
          }else
          {false}
        }
    })
  .map({case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,sampleRate)) =>
           (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,1L,sampleRate))
  })
  .reduceByKey({
    case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,pairsA,sampleRateA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,pairsB,sampleRateB)) =>
      (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB,pairsA+pairsB,(sampleRateA+sampleRateB)/2)
  })
  .filter{case  (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,pairs,sampleRate)) => 
                pairs > ddosMinPairsThreshold 
         }
  .foreach{case  (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,pairs,sampleRate)) => 
    
              
                            println("MyIP: "+myIP+ " - DDoS Attack: "+numberOfflows+" Pairs: "+pairs.toString)
                            
                            val flowMap: Map[String,String] = new HashMap[String,String]
                            flowMap.put("flow:id",System.currentTimeMillis.toString)
                            val event = new NadsEvent(new HogFlow(flowMap,myIP,"255.255.255.255"))
                            event.data.put("numberOfFlows",numberOfflows.toString)
                            event.data.put("numberOfAttackers",pairs.toString)
                            event.data.put("myIP", myIP)
                            event.data.put("bytesUp",   (bytesUp*sampleRate).toString)
                            event.data.put("bytesDown", (bytesDown*sampleRate).toString)
                            event.data.put("numberPkts", numberPkts.toString)
                            event.data.put("stringFlows", setFlows2String(flowSet))
                            event.data.put("flowsMean", ddosStats.mean.round.toString)
                            event.data.put("flowsStdev", ddosStats.stdev.round.toString)
                            
                            // TODO: 
                           populateDDoSAttack(event).alert()
                  
          }

/*
 * 
 * DNS Tunneling 
 * 
 * 
 */
    println("")
    println("DNS Tunneling...")

    val dnsTunnelCollection: PairRDDFunctions[String, (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)],Long,Long)] =
    sflowSummary
    .filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) 
                  =>  
                      alienPort.equals("53") &
                      proto.equals("UDP")  &
                      (bytesUp+bytesDown)*sampleRate > dnsTunnelThreshold &
                      !isMyIP(alienIP,myNets) 
           })
    .map({
          case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) =>
               val flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)] = new HashSet()
               flowSet.add((myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status))
               (myIP,(bytesUp,bytesDown,numberPkts,flowSet,1L,sampleRate))
        })
  
  
    dnsTunnelCollection
    .reduceByKey({
                   case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,connectionsA,sampleRateA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,connectionsB,sampleRateB)) =>
                        (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, connectionsA+connectionsB,(sampleRateA+sampleRateB)/2)
                })
    .sortBy({ 
              case   (myIP,(bytesUp,bytesDown,numberPkts,flowSet,connections,sampleRate)) =>    bytesUp+bytesDown  
            }, false, 15
           )
    .take(30)
    .foreach{ case (myIP,(bytesUp,bytesDown,numberPkts,flowSet,connections,sampleRate)) => 
                    println("("+myIP+","+bytesUp+")" ) 
                    val flowMap: Map[String,String] = new HashMap[String,String]
                    flowMap.put("flow:id",System.currentTimeMillis.toString)
                    val event = new NadsEvent(new HogFlow(flowMap,myIP,"255.255.255.255"))
                    
                    event.data.put("hostname", myIP)
                    event.data.put("bytesUp",   (bytesUp*sampleRate).toString)
                    event.data.put("bytesDown", (bytesDown*sampleRate).toString)
                    event.data.put("numberPkts", numberPkts.toString)
                    event.data.put("connections", connections.toString)
                    event.data.put("stringFlows", setFlows2String(flowSet.filter({p => p._4.equals("53")}))) // 4: alienPort
                    
                    populateDNSTunnel(event).alert()
    }

/*
 * 
 *  Abused SMTP Server
 * 
 */
   
    println("")
    println("Abused SMTP Server")
    val abusedSMTPCollection: PairRDDFunctions[(String, String), (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)],Long,Long)] = sflowSummary
    .filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) 
                  =>  
                      ( myPort.equals("465") | //SMTPS protocol 465-->>SSL  587 -->> TLS
                        myPort.equals("587")
                      ) &
                      proto.equals("TCP")  &
                      !isMyIP(alienIP,myNets) 
           })
    .map({
          case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) =>
               val flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)] = new HashSet()
               flowSet.add((myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status))
               ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,1L,sampleRate))
        })
  
  
    abusedSMTPCollection
    .reduceByKey({
                   case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,connectionsA,sampleRateA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,connectionsB,sampleRateB)) =>
                        (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, connectionsA+connectionsB,(sampleRateA+sampleRateB)/2)
                })
    .filter({ case  ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections,sampleRate)) => 
                    connections>50 &
                    bytesDown*sampleRate > abusedSMTPBytesThreshold
           })
    .sortBy({ 
              case   ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections,sampleRate)) =>    bytesDown  
            }, false, 15
           )
    .take(100)
    .foreach{ case ((myIP,alienIP),(bytesUp,bytesDown,numberPkts,flowSet,connections,sampleRate)) => 
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
                    
                    populateAbusedSMTP(event).alert()
    }

/*
  * 
  * Horizontal PortScans
  * 
  * 
  */
  //val hPortScanMinFlowsThreshold = 300
  
    println("")
    println("Horizontal portscan")
 
    val hPortScanCollection: PairRDDFunctions[(String,String,String), (Long,Long,Long,HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)],Long,Long,Long)] = 
    sflowSummary
    .filter({case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) 
                  => !hPortScanExceptionPorts.contains(alienPort)  & // Avoid common ports
                     (   
                        !isMyIP(alienIP, myNets) ||
                        !hPortScanExceptionInternalPorts.contains(alienPort) ) &
                        numberPkts < 5 // few pkts per flow
           })
    .map({
        case ((myIP,myPort,alienIP,alienPort,proto),(bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status)) =>
        val flowSet:HashSet[(String,String,String,String,String,Long,Long,Long,Int,Long,Long,Long,Int)] = new HashSet()
        flowSet.add((myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status))
        ((myIP,alienIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,1L,1L,sampleRate))
    })
  
    val hPortScanCollectionFinal=
    hPortScanCollection
    .reduceByKey({
    case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,numberOffPairsA,sampleRateA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,numberOffPairsB,sampleRateB)) =>
      (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB,1L, (sampleRateA+sampleRateB)/2)
    })
    .map({
        case ((myIP,alienIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOffPairsPort,sampleRate)) =>
        ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOffPairsPort,sampleRate))
    })
    .reduceByKey({
        case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,numberOffPairsA,sampleRateA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,numberOffPairsB,sampleRateB)) =>
        (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB,numberOffPairsA+numberOffPairsB, (sampleRateA+sampleRateB)/2)
    })
    .cache
  
    val hPortScanStats = 
    hPortScanCollectionFinal
    .map({case ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOffPairsPort,sampleRate)) =>
        numberOffPairsPort
    }).stats()
  
  
    hPortScanCollectionFinal
    .filter({case ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,sampleRate)) =>
        numberOfPairsPort > hPortScanMinFlowsThreshold
    })
    .map({
        case ((myIP,alienPort),(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,sampleRate)) =>
    
        val histogram: Map[String,Double] = new HashMap()
        histogram.put(alienPort,numberOfPairsPort)
         
        (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,histogram,sampleRate))
    })
    .reduceByKey({
        case ((bytesUpA,bytesDownA,numberPktsA,flowSetA,numberOfflowsA,numberOfPairsPortA,histogramA,sampleRateA),(bytesUpB,bytesDownB,numberPktsB,flowSetB,numberOfflowsB,numberOfPairsPortB,histogramB,sampleRateB)) =>
         
        histogramB./:(0){case  (c,(key,qtdH))=> val qtdH2 = {if(histogramA.get(key).isEmpty) 0D else histogramA.get(key).get }
                                                        histogramA.put(key,  qtdH2 + qtdH) 
                                                        0
                                 }
        (bytesUpA+bytesUpB,bytesDownA+bytesDownB, numberPktsA+numberPktsB, flowSetA++flowSetB, numberOfflowsA+numberOfflowsB,numberOfPairsPortA+numberOfPairsPortB, histogramA,(sampleRateA+sampleRateB)/2)
    })
    .filter{case (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,numPorts,sampleRate)) =>
                   !p2pTalkers.contains(myIP)// Avoid P2P talkers
           }
    .foreach{
        case  (myIP,(bytesUp,bytesDown,numberPkts,flowSet,numberOfflows,numberOfPairsPort,histogram,sampleRate)) => 
    
                val savedHistogram=HogHBaseHistogram.getHistogram("HIST07-"+myIP)
                
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
                                        })
                        
                       // Histograms.atypical(savedHistogram.histMap, histogram)

                    if(atypical.size>0)
                    {
                        println("MyIP: "+myIP+ "  (N:1,S:"+savedHistogram.histSize+") - Horizontal PortScan: "+numberOfflows+", Ports: "+atypical.toString)
                        
                        val flowMap: Map[String,String] = new HashMap[String,String]
                        flowMap.put("flow:id",System.currentTimeMillis.toString)
                        val event = new NadsEvent(new HogFlow(flowMap,myIP,"255.255.255.255"))
                        event.data.put("numberOfFlows",numberOfflows.toString)
                        event.data.put("numberOfFlowsAlienPort",numberOfPairsPort.toString)
                        event.data.put("numberOfFlowsPerPort",atypical.map({case (port,number) => port+"="+number}).mkString("[",", ","]"))
                        event.data.put("myIP", myIP)
                        event.data.put("bytesUp",   (bytesUp*sampleRate).toString)
                        event.data.put("bytesDown", (bytesDown*sampleRate).toString)
                        event.data.put("numberPkts", numberPkts.toString)
                        event.data.put("stringFlows", setFlows2String(flowSet.filter({p => atypical.keySet.contains(p._4)})))
                        event.data.put("flowsMean", hPortScanStats.mean.round.toString)
                        event.data.put("flowsStdev", hPortScanStats.stdev.round.toString)
                        
                        event.data.put("ports",flowSet
                                          .map({case (myIP,myPort,alienIP,alienPort,proto,bytesUp,bytesDown,numberPkts,direction,beginTime,endTime,sampleRate,status) =>
                                                (proto,alienPort)
                                               }).toArray
                                               .distinct
                                               .map({case (proto,alienPort) => proto+"/"+alienPort})
                                               .mkString(", ")
                                        )
                        
                        populateHorizontalPortScan(event).alert()
                    }
                      
                    HogHBaseHistogram.saveHistogram(Histograms.mergeMax(savedHistogram, new HogHistogram("",numberOfflows,histogram)))
                }
        }

}
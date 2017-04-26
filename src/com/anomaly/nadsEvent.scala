/*
 *为了展示异常检测的结果，将结果生成一个event，推送到前端
 *Function：1、生成event
 *          2、alert event，即写入数据库"mysql://localhost:3306/anomaly", "anomaly.events"
 */

package com.anomaly

import java.util.Properties
import java.util.HashMap
import java.util.Map
import org.apache.hadoop.hbase.client.Put
import org.apache.hadoop.hbase.util.Bytes

class nadsEvent {

    var signature_id:Double=0
    var priorityid:Int=0
    var text:String=""
    var data:Map[String,String]=new HashMap()
    var ports:String=""
    var title:String=""

    //TODO: write into database(HBase or MySQL)
    /*def alert()
    {
        val put = new Put(Bytes.toBytes(flow.get("flow:id")))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("note"), Bytes.toBytes(text))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("lower_ip"), formatIPtoBytes(flow.lower_ip))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("upper_ip"), formatIPtoBytes(flow.upper_ip))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("lower_ip_str"), Bytes.toBytes(flow.lower_ip))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("upper_ip_str"), Bytes.toBytes(flow.upper_ip))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("signature_id"), Bytes.toBytes("%.0f".format(signature_id)))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("time"), Bytes.toBytes(System.currentTimeMillis))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("ports"), Bytes.toBytes(ports))
        put.add(Bytes.toBytes("event"), Bytes.toBytes("title"), Bytes.toBytes(title))
        nadsHBaseRDD.nads_events.put(put)

        //println(f"ALERT: $text%100s\n\n@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@")
    }*/
    def alert()
    {
        
    }
}
# -*- coding: UTF-8 -*-
# pre processing data set
# function: get flow format that we need
# usage:
import os
import sys
import string

def processFlowData(arg1, arg2):
    print "The parameter you input "+arg1 + " " + arg2
    rawFile = arg1
    newFile = open(arg2, 'w')
    with open(rawFile) as file:
        for line in file:
            res = line.split("\t")
            #if(long(res[5])-long(res[4])==0):
            #    print "!!!!!!!!"
            #else:
                #print str(len(res))
            # srcIP, srcPort, dstIP, dstPort
            newFile.write(res[0]+" "+res[2]+" "+res[1]+" "+res[3])
            # proto(TCP), bytesUp, bytesDown, numberPkts, beginTime, endTime
            # 注意list中最后一个字符串结尾有换行符'\n'
            newFile.write(" TCP "+res[7]+ " " + res[9].strip('\n') + " " + str(int(res[6])+int(res[8])).strip()+ " "+res[4]+" "+res[5])
            newFile.write('\n')


    file.close()
    newFile.close()
    return

if __name__=='__main__':
    processFlowData('/home/u928/fl/anomalyDetection/tcp_session_5min.txt', '/home/u928/fl/anomalyDetection/processed_tcp_session_5min.txt')
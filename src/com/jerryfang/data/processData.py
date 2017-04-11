# -*- coding: UTF-8 -*-
# pre processing data set
# function: filter records(up & down)
# usage: python preProcessing.py filepath
import os
import sys
import string

def processData(arg1, arg2):
    print "The parameter you input "+arg1 + " " + arg2
    rawFile = arg1
    newFile = open(arg2, 'w')
    with open(rawFile) as file:
        for line in file:
            if (line.find("up") != -1 and line.find("down") != -1):
                #newFile.write(line)
                # DNS behavior
                print line
                dnsNum = 0
                dnsIndex = line.find("dns")
                if(dnsIndex!= -1):
                    newFile.write(line[dnsIndex+4: line.find(' ', dnsIndex+4, len(line))]+" ")
                    print "Expected DNS number: " + line[dnsIndex+4: line.find(' ', dnsIndex+4, len(line))]
                else:
                    newFile.write("0 ")
                    print "Expected DNS number: 0"

                #Ratio of upload package number/download package number
                upIndex = line.find("up")+3
                print "upload package number: " + line[upIndex: line.find(' ', upIndex, len(line))]
                downIndex = line.find("down")+5
                print "download package number: " + line[downIndex: line.find(' ', downIndex, len(line))]
                ratio = 0.0

                #Ratio of upload package size/download package size
                upsizeIndex = line.find(' ', upIndex, len(line))+1
                print "upload package size: " + line[upsizeIndex : line.find(' ', upsizeIndex, len(line))]
                downsizeIndex = line.find(' ', downIndex, len(line))+1
                print "download package size: " + line[downsizeIndex : line.find(' ', downsizeIndex, len(line))]

                #Proportion of SYN packages
                synIndex = line.find("syn")+4
                print "IP->OUT syn package number: " + line[synIndex : line.find(" ", synIndex, len(line))]

                #Proportion of PSH packages
                pshIndex = line.find("psh")+4
                print "IP<-IP psh package number: " + line[pshIndex : line.find(" ", pshIndex, len(line))]

                #Proportion of small packages
                smlIndex1 = line.find("small")+6
                print "small package number1: " + line[smlIndex1 : line.find(" ", smlIndex1, len(line))]
                smlIndex2 = line.find("small", smlIndex1+1, len(line))+6
                print "small package number2: " + line[smlIndex2 : line.find(" ", smlIndex2, len(line))]
                newFile.write('\n')

    file.close()
    newFile.close()
    return

if __name__=='__main__':
    processData('/home/jerry/NADSpark/data/result.txt', '/home/jerry/NADSpark/data/dataSet.txt')
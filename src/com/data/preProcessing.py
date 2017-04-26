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
                dnsNum = 0
                dnsIndex = line.find("dns")
                if(dnsIndex!= -1):
                    print "Expected DNS number: " + line[dnsIndex+1: line.find(' ', dnsIndex, len(line))]
                else
                    print "Expected DNS number: 0"

                #Ratio of upload package number/download package number
                upIndex = line.find("up")
                print "upload package number: " + line[upIndex: line.find(' ', upIndex, len(line))]
                downIndex = line.find("down")
                print "upload package number: " + line[downIndex: line.find(' ', downIndex, len(line))]
                ratio = 0.0

                #Ratio of upload package size/download package size
                upsizeIndex = line.find(' ', upIndex, len(line))+1
                print "upload package size: " + line[upsizeIndex : line.find(' ', upsizeIndex, len(line))]
                downsizeIndex = line.find(' ', downIndex, len(line))+1
                print "download package size: " + line[downsizeIndex : line.find(' ', downsizeIndex, len(line))]

                #Proportion of SYN packages
                

                #Proportion of PSH packages

                #Proportion of small packages


    file.close()
    newFile.close()
    return

if __name__=='__main__':
    processData('/home/hadoop/trojanData/result.txt', '/home/hadoop/trojanData/dataSet.txt')
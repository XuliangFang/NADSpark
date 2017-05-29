# -*- coding: UTF-8 -*-
# pre processing data set
# function: filter records(up & down)
import os
import sys
import string

def processData(arg1, arg2):
    print "The parameter you input "+arg1 + " " + arg2
    rawFile = arg1
    newFile = open(arg2, 'w')
    with open(rawFile) as file:
        for line in file:
            if (line.find("up") != -1 and line.find("down") != -1 and line.find("com") != -1):
                #newFile.write(line)
                # DNS behavior
                print line

                # Mean interval time and session duaration
                comIndex1 = line.find("com")+4
                times = int(line[comIndex1 : line.find(" ", comIndex1, len(line))])
                if(times==0):
                    continue
                # ip addr
                firstSpaceIndex = line.find(' ')
                print "IP address: " + line[0 : firstSpaceIndex]
                #newFile.write(line[0:firstSpaceIndex]+" ")

                comIndex2 = line.find(" ", comIndex1, len(line))+1
                duaration = int(line[comIndex2 : line.find(" ", comIndex2, len(line))])
                newFile.write(str(1.0 * duaration / times) + " ")
                #print "[debug] times= " + line[comIndex1 : line.find(" ", comIndex1, len(line))]
                #print "[debug] session time= " + line[comIndex2 : line.find(" ", comIndex2, len(line))]

                # dns 
                dnsNum = 0
                dnsIndex = line.find("dns")
                if(dnsIndex!= -1):
                    newFile.write(line[dnsIndex+4: line.find(' ', dnsIndex+4, len(line))]+" ")
                    print "DNS number: " + line[dnsIndex+4: line.find(' ', dnsIndex+4, len(line))]
                else:
                    newFile.write("0 ")
                    print "DNS number: 0"

                # Ratio of upload package number/download package number
                upIndex = line.find("up")+3
                uploadNum = int(line[upIndex: line.find(' ', upIndex, len(line))])
                #print "upload package number: " + line[upIndex: line.find(' ', upIndex, len(line))]
                #print "upload package number: "+ str(uploadNum)
                downIndex = line.find("down")+5
                downloadNum = int(line[downIndex: line.find(' ', downIndex, len(line))])
                #print "download package number: " + line[downIndex: line.find(' ', downIndex, len(line))]
                #print "download package number: "+ str(downloadNum)
                ratioNum = 1.0 * uploadNum / downloadNum
                print "upload number / download number = " + str(ratioNum)
                newFile.write(str(ratioNum) + " ")

                # Ratio of upload package size/download package size
                upsizeIndex = line.find(' ', upIndex, len(line))+1
                uploadSize = int(line[upsizeIndex : line.find(' ', upsizeIndex, len(line))])
                #print "upload package size: " + line[upsizeIndex : line.find(' ', upsizeIndex, len(line))]
                downsizeIndex = line.find(' ', downIndex, len(line))+1
                downloadSize = int(line[downsizeIndex : line.find(' ', downsizeIndex, len(line))])
                #print "download package size: " + line[downsizeIndex : line.find(' ', downsizeIndex, len(line))]
                ratioSize = 1.0 * uploadSize / downloadSize
                print "uploadSize/downloadSize = " + str(ratioSize)
                newFile.write(str(ratioSize) + " ")

                # Proportion of SYN packages
                synIndex = line.find("syn")+4
                synNum = int(line[synIndex : line.find(" ", synIndex, len(line))])
                #print "IP->OUT syn package number: " + line[synIndex : line.find(" ", synIndex, len(line))]
                ratioSyn = 1.0 * synNum / uploadNum
                print "synNum/uploadNum = " + str(ratioSyn)
                newFile.write(str(ratioSyn) + " ")

                # Proportion of PSH packages
                pshIndex = line.find("psh")+4
                pshNum = int(line[pshIndex : line.find(" ", pshIndex, len(line))])
                #print "IP<-IP psh package number: " + line[pshIndex : line.find(" ", pshIndex, len(line))]
                ratioPsh = 1.0 * pshNum / downloadNum
                print "pshNum/downloadNum = " + str(ratioPsh)
                newFile.write(str(ratioPsh) + " ")

                # Proportion of small packages
                smlIndex1 = line.find("small")+6
                smlNum1 = int(line[smlIndex1 : line.find(" ", smlIndex1, len(line))])
                #print "small package number1: " + line[smlIndex1 : line.find(" ", smlIndex1, len(line))]
                smlIndex2 = line.find("small", smlIndex1+1, len(line))+6
                smlNum2 = int(line[smlIndex2 : line.find(" ", smlIndex2, len(line))])
                #print "small package number2: " + line[smlIndex2 : line.find(" ", smlIndex2, len(line))]
                smlNum = smlNum1 + smlNum2
                ratioSml = 1.0 * smlNum / (uploadNum + downloadNum)
                print "smallNum/packagesNum = " + str(ratioSml)
                newFile.write(str(ratioSml))

                newFile.write('\n')

    file.close()
    newFile.close()
    return

if __name__=='__main__':
    processData('/home/u928/jrj2/dataForTrojan/data.txt', '/home/hadoop/trojanData/normalizeDataSet_v2.txt')
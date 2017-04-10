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
            if line.find("up") != -1 and line.find("down") != -1:
                newFile.write(line)
    file.close()
    newFile.close()
    return

if __name__=='__main__':
    processData('/home/hadoop/trojanData/result.txt', '/home/hadoop/trojanData/dataSet.txt')
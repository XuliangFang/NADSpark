#### k=8

```shell
The threshold is 536.235
Cluster:2 Number:3
Cluster:5 Number:1
Cluster:4 Number:2
Cluster:7 Number:15
Cluster:1 Number:1
Cluster:3 Number:1
Cluster:6 Number:3
Cluster:0 Number:107221
Selecting anomalous cluster...
How many clusters? Clusters Number: 8
Center Point of Cluster 0:
[2.6887363482899804,3.367009014156229,5.6494627283865295,0.2510021380193687,0.9999999999999999,0.742346715408312]
Center Point of Cluster 1:
[77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
Center Point of Cluster 2:
[99913.66666666666,3.880962557463333,3.4387146794756664,0.5870692550026666,1.0,0.34104775356533334]
Center Point of Cluster 3:
[0.0,2719.42857143,56915.8978289,1.05064089094E-4,1.0,0.00105025468676]
Center Point of Cluster 4:
[0.0,1453.9,34649.143289650005,9.62705632375E-5,1.0,9.302886908604999E-4]
Center Point of Cluster 5:
[167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
Center Point of Cluster 6:
[59805.666666666664,0.692724642054,0.3694288765801666,0.26462213827939995,1.0,0.5283086461553332]
Center Point of Cluster 7:
[0.0,1439.5433386227335,8741.994330216665,0.0676334300006,1.0,0.10494856894968786]
Got anomalous clusters...---------->>>>>>>>

The index of anomalous cluster is 2
The points in this cluster are as follows: 
[97841.0,0.544809228039,0.437771447757,0.42671009772,1.0,0.604824813326]
[95608.0,10.2487961477,8.7403233709,0.888018794049,1.0,0.122716894977]
[106292.0,0.849282296651,1.13804921977,0.446478873239,1.0,0.295601552393]

The index of anomalous cluster is 5
The points in this cluster are as follows:
[167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]

The index of anomalous cluster is 4
The points in this cluster are as follows:
[0.0,740.8,24918.5302083,8.9992800576E-5,1.0,0.00134807225667]
[0.0,2167.0,44379.756371,1.02548325899E-4,1.0,5.12505125051E-4]

The index of anomalous cluster is 7
The points in this cluster are as follows:
[0.0,239.935483871,5192.11644961,2.24074572018E-4,1.0,3.12402374258E-4]
[0.0,1104.75,14977.9347826,0.0,1.0,0.0153741804205]
[0.0,344.676470588,7493.43123799,1.70663025855E-4,1.0,0.00238237045861]
[0.0,389.75,11735.2325581,0.00128287363695,1.0,0.00383877159309]
[0.0,4309.5,8907.18656716,1.16022740457E-4,1.0,0.0184433360399]
[0.0,190.0,4998.48630137,0.00526315789474,1.0,0.00261780104712]
[0.0,4066.0,4066.0,1.0,1.0,1.0]
[0.0,362.388888889,4791.23759791,7.66518473095E-4,1.0,0.00290475462468]
[0.0,609.049235993,8699.12439682,2.78761185293E-6,1.0,0.00279139149335]
[0.0,391.2,4880.54810496,0.0,1.0,0.204487506374]
[0.0,1477.0,8112.41102757,6.77048070413E-4,1.0,0.0020297699594]
[0.0,309.4,4632.08746356,0.00129282482224,1.0,0.239690721649]
[0.0,464.0,16578.96875,0.00431034482759,1.0,0.00430107526882]
[0.0,1085.5,12844.5152587,1.31604922024E-4,1.0,0.065183091184]
[0.0,6250.0,13220.6344569,2.63529411765E-4,1.0,0.00987136175859]

The index of anomalous cluster is 1
The points in this cluster are as follows: 
[77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]

The index of anomalous cluster is 3
The points in this cluster are as follows: 
[0.0,2719.42857143,56915.8978289,1.05064089094E-4,1.0,0.00105025468676]

The index of anomalous cluster is 6
The points in this cluster are as follows: 
[38369.0,0.954158302147,0.0888580369825,0.0397530693172,1.0,0.335750914202]
[65657.0,0.637529137529,0.569460871648,0.254113345521,1.0,0.6128113879]
[75391.0,0.486486486486,0.44996772111,0.5,1.0,0.636363636364]
```

#### Wednesday April 12th, 2017

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/trainSet.txt /tcpheader/trainSet.txt 8 50 3 2>&1 | grep "debug"
[debug] The threshold is 536.235
[debug] Cluster:2 Number:2
[debug] Cluster:5 Number:1
[debug] Cluster:4 Number:3
[debug] Cluster:7 Number:3
[debug] Cluster:1 Number:1
[debug] Cluster:3 Number:2
[debug] Cluster:6 Number:22
[debug] Cluster:0 Number:107213
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 8
[debug] Center Point of Cluster 0:
[debug] Center Point of Cluster 1:
[debug] Center Point of Cluster 2:
[debug] Center Point of Cluster 3:
[debug] Center Point of Cluster 4:
[debug] Center Point of Cluster 5:
[debug] Center Point of Cluster 6:
[debug] Center Point of Cluster 7:
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] The index of anomalous cluster is 2
[debug] The points in this cluster are as follows: 
[debug] In cluster 2: [0.0,2167.0,44379.756371,1.02548325899E-4,1.0,5.12505125051E-4]
[debug] In cluster 2: [0.0,2719.42857143,56915.8978289,1.05064089094E-4,1.0,0.00105025468676]
[debug] =======================================
[debug] The index of anomalous cluster is 5
[debug] The points in this cluster are as follows: 
[debug] In cluster 5: [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
[debug] =======================================
[debug] The index of anomalous cluster is 4
[debug] The points in this cluster are as follows: 
[debug] In cluster 4: [17016.0,3.7,4.29779470094,0.628733997155,1.0,0.354983202688]
[debug] In cluster 4: [38369.0,0.954158302147,0.0888580369825,0.0397530693172,1.0,0.335750914202]
[debug] In cluster 4: [18420.0,0.92987804878,1.28885251269,0.252459016393,1.0,0.424960505529]
[debug] =======================================
[debug] The index of anomalous cluster is 7
[debug] The points in this cluster are as follows: 
[debug] In cluster 7: [97841.0,0.544809228039,0.437771447757,0.42671009772,1.0,0.604824813326]
[debug] In cluster 7: [95608.0,10.2487961477,8.7403233709,0.888018794049,1.0,0.122716894977]
[debug] In cluster 7: [106292.0,0.849282296651,1.13804921977,0.446478873239,1.0,0.295601552393]
[debug] =======================================
[debug] The index of anomalous cluster is 1
[debug] The points in this cluster are as follows: 
[debug] In cluster 1: [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
[debug] =======================================
[debug] The index of anomalous cluster is 3
[debug] The points in this cluster are as follows: 
[debug] In cluster 3: [65657.0,0.637529137529,0.569460871648,0.254113345521,1.0,0.6128113879]
[debug] In cluster 3: [75391.0,0.486486486486,0.44996772111,0.5,1.0,0.636363636364]
[debug] =======================================
[debug] The index of anomalous cluster is 6
[debug] The points in this cluster are as follows: 
[debug] In cluster 6: [5252.0,3.11904761905,3.23838797814,0.335877862595,1.0,0.554913294798]
[debug] In cluster 6: [6761.0,2.28301886792,2.30614937978,0.404958677686,1.0,0.505747126437]
[debug] In cluster 6: [4752.0,1.3676975945,1.44781309693,0.268844221106,1.0,0.595065312046]
[debug] In cluster 6: [5343.0,1.37654320988,1.51076356946,0.295964125561,1.0,0.615584415584]
[debug] In cluster 6: [6386.0,2.62962962963,2.71214116408,0.366197183099,1.0,0.530612244898]
[debug] In cluster 6: [3739.0,1.80225988701,1.90839195158,0.294670846395,1.0,0.592741935484]
[debug] In cluster 6: [12687.0,6.0,7.328125,0.166666666667,1.0,0.642857142857]
[debug] In cluster 6: [11887.0,1.5,1.43042071197,0.166666666667,1.0,0.6]
[debug] In cluster 6: [9887.0,0.992551635898,0.161954507379,0.106440771604,1.0,0.476000458663]
[debug] In cluster 6: [6701.0,0.825196346545,0.64772050822,0.0609041227406,1.0,0.176734877858]
[debug] In cluster 6: [11374.0,1.0,0.932038834951,0.0,1.0,0.5]
[debug] In cluster 6: [5558.0,6.29145347932,3.68229685671,0.891745970476,1.0,0.891161187719]
[debug] In cluster 6: [8975.0,1.05882352941,1.10482529118,0.166666666667,1.0,0.628571428571]
[debug] In cluster 6: [8812.0,0.625,0.609324758842,0.2,1.0,0.538461538462]
[debug] In cluster 6: [10559.0,3.75,3.55627009646,0.166666666667,1.0,0.631578947368]
[debug] In cluster 6: [5079.0,2.32653061224,2.61075949367,0.473684210526,1.0,0.521472392638]
[debug] In cluster 6: [6573.0,1.72164948454,1.75881073241,0.359281437126,1.0,0.556818181818]
[debug] In cluster 6: [4399.0,1.28729281768,1.38994209566,0.25321888412,1.0,0.601449275362]
[debug] In cluster 6: [4370.0,1.4858490566,1.61521191918,0.263492063492,1.0,0.599620493359]
[debug] In cluster 6: [6615.0,1.15909090909,1.21840209561,0.352941176471,1.0,0.573684210526]
[debug] In cluster 6: [11406.0,2.5625,2.35587761675,0.365853658537,1.0,0.526315789474]
[debug] In cluster 6: [4488.0,1.05143989983,0.470761643917,0.286593232113,1.0,0.25212349321]
[debug] =======================================

```

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/trainSet.txt /tcpheader/dataSet.txt 8 50 3 2>&1 | grep "debug"
[debug] The threshold is 536.235
[debug] Cluster:2 Number:4
[debug] Cluster:5 Number:1
[debug] Cluster:4 Number:2
[debug] Cluster:7 Number:26
[debug] Cluster:1 Number:1
[debug] Cluster:3 Number:2
[debug] Cluster:6 Number:6
[debug] Cluster:0 Number:107205
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 8
[debug] Center Point of Cluster 0:
[debug] [2.6891376335059003,3.246403089189776,5.217127148719398,0.25099228665599915,1.0,0.7424157379785072]
[debug] Center Point of Cluster 1:
[debug] [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
[debug] Center Point of Cluster 2:
[debug] [93783.0,3.0323435397189997,2.6915279398842498,0.565301941252,1.0,0.414876724265]
[debug] Center Point of Cluster 3:
[debug] [0.0,2443.214285715,50647.82709995,1.038062074965E-4,1.0,7.813799059054999E-4]
[debug] Center Point of Cluster 4:
[debug] [52013.0,0.795843719838,0.32915945431524996,0.14693320741910001,1.0,0.474281151051]
[debug] Center Point of Cluster 5:
[debug] [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
[debug] Center Point of Cluster 6:
[debug] [0.0,1672.4666666666665,15712.63600243333,0.0010130575998174999,1.0,0.016652758746945]
[debug] Center Point of Cluster 7:
[debug] [0.0,972.4069388306153,4161.982937262307,0.23387164290607343,1.0,0.22898626395304136]
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] The index of anomalous cluster is 2
[debug] The points in this cluster are as follows: 
[debug] In cluster 2: 202.121.244.200 [97841.0,0.544809228039,0.437771447757,0.42671009772,1.0,0.604824813326]
[debug] In cluster 2: 210.35.96.2 [95608.0,10.2487961477,8.7403233709,0.888018794049,1.0,0.122716894977]
[debug] In cluster 2: 210.35.96.6 [106292.0,0.849282296651,1.13804921977,0.446478873239,1.0,0.295601552393]
[debug] In cluster 2: 202.121.64.5 [75391.0,0.486486486486,0.44996772111,0.5,1.0,0.636363636364]
[debug] =======================================
[debug] The index of anomalous cluster is 5
[debug] The points in this cluster are as follows: 
[debug] In cluster 5: 202.121.209.11 [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
[debug] =======================================
[debug] The index of anomalous cluster is 4
[debug] The points in this cluster are as follows: 
[debug] In cluster 4: 202.121.244.201 [65657.0,0.637529137529,0.569460871648,0.254113345521,1.0,0.6128113879]
[debug] In cluster 4: 202.121.64.130 [38369.0,0.954158302147,0.0888580369825,0.0397530693172,1.0,0.335750914202]
[debug] =======================================
[debug] The index of anomalous cluster is 7
[debug] The points in this cluster are as follows: 
[debug] In cluster 7: 223.105.4.244 [0.0,2809.44585987,2809.44585987,0.999990931412,1.0,1.0]
[debug] In cluster 7: 182.254.35.146 [0.0,239.935483871,5192.11644961,2.24074572018E-4,1.0,3.12402374258E-4]
[debug] In cluster 7: 121.205.163.167 [0.0,344.676470588,7493.43123799,1.70663025855E-4,1.0,0.00238237045861]
[debug] In cluster 7: 182.254.17.32 [0.0,129.225806452,3484.73757372,2.49625561658E-4,1.0,3.71563041863E-4]
[debug] In cluster 7: 202.195.235.30 [0.0,83.0,2618.25252525,0.0200803212851,1.0,0.0277777777778]
[debug] In cluster 7: 1.165.83.79 [0.0,226.5,3493.12259615,0.0,1.0,0.0281318681319]
[debug] In cluster 7: 137.74.86.230 [0.0,2026.88235294,2026.88235294,1.0,1.0,1.0]
[debug] In cluster 7: 14.113.67.233 [0.0,242.0,2210.09090909,0.00413223140496,1.0,0.0493827160494]
[debug] In cluster 7: 140.207.55.22 [0.0,4309.5,8907.18656716,1.16022740457E-4,1.0,0.0184433360399]
[debug] In cluster 7: 54.192.75.206 [0.0,190.0,4998.48630137,0.00526315789474,1.0,0.00261780104712]
[debug] In cluster 7: 175.19.209.140 [0.0,4066.0,4066.0,1.0,1.0,1.0]
[debug] In cluster 7: 117.45.74.51 [0.0,117.0,3088.86259542,0.0042735042735,1.0,0.0360169491525]
[debug] In cluster 7: 210.41.98.241 [0.0,282.0,2887.27777778,0.00177304964539,1.0,0.0150176678445]
[debug] In cluster 7: 210.44.112.103 [0.0,143.25,3559.92248062,0.00261780104712,1.0,0.00779896013865]
[debug] In cluster 7: 202.201.11.14 [0.0,362.388888889,4791.23759791,7.66518473095E-4,1.0,0.00290475462468]
[debug] In cluster 7: 184.50.87.34 [0.0,146.930232558,4282.44859813,0.00110794555239,1.0,0.00204370382015]
[debug] In cluster 7: 210.32.175.67 [0.0,609.049235993,8699.12439682,2.78761185293E-6,1.0,0.00279139149335]
[debug] In cluster 7: 121.235.42.23 [0.0,177.0,3160.3671875,0.00282485875706,1.0,0.213483146067]
[debug] In cluster 7: 115.231.232.195 [0.0,2261.0,2261.0,1.0,1.0,1.0]
[debug] In cluster 7: 58.221.44.93 [0.0,2051.0,2051.0,0.997562164798,1.0,1.0]
[debug] In cluster 7: 218.64.159.165 [0.0,391.2,4880.54810496,0.0,1.0,0.204487506374]
[debug] In cluster 7: 222.94.116.250 [0.0,1477.0,8112.41102757,6.77048070413E-4,1.0,0.0020297699594]
[debug] In cluster 7: 95.211.209.158 [0.0,1958.66666667,2101.60416667,1.0,1.0,5.10290865794E-4]
[debug] In cluster 7: 219.223.240.99 [0.0,102.529411765,2226.42541924,0.00229489386116,1.0,0.0272727272727]
[debug] In cluster 7: 121.224.121.237 [0.0,309.4,4632.08746356,0.00129282482224,1.0,0.239690721649]
[debug] In cluster 7: 117.21.201.1 [0.0,227.0,4177.48717949,0.0352422907489,1.0,0.0701754385965]
[debug] =======================================
[debug] The index of anomalous cluster is 1
[debug] The points in this cluster are as follows: 
[debug] In cluster 1: 202.121.66.133 [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
[debug] =======================================
[debug] The index of anomalous cluster is 3
[debug] The points in this cluster are as follows: 
[debug] In cluster 3: 2.17.49.71 [0.0,2167.0,44379.756371,1.02548325899E-4,1.0,5.12505125051E-4]
[debug] In cluster 3: 202.119.25.74 [0.0,2719.42857143,56915.8978289,1.05064089094E-4,1.0,0.00105025468676]
[debug] =======================================
[debug] The index of anomalous cluster is 6
[debug] The points in this cluster are as follows: 
[debug] In cluster 6: 221.130.188.246 [0.0,1104.75,14977.9347826,0.0,1.0,0.0153741804205]
[debug] In cluster 6: 58.205.220.24 [0.0,740.8,24918.5302083,8.9992800576E-5,1.0,0.00134807225667]
[debug] In cluster 6: 111.161.50.19 [0.0,389.75,11735.2325581,0.00128287363695,1.0,0.00383877159309]
[debug] In cluster 6: 110.210.27.245 [0.0,1085.5,12844.5152587,1.31604922024E-4,1.0,0.065183091184]
[debug] In cluster 6: 180.153.93.66 [0.0,464.0,16578.96875,0.00431034482759,1.0,0.00430107526882]
[debug] In cluster 6: 101.4.56.88 [0.0,6250.0,13220.6344569,2.63529411765E-4,1.0,0.00987136175859]
[debug] =======================================
```

##### k=6, iterationNum=100, runtimes=3

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/trainSet.txt /tcpheader/dataSet.txt 6 100 3 2>&1 | grep "debug"
[debug] The threshold is 536.235
[debug] Cluster:2 Number:3
[debug] Cluster:5 Number:1
[debug] Cluster:4 Number:3
[debug] Cluster:1 Number:1
[debug] Cluster:3 Number:3
[debug] Cluster:0 Number:107236
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 6
[debug] Center Point of Cluster 0:
[debug] [2.688360252154127,3.567899059888344,6.871488661956645,0.25097648869805605,1.0,0.742257557176031]
[debug] Center Point of Cluster 1:
[debug] [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
[debug] Center Point of Cluster 2:
[debug] [59805.666666666664,0.692724642054,0.3694288765801666,0.26462213827939995,1.0,0.5283086461553332]
[debug] Center Point of Cluster 3:
[debug] [0.0,1875.7428571433334,42071.39480273333,9.920173852299998E-5,1.0,9.702773561603333E-4]
[debug] Center Point of Cluster 4:
[debug] [99913.66666666666,3.880962557463333,3.4387146794756664,0.5870692550026666,1.0,0.34104775356533334]
[debug] Center Point of Cluster 5:
[debug] [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] =======================================
[debug] The index of anomalous cluster is 2
[debug] The points in this cluster are as follows: 
[debug] In cluster 2: 202.121.64.130 [38369.0,0.954158302147,0.0888580369825,0.0397530693172,1.0,0.335750914202]
[debug] In cluster 2: 202.121.244.201 [65657.0,0.637529137529,0.569460871648,0.254113345521,1.0,0.6128113879]
[debug] In cluster 2: 202.121.64.5 [75391.0,0.486486486486,0.44996772111,0.5,1.0,0.636363636364]
[debug] =======================================
[debug] The index of anomalous cluster is 5
[debug] The points in this cluster are as follows: 
[debug] In cluster 5: 202.121.209.11 [167053.0,1.25210084034,1.19340262088,0.590604026846,1.0,0.182835820896]
[debug] =======================================
[debug] The index of anomalous cluster is 4
[debug] The points in this cluster are as follows: 
[debug] In cluster 4: 202.121.244.200 [97841.0,0.544809228039,0.437771447757,0.42671009772,1.0,0.604824813326]
[debug] In cluster 4: 210.35.96.2 [95608.0,10.2487961477,8.7403233709,0.888018794049,1.0,0.122716894977]
[debug] In cluster 4: 210.35.96.6 [106292.0,0.849282296651,1.13804921977,0.446478873239,1.0,0.295601552393]
[debug] =======================================
[debug] The index of anomalous cluster is 1
[debug] The points in this cluster are as follows: 
[debug] In cluster 1: 202.121.66.133 [77690.0,84168.0181818,1398423.62123,0.99946881141,1.0,1.53371052532E-5]
[debug] =======================================
[debug] The index of anomalous cluster is 3
[debug] The points in this cluster are as follows: 
[debug] In cluster 3: 58.205.220.24 [0.0,740.8,24918.5302083,8.9992800576E-5,1.0,0.00134807225667]
[debug] In cluster 3: 2.17.49.71 [0.0,2167.0,44379.756371,1.02548325899E-4,1.0,5.12505125051E-4]
[debug] In cluster 3: 202.119.25.74 [0.0,2719.42857143,56915.8978289,1.05064089094E-4,1.0,0.00105025468676]
[debug] =======================================
[debug] [Info] There are 11 anomalous IPs among 5 clusters.
```


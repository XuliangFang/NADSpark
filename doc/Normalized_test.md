### Test after normalization

#### 测试结果：

> 参数

|              测试参数              | 参数值  |
| :----------------------------: | :--: |
|            K值（分类个数）            |  16  |
|      IterationNum（最大迭代次数）      |  50  |
|         runTimes（运行次数）         |  3   |
| maxAnamolousProportion（最大异常比例） |  1%  |

首先对测试数据做归一化（标准化处理），减少不同维度的量纲不一致带来的影响。运行K-Means算法，将21238个IP分为16个类，聚类结果中，小于最大异常比例的数据认定为异常。总共发现360个IP（分布于两个类，156+204）可能存在异常。

|      |      |      |      |      |      |      |      |      |      |      |      |
| ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
|      |      |      |      |      |      |      |      |      |      |      |      |

#### 测试说明：

* 数据集大小：原始数据共750311条记录，经过数据清洗（过滤掉只有上行记录或只有下行记录的数据；过滤掉无法统计SessionTime的数据）后，得到测试数据，共21238条记录

* 测试数据字段说明

  |     IP地址     | 报文平均间隔时间 | DNS次数 | 上下行包数比例 | 上下行字节比例 | SYN包占比 | PSH包占比 | 小数据包占比  |
  | :----------: | :------: | :---: | :-----: | :-----: | :----: | :----: | :-----: |
  | 106.11.47.10 |   5.0    |   0   |   0.8   | 0.8302  |  0.25  |  1.0   | 0.31875 |

#### 测试结果

##### k=15, 26 anomalous IPs

![trojan_result_mysql](../pics/trojan_result_mysql.png)



**k=15, 202 anomalous IPs but only shows meaningful records**

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/normalizeDataSet_v2.txt /tcpheader/trainSet_v2.txt 15 100 5 2>&1 | grep "debug"
[debug] Calculating some variables to normalize data...
[debug] Normalizing data...
[debug] Morlization Finished!
[debug] The threshold is 212.38
[debug] Cluster:8 Number:3027
[debug] Cluster:11 Number:242
[debug] Cluster:2 Number:3169
[debug] Cluster:5 Number:189
[debug] Cluster:14 Number:2485
[debug] Cluster:13 Number:235
[debug] Cluster:4 Number:1755
[debug] Cluster:7 Number:2489
[debug] Cluster:10 Number:863
[debug] Cluster:1 Number:519
[debug] Cluster:9 Number:345
[debug] Cluster:12 Number:475
[debug] Cluster:3 Number:1490
[debug] Cluster:6 Number:2074
[debug] Cluster:0 Number:1881
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 15
[debug] Center Point of Cluster 0:
[debug] [5.0566707756621725E-5,-7.209952626424E-8,-8.527607106415685E-7,-9.484771797945752E-9,-0.00787076113803181,0.0,0.007830664742548095]
[debug] Center Point of Cluster 1:
[debug] [-1.58919370937831E-5,-6.138378623814264E-8,7.641869967368956E-7,-7.44797627883756E-9,0.044884598590835406,0.0,4.585249753910091E-4]
[debug] Center Point of Cluster 2:
[debug] [-2.5591928228857103E-5,-6.9873371721832E-8,1.941879951611647E-6,4.9737491557955106E-8,-0.020054678486693272,0.0,-0.016261647162168245]
[debug] Center Point of Cluster 3:
[debug] [-4.707023099127402E-6,-6.858587549437691E-8,-7.580717992465063E-7,-9.425597873032971E-9,0.005190305444391787,0.0,-0.014481381701631081]
[debug] Center Point of Cluster 4:
[debug] [-5.297585006180546E-6,-1.1148072300517398E-9,-5.197478966548646E-7,-9.089084128487634E-9,0.01326889143647139,0.0,0.006233556065574792]
[debug] Center Point of Cluster 5:
[debug] [1.2039155726215224E-4,-7.481591691068398E-8,5.046202805741508E-6,-6.157421327849655E-9,0.1175373443908945,0.0,0.02090333792375121]
[debug] Center Point of Cluster 6:
[debug] [4.2965715362403076E-5,-7.581915635570181E-8,-1.326877423277623E-7,-8.858762207212534E-9,-0.01993701277446145,0.0,0.006526041165495562]
[debug] Center Point of Cluster 7:
[debug] [-2.7806324200365397E-5,-7.479141696992827E-8,-7.154337252583353E-7,-9.142164868555973E-9,-0.009691011340961187,0.0,-0.001741196128911193]
[debug] Center Point of Cluster 8:
[debug] [-2.7480340980950687E-5,-7.576010018180956E-8,-8.343738627189275E-7,-9.59875885963969E-9,0.0027769201241005436,0.0,0.0075843066951930185]
[debug] Center Point of Cluster 9:
[debug] [1.2633038602717354E-4,3.6950693846901383E-7,2.0455243124954967E-6,-7.628239130678869E-9,0.06705361654516225,0.0,0.016342993629982313]
[debug] Center Point of Cluster 10:
[debug] [3.089857302293635E-5,1.4960795974581252E-7,2.520427122939089E-7,-8.310443675367334E-9,0.027365454697179412,0.0,0.006875952720476969]
[debug] Center Point of Cluster 11:
[debug] [-6.389700393724749E-5,2.897016886032425E-6,1.0213724744732428E-6,-8.688929404988734E-9,0.12558123818423317,0.0,-0.011522479210463853]
[debug] Center Point of Cluster 12:
[debug] [1.6045512378893325E-4,-7.658832307263365E-8,1.5823379002005443E-6,-8.267185523180016E-9,0.04681913037945387,0.0,0.01850346115889642]
[debug] Center Point of Cluster 13:
[debug] [1.2822941158347378E-6,5.183631114179303E-7,6.93555486059392E-7,-8.660224450143287E-9,0.08451904356255598,0.0,0.0032288657697936334]
[debug] Center Point of Cluster 14:
[debug] [-3.2277745305513665E-5,1.4847270384600306E-8,-5.479071279742051E-7,-6.720403663398842E-9,-0.0242810893194913,0.0,-0.0029133379799639405]
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] =======================================
[debug] The index of anomalous cluster is 5
[debug] The points in this cluster are as follows: 
[debug] In cluster 5: 71.6.165.200
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS Query Times	 2.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.3	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 16.2851592852	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.90472027972	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.932189542484	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 198.20.69.98
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 12.0	-->>	7.760409190141052
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.8571428571	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 22.8926007731	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.848516949153	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.867	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 71.6.167.142
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS Query Times	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 17.0714285714	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 21.967920354	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.846025104603	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.891699604743	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 71.6.158.166
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 7.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 17.5063291139	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 22.8577586207	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.877078814172	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.919972640219	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 94.102.49.190
[debug] ******* Details ******* (data -->> means)
[debug] In cluster 5: 71.6.135.131
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 7.0	-->>	7.760409190141052
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.7755102041	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.9746514575	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.841849148418	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.888633754305	-->>	0.4035539130826372
[debug] 
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 8.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 20.0144927536	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 24.4615902965	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.922519913106	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.92275862069	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 89.248.172.16
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] In cluster 5: 94.102.49.193
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 3.85714285714	-->>	7.760409190141052
[debug] DNS Query Times	 3.0	-->>	42.29696769940672
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.0357142857	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.4917127072	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.872773536896	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.884798099762	-->>	0.4035539130826372
[debug] 
[debug] Up/Down Packages Number	 15.775	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.8727695888	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.876386687797	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.912071535022	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 93.174.95.106
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 21.3	-->>	7.760409190141052
[debug] DNS Query Times	 18.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.3666666667	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.1362383741	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.92343387471	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.930585683297	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 80.82.77.33
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS Query Times	 27.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 18.7723577236	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 21.5502188716	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.914248592464	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.939555921053	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 71.6.146.185
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 5.0	-->>	7.760409190141052
[debug] DNS Query Times	 7.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 13.4361702128	-->>	3.7452182364698317
[debug] In cluster 5: 80.82.77.139
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS Query Times	 25.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.0319148936	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.9940495868	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.897146648971	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.928794503435	-->>	0.4035539130826372
[debug] 
[debug] Up/Down Packages Size	 17.1892515661	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.89469517023	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.920412675018	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 66.240.192.138
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 2.5	-->>	7.760409190141052
[debug] DNS Query Times	 5.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.453125	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.1769268056	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.893637226971	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.92927484333	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 66.240.219.146
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.125	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 20.6940681924	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.869186046512	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.874087591241	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 71.6.146.186
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 2.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 18.5666666667	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 23.6710526316	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.887791741472	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.904599659284	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 89.248.167.131
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.8	-->>	7.760409190141052
[debug] DNS Query Times	 19.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 13.2435897436	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 11.6666666667	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.909970958374	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.90099009901	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 71.6.146.130
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 12.7837837838	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 14.8287269682	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.915433403805	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.935294117647	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 185.163.109.66
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS Query Times	 1.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.5	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 31.5234375	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.838709677419	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.863636363636	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 198.20.69.74
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 9.0	-->>	7.760409190141052
[debug] DNS Query Times	 6.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.2058823529	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.943537415	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.836438923395	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.852998065764	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 202.121.66.24
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 4.0	-->>	7.760409190141052
[debug] DNS Query Times	 27.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 0.973342447027	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 1.13332198587	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.823735955056	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.739868375476	-->>	0.4035539130826372
[debug] 
[debug] In cluster 5: 66.240.236.119
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.6	-->>	7.760409190141052
[debug] DNS Query Times	 6.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.8852459016	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.575	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.921568627451	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.938834951456	-->>	0.4035539130826372
[debug] 
[debug] =======================================
[debug] [Info] There are 189 anomalous IPs among 1 clusters.
[debug] Begin to optimize K-means model by choosing a K value that takes minimum cost.
[debug] Sum of squared distances of points to their nearest center when K=8 --->>> 1.5719346151811713
[debug] Sum of squared distances of points to their nearest center when K=9 --->>> 1.419600663492599
[debug] Sum of squared distances of points to their nearest center when K=10 --->>> 1.3137174084001124
[debug] Sum of squared distances of points to their nearest center when K=11 --->>> 1.1963166196239945
[debug] Sum of squared distances of points to their nearest center when K=12 --->>> 1.177393703855155
[debug] Sum of squared distances of points to their nearest center when K=13 --->>> 1.0042647514731717
[debug] Sum of squared distances of points to their nearest center when K=14 --->>> 0.9473115940071762
[debug] Sum of squared distances of points to their nearest center when K=15 --->>> 0.8399355430536072
[debug] Sum of squared distances of points to their nearest center when K=16 --->>> 0.7955712991860321
[debug] Sum of squared distances of points to their nearest center when K=17 --->>> 0.757644352132363
[debug] Sum of squared distances of points to their nearest center when K=18 --->>> 0.6950810283316726
[debug] Sum of squared distances of points to their nearest center when K=19 --->>> 0.6486530905079353
[debug] Sum of squared distances of points to their nearest center when K=20 --->>> 0.649752741108198
```

**k=4, proportion=0.05, **

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/normalizeDataSet_v2.txt /tcpheader/trainSet_v2.txt 4 100 5 0.05 2>&1 | grep "debug"
[debug] Calculating some variables to normalize data...
[debug] Normalizing data...
[debug] Morlization Finished!
[debug] The threshold is 1061.9
[debug] Cluster:2 Number:10521
[debug] Cluster:1 Number:624
[debug] Cluster:3 Number:2023
[debug] Cluster:0 Number:8070
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 4
[debug] Center Point of Cluster 0:
[debug] [-5.113249187809294E-6,-5.313057567224868E-8,-7.107058456424575E-7,-9.376674580625146E-9,0.004222780537757839,0.0,0.0036113713968711873]
[debug] Center Point of Cluster 1:
[debug] [2.3627515111992174E-5,1.2737653257993241E-6,2.321109684490624E-6,-7.804673709360071E-9,0.11148874170819693,0.0,0.004180316750373432]
[debug] Center Point of Cluster 2:
[debug] [-9.678160150680458E-6,-5.1778984904924855E-8,2.235750421230059E-7,9.198905839471351E-9,-0.01850155406769818,0.0,-0.004719916870586817]
[debug] Center Point of Cluster 3:
[debug] [6.246923022060349E-5,8.633664670956198E-8,9.337469811346069E-7,-8.029522650482654E-9,0.04438842521784186,0.0,0.008769019378378916]
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] =======================================
[debug] The index of anomalous cluster is 1
[debug] The points in this cluster are as follows: 
[debug] In cluster 1: 202.121.66.126
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 16.2916666667	-->>	7.760409190141052
[debug] DNS Query Times	 123.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 80.6583333333	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 11.7782664895	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.98532906292	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.0164302479845	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 71.6.165.200
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS Query Times	 2.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.3	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 16.2851592852	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.90472027972	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.932189542484	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 210.35.96.2
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.0	-->>	7.760409190141052
[debug] DNS Query Times	 67676.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 12.6536697248	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 11.3335707314	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.934384629328	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.0940702166975	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 210.35.96.6
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 9.0	-->>	7.760409190141052
[debug] DNS Query Times	 217851.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 1.16920152091	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 1.15260305055	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.839024390244	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.245398773006	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 198.20.69.98
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 12.0	-->>	7.760409190141052
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.8571428571	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 22.8926007731	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.848516949153	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.867	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 71.6.158.166
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 7.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 17.5063291139	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 22.8577586207	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.877078814172	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.919972640219	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 71.6.167.142
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS Query Times	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 17.0714285714	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 21.967920354	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.846025104603	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.891699604743	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 94.102.49.190
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 7.0	-->>	7.760409190141052
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.7755102041	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.9746514575	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.841849148418	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.888633754305	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 94.102.49.193
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 3.85714285714	-->>	7.760409190141052
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.0357142857	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.4917127072	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.872773536896	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.884798099762	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 93.174.95.106
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 21.3	-->>	7.760409190141052
[debug] DNS Query Times	 18.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.3666666667	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.1362383741	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.92343387471	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.930585683297	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 80.82.77.139
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS Query Times	 25.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.0319148936	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.9940495868	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.897146648971	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.928794503435	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 71.6.135.131
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 8.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 20.0144927536	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 24.4615902965	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.922519913106	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.92275862069	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 66.240.219.146
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.125	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 20.6940681924	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.869186046512	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.874087591241	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 202.121.209.11
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.0	-->>	7.760409190141052
[debug] DNS Query Times	 111766.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 3.56056338028	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 3.31199973095	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.973892405063	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.0512662137122	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 89.248.172.16
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS Query Times	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.775	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.8727695888	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.876386687797	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.912071535022	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 71.6.146.130
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS Query Times	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 12.7837837838	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 14.8287269682	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.915433403805	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.935294117647	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 80.82.77.33
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS Query Times	 27.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 18.7723577236	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 21.5502188716	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.914248592464	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.939555921053	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 202.121.244.200
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.0	-->>	7.760409190141052
[debug] DNS Query Times	 67715.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 0.874484384207	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 0.82115841526	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.685983827493	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.834014460861	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 71.6.146.185
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 5.0	-->>	7.760409190141052
[debug] DNS Query Times	 7.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 13.4361702128	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.1892515661	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.89469517023	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.920412675018	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 66.240.192.138
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 2.5	-->>	7.760409190141052
[debug] DNS Query Times	 5.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.453125	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.1769268056	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.893637226971	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.92927484333	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 71.6.146.186
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS Query Times	 2.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 18.5666666667	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 23.6710526316	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.887791741472	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.904599659284	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 89.248.167.131
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.8	-->>	7.760409190141052
[debug] DNS Query Times	 19.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 13.2435897436	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 11.6666666667	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.909970958374	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.90099009901	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 210.35.98.140
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.0	-->>	7.760409190141052
[debug] DNS Query Times	 32.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 2.54330708661	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 0.463200966166	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.665634674923	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.324444444444	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 185.163.109.66
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS Query Times	 1.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.5	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 31.5234375	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.838709677419	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.863636363636	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 198.20.69.74
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 9.0	-->>	7.760409190141052
[debug] DNS Query Times	 6.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.2058823529	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.943537415	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.836438923395	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.852998065764	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 202.121.66.24
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 4.0	-->>	7.760409190141052
[debug] DNS Query Times	 27.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 0.973342447027	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 1.13332198587	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.823735955056	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.739868375476	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 223.93.160.71
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 11.5	-->>	7.760409190141052
[debug] DNS Query Times	 1.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 1.04347826087	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 0.939960629921	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.708333333333	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.468085106383	-->>	0.4035539130826372
[debug] 
[debug] In cluster 1: 66.240.236.119
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.6	-->>	7.760409190141052
[debug] DNS Query Times	 6.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.8852459016	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.575	-->>	49.546375466172236
[debug] SYN Packages proportion	 0.921568627451	-->>	0.17065055011780997
[debug] PSH Packages proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.938834951456	-->>	0.4035539130826372
[debug] 
[debug] =======================================
[debug] [Info] There are 624 anomalous IPs among 1 clusters.
[debug] Begin to optimize K-means model by choosing a K value that takes minimum cost.
[debug] Sum of squared distances of points to their nearest center when K=8 --->>> 1.5704834325151467
[debug] Sum of squared distances of points to their nearest center when K=9 --->>> 1.4830114301440762
[debug] Sum of squared distances of points to their nearest center when K=10 --->>> 1.3344281396676858
[debug] Sum of squared distances of points to their nearest center when K=11 --->>> 1.1815552292668703
[debug] Sum of squared distances of points to their nearest center when K=12 --->>> 1.0980180818270295
[debug] Sum of squared distances of points to their nearest center when K=13 --->>> 1.0025247869555085
[debug] Sum of squared distances of points to their nearest center when K=14 --->>> 0.9335871384612366
[debug] Sum of squared distances of points to their nearest center when K=15 --->>> 0.88135774090665
[debug] Sum of squared distances of points to their nearest center when K=16 --->>> 0.8265689506308564
[debug] Sum of squared distances of points to their nearest center when K=17 --->>> 0.7435645405128073
[debug] Sum of squared distances of points to their nearest center when K=18 --->>> 0.712376359617301
[debug] Sum of squared distances of points to their nearest center when K=19 --->>> 0.6796333100821896
[debug] Sum of squared distances of points to their nearest center when K=20 --->>> 0.6327520355573425
```



**k=15, 166 anomalous IPs but only shows meaningful records**

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/normalizeDataSet_v2.txt /tcpheader/trainSet_v2.txt 15 100 3 2>&1 | grep "debug"
[debug] Calculating some variables to normalize data...
[debug] Normalizing data...
[debug] Morlization Finished!
[debug] The threshold is 212.38
[debug] Cluster:8 Number:1256
[debug] Cluster:11 Number:1619
[debug] Cluster:2 Number:462
[debug] Cluster:5 Number:2155
[debug] Cluster:14 Number:233
[debug] Cluster:13 Number:1055
[debug] Cluster:4 Number:225
[debug] Cluster:7 Number:166
[debug] Cluster:1 Number:247
[debug] Cluster:10 Number:3246
[debug] Cluster:9 Number:3657
[debug] Cluster:3 Number:295
[debug] Cluster:12 Number:1786
[debug] Cluster:6 Number:740
[debug] Cluster:0 Number:4096
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 15
[debug] Center Point of Cluster 0:
[debug] [-1.922303829282051E-5,-6.832775863076238E-8,-7.889799928602647E-7,-9.5438012964347E-9,0.005839551250349033,0.0,0.007763309047114593]
[debug] Center Point of Cluster 1:
[debug] [-6.44556492841941E-5,2.8368224486011484E-6,9.869562234574477E-7,-8.710426367269752E-9,0.12512053849878696,0.0,-0.011468208553013365]
[debug] Center Point of Cluster 2:
[debug] [1.4744085510676901E-4,-7.658832307263369E-8,2.0777726525098945E-6,-7.701275332765197E-9,0.05987421948450896,0.0,0.018290015483580318]
[debug] Center Point of Cluster 3:
[debug] [8.81270616295372E-5,3.3475393987259047E-7,-7.200712042731676E-7,-9.440038284654297E-9,0.027314430738120232,0.0,-0.012444881017610024]
[debug] Center Point of Cluster 4:
[debug] [-3.102136726482781E-6,7.249066749875144E-7,-1.5082280115644825E-7,-9.058746878261623E-9,0.06278423808595274,0.0,-0.002499646604896397]
[debug] Center Point of Cluster 5:
[debug] [-5.262174466004494E-5,-6.558770615575289E-8,3.324545173262199E-6,7.818349733464761E-8,-0.024351803649762697,0.0,-0.015775996984889032]
[debug] Center Point of Cluster 6:
[debug] [4.1975533600266705E-5,-4.655268176134278E-8,1.3224062190825946E-6,-7.377587229910969E-9,0.040803727630960934,0.0,0.01038111304209456]
[debug] Center Point of Cluster 7:
[debug] [1.234290291778324E-4,-7.457034256294401E-8,5.482565966115527E-6,-5.889724500288795E-9,0.11978922850718522,0.0,0.02130244529918902]
[debug] Center Point of Cluster 8:
[debug] [-1.3941538293386298E-5,-6.710939026448901E-8,-6.090727774437758E-7,-9.166117259803185E-9,0.0048958502338269745,0.0,-0.01334640556487631]
[debug] Center Point of Cluster 9:
[debug] [-2.6015026554554017E-5,-7.330764515470131E-8,-8.355220154377945E-7,-9.392492747273669E-9,-0.0077198457042211574,0.0,0.00210881968840368]
[debug] Center Point of Cluster 10:
[debug] [-1.595415378711384E-5,-6.477814159587081E-9,-5.591124970373438E-7,-7.482551718120736E-9,-0.023013348376068228,0.0,-0.0011294426760994137]
[debug] Center Point of Cluster 11:
[debug] [1.3581227066645833E-5,-7.642279632051184E-8,-7.241213104729402E-7,-9.178517457461103E-9,-0.011990802099302781,0.0,-0.01508870301189038]
[debug] Center Point of Cluster 12:
[debug] [9.827457029294292E-5,-7.610747407118475E-8,-1.94699530275232E-7,-9.145513207976918E-9,-0.01706080882060415,0.0,0.009182696804321088]
[debug] Center Point of Cluster 13:
[debug] [1.3718306882362592E-5,6.802795094373016E-8,1.5517988565280917E-8,-8.51508387842711E-9,0.022621234014867795,0.0,0.0070655940958176495]
[debug] Center Point of Cluster 14:
[debug] [5.932503899021198E-5,4.499062328418457E-7,1.5499794664592454E-6,-8.14961798300242E-9,0.0876712189035336,0.0,0.011484497074149915]
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] =======================================
[debug] The index of anomalous cluster is 7
[debug] The points in this cluster are as follows: 
[debug] In cluster 7: 71.6.165.200
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS	 2.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.3	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 16.2851592852	-->>	49.546375466172236
[debug] SYN proportion	 0.90472027972	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.932189542484	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 71.6.158.166
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS	 7.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 17.5063291139	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 22.8577586207	-->>	49.546375466172236
[debug] SYN proportion	 0.877078814172	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.919972640219	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 198.20.69.98
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 12.0	-->>	7.760409190141052
[debug] DNS	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.8571428571	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 22.8926007731	-->>	49.546375466172236
[debug] SYN proportion	 0.848516949153	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.867	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 71.6.167.142
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 17.0714285714	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 21.967920354	-->>	49.546375466172236
[debug] SYN proportion	 0.846025104603	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.891699604743	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 94.102.49.190
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 7.0	-->>	7.760409190141052
[debug] DNS	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.7755102041	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.9746514575	-->>	49.546375466172236
[debug] SYN proportion	 0.841849148418	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.888633754305	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 94.102.49.193
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 3.85714285714	-->>	7.760409190141052
[debug] DNS	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.0357142857	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.4917127072	-->>	49.546375466172236
[debug] SYN proportion	 0.872773536896	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.884798099762	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 93.174.95.106
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 21.3	-->>	7.760409190141052
[debug] DNS	 18.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.3666666667	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.1362383741	-->>	49.546375466172236
[debug] SYN proportion	 0.92343387471	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.930585683297	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 71.6.135.131
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS	 8.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 20.0144927536	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 24.4615902965	-->>	49.546375466172236
[debug] SYN proportion	 0.922519913106	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.92275862069	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 89.248.172.16
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.775	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.8727695888	-->>	49.546375466172236
[debug] SYN proportion	 0.876386687797	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.912071535022	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 80.82.77.139
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS	 25.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.0319148936	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.9940495868	-->>	49.546375466172236
[debug] SYN proportion	 0.897146648971	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.928794503435	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 66.240.219.146
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS	 3.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.125	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 20.6940681924	-->>	49.546375466172236
[debug] SYN proportion	 0.869186046512	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.874087591241	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 80.82.77.33
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS	 27.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 18.7723577236	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 21.5502188716	-->>	49.546375466172236
[debug] SYN proportion	 0.914248592464	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.939555921053	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 71.6.146.130
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.0	-->>	7.760409190141052
[debug] DNS	 4.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 12.7837837838	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 14.8287269682	-->>	49.546375466172236
[debug] SYN proportion	 0.915433403805	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.935294117647	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 71.6.146.185
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 5.0	-->>	7.760409190141052
[debug] DNS	 7.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 13.4361702128	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.1892515661	-->>	49.546375466172236
[debug] SYN proportion	 0.89469517023	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.920412675018	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 66.240.192.138
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 2.5	-->>	7.760409190141052
[debug] DNS	 5.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 16.453125	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 19.1769268056	-->>	49.546375466172236
[debug] SYN proportion	 0.893637226971	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.92927484333	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 71.6.146.186
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.833333333333	-->>	7.760409190141052
[debug] DNS	 2.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 18.5666666667	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 23.6710526316	-->>	49.546375466172236
[debug] SYN proportion	 0.887791741472	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.904599659284	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 89.248.167.131
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.8	-->>	7.760409190141052
[debug] DNS	 19.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 13.2435897436	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 11.6666666667	-->>	49.546375466172236
[debug] SYN proportion	 0.909970958374	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.90099009901	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 185.163.109.66
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 1.2	-->>	7.760409190141052
[debug] DNS	 1.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.5	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 31.5234375	-->>	49.546375466172236
[debug] SYN proportion	 0.838709677419	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.863636363636	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 198.20.69.74
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 9.0	-->>	7.760409190141052
[debug] DNS	 6.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 14.2058823529	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 18.943537415	-->>	49.546375466172236
[debug] SYN proportion	 0.836438923395	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.852998065764	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 202.121.66.24
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 4.0	-->>	7.760409190141052
[debug] DNS	 27.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 0.973342447027	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 1.13332198587	-->>	49.546375466172236
[debug] SYN proportion	 0.823735955056	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.739868375476	-->>	0.4035539130826372
[debug] 
[debug] In cluster 7: 66.240.236.119
[debug] ******* Details ******* (data -->> means)
[debug] IntervalTime	 0.6	-->>	7.760409190141052
[debug] DNS	 6.0	-->>	42.29696769940672
[debug] Up/Down Packages Number	 15.8852459016	-->>	3.7452182364698317
[debug] Up/Down Packages Size	 17.575	-->>	49.546375466172236
[debug] SYN proportion	 0.921568627451	-->>	0.17065055011780997
[debug] PSH proportion	 1.0	-->>	1.0
[debug] Small Packages proportion	 0.938834951456	-->>	0.4035539130826372
[debug] 
[debug] =======================================
[debug] [Info] There are 166 anomalous IPs among 1 clusters.
[debug] Begin to optimize K-means model by choosing a K value that takes minimum cost.
[debug] Sum of squared distances of points to their nearest center when K=8 --->>> 1.571337714375117
[debug] Sum of squared distances of points to their nearest center when K=9 --->>> 1.419638555970009
[debug] Sum of squared distances of points to their nearest center when K=10 --->>> 1.2980034792340733
[debug] Sum of squared distances of points to their nearest center when K=11 --->>> 1.232168897811213
[debug] Sum of squared distances of points to their nearest center when K=12 --->>> 1.0733329639147824
[debug] Sum of squared distances of points to their nearest center when K=13 --->>> 1.0220379707090406
[debug] Sum of squared distances of points to their nearest center when K=14 --->>> 0.9338126194213148
[debug] Sum of squared distances of points to their nearest center when K=15 --->>> 0.8918512006366202
[debug] Sum of squared distances of points to their nearest center when K=16 --->>> 0.8613669456550014
[debug] Sum of squared distances of points to their nearest center when K=17 --->>> 0.758069213552768
[debug] Sum of squared distances of points to their nearest center when K=18 --->>> 0.6958051736075939
[debug] Sum of squared distances of points to their nearest center when K=19 --->>> 0.6730375966721984
[debug] Sum of squared distances of points to their nearest center when K=20 --->>> 0.6362834055467342
```

> **历史测试结果，可忽略。**

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/normalizeDataSet_v2.txt /tcpheader/trainSet_v2.txt 16 50 3 2>&1 | grep "debug"
[debug] Calculating some variables to normalize data...
[debug] Normalizing data...
[debug] Morlization Finished!
[debug] The threshold is 212.38
[debug] Cluster:8 Number:287
[debug] Cluster:11 Number:313
[debug] Cluster:2 Number:762
[debug] Cluster:5 Number:863
[debug] Cluster:14 Number:2576
[debug] Cluster:13 Number:156
[debug] Cluster:4 Number:2401
[debug] Cluster:7 Number:1131
[debug] Cluster:10 Number:1760
[debug] Cluster:1 Number:204
[debug] Cluster:9 Number:3010
[debug] Cluster:3 Number:2074
[debug] Cluster:12 Number:246
[debug] Cluster:6 Number:1515
[debug] Cluster:15 Number:918
[debug] Cluster:0 Number:3022
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 16
[debug] Center Point of Cluster 0:
[debug] [-2.5587166225280306E-5,-6.940178833401832E-8,2.0831112641393414E-6,5.268400807183595E-8,-0.020638595425094903,0.0,-0.016249098370355597]
[debug] Center Point of Cluster 1:
[debug] [1.0831062091889609E-4,-7.494624089318028E-8,4.803252064173971E-6,-6.281196935617725E-9,0.11616323278878361,0.0,0.02022977181547362]
[debug] Center Point of Cluster 2:
[debug] [7.919452452060357E-5,-6.649624464962048E-8,1.5272908875454996E-6,-7.715765530853872E-9,0.05161809717640132,0.0,0.0132198508401513]
[debug] Center Point of Cluster 3:
[debug] [3.6368783838397795E-5,-7.591104098618639E-8,-1.4050966143060785E-7,-8.807895226410588E-9,-0.020305697662868095,0.0,0.006253448095938931]
[debug] Center Point of Cluster 4:
[debug] [-3.108294036572511E-5,1.818776195351782E-8,-5.338242325533612E-7,-6.66248875308919E-9,-0.024300720849971733,0.0,-0.003126643733351781]
[debug] Center Point of Cluster 5:
[debug] [2.6847771905118033E-5,-2.627608937781546E-8,6.036228433745824E-7,-7.95500868468797E-9,0.03204850769566601,0.0,0.008559349650879566]
[debug] Center Point of Cluster 6:
[debug] [-1.3817805785730492E-5,-6.871834298989496E-8,-7.562137487400034E-7,-9.37738933081722E-9,0.0016648576966281597,0.0,-0.014536739243374475]
[debug] Center Point of Cluster 7:
[debug] [-3.1665514314073964E-5,-5.630624823562224E-8,-2.0985058442247377E-7,-8.504585936212134E-9,0.014553238844144212,0.0,0.0026270926605280382]
[debug] Center Point of Cluster 8:
[debug] [1.342530936288134E-4,3.506363371677194E-7,2.1297373588936218E-6,-7.722429135812431E-9,0.07593727695318475,0.0,0.01707261724776928]
[debug] Center Point of Cluster 9:
[debug] [-2.9919087225424318E-5,-7.572266252442372E-8,-8.409275312004357E-7,-9.582715926002637E-9,0.0026044055771011466,0.0,0.007169185754476697]
[debug] Center Point of Cluster 10:
[debug] [6.138932561080368E-5,-7.184569433787978E-8,-8.402223057709758E-7,-9.508289127262794E-9,-0.008118451309464425,0.0,0.008388209080840014]
[debug] Center Point of Cluster 11:
[debug] [8.061375583132095E-5,3.1109847427190657E-7,-7.263763802238725E-7,-9.449657598725882E-9,0.027250158167646945,0.0,-0.012927537842603667]
[debug] Center Point of Cluster 12:
[debug] [-6.380395700265555E-5,2.848665581819335E-6,9.905705542884084E-7,-8.708648215281905E-9,0.12520121278880095,0.0,-0.011460251599931609]
[debug] Center Point of Cluster 13:
[debug] [-8.570570631414565E-6,1.0202256182617873E-6,-4.0324419153939647E-7,-9.459162041929732E-9,0.07705862361791489,0.0,-0.005936173690132498]
[debug] Center Point of Cluster 14:
[debug] [-2.779963520099112E-5,-7.478884574357073E-8,-7.534082850617351E-7,-9.198469536608552E-9,-0.009908002407422595,0.0,-0.001197950451669281]
[debug] Center Point of Cluster 15:
[debug] [3.700423366211072E-5,7.595272839645246E-8,-6.670692585194317E-7,-9.615810309584938E-9,0.012869893161117847,0.0,0.011568149832918182]
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] =======================================
[debug] The index of anomalous cluster is 13
[debug] The points in this cluster are as follows: 
[debug] In cluster 13: 210.35.112.1 [-8.558256580572115E-5,1.5622427402091204E-5,-8.78591038973899E-7,-9.760945841263417E-9,0.07572055123818275,0.0,-0.006450008303577511]
[debug] In cluster 13: 113.160.244.189 [-9.774759100737036E-5,-7.658832307263357E-8,-1.789023328591935E-7,-9.7997172169316E-9,0.05976734430753897,0.0,-0.011788786539155372]
[debug] In cluster 13: 124.93.55.108 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.017299639395394E-6,-9.972991865913689E-9,0.0646976439639165,0.0,-0.003700327045380202]
[debug] In cluster 13: 180.97.76.4 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.1690711933781851E-6,-9.996702508929537E-9,0.08337401087400864,0.0,-0.004119510017604946]
[debug] In cluster 13: 202.120.54.44 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.148357958141008E-7,-9.743423321739957E-9,0.0973812860565357,0.0,-8.208624746770479E-4]
[debug] In cluster 13: 49.65.250.238 [-8.719479806136109E-5,-7.658832307263357E-8,-9.91667110278352E-7,-9.868606977939208E-9,0.07216819072788612,0.0,-0.0024154401087726094]
[debug] In cluster 13: 137.116.71.170 [-8.719479806136109E-5,-7.658832307263357E-8,1.0091210587272552E-6,-8.551484134924783E-9,0.09703012874002284,0.0,-0.010600668880131725]
[debug] In cluster 13: 61.129.6.109 [2.987488003659761E-4,-7.658832307263357E-8,-1.1696285465426165E-6,-9.942681400277735E-9,0.0889264442797117,0.0,0.005630053187264331]
[debug] In cluster 13: 117.157.64.173 [-1.0009265610648067E-4,-7.658832307263357E-8,-9.147695229272267E-7,-9.80073880762327E-9,0.08337401087400864,0.0,0.0027355288548142533]
[debug] In cluster 13: 183.192.199.147 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.719597178468986E-7,-9.739304540633203E-9,0.09457983101996306,0.0,-4.939882715892422E-4]
[debug] In cluster 13: 114.252.87.78 [6.737788266006939E-4,-7.658832307263357E-8,-9.147695229272267E-7,-9.982794302086371E-9,0.07828045626216532,0.0,-0.009610806953731777]
[debug] In cluster 13: 112.253.38.181 [-6.139908197112193E-5,-7.658832307263357E-8,-1.1731872654545186E-6,-9.97068148611736E-9,0.07828045626216532,0.0,-0.012434702909914337]
[debug] In cluster 13: 219.228.252.126 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.719597178468986E-7,-9.755044695490684E-9,0.07216819072788612,0.0,-0.0021937341275591452]
[debug] In cluster 13: 123.150.183.11 [-9.686819159520077E-5,-7.658832307263357E-8,-2.483237658834737E-7,-9.671425297934192E-9,0.06936673569131346,0.0,-0.0012376270835814925]
[debug] In cluster 13: 111.37.2.151 [-9.364372708392087E-5,-7.658832307263357E-8,-7.689845135739057E-7,-9.604481435691044E-9,0.07728389122940241,0.0,0.004180312832393004]
[debug] In cluster 13: 210.35.112.50 [-9.122537870046096E-5,-7.658832307263357E-8,-7.959064839433457E-7,-9.74528015081703E-9,0.0865217131621792,0.0,-0.008912457883041494]
[debug] In cluster 13: 202.121.66.210 [-1.0009265610648067E-4,-7.658832307263357E-8,-2.694807740429053E-7,-9.734129963876192E-9,0.07171390072191453,0.0,-0.005956477978142721]
[debug] In cluster 13: 14.17.57.249 [5.46949889157448E-4,-7.658832307263357E-8,-9.42538096137272E-7,-9.819858311308997E-9,0.08592078817984625,0.0,-0.0033576905289515106]
[debug] In cluster 13: 58.50.120.27 [-1.0009265610648067E-4,-7.658832307263357E-8,8.489911263840274E-8,-9.005440498278146E-9,0.0973812860565357,0.0,-0.0023212150667763927]
[debug] In cluster 13: 117.184.207.146 [-3.0075712432992824E-5,-7.658832307263357E-8,-5.960345956455767E-7,-9.691311031284286E-9,0.07590346410987092,0.0,-0.01749144683150498]
[debug] In cluster 13: 180.84.88.192 [-8.977436967038501E-5,-7.658832307263357E-8,1.2511791874649705E-6,-6.920429205748942E-9,0.07216819072788612,0.0,-0.00474335291155734]
[debug] In cluster 13: 104.31.81.172 [9.337521457031298E-5,-7.658832307263357E-8,-8.175795166905685E-7,-8.63339524647243E-9,0.058070546027985184,0.0,-0.004882423027059183]
[debug] In cluster 13: 39.79.70.104 [-9.57933700914451E-5,-7.658832307263357E-8,-1.0550738928311922E-6,-9.975152535732309E-9,0.07828045626216532,0.0,-0.0030436070555874377]
[debug] In cluster 13: 177.33.228.99 [-1.0009265610648067E-4,-7.658832307263357E-8,-4.315963490705057E-7,-9.95356568478694E-9,0.06507879430899641,0.0,-0.01749144683150498]
[debug] In cluster 13: 210.35.68.111 [-8.719479806136109E-5,-7.658832307263357E-8,-9.613642474664437E-7,-9.839665101786535E-9,0.09386221692201749,0.0,-0.01265355970835227]
[debug] In cluster 13: 27.218.34.87 [-1.0009265610648067E-4,-7.658832307263357E-8,-4.7047235156583543E-7,-9.546776051310529E-9,0.06736569637964396,0.0,0.004180312832393004]
[debug] In cluster 13: 211.80.63.15 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.481249472228513E-7,-9.814345288124605E-9,0.08337401087400864,0.0,0.002210152862928031]
[debug] In cluster 13: 218.18.37.217 [-1.0009265610648067E-4,-7.658832307263357E-8,-6.926209372448648E-7,-9.7038861435027E-9,0.07216819072788612,0.0,-0.006655566999555988]
[debug] In cluster 13: 80.82.65.21 [0.0014175553072068889,-7.658832307263357E-8,2.114108495690247E-7,-9.604200555423507E-9,0.05653034728867971,0.0,-0.016235112937944673]
[debug] In cluster 13: 223.74.123.177 [-1.0009265610648067E-4,-7.658832307263357E-8,-5.074971158456737E-7,-9.501483931938974E-9,0.08897692094698584,0.0,-0.002545405683984646]
[debug] In cluster 13: 202.196.170.2 [-9.751308449745675E-5,-7.658832307263357E-8,-7.744651530229279E-7,-9.771503667301638E-9,0.08337401087400864,0.0,0.002295811992035203]
[debug] In cluster 13: 114.80.136.175 [-6.139908197112193E-5,-7.658832307263357E-8,-1.1268204456230875E-6,-9.949020786846292E-9,0.055359460508786394,0.0,-0.014601878876317137]
[debug] In cluster 13: 121.51.2.68 [-7.859622603123838E-5,-7.658832307263357E-8,-1.217699412492566E-6,-9.979683420705946E-9,0.07216819072788612,0.0,-0.012434702909914337]
[debug] In cluster 13: 66.249.69.201 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0258438157677411E-6,-1.001062434807734E-8,0.06936673569131346,0.0,-0.01749144683150498]
[debug] In cluster 13: 210.35.114.92 [-1.0009265610648067E-4,-7.658832307263357E-8,-6.336127191743934E-7,-9.686241133325336E-9,0.0995179890505261,0.0,0.0012431146361683147]
[debug] In cluster 13: 88.200.229.59 [-2.2705507835763208E-5,-7.658832307263357E-8,-6.926209372448648E-7,-9.7038861435027E-9,0.07216819072788612,0.0,-0.006655566999555988]
[debug] In cluster 13: 211.80.112.50 [-1.0009265610648067E-4,-7.658832307263357E-8,-5.996129450481427E-7,-9.569029139837786E-9,0.09048606390777084,0.0,-0.008539104393664643]
[debug] In cluster 13: 115.200.55.73 [-1.0009265610648067E-4,-7.658832307263357E-8,-9.147695229272267E-7,-9.909893100499726E-9,0.08337401087400864,0.0,0.004180312832393004]
[debug] In cluster 13: 202.121.66.94 [-1.0009265610648067E-4,-7.658832307263357E-8,-4.4433722383830274E-7,-9.888118147856609E-9,0.09123175061046622,0.0,-0.0014244525979433768]
[debug] In cluster 13: 43.224.214.34 [-1.0009265610648067E-4,-7.658832307263357E-8,-4.14935205144412E-7,-9.342333647008726E-9,0.08897692094698584,0.0,-0.0020116185001430864]
[debug] In cluster 13: 112.239.50.10 [3.0902082544759425E-6,-7.658832307263357E-8,-3.593980587229885E-7,-9.468136628152685E-9,0.07637037328266105,0.0,-0.01749144683150498]
[debug] In cluster 13: 182.246.11.229 [-1.0009265610648067E-4,-7.658832307263357E-8,3.083905019335291E-6,-5.497752812059568E-9,0.061824356746979246,0.0,-0.0051075841664328025]
[debug] In cluster 13: 60.211.157.2 [1.598806629959552E-5,-7.658832307263357E-8,-2.483237658834737E-7,-9.08232250541205E-9,0.08337401087400864,0.0,-0.012073506915530484]
[debug] In cluster 13: 36.25.46.228 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0813809621881649E-6,-9.961030542537676E-9,0.07637037328266105,0.0,-0.0012376270835814925]
[debug] In cluster 13: 123.151.137.104 [-9.794301309895644E-5,-7.658832307263357E-8,-1.0375358465933214E-6,-9.828250982206122E-9,0.07403582741887853,0.0,0.0016169864205443569]
[debug] In cluster 13: 101.94.202.116 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0813809621881649E-6,-9.992314554831677E-9,0.07216819072788612,0.0,-0.005933175010744941]
[debug] In cluster 13: 223.11.79.55 [6.866766846458135E-4,-7.658832307263357E-8,-7.481580836662885E-7,-9.65235500446154E-9,0.0646976439639165,0.0,-0.005933175010744941]
[debug] In cluster 13: 210.35.66.93 [-9.235394127940892E-5,-7.658832307263357E-8,-5.694294488239482E-7,-9.912357821847876E-9,0.08312388096004995,0.0,-0.0029570930449545007]
[debug] In cluster 13: 183.206.5.203 [-9.57933700914451E-5,-7.658832307263357E-8,-4.98240924774881E-7,-9.015661082718092E-9,0.0646976439639165,0.0,0.0025132543966885383]
[debug] In cluster 13: 182.34.127.120 [1.7936093487154006E-4,-7.658832307263357E-8,-9.443893343514305E-7,-9.85516668425233E-9,0.09020682803623337,0.0,-0.009931530669687157]
[debug] In cluster 13: 220.171.124.104 [-8.719479806136109E-5,-7.658832307263357E-8,-8.939430930196094E-7,-9.888116519183754E-9,0.09985315814758967,0.0,-0.004357047035216307]
[debug] In cluster 13: 210.35.68.250 [-9.57933700914451E-5,-7.658832307263357E-8,-9.80423101223414E-7,-9.836361628561177E-9,0.08570855673779117,0.0,-0.01454230015559318]
[debug] In cluster 13: 1.202.21.146 [1.598806629959552E-5,-7.658832307263357E-8,-1.140885047638357E-6,-9.995987834583308E-9,0.08337401087400864,0.0,-0.005776982148328595]
[debug] In cluster 13: 112.28.174.144 [-9.57933700914451E-5,-7.658832307263357E-8,-7.89810943481523E-7,-9.458249525837684E-9,0.07828045626216532,0.0,0.0053209317620855345]
[debug] In cluster 13: 124.163.220.170 [-3.5603365880882786E-5,-7.658832307263357E-8,-1.0536123889781196E-6,-9.937592378955723E-9,0.06736569637964396,0.0,7.584560433587622E-4]
[debug] In cluster 13: 210.35.68.32 [-8.719479806136109E-5,-7.658832307263357E-8,-8.229481075108286E-7,-8.648637407347163E-9,0.06678002633708109,0.0,-0.006401601065994684]
[debug] In cluster 13: 123.103.58.217 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0480586743359773E-6,-9.972662317524128E-9,0.055359460508786394,0.0,-0.014782476873517733]
[debug] In cluster 13: 202.120.37.168 [-1.0009265610648067E-4,-7.658832307263357E-8,-6.648523640358193E-7,-9.756300397663831E-9,0.09137816812119097,0.0,-0.0076406469842668035]
[debug] In cluster 13: 163.172.69.249 [-8.719479806136109E-5,-7.658832307263357E-8,1.2057397040313809E-6,-8.39916843910088E-9,0.07714855523731126,0.0,-0.0014731896886370527]
[debug] In cluster 13: 120.68.196.50 [-9.493351288843284E-5,-7.658832307263357E-8,-2.483237658834737E-7,-9.079735821059703E-9,0.08337401087400864,0.0,-0.0012376270835814925]
[debug] In cluster 13: 221.2.77.210 [-4.1466028628723035E-5,-7.658832307263357E-8,-5.289325056920712E-7,-9.895331284236553E-9,0.08200744744153007,0.0,-0.016769054842706934]
[debug] In cluster 13: 211.80.112.2 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.913536419933218E-7,-9.694702030489187E-9,0.07239533573078788,0.0,-0.007417464800239902]
[debug] In cluster 13: 140.207.135.43 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.058091191108772E-6,-9.915761212823057E-9,0.08548831656191187,0.0,-8.665353084981212E-4]
[debug] In cluster 13: 210.35.68.172 [-9.493351288843284E-5,-7.658832307263357E-8,-7.869383324581741E-7,-9.664850834095647E-9,0.0749958275872509,0.0,-0.014042776215913395]
[debug] In cluster 13: 204.93.180.6 [-6.999765400124465E-5,-7.658832307263357E-8,-2.6175180201112054E-8,-9.235306924262891E-9,0.09943829849597599,0.0,-0.017015144421309896]
[debug] In cluster 13: 210.35.68.174 [-4.5921652316978445E-5,-7.658832307263357E-8,-9.697456880705116E-7,-9.847576089267486E-9,0.08043700156145925,0.0,-0.014071682921238827]
[debug] In cluster 13: 117.185.24.121 [6.608809685555743E-4,-7.658832307263357E-8,-1.0056484897967384E-6,-9.890739339754246E-9,0.055359460508786394,0.0,-0.015210208972145929]
[debug] In cluster 13: 190.252.151.194 [-1.0009265610648067E-4,-7.658832307263357E-8,-2.483237658834737E-7,-9.459618487595693E-9,0.08337401087400864,0.0,0.004180312832393004]
[debug] In cluster 13: 112.10.128.138 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.296457015247033E-7,-9.692149566157336E-9,0.06736569637964396,0.0,0.0013535615718996467]
[debug] In cluster 13: 120.193.158.173 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.844765339703894E-7,-9.806940376932488E-9,0.06936673569131346,0.0,0.0032380624122141047]
[debug] In cluster 13: 154.59.123.106 [-9.751308449745675E-5,-7.658832307263357E-8,-1.0306731328480389E-6,-1.001171677241127E-8,0.06096237058176361,0.0,-0.0026634007456754817]
[debug] In cluster 13: 114.252.126.48 [-5.495015294856215E-5,-7.658832307263357E-8,-5.815466444053502E-7,-9.772129306889986E-9,0.07374400918592672,0.0,0.0023743328603870583]
[debug] In cluster 13: 61.160.224.20 [-9.57933700914451E-5,-7.658832307263357E-8,-3.149683415878491E-7,-9.675907003836525E-9,0.06016195485702857,0.0,-0.01749144683150498]
[debug] In cluster 13: 62.210.127.17 [-8.719479806136109E-5,-7.658832307263357E-8,6.949290410189059E-6,-4.451582696286872E-9,0.09020682803623337,0.0,-0.005301082020562365]
[debug] In cluster 13: 59.78.22.207 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.481249472228513E-7,-9.814345288124605E-9,0.08337401087400864,0.0,0.002210152862928031]
[debug] In cluster 13: 182.38.157.122 [-8.719479806136109E-5,-7.658832307263357E-8,-1.1591329671764917E-6,-9.984796400610449E-9,0.06936673569131346,0.0,-0.0030436070555874377]
[debug] In cluster 13: 202.121.244.241 [-9.57933700914451E-5,-7.658832307263357E-8,-8.671662545660065E-7,-9.800533308533934E-9,0.0973812860565357,0.0,0.0010843471661032874]
[debug] In cluster 13: 210.195.160.44 [-9.364372708392087E-5,-7.658832307263357E-8,-7.353418191082674E-7,-9.828828365412445E-9,0.08897692094698584,0.0,-0.0030436070555874377]
[debug] In cluster 13: 180.84.88.163 [-1.0009265610648067E-4,-7.658832307263357E-8,-2.959270342446939E-7,-9.88987914910802E-9,0.08057255583743599,0.0,-0.009464869178217457]
[debug] In cluster 13: 139.226.16.121 [-1.0009265610648067E-4,-7.658832307263357E-8,-5.198387039367317E-7,-9.529744384795262E-9,0.07387755312297759,0.0,1.4835754608438578E-4]
[debug] In cluster 13: 117.89.141.3 [-8.620265513485312E-5,-7.658832307263357E-8,-6.082044746871003E-7,-9.803323894207594E-9,0.08687582966959838,0.0,0.003289692572253595]
[debug] In cluster 13: 182.109.22.184 [0.0014476503093078686,-7.658832307263357E-8,-9.888190514875697E-7,-9.827272716795992E-9,0.06736569637964396,0.0,-0.01749144683150498]
[debug] In cluster 13: 202.119.79.13 [-9.149408407639664E-5,-7.658832307263357E-8,-5.149020687009748E-7,-9.43663654798974E-9,0.07828045626216532,0.0,0.0014713428744057558]
[debug] In cluster 13: 180.114.160.253 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.481580836662885E-7,-9.775257121103145E-9,0.055359460508786394,0.0,-0.008822742965945787]
[debug] In cluster 13: 106.39.93.66 [4.996577429915796E-4,-7.658832307263357E-8,-9.299160174056453E-7,-9.972813492970718E-9,0.09938232536837333,0.0,-0.007411558615733436]
[debug] In cluster 13: 139.162.73.19 [2.8885924344715094E-5,-7.658832307263357E-8,3.0296594344511124E-6,-6.674660620259468E-9,0.08337401087400864,0.0,-0.00547181542126492]
[debug] In cluster 13: 14.17.42.45 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.689845135739057E-7,-9.675433260174918E-9,0.08093796301603168,0.0,0.0036246266871437344]
[debug] In cluster 13: 183.61.46.199 [-1.0009265610648067E-4,-7.658832307263357E-8,-6.529515469471804E-7,-9.735225987825253E-9,0.0856151749031659,0.0,0.0014018821062333421]
[debug] In cluster 13: 163.172.69.250 [-8.719479806136109E-5,-7.658832307263357E-8,6.860430975905451E-6,-4.463845496197613E-9,0.09795692750235385,0.0,-0.007225876464402253]
[debug] In cluster 13: 179.209.161.53 [-9.57933700914451E-5,-7.658832307263357E-8,-3.7406824834587946E-7,-9.949881733380291E-9,0.07652153092859365,0.0,-0.01749144683150498]
[debug] In cluster 13: 60.191.52.254 [-5.7099795956125076E-5,-7.658832307263357E-8,3.2421858866331824E-6,-4.61141128983647E-9,0.06954864835614821,0.0,-0.012925031151026223]
[debug] In cluster 13: 125.88.222.249 [6.866766846458135E-4,-7.658832307263357E-8,-9.814140986316021E-7,-9.866582086582933E-9,0.055359460508786394,0.0,-0.01749144683150498]
[debug] In cluster 13: 27.155.136.84 [-1.0009265610648067E-4,-7.658832307263357E-8,1.6820483226887186E-7,-8.693622171819886E-9,0.08007818141915796,0.0,-0.0051075841664328025]
[debug] In cluster 13: 210.35.68.130 [-4.1131019328772743E-5,-7.658832307263357E-8,-9.163122214390255E-7,-9.79770476842983E-9,0.09640403429967385,0.0,-0.011859806269173436]
[debug] In cluster 13: 114.83.243.93 [4.178378238983467E-5,-7.658832307263357E-8,-1.0662344677097463E-6,-1.000867335920994E-8,0.055359460508786394,0.0,-0.01749144683150498]
[debug] In cluster 13: 138.204.194.206 [-3.237890136960289E-5,-7.658832307263357E-8,-5.260094979839268E-7,-9.608953628226895E-9,0.07475414922319688,0.0,0.0053209317620855345]
[debug] In cluster 13: 123.151.148.66 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.198329376284939E-6,-9.998380999752159E-9,0.07433705914317791,0.0,0.004996404200496289]
[debug] In cluster 13: 122.226.185.140 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.2314689529273586E-6,-1.0024783078300248E-8,0.06936673569131346,0.0,-0.015614365285811869]
[debug] In cluster 13: 123.151.10.158 [-9.728877392275677E-5,-7.658832307263357E-8,-1.067743726946138E-6,-9.849311658827715E-9,0.06862951068175102,0.0,0.002328453461581937]
[debug] In cluster 13: 106.41.94.48 [-1.0009265610648067E-4,-7.658832307263357E-8,-7.661008540495172E-7,-9.102779579863591E-9,0.0643002744551661,0.0,-0.01449283857612317]
[debug] In cluster 13: 210.35.113.44 [5.448002461494982E-4,1.3647515289723968E-6,-1.0259939161636548E-6,-9.93900894871305E-9,0.0705316409297499,0.0,-0.01278095892401398]
[debug] In cluster 13: 119.193.140.217 [-8.322622635520021E-5,-7.658832307263357E-8,-6.767531811244583E-7,-9.872207308081426E-9,0.07403582741887853,0.0,-7.623691962023818E-4]
[debug] In cluster 13: 202.121.66.11 [-1.0009265610648067E-4,1.5382627690879592E-4,-7.75926656875334E-7,-9.728850355667337E-9,0.06979519352045414,0.0,0.0021177591540163868]
[debug] In cluster 13: 210.35.115.32 [-3.5603365880882786E-5,-7.658832307263357E-8,-9.147695229272267E-7,-9.773754976317794E-9,0.06954089364207432,0.0,-0.010986069596681289]
[debug] In cluster 13: 210.35.115.34 [-8.558256580572115E-5,-7.658832307263357E-8,-8.307919829779094E-7,-9.768106270211986E-9,0.07525385134786701,0.0,-0.01163211700296108]
[debug] In cluster 13: 121.228.116.64 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.635044646918102E-7,-9.796317139894956E-9,0.07216819072788612,0.0,-0.008203549832679175]
[debug] In cluster 13: 211.36.151.105 [2.8885924344715094E-5,-7.658832307263357E-8,-1.0685646976294774E-6,-9.920860340143348E-9,0.06736569637964396,0.0,-1.5403910038659218E-4]
[debug] In cluster 13: 27.192.107.52 [-1.0009265610648067E-4,-7.658832307263357E-8,-6.767531811244583E-7,-9.721957960994934E-9,0.06936673569131346,0.0,-0.00380401967536801]
[debug] In cluster 13: 210.35.115.35 [-8.146241670803193E-5,-7.658832307263357E-8,-8.644485725810006E-7,-9.811729742269861E-9,0.07458864787943648,0.0,-0.013706310725823633]
[debug] In cluster 13: 210.35.98.140 [-1.0009265610648067E-4,-1.8645012437657463E-8,-4.0050429308383157E-7,-9.93130292458597E-9,0.08320054616587629,0.0,-0.0034288827829504413]
[debug] In cluster 13: 210.35.115.36 [-9.041926257264099E-5,-7.658832307263357E-8,-8.799051573674596E-7,-9.844820589627614E-9,0.06653466365988642,0.0,-0.01040565160679594]
[debug] In cluster 13: 168.235.195.1 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.592323765058031E-7,-9.859143817356314E-9,0.06736569637964396,0.0,0.0025132543966885383]
[debug] In cluster 13: 125.88.219.97 [-9.364372708392087E-5,-7.658832307263357E-8,-2.6872516661066294E-7,-9.6005728994469E-9,0.05886127930437614,0.0,-0.017266869011153665]
[debug] In cluster 13: 58.48.109.62 [-6.139908197112193E-5,-7.658832307263357E-8,-1.0056484897967384E-6,-9.877080500007938E-9,0.0973812860565357,0.0,-0.006085257534709723]
[debug] In cluster 13: 188.18.244.37 [-5.5083637756467715E-6,-7.658832307263357E-8,-7.481580836662885E-7,-9.737445557960452E-9,0.0646976439639165,0.0,-1.5403910038659218E-4]
[debug] In cluster 13: 101.94.47.234 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.6501804625300462E-7,-9.857058951905085E-9,0.06505680486590759,0.0,-0.006655566999555988]
[debug] In cluster 13: 27.21.7.215 [-9.364372708392087E-5,-7.658832307263357E-8,-8.03695230087712E-7,-9.844728695276725E-9,0.06936673569131346,0.0,-0.0051075841664328025]
[debug] In cluster 13: 210.35.112.32 [-9.57933700914451E-5,-7.658832307263357E-8,-8.671662545660065E-7,-9.7884298878915E-9,0.0967976495906321,0.0,-0.005050251468898484]
[debug] In cluster 13: 39.130.136.48 [-4.850122392600236E-5,-7.658832307263357E-8,-8.92554664357991E-7,-8.888667544140021E-9,0.055359460508786394,0.0,-0.009102378574502424]
[debug] In cluster 13: 62.210.127.77 [-6.56983679861188E-5,-7.658832307263357E-8,6.860430975905451E-6,-4.134564292964277E-9,0.07032613810106639,0.0,-0.013499280577628807]
[debug] In cluster 13: 179.218.193.237 [-9.57933700914451E-5,-7.658832307263357E-8,2.1508809594740366E-6,-9.457198143674076E-9,0.08996566978337381,0.0,-0.016717455414939054]
[debug] In cluster 13: 191.81.25.111 [-2.2705507835763208E-5,-7.658832307263357E-8,-9.147695229272267E-7,-9.830695157437725E-9,0.07637037328266105,0.0,0.0014713428744057558]
[debug] In cluster 13: 119.146.74.48 [-9.686819159520077E-5,-7.658832307263357E-8,-6.804721864665323E-7,-8.815590526244007E-9,0.0746355272738815,0.0,-0.0014568500859529437]
[debug] In cluster 13: 218.28.22.18 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.057579328007888E-6,-9.986121642110676E-9,0.07637037328266105,0.0,-0.003700327045380202]
[debug] In cluster 13: 180.115.178.76 [-1.0009265610648067E-4,-7.658832307263357E-8,6.40270576842641E-7,-8.592864344108228E-9,0.09985315814758967,0.0,-0.004488391033166191]
[debug] In cluster 13: 112.64.216.149 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.713056692065472E-7,-9.973766438565598E-9,0.09414883793743929,0.0,-0.007761269023240175]
[debug] In cluster 13: 124.10.19.131 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0359414787532425E-6,-9.93681533487277E-9,0.06736569637964396,0.0,0.001772339536385075]
[debug] In cluster 13: 60.12.168.170 [-8.07458690388013E-5,-7.658832307263357E-8,-1.0293148874191218E-6,-1.0016276499229634E-8,0.06736569637964396,0.0,-0.01749144683150498]
[debug] In cluster 13: 210.35.66.178 [-9.493351288843284E-5,-7.658832307263357E-8,-8.440858820290507E-7,-9.78713662310184E-9,0.10158346861131065,0.0,-5.696618884660983E-4]
[debug] In cluster 13: 177.183.168.59 [-1.0009265610648067E-4,-7.658832307263357E-8,-3.023599083457602E-7,-9.611276086630484E-9,0.09618066246943315,0.0,-0.004366296612544046]
[debug] In cluster 13: 189.100.55.203 [-9.57933700914451E-5,-7.658832307263357E-8,9.600654192135451E-8,-9.711021945904177E-9,0.0866153638087416,0.0,-0.004861547159821585]
[debug] In cluster 13: 123.125.112.76 [3.318521035932503E-5,-7.658832307263357E-8,-1.1845560985358365E-6,-1.0013646242478804E-8,0.06791770722417868,0.0,-0.008010051978549612]
[debug] In cluster 13: 204.13.201.139 [-9.364372708392087E-5,-7.658832307263357E-8,-2.483237658834737E-7,-9.951063631842263E-9,0.07536985362682629,0.0,-0.015943463998368792]
[debug] In cluster 13: 178.218.207.28 [2.997409432922262E-4,-7.658832307263357E-8,3.4171278978571674E-6,-7.167792872317452E-9,0.06736569637964396,0.0,-1.5403910038659218E-4]
[debug] In cluster 13: 103.240.125.55 [-5.1725688437282256E-5,-7.658832307263357E-8,-4.4825749299659967E-7,-9.570580309925202E-9,0.06936673569131346,0.0,-0.01749144683150498]
[debug] In cluster 13: 223.93.160.71 [4.823271141239446E-5,-7.477759461529056E-8,-9.002815716870003E-7,-9.834837186760188E-9,0.09037764846518813,0.0,0.002797009024060012]
[debug] In cluster 13: 219.154.180.50 [-9.825010495717971E-5,-7.658832307263357E-8,-6.215333898279754E-7,-9.80586826361851E-9,0.08218190234775744,0.0,0.001772339536385075]
[debug] In cluster 13: 124.239.229.48 [2.8885924344715094E-5,-7.658832307263357E-8,-8.115173633850961E-7,-8.802984729720898E-9,0.0653001074125153,0.0,-3.1261295156999E-4]
[debug] In cluster 13: 209.126.136.5 [-9.149408407639664E-5,-7.658832307263357E-8,-4.439111076248649E-7,-9.826646659298617E-9,0.08261686086405598,0.0,0.004525404546765026]
[debug] In cluster 13: 106.37.84.120 [-5.2800509940999227E-5,-7.658832307263357E-8,-1.3724947304395893E-7,-9.094489502180118E-9,0.07216819072788612,0.0,-8.208624746770479E-4]
[debug] In cluster 13: 182.254.221.48 [-1.0009265610648067E-4,-7.658832307263357E-8,-5.815466444053502E-7,-9.59522192158338E-9,0.06936673569131346,0.0,-0.01749144683150498]
[debug] In cluster 13: 106.39.205.50 [9.833274196835637E-4,-7.658832307263357E-8,-9.147695229272267E-7,-9.817068598924437E-9,0.055359460508786394,0.0,-0.01749144683150498]
[debug] In cluster 13: 182.89.190.249 [6.113056945751404E-5,-7.658832307263357E-8,-4.7047235156583543E-7,-9.540532148614914E-9,0.06736569637964396,0.0,-0.01749144683150498]
[debug] In cluster 13: 210.35.68.196 [-9.364372708392087E-5,-7.658832307263357E-8,-9.422761284654822E-7,-9.752057508646129E-9,0.08020526995866173,0.0,-0.011840262269702976]
[debug] In cluster 13: 210.35.112.118 [-9.892012355692422E-5,-7.658832307263357E-8,-8.485814169182241E-7,-9.765851082155966E-9,0.08129292998973114,0.0,-0.006419270554606859]
[debug] In cluster 13: 202.120.60.21 [-9.256890558016522E-5,-7.658832307263357E-8,-9.21104558640535E-7,-9.998346148939258E-9,0.07751050033233323,0.0,-0.010836012001884021]
[debug] In cluster 13: 123.151.148.33 [6.763583982097179E-4,-7.658832307263357E-8,-7.481580836662885E-7,-9.720151963313298E-9,0.10018274109310836,0.0,-0.005355261419722109]
[debug] In cluster 13: 181.23.22.163 [-4.850122392600236E-5,-7.658832307263357E-8,4.1812199116027924E-7,-8.943791187884941E-9,0.07216819072788612,0.0,0.004180312832393004]
[debug] In cluster 13: 27.205.81.207 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.2198892671159297E-6,-1.0024356211142337E-8,0.06736569637964396,0.0,-0.01508347353551439]
[debug] In cluster 13: 106.184.3.122 [-9.041926257264099E-5,-7.658832307263357E-8,1.9242894020791606E-6,-7.688941125492381E-9,0.08078443058815057,0.0,-0.014195361711521618]
[debug] In cluster 13: 125.39.133.144 [4.178378238983467E-5,-7.658832307263357E-8,-7.851828479461267E-7,-9.828950266735261E-9,0.07216819072788612,0.0,-0.012451502723640881]
[debug] In cluster 13: 121.234.86.39 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0813809621881649E-6,-9.919415621679854E-9,0.06936673569131346,0.0,-6.356337595795114E-4]
[debug] In cluster 13: 183.198.222.118 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.17123266225355E-8,-9.134591519835607E-9,0.09137816812119097,0.0,-0.0030436070555874377]
[debug] In cluster 13: 210.35.68.13 [-9.865956076813548E-5,-7.658832307263357E-8,-9.345160638765848E-7,-9.67981257271106E-9,0.08646222902448704,0.0,-0.005993833880026808]
[debug] In cluster 13: 223.74.26.70 [-9.57933700914451E-5,-7.658832307263357E-8,-4.7047235156583543E-7,-9.687798250065524E-9,0.08337401087400864,0.0,-0.007377958988367036]
[debug] In cluster 13: 122.190.248.98 [-5.495015294856215E-5,-7.658832307263357E-8,-5.398937845901157E-7,-9.611681299309076E-9,0.07019069305511019,0.0,0.0050471832189489235]
[debug] =======================================
[debug] The index of anomalous cluster is 1
[debug] The points in this cluster are as follows: 
[debug] In cluster 1: 221.200.86.173 [-1.4106935805640495E-5,-7.658832307263357E-8,1.667707785617316E-6,-8.335892688668289E-9,0.115390639862738,0.0,0.023629327915380596]
[debug] In cluster 1: 202.121.66.31 [-1.9481043324483316E-5,-7.658832307263357E-8,-1.1960081364495593E-6,-9.982962347745674E-9,0.09586970959805008,0.0,0.02433715337414054]
[debug] In cluster 1: 123.196.124.249 [-8.166714461347113E-5,-7.658832307263357E-8,3.028367872903871E-6,-7.314563258525927E-9,0.11975654381574655,0.0,0.022196594962603695]
[debug] In cluster 1: 60.2.127.118 [-4.850122392600236E-5,-7.658832307263357E-8,1.4415922609065186E-6,-8.462921208338277E-9,0.12601562735900368,0.0,0.021756621851161295]
[debug] In cluster 1: 71.6.165.200 [-8.719479806136109E-5,-7.296686615794755E-8,3.517094761413731E-6,-6.7299480300185926E-9,0.12338780046890699,0.0,0.022912928620319555]
[debug] In cluster 1: 182.37.126.162 [-1.0009265610648067E-4,-7.658832307263357E-8,1.667707785617316E-6,-8.394855593396818E-9,0.12499562855939043,0.0,0.01807246646310462]
[debug] In cluster 1: 221.203.78.174 [2.8885924344715094E-5,-7.658832307263357E-8,1.7745711674297856E-5,1.7420892802707997E-9,0.11876081133519159,0.0,0.02286286426680426]
[debug] In cluster 1: 202.121.66.35 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0904628543182389E-6,-9.916377873072876E-9,0.1013456092213543,0.0,0.022760273674792175]
[debug] In cluster 1: 114.222.101.211 [-1.0009265610648067E-4,-7.658832307263357E-8,1.9024784500319712E-6,-8.28566214470721E-9,0.131321991306628,0.0,0.021706170647547172]
[debug] In cluster 1: 172.82.166.210 [-4.276884257267321E-5,-7.658832307263357E-8,6.960446449682309E-6,-5.087673573479003E-9,0.13416974723225006,0.0,0.025387723896195032]
[debug] In cluster 1: 81.182.35.144 [2.997409432922262E-4,-7.658832307263357E-8,8.415471075685314E-6,-4.292166297710281E-9,0.0988303145236705,0.0,0.02296250454109014]
[debug] In cluster 1: 111.40.30.206 [6.866766846458135E-4,-7.658832307263357E-8,1.7621209345307372E-6,-9.23053406196966E-9,0.12451715126262271,0.0,0.021388122000719538]
[debug] In cluster 1: 222.187.86.51 [7.253702587811722E-4,-7.658832307263357E-8,9.026379686297647E-6,-3.796634813993416E-9,0.11850577133194529,0.0,0.024944354814031892]
[debug] In cluster 1: 113.172.167.81 [2.997409432922262E-4,-7.658832307263357E-8,9.081916832729067E-6,-3.7336340838847454E-9,0.10687008537373148,0.0,0.024497587517297362]
[debug] In cluster 1: 112.11.77.121 [-3.5603365880882786E-5,-7.658832307263357E-8,1.959734054779174E-7,-9.206136293941523E-9,0.10061373417563213,0.0,0.012307222706354749]
[debug] In cluster 1: 23.248.219.53 [2.8885924344715094E-5,-7.658832307263357E-8,1.5662921269357308E-6,-8.31936599349723E-9,0.12252948281292765,0.0,0.024854523144665346]
[debug] In cluster 1: 163.172.173.181 [-3.5603365880882786E-5,-7.658832307263357E-8,6.679935250061649E-6,-5.021514679933364E-9,0.13013034449568928,0.0,0.02471081456440567]
[debug] In cluster 1: 202.121.244.225 [-1.0009265610648067E-4,-7.658832307263357E-8,-9.223170399944926E-7,-9.827422371777911E-9,0.13531292232730632,0.0,0.025231460822088502]
[debug] In cluster 1: 202.121.244.30 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.1857419735934303E-6,-9.944931221943563E-9,0.10974064651180554,0.0,0.0073909438937080135]
[debug] In cluster 1: 115.211.31.99 [-4.850122392600236E-5,-7.658832307263357E-8,-1.1503461447472318E-7,-9.368608976140422E-9,0.09985315814758967,0.0,0.01403111267963118]
[debug] In cluster 1: 180.97.215.92 [-8.719479806136109E-5,-7.658832307263357E-8,4.27766380754198E-6,-6.6661097123425755E-9,0.1388317459793964,0.0,0.025805754320810642]
[debug] In cluster 1: 175.43.160.207 [-3.5603365880882786E-5,-7.658832307263357E-8,3.6392764835495257E-6,-7.0437278265353545E-9,0.1336728626659403,0.0,0.024468768687957998]
[debug] In cluster 1: 111.132.190.10 [2.2235379502150873E-4,-7.658832307263357E-8,2.1411163338038494E-5,3.778097274795148E-9,0.12210000696690727,0.0,0.025223905549519504]
[debug] In cluster 1: 76.118.167.177 [-2.2705507835763208E-5,-7.658832307263357E-8,3.5837393371181056E-6,-7.050050832729529E-9,0.12781088386703862,0.0,0.021657538367811383]
[debug] In cluster 1: 220.187.169.152 [-1.0009265610648067E-4,-7.658832307263357E-8,8.624191625216704E-7,-8.87226580589069E-9,0.1128630112583558,0.0,0.01403111267963118]
[debug] In cluster 1: 122.190.255.167 [6.866766846458135E-4,-7.658832307263357E-8,2.2410831973604126E-5,3.948435229540028E-9,0.11099399010722218,0.0,0.024648085848287028]
[debug] In cluster 1: 117.6.57.231 [2.997409432922262E-4,-7.658832307263357E-8,8.415471075685314E-6,-4.3166918974912945E-9,0.0988303145236705,0.0,0.02296250454109014]
[debug] In cluster 1: 60.169.77.130 [-6.784801099368173E-5,-7.658832307263357E-8,1.2944138014493915E-6,-8.456379454074436E-9,0.13812496953045397,0.0,0.025608361227595865]
[debug] In cluster 1: 111.40.166.130 [6.866766846458135E-4,-7.658832307263357E-8,5.244804898218577E-6,-6.171274399215532E-9,0.13652759010486876,0.0,0.025531484335588595]
[debug] In cluster 1: 222.85.219.9 [3.5334853367274886E-5,-7.658832307263357E-8,2.135501441988417E-6,-8.078284909175105E-9,0.13176277968643602,0.0,0.02286286426680426]
[debug] In cluster 1: 143.208.25.152 [4.178378238983467E-5,-7.658832307263357E-8,1.1956420410435468E-6,-8.61063628870171E-9,0.10120145201537618,0.0,0.017183368630731798]
[debug] In cluster 1: 123.113.13.201 [-8.903734921066205E-5,-7.658832307263357E-8,9.581751150511883E-6,-1.7987941884671969E-9,0.09802777568032137,0.0,0.015501381313544742]
[debug] In cluster 1: 71.6.158.166 [-8.934444106888532E-5,-6.391322387123255E-8,4.585516978235987E-6,-5.400073268092289E-9,0.11874162109633825,0.0,0.022383405080870736]
[debug] In cluster 1: 80.82.78.38 [-3.237890136960289E-5,-7.658832307263357E-8,5.778235945516562E-6,-5.775761108490402E-9,0.13776639461626472,0.0,0.025700607883865897]
[debug] In cluster 1: 220.190.178.250 [-5.495015294856215E-5,-7.658832307263357E-8,2.4591121221067728E-6,-7.946743439729241E-9,0.12240551924782783,0.0,0.020489987631004684]
[debug] In cluster 1: 115.79.238.139 [3.384345174275849E-4,-7.658832307263357E-8,2.9075289544041655E-5,7.848005595105708E-9,0.1320146587607321,0.0,0.02490982207611209]
[debug] In cluster 1: 198.20.69.98 [5.468164043495425E-5,-6.934540924326155E-8,4.369193265048249E-6,-5.393023439438095E-9,0.11394073425975114,0.0,0.020087384425694126]
[debug] In cluster 1: 122.114.122.233 [3.0902082544759425E-6,-7.658832307263357E-8,-1.2336518643777006E-7,-9.411134468835797E-9,0.10827583342079813,0.0,0.010991437298195706]
[debug] In cluster 1: 177.124.102.19 [2.997409432922262E-4,-7.658832307263357E-8,4.416796533422797E-6,-6.614513040084975E-9,0.12292396433070397,0.0,0.024246756965633484]
[debug] In cluster 1: 71.6.167.142 [-8.719479806136109E-5,-7.115613770060456E-8,4.44059816759341E-6,-5.580119760001347E-9,0.11352188683186229,0.0,0.021157952221261267]
[debug] In cluster 1: 88.100.3.126 [2.868430852471066E-4,-7.658832307263357E-8,1.584402065986847E-6,-8.30752030876478E-9,0.09985315814758967,0.0,0.023570834636949273]
[debug] In cluster 1: 125.123.19.66 [-5.495015294856215E-5,-7.658832307263357E-8,6.085350646018276E-7,-8.983972872409736E-9,0.10492366500103802,0.0,0.014545067454274217]
[debug] In cluster 1: 183.80.102.207 [-7.429694001624152E-5,-7.658832307263357E-8,1.1081254103860326E-5,1.6982114494094174E-9,0.09851701107138064,0.0,0.015586502129188261]
[debug] In cluster 1: 125.125.213.86 [-1.0009265610648067E-4,-7.658832307263357E-8,2.417459262291538E-6,-7.98157314598977E-9,0.1241224477687551,0.0,0.019531142594335194]
[debug] In cluster 1: 82.221.105.6 [1.9673168598145837E-5,-7.658832307263357E-8,2.6817394762996906E-6,-7.48795848876337E-9,0.13121172260858635,0.0,0.022814521276539945]
[debug] In cluster 1: 93.174.93.136 [-3.3991133625242836E-5,-7.658832307263357E-8,5.442915992820822E-6,-6.211070674118221E-9,0.13412329818466517,0.0,0.025190325833862475]
[debug] In cluster 1: 117.218.13.177 [1.5786450479591085E-4,-7.658832307263357E-8,2.417459262291538E-6,-7.848378266301294E-9,0.10884178393322518,0.0,0.018628152608310543]
[debug] In cluster 1: 183.60.49.183 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0429321685124357E-6,-9.895916591266708E-9,0.10788674244347304,0.0,0.002116335721547641]
[debug] In cluster 1: 94.102.49.190 [-9.807649790643633E-6,-6.934540924326155E-8,4.341991397434211E-6,-5.983430210536574E-9,0.11281996162265782,0.0,0.021025067473545683]
[debug] In cluster 1: 210.122.100.219 [-5.495015294856215E-5,-7.658832307263357E-8,5.638613754680784E-6,-5.771749747147229E-9,0.11500334193132784,0.0,0.02051748550209738]
[debug] In cluster 1: 115.239.106.105 [-5.495015294856215E-5,-7.658832307263357E-8,8.489911263840274E-8,-9.271159734390395E-9,0.0973812860565357,0.0,0.010991437298195706]
[debug] In cluster 1: 115.224.163.86 [6.866766846458135E-4,-7.658832307263357E-8,2.19531067659918E-6,-7.93771244582144E-9,0.11500334193132784,0.0,0.02457726310429189]
[debug] In cluster 1: 186.219.161.130 [5.468164043495425E-5,-7.658832307263357E-8,2.51510551899341E-7,-9.139423093098798E-9,0.10827583342079813,0.0,0.02322519253703325]
[debug] In cluster 1: 183.129.160.229 [-6.623577873804178E-5,-7.658832307263357E-8,2.54206781769454E-6,-9.465132473285286E-9,0.11406893380824942,0.0,0.01990613639835277]
[debug] In cluster 1: 101.99.36.4 [2.137552229909991E-4,-7.658832307263357E-8,-6.319994448095032E-8,-9.50342382664181E-9,0.10263401425000437,0.0,0.017394800432309655]
[debug] In cluster 1: 180.126.3.99 [-1.0009265610648067E-4,-7.658832307263357E-8,3.16721073896576E-6,-7.57259572405747E-9,0.1330601945404072,0.0,0.02128959677756422]
[debug] In cluster 1: 94.102.49.193 [-5.034377507534201E-5,-6.934540924326155E-8,3.429028714942474E-6,-6.283482399455903E-9,0.11801795865383122,0.0,0.020858816706726412]
[debug] In cluster 1: 123.169.200.110 [-6.784801099368173E-5,-7.658832307263357E-8,4.950195785117378E-7,-9.004147156102985E-9,0.10726877442075157,0.0,0.017290389666088733]
[debug] In cluster 1: 112.86.90.123 [-3.5603365880882786E-5,-7.658832307263357E-8,1.667707785617316E-6,-8.399160618889776E-9,0.1297981229076326,0.0,0.019183838753559817]
[debug] In cluster 1: 116.252.34.161 [6.737788266006939E-4,-7.658832307263357E-8,3.7265491422084307E-6,-7.149450633489775E-9,0.12733942962889322,0.0,0.024297148036095947]
[debug] In cluster 1: 114.251.176.214 [-3.5603365880882786E-5,-7.658832307263357E-8,4.7500194119446735E-6,-6.300774598719793E-9,0.11138856123906278,0.0,0.019008358918222504]
[debug] In cluster 1: 220.133.16.194 [2.997409432922262E-4,-7.658832307263357E-8,1.3413814253513461E-5,-1.1032635071079475E-9,0.11266194989206564,0.0,0.024888883177905153]
[debug] In cluster 1: 61.155.238.89 [-9.807649790643633E-6,-7.658832307263357E-8,2.361922115860118E-6,-8.049659293367219E-9,0.1264733191280674,0.0,0.020968295670625057]
[debug] In cluster 1: 113.225.220.190 [-5.495015294856215E-5,-7.658832307263357E-8,1.0475429839241941E-6,-8.720216173993055E-9,0.12042551296989351,0.0,0.018526407257792095]
[debug] In cluster 1: 93.174.95.106 [1.7463172025456632E-4,-4.399521084045951E-8,3.539309619992963E-6,-6.5577439227914705E-9,0.12653331816507105,0.0,0.022843411718648756]
[debug] In cluster 1: 117.131.187.200 [-3.5603365880882786E-5,-7.658832307263357E-8,1.4733277731473323E-6,-8.473042607507087E-9,0.12911205228645514,0.0,0.022108768554329212]
[debug] In cluster 1: 113.122.47.186 [6.866766846458135E-4,-7.658832307263357E-8,8.415471075685314E-6,-4.2031661830230945E-9,0.12201476999833138,0.0,0.024888883177905153]
[debug] In cluster 1: 139.226.16.168 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0119595291628851E-6,-9.87020301470937E-9,0.11468439069391347,0.0,0.00840894886438367]
[debug] In cluster 1: 176.53.82.66 [-2.9154436858322997E-5,-7.658832307263357E-8,3.440724626006027E-7,-9.118403435862386E-9,0.11594906943808764,0.0,0.01751678031785535]
[debug] In cluster 1: 202.121.244.150 [-1.0009265610648067E-4,-7.658832307263357E-8,-9.266570726626517E-7,-9.795333936287199E-9,0.13376781476777236,0.0,0.02494190778850913]
[debug] In cluster 1: 218.200.55.58 [-3.5603365880882786E-5,-7.658832307263357E-8,-4.045219901906033E-7,-9.507791062723754E-9,0.12280189657303599,0.0,0.023550646691264148]
[debug] In cluster 1: 27.219.170.91 [6.866766846458135E-4,-7.658832307263357E-8,5.749688047510303E-6,-5.79064232177865E-9,0.12339479710992035,0.0,0.025195352506454882]
[debug] In cluster 1: 211.116.216.80 [2.868430852471066E-4,-7.658832307263357E-8,5.08324229046655E-6,-6.174314474836293E-9,0.12802878288464908,0.0,0.025542475929657683]
[debug] In cluster 1: 106.226.226.247 [-3.5603365880882786E-5,-7.658832307263357E-8,5.847334304212174E-7,-8.996124154044982E-9,0.1241224477687551,0.0,0.015849721882194228]
[debug] In cluster 1: 218.58.59.66 [-1.0009265610648067E-4,-7.658832307263357E-8,3.194979312164809E-6,-8.757716082689828E-9,0.11839219883041037,0.0,0.016780173102118268]
[debug] In cluster 1: 89.110.53.102 [2.997409432922262E-4,-7.658832307263357E-8,2.7506821408134144E-6,-7.627960694784915E-9,0.10205037778410075,0.0,0.02029521104401501]
[debug] In cluster 1: 62.210.189.248 [-9.57933700914451E-5,-7.658832307263357E-8,1.1956420410435468E-6,-8.619086222737964E-9,0.13176277968643602,0.0,0.021951155756789353]
[debug] In cluster 1: 111.40.168.90 [-5.495015294856215E-5,-7.658832307263357E-8,3.7830196860426253E-6,-7.114469678715619E-9,0.13503720765127647,0.0,0.024004297616283796]
[debug] In cluster 1: 190.175.22.188 [-2.2705507835763208E-5,-7.658832307263357E-8,-6.370837908267737E-7,-9.681810107129017E-9,0.10884178393322518,0.0,0.002905503440393906]
[debug] In cluster 1: 66.240.205.34 [-8.719479806136109E-5,-7.658832307263357E-8,4.164354958776933E-6,-6.447443546325898E-9,0.1248209109478376,0.0,0.021852890133011487]
[debug] In cluster 1: 163.172.173.62 [-3.5603365880882786E-5,-7.658832307263357E-8,6.169474411230508E-6,-5.439039953170542E-9,0.1304176440760202,0.0,0.024863088627890086]
[debug] In cluster 1: 118.145.22.56 [-2.9154436858322997E-5,-7.658832307263357E-8,1.084567748204032E-6,-8.74543614172035E-9,0.115390639862738,0.0,0.016822172636347943]
[debug] In cluster 1: 122.97.218.114 [-1.0009265610648067E-4,-7.658832307263357E-8,8.54042965513102E-6,-4.066735954950923E-9,0.12366727905867692,0.0,0.02353328339644274]
[debug] In cluster 1: 119.50.250.226 [6.866766846458135E-4,-7.658832307263357E-8,2.1411163338038494E-5,3.359820834734992E-9,0.11468439069391347,0.0,0.024595738602748017]
[debug] In cluster 1: 123.249.27.191 [1.8366022088615001E-4,-7.658832307263357E-8,6.249522365293117E-6,-5.488670192394532E-9,0.13090102131901538,0.0,0.02449021525862109]
[debug] In cluster 1: 201.179.221.1 [-8.719479806136109E-5,-7.658832307263357E-8,-7.481580836662885E-7,-9.74232186734228E-9,0.10205037778410075,0.0,-1.5403910038659218E-4]
[debug] In cluster 1: 71.6.135.131 [-8.934444106888532E-5,-6.210249541388954E-8,5.421294486060726E-6,-5.07556006423637E-9,0.12637969282474854,0.0,0.022504159279262383]
[debug] In cluster 1: 202.121.66.53 [3.5334853367274886E-5,-7.658832307263357E-8,4.390048622496022E-8,-9.310254081985505E-9,0.1043329047325471,0.0,0.019058563294060575]
[debug] In cluster 1: 115.148.82.167 [-5.495015294856215E-5,-7.658832307263357E-8,1.3269116598553972E-6,-8.546248989931593E-9,0.1255606278945173,0.0,0.020434132580316493]
[debug] In cluster 1: 218.63.123.70 [6.737788266006939E-4,-7.658832307263357E-8,5.583076608249365E-6,-5.8550026620304554E-9,0.10250589892820425,0.0,0.024844083674735503]
[debug] In cluster 1: 61.174.123.128 [-5.495015294856215E-5,-7.658832307263357E-8,3.7646913134504465E-7,-9.102950286658895E-9,0.10492366500103802,0.0,0.014785642029627052]
[debug] In cluster 1: 182.243.78.75 [-5.7099795956125076E-5,-7.658832307263357E-8,7.037416013209357E-7,-8.93175707932216E-9,0.11070527952290754,0.0,0.015016192664341996]
[debug] In cluster 1: 27.208.114.208 [-9.170904837715294E-5,-7.658832307263357E-8,-2.483237658834737E-7,-9.692812096749676E-9,0.0988303145236705,0.0,0.00866412517664477]
[debug] In cluster 1: 89.248.172.16 [-8.461522645233718E-5,-7.115613770060456E-8,4.008598507233498E-6,-6.004044600516229E-9,0.11862528344118928,0.0,0.022040943375053614]
[debug] In cluster 1: 1.34.188.54 [0.002685844681639348,-7.658832307263357E-8,2.6742729394388518E-5,6.990184421436045E-9,0.12339479710992035,0.0,0.02534214873950869]
[debug] In cluster 1: 80.82.77.139 [-8.461522645233718E-5,-3.1320111639058484E-8,4.094208427614032E-6,-6.181841465015156E-9,0.1221147693085168,0.0,0.022765775679680602]
[debug] In cluster 1: 58.221.58.113 [-1.0009265610648067E-4,-7.658832307263357E-8,1.1523799512466507E-6,-8.590626155807436E-9,0.11040844507101788,0.0,0.02112394750306602]
[debug] In cluster 1: 60.191.38.77 [-6.784801099368173E-5,-7.658832307263357E-8,7.431181957605305E-6,-4.733719859937944E-9,0.1369210191324044,0.0,0.02530966212929152]
[debug] In cluster 1: 60.184.104.211 [-3.5603365880882786E-5,-7.658832307263357E-8,1.084567748204032E-6,-8.70194741196135E-9,0.115390639862738,0.0,0.013661707685348372]
[debug] In cluster 1: 66.240.219.146 [-8.934444106888532E-5,-7.115613770060456E-8,4.125226514716155E-6,-5.837866163465667E-9,0.11741494707354852,0.0,0.020394585573635923]
[debug] In cluster 1: 78.70.115.52 [2.997409432922262E-4,-7.658832307263357E-8,9.248528271990006E-6,-3.629304481680858E-9,0.11272258744701057,0.0,0.02518524912200053]
[debug] In cluster 1: 112.91.82.252 [6.866766846458135E-4,-7.658832307263357E-8,4.616730260535923E-6,-6.468432446181875E-9,0.09929136903603998,0.0,0.024453894453464454]
[debug] In cluster 1: 115.196.26.234 [-5.495015294856215E-5,-7.658832307263357E-8,6.561383329630476E-7,-8.957864975864936E-9,0.1099878337208605,0.0,0.014785642029627052]
[debug] In cluster 1: 1.55.86.170 [1.7076236284103044E-4,-7.658832307263357E-8,1.959734054779174E-7,-9.151016308412811E-9,0.10061373417563213,0.0,0.020434132580316493]
[debug] In cluster 1: 113.209.68.135 [6.737788266006939E-4,-7.658832307263357E-8,-2.746308352401133E-7,-9.776509542257249E-9,0.10760281118980383,0.0,0.01363443617569868]
[debug] In cluster 1: 106.75.9.66 [-8.07458690388013E-5,-7.658832307263357E-8,3.940519877821982E-5,1.440247213718873E-8,0.1063367570749774,0.0,0.01492809608035122]
[debug] In cluster 1: 183.144.121.228 [6.737788266006939E-4,-7.658832307263357E-8,6.249522365293117E-6,-5.450330265603256E-9,0.10578565116608558,0.0,0.02492986995738788]
[debug] In cluster 1: 59.32.201.87 [-6.784801099368173E-5,-7.658832307263357E-8,4.194647947730439E-6,-6.686477976564467E-9,0.12911205228645514,0.0,0.0225179556249254]
[debug] In cluster 1: 183.95.174.68 [-5.7099795956125076E-5,-7.658832307263357E-8,1.0081585468294697E-5,-2.968550334389571E-9,0.10479690232969761,0.0,0.015944982364241914]
[debug] In cluster 1: 202.121.244.246 [-6.536765367728281E-5,-7.658832307263357E-8,2.293433647016051E-7,-9.455218963010792E-9,0.12329406715415185,0.0,0.019894906332748062]
[debug] In cluster 1: 71.6.146.130 [-8.719479806136109E-5,-6.934540924326155E-8,3.0118568293900188E-6,-7.024637007995796E-9,0.12518854059439202,0.0,0.02304749183390164]
[debug] In cluster 1: 210.35.68.90 [-1.0009265610648067E-4,-7.658832307263357E-8,-8.632802734754308E-7,-9.744524103525572E-9,0.10671946951166582,0.0,0.003036606977340636]
[debug] In cluster 1: 80.82.77.33 [-8.461522645233718E-5,-2.769865472437248E-8,5.007386675851269E-6,-5.6646358899383474E-9,0.12498938885247804,0.0,0.023232213392200867]
[debug] In cluster 1: 121.31.74.244 [-5.495015294856215E-5,-7.658832307263357E-8,8.489911263840274E-8,-9.271159734390395E-9,0.0973812860565357,0.0,0.010991437298195706]
[debug] In cluster 1: 175.100.91.178 [3.384345174275849E-4,-7.658832307263357E-8,2.207760909508225E-5,4.147993137154385E-9,0.1321993700818377,0.0,0.025546836444692286]
[debug] In cluster 1: 59.152.161.196 [2.997409432922262E-4,-7.658832307263357E-8,7.9156367579025E-6,-4.4810134160362295E-9,0.11189791670036478,0.0,0.022810422017168704]
[debug] In cluster 1: 114.255.78.180 [-5.495015294856215E-5,-7.658832307263357E-8,8.994439268015088E-7,-8.803225896337824E-9,0.1191167130640618,0.0,0.018089054109220466]
[debug] In cluster 1: 77.29.95.47 [2.997409432922262E-4,-7.658832307263357E-8,9.415139711250944E-6,-3.5060058608944454E-9,0.10263401425000437,0.0,0.02453863251666212]
[debug] In cluster 1: 114.255.78.181 [6.866766846458135E-4,-7.658832307263357E-8,2.4729964087229583E-6,-7.91228174050243E-9,0.11180668885644413,0.0,0.022883338295754053]
[debug] In cluster 1: 59.188.239.77 [-7.859622603123838E-5,-7.658832307263357E-8,1.5288649195654233E-6,-5.934646555111506E-9,0.11699147131220809,0.0,0.020176135441477075]
[debug] In cluster 1: 153.35.0.18 [6.866766846458135E-4,-7.658832307263357E-8,8.193322489992956E-6,-4.322064773730103E-9,0.10776314883886151,0.0,0.024374452519203095]
[debug] In cluster 1: 39.72.29.85 [-7.429694001624152E-5,-7.658832307263357E-8,1.6177243538390343E-6,-7.327209270914277E-9,0.10422204835507298,0.0,0.0095982527483675]
[debug] In cluster 1: 95.65.55.130 [2.997409432922262E-4,-7.658832307263357E-8,4.6389451191151554E-6,-6.472669107323769E-9,0.11403144334910995,0.0,0.0243040896631678]
[debug] In cluster 1: 117.71.18.20 [-5.495015294856215E-5,-7.658832307263357E-8,6.97150526876829E-6,-4.97505096668118E-9,0.12123151136743864,0.0,0.021348849708992014]
[debug] In cluster 1: 78.60.144.1 [2.868430852471066E-4,-7.658832307263357E-8,4.616730260535923E-6,-6.468432446181875E-9,0.1145720328715699,0.0,0.024453894453464454]
[debug] In cluster 1: 24.107.157.190 [-5.2800509940999227E-5,-7.658832307263357E-8,2.232335440875686E-6,-8.051598801049468E-9,0.11079250697610528,0.0,0.020381531221909395]
[debug] In cluster 1: 71.6.146.185 [-3.5603365880882786E-5,-6.391322387123255E-8,3.2292469131700065E-6,-6.547017435067024E-9,0.12170270686056352,0.0,0.02240247773768609]
[debug] In cluster 1: 144.0.239.71 [6.866766846458135E-4,-7.658832307263357E-8,2.084236383769662E-6,-8.005433504670593E-9,0.10578565116608558,0.0,0.02427594452071901]
[debug] In cluster 1: 66.240.192.138 [-6.784801099368173E-5,-6.753468078591855E-8,4.234565271731146E-6,-6.144838783823287E-9,0.12152488003228705,0.0,0.022786595301203444]
[debug] In cluster 1: 71.6.146.186 [-8.934444106888532E-5,-7.296686615794755E-8,4.938845709784844E-6,-5.235514449896893E-9,0.12054232814476355,0.0,0.02171708598458872]
[debug] In cluster 1: 211.116.220.142 [2.868430852471066E-4,-7.658832307263357E-8,1.0859105518167966E-5,-2.7346642908261595E-9,0.12706642887464434,0.0,0.025078081079729395]
[debug] In cluster 1: 46.127.67.221 [6.866766846458135E-4,-7.658832307263357E-8,1.3747037132035338E-5,-8.756352841176479E-10,0.10578565116608558,0.0,0.02490982207611209]
[debug] In cluster 1: 190.96.223.7 [1.5786450479591085E-4,-7.658832307263357E-8,1.751013505247785E-6,-8.240787862842034E-9,0.10205037778410075,0.0,0.017183368630731798]
[debug] In cluster 1: 106.75.81.187 [-7.798204231479183E-5,-7.658832307263357E-8,2.441016924473538E-5,5.537895936621475E-9,0.13154448448890327,0.0,0.023184778999215856]
[debug] In cluster 1: 89.248.167.131 [-8.977436967038501E-5,-4.2184482383116507E-8,3.1650746948760886E-6,-7.664436267923456E-9,0.1242703728785297,0.0,0.02156063493908776]
[debug] In cluster 1: 36.79.44.104 [3.255366593824653E-4,-7.658832307263357E-8,7.513448696821557E-7,-8.81726587837026E-9,0.11138856123906278,0.0,0.021724118274600263]
[debug] In cluster 1: 186.133.237.183 [-3.3587207680838453E-6,-7.658832307263357E-8,6.40270576842641E-7,-8.773070020642215E-9,0.10974064651180554,0.0,0.004180312832393004]
[debug] In cluster 1: 58.211.200.75 [-6.462354648240183E-5,-7.658832307263357E-8,3.5837393371181056E-6,-6.895927556746514E-9,0.10752448532664732,0.0,0.020259360324941505]
[debug] In cluster 1: 181.57.149.131 [6.866766846458135E-4,-7.658832307263357E-8,4.878182057524884E-6,-6.502723674106415E-9,0.13096358178715045,0.0,0.02413209157056485]
[debug] In cluster 1: 202.121.244.206 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.0058366450077934E-6,-9.810556998856589E-9,0.0977404469586949,0.0,0.014938236694331405]
[debug] In cluster 1: 78.154.179.22 [-7.171736840721759E-5,-7.658832307263357E-8,2.550748413700289E-6,-7.641954468124193E-9,0.1128630112583558,0.0,0.0160648261964619]
[debug] In cluster 1: 185.163.109.66 [-8.461522645233718E-5,-7.477759461529056E-8,3.916962215639982E-6,-3.646692748714839E-9,0.11229225641212906,0.0,0.019941592587939413]
[debug] In cluster 1: 1.82.230.35 [-5.5083637756467715E-6,-7.658832307263357E-8,1.4043625905982632E-7,-9.295010505516146E-9,0.10578565116608558,0.0,0.01187029206798229]
[debug] In cluster 1: 106.75.81.90 [-8.07458690388013E-5,-7.658832307263357E-8,2.536467433177927E-6,-7.742028320344107E-9,0.12354581894467452,0.0,0.02259504503233177]
[debug] In cluster 1: 172.125.185.15 [4.1582166569830234E-4,-7.658832307263357E-8,1.541315152464472E-5,1.360457069503512E-10,0.1225943813851853,0.0,0.025002199568306037]
[debug] In cluster 1: 181.52.250.220 [1.320687887056717E-4,-7.658832307263357E-8,-1.3724947304395893E-7,-9.351266566438744E-9,0.10578565116608558,0.0,0.019183838753559817]
[debug] In cluster 1: 36.251.49.217 [3.126388013373457E-4,-7.658832307263357E-8,1.084567748204032E-6,-8.608671859593666E-9,0.10338440399188045,0.0,0.020434132580316493]
[debug] In cluster 1: 221.122.101.203 [-4.3342080707954527E-5,-7.658832307263357E-8,2.5285335551210565E-6,-7.836060721762458E-9,0.1349537418404551,0.0,0.02409490279379286]
[debug] In cluster 1: 42.82.168.234 [3.126388013373457E-4,-7.658832307263357E-8,6.0829109260321795E-6,-5.597156351304814E-9,0.1241224477687551,0.0,0.02490982207611209]
[debug] In cluster 1: 210.35.66.18 [-1.0009265610648067E-4,-7.658832307263357E-8,-1.1712982468685814E-6,-9.990852911390544E-9,0.11042254226108499,0.0,0.006277579896632806]
[debug] In cluster 1: 210.35.112.31 [5.468164043495425E-5,-7.658832307263357E-8,-8.554671462427678E-7,-9.816293566403222E-9,0.09889286251502134,0.0,0.012612904141307954]
[debug] In cluster 1: 109.201.12.202 [3.384345174275849E-4,-7.658832307263357E-8,4.416796533422797E-6,-6.566340858875143E-9,0.09985315814758967,0.0,0.02344409920028306]
[debug] In cluster 1: 182.43.5.148 [3.7712809156294365E-4,-7.658832307263357E-8,3.990503309600264E-5,1.4600846507862441E-8,0.13395898845633314,0.0,0.025503931778801087]
[debug] In cluster 1: 202.215.132.129 [-5.495015294856215E-5,-7.658832307263357E-8,9.748362589772821E-6,-3.1708865326031705E-9,0.09865467470953856,0.0,0.015653597360341543]
[debug] In cluster 1: 112.12.14.55 [6.866766846458135E-4,-7.658832307263357E-8,5.149886866170925E-6,-6.15550254957222E-9,0.1218940176260001,0.0,0.024993784984850345]
[debug] In cluster 1: 198.20.69.74 [1.598806629959552E-5,-6.572395232857554E-8,3.485732608127362E-6,-6.19206190582068E-9,0.11191057149429555,0.0,0.019480491318509527]
[debug] In cluster 1: 122.194.212.177 [-5.495015294856215E-5,-7.658832307263357E-8,1.4844352024302838E-6,-8.465154323651744E-9,0.12505419556351224,0.0,0.02019856997526093]
[debug] In cluster 1: 1.119.0.247 [-5.495015294856215E-5,-7.658832307263357E-8,1.6260549258020814E-6,-8.40922617849827E-9,0.11991472874147788,0.0,0.019097238315342525]
[debug] In cluster 1: 112.82.171.169 [6.737788266006939E-4,-7.658832307263357E-8,1.4413482889079091E-5,-4.709628876904483E-10,0.10363985581893427,0.0,0.024949082510309688]
[debug] In cluster 1: 1.55.169.104 [1.7076236284103044E-4,-7.658832307263357E-8,-2.483237658834737E-7,-9.418016652448064E-9,0.10205037778410075,0.0,0.018628152608310543]
[debug] In cluster 1: 14.157.26.25 [6.866766846458135E-4,-7.658832307263357E-8,3.4171278978571674E-6,-7.206920053422238E-9,0.1313989543571027,0.0,0.02392569385951932]
[debug] In cluster 1: 191.96.249.97 [-8.719479806136109E-5,-7.658832307263357E-8,2.8652113105734467E-6,-8.222256935647656E-9,0.1140322425070774,0.0,0.022553317470537293]
[debug] In cluster 1: 143.202.208.6 [-6.913779679819368E-5,-7.658832307263357E-8,5.860762340339821E-6,-5.681950285442714E-9,0.11051310654020738,0.0,0.016795217711360933]
[debug] In cluster 1: 182.44.78.46 [6.866766846458135E-4,-7.658832307263357E-8,5.416465168988426E-6,-5.9340402394576435E-9,0.10578565116608558,0.0,0.023788095385445622]
[debug] In cluster 1: 202.121.66.117 [-1.0009265610648067E-4,-7.658832307263357E-8,-9.028568877459814E-7,-9.887212898824812E-9,0.12763861202624888,0.0,0.023716611300137868]
[debug] In cluster 1: 89.233.222.159 [2.997409432922262E-4,-7.658832307263357E-8,1.0414808346816574E-5,-2.928504628486384E-9,0.12579604428412547,0.0,0.02545074361363745]
[debug] In cluster 1: 123.16.238.46 [2.997409432922262E-4,-7.658832307263357E-8,3.2505164585962293E-6,-7.326171649370451E-9,0.10205037778410075,0.0,0.02286286426680426]
[debug] In cluster 1: 202.121.66.24 [-4.850122392600236E-5,-2.769865472437248E-8,-9.236524294632391E-7,-9.795713185116041E-9,0.10977536381637537,0.0,0.014577052400964033]
[debug] In cluster 1: 122.189.199.45 [-9.807649790643633E-6,-7.658832307263357E-8,5.014277107907484E-7,-9.007349109407053E-9,0.09938232536837333,0.0,0.022384590950067312]
[debug] In cluster 1: 61.164.98.59 [-5.7099795956125076E-5,-7.658832307263357E-8,1.5933930079336267E-9,-9.311067519106881E-9,0.10018274109310836,0.0,0.012164645340154018]
[debug] In cluster 1: 114.99.240.214 [-1.0009265610648067E-4,-7.658832307263357E-8,-2.899766256987083E-7,-9.478356197271611E-9,0.10286239373664768,0.0,0.012569381089395558]
[debug] In cluster 1: 191.249.159.113 [-2.2705507835763208E-5,-7.658832307263357E-8,2.417459262291538E-6,-7.81177852508309E-9,0.12030228180991463,0.0,0.02224011255232244]
[debug] In cluster 1: 113.225.211.229 [1.8366022088615001E-4,-7.658832307263357E-8,6.416133804554056E-6,-5.289093607651794E-9,0.1247868244572637,0.0,0.020434132580316493]
[debug] In cluster 1: 114.84.1.182 [6.866766846458135E-4,-7.658832307263357E-8,2.5840707015524763E-6,-7.69078212707934E-9,0.1247868244572637,0.0,0.025274158905268157]
[debug] In cluster 1: 113.190.203.238 [2.7394522720198704E-4,-7.658832307263357E-8,1.001262028573563E-6,-8.665137775373828E-9,0.10205037778410075,0.0,0.023055716410637916]
[debug] In cluster 1: 80.130.0.225 [-2.2705507835763208E-5,-7.658832307263357E-8,-2.483237658834737E-7,-9.455326873395138E-9,0.0973812860565357,0.0,0.0095982527483675]
[debug] In cluster 1: 222.205.127.11 [6.866766846458135E-4,-7.658832307263357E-8,6.416133804554056E-6,-5.48130071827514E-9,0.11260658516805126,0.0,0.013812206016338034]
[debug] In cluster 1: 139.201.165.144 [-5.2800509940999227E-5,-7.658832307263357E-8,1.084567748204032E-6,-8.699463743407876E-9,0.11805869227846545,0.0,0.018628152608310543]
[debug] In cluster 1: 118.193.154.98 [-5.495015294856215E-5,-7.658832307263357E-8,5.583076608249365E-6,-5.7886110969916184E-9,0.10660558922547186,0.0,0.017788161923673752]
[debug] In cluster 1: 27.209.34.41 [-5.495015294856215E-5,-7.658832307263357E-8,-1.0948089983158118E-7,-9.381009794343052E-9,0.10250589892820425,0.0,0.013585038724252818]
[debug] In cluster 1: 37.60.214.242 [3.384345174275849E-4,-7.658832307263357E-8,2.6076283637344767E-5,6.585512025008845E-9,0.12915388586094792,0.0,0.025329861420056138]
[debug] In cluster 1: 163.172.134.70 [-3.3991133625242836E-5,-7.658832307263357E-8,5.923628435713218E-6,-5.644824492629174E-9,0.12845533527949388,0.0,0.024316989520083765]
[debug] In cluster 1: 219.77.232.8 [-9.711622732684067E-5,-7.658832307263357E-8,1.0613196404003353E-6,-8.866157673100826E-9,0.11966131436700839,0.0,0.023182823505412745]
[debug] In cluster 1: 115.76.252.179 [1.5786450479591085E-4,-7.658832307263357E-8,-4.4825749299659967E-7,-9.550454891279705E-9,0.0973812860565357,0.0,0.015653597360341543]
[debug] In cluster 1: 122.160.31.159 [-3.5603365880882786E-5,-7.658832307263357E-8,3.083905019335291E-6,-7.312455589787792E-9,0.10061373417563213,0.0,0.016564175497465184]
[debug] In cluster 1: 114.241.26.89 [-1.0009265610648067E-4,-7.658832307263357E-8,2.84088437869979E-5,8.065095474445794E-9,0.1318486261125076,0.0,0.024407288518712236]
[debug] In cluster 1: 107.182.21.213 [-2.2705507835763208E-5,-7.658832307263357E-8,1.1317961876780517E-6,-8.564821562700813E-9,0.13810585568879227,0.0,0.02559008313867219]
[debug] In cluster 1: 119.132.116.87 [4.178378238983467E-5,-7.658832307263357E-8,6.249522365293117E-6,-5.195829266287712E-9,0.10205037778410075,0.0,0.021241059801862132]
[debug] In cluster 1: 112.227.214.25 [-5.495015294856215E-5,-7.658832307263357E-8,2.474339212325726E-5,5.776167232154446E-9,0.12216338830266153,0.0,0.025303420352905556]
[debug] In cluster 1: 106.113.214.175 [-5.495015294856215E-5,-7.658832307263357E-8,7.549091591528435E-6,-4.64794077956245E-9,0.12794261372759558,0.0,0.02332106406838796]
[debug] In cluster 1: 122.241.10.60 [6.866766846458135E-4,-7.658832307263357E-8,5.08324229046655E-6,-6.205668763282445E-9,0.11581191129677369,0.0,0.024407288518712236]
[debug] In cluster 1: 113.251.17.73 [-1.0009265610648067E-4,-7.658832307263357E-8,-9.782405474075205E-7,-9.844176078773955E-9,0.09985315814758967,0.0,0.007602169621427248]
[debug] In cluster 1: 223.155.231.174 [-9.493351288843284E-5,-7.658832307263357E-8,1.5694730259033968E-7,-9.3383664137194E-9,0.10923359582644389,0.0,0.020911360448549927]
[debug] In cluster 1: 66.240.236.119 [-9.235394127940892E-5,-6.572395232857554E-8,4.0453349639098895E-6,-6.468966563484848E-9,0.12621979378538659,0.0,0.023200964032538546]
[debug] In cluster 1: 182.243.110.127 [-5.495015294856215E-5,-7.658832307263357E-8,3.916962215639982E-6,-6.944683125034095E-9,0.1014479143353339,0.0,0.01403111267963118]
[debug] In cluster 1: 94.102.49.174 [-3.3991133625242836E-5,-7.658832307263357E-8,5.024664205194788E-6,-6.520722281848018E-9,0.13321434917671865,0.0,0.02525979297647959]
[debug] In cluster 1: 85.61.73.115 [-9.364372708392087E-5,-7.658832307263357E-8,-1.816791901790984E-7,-9.3686972041335E-9,0.0973812860565357,0.0,0.023788095385445622]
[debug] In cluster 1: 125.123.179.245 [-5.7099795956125076E-5,-7.658832307263357E-8,3.348162715298101E-7,-9.126074423596436E-9,0.10843966120064488,0.0,0.014545067454274217]
[debug] In cluster 1: 212.47.246.179 [9.405652469130166E-7,-7.658832307263357E-8,3.722582203179995E-6,-6.74447460859153E-9,0.12696089510127226,0.0,0.022745976052312676]
[debug] In cluster 1: 111.11.27.140 [-5.495015294856215E-5,-7.658832307263357E-8,6.166216645662649E-6,-5.7912636752004E-9,0.13373724748540994,0.0,0.023521775758246766]
[debug] In cluster 1: 122.189.193.228 [-3.5603365880882786E-5,-7.658832307263357E-8,2.9728307265057727E-6,-7.743638204845244E-9,0.1305564114890313,0.0,0.01950911844830499]
[debug] In cluster 1: 188.17.12.244 [-9.041926257264099E-5,-7.658832307263357E-8,2.002454936639245E-7,-9.198932267804546E-9,0.11262814311372238,0.0,0.021174714295438782]
[debug] In cluster 1: 49.117.177.165 [-5.495015294856215E-5,-7.658832307263357E-8,6.109747845779274E-8,-9.258548292839111E-9,0.1180101822344759,0.0,0.018314069134946476]
[debug] In cluster 1: 125.124.6.62 [-5.495015294856215E-5,-7.658832307263357E-8,2.174438621656037E-5,4.113216603086422E-9,0.12235077659945484,0.0,0.02089852743024478]
[debug] =======================================
[debug] [Info] There are 360 anomalous IPs among 2 clusters.
[debug] Begin to optimize K-means model by choosing a K value that takes minimum cost.
[debug] Sum of squared distances of points to their nearest center when K=8 --->>> 1.575516136018944
[debug] Sum of squared distances of points to their nearest center when K=9 --->>> 1.4462338204338834
[debug] Sum of squared distances of points to their nearest center when K=10 --->>> 1.3239982983472958
[debug] Sum of squared distances of points to their nearest center when K=11 --->>> 1.2000221281160182
[debug] Sum of squared distances of points to their nearest center when K=12 --->>> 1.1542830610918682
[debug] Sum of squared distances of points to their nearest center when K=13 --->>> 0.9952041035513284
[debug] Sum of squared distances of points to their nearest center when K=14 --->>> 0.8952636850526234
[debug] Sum of squared distances of points to their nearest center when K=15 --->>> 0.8696592845113185
[debug] Sum of squared distances of points to their nearest center when K=16 --->>> 0.7856318006308629
[debug] Sum of squared distances of points to their nearest center when K=17 --->>> 0.761385022597362
[debug] Sum of squared distances of points to their nearest center when K=18 --->>> 0.7111374086304328
[debug] Sum of squared distances of points to their nearest center when K=19 --->>> 0.6721252171962654
[debug] Sum of squared distances of points to their nearest center when K=20 --->>> 0.6464492990992937
```

```shell
u928@master:~/fl/kmeansTest$ spark-submit --class "nadsTrojan" ./trojan-kmeans_2.11-1.0.jar /tcpheader/normalizeDataSet_v2.txt /tcpheader/trainSet_v2.txt 15 100 3 2>&1 | grep "debug"
[debug] Calculating some variables to normalize data...
[debug] Normalizing data...
[debug] Morlization Finished!
[debug] The threshold is 212.38
[debug] Cluster:8 Number:674
[debug] Cluster:11 Number:246
[debug] Cluster:2 Number:2003
[debug] Cluster:5 Number:1640
[debug] Cluster:14 Number:523
[debug] Cluster:4 Number:2617
[debug] Cluster:13 Number:194
[debug] Cluster:7 Number:3097
[debug] Cluster:1 Number:335
[debug] Cluster:10 Number:1532
[debug] Cluster:9 Number:1833
[debug] Cluster:3 Number:1234
[debug] Cluster:12 Number:2459
[debug] Cluster:6 Number:2068
[debug] Cluster:0 Number:783
[debug] Selecting anomalous cluster...
[debug] How many clusters? Clusters Number: 15
[debug] Center Point of Cluster 0:
[debug] [-2.2882065554128215E-6,1.189472247973277E-7,4.659033088591709E-7,-7.704886936939513E-9,0.03465112485679515,0.0,-9.604085932371904E-4]
[debug] Center Point of Cluster 1:
[debug] [5.053533496579129E-5,8.001772062920007E-7,1.0313069960493178E-6,-8.459451839735215E-9,0.08065798641740901,0.0,0.007028737370615804]
[debug] Center Point of Cluster 2:
[debug] [3.929057649391071E-5,-7.58867559711344E-8,-1.1333617316880932E-7,-8.833377423384945E-9,-0.02009231528930812,0.0,0.006516497929118944]
[debug] Center Point of Cluster 3:
[debug] [-5.889472725850551E-6,-6.694039794538104E-8,-7.106608616606782E-7,-9.348267491869592E-9,0.006139927276388755,0.0,-0.013948708781807125]
[debug] Center Point of Cluster 4:
[debug] [-2.7477183608333024E-5,-7.480452764726632E-8,-7.720808242226025E-7,-9.219072890410892E-9,-0.009574088044699725,0.0,-6.779351776839551E-4]
[debug] Center Point of Cluster 5:
[debug] [6.525018790773125E-5,-7.14994562460344E-8,-8.315772799368176E-7,-9.51374315360487E-9,-0.007870264608242424,0.0,0.00874348258201865]
[debug] Center Point of Cluster 6:
[debug] [-5.236772877203328E-5,-6.609976389519502E-8,3.422149690535211E-6,8.149165173366438E-8,-0.024186043146335674,0.0,-0.01614371324852443]
[debug] Center Point of Cluster 7:
[debug] [-2.5803577950173552E-5,-7.575192784266176E-8,-8.365882746610041E-7,-9.598756280352618E-9,0.002899698078268384,0.0,0.007540680132967912]
[debug] Center Point of Cluster 8:
[debug] [9.576311438755219E-5,-6.517858455878211E-8,1.7374034068072177E-6,-7.638065924581356E-9,0.056272801832731915,0.0,0.014528044021061226]
[debug] Center Point of Cluster 9:
[debug] [-1.1206441211055734E-5,1.5820994950939374E-8,-4.934134323571207E-7,-9.039810672088113E-9,0.01429292651456769,0.0,0.006014301738961112]
[debug] Center Point of Cluster 10:
[debug] [1.1678690118496526E-5,-7.6413396302604E-8,-7.485477177582037E-7,-9.242787838140797E-9,-0.011718838499096622,0.0,-0.015605973713631474]
[debug] Center Point of Cluster 11:
[debug] [-6.43107834028027E-5,2.848665581819335E-6,9.952078022865938E-7,-8.705607301318545E-9,0.12519059458031262,0.0,-0.011523430277706703]
[debug] Center Point of Cluster 12:
[debug] [-2.8814388746154458E-5,1.5821598202459524E-8,-5.608250158525539E-7,-6.9204717921035345E-9,-0.024039934852501672,0.0,-0.002693468842738204]
[debug] Center Point of Cluster 13:
[debug] [1.1589460228772661E-4,-7.486159748186825E-8,5.023940614241182E-6,-6.15005731358746E-9,0.11707412066534181,0.0,0.02072409251445695]
[debug] Center Point of Cluster 14:
[debug] [1.3173358825812543E-4,-6.681108184216208E-8,6.372704378752955E-7,-8.870255983580412E-9,0.03218240387660334,0.0,0.01627880271984613]
[debug] Got anomalous clusters...---------->>>>>>>>
[debug] =======================================
[debug] The index of anomalous cluster is 13
[debug] The points in this cluster are as follows: 
[debug] In cluster 13: 202.121.66.31 [6.25,0.0,0.156004489338,0.207886180103,0.741007194245,1.0,0.965048543689]
[debug] In cluster 13: 221.200.86.173 [6.66666666667,0.0,8.75,8.34814814815,0.857142857143,1.0,0.948717948718]
[debug] In cluster 13: 71.6.165.200 [1.0,2.0,14.3,16.2851592852,0.90472027972,1.0,0.932189542484]
[debug] In cluster 13: 221.203.78.174 [10.0,0.0,57.0,58.15625,0.877192982456,1.0,0.931034482759]
[debug] In cluster 13: 202.121.66.35 [0.0,0.0,0.472745292369,0.536964591483,0.77358490566,1.0,0.92866756393]
[debug] In cluster 13: 123.196.124.249 [1.42857142857,0.0,12.8333333333,13.3958333333,0.883116883117,1.0,0.915662650602]
[debug] In cluster 13: 60.2.127.118 [4.0,0.0,8.07142857143,7.72033898305,0.920353982301,1.0,0.905511811024]
[debug] In cluster 13: 111.40.30.206 [61.0,0.0,9.03333333333,3.92658946908,0.911439114391,1.0,0.897009966777]
[debug] In cluster 13: 182.37.126.162 [0.0,0.0,8.75,8.05673758865,0.914285714286,1.0,0.820512820513]
[debug] In cluster 13: 114.222.101.211 [0.0,0.0,9.45454545455,8.59640102828,0.951923076923,1.0,0.904347826087]
[debug] In cluster 13: 222.187.86.51 [64.0,0.0,30.8333333333,30.7823834197,0.875675675676,1.0,0.979057591623]
[debug] In cluster 13: 113.172.167.81 [31.0,0.0,31.0,31.09375,0.806451612903,1.0,0.96875]
[debug] In cluster 13: 172.82.166.210 [4.44444444444,0.0,24.6334792123,24.4017220705,0.968865200977,1.0,0.989286781339]
[debug] In cluster 13: 81.182.35.144 [31.0,0.0,29.0,28.3333333333,0.758620689655,1.0,0.933333333333]
[debug] In cluster 13: 112.11.77.121 [5.0,0.0,4.33333333333,4.04716981132,0.769230769231,1.0,0.6875]
[debug] In cluster 13: 23.248.219.53 [10.0,0.0,8.44565217391,8.4298275279,0.899613899614,1.0,0.976985040276]
[debug] In cluster 13: 163.172.173.181 [5.0,0.0,23.7916666667,24.7286971453,0.944833625219,1.0,0.973669467787]
[debug] In cluster 13: 202.121.244.225 [0.0,0.0,0.977349943375,0.97660664308,0.975666280417,1.0,0.985681557847]
[debug] In cluster 13: 202.121.244.30 [0.0,0.0,0.186813186813,0.395846249225,0.823529411765,1.0,0.574074074074]
[debug] In cluster 13: 175.43.160.207 [5.0,0.0,14.6666666667,14.734375,0.965909090909,1.0,0.968085106383]
[debug] In cluster 13: 115.211.31.99 [4.0,0.0,3.4,3.24418604651,0.764705882353,1.0,0.727272727273]
[debug] In cluster 13: 180.97.215.92 [1.0,0.0,16.5824634656,16.6006654489,0.996600780562,1.0,0.998931370221]
[debug] In cluster 13: 117.6.57.231 [31.0,0.0,29.0,28.2121212121,0.758620689655,1.0,0.933333333333]
[debug] In cluster 13: 60.169.77.130 [2.5,0.0,7.62974683544,7.75267009494,0.992395962948,1.0,0.994377215499]
[debug] In cluster 13: 111.132.190.10 [25.0,0.0,68.0,68.21875,0.897058823529,1.0,0.985507246377]
[debug] In cluster 13: 143.208.25.152 [11.0,0.0,7.33333333333,6.99029126214,0.772727272727,1.0,0.8]
[debug] In cluster 13: 76.118.167.177 [6.0,0.0,14.5,14.703125,0.931034482759,1.0,0.903225806452]
[debug] In cluster 13: 220.187.169.152 [0.0,0.0,6.33333333333,5.69724770642,0.842105263158,1.0,0.727272727273]
[debug] In cluster 13: 122.190.255.167 [61.0,0.0,71.0,69.0606060606,0.830985915493,1.0,0.972222222222]
[debug] In cluster 13: 111.40.166.130 [61.0,0.0,19.4848484848,19.0462748727,0.982892690513,1.0,0.992603550296]
[debug] In cluster 13: 222.85.219.9 [10.5,0.0,10.1538461538,9.62131519274,0.954545454545,1.0,0.931034482759]
[debug] In cluster 13: 220.190.178.250 [3.5,0.0,11.125,10.2714285714,0.898876404494,1.0,0.876288659794]
[debug] In cluster 13: 198.20.69.98 [12.0,4.0,16.8571428571,22.8926007731,0.848516949153,1.0,0.867]
[debug] In cluster 13: 177.124.102.19 [31.0,0.0,17.0,16.8556701031,0.901960784314,1.0,0.962962962963]
[debug] In cluster 13: 71.6.167.142 [1.0,3.0,17.0714285714,21.967920354,0.846025104603,1.0,0.891699604743]
[debug] In cluster 13: 71.6.158.166 [0.833333333333,7.0,17.5063291139,22.8577586207,0.877078814172,1.0,0.919972640219]
[debug] In cluster 13: 80.82.78.38 [5.25,0.0,21.0856720827,21.0010081049,0.990262697023,1.0,0.996505484216]
[debug] In cluster 13: 115.79.238.139 [34.0,0.0,91.0,88.3333333333,0.956043956044,1.0,0.978260869565]
[debug] In cluster 13: 122.114.122.233 [8.0,0.0,3.375,3.03401360544,0.814814814815,1.0,0.657142857143]
[debug] In cluster 13: 88.100.3.126 [30.0,0.0,8.5,8.48837209302,0.764705882353,1.0,0.947368421053]
[debug] In cluster 13: 93.174.93.136 [5.125,0.0,20.0793787748,18.8495909612,0.968588862152,1.0,0.984732511972]
[debug] In cluster 13: 117.218.13.177 [20.0,0.0,11.0,10.7575757576,0.818181818182,1.0,0.833333333333]
[debug] In cluster 13: 125.123.19.66 [3.5,0.0,5.57142857143,5.14516129032,0.794871794872,1.0,0.739130434783]
[debug] In cluster 13: 183.80.102.207 [2.0,0.0,37.0,57.9393939394,0.756756756757,1.0,0.763157894737]
[debug] In cluster 13: 125.125.213.86 [0.0,0.0,11.0,10.0992907801,0.909090909091,1.0,0.854166666667]
[debug] In cluster 13: 82.221.105.6 [9.28571428571,0.0,11.7931034483,12.5388673936,0.95126705653,1.0,0.929919137466]
[debug] In cluster 13: 210.122.100.219 [3.5,0.0,20.6666666667,21.0208333333,0.854838709677,1.0,0.876923076923]
[debug] In cluster 13: 183.60.49.183 [0.0,0.0,0.615384615385,0.638089758343,0.8125,1.0,0.452380952381]
[debug] In cluster 13: 101.99.36.4 [24.3333333333,0.0,3.55555555556,2.57789473684,0.78125,1.0,0.80487804878]
[debug] In cluster 13: 180.126.3.99 [0.0,0.0,13.25,12.1205673759,0.962264150943,1.0,0.894736842105]
[debug] In cluster 13: 94.102.49.190 [7.0,4.0,16.7755102041,19.9746514575,0.841849148418,1.0,0.888633754305]
[debug] In cluster 13: 115.224.163.86 [61.0,0.0,10.3333333333,10.3160621762,0.854838709677,1.0,0.970588235294]
[debug] In cluster 13: 186.219.161.130 [12.0,0.0,4.5,4.37688442211,0.814814814815,1.0,0.939393939394]
[debug] In cluster 13: 123.169.200.110 [2.5,0.0,5.23076923077,5.04545454545,0.808823529412,1.0,0.802469135802]
[debug] In cluster 13: 183.129.160.229 [2.625,0.0,11.3739495798,2.76714092064,0.849279645364,1.0,0.862818336163]
[debug] In cluster 13: 112.86.90.123 [5.0,0.0,8.75,8.03546099291,0.942857142857,1.0,0.846153846154]
[debug] In cluster 13: 116.252.34.161 [60.0,0.0,14.9285714286,14.2118644068,0.928229665072,1.0,0.964125560538]
[debug] In cluster 13: 220.133.16.194 [31.0,0.0,44.0,44.09375,0.840909090909,1.0,0.977777777778]
[debug] In cluster 13: 94.102.49.193 [3.85714285714,4.0,14.0357142857,18.4917127072,0.872773536896,1.0,0.884798099762]
[debug] In cluster 13: 61.155.238.89 [7.0,0.0,10.8333333333,9.76279069767,0.923076923077,1.0,0.887323943662]
[debug] In cluster 13: 113.225.220.190 [3.5,0.0,6.88888888889,6.44871794872,0.887096774194,1.0,0.830985915493]
[debug] In cluster 13: 114.251.176.214 [5.0,0.0,18.0,18.40625,0.833333333333,1.0,0.842105263158]
[debug] In cluster 13: 139.226.16.168 [0.0,0.0,0.708333333333,0.765173180807,0.852941176471,1.0,0.59756097561]
[debug] In cluster 13: 202.121.244.150 [0.0,0.0,0.964325529543,1.13519633575,0.966473988439,1.0,0.979001135074]
[debug] In cluster 13: 106.226.226.247 [5.0,0.0,5.5,5.08510638298,0.909090909091,1.0,0.769230769231]
[debug] In cluster 13: 89.110.53.102 [31.0,0.0,12.0,11.8469387755,0.777777777778,1.0,0.871794871795]
[debug] In cluster 13: 93.174.95.106 [21.3,18.0,14.3666666667,17.1362383741,0.92343387471,1.0,0.930585683297]
[debug] In cluster 13: 190.175.22.188 [6.0,0.0,1.83333333333,1.69626168224,0.818181818182,1.0,0.470588235294]
[debug] In cluster 13: 117.131.187.200 [5.0,0.0,8.16666666667,7.6703163017,0.938775510204,1.0,0.913636363636]
[debug] In cluster 13: 113.122.47.186 [61.0,0.0,29.0,28.7731958763,0.896551724138,1.0,0.977777777778]
[debug] In cluster 13: 176.53.82.66 [5.5,0.0,4.77777777778,4.48076923077,0.860465116279,1.0,0.807692307692]
[debug] In cluster 13: 218.200.55.58 [5.0,0.0,2.53125,2.55631067961,0.901234567901,1.0,0.946902654867]
[debug] In cluster 13: 27.219.170.91 [61.0,0.0,21.0,20.9274611399,0.904761904762,1.0,0.984848484848]
[debug] In cluster 13: 211.116.216.80 [30.0,0.0,19.0,19.03125,0.932330827068,1.0,0.992857142857]
[debug] In cluster 13: 218.58.59.66 [0.0,0.0,13.3333333333,6.26338329764,0.875,1.0,0.790697674419]
[debug] In cluster 13: 71.6.135.131 [0.833333333333,8.0,20.0144927536,24.4615902965,0.922519913106,1.0,0.92275862069]
[debug] In cluster 13: 62.210.189.248 [0.333333333333,0.0,7.33333333333,6.94852941176,0.954545454545,1.0,0.91]
[debug] In cluster 13: 111.40.168.90 [3.5,0.0,15.0980392157,14.384749709,0.974025974026,1.0,0.957369062119]
[debug] In cluster 13: 202.121.66.53 [10.5,0.0,3.87696335079,3.53259165396,0.791357191087,1.0,0.843263553408]
[debug] In cluster 13: 66.240.205.34 [1.0,0.0,16.2424242424,17.681372549,0.913246268657,1.0,0.907732864675]
[debug] In cluster 13: 163.172.173.62 [5.0,0.0,22.2597765363,22.6651747656,0.946542853558,1.0,0.977182658821]
[debug] In cluster 13: 61.174.123.128 [3.5,0.0,4.875,4.55714285714,0.794871794872,1.0,0.744680851064]
[debug] In cluster 13: 118.145.22.56 [5.5,0.0,7.0,6.32407407407,0.857142857143,1.0,0.791666666667]
[debug] In cluster 13: 182.243.78.75 [3.33333333333,0.0,5.85714285714,5.40322580645,0.829268292683,1.0,0.75]
[debug] In cluster 13: 89.248.172.16 [1.2,3.0,15.775,19.8727695888,0.876386687797,1.0,0.912071535022]
[debug] In cluster 13: 1.34.188.54 [216.0,0.0,84.0,84.09375,0.904761904762,1.0,0.988235294118]
[debug] In cluster 13: 122.97.218.114 [0.0,0.0,29.375,29.4474708171,0.906382978723,1.0,0.946502057613]
[debug] In cluster 13: 119.50.250.226 [61.0,0.0,68.0,66.1515151515,0.852941176471,1.0,0.971014492754]
[debug] In cluster 13: 123.249.27.191 [22.0,0.0,22.5,22.4198887532,0.949418604651,1.0,0.968579910935]
[debug] In cluster 13: 58.221.58.113 [0.0,0.0,7.20350404313,7.08918672954,0.827502338634,1.0,0.890915064892]
[debug] In cluster 13: 60.184.104.211 [5.0,0.0,7.0,6.5390070922,0.857142857143,1.0,0.71875]
[debug] In cluster 13: 115.148.82.167 [3.5,0.0,7.72727272727,7.3085106383,0.917647058824,1.0,0.875]
[debug] In cluster 13: 78.70.115.52 [31.0,0.0,31.5,31.609375,0.84126984127,1.0,0.984615384615]
[debug] In cluster 13: 112.91.82.252 [61.0,0.0,17.6,17.5776397516,0.761363636364,1.0,0.967741935484]
[debug] In cluster 13: 218.63.123.70 [60.0,0.0,20.5,20.609375,0.780487804878,1.0,0.976744186047]
[debug] In cluster 13: 115.196.26.234 [3.5,0.0,5.71428571429,5.27419354839,0.825,1.0,0.744680851064]
[debug] In cluster 13: 113.209.68.135 [60.0,0.0,2.92105263158,1.22823156225,0.810810810811,1.0,0.718120805369]
[debug] In cluster 13: 80.82.77.139 [1.2,25.0,16.0319148936,18.9940495868,0.897146648971,1.0,0.928794503435]
[debug] In cluster 13: 60.191.38.77 [2.5,0.0,26.0461538462,26.1510566763,0.985233313644,1.0,0.987485779295]
[debug] In cluster 13: 106.75.9.66 [1.5,0.0,122.0,120.727272727,0.803278688525,1.0,0.747967479675]
[debug] In cluster 13: 183.144.121.228 [60.0,0.0,22.5,22.609375,0.8,1.0,0.978723404255]
[debug] In cluster 13: 202.121.244.246 [2.69230769231,0.0,4.43347639485,2.81613615907,0.904162633107,1.0,0.862559241706]
[debug] In cluster 13: 66.240.219.146 [0.833333333333,3.0,16.125,20.6940681924,0.869186046512,1.0,0.874087591241]
[debug] In cluster 13: 1.55.86.170 [21.0,0.0,4.33333333333,4.31958762887,0.769230769231,1.0,0.875]
[debug] In cluster 13: 80.82.77.33 [1.2,27.0,18.7723577236,21.5502188716,0.914248592464,1.0,0.939555921053]
[debug] In cluster 13: 59.152.161.196 [31.0,0.0,27.5,27.4,0.836363636364,1.0,0.929824561404]
[debug] In cluster 13: 59.32.201.87 [2.5,0.0,16.3333333333,16.5,0.938775510204,1.0,0.923076923077]
[debug] In cluster 13: 183.95.174.68 [3.33333333333,0.0,34.0,34.875,0.794117647059,1.0,0.771428571429]
[debug] In cluster 13: 59.188.239.77 [1.66666666667,0.0,8.33333333333,20.2157534247,0.866666666667,1.0,0.869047619048]
[debug] In cluster 13: 153.35.0.18 [61.0,0.0,28.3333333333,28.1855670103,0.811764705882,1.0,0.965909090909]
[debug] In cluster 13: 71.6.146.130 [1.0,4.0,12.7837837838,14.8287269682,0.915433403805,1.0,0.935294117647]
[debug] In cluster 13: 39.72.29.85 [2.0,0.0,8.6,13.3333333333,0.790697674419,1.0,0.625]
[debug] In cluster 13: 210.35.68.90 [0.0,0.0,1.15451895044,1.38631221719,0.805555555556,1.0,0.473612990528]
[debug] In cluster 13: 175.100.91.178 [34.0,0.0,70.0,70.046875,0.957142857143,1.0,0.992957746479]
[debug] In cluster 13: 117.71.18.20 [3.5,0.0,24.6666666667,24.9583333333,0.891891891892,1.0,0.896103896104]
[debug] In cluster 13: 78.60.144.1 [30.0,0.0,17.6,17.5776397516,0.852272727273,1.0,0.967741935484]
[debug] In cluster 13: 114.255.78.180 [3.5,0.0,6.44444444444,6.03846153846,0.879310344828,1.0,0.820895522388]
[debug] In cluster 13: 24.107.157.190 [3.66666666667,0.0,10.4444444444,9.75320512821,0.829787234043,1.0,0.873786407767]
[debug] In cluster 13: 71.6.146.185 [5.0,7.0,13.4361702128,17.1892515661,0.89469517023,1.0,0.920412675018]
[debug] In cluster 13: 77.29.95.47 [31.0,0.0,32.0,32.21875,0.78125,1.0,0.969696969697]
[debug] In cluster 13: 66.240.192.138 [2.5,5.0,16.453125,19.1769268056,0.893637226971,1.0,0.92927484333]
[debug] In cluster 13: 114.255.78.181 [61.0,0.0,11.1666666667,10.4417475728,0.835820895522,1.0,0.931506849315]
[debug] In cluster 13: 71.6.146.186 [0.833333333333,2.0,18.5666666667,23.6710526316,0.887791741472,1.0,0.904599659284]
[debug] In cluster 13: 46.127.67.221 [61.0,0.0,45.0,45.21875,0.8,1.0,0.978260869565]
[debug] In cluster 13: 89.248.167.131 [0.8,19.0,13.2435897436,11.6666666667,0.909970958374,1.0,0.90099009901]
[debug] In cluster 13: 95.65.55.130 [31.0,0.0,17.6666666667,17.5567010309,0.849056603774,1.0,0.964285714286]
[debug] In cluster 13: 36.79.44.104 [33.0,0.0,6.0,5.96907216495,0.833333333333,1.0,0.904761904762]
[debug] In cluster 13: 186.133.237.183 [7.5,0.0,5.66666666667,6.1875,0.823529411765,1.0,0.5]
[debug] In cluster 13: 58.211.200.75 [2.75,0.0,14.5,15.46484375,0.810344827586,1.0,0.870967741935]
[debug] In cluster 13: 144.0.239.71 [61.0,0.0,10.0,9.98136645963,0.8,1.0,0.963636363636]
[debug] In cluster 13: 211.116.220.142 [30.0,0.0,36.3333333333,36.0309278351,0.926605504587,1.0,0.982142857143]
[debug] In cluster 13: 185.163.109.66 [1.2,1.0,15.5,31.5234375,0.838709677419,1.0,0.863636363636]
[debug] In cluster 13: 1.82.230.35 [7.33333333333,0.0,4.16666666667,3.60792951542,0.8,1.0,0.677419354839]
[debug] In cluster 13: 190.96.223.7 [20.0,0.0,9.0,8.81818181818,0.777777777778,1.0,0.8]
[debug] In cluster 13: 106.75.81.187 [1.71428571429,0.0,77.0,76.9161490683,0.953246753247,1.0,0.938461538462]
[debug] In cluster 13: 210.35.66.18 [0.0,0.0,0.230158730159,0.168888888889,0.827586206897,1.0,0.548387096774]
[debug] In cluster 13: 182.43.5.148 [37.0,0.0,123.5,121.707692308,0.967611336032,1.0,0.991967871486]
[debug] In cluster 13: 202.215.132.129 [3.5,0.0,33.0,33.875,0.757575757576,1.0,0.764705882353]
[debug] In cluster 13: 181.57.149.131 [61.0,0.0,18.3846153846,17.4081632653,0.949790794979,1.0,0.960317460317]
[debug] In cluster 13: 1.119.0.247 [3.5,0.0,8.625,7.98571428571,0.884057971014,1.0,0.844155844156]
[debug] In cluster 13: 78.154.179.22 [2.2,0.0,11.4,11.7777777778,0.842105263158,1.0,0.774193548387]
[debug] In cluster 13: 106.75.81.90 [1.5,0.0,11.3571428571,11.2831858407,0.905660377358,1.0,0.924855491329]
[debug] In cluster 13: 191.96.249.97 [1.0,0.0,12.3437013997,8.90976665252,0.849061358196,1.0,0.923892773893]
[debug] In cluster 13: 172.125.185.15 [40.0,0.0,50.0,50.21875,0.9,1.0,0.980392156863]
[debug] In cluster 13: 181.52.250.220 [18.0,0.0,3.33333333333,3.32989690722,0.8,1.0,0.846153846154]
[debug] In cluster 13: 36.251.49.217 [32.0,0.0,7.0,7.0,0.785714285714,1.0,0.875]
[debug] In cluster 13: 182.44.78.46 [61.0,0.0,20.0,20.21875,0.8,1.0,0.952380952381]
[debug] In cluster 13: 221.122.101.203 [4.4,0.0,11.3333333333,10.818452381,0.973529411765,1.0,0.959459459459]
[debug] In cluster 13: 42.82.168.234 [32.0,0.0,22.0,21.8837209302,0.909090909091,1.0,0.978260869565]
[debug] In cluster 13: 89.233.222.159 [31.0,0.0,35.0,35.0729166667,0.919047619048,1.0,0.990740740741]
[debug] In cluster 13: 123.16.238.46 [31.0,0.0,13.5,13.3384615385,0.777777777778,1.0,0.931034482759]
[debug] In cluster 13: 122.189.199.45 [7.0,0.0,5.25,5.02962962963,0.761904761905,1.0,0.92]
[debug] In cluster 13: 109.201.12.202 [34.0,0.0,17.0,17.09375,0.764705882353,1.0,0.944444444444]
[debug] In cluster 13: 61.164.98.59 [3.33333333333,0.0,3.75,3.52857142857,0.766666666667,1.0,0.684210526316]
[debug] In cluster 13: 114.99.240.214 [0.0,0.0,2.875,2.70178571429,0.782608695652,1.0,0.693548387097]
[debug] In cluster 13: 191.249.159.113 [6.0,0.0,11.0,10.9384615385,0.886363636364,1.0,0.916666666667]
[debug] In cluster 13: 114.84.1.182 [61.0,0.0,11.5,11.5364583333,0.913043478261,1.0,0.986666666667]
[debug] In cluster 13: 112.12.14.55 [61.0,0.0,19.2,19.1242236025,0.895833333333,1.0,0.980198019802]
[debug] In cluster 13: 198.20.69.74 [9.0,6.0,14.2058823529,18.943537415,0.836438923395,1.0,0.852998065764]
[debug] In cluster 13: 122.194.212.177 [3.5,0.0,8.2,7.70930232558,0.914634146341,1.0,0.869565217391]
[debug] In cluster 13: 112.82.171.169 [60.0,0.0,47.0,47.21875,0.787234042553,1.0,0.979166666667]
[debug] In cluster 13: 1.55.169.104 [21.0,0.0,3.0,3.0,0.777777777778,1.0,0.833333333333]
[debug] In cluster 13: 14.157.26.25 [61.0,0.0,14.0,13.9278350515,0.952380952381,1.0,0.955555555556]
[debug] In cluster 13: 143.202.208.6 [2.4,0.0,21.3333333333,21.4646464646,0.828125,1.0,0.791044776119]
[debug] In cluster 13: 27.209.34.41 [3.5,0.0,3.41666666667,3.18289786223,0.780487804878,1.0,0.716981132075]
[debug] In cluster 13: 202.121.66.117 [0.0,0.0,1.03574975174,0.681105751125,0.930009587728,1.0,0.950731707317]
[debug] In cluster 13: 163.172.134.70 [5.125,0.0,21.5219941349,21.6481321342,0.934868510696,1.0,0.964583333333]
[debug] In cluster 13: 219.77.232.8 [0.230769230769,0.0,6.93023255814,5.72743574417,0.88255033557,1.0,0.938416422287]
[debug] In cluster 13: 202.121.66.24 [4.0,27.0,0.973342447027,1.13332198587,0.823735955056,1.0,0.739868375476]
[debug] In cluster 13: 114.241.26.89 [0.0,0.0,89.0,89.40625,0.955056179775,1.0,0.966666666667]
[debug] In cluster 13: 119.132.116.87 [11.0,0.0,22.5,23.8671875,0.777777777778,1.0,0.893617021277]
[debug] In cluster 13: 113.225.211.229 [22.0,0.0,23.0,23.40625,0.913043478261,1.0,0.875]
[debug] In cluster 13: 113.190.203.238 [29.0,0.0,6.75,6.72093023256,0.777777777778,1.0,0.935483870968]
[debug] In cluster 13: 222.205.127.11 [61.0,0.0,23.0,22.4563106796,0.840579710145,1.0,0.722222222222]
[debug] In cluster 13: 122.241.10.60 [61.0,0.0,19.0,18.8762886598,0.859649122807,1.0,0.966666666667]
[debug] In cluster 13: 139.201.165.144 [3.66666666667,0.0,7.0,6.55128205128,0.873015873016,1.0,0.833333333333]
[debug] In cluster 13: 223.155.231.174 [0.4,0.0,4.21621621622,3.3936529372,0.820512820513,1.0,0.886010362694]
[debug] In cluster 13: 94.102.49.174 [5.125,0.0,18.8242074928,17.3192092971,0.963181261482,1.0,0.986335223143]
[debug] In cluster 13: 85.61.73.115 [0.5,0.0,3.2,3.24375,0.75,1.0,0.952380952381]
[debug] In cluster 13: 118.193.154.98 [3.5,0.0,20.5,20.9375,0.80487804878,1.0,0.813953488372]
[debug] In cluster 13: 37.60.214.242 [34.0,0.0,82.0,82.09375,0.939024390244,1.0,0.987951807229]
[debug] In cluster 13: 122.189.193.228 [5.0,0.0,12.6666666667,11.2752293578,0.947368421053,1.0,0.853658536585]
[debug] In cluster 13: 188.17.12.244 [0.75,0.0,4.34615384615,4.08277404922,0.840707964602,1.0,0.892086330935]
[debug] In cluster 13: 122.160.31.159 [5.0,0.0,13.0,13.40625,0.769230769231,1.0,0.785714285714]
[debug] In cluster 13: 107.182.21.213 [6.0,0.0,7.14173228346,7.21671998031,0.992282249173,1.0,0.993955512573]
[debug] In cluster 13: 112.227.214.25 [3.5,0.0,78.0,78.09375,0.897435897436,1.0,0.987341772152]
[debug] In cluster 13: 106.113.214.175 [3.5,0.0,26.4,26.575,0.931818181818,1.0,0.941605839416]
[debug] In cluster 13: 49.117.177.165 [3.5,0.0,3.92857142857,3.78813559322,0.872727272727,1.0,0.826086956522]
[debug] In cluster 13: 125.124.6.62 [3.5,0.0,69.0,69.875,0.898550724638,1.0,0.885714285714]
[debug] In cluster 13: 66.240.236.119 [0.6,6.0,15.8852459016,17.575,0.921568627451,1.0,0.938834951456]
[debug] In cluster 13: 182.243.110.127 [3.5,0.0,15.5,15.223880597,0.774193548387,1.0,0.727272727273]
[debug] In cluster 13: 125.123.179.245 [3.33333333333,0.0,4.75,4.44285714286,0.815789473684,1.0,0.739130434783]
[debug] In cluster 13: 212.47.246.179 [7.83333333333,0.0,14.9166666667,16.2133650205,0.925977653631,1.0,0.928337696335]
[debug] In cluster 13: 111.11.27.140 [3.5,0.0,22.25,20.9243902439,0.966292134831,1.0,0.94623655914]
[debug] =======================================
[debug] [Info] There are 194 anomalous IPs among 1 clusters.
[debug] Begin to optimize K-means model by choosing a K value that takes minimum cost.
[debug] Sum of squared distances of points to their nearest center when K=8 --->>> 1.5751940133692288
[debug] Sum of squared distances of points to their nearest center when K=9 --->>> 1.441711506568701
[debug] Sum of squared distances of points to their nearest center when K=10 --->>> 1.3686312704403432
[debug] Sum of squared distances of points to their nearest center when K=11 --->>> 1.2446601232142052
[debug] Sum of squared distances of points to their nearest center when K=12 --->>> 1.1136972833507586
[debug] Sum of squared distances of points to their nearest center when K=13 --->>> 1.0146531643273324
[debug] Sum of squared distances of points to their nearest center when K=14 --->>> 0.9055073842104633
[debug] Sum of squared distances of points to their nearest center when K=15 --->>> 0.9155023314827573
[debug] Sum of squared distances of points to their nearest center when K=16 --->>> 0.8232775969626513
[debug] Sum of squared distances of points to their nearest center when K=17 --->>> 0.7546247572926772
[debug] Sum of squared distances of points to their nearest center when K=18 --->>> 0.7409453921523602
[debug] Sum of squared distances of points to their nearest center when K=19 --->>> 0.6752391871312294
[debug] Sum of squared distances of points to their nearest center when K=20 --->>> 0.6333008305156812
```


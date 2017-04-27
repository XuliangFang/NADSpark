**ddosMinConnectionsThreshold=7**

**ddosMinPairsThreshold=8**

```shell
u928@master:~/fl/anomalyDetection$ spark-submit --class "runAnomalyDetection" ./anomalydetection_2.11-1.0.jar /tcpSession/raw_tcp_session_5min.txt 7 8
17/04/27 19:39:57 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
17/04/27 19:39:57 WARN SparkConf: In Spark 1.0 and later spark.local.dir will be overridden by the value set by the cluster manager (via SPARK_LOCAL_DIRS in mesos/standalone and LOCAL_DIRS in YARN).
17/04/27 19:39:57 WARN SparkConf: 
SPARK_JAVA_OPTS was detected (set to ' -Dspark.local.dir=~/jrj2/tmp -Dhadoop.tmp.dir=~/jrj2/tmp').
This is deprecated in Spark 1.0+.

Please instead use:
 - ./spark-submit with conf/spark-defaults.conf to set defaults for an application
 - ./spark-submit with --driver-java-options to set -X options for a driver
 - spark.executor.extraJavaOptions to set -X options for executors
 - SPARK_DAEMON_JAVA_OPTS to set java options for standalone daemons (master or worker)
        
17/04/27 19:39:57 WARN SparkConf: Setting 'spark.executor.extraJavaOptions' to ' -Dspark.local.dir=~/jrj2/tmp -Dhadoop.tmp.dir=~/jrj2/tmp' as a work-around.
17/04/27 19:39:57 WARN SparkConf: Setting 'spark.driver.extraJavaOptions' to ' -Dspark.local.dir=~/jrj2/tmp -Dhadoop.tmp.dir=~/jrj2/tmp' as a work-around.
[debug] -----------------------------------
[debug] Detecting server under DDoS attack!
[Stage 3:>                                                          (0 + 2) / 2][debug] IP: 210.35.99.251 - DDoS Attack: 115 Pairs: 9
[debug] alert event...
[debug] Event Title: DDoS Detection Event
[debug] Event Content: 
[debug] This IP was detected by NADSpark performing an abnormal activity. In what follows, you can see more information.
Abnormal behaviour: Host possibly under DDoS attack.
IP: 210.35.99.251
Number of Attackers: 9
Number of flows: 115
Mean/Stddev of flows per AlienIP (all flows for this IP): 4/26
Bytes Up: 3.0MB
Bytes Down: 204.8KB
Packets: 3771
Flows: 
222.199.191.48:443 => 210.35.99.251:34880 (TCP, Up: 519.4KB, Down: 33.1KB,417 pkts, duration: 5s)
222.192.186.38:80 => 210.35.99.251:49869 (TCP, Up: 285.3KB, Down: 9.2KB,243 pkts, duration: 2s)
222.200.129.53:80 => 210.35.99.251:34817 (TCP, Up: 203.8KB, Down: 6.7KB,195 pkts, duration: 32s)
222.199.191.49:80 => 210.35.99.251:28346 (TCP, Up: 199.4KB, Down: 6.6KB,165 pkts, duration: 22s)
222.192.186.38:80 => 210.35.99.251:49596 (TCP, Up: 148.4KB, Down: 8.7KB,232 pkts, duration: 30s)
222.192.186.38:80 => 210.35.99.251:49648 (TCP, Up: 80.1KB, Down: 5.8KB,110 pkts, duration: 47s)
222.192.186.38:80 => 210.35.99.251:49833 (TCP, Up: 69.2KB, Down: 1.9KB,44 pkts, duration: 0s)
222.199.191.48:80 => 210.35.99.251:35072 (TCP, Up: 66.5KB, Down: 3.7KB,67 pkts, duration: 29s)
222.192.186.38:80 => 210.35.99.251:49767 (TCP, Up: 67.0KB, Down: 1.9KB,64 pkts, duration: 4s)
222.192.186.37:80 => 210.35.99.251:34657 (TCP, Up: 64.1KB, Down: 2.9KB,58 pkts, duration: 55s)
222.199.191.48:80 => 210.35.99.251:35070 (TCP, Up: 56.4KB, Down: 2.7KB,51 pkts, duration: 20s)
222.192.186.37:80 => 210.35.99.251:34348 (TCP, Up: 54.4KB, Down: 2.5KB,53 pkts, duration: 45s)
58.205.220.44:80 => 210.35.99.251:14556 (TCP, Up: 53.5KB, Down: 2.6KB,54 pkts, duration: 0s)
222.200.129.53:80 => 210.35.99.251:34809 (TCP, Up: 51.7KB, Down: 2.5KB,65 pkts, duration: 39s)
58.205.220.44:80 => 210.35.99.251:14682 (TCP, Up: 51.2KB, Down: 2.5KB,51 pkts, duration: 0s)
222.192.186.37:80 => 210.35.99.251:35045 (TCP, Up: 45.7KB, Down: 3.0KB,56 pkts, duration: 27s)
222.199.191.48:80 => 210.35.99.251:34692 (TCP, Up: 44.0KB, Down: 4.3KB,54 pkts, duration: 20s)
222.199.191.48:80 => 210.35.99.251:35063 (TCP, Up: 44.9KB, Down: 3.3KB,49 pkts, duration: 27s)
222.192.186.38:443 => 210.35.99.251:49837 (TCP, Up: 43.5KB, Down: 2.4KB,49 pkts, duration: 2s)
58.205.220.44:80 => 210.35.99.251:14561 (TCP, Up: 41.7KB, Down: 2.4KB,52 pkts, duration: 0s)
58.205.220.44:443 => 210.35.99.251:14698 (TCP, Up: 39.0KB, Down: 4.4KB,75 pkts, duration: 2s)
222.199.191.48:80 => 210.35.99.251:35067 (TCP, Up: 40.4KB, Down: 2.2KB,36 pkts, duration: 20s)
222.199.191.49:80 => 210.35.99.251:28290 (TCP, Up: 37.1KB, Down: 2.2KB,43 pkts, duration: 21s)
58.205.220.44:80 => 210.35.99.251:14663 (TCP, Up: 36.2KB, Down: 2.8KB,53 pkts, duration: 0s)
222.192.186.38:80 => 210.35.99.251:49661 (TCP, Up: 33.3KB, Down: 1.7KB,41 pkts, duration: 32s)
222.192.186.38:80 => 210.35.99.251:49801 (TCP, Up: 29.0KB, Down: 2.0KB,36 pkts, duration: 0s)
222.192.186.38:443 => 210.35.99.251:49670 (TCP, Up: 26.6KB, Down: 2.1KB,38 pkts, duration: 7s)
58.205.212.206:80 => 210.35.99.251:14641 (TCP, Up: 26.4KB, Down: 1.9KB,29 pkts, duration: 0s)
222.199.191.48:80 => 210.35.99.251:35452 (TCP, Up: 26.2KB, Down: 1.9KB,30 pkts, duration: 21s)
222.199.191.49:80 => 210.35.99.251:28410 (TCP, Up: 26.8KB, Down: 968 B,23 pkts, duration: 0s)
58.205.220.44:80 => 210.35.99.251:14678 (TCP, Up: 25.2KB, Down: 2.0KB,37 pkts, duration: 0s)
58.205.212.206:80 => 210.35.99.251:14506 (TCP, Up: 24.8KB, Down: 2.0KB,30 pkts, duration: 0s)
58.205.220.44:80 => 210.35.99.251:14658 (TCP, Up: 24.4KB, Down: 1.6KB,28 pkts, duration: 0s)
222.199.191.49:80 => 210.35.99.251:28280 (TCP, Up: 23.6KB, Down: 1.1KB,25 pkts, duration: 0s)
222.200.129.53:443 => 210.35.99.251:35380 (TCP, Up: 21.5KB, Down: 2.4KB,46 pkts, duration: 32s)
222.199.191.49:80 => 210.35.99.251:28420 (TCP, Up: 19.1KB, Down: 1.6KB,24 pkts, duration: 21s)
222.200.129.53:80 => 210.35.99.251:35235 (TCP, Up: 18.4KB, Down: 1.3KB,27 pkts, duration: 9s)
222.199.191.48:80 => 210.35.99.251:34829 (TCP, Up: 17.1KB, Down: 1.9KB,25 pkts, duration: 20s)
222.200.129.53:80 => 210.35.99.251:35396 (TCP, Up: 17.3KB, Down: 1.7KB,34 pkts, duration: 31s)
58.205.220.44:80 => 210.35.99.251:14553 (TCP, Up: 17.3KB, Down: 1.4KB,26 pkts, duration: 0s)
222.199.191.48:80 => 210.35.99.251:35329 (TCP, Up: 16.6KB, Down: 1.7KB,20 pkts, duration: 20s)
222.192.186.37:80 => 210.35.99.251:35305 (TCP, Up: 16.4KB, Down: 1022 B,20 pkts, duration: 31s)
222.199.191.32:443 => 210.35.99.251:49862 (TCP, Up: 16.2KB, Down: 1.2KB,29 pkts, duration: 1s)
222.200.129.53:443 => 210.35.99.251:34955 (TCP, Up: 14.0KB, Down: 1.9KB,32 pkts, duration: 25s)
222.192.186.37:80 => 210.35.99.251:35044 (TCP, Up: 14.6KB, Down: 1.2KB,23 pkts, duration: 18s)
58.205.220.30:80 => 210.35.99.251:35471 (TCP, Up: 14.7KB, Down: 956 B,21 pkts, duration: 0s)
222.200.129.53:443 => 210.35.99.251:35397 (TCP, Up: 11.9KB, Down: 1.9KB,29 pkts, duration: 15s)
58.205.220.44:80 => 210.35.99.251:14588 (TCP, Up: 12.5KB, Down: 924 B,22 pkts, duration: 0s)
58.205.220.44:80 => 210.35.99.251:14667 (TCP, Up: 12.1KB, Down: 1.2KB,17 pkts, duration: 0s)
222.199.191.48:80 => 210.35.99.251:34920 (TCP, Up: 10.9KB, Down: 1.8KB,18 pkts, duration: 21s)
58.205.220.44:80 => 210.35.99.251:14564 (TCP, Up: 11.5KB, Down: 1.2KB,22 pkts, duration: 0s)
222.192.186.37:80 => 210.35.99.251:34669 (TCP, Up: 10.3KB, Down: 1004 B,17 pkts, duration: 0s)
222.192.186.37:80 => 210.35.99.251:35205 (TCP, Up: 10.1KB, Down: 1.1KB,21 pkts, duration: 16s)
58.205.220.30:443 => 210.35.99.251:35583 (TCP, Up: 9.2KB, Down: 2.0KB,26 pkts, duration: 1s)
58.205.220.30:443 => 210.35.99.251:35578 (TCP, Up: 8.1KB, Down: 856 B,20 pkts, duration: 1s)
222.192.186.37:80 => 210.35.99.251:34660 (TCP, Up: 7.8KB, Down: 1.1KB,16 pkts, duration: 0s)
58.205.220.44:80 => 210.35.99.251:14687 (TCP, Up: 7.0KB, Down: 1.1KB,15 pkts, duration: 0s)
58.205.220.30:443 => 210.35.99.251:35055 (TCP, Up: 5.8KB, Down: 1.6KB,28 pkts, duration: 31s)
222.200.129.53:443 => 210.35.99.251:35100 (TCP, Up: 5.7KB, Down: 1.6KB,24 pkts, duration: 10s)
58.205.220.44:80 => 210.35.99.251:14670 (TCP, Up: 6.1KB, Down: 1.0KB,14 pkts, duration: 0s)
58.205.212.206:443 => 210.35.99.251:14998 (TCP, Up: 5.4KB, Down: 1.5KB,19 pkts, duration: 21s)
222.199.191.32:443 => 210.35.99.251:49590 (TCP, Up: 5.4KB, Down: 852 B,15 pkts, duration: 10s)
58.205.220.44:80 => 210.35.99.251:14568 (TCP, Up: 5.2KB, Down: 1.0KB,14 pkts, duration: 0s)
222.192.186.37:80 => 210.35.99.251:34806 (TCP, Up: 5.5KB, Down: 747 B,13 pkts, duration: 3s)
222.192.186.38:80 => 210.35.99.251:49544 (TCP, Up: 4.5KB, Down: 1.3KB,25 pkts, duration: 4s)
222.192.186.37:80 => 210.35.99.251:35422 (TCP, Up: 4.8KB, Down: 704 B,12 pkts, duration: 20s)
58.205.220.30:443 => 210.35.99.251:35585 (TCP, Up: 3.7KB, Down: 1.4KB,18 pkts, duration: 1s)
58.205.220.44:80 => 210.35.99.251:14623 (TCP, Up: 4.1KB, Down: 837 B,10 pkts, duration: 0s)
58.205.220.30:443 => 210.35.99.251:35588 (TCP, Up: 2.9KB, Down: 1.6KB,20 pkts, duration: 1s)
222.199.191.48:443 => 210.35.99.251:35478 (TCP, Up: 4.0KB, Down: 574 B,15 pkts, duration: 12s)
58.205.212.206:443 => 210.35.99.251:15015 (TCP, Up: 3.0KB, Down: 1.5KB,18 pkts, duration: 16s)
58.205.220.44:80 => 210.35.99.251:14576 (TCP, Up: 3.5KB, Down: 782 B,12 pkts, duration: 0s)
222.199.191.32:443 => 210.35.99.251:49700 (TCP, Up: 3.3KB, Down: 852 B,15 pkts, duration: 16s)
58.205.220.44:80 => 210.35.99.251:14577 (TCP, Up: 3.4KB, Down: 712 B,10 pkts, duration: 0s)
222.199.191.32:443 => 210.35.99.251:49701 (TCP, Up: 3.3KB, Down: 724 B,14 pkts, duration: 16s)
222.200.129.53:80 => 210.35.99.251:35084 (TCP, Up: 2.8KB, Down: 654 B,11 pkts, duration: 7s)
58.205.220.30:80 => 210.35.99.251:35420 (TCP, Up: 1.5KB, Down: 1.8KB,16 pkts, duration: 30s)
58.205.220.30:80 => 210.35.99.251:34658 (TCP, Up: 2.8KB, Down: 509 B,12 pkts, duration: 0s)
58.205.220.44:443 => 210.35.99.251:14168 (TCP, Up: 2.1KB, Down: 895 B,12 pkts, duration: 15s)
222.192.186.37:80 => 210.35.99.251:34632 (TCP, Up: 2.1KB, Down: 860 B,9 pkts, duration: 0s)
222.199.191.49:80 => 210.35.99.251:28295 (TCP, Up: 1.8KB, Down: 796 B,10 pkts, duration: 20s)
222.192.186.38:80 => 210.35.99.251:49557 (TCP, Up: 2.1KB, Down: 192 B,7 pkts, duration: 0s)
58.205.220.44:80 => 210.35.99.251:14840 (TCP, Up: 1.7KB, Down: 486 B,11 pkts, duration: 0s)
58.205.212.206:80 => 210.35.99.251:14803 (TCP, Up: 647 B, Down: 1.4KB,7 pkts, duration: 0s)
58.205.212.206:80 => 210.35.99.251:14636 (TCP, Up: 128 B, Down: 1.5KB,7 pkts, duration: 0s)
58.205.212.206:80 => 210.35.99.251:14786 (TCP, Up: 672 B, Down: 735 B,9 pkts, duration: 0s)
58.205.220.44:80 => 210.35.99.251:14845 (TCP, Up: 919 B, Down: 439 B,10 pkts, duration: 0s)
222.199.191.32:443 => 210.35.99.251:49672 (TCP, Up: 476 B, Down: 505 B,9 pkts, duration: 0s)
58.205.212.206:80 => 210.35.99.251:14644 (TCP, Up: 590 B, Down: 198 B,6 pkts, duration: 0s)
222.200.129.53:443 => 210.35.99.251:34635 (TCP, Up: 193 B, Down: 448 B,8 pkts, duration: 0s)
222.199.191.48:443 => 210.35.99.251:34418 (TCP, Up: 217 B, Down: 281 B,7 pkts, duration: 0s)
222.199.191.49:443 => 210.35.99.251:28350 (TCP, Up: 198 B, Down: 270 B,7 pkts, duration: 23s)
222.199.191.48:443 => 210.35.99.251:33711 (TCP, Up: 153 B, Down: 281 B,6 pkts, duration: 0s)
222.200.129.53:443 => 210.35.99.251:35319 (TCP, Up: 281 B, Down: 153 B,6 pkts, duration: 0s)
222.199.191.48:443 => 210.35.99.251:34448 (TCP, Up: 153 B, Down: 281 B,6 pkts, duration: 0s)
58.205.220.30:80 => 210.35.99.251:35258 (TCP, Up: 198 B, Down: 198 B,6 pkts, duration: 11s)
222.199.191.49:80 => 210.35.99.251:28441 (TCP, Up: 198 B, Down: 198 B,6 pkts, duration: 10s)
222.199.191.48:443 => 210.35.99.251:34457 (TCP, Up: 153 B, Down: 217 B,5 pkts, duration: 0s)
222.199.191.32:443 => 210.35.99.251:49579 (TCP, Up: 213 B, Down: 128 B,5 pkts, duration: 0s)
222.199.191.49:80 => 210.35.99.251:28444 (TCP, Up: 134 B, Down: 198 B,5 pkts, duration: 10s)
222.199.191.48:80 => 210.35.99.251:34686 (TCP, Up: 134 B, Down: 198 B,5 pkts, duration: 18s)
222.200.129.53:80 => 210.35.99.251:34683 (TCP, Up: 134 B, Down: 198 B,5 pkts, duration: 22s)
222.199.191.48:80 => 210.35.99.251:34928 (TCP, Up: 134 B, Down: 198 B,5 pkts, duration: 11s)
222.192.186.37:80 => 210.35.99.251:35439 (TCP, Up: 134 B, Down: 198 B,5 pkts, duration: 0s)
222.199.191.49:80 => 210.35.99.251:28437 (TCP, Up: 134 B, Down: 198 B,5 pkts, duration: 10s)
222.199.191.49:80 => 210.35.99.251:28276 (TCP, Up: 198 B, Down: 128 B,5 pkts, duration: 13s)
222.199.191.49:80 => 210.35.99.251:28473 (TCP, Up: 130 B, Down: 194 B,5 pkts, duration: 6s)
222.199.191.48:80 => 210.35.99.251:35332 (TCP, Up: 130 B, Down: 194 B,5 pkts, duration: 6s)
222.199.191.49:80 => 210.35.99.251:28452 (TCP, Up: 130 B, Down: 194 B,5 pkts, duration: 6s)
222.199.191.48:443 => 210.35.99.251:34860 (TCP, Up: 153 B, Down: 153 B,4 pkts, duration: 0s)
222.199.191.32:443 => 210.35.99.251:49481 (TCP, Up: 153 B, Down: 128 B,4 pkts, duration: 0s)
222.199.191.49:443 => 210.35.99.251:28217 (TCP, Up: 153 B, Down: 128 B,4 pkts, duration: 0s)
222.199.191.32:443 => 210.35.99.251:49798 (TCP, Up: 149 B, Down: 128 B,4 pkts, duration: 0s)
222.199.191.32:443 => 210.35.99.251:49570 (TCP, Up: 149 B, Down: 128 B,4 pkts, duration: 0s)
222.199.191.49:80 => 210.35.99.251:28273 (TCP, Up: 134 B, Down: 128 B,4 pkts, duration: 13s)

Detecting Abused SMTP Server
[debug] SIZESIZESIZE Map((202.121.223.3,182.254.34.84) -> 1, (202.121.223.30,182.254.34.84) -> 1)

```


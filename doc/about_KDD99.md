#### Basic statistic

23种异常类型

```
(smurf.,280790)
(neptune.,107201)
(normal.,97278)
(back.,2203)
(satan.,1589)
(ipsweep.,1247)
(portsweep.,1040)
(warezclient.,1020)
(teardrop.,979)
(pod.,264)
(nmap.,231)
(guess_passwd.,53)
(buffer_overflow.,30)
(land.,21)
(warezmaster.,20)
(imap.,12)
(rootkit.,10)
(loadmodule.,9)
(ftp_write.,8)
(multihop.,7)
(phf.,4)
(perl.,3)
(spy.,2)
```

#### K=2 run kmeans algorithm

```
0   back.               2203
0   buffer_overflow.    30
0   ftp_write.          8
0   guess_passwd.       53
0   imap.               12
0   ipsweep.            1247
0   land.               21
0   loadmodule.         9
0   multihop.           7
0   neptune.            107201
0   nmap.               231
0   normal.             97278
0   perl.               3
0   phf.                4
0   pod.                264
0   portsweep.          1039
0   rootkit.            10
0   satan.              1589
0   smurf.              280790
0   spy.                2
0   teardrop.           979
0   warezclient.        1020
0   warezmaster.        20
1   portsweep.          1
```

#### Compute cost to get an optimized K

```shell
(5,1779.3473960726312)
(10,1054.5660956505587)
(15,998.6026754769782)
(20,438.0714456623944)
(25,386.70458251397577)
(30,329.4472646112194)
(35,644.939843705805)
(40,221.3547720824891)
# cost(k=35) < cost(k=40)
```

#### K=35 run kmeans algorithm

```shell
0   buffer_overflow.        1                                               
0	ftp_write.	6
0	guess_passwd.	52
0	imap.	9
0	ipsweep.	1247
0	land.	21
0	loadmodule.	2
0	multihop.	2
0	neptune.	107201
0	nmap.	231
0	normal.	48592
0	pod.	5
0	portsweep.	962
0	rootkit.	6
0	satan.	1588
0	smurf.	24
0	spy.	1
0	teardrop.	979
0	warezclient.	590
0	warezmaster.	2
1	portsweep.	1
2	warezclient.	59
3	normal.	1
3	warezmaster.	15
4	normal.	2
5	normal.	23
6	normal.	8
7	normal.	42
8	multihop.	1
8	normal.	10
9	normal.	2
10	back.	2173
10	normal.	70
11	buffer_overflow.	20
11	ftp_write.	1
11	guess_passwd.	1
11	loadmodule.	4
11	normal.	21151
11	perl.	3
11	rootkit.	1
11	spy.	1
11	warezclient.	275
11	warezmaster.	1
12	back.	20
12	normal.	87
13	normal.	2
14	buffer_overflow.	1
14	normal.	237
15	normal.	4
16	normal.	4
17	normal.	3
18	ftp_write.	1
18	normal.	737
19	normal.	3
20	normal.	3173
20	portsweep.	2
20	warezclient.	10
21	normal.	64
22	normal.	78
22	portsweep.	56
23	warezclient.	1
24	normal.	659
24	portsweep.	19
24	warezclient.	22
25	multihop.	1
25	normal.	4
25	warezmaster.	1
26	buffer_overflow.	1
26	normal.	3628
27	normal.	12
28	multihop.	1
28	normal.	1425
28	rootkit.	1
29	buffer_overflow.	7
29	imap.	1
29	loadmodule.	3
29	multihop.	1
29	normal.	7226
29	phf.	4
29	rootkit.	2
29	warezmaster.	1
30	imap.	1
30	normal.	9
31	normal.	21
32	normal.	3
33	back.	10
33	normal.	1233
33	warezclient.	32
34	imap.	1
34	multihop.	1
34	normal.	8765
34	pod.	259
34	satan.	1
34	smurf.	280766
34	warezclient.	31

```

#### k=35 with 4 dimensions KDDCUP 99 data set

duration, bytesup, bytesdown, urgent pkts

```shell
0       buffer_overflow.        2                                               
0	ftp_write.	4
0	guess_passwd.	52
0	imap.	9
0	ipsweep.	1247
0	land.	21
0	loadmodule.	5
0	multihop.	2
0	neptune.	107201
0	nmap.	231
0	normal.	56929
0	portsweep.	962
0	rootkit.	6
0	satan.	1588
0	spy.	2
0	teardrop.	979
0	warezclient.	590
0	warezmaster.	2
1	portsweep.	1
2	warezclient.	59
3	normal.	1
3	warezmaster.	15
4	imap.	1
4	normal.	8
5	normal.	22
6	normal.	5
7	normal.	7
8	normal.	2
9	normal.	2
10	back.	2173
10	normal.	70
11	buffer_overflow.	1
11	multihop.	1
11	normal.	1947
11	rootkit.	1
12	normal.	3
13	normal.	2
14	imap.	1
14	loadmodule.	1
14	multihop.	1
14	normal.	6392
14	phf.	4
14	rootkit.	1
14	warezmaster.	1
15	normal.	12
16	normal.	31
17	buffer_overflow.	25
17	ftp_write.	1
17	guess_passwd.	1
17	loadmodule.	3
17	normal.	16679
17	perl.	3
17	rootkit.	2
17	warezclient.	275
17	warezmaster.	1
18	normal.	58
19	back.	10
19	normal.	1232
19	warezclient.	32
20	ftp_write.	1
20	normal.	878
21	normal.	21
22	buffer_overflow.	1
22	ftp_write.	2
22	imap.	1
22	multihop.	1
22	normal.	10888
22	pod.	264
22	satan.	1
22	smurf.	280790
22	warezclient.	31
23	normal.	1433
23	portsweep.	7
23	warezclient.	22
24	normal.	1
25	normal.	27
26	normal.	1
27	buffer_overflow.	1
27	normal.	235
28	normal.	4
29	multihop.	1
29	normal.	5
29	warezmaster.	1
30	normal.	281
30	portsweep.	70
30	warezclient.	10
31	warezclient.	1
32	back.	20
32	normal.	87
33	normal.	5
34	multihop.	1
34	normal.	10

```


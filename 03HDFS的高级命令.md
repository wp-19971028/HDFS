## hdfs安全模式

- hdfs的安全模式是hdfs的一种保护机制，当hdfs刚启动的时候，此时hdfs会自动进入安全模式，datanode和namenode会进行块的校验，此时无法进行hdfs数据的写入和修改和删除，只能进行查询。
- hdfs默认的副本率0.999，默认3备份的情况下，三个副本每个副本都要启动。
- 默认情况下，hdfs启动到达30s后，会自动的退出安全模式，此时数据块都校验完毕。
- 如果hdfs中的块副本不够，此时就要新增，如果多余设定副本（默认3个副本），就会删除多余副本。
- 安全模式的操作命令

```sh
#查看安全模式状态
[root@node1 data]# hdfs dfsadmin -safemode get
Safe mode is OFF
#进入安全模式
[root@node1 data]# hdfs dfsadmin -safemode enter
Safe mode is ON
您在 /var/spool/mail/root 中有新邮件
#离开安全模式
[root@node1 data]# hdfs dfsadmin -safemode leave
Safe mode is OFF
[root@node1 data]#
```

## HDFS的基准测试

- 目的：验证HDFS的读写的效率，就是其吞吐量的问题
- 如何测试写入的速度呢



```sh
# 向HDFS文件系统中写入数据,10个文件,每个文件10MB,文件存放到/benchmarks/TestDFSIO中
hadoop jar /export/server/hadoop-2.7.5/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-2.7.5.jar TestDFSIO -write  -nrFiles 10 -fileSize 10MB
```

```sh
# 完成之后查看写入速度结果
hadoop fs -text /benchmarks/TestDFSIO/io_write/part-00000
```

```sh
# 测试读取的速度
hadoop jar /export/server/hadoop-2.7.5/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-2.7.5.jar TestDFSIO -read -nrFiles 10 -fileSize 10MB
```

```sh
# 删除基于测试数据呢
hadoop jar /export/server/hadoop-2.7.5/share/hadoop/mapreduce/hadoop-mapreduce-client-jobclient-2.7.5.jar TestDFSIO -clean
```




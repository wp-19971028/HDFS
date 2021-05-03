# NameNode的原理

- NameNode在内存中保存着整个文件系统的**名称空间**和文件数据块的**地址映射**，整个HDFS可存储的文件数受限于**NameNode的内存大小** 。

- **NameNode元数据信息** 文件名，文件目录结构，文件属性(生成时间，副本数，权限)每个文件的块列表。 以及列表中的块与块所在的DataNode之间的地址映射关系 在内存中加载文件系统中每个文件和每个数据块的引用关系(文件、block、datanode之间的映射信息) 数据会定期保存到本地磁盘（fsImage文件和edits文件）

  **NameNode文件操作** NameNode负责文件元数据的操作 DataNode负责处理文件内容的读写请求，数据流不经过NameNode，会询问它跟那个DataNode联系

  **dataNode副本**文件数据块到底存放到哪些DataNode上，是由NameNode决定的，NameNode根据全局情况做出放置副本的决定 

  **NameNode心跳机制**全权管理数据块的复制，周期性的接受心跳和块的状态报告信息（包含该DataNode上所有数据块的列表） 若接受到心跳信息，NameNode认为DataNode工作正常，如果在10分钟后还接受到不到DataNode的心跳，那么NameNode认为DataNode已经宕机 ,这时候NN准备要把DN上的数据块进行重新的复制。 块的状态报告包含了一个DataNode上所有数据块的列表，blocks report 每隔1小时发送一次.

  ![image-20200819150927768](./assets\image-20200819150927768-1620009231148.png)

# DataNode的原理

- Data Node以数据块的形式存储HDFS文件
- DataNode也称为Slave。
- NameNode和DataNode会保持不断通信。
- DataNode启动时，它将自己发布到NameNode并汇报自己负责持有的块列表。
- 当某个DataNode关闭时，它不会影响数据或群集的可用性。NameNode将安排由其他DataNode管理的块进行副本复制。
- DataNode所在机器通常配置有大量的硬盘空间。因为实际数据存储在DataNode中。
- DataNode会定期（dfs.heartbeat.interval配置项配置，默认是3秒）向NameNode发送心跳，如果NameNode长时间没有接受到DataNode发送的心跳， NameNode就会认为该DataNode失效。timeout  = 2 * heartbeat.recheck.interval + 10 * dfs.heartbeat.interval，而默认的heartbeat.recheck.interval 大小为5分钟（单位毫秒），dfs.heartbeat.interval默认的大小为3秒。所以namenode如果在10分钟+30秒后，仍然没有收到datanode的心跳，就认为datanode已经宕机，并标记为dead。
- datanode中的数据block汇报时间间隔取参数dfs.blockreport.intervalMsec,参数未配置的话默认为6小时.




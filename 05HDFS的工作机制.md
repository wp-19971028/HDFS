# HDFS的写入机制

写入机制流程：![image-20200819154918889](./assets\image-20200819154918889-1620009413624.png)

详细步骤解析：

1. client发起文件上传请求，通过RPC与NameNode建立通讯，NameNode检查目标文件是否已存在，父目录是否存在，返回是否可以上传；
2. client请求第一个 block该传输到哪些DataNode服务器上；
3. NameNode根据配置文件中指定的备份数量及副本放置策略进行文件分配，返回可用的DataNode的地址，如：A，B，C；
4. client请求3台DataNode中的一台A上传数据（本质上是一个RPC调用，建立pipeline），A收到请求会继续调用B，然后B调用C，将整个pipeline建立完成，后逐级返回client；
5. client开始往A上传第一个block（先从磁盘读取数据放到一个本地内存缓存），以packet为单位（默认64K），A收到一个packet就会传给B，B传给C；A每传一个packet会放入一个应答队列等待应答。
6. 数据被分割成一个个packet数据包在pipeline上依次传输，在pipeline反方向上，逐个发送ack（命令正确应答），最终由pipeline中第一个DataNode节点A将pipeline ack发送给client;
7. 当一个block传输完成之后，client再次请求NameNode上传第二个block到服务器。

# HDFS的读取机制

![image-20200819162355075](./assets\image-20200819162355075-1620009448040.png)

详细步骤解析：

1. Client向NameNode发起RPC请求，来确定请求文件block所在的位置；
2. NameNode会视情况返回文件的部分或者全部block列表，对于每个block，NameNode都会返回含有该block副本的DataNode地址；
3. 这些返回的DN地址，会按照集群拓扑结构得出DataNode与客户端的距离，然后进行排序，排序两个规则：网络拓扑结构中距离Client近的排靠前；心跳机制中超时汇报的DN状态为STALE，这样的排靠后；
4. Client选取排序靠前的DataNode来读取block，如果客户端本身就是DataNode,那么将从本地直接获取数据；底层上本质是建立Socket Stream（FSDataInputStream），重复的调用父类DataInputStream的read方法，直到这个块上的数据读取完毕；
5. 当读完列表的block后，若文件读取还没有结束，客户端会继续向NameNode获取下一批的block列表；
6. 读取完一个block都会进行checksum验证，如果读取DataNode时出现错误，客户端会通知NameNode，然后再从下一个拥有该block副本的DataNode继续读。
7. read方法是并行的读取block信息，不是一块一块的读取；NameNode只是返回Client请求包含块的DataNode地址，并不是返回请求块的数据；

最终读取来所有的block会合并成一个完整的最终文件。
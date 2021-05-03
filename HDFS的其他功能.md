## 集群内部的文件拷贝

#### 本地复制到远程——两个节点间数据拷贝

- 方式1：指定用户名，命令执行后需要再输入密码；

scp -r local_folder remote_username@remote_ip:remote_folder 

```shell
[hadoop]scp -r core-site.xml root@node2:$PWD
```

- 方式2 : 没有指定用户名，命令执行后需要输入用户名和密码；

scp -r local_folder remote_ip:remote_folder 

```shell
[hadoop]scp -r core-site.xml node2:$PWD
```

注意，如果实现了ssh免密登录之后，则不需要输入密码即可拷贝。

#### 从其他节点复制到本地

```shell
scp root@node2:/root/core-site.xml /root/core-site.xml
```

### 不同集群之间的数据拷贝

distcp（distributed copy）是一款被用于大型集群间/集群内的复制工具，数据的迁移。该命令的内部原理是MapReduce。

```shell
#进入当前目录
cd /export/server/hadoop-2.7.5/
#将一个集群中的文件拷贝到另外一个集群中，使用 hadoop distcp
bin/hadoop distcp hdfs://node1:8020/jdk-8u241-linux-x64.tar.gz  hdfs://cluster2:8020/
```

# 归档 archive 文件的使用

> HDFS并不擅长存储小文件，因为每个文件最少一个block，每个block的元数据都会在NameNode占用内存，如果存在大量的小文件，它们会吃掉NameNode节点的大量内存。
>
> Hadoop Archives可以有效的处理以上问题，它可以把多个文件归档成为一个文件，归档成一个文件后还可以透明的访问每一个文件。

- 功能：对存储的小文件进行合并成为大文件。
- 目的：减少hdfs中小文件的数量。
- 如何进行小文件的归档操作呢：
  - 使用 HDFS JAVA API 实现小文件的合并
    - 场景：初始文件一般都必须在本地，而且文件的类型都要一致
    - 缺点：如果多种类型的文件，那么没办法归档成一个文件；如果合并之后的文件，想要拆分，只能自己写代码还是先将归档的文件解压到指定目录。
  - 使用 hadoop 归档操作命令，使用 hadoop archive shell命令实现归档文件操作。
    - 场景：这些文件已经存在于HDFS 中，对HDFS中某个目录下文件进行归档
- 什么是归档文件
  - 归档文件，可以理解为 将多个文件压到一起，类似于 Linux 中 `tar cvf archive.tar a.txt b.txt` 这个命令只是将多个小文件进行了压成一个文件，而并没有缩小存储。
  - 归档文件注意：
    - 归档文件的后缀名 .har 
    - 启动归档，底层会运行 MapReduce程序，必须启动yarn集群
    - 创建归档文件之后，源文件不会被删除或者修改，归档的文档文件一旦创建就不能修改了。
- 如何使用归档文件，如何创建？

```sh
#格式
hadoop archive -archiveName name -p <parent> <src>* <dest>
#示例.将config2 下的所有内容归档保存到 outputdir 文件夹下
#                       指定归档后的名字   指定归档目录  指定归档后存储的目录
hadoop archive -archiveName wp.har -p /wp1/ /wp2
```

- 如何查看归档文件的内容？

~~~sh
#1.直接在web browser 50070端口web查看
http://node1:50070/explorer.html#/wp3
#2.通过shell 命令进行查看
hadoop fs -ls /wp3/wp.har

[root@node1 current]# hadoop fs -ls /wp3/wp.har
Found 4 items
-rw-r--r--   3 root supergroup          0 2021-05-03 18:33 /wp3/wp.har/_SUCCESS
-rw-r--r--   5 root supergroup        395 2021-05-03 18:33 /wp3/wp.har/_index
-rw-r--r--   5 root supergroup         23 2021-05-03 18:33 /wp3/wp.har/_masterindex
-rw-r--r--   3 root supergroup       1018 2021-05-03 18:33 /wp3/wp.har/part-0
您在 /var/spool/mail/root 中有新邮件

#3.查看 har 文件中原有的文件列表
#格式
#har://scheme-hostname:port/archivepath/fileinarchive   
hadoop fs -ls har://hdfs-node1:8020/wp3/wp.har

[root@node1 current]# hadoop fs -ls har://hdfs-node1:8020/wp3/wp.har
Found 5 items
-rw-r--r--   3 root supergroup         17 2021-05-02 16:44 har://hdfs-node1:8020/wp3/wp.har/1.txt
-rw-r--r--   3 root supergroup         17 2021-05-02 16:44 har://hdfs-node1:8020/wp3/wp.har/2.txt
-rw-r--r--   3 root supergroup         34 2021-05-02 16:44 har://hdfs-node1:8020/wp3/wp.har/3.txt
-rw-r--r--   3 root supergroup         12 2021-05-02 16:44 har://hdfs-node1:8020/wp3/wp.har/b.txt
-rw-r--r--   3 root supergroup        938 2021-05-03 14:48 har://hdfs-node1:8020/wp3/wp.har/hosts.txt
您在 /var/spool/mail/root 中有新邮件

#4.查看 har 文件中某一个文件的内容
hadoop fs -cat har://hdfs-node1:8020/wp3/wp.har/1.txt

[root@node1 current]# hadoop fs -cat har://hdfs-node1:8020/wp3/wp.har/1.txt
“Hello HDFS”
您在 /var/spool/mail/root 中有新邮件
#5.将归档的文件解压出来
#5.1 创建一个文件夹 config3
hdfs dfs -mkdir -p /wp4
#5.2 将归档的文件解压到config3中
hadoop fs -cp har://hdfs-node1:8020/wp3/wp.har/* /wp4
~~~

![1620038704619](./assets\1620038704619.png)

# HDFS 的快照的使用

> 照顾名思义，就是相当于对hdfs文件系统做一个备份，可以通过快照对指定的文件夹设置备份，但是添加快照之后，并不会立即复制所有文件，而是指向同一个文件。当写入发生时，才会产生新文件。

- 快照就是snapshot （几乎不用）
- hdfs 的快照什么场景上使用：
  - 数据的备份
  - 放置用户操作不当出现错误的操作
  - 试验、测试
  - 灾备恢复
- hdfs 的快照是什么呢？
  - 相当于对HDFS中的某一个文件夹进行 拍照，保持当前这个文件夹的一个状态信息（差异化快照）
  - 差异化快照：拍完快照，快照文件只是对源文件的映射关系匹配。
- hdfs 的快照主要是针对文件夹。
- 如何进行快照的使用

```sh
#开启快照
hdfs dfsadmin -allowSnapshot /wp1
#创建快照
hdfs dfs -createSnapshot /wp1 backup_config3_2020503
#修改一下文件在创建一个快照
hdfs dfs -rm -r /config3/core-site.xml
hdfs dfs -createSnapshot /wp1 backup_config3_20200820_1523
#查看快照
hdfs dfs -ls /wp1/.snapshot
#重命名快照
hdfs dfs renameSnapshot /wp1 backup_wp1_20210503 backup_wp2_20210503
#删除快照
hdfs dfs -deleteSnapshot /wp1 backup_wp1_20210503 
#禁用快照
hdfs dfsadmin -disallowSnapshot /wp1
#列出当前用户可快照的目录
hdfs lsSnapshottableDir
```

> 总结：HDFS的快照功能虽然能够保证数据的安全性，但是一般不建议大家使用，快照功能会占用非常大的磁盘空间。HDFS本身是带3备份，不能放置数据丢失，这个时候就开启快照功能。

# HDFS的Trash回收站功能

>  和Linux系统的回收站设计一样，HDFS会为**每一个用户**创建一个回收站目录：**/user/用户名/.Trash/current**，每一个被用户通过Shell删除的文件/目录，在系统回收站中都一个周期，也就是当系统回收站中的文件/目录在一段时间之后没有被用户恢复的话，HDFS就会自动的把这个文件/目录彻底删除，之后，用户就永远也找不回这个文件/目录了。
>
> 如果检查点已经启用，会定期使用时间戳重命名Current目录。.Trash中的文件在用户可配置的时间延迟后被永久删除。回收站中的文件和目录可以简单地通过将它们移动到.Trash目录之外的位置来恢复。

- Trash回收机制应用场景
  - 放置用户手一抖彻底删除数据，当放置到Trash回收站里，还可以再次恢复数据。
- Trash回收站原理
  - 当用户默认删除数据的时候，并不是直接从物理磁盘删掉，而只是将文件移动到指定的文件夹下，如果一致不恢复数据（根据默认时间7天等相关参数），Trash数据将从磁盘中抹掉。

```xml
<property>  
    <name>fs.trash.interval</name>  
    <value>10080</value>  
    <description>Number of minutes after which the checkpoint gets deleted. If zero, the trash feature is disabled.</description>  
</property>  

<property>  
    <name>fs.trash.checkpoint.interval</name>  
    <value>0</value>  
    <description>Number of minutes between trash checkpoints. Should be smaller or equal to fs.trash.interval. If zero, the value is set to the value of fs.trash.interval.</description>  
</property>
```

- 如果使用java API 来删除数据的话，直接将文件从磁盘抹掉，不会移动到Trash回收站中，shell才会。
- 如果不想放到回收站

```sh
#直接删除数据，不放在回收站中
hdfs dfs -rm -r -skipTrash /wp1/1.txt
```

- 恢复数据

```sh
#将Trash回收站中的指定的数据恢复到指定文件夹中
hdfs dfs -mv hdfs://node1:8020/user/root/.Trash/200820154000/wp1/1.txt /wp1/
```

- 清空回收站

```sh
hdfs dfs -expunge
```

> 总结，Trash回收机制为了保证操作数据，删除数据的时候，防止误删除，所以建议生产环境，在删除数据的时候，不要 skipTrash 跳过Trash回收机制。


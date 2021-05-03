> 当 Hadoop 的集群当中, NameNode的所有元数据信息都保存在了 FsImage 与 Eidts 文件当中, 这两个文件就记录了所有的数据的元数据信息, 元数据信息的保存目录配置在了 hdfs-site.xml 当中

```xml
<property>
   <name>dfs.namenode.name.dir</name>    
   <value>
       file:///export/serverss/hadoop2.7.5/hadoopDatas/namenodeDatas</value>
</property>
<property>
    <name>dfs.namenode.edits.dir</name>
    <value>file:///export/serverss/hadoop-2.7.5/hadoopDatas/nn/edits</value>
</property>>
```

### **FsImage和Edits**介绍

**edits**:

> edits 是在NameNode启动时对整个文件系统的快照存放了客户端最近一段时间的操作日志
>
> 客户端对 HDFS 进行写文件时会首先被记录在 edits 文件中
>
> edits 修改时元数据也会更新

**fsimage**:

> fsimage是在NameNode启动时对整个文件系统的快照
>
> NameNode 中关于元数据的镜像, 一般称为检查点, fsimage 存放了一份比较完整的元数据信息
>
> 因为 fsimage 是 NameNode 的完整的镜像, 如果每次都加载到内存生成树状拓扑结构，这是非常耗内存和CPU, 所以一般开始时对 NameNode 的操作都放在 edits 中
>
> fsimage 内容包含了 NameNode 管理下的所有 DataNode 文件及文件 block 及 block 所在的 DataNode 的元数据信息.
>
> 随着edits 内容增大, 就需要在一定时间点和 fsimage 合并

###  **SecondaryNameNode的作用**

> SecondaryNameNode的作用是合并fsimage和edits文件。
>
> NameNode的存储目录树的信息，而目录树的信息则存放在fsimage文件中，当NameNode启动的时候会首先读取整个fsimage文件，将信息装载到内存中。
>
> Edits文件存储日志信息，在NameNode上所有对目录的操作，增加，删除，修改等都会保存到edits文件中，并不会同步到fsimage中，当NameNode关闭的时候，也不会将fsimage和edits进行合并。
>
> 所以当NameNode启动的时候，首先装载fsimage文件，然后按照edits中的记录执行一遍所有记录的操作，最后把信息的目录树写入fsimage中，并删掉edits文件，重新启用新的edits文件。

### **SecondaryNameNode唤醒合并的规则**

> SecondaryNameNode 会按照一定的规则被唤醒，进行fsimage和edits的合并，防止文件过大。
>
> 合并的过程是，将NameNode的fsimage和edits下载到SecondryNameNode 所在的节点的数据目录，然后合并到fsimage文件，最后上传到NameNode节点。合并的过程中不影响NameNode节点的操作
>
> SecondaryNameNode被唤醒的条件可以在core-site.xml中配置：
>
> fs.checkpoint.period：单位秒，默认值3600，检查点的间隔时间，当距离上次检查点执行超过该时间后启动检查点
>
> fs.checkpoint.size：单位字节，默认值67108864，当edits文件超过该大小后，启动检查点

[core-site.xml]

```xml

<!-- 多久记录一次 HDFS 镜像, 默认 1小时 -->
<property>
 <name>fs.checkpoint.period</name>
 <value>3600</value>
</property>
<!-- 一次记录多大, 默认 64M -->
<property>
 <name>fs.checkpoint.size</name>
 <value>67108864</value>
</property>
```

**SecondaryNameNode一般处于休眠状态，当两个检查点满足一个，即唤醒SecondaryNameNode执行合并过程。**

###  **SecondaryNameNode工作过程**

![image-20200819171348215](./assets\image-20200819171348215.png)

#### snn 元数据辅助执行流程

**第一步：**将hdfs更新记录写入一个新的文件——edits.new。

**第二步：**将fsimage和editlog通过http协议发送至secondary namenode。

**第三步：**将fsimage与editlog合并，生成一个新的文件——fsimage.ckpt。这步之所以要在secondary namenode中进行，是因为比较耗时，如果在namenode中进行，或导致整个系统卡顿。

**第四步**将生成的fsimage.ckpt通过http协议发送至namenode。

**第五步**重命名fsimage.ckpt为fsimage，edits.new为edits。

**第六步**等待下一次checkpoint触发SecondaryNameNode进行工作，一直这样循环操作。

#### fsimage的信息查看

```shell
cd /export/server/hadoop2.7.5/hadoopDatas/namenodeDatas
hdfs oiv -i fsimage_0000000000000000864 -p XML -o fsimage.xml
```

#### edits的文件查看

```shell
cd /export/server/hadoop2.7.5/hadoopDatas/namenodeDatas
hdfs oev -i  edits_0000000000000000865-0000000000000000866 -p XML -o myedit.xml
```
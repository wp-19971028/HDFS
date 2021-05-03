### HDFS的高可用机制配置

参考HDFS的高可用搭建   三个 core-site.xml hdfs-site.xml yarn-site.xml

### HDFS的联邦机制

- HDFS的联邦机制，只是解决单台namenode 内存存储数据有限，内存不够用的这种场景，提供一种namenode的水平扩展的方案，每个namenode但是不能支持HA，存在单点故障的可能。
- 核心思想
  - 对datanode 实施命名空间操作，将datanode所有数据划分为多个nameservices（命名空间），每个命名空间交给一个namenode来管理。所有的namenode 共同管理datanode。
- 缺点：
  - 在写入数据的时候，从哪个管理的namenode写就从哪个namenode来读，否则会获取不到数据；不能保证每个namenode的HA高可用。
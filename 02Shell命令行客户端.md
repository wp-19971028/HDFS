## Shell命名操作客户端

> HDFS是存取数据的分布式文件系统，那么对HDFS的操作，就是文件系统的基本操作，比如文件的创建、修改、删除、修改权限等，文件夹的创建、删除、重命名等。对HDFS的操作命令类似于Linux的shell对文件的操作，如ls、mkdir、rm等。



- Hadoop客户端使用命令格式

  ```sh
  hadoop fs <args>
  # 或者
  hdfs dfs <args>
  ```

- 所有FS shell命令都将路径URI作为参数。URI格式为scheme://authority/path。对于HDFS，该scheme是hdfs，对于本地FS，该scheme是file。scheme和authority是可选的。如果未指定，则使用配置中指定的默认方案。

| **选项名称**   | **使用格式**                                                 | **含义**                   |
| -------------- | ------------------------------------------------------------ | -------------------------- |
| -ls            | -ls <路径>                                                   | 查看指定路径的当前目录结构 |
| -lsr           | -lsr <路径>                                                  | 递归查看指定路径的目录结构 |
| -du            | -du <路径>                                                   | 统计目录下个文件大小       |
| -dus           | -dus <路径>                                                  | 汇总统计目录下文件(夹)大小 |
| -count         | -count [-q] <路径>                                           | 统计文件(夹)数量           |
| -mv            | -mv <源路径> <目的路径>                                      | 移动                       |
| -cp            | -cp <源路径> <目的路径>                                      | 复制                       |
| -rm            | -rm [-skipTrash] <路径>                                      | 删除文件/空白文件夹        |
| -rmr           | -rmr [-skipTrash] <路径>                                     | 递归删除                   |
| -put           | -put <多个linux上的文件> <hdfs路径>                          | 上传文件                   |
| -copyFromLocal | -copyFromLocal <多个linux上的文件> <hdfs路径>                | 从本地复制                 |
| -moveFromLocal | -moveFromLocal <多个linux上的文件> <hdfs路径>                | 从本地移动                 |
| -getmerge      | -getmerge <源路径> <linux路径>                               | 合并到本地                 |
| -cat           | -cat <hdfs路径>                                              | 查看文件内容               |
| -text          | -text <hdfs路径>                                             | 查看文件内容               |
| -copyToLocal   | -copyToLocal [-ignoreCrc] [-crc] [hdfs源路径] [linux目的路径] | 从本地复制                 |
| -moveToLocal   | -moveToLocal [-crc] <hdfs源路径> <linux目的路径>             | 从本地移动                 |
| -mkdir         | -mkdir <hdfs路径>                                            | 创建空白文件夹             |
| -setrep        | -setrep [-R] [-w] <副本数> <路径>                            | 修改副本数量               |
| -touchz        | -touchz <文件路径>                                           | 创建空白文件               |
| -stat          | -stat [format] <路径>                                        | 显示文件统计信息           |
| -tail          | -tail [-f] <文件>                                            | 查看文件尾部信息           |
| -chmod         | -chmod [-R] <权限模式> [路径]                                | 修改权限                   |
| -chown         | -chown [-R] [属主][:[属组]] 路径                             | 修改属主                   |
| -chgrp         | -chgrp [-R] 属组名称 路径                                    | 修改属组                   |
| -help          | -help [命令选项]                                             | 帮助                       |

## **常用的Shell命令**

### 查看列表

```sh
# 格式: hadoop fs -ls url
# 作用类似于Linux的ls命令
hadoop fs -ls /

[root@node1 data]# hadoop fs -ls /
Found 3 items
-rw-r--r--   3 root supergroup         12 2021-05-01 21:51 /b.txt
drwxrwx---   - root supergroup          0 2021-05-01 21:52 /tmp
drwxr-xr-x   - root supergroup          0 2021-05-01 21:52 /user
```

```sh
# 格式: hdfs dfs -lsr url
# 作用: 类似于linux的 ls -r
hdfs dfs -lsr /
或者
hadoop fs -lsr /
或者
hdfs dfs -ls -R /

[root@node1 data]# hdfs dfs -lsr /
lsr: DEPRECATED: Please use 'ls -R' instead.
-rw-r--r--   3 root supergroup         12 2021-05-01 21:51 /b.txt
drwxrwx---   - root supergroup          0 2021-05-01 21:52 /tmp
drwxrwx---   - root supergroup          0 2021-05-01 21:49 /tmp/hadoop-yarn
drwxrwx---   - root supergroup          0 2021-05-01 21:52 /tmp/hadoop-yarn/staging


lsr: DEPRECATED: Please use 'ls -R' instead.
-rw-r--r--   3 root supergroup         12 2021-05-01 21:51 /b.txt
drwxrwx---   - root supergroup          0 2021-05-01 21:52 /tmp
drwxrwx---   - root supergroup          0 2021-05-01 21:49 /tmp/hadoop-yarn
drwxrwx---   - root supergroup          0 2021-05-01 21:52 /tmp/hadoop-yarn/staging
drwxrwx---   - root supergroup          0 2021-05-01 21:49 /tmp/hadoop-yarn/staging/history
drwxrwx---   - root supergroup          0 2021-05-01 21:53 /tmp/hadoop-yarn/staging/history/done
drwxrwx---   - root supergroup          0 2021-05-01 21:53 /tmp/hadoop-yarn/staging/history/done/2021
drwxrwx---   - root supergroup          0 2021-05-01 21:53 /tmp/hadoop-yarn/staging/history/done/2021/05


[root@node1 data]# hdfs dfs -ls -R /
-rw-r--r--   3 root supergroup         12 2021-05-01 21:51 /b.txt
drwxrwx---   - root supergroup          0 2021-05-01 21:52 /tmp
drwxrwx---   - root supergroup          0 2021-05-01 21:49 /tmp/hadoop-yarn
drwxrwx---   - root supergroup          0 2021-05-01 21:52 /tmp/hadoop-yarn/staging
drwxrwx---   - root supergroup          0 2021-05-01 21:49 /tmp/hadoop-yarn/staging/history
drwxrwx---   - root supergroup          0 2021-05-01 21:53 /tmp/hadoop-yarn/staging/history/done
drwxrwx---   - root supergroup          0 2021-05-01 21:53 /tmp/hadoop-yarn/staging/history/done/2021
drwxrwx---   - root supergroup          0 2021-05-01 21:53 /tmp/hadoop-yarn/staging/history/done/2021/05
drwxrwx---   - root supergroup          0 2021-05-01 21:53 /tmp/hadoop-yarn/staging/history/done/2021/05/01
drwxrwx---   - root supergroup   
```

### 创建目录

```
格式 ： hdfs  dfs -mkdir [-p] <paths>
作用 :   以<paths>中的URI作为参数，创建目录。使用-p参数可以递归创建目录

hadoop fs -mkdir /wp1
hadoop fs -mkdir /wp2
hadoop fs -mkdir -p /aaa/bbb/ccc
```

![1619941443450](./assets\1619941443450.png)

### 从本地拷贝到hdfs

```sh
格式   ： hadoop fs -put <localsrc >  ... <dst>
作用 ：  将单个的源文件src或者多个源文件srcs从本地文件系统拷贝到目标文件系统中（<dst>对应的路径）。也可以从标准输入中读取输入，写入目标文件系统中
[root@node1 data]# echo “Hello HDFS” >> 1.txt
您在 /var/spool/mail/root 中有新邮件
[root@node1 data]# ll
总用量 8
-rw-r--r-- 1 root root 17 5月   2 15:47 1.txt
-rw-r--r-- 1 root root 12 5月   1 21:07 b.txt
[root@node1 data]# hdfs dfs -put 1.txt /wp1
```

![1619941728522](./assets\1619941728522.png)

### 从本地移动到hdfs

```
格式： hdfs  dfs -moveFromLocal  <localsrc>   <dst>
作用:   和put命令类似，但是源文件localsrc拷贝之后自身被删除
[root@node1 data]# echo “Hello HDFS” >> 2.txt
您在 /var/spool/mail/root 中有新邮件
[root@node1 data]# ll
总用量 12
-rw-r--r-- 1 root root 17 5月   2 15:47 1.txt
-rw-r--r-- 1 root root 17 5月   2 15:50 2.txt
-rw-r--r-- 1 root root 12 5月   1 21:07 b.txt
[root@node1 data]# hdfs dfs -moveFromLocal 2.txt  /wp1
您在 /var/spool/mail/root 中有新邮件
[root@node1 data]# ls
1.txt  b.txt
```

![1619942136607](./assets\1619942136607.png)

### 从服务器拷贝到本机

```hdfs
格式:  hadoop fs  -get [-ignorecrc ]  [-crc]  <src> <localdst>
作用:  将文件拷贝到本地文件系统。 CRC 校验失败的文件通过-ignorecrc选项拷贝。 文件和CRC校验和可以通过-CRC选项拷贝
[root@node1 data]# hadoop fs -get /wp1/2.txt /export/data
您在 /var/spool/mail/root 中有新邮件
[root@node1 data]# ls
1.txt  2.txt  b.txt
```

### 合并下载多个文件

```
格式: hadoop fs -getmerge -nl  < hdfs dir >  < local file >
功能：合并下载多个文件
参数: 加上nl后，合并到local file中的hdfs文件之间会空出一行
示例：比如hdfs的目录 /aaa/下有多个文件:log.1, log.2,log.3,...

[root@node1 data]# hdfs dfs -getmerge /wp1/* ./3.txt
您在 /var/spool/mail/root 中有新邮件
[root@node1 data]# ll
总用量 16
-rw-r--r-- 1 root root 17 5月   2 15:47 1.txt
-rw-r--r-- 1 root root 17 5月   2 16:08 2.txt
-rw-r--r-- 1 root root 34 5月   2 16:11 3.txt
-rw-r--r-- 1 root root 12 5月   1 21:07 b.txt
[root@node1 data]# cat 3.txt 
“Hello HDFS”
“Hello HDFS”

```

### 将hdfs上的文件从原路径移动到目标路径

```sh
格式  ： hdfs  dfs -mv URI   <dest>
作用： 将hdfs上的文件从原路径移动到目标路径（移动之后文件删除），该命令不能夸文件系统
hdfs dfs -mv /wp1/1.txt /wp2
[root@node1 data]# hdfs dfs -mv /wp1/1.txt /wp2
您在 /var/spool/mail/root 中有新邮件
[root@node1 data]# hdfs dfs -ls /wp2
Found 1 items
-rw-r--r--   3 root supergroup         17 2021-05-02 15:47 /wp2/1.txt
[root@node1 data]# hdfs dfs -ls /wp1
Found 1 items
-rw-r--r--   3 root supergroup         17 2021-05-02 15:54 /wp1/2.txt
[root@node1 data]# 

```

### 删除参数指定的文件和目录

```sh
格式： hadoop fs -rm [-r] 【-skipTrash】 URI 【URI 。。。】
作用：   删除参数指定的文件和目录，参数可以有多个，删除目录需要加-r参数
如果指定-skipTrash选项，那么在回收站可用的情况下，该选项将跳过回收站而直接删除文件；
否则，在回收站可用时，在HDFS Shell 中执行此命令，会将文件暂时放到回收站中。
hdfs dfs -rm /wp1/2.txt

hdfs dfs -rm -r /wp2

# 如果想直接删除用
hdfs dfs -rm -r -skipTrash /wp2
```

![1619943437653](./assets\1619943437653.png)



![1619943500478](./assets\1619943500478.png)

### 拷贝

```sh
格式: hdfs  dfs  -cp URI [URI ...] <dest>
作用：将文件拷贝到目标路径中。如果<dest>  为目录的话，可以将多个文件拷贝到该目录下。
-f 选项将覆盖目标，如果它已经存在。
-p 选项将保留文件属性（时间戳、所有权、许可、ACL、XAttr）。

[root@node1 data]# hdfs dfs -cp /wp1/1.txt /wp2/1.txt
[root@node1 data]# hdfs dfs -ls /wp2
Found 1 items
-rw-r--r--   3 root supergroup         17 2021-05-02 16:45 /wp2/1.txt
```

### 文件内容输出到控制台

~~~sh
hadoop fs  -cat  URI [uri  ...]
作用：将参数所指示的文件内容输出到控制台
[root@node1 data]# hdfs dfs -cat /wp1/1.txt
“Hello HDFS”
~~~

### 显示目录中所有文件大小

```sh
功能：显示目录中所有文件大小，当只指定一个文件时，显示此文件的大小。

[root@node1 data]# hdfs dfs -du /wp1
17  /wp1/1.txt
17  /wp1/2.txt
34  /wp1/3.txt
12  /wp1/b.txt

```

### 改变文件权限

![1619945510183](./assets\1619945510183.png)

```
格式:  hadoop fs  -chmod  [-R]  URI[URI  ...]
作用： 改变文件权限。如果使用  -R 选项，则对整个目录有效递归执行。使用这一命令的用户必须是文件的所属用户，或者超级用户。
例如:可以创建一个用户hadoop，将/a.txt的所属用户和所属用户组修改为hadoop
hadoop fs -chmod -R 777 /dir1

[root@node1 data]# hdfs dfs -chmod -R 777 /wp1

格式: hdfs   dfs  -chmod  [-R]  URI[URI  ...]
作用：改变文件的所属用户和用户组。如果使用  -R 选项，则对整个目录有效递归执行。使用这一命令的用户必须是文件的所属用户，或者超级用户。
hadoop fs  -chown  -R hadoop:hadoop  /a.txt
```

![1619945707697](./assets\1619945707697.png)

### 追加一个或者多个文件到hdfs指定文件中

```
格式: hadoop fs -appendToFile <localsrc> ... <dst>
作用: 追加一个或者多个文件到hdfs指定文件中.也可以从命令行读取输入.
cd /export/server/hadoop2.7.5/etc/hadoop/
hadoop fs -appendToFile  *.xml  /big.xml

[root@node1 hadoop]# hdfs dfs -appendToFile *xml /conf.xml
[root@node1 hadoop]# hdfs dfs -cat /conf.xml

....
-->
<configuration>
<!-- 配置yarn主节点的位置 -->
        <property>
                <name>yarn.resourcemanager.hostname</name>
                <value>node1</value>
        </property>

        <property>
                <name>yarn.nodemanager.aux-services</name>
                <value>mapreduce_shuffle</value>
        </property>

        <!-- 开启日志聚合功能 -->
        <property>
                <name>yarn.log-aggregation-enable</name>
                <value>true</value>
                ....

```


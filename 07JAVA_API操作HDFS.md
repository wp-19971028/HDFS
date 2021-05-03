## 配置windows 的 hadoop 的环境变量

1. 设置HADOOP_HOME 和 PATH
2. 将 hadoop.dll 保存到 C:/WINDOWS/system32
3. 关闭重启。

## 导入maven依赖

```xml
<dependencies>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-common</artifactId>
        <version>2.7.5</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-client</artifactId>
        <version>2.7.5</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-hdfs</artifactId>
        <version>2.7.5</version>
    </dependency>
    <dependency>
        <groupId>org.apache.hadoop</groupId>
        <artifactId>hadoop-mapreduce-client-core</artifactId>
        <version>2.7.5</version>
    </dependency>
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13</version>
    </dependency>
</dependencies>
```

## 如何获取本地的客户端

```Java
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import java.io.IOException;

public class HDFSTest {
    // 如何获取HDFS的客户端
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);
        System.out.println(fileSystem);

    }
}

结果:
org.apache.hadoop.fs.LocalFileSystem@c5ee75e
```

## 获取远程的HDFS客户端

~~~java
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

public class HDFSTest2 {
    // 如何获取远程HDFS的客户端
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        Configuration configuration = new Configuration();
        configuration.set("fs.defaultFS","hdfs://node1:8020");
        FileSystem fileSystem = FileSystem.get(configuration);
        System.out.println(fileSystem);


    }
}

结果:
DFS[DFSClient[clientName=DFSClient_NONMAPREDUCE_-754845310_1, ugi=wp (auth:SIMPLE)]]
~~~

## 通过uri和伪装用户来创建

```java
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import java.net.URI;

public class HDFSTest3 {
    //通过uri和伪装用户来创建 FileSystem
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        URI uri = new URI("hdfs://node1:8020");
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(uri,configuration,"root");
        System.out.println(fileSystem);


    }
}

结果：
DFS[DFSClient[clientName=DFSClient_NONMAPREDUCE_-1149967048_1, ugi=root (auth:SIMPLE)]]

```

## 获取HDFS某个目录下的所有文件

```java
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

import java.net.URI;

public class HDFSTest4 {
    //通过uri和伪装用户来创建 FileSystem
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        URI uri = new URI("hdfs://node1:8020");
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(uri,configuration,"root");
        System.out.println(fileSystem);
        // 执行查询路劲下的所有文件
        RemoteIterator<LocatedFileStatus> listfiles = fileSystem.listFiles(new Path("/wp1"), true);
        while (listfiles.hasNext()){
            LocatedFileStatus fileStatus = listfiles.next();
            Path path = fileStatus.getPath();
            String filename = path.getName();
            System.out.println(filename);
        }
        // 关闭释放资源
        fileSystem.close(); 
    }
}

DFS[DFSClient[clientName=DFSClient_NONMAPREDUCE_-1601308980_1, ugi=root (auth:SIMPLE)]]
1.txt
2.txt
3.txt
b.txt
```

在HDFS上创建文件夹

```java
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.junit.Test;

import java.net.URI;

public class HDFSTest5 {
    //通过uri和伪装用户来创建 FileSystem
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        URI uri = new URI("hdfs://node1:8020");
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.newInstance(uri,configuration,"root");
        System.out.println(fileSystem);
        // 创建文件夹
        fileSystem.mkdirs(new Path("/wp3"));
        // 关闭释放资源
        fileSystem.close();

    }
}
```

![1620023207754](./assets\1620023207754.png)

## 创建一个文件

```
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.net.URI;

public class HDFSTest6 {
    //通过uri和伪装用户来创建 FileSystem
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        URI uri = new URI("hdfs://node1:8020");
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.newInstance(uri,configuration,"root");
        System.out.println(fileSystem);
        // 创建文件
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/wp3/1.txt"));
        fsDataOutputStream.write("username = zhansan\r\npassward=123456".getBytes());
        // 关闭释放资源
        fileSystem.close();

    }
}
```

![1620023676823](./assets\1620023676823.png)

```
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.net.URI;

public class HDFSTest7 {
    //通过uri和伪装用户来创建 FileSystem
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        URI uri = new URI("hdfs://node1:8020");
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.newInstance(uri,configuration,"root");
        System.out.println(fileSystem);
        // 下载文件
        fileSystem.copyToLocalFile(new Path("/wp1/1.txt"),
                new Path("F:/1.txt"));
        // 关闭释放资源
        fileSystem.close();

    }
}
```

![1620024227325](./assets\1620024227325.png)

## 上传

```
package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.junit.Test;

import java.net.URI;

public class HDFSTest8 {
    //通过uri和伪装用户来创建 FileSystem
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        URI uri = new URI("hdfs://node1:8020");
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.newInstance(uri,configuration,"root");
        System.out.println(fileSystem);
        // 上传文件
       fileSystem.copyFromLocalFile(new Path("F:/hosts.txt"),
               new Path("/wp1/hosts.txt"));
        // 关闭释放资源
        fileSystem.close();

    }
}
```

![1620024567947](./assets\1620024567947.png)

## **小文件合并**

> 由于 Hadoop 擅长存储大文件，因为大文件的元数据信息比较少，如果 Hadoop 集群当中有大量的小文件，那么每个小文件都需要维护一份元数据信息，会大大的增加集群管理元数据的内存压力，所以在实际工作当中，如果有必要一定要将小文件合并成大文件进行一起处理,可以在上传的时候将小文件合并到一个大文件里面去

![image-20200820104847247](./assets\image-20200820104847247.png)

```
package demo;

import org.apache.commons.io.IOUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import org.junit.Test;

import java.net.URI;

public class HDFSTest9 {
    //通过uri和伪装用户来创建 FileSystem
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        URI uri = new URI("hdfs://node1:8020");
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.newInstance(uri,configuration,"root");
        System.out.println(fileSystem);
        // 合并上传文件
            // 创建一个文件形成输出流
        FSDataOutputStream fsDataOutputStream = fileSystem.create(new Path("/wp2/2.txt"));
            //获取本地文件系统
        LocalFileSystem localFileSystem = FileSystem.getLocal(new Configuration());
            // 获取本地文件
        FileStatus[] fileStatuses = localFileSystem.listStatus(new Path("F:/demo/"));
        // 关闭释放资源
        for (FileStatus fileStatus: fileStatuses){

            FSDataInputStream inputStream = localFileSystem.open(fileStatus.getPath());
            IOUtils.copy(inputStream,fsDataOutputStream);
            IOUtils.closeQuietly(inputStream);
        }

        fsDataOutputStream.close();
        fileSystem.close();

    }
}
```

## 访问权限设置

> HDFS权限模型和Linux系统类似。每个文件和目录有一个所有者（owner）和一个组（group）。文件或目录对其所有者、同组的其他用户以及所有其他用户（other）分别有着不同的权限。对文件而言，当读取这个文件时需要有r权限，当写入或者追加到文件时需要有w权限。对目录而言，当列出目录内容时需要具有r权限，当新建或删除子文件或子目录时需要有w权限，当访问目录的子节点时需要有x权限。但hdfs的文件权限需要开启之后才生效，否则在HDFS中设置权限将不具有任何意义!
>
> HDFS的权限设置是通过hdfs-site.xml文件来设置，在搭建Hadoop集群时，将HDFS的权限关闭了，所以对HDFS的任何操作都不会受到影响的。
>
> 接下来我们将HDFS的权限开启，测试下HDFS的权限控制。

1. 停止hdfs集群，在node1机器上执行以下命令

```sh
stop-dfs.sh
```

2. 修改node1机器上的hdfs-site.xml当中的配置文件

```sh
vim hdfs-site.xml
```

```xml
<property>
	<name>dfs.permissions.enabled</name>
	<value>true</value>
</property>
```

3. 修改完成之后配置文件发送到其他机器上面去

```sh
scp hdfs-site.xml node2:$PWD
scp hdfs-site.xml node3:$PWD
```

4. 重启hdfs集群

```sh
start-dfs.sh
```

5. 随意上传一些文件到我们hadoop集群当中准备测试使用

```xml
cd /export/servers/hadoop-2.7.5/etc/hadoop
hadoop fs -mkdir /config
hadoop fs -put *.xml /config
hadoop fs -chmod 600 /config/core-site.xml
```

6. 经过以上操作之后,core-site.xml文件的权限如下:

![1620029571343](./assets\1620029571343.png)

这个权限是当前所属用户root具有对core-site.xml文件的可读，可写权限。

```java
@Test
public void getConfig()throws  Exception{
    FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration(),"root");
    fileSystem.copyToLocalFile(new Path("/config/core-site.xml"),new Path("file:///c:/core-site.xml"));
    fileSystem.close();
}
```

当HDFS的权限开启之后，运行以上代码发现权限拒绝，不允许访问。

![1620029657511](./assets\1620029657511.png)

这是因为我们在Windows下运行HDFS的客户端，用户名一般不是root，是其他用户，所以对core-site.xml文件没有任何操作权限。

解决方法:

  方式1-修改core-site.xml的文件权限

~~~
hadoop fs -chmod 777/config/core-site.xml
~~~

方式2-伪造用户

在这里，我们可以以root用户的身份去访问文件

~~~java
@Test
public void getConfig()throws  Exception{
    FileSystem fileSystem = FileSystem.get(new URI("hdfs://node01:8020"), new Configuration(),"root");
    fileSystem.copyToLocalFile(new Path("/config/core-site.xml"),new Path("file:///c:/core-site.xml"));
    fileSystem.close();
}
~~~

![1620029718064](./assets\1620029718064.png)
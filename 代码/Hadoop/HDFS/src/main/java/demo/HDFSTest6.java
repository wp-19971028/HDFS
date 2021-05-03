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

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

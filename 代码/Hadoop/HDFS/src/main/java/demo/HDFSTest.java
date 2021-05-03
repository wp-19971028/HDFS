package demo;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.junit.Test;

import java.io.IOException;

public class HDFSTest {
    // 如何获取本地HDFS的客户端
    @Test
    public void test01() throws Exception {

        // 获取FileSystem
        Configuration configuration = new Configuration();
        FileSystem fileSystem = FileSystem.get(configuration);
        System.out.println(fileSystem);

    }
}

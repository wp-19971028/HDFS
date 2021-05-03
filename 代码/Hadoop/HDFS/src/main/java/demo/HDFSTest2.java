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

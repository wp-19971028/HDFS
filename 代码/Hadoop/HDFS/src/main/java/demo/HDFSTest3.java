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

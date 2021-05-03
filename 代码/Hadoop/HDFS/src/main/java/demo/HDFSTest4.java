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

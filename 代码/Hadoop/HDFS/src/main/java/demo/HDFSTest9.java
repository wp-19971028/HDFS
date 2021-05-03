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

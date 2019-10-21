package com.wuhulala.flink.dataset.source.hdfs;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

/**
 * @author wuhulala<br>
 * @date 2019/10/20<br>
 * @since v1.0<br>
 */
public class DataSetHdfsSourceExample {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> text = env.readTextFile("hdfs://flinkhadoop:9000/wordcount/input/README.txt");
        text.print();
    }
}

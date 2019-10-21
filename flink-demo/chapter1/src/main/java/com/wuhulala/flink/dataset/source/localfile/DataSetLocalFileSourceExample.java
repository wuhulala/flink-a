package com.wuhulala.flink.dataset.source.localfile;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;

/**
 * @author wuhulala<br>
 * @date 2019/10/20<br>
 * @since v1.0<br>
 */
public class DataSetLocalFileSourceExample {

    public static void main(String[] args) throws Exception {
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        DataSource<String> text = env.readTextFile("D:\\01code\\personal\\flink\\flink-demo\\chapter1\\src\\main\\resources\\test\\hello.txt");
        text.print();
    }
}

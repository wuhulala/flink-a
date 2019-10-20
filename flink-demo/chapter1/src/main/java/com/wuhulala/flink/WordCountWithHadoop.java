package com.wuhulala.flink;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.operators.DataSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.util.Collector;

public class WordCountWithHadoop {

    public static void main(String[] args) throws Exception {

        Configuration config = new Configuration();
        config.setString("jobmanager.rpc.address", "jobmanager");
        config.setString("jobmanager.rpc.port", "6123");

        // get the execution environment
        final ExecutionEnvironment env = ExecutionEnvironment.getExecutionEnvironment();
        // get input data by connecting to the socket
        DataSource<String> text = env.readTextFile("hdfs://9001");

        env.setParallelism(3);
        // parse the data, group it, window it, and aggregate the counts
        text
                .flatMap(new FlatMapFunction<String, WordWithCount>() {
                    @Override
                    public void flatMap(String value, Collector<WordWithCount> out) {
                        for (String word : value.split(" ")) {
                            System.out.println(word);
                            out.collect(new WordWithCount(word, 1L));
                        }
                    }
                })
                .groupBy("word")
                .reduce(new ReduceFunction<WordWithCount>() {
                    @Override
                    public WordWithCount reduce(WordWithCount a, WordWithCount b) {
                        return new WordWithCount(a.word, a.count + b.count);
                    }
                }).collect()
                .forEach(wordWithCount -> {
                    System.out.println("show:::" + wordWithCount);
                });
        //env.execute("file Window WordCount");
    }

    // Data type for words with count
    public static class WordWithCount {

        public String word;
        public long count;

        public WordWithCount() {
        }

        public WordWithCount(String word, long count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public String toString() {
            return word + " : " + count;
        }
    }
}
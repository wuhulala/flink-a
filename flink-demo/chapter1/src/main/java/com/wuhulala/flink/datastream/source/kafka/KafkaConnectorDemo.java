package com.wuhulala.flink.datastream.source.kafka;

import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import java.util.Properties;

/**
 * @author wuhulala<br>
 * @date 2019/10/20<br>
 * @since v1.0<br>
 */
public class KafkaConnectorDemo {

    public static final String BOOTSTRAP_SERVERS = "192.168.121.101:9092";

    public static void main(String[] args) throws Exception {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        // 1.0 配置KafkaConsumer
        Properties props = new Properties();
        props.setProperty("bootstrap.servers", BOOTSTRAP_SERVERS);
        props.setProperty("group.id", "test111");
        props.put("enable.auto.commit", "true");
        props.put("auto.commit.interval.ms", "1000");
        props.put("session.timeout.ms", "30000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        // 1.1 把kafka设置为source
        env.enableCheckpointing(5000); // checkpoint every 5000 msecs
        DataStream<String> stream = env
                .addSource(new FlinkKafkaConsumer<>("demo", new SimpleStringSchema(), props));

        // 2.0 配置 kafkaProducer
        FlinkKafkaProducer<String> myProducer = new FlinkKafkaProducer<String>(
                BOOTSTRAP_SERVERS,            // broker list
                "demo_flink",                  // target topic
                new SimpleStringSchema());   // serialization schema
        myProducer.setWriteTimestampToKafka(true);

        // 2.1 把kafka设置为sink
        stream.addSink(myProducer);

        // 2.2 把kafka接收debug打印出来
        stream.print();

        env.execute("Kafka Source");
    }
}

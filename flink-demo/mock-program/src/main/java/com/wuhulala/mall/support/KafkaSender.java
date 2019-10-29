package com.wuhulala.mall.support;

import com.alibaba.fastjson.JSON;
import com.wuhulala.mall.dto.Event;
import org.apache.kafka.clients.producer.*;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import javax.annotation.PreDestroy;
import java.util.Properties;

/**
 * @author wuhulala<br>
 * @date 2019/10/29<br>
 * @since v1.0<br>
 */
@Component
public class KafkaSender implements InitializingBean {

    @Value("${kafka.brokers:}")
    private String kafkaBrokers;


    private Producer<String, String> producer;

    @Override
    public void afterPropertiesSet() throws Exception {
        Properties props = new Properties();
        // kafka服务器地址
        props.put("bootstrap.servers", kafkaBrokers);
        // 需要收到多少个服务器的确认信号，all会保证集群leader和所有备份都返回确认信号
        props.put("acks", "all");
        // 失败重试次数
        props.put("retries", 0);
        // 批处理字节大小
        props.put("batch.size", 16384);
        // 发送延迟 ms
        props.put("linger.ms", 1);
        //缓存数据的内存大小
        props.put("buffer.memory", 33554432);
        //key的序列化策略
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //value的序列化策略
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        //写入分区的策略
        //props.put("partitioner.class", "com.wuhulala.kafka.partitioner.MoldPartitioner");
        Producer<String, String> producer = new KafkaProducer<>(props);
    }

    public void sendEvent(Event event, String topic) {
        producer.send(new ProducerRecord<>(topic, event.getEventId(), JSON.toJSONString(event)));
    }

    @PreDestroy
    public void destroy(){
        producer.close();
    }

}

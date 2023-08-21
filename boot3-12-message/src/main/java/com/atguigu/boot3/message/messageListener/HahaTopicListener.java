package com.atguigu.boot3.message.messageListener;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

@Component
public class HahaTopicListener {


    //默认的监听是从消息队列最后一个消息开始拿 新消息才能拿到
    @KafkaListener(topics = {"newshh"}, groupId = "hh")
    public void listen(ConsumerRecord record) {
        //String 类型只获取消息
        //ConsumerRecord 获取消息的所有信息
        Object key = record.key();
        Object value = record.value();
        System.out.println("keys:" + key);
        System.out.println("value:" + value);
    }

    //拿到以前完整的消息 以后可以直接去看官方文档
    @KafkaListener(groupId = "hehe", topicPartitions = {@TopicPartition(topic = "newshh", partitionOffsets = {
            @PartitionOffset(partition = "0", initialOffset = "0"),
    })})
    public void listenBeforeAll(ConsumerRecord record) {
        //String 类型只获取消息
        //ConsumerRecord 获取消息的所有信息
        Object key = record.key();
        Object value = record.value();
        System.out.println("before-keys:" + key);
        System.out.println("before-value:" + value);
    }
}

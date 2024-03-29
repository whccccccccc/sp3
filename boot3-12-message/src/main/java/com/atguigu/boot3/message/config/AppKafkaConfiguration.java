package com.atguigu.boot3.message.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class AppKafkaConfiguration {

    @Bean
    public NewTopic topic1() {
        return TopicBuilder.name("thing").partitions(1).compact().build();
    }
}

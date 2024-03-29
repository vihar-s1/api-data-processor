package com.versatileDataProcessor.dataProducer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApiMessageTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Value(value = "${spring.kafka.topic.name}")
    private String kafkaTopicName;

    @Value(value = "${spring.kafka.topic.numOfPartitions}")
    private int numOfPartitions;

    @Value(value = "${spring.kafka.topic.replicationFactor}")
    private short replicationFactor;

    @Bean
    public KafkaAdmin getKafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();

        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);

        return new KafkaAdmin(configs);
    }

    @Bean
    public NewTopic getNewTopic() {
        return new NewTopic(kafkaTopicName, numOfPartitions, replicationFactor);
    }
}

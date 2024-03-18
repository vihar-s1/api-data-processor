package com.VersatileDataProcessor.DataConsumer.config;

import com.VersatileDataProcessor.DataConsumer.models.ApiMessages.ApiMessageInterface;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class ApiMessageConsumerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;
    @Value(value = "${spring.kafka.consumer.group-id}")
    private int groupId;

    @Bean
    public ConsumerFactory<String, ApiMessageInterface> getConsumerFactory() {
        Map<String, Object> configs = new HashMap<>();

        configs.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configs.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configs.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configs.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);

        // Important: Configure JsonDeserializer to trust all packages
        configs.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        configs.put(JsonDeserializer.TYPE_MAPPINGS,
                "ApiMessageInterface:com.VersatileDataProcessor.DataConsumer.models.ApiMessages.ApiMessageInterface," +
                        "MockApiMessage:com.VersatileDataProcessor.DataConsumer.models.ApiMessages.MockApiMessage"
        );
        return new DefaultKafkaConsumerFactory<>(configs, new StringDeserializer(), new JsonDeserializer<>(ApiMessageInterface.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ApiMessageInterface> getStandardContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, ApiMessageInterface> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(getConsumerFactory());
        return factory;
    }

}

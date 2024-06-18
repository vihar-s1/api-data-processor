package com.apiDataProcessor.producer.config;

import com.apiDataProcessor.models.apiResponse.ApiResponseInterface;
import org.apache.kafka.common.serialization.StringSerializer;
import org.reflections.Reflections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    @Bean
    public ProducerFactory<String, ApiResponseInterface> getProducerFactory(){
        Map<String, Object> configs = new HashMap<>();

        configs.put(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServer);
        configs.put(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);

        /*DYNAMIC TYPE MAPPING BUILDING*/
        StringBuilder typeMappings = new StringBuilder();
        String apiMessagePackage = "com.VersatileDataProcessor.DataProducer.models.apiMessages";
        Reflections reflections = new Reflections(apiMessagePackage);

//        Set<Class<? extends ApiMessageInterface>> subTypes = reflections.getSubTypesOf(ApiMessageInterface.class);
//
//        typeMappings.append(ApiMessageInterface.class.getSimpleName())
//                .append(":")
//                .append(ApiMessageInterface.class.getCanonicalName());
//
//        subTypes.forEach(cls -> typeMappings.append(",").append(cls.getSimpleName()).append(":").append(cls.getCanonicalName()));
//
//        /*END DYNAMIC TYPE MAPPING BUILDING*/
//
//        configs.put(JsonSerializer.TYPE_MAPPINGS, typeMappings.toString());

        return new DefaultKafkaProducerFactory<>(configs, new StringSerializer(), new JsonSerializer<>());
    }

    @Bean
    public KafkaTemplate<String, ApiResponseInterface> getKafkaTemplate() {
        return new KafkaTemplate<>(getProducerFactory());
    }
}

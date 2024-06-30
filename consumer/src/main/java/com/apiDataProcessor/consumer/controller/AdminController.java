package com.apiDataProcessor.consumer.controller;

import com.apiDataProcessor.consumer.services.KafkaConsumerService;
import com.apiDataProcessor.models.ApiType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Value(value = "${spring.kafka.consumer.listener-id}")
    private String listenerId;

    private final KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;
    private final KafkaConsumerService kafkaConsumerService;

    public AdminController(KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry, KafkaConsumerService kafkaConsumerService) {
        this.kafkaListenerEndpointRegistry = kafkaListenerEndpointRegistry;
        this.kafkaConsumerService = kafkaConsumerService;
    }

    @GetMapping("/pause")
    public ResponseEntity<?> pause() {
        log.info("Pausing API service.");
        // Pause API service
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(listenerId);

        if (listenerContainer == null) {
            log.error("Listener Container not found for id=[{}]", listenerId);
        }
        else if (listenerContainer.isContainerPaused()) {
            log.warn("Listener Container is already paused.");
        }
        else {
            listenerContainer.pause();
            log.info("Listener Container Paused.");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/resume")
    public ResponseEntity<?> resume() {
        log.info("Resuming API service.");
        // Resume API service
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(listenerId);

        if (listenerContainer == null) {
            log.error("Listener Container not found for id=[{}]", listenerId);
        }
        else if (!listenerContainer.isContainerPaused()) {
            log.warn("Listener Container is already running.");
        }
        else {
            listenerContainer.resume();
            log.info("Listener Container Resumed.");
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
    
    @GetMapping("/disable/{apiType}")
    public ResponseEntity<?> disableApiType(
            @PathVariable(name = "apiType") String apiType
    ) {
        log.debug("Ignoring API Type=[{}]", apiType);
        // Ignore API Type
        boolean success = kafkaConsumerService.disableApiType(getApiType(apiType));
        if (success) {
            log.info("API Type=[{}] Ignored.", apiType);
        }
        else {
            log.error("Failed to Ignore API Type=[{}]", apiType);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/enable/{apiType}")
    public ResponseEntity<?> enableApiType(
            @PathVariable(name = "apiType") String apiType
    ) {
        log.debug("Enabling API Type=[{}]", apiType);
        // Enable API Type
        boolean success = kafkaConsumerService.enableApiType(getApiType(apiType));
        if (success) {
            log.info("API Type=[{}] Enabled.", apiType);
        }
        else {
            log.error("Failed to Enable API Type=[{}]", apiType);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/listener-state")
    public ResponseEntity<?> getListenerState() {
        log.info("Fetching Listener State.");
        // Get Listener State
        MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer(listenerId);
        if (listenerContainer == null) {
            log.error("Listener Container not found for id=[{}]", listenerId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Listener Container not found.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(listenerContainer.isRunning() ? "RUNNING" : "PAUSED");
    }

    @GetMapping("/disabled-channels")
    public ResponseEntity<?> getDisabledChannels() {
        log.info("Fetching Disabled Channels.");
        // Get Disabled Channels
        return ResponseEntity.status(HttpStatus.OK).body(kafkaConsumerService.getIgnoredApiTypes());
    }

    private ApiType getApiType(String apiTypeStr) {
        try {
            return ApiType.valueOf(apiTypeStr.toUpperCase(Locale.ROOT).replace("-", "_"));
        }
        catch (IllegalArgumentException e) {
            log.error("Invalid API type: {}", apiTypeStr);
            return null;
        }
    }
}

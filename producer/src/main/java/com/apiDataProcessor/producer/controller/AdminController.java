package com.apiDataProcessor.producer.controller;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.producer.ApiServiceScheduler;
import com.apiDataProcessor.producer.service.api.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Locale;
import java.util.Set;


@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ApiServiceScheduler apiServiceScheduler;

    public AdminController(ApiServiceScheduler apiServiceScheduler) {
        this.apiServiceScheduler = apiServiceScheduler;
    }

    @GetMapping("/trigger-scheduler")
    public ResponseEntity<?> triggerScheduler() {
        log.info("Scheduler triggered explicitly.");
        apiServiceScheduler.fetchData();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/toggle-scheduler")
    public ResponseEntity<?> disableScheduler() {
        log.info("Toggling scheduler state.");

        if (apiServiceScheduler.isExecutorActive()) {
            apiServiceScheduler.toggleOff();
        } else {
            apiServiceScheduler.toggleOn();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/scheduler-state")
    public ResponseEntity<?> getSchedulerState() {
        log.info("Fetching scheduler state.");
        return ResponseEntity.status(HttpStatus.OK).body(apiServiceScheduler.isExecutorActive() ? "ON" : "OFF");
    }

    @GetMapping("/unauthorized-channels")
    public ResponseEntity<?> getUnauthorizedChannels() {
        log.info("Fetching unauthorized channels.");
        final Set<ApiService> apiServices = apiServiceScheduler.getApiServices();

        List<ApiType> unauthorizedChannels = apiServices.stream().filter(ApiService::isUnauthorized).map(ApiService::getApiType).toList();

        return ResponseEntity.status(HttpStatus.OK).body(unauthorizedChannels);
    }

    @GetMapping("/disable/{apiType}")
    public ResponseEntity<?> disableService(
            @PathVariable(name = "apiType") String apiType
    ) {
        ApiType apiTypeEnum = getApiType(apiType);
        if (apiTypeEnum != null) {
            log.info("Disabling service: {}", apiTypeEnum);
            apiServiceScheduler.disableService(apiTypeEnum);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/enable/{apiType}")
    public ResponseEntity<?> enableService(
            @PathVariable(name = "apiType") String apiType
    ) {
        ApiType apiTypeEnum = getApiType(apiType);

        if (apiTypeEnum != null) {
            log.info("Enabling service: {}", apiTypeEnum);
            apiServiceScheduler.enableService(apiTypeEnum);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
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

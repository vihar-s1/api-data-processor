package com.apiDataProcessor.producer.controller;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.producer.ApiServiceScheduler;
import com.apiDataProcessor.producer.service.api.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Set;

import static com.apiDataProcessor.utils.utils.validateBasicAuth;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Value(value = "${admin.username}")
    private String adminUsername;

    @Value(value = "${admin.password}")
    private String adminPassword;

    private final ApiServiceScheduler apiServiceScheduler;

    public AdminController(ApiServiceScheduler apiServiceScheduler) {
        this.apiServiceScheduler = apiServiceScheduler;
    }

    @GetMapping("/trigger-scheduler")
    public ResponseEntity<?> triggerScheduler(@RequestHeader(value = "Authorization") String authHeader) {
        if (isInvalidAuthHeader(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Scheduler triggered explicitly.");
        apiServiceScheduler.fetchData();
        return ResponseEntity.status(HttpStatus.OK).build();

    }

    @GetMapping("/toggle-scheduler")
    public ResponseEntity<?> disableScheduler(@RequestHeader(value = "Authorization") String authHeader) {
        if (isInvalidAuthHeader(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Toggling scheduler state.");

        if (apiServiceScheduler.isExecutorActive()) {
            apiServiceScheduler.toggleOff();
        } else {
            apiServiceScheduler.toggleOn();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/scheduler-state")
    public ResponseEntity<?> getSchedulerState(@RequestHeader(value = "Authorization") String authHeader) {
        if (isInvalidAuthHeader(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Fetching scheduler state.");
        return ResponseEntity.status(HttpStatus.OK).body(apiServiceScheduler.isExecutorActive() ? "ON" : "OFF");
    }

    @GetMapping("/unauthorized-channels")
    public ResponseEntity<?> getUnauthorizedChannels(@RequestHeader(value = "Authorization") String authHeader) {
        if (isInvalidAuthHeader(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        log.info("Fetching unauthorized channels.");
        final Set<ApiService> apiServices = apiServiceScheduler.getApiServices();

        List<ApiType> unauthorizedChannels = apiServices.stream().filter(apiService -> apiService.isUnauthorized()).map(ApiService::getApiType).toList();

        return ResponseEntity.status(HttpStatus.OK).body(unauthorizedChannels);
    }

    @GetMapping("/disable/{apiType}")
    public ResponseEntity<?> disableService(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable(name = "apiType") String apiType
    ) {
        if (isInvalidAuthHeader(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ApiType apiTypeEnum = getApiType(apiType);
        if (apiTypeEnum != null) {
            log.info("Disabling service: {}", apiTypeEnum);
            apiServiceScheduler.disableService(apiTypeEnum);
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/enable/{apiType}")
    public ResponseEntity<?> enableService(
            @RequestHeader(value = "Authorization") String authHeader,
            @PathVariable(name = "apiType") String apiType
    ) {
        if (isInvalidAuthHeader(authHeader)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        ApiType apiTypeEnum = getApiType(apiType);

        if (apiTypeEnum != null) {
            log.info("Enabling service: {}", apiTypeEnum);
            apiServiceScheduler.enableService(apiTypeEnum);
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private boolean isInvalidAuthHeader(String authHeader) {
        return !validateBasicAuth(authHeader, adminUsername, adminPassword);
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

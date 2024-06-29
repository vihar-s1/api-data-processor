package com.apiDataProcessor.producer;

import com.apiDataProcessor.producer.service.api.ApiService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public final class ApiServiceScheduler {

    public ApiServiceScheduler(Set<ApiService> apiServices) {
        this.apiServices = apiServices;
    }

    @Getter
    private final Set<ApiService> apiServices;

    BlockingQueue<Runnable> workerQueue = new LinkedBlockingQueue<>(10);
    ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, workerQueue);
    Boolean isExecutorActive = true;

    @Scheduled(fixedRate = 60_000) // Run every 1 minute
    public void fetchData() {
        if (!isExecutorActive) {
            log.warn("Executor is not active. Please turn on the executor to fetch data.");
            return;
        }
        apiServices.forEach(apiService -> {
            if (apiService.isExecutable()) {
                threadPoolExecutor.execute(apiService::fetchData);
            }
        });
    }

    public void toggleOff() {
        if (!threadPoolExecutor.isShutdown()) {
            log.info("Shutting down executor.");
            threadPoolExecutor.shutdown();
            isExecutorActive = false;
        }
    }

    public void toggleOn() {
        if (threadPoolExecutor.isShutdown()) {
            log.info("Starting new executor.");
            threadPoolExecutor = new ThreadPoolExecutor(10, 10, 0L, TimeUnit.MILLISECONDS, workerQueue);
            isExecutorActive = true;
        }
    }

    public Boolean isExecutorActive() {
        return isExecutorActive;
    }
}
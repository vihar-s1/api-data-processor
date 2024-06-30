package com.apiDataProcessor.elasticsearchManager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {

    private final ElasticsearchController elasticsearchController;

    public AdminController(ElasticsearchController elasticsearchController) {
        this.elasticsearchController = elasticsearchController;
    }

    @GetMapping("/disable")
    public ResponseEntity<?> disable() {
        log.info("Disabling application");

        if (elasticsearchController.isDisabled()) {
            log.warn("Application is already disabled");
        }
        else {
            elasticsearchController.disable();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/enable")
    public ResponseEntity<?> enable() {
        log.info("Enabling application");

        if (!elasticsearchController.isDisabled()) {
            log.warn("Application is already enabled");
        }
        else {
            elasticsearchController.enable();
        }

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

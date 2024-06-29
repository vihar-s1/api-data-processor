package com.apiDataProcessor.searchService.controller;

import ch.qos.logback.classic.Level;
import com.apiDataProcessor.models.ExternalResponse;
import com.apiDataProcessor.searchService.repositories.CentralRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/restricted")
public class RestrictedController extends AbstractController {

    public RestrictedController(CentralRepository centralRepository) {
        super(centralRepository);
    }

    @GetMapping("/loglevel/{moduleName}/{logLevel}")
    public ResponseEntity<ExternalResponse<?>> setLogLevel(
            @PathVariable(name = "moduleName") String moduleName,
            @PathVariable(name = "logLevel") String logLevel
    ) {
        try {
            log.info("Setting log level to {}", logLevel);
            ch.qos.logback.classic.Logger logger;

            switch (moduleName.toLowerCase()) {
                case "controller" -> logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(AbstractController.class.getPackageName());
                case "repository" -> logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(CentralRepository.class.getPackageName());
                default -> logger = null;
            }

            if (logger == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ExternalResponse.builder().success(false).error("Invalid Module Name").build()
                );
            }

            Level level = Level.toLevel(logLevel.toUpperCase(), null);

            if (level == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ExternalResponse.builder().success(false).error("Invalid Log Level").build()
                );
            }

            logger.setLevel(level);

            return ResponseEntity.status(HttpStatus.OK).body(
                    ExternalResponse.builder().success(true).build()
            );
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return handleGenericException(e);
        }
    }



    @Override
    protected ResponseEntity<ExternalResponse<?>> handleGenericException(Exception exception){
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExternalResponse.builder().success(false).error(exception.getMessage()).build()
        );
    }
}
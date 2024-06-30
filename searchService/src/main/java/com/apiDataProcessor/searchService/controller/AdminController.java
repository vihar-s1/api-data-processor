package com.apiDataProcessor.searchService.controller;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.apiDataProcessor.models.ExternalResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController extends AbstractController {

    private final ApiController apiController;

    public AdminController(ApiController apiController) {
        this.apiController = apiController;
    }

    @GetMapping("/loglevel/{moduleName}/{logLevel}")
    public ResponseEntity<?> setLogLevel(
            @PathVariable(name = "moduleName") String moduleName,
            @PathVariable(name = "logLevel") String logLevel
    ) {
        try {
            Logger logger = null;

            Optional<Package> packageName = Arrays.stream(Package.getPackages())
                    .filter(p -> p.getName().contains(moduleName))
                    .findFirst();

            if (packageName.isPresent()) {
                logger = (Logger) LoggerFactory.getLogger(packageName.get().getName());
            }

            Level level = Level.toLevel(logLevel.toUpperCase(), null);
            if (logger != null && level != null) {
                logger.setLevel(level);
                log.info("Log level for module [{}] set to [{}]", packageName.get().getName(), level.levelStr);
            }
            else {
                log.error("Invalid module name or log level");
            }
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/disable")
    public ResponseEntity<?> disableController() {
        apiController.disableController();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/enable")
    public ResponseEntity<?> enableController() {
        apiController.enableController();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Override
    protected ResponseEntity<ExternalResponse<?>> handleGenericException(Exception exception){
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExternalResponse.builder().success(false).error(exception.getMessage()).build()
        );
    }
}
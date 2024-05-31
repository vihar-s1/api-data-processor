package com.versatileDataProcessor.elasticsearchWriter.controller;

import com.versatileDataProcessor.elasticsearchWriter.models.MyResponseBody;
import com.versatileDataProcessor.elasticsearchWriter.models.processedMessages.MessageInterface;
import com.versatileDataProcessor.elasticsearchWriter.models.standardMessage.Adapter;
import com.versatileDataProcessor.elasticsearchWriter.models.standardMessage.StandardMessage;
import com.versatileDataProcessor.elasticsearchWriter.repositories.CentralRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
public class ElasticsearchController {

    private final CentralRepository centralRepository;

    public ElasticsearchController(CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<MyResponseBody<Object>> addMessage(@RequestBody MessageInterface message) {
        try {
            if (message == null || message.getId() == null || message.getId().isBlank() ) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : id was either empty or null : dataType=[{}]",
                        HttpStatus.BAD_REQUEST,
                        message == null ? "null" : message.getMessageType()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new MyResponseBody<>("Validation Failed. Id cannot be empty or null", false, message)
                );
            }

            if (centralRepository.findById(message.getId()).isPresent()) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : resource already exists : dataType=[{}]",
                        HttpStatus.CONFLICT,
                        message.getMessageType()
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new MyResponseBody<>("The resource you are trying to create already exists", false, message)
                );
            }

            StandardMessage savedMessage = centralRepository.save(Adapter.genericAdapter(message));
            log.info(
                    "[POST /api/add] Successful with return-code=[{}] : dataType=[{}]",
                    HttpStatus.OK,
                    message.getMessageType()
            );
            return ResponseEntity.status(HttpStatus.OK).body(
                    new MyResponseBody<>("The resource was created successfully", true, savedMessage)
            );
        } catch (Exception exception) {
            log.error(
                    "Error Processing [POST /api/add] : Exception=[{}] : Message=[{}] : dataType=[{}]",
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    message == null ? "null" : message.getMessageType()
            );
            log.debug(
                    "Error Occurred for Object=[{}]", message
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MyResponseBody<>("Internal Server Error !", false, null)
            );
        }
    }
}
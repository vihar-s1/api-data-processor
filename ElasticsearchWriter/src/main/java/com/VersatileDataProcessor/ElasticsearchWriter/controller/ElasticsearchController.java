package com.VersatileDataProcessor.ElasticsearchWriter.controller;

import com.VersatileDataProcessor.ElasticsearchWriter.models.ApiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.ElasticsearchWriter.models.MyResponseBody;
import com.VersatileDataProcessor.ElasticsearchWriter.repositories.ApiMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ElasticsearchController {

    @Autowired
    private ApiMessageRepository messageRepository;

    @PostMapping("/add")
    public ResponseEntity<MyResponseBody<Object>> addMessage(@RequestBody ApiMessageInterface message){
        if (message == null || message.getId() == null){
            log.info("[POST /api/add] failed with error-code=[" + HttpStatus.BAD_REQUEST + "]");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MyResponseBody<>("Validation Failed. Id cannot be empty or null", false, message)
            );
        }

        if (messageRepository.findById(message.getId()).isPresent()){
            log.info("[POST /api/add] failed with error-code=[" + HttpStatus.CONFLICT + "]");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new MyResponseBody<>("The resource you are trying to create already exists", false, message)
            );
        }

        log.info("[POST /api/add] Successful with return-code=[" + HttpStatus.OK + "]");

        return ResponseEntity.status(HttpStatus.OK).body(
                new MyResponseBody<>("The resource was created successfully", true, messageRepository.save(message))
        );
    }

    @GetMapping("/all")
    public ResponseEntity<MyResponseBody<Object>> getAllMessages() {
        log.info("[GET /api/all] Successful with return-code=[" + HttpStatus.OK + "]");
        return ResponseEntity.status(HttpStatus.OK).body(
                new MyResponseBody<>("Fetching All messages", true, messageRepository.findAll())
        );
    }
}

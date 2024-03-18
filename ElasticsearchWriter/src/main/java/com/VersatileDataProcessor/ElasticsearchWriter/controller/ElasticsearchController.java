package com.VersatileDataProcessor.ElasticsearchWriter.controller;

import com.VersatileDataProcessor.ElasticsearchWriter.models.ApiMessages.ApiMessageInterface;
import com.VersatileDataProcessor.ElasticsearchWriter.models.MyResponseBody;
import com.VersatileDataProcessor.ElasticsearchWriter.repositories.ApiMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ElasticsearchController {

    @Autowired
    private ApiMessageRepository messageRepository;

    @PostMapping("/add")
    public ResponseEntity<MyResponseBody<Object>> addMessage(@RequestBody ApiMessageInterface message){
        if (message == null || message.getId() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MyResponseBody<>("Validation Failed. Id cannot be empty or null", false, message)
            );
        }

        if (messageRepository.findById(message.getId()).isPresent()){
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    new MyResponseBody<>("The resource you are trying to create already exists", false, message)
            );
        }

        return ResponseEntity.status(HttpStatus.OK).body(
                new MyResponseBody<>("The resource was created successfully", true, messageRepository.save(message))
        );
    }

    @GetMapping("/all")
    public ResponseEntity<MyResponseBody<Object>> getAllMessages() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new MyResponseBody<>("Fetching All messages", true, messageRepository.findAll())
        );
    }
}

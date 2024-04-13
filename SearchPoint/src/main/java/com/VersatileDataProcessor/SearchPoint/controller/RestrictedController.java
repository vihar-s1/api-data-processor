package com.versatileDataProcessor.searchPoint.controller;

import com.versatileDataProcessor.searchPoint.models.MyResponseBody;
import com.versatileDataProcessor.searchPoint.models.StandardMessage;
import com.versatileDataProcessor.searchPoint.repositories.CentralRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/restricted")
public class RestrictedController {

    private final CentralRepository centralRepository;

    public RestrictedController(CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<MyResponseBody<Object>> getAllMessages() {
        try {
            Iterable<StandardMessage> standardMessages = centralRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(
                    new MyResponseBody<>("All Messages", true, standardMessages)
            );
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new MyResponseBody<>(e.getMessage(), false, null)
            );
        }
    }
}
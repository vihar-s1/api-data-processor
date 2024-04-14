package com.versatileDataProcessor.searchPoint.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.versatileDataProcessor.searchPoint.models.MessageType;
import com.versatileDataProcessor.searchPoint.models.MyResponseBody;
import com.versatileDataProcessor.searchPoint.models.StandardMessage;
import com.versatileDataProcessor.searchPoint.repositories.CentralRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController {

    private final CentralRepository centralRepository;
    private static final int MIN_PAGE = 1;
    private static final int MIN_PAGE_SIZE = 1;
    private static final int MAX_PAGE_SIZE = 50;

    public ApiController(CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    @GetMapping("/{messageType}/all")
    public ResponseEntity<MyResponseBody<Object>> getAllMessages(
            @PathVariable String messageType,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
        try {
            MessageType type = MessageType.valueOf(messageType.toUpperCase().replace("-", "_"));

            if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                log.error("Invalid Page=[{}] and PageSize=[{}] requested", pageSize, page);

                String jsonData = String.format(
                        "{\"page\": {\"expected\": {\"min\": %d}, \"received\": %d}, \"pageSize\": {\"expected\": {\"min\": %d, \"max\": %d}, \"received\": %d}}",
                        MIN_PAGE, page, MIN_PAGE_SIZE, MAX_PAGE_SIZE, pageSize
                );

                ObjectMapper objectMapper = new ObjectMapper();
                Map<String, Object> data = objectMapper.readValue(jsonData, new TypeReference<>() {});

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new MyResponseBody<>("Invalid 'page' or 'pageSize' value", false, data)
                );
            }
            // PageRequest.of takes 0-based page number and pageSize.
            List<StandardMessage> standardMessages = centralRepository.findAllByMessageType(type, PageRequest.of(page-1, pageSize));
            Map<String, Object> data = new HashMap<>();
            data.put("standardMessages", standardMessages);
            data.put("size", standardMessages.size());
            return ResponseEntity.status(HttpStatus.OK).body(
                    new MyResponseBody<>("All Messages", true, data)
            );
        }
        catch (IllegalArgumentException e) {
            log.error( "Invalid Endpoint : [/api/{}/all] : {} : Invalid MessageType value received", messageType, messageType);
            Map<String, String> data = new HashMap<>();
            data.put("messageType", messageType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new MyResponseBody<>("Invalid MessageType", false, data)
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
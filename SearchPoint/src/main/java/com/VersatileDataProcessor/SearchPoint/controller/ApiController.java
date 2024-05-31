package com.versatileDataProcessor.searchPoint.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
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

import java.util.*;

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
                return handleInvalidPageOrPageSize(page, pageSize, "GET /" + messageType + "/all");
            }
            // PageRequest.of takes 0-based page number and pageSize.
            List<StandardMessage> standardMessages = centralRepository.findAllByMessageType(type, PageRequest.of(page-1, pageSize));
            Map<String, Object> data = new HashMap<>();
            data.put("standardMessages", standardMessages);
            data.put("size", standardMessages.size());

            return handleGenericSuccess(data, "[GET /{}/all]: Executed Successfully", messageType);
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
            return handleGenericException(e);
        }
    }


    @GetMapping("/message/{messageId}")
    public ResponseEntity<MyResponseBody<Object>> getMessageById(@PathVariable String messageId) {
        try {
            if (messageId == null || messageId.isBlank()){
                log.error("[GET /message/{}]: null or blank messageId received", messageId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new MyResponseBody<>("Invalid or Missing Message ID", false, null)
                );
            }
            Optional<StandardMessage> standardMessage = centralRepository.findById(messageId);

            if (standardMessage.isEmpty()){
                log.warn("[GET /message/{}]: No StandardMessage Object found for given ID", messageId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new MyResponseBody<>("No StandardMessage Object found for given ID", false, null)
                );
            }
            return handleGenericSuccess(standardMessage.get(), "[GET /message/{}]: Executed Successfully", messageId);
        }
        catch (Exception exception) {
            return handleGenericException(exception);
        }
    }


    @GetMapping("/tag/{tag}")
    public ResponseEntity<MyResponseBody<Object>> searchMessagesByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
            try {
                if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                    return handleInvalidPageOrPageSize(page, pageSize, "GET /searchByTag");
                }

                List<StandardMessage> standardMessages = centralRepository.findAllByTagsContainingIgnoreCase(tag, PageRequest.of(page-1, pageSize));
                Map<String, Object> data = new HashMap<>();
                data.put("standardMessages", standardMessages);
                data.put("size", standardMessages.size());

                return handleGenericSuccess(data, "[GET /searchByTag]: Executed Successfully");
            }
            catch (Exception e) {
                return handleGenericException(e);
            }
        }

    @GetMapping("/category/{category}")
    public ResponseEntity<MyResponseBody<Object>> searchMessagesByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
        try {
            if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                return handleInvalidPageOrPageSize(page, pageSize, "GET /searchByTag");
            }

            List<StandardMessage> standardMessages = centralRepository.findAllByAdditionalDataContainsIgnoreCase("category", category, PageRequest.of(page-1, pageSize));
            Map<String, Object> data = new HashMap<>();
            data.put("standardMessages", standardMessages);
            data.put("size", standardMessages.size());

            return handleGenericSuccess(data, "[GET /searchMessagesByCategory]: Executed Successfully");
        }
        catch (Exception e) {
            return handleGenericException(e);
        }
    }


    @GetMapping("/username/{userName}")
    public ResponseEntity<MyResponseBody<Object>> searchMessagesByUserName(
            @PathVariable String userName,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
        try {
            if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                return handleInvalidPageOrPageSize(page, pageSize, "GET /searchByUserName");
            }

            List<StandardMessage> standardMessages = centralRepository.findAllByNameContainingIgnoreCase(userName, PageRequest.of(page-1, pageSize));
            Map<String, Object> data = new HashMap<>();
            data.put("standardMessages", standardMessages);
            data.put("size", standardMessages.size());

            return handleGenericSuccess(data, "[GET /searchByUserName]: Executed Successfully");
        }
        catch (Exception e) {
            return handleGenericException(e);
        }
    }


    /****************************************** PRIVATE METHODS ******************************************/

    private ResponseEntity<MyResponseBody<Object>> handleGenericException(Exception exception){
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new MyResponseBody<>(exception.getMessage(), false, null)
        );
    }

    private ResponseEntity<MyResponseBody<Object>> handleGenericSuccess(Object data, String logMessage, Object... logMessageArguments) {
        log.info(logMessage, logMessageArguments);
        return ResponseEntity.status(HttpStatus.OK).body(
                new MyResponseBody<>("Operation Successful", true, data)
        );
    }

    private ResponseEntity<MyResponseBody<Object>> handleInvalidPageOrPageSize(int page, int pageSize, String callingEndpoint) throws JsonProcessingException {
        log.error("[{}]: Invalid Page=[{}] or PageSize=[{}] requested", callingEndpoint, page, pageSize);

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
}
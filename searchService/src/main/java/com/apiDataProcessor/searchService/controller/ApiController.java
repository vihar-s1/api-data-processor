package com.apiDataProcessor.searchService.controller;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.InternalHttpResponse;
import com.apiDataProcessor.models.standardMediaData.StandardMediaData;
import com.apiDataProcessor.searchPoint.repositories.CentralRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @GetMapping("/{apiType}/all")
    public ResponseEntity<InternalHttpResponse<Object>> getAllMessages(
            @PathVariable String apiType,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
        try {
            ApiType apiType_enum = ApiType.valueOf(apiType.toUpperCase().replace("-", "_"));

            if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                return handleInvalidPageOrPageSize(page, pageSize, "GET /" + apiType + "/all");
            }
            // PageRequest.of takes 0-based page number and pageSize.
            List<StandardMediaData> StandardMediaDatas = centralRepository.findAllByApiType(apiType_enum, PageRequest.of(page-1, pageSize));
            Map<String, Object> data = new HashMap<>();
            data.put("StandardMediaDatas", StandardMediaDatas);
            data.put("size", StandardMediaDatas.size());

            return handleGenericSuccess(data, "[GET /{}/all]: Executed Successfully", apiType);
        }
        catch (IllegalArgumentException e) {
            log.error( "Invalid Endpoint : [/api/{}/all] : {} : Invalid ApiType value received", apiType, apiType);
            Map<String, Object> data = new HashMap<>();
            data.put("apiType", apiType);
            data.put("message", "Invalid apiType Value");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new InternalHttpResponse<>(false, data)
            );
        }
        catch (Exception e) {
            return handleGenericException(e);
        }
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<InternalHttpResponse<Object>> getMessageById(@PathVariable String messageId) {
        try {
            if (messageId == null || messageId.isBlank()){
                log.error("[GET /message/{}]: null or blank messageId received", messageId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new InternalHttpResponse<>(false,"Invalid or Missing Message ID")
                );
            }
            Optional<StandardMediaData> StandardMediaData = centralRepository.findById(messageId);

            if (StandardMediaData.isEmpty()){
                log.warn("[GET /message/{}]: No StandardMediaData Object found for given ID", messageId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        new InternalHttpResponse<>(false,"No StandardMediaData Object found for given ID")
                );
            }
            return handleGenericSuccess(StandardMediaData.get(), "[GET /message/{}]: Executed Successfully", messageId);
        }
        catch (Exception exception) {
            return handleGenericException(exception);
        }
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<InternalHttpResponse<Object>> searchMessagesByTag(
            @PathVariable String tag,
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "1") int page
    ) {
            try {
                if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                    return handleInvalidPageOrPageSize(page, pageSize, "GET /searchByTag");
                }

                List<StandardMediaData> StandardMediaDatas = centralRepository.findAllByTagsContainingIgnoreCase(tag, PageRequest.of(page-1, pageSize));
                Map<String, Object> data = new HashMap<>();
                data.put("StandardMediaDatas", StandardMediaDatas);
                data.put("size", StandardMediaDatas.size());

                return handleGenericSuccess(data, "[GET /searchByTag]: Executed Successfully");
            }
            catch (Exception e) {
                return handleGenericException(e);
            }
        }


    /****************************************** PRIVATE METHODS ******************************************/

    private ResponseEntity<InternalHttpResponse<Object>> handleGenericException(Exception exception){
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new InternalHttpResponse<>(false, exception.getMessage())
        );
    }

    private ResponseEntity<InternalHttpResponse<Object>> handleGenericSuccess(Object data, String logMessage, Object... logMessageArguments) {
        log.info(logMessage, logMessageArguments);
        return ResponseEntity.status(HttpStatus.OK).body(
                new InternalHttpResponse<>( true, data)
        );
    }

    private ResponseEntity<InternalHttpResponse<Object>> handleInvalidPageOrPageSize(int page, int pageSize, String callingEndpoint) throws JsonProcessingException {
        log.error("[{}]: Invalid Page=[{}] or PageSize=[{}] requested", callingEndpoint, page, pageSize);

        String jsonData = String.format(
                "{\"page\": {\"expected\": {\"min\": %d}, \"received\": %d}, \"pageSize\": {\"expected\": {\"min\": %d, \"max\": %d}, \"received\": %d}}",
                MIN_PAGE, page, MIN_PAGE_SIZE, MAX_PAGE_SIZE, pageSize
        );

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> data = objectMapper.readValue(jsonData, new TypeReference<>() {});

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new InternalHttpResponse<>(false, data)
        );
    }
}
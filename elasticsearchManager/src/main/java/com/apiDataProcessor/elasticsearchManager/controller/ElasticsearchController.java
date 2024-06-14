package com.apiDataProcessor.elasticsearchManager.controller;

import com.apiDataProcessor.elasticsearchManager.repository.MediaDataRepository;
import com.apiDataProcessor.models.InternalHttpResponse;
import com.apiDataProcessor.models.standardMediaData.StandardMediaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ElasticsearchController {

    private final MediaDataRepository mediaDataRepository;

    public ElasticsearchController(MediaDataRepository mediaDataRepository) {
        this.mediaDataRepository = mediaDataRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<InternalHttpResponse<?>> addMessage(@RequestBody StandardMediaData mediaData) {
        try {
            if (mediaData == null || mediaData.getId() == null || mediaData.getId().isBlank() ) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : id was either empty or null : apiType=[{}]",
                        HttpStatus.BAD_REQUEST,
                        mediaData == null ? "null" : mediaData.getApiType()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new InternalHttpResponse<>(false,"Validation Failed. Id cannot be empty or null")
                );
            }

            if (mediaDataRepository.findById(mediaData.getId()).isPresent()) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : resource already exists : apiType=[{}]",
                        HttpStatus.CONFLICT,
                        mediaData.getApiType()
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new InternalHttpResponse<>(false, "The resource you are trying to create already exists")
                );
            }

            StandardMediaData savedMediaData = mediaDataRepository.save(mediaData);
            log.info(
                    "[POST /api/add] Successful with return-code=[{}] : apiType=[{}]",
                    HttpStatus.OK,
                    mediaData.getApiType()
            );
            return ResponseEntity.status(HttpStatus.OK).body(
                    new InternalHttpResponse<>(true, savedMediaData)
            );
        } catch (Exception exception) {
            log.error(
                    "Error Processing [POST /api/add] : Exception=[{}] : Message=[{}] : apiType=[{}]",
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    mediaData == null ? "null" : mediaData.getApiType()
            );
            log.debug(
                    "Error Occurred for Object=[{}]", mediaData
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new InternalHttpResponse<>(false, "Internal Server Error !")
            );
        }
    }

    @GetMapping("/all")
    public ResponseEntity<InternalHttpResponse<?>> getAllMessages() {
        try {
            log.info("[GET /api/all] : Request Received");
            return ResponseEntity.status(HttpStatus.OK).body(
                    new InternalHttpResponse<>(true, mediaDataRepository.findAll())
            );
        } catch (Exception exception) {
            log.error(
                    "Error Processing [GET /api/all] : Exception=[{}] : Message=[{}]",
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            );
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new InternalHttpResponse<>(false, "Internal Server Error !")
            );
        }
    }
}
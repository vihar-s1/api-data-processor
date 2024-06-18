package com.apiDataProcessor.elasticsearchManager.controller;

import com.apiDataProcessor.elasticsearchManager.repository.ChannelPostRepository;
import com.apiDataProcessor.models.InternalHttpResponse;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
public class ElasticsearchController {

    private final ChannelPostRepository channelPostRepository;

    public ElasticsearchController(ChannelPostRepository channelPostRepository) {
        this.channelPostRepository = channelPostRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<InternalHttpResponse<?>> addMessage(@RequestBody GenericChannelPost channelPost) {
        try {
            if (channelPost == null || channelPost.getId() == null || channelPost.getId().isBlank() ) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : id was either empty or null : apiType=[{}]",
                        HttpStatus.BAD_REQUEST,
                        channelPost == null ? "null" : channelPost.getApiType()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        new InternalHttpResponse<>(false,"Validation Failed. Id cannot be empty or null")
                );
            }

            if (channelPostRepository.findById(channelPost.getId()).isPresent()) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : resource already exists : apiType=[{}]",
                        HttpStatus.CONFLICT,
                        channelPost.getApiType()
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        new InternalHttpResponse<>(false, "The resource you are trying to create already exists")
                );
            }

            GenericChannelPost savedchannelPost = channelPostRepository.save(channelPost);
            log.info(
                    "[POST /api/add] Successful with return-code=[{}] : apiType=[{}]",
                    HttpStatus.OK,
                    channelPost.getApiType()
            );
            return ResponseEntity.status(HttpStatus.OK).body(
                    new InternalHttpResponse<>(true, savedchannelPost)
            );
        } catch (Exception exception) {
            log.error(
                    "Error Processing [POST /api/add] : Exception=[{}] : Message=[{}] : apiType=[{}]",
                    exception.getClass().getSimpleName(),
                    exception.getMessage(),
                    channelPost == null ? "null" : channelPost.getApiType()
            );
            log.debug(
                    "Error Occurred for Object=[{}]", channelPost
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
                    new InternalHttpResponse<>(true, channelPostRepository.findAll())
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
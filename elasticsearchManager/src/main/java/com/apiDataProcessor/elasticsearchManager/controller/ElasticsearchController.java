package com.apiDataProcessor.elasticsearchManager.controller;

import com.apiDataProcessor.elasticsearchManager.repository.ChannelPostRepository;
import com.apiDataProcessor.models.InternalResponse;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api")
public class ElasticsearchController {

    private final ChannelPostRepository channelPostRepository;

    public ElasticsearchController(ChannelPostRepository channelPostRepository) {
        this.channelPostRepository = channelPostRepository;
    }

    @PostMapping("/add")
    public ResponseEntity<InternalResponse<?>> addMessage(@RequestBody GenericChannelPost channelPost) {
        try {
            if (channelPost == null || channelPost.getId() == null || channelPost.getId().isBlank() ) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : id was either empty or null : apiType=[{}]",
                        HttpStatus.BAD_REQUEST,
                        channelPost == null ? "null" : channelPost.getApiType()
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        InternalResponse.builder().success(false).data("Validation Failed. Id cannot be empty or null").build()
                );
            }

            if (channelPostRepository.findById(channelPost.getId()).isPresent()) {
                log.info(
                        "[POST /api/add] failed with error-code=[{}] : resource already exists : apiType=[{}]",
                        HttpStatus.CONFLICT,
                        channelPost.getApiType()
                );
                return ResponseEntity.status(HttpStatus.CONFLICT).body(
                        InternalResponse.builder().success(false).data("Resource Already Exists").build()
                );
            }

            GenericChannelPost savedchannelPost = channelPostRepository.save(channelPost);
            log.info(
                    "[POST /api/add] Successful with return-code=[{}] : apiType=[{}]",
                    HttpStatus.OK,
                    channelPost.getApiType()
            );
            return ResponseEntity.status(HttpStatus.OK).body(
                    InternalResponse.builder().success(true).data(savedchannelPost).build()
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
                    InternalResponse.builder().success(false).data("Internal Server Error !").build()
            );
        }
    }
}
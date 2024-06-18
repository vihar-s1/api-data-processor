package com.apiDataProcessor.elasticsearchManager.controller;

import com.apiDataProcessor.elasticsearchManager.repository.ChannelPostRepository;
import com.apiDataProcessor.models.InternalHttpResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/restricted")
public class RestrictedController {

    private final ChannelPostRepository channelPostRepository;

    public RestrictedController(ChannelPostRepository channelPostRepository) {
        this.channelPostRepository = channelPostRepository;
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

package com.apiDataProcessor.searchService.controller;

import com.apiDataProcessor.models.InternalHttpResponse;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
import com.apiDataProcessor.searchService.repositories.CentralRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/restricted")
public class RestrictedController {

    private final CentralRepository centralRepository;

    public RestrictedController(CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<InternalHttpResponse<?>> getAllMessages(
            @RequestParam(defaultValue = "20") int pageSize,
            @RequestParam(defaultValue = "0") int page
    ) {
        try {
            Page<GenericChannelPost> standardMessages = centralRepository.findAll(PageRequest.of(page, pageSize));

            return ResponseEntity.status(HttpStatus.OK).body(
                    new InternalHttpResponse<>(true, standardMessages)
            );
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new InternalHttpResponse<>(false, e)
            );
        }
    }
}
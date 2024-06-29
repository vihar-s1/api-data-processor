package com.apiDataProcessor.producer.controller;

import com.apiDataProcessor.models.InternalResponse;
import com.apiDataProcessor.producer.service.api.RedditService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/reddit")
public class RedditController {

    private final RedditService redditService;

    public RedditController(RedditService redditService) {
        this.redditService = redditService;
    }

    @GetMapping("/callback")
    public ResponseEntity<InternalResponse<Map<String, String>>> callback(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            Map<String, String> data = Maps.newHashMap();
            data.put("erorr", error);
            data.put("accessToken", null);
            log.error("Reddit callback error: {}", error);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    InternalResponse.<Map<String, String>>builder().success(false).data(data).build()
            );
        }
        if (!redditService.checkState(state)) {
            Map<String, String> data = Maps.newHashMap();
            data.put("error", "State changed error.");
            data.put("accessToken", null);
            log.error("Reddit callback state changed error.");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    InternalResponse.<Map<String, String>>builder().success(false).data(data).build()
            );
        }
        try {
            String accessToken = redditService.getAccessToken(code);
            Map<String, String> data = Maps.newHashMap();
            if (accessToken != null) {
                data.put("accessToken", accessToken);
                log.info("Reddit callback authentication successful.");
                return ResponseEntity.status(HttpStatus.OK).body(
                        InternalResponse.<Map<String, String>>builder().success(true).data(data).build()
                );
            }
            log.error("Reddit callback error: accessToken is null.");
            data.put("accessToken", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    InternalResponse.<Map<String, String>>builder().success(false).data(data).build()
            );
        }
        catch (Exception eX) {
            log.error("Reddit callback error: {}", eX.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    InternalResponse.<Map<String, String>>builder().success(false).data(Map.of("error", eX.getMessage())).build()

            );
        }
    }
}

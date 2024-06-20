package com.apiDataProcessor.producer.controller;

import com.apiDataProcessor.models.InternalHttpResponse;
import com.apiDataProcessor.producer.service.RedditService;
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
    public ResponseEntity<InternalHttpResponse<Map<String, String>>> callback(@RequestParam(value = "code", required = false) String code, @RequestParam(value = "state", required = false) String state, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            Map<String, String> data = Maps.newHashMap();
            data.put("erorr", error);
            data.put("accessToken", null);
            log.error("Reddit callback error: {}", error);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    new InternalHttpResponse<>(false, data)
            );
        }
        if (!redditService.checkState(state)) {
            Map<String, String> data = Maps.newHashMap();
            data.put("error", "State changed error.");
            data.put("accessToken", null);
            log.error("Reddit callback state changed error.");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(
                    new InternalHttpResponse<>(false, data)
            );
        }
        try {
            String accessToken = redditService.getAccessToken(code);
            Map<String, String> data = Maps.newHashMap();
            if (accessToken != null) {
                data.put("accessToken", accessToken);
                log.info("Reddit callback successful.");
                return ResponseEntity.status(HttpStatus.OK).body(
                        new InternalHttpResponse<>(true, data)
                );
            }
            log.error("Reddit callback error: accessToken is null.");
            data.put("accessToken", null);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new InternalHttpResponse<>(false, data)
            );
        }
        catch (Exception eX) {
            log.error("Reddit callback error: {}", eX.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    new InternalHttpResponse<>(false, Map.of("error", eX.getMessage()))
            );
        }
    }

    @GetMapping("/configs")
    public ResponseEntity<InternalHttpResponse<Map<String, String>>> configs() {
        return ResponseEntity.status(HttpStatus.OK).body(
                new InternalHttpResponse<>(true, redditService.getConfigs())
        );
    }
}

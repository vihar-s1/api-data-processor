package com.apiDataProcessor.searchService.controller;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.ExternalResponse;
import com.apiDataProcessor.models.genericChannelPost.GenericChannelPost;
import com.apiDataProcessor.searchService.repositories.CentralRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api")
public class ApiController extends AbstractController {

    public ApiController(CentralRepository centralRepository) {
        super(centralRepository);
    }

    @GetMapping("/{apiType}/all")
    public ResponseEntity<ExternalResponse<?>> getAllMessages(
            @PathVariable(name = "apiType") String apiType,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        try {
            ApiType apiType_enum = ApiType.valueOf(apiType.toUpperCase().replace("-", "_"));

            if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                return handleInvalidPageOrPageSize(page, pageSize, "GET /" + apiType + "/all");
            }
            // PageRequest.of takes 0-based page number and pageSize.
            Page<GenericChannelPost> genericChannelPosts = centralRepository.findAllByApiType(apiType_enum, PageRequest.of(page-1, pageSize));

            return handleGenericSuccess(genericChannelPosts.toList(), genericChannelPosts.hasNext(), "[GET /{}/all]: Executed Successfully", apiType);
        }
        catch (IllegalArgumentException e) {
            log.error( "Invalid Endpoint : [/api/{}/all] : {} : Invalid ApiType value received", apiType, apiType);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    ExternalResponse.builder().success(false).error("Invalid Api Type: " + apiType).build()
            );
        }
        catch (Exception e) {
            return handleGenericException(e);
        }
    }

    @GetMapping("/message/{messageId}")
    public ResponseEntity<ExternalResponse<?>> getMessageById(@PathVariable(name = "messageId") String messageId) {
        try {
            if (messageId == null || messageId.isBlank()){
                log.error("[GET /message/{}]: null or blank messageId received", messageId);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        ExternalResponse.builder().success(false).error("Invalid or Missing Message ID").build()
                );
            }
            Optional<GenericChannelPost> genericChannelPost = centralRepository.findById(messageId);

            if (genericChannelPost.isEmpty()){
                log.warn("[GET /message/{}]: No genericChannelPost Object found for given ID", messageId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                        ExternalResponse.builder().success(false).error("No genericChannelPost Object found for given ID").build()
                );
            }
            return handleGenericSuccess(List.of(genericChannelPost.get()), null, "[GET /message/{}]: Executed Successfully", messageId);
        }
        catch (Exception exception) {
            return handleGenericException(exception);
        }
    }

    @GetMapping("/tag/{tag}")
    public ResponseEntity<ExternalResponse<?>> searchMessagesByTag(
            @PathVariable(name = "tag") String tag,
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "page", defaultValue = "1") int page
    ) {
            try {
                if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                    return handleInvalidPageOrPageSize(page, pageSize, "GET /searchByTag");
                }

                Page<GenericChannelPost> genericChannelPosts = centralRepository.findAllByTagsContainingIgnoreCase(tag, PageRequest.of(page-1, pageSize));

                return handleGenericSuccess(genericChannelPosts.toList(), genericChannelPosts.hasNext(), "[GET /searchByTag]: Executed Successfully");
            }
            catch (Exception e) {
                return handleGenericException(e);
            }
        }

    @GetMapping("/recentposts")
    public ResponseEntity<ExternalResponse<?>> getRecentPosts(
            @RequestParam(name = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam(name = "page", defaultValue = "1") int page
    ) {
        try {
            if (page < MIN_PAGE || pageSize < MIN_PAGE_SIZE || pageSize > MAX_PAGE_SIZE) {
                return handleInvalidPageOrPageSize(page, pageSize, "GET /recentposts");
            }

            Page<GenericChannelPost> genericChannelPosts = centralRepository.findAll(PageRequest.of(page-1, pageSize, Sort.by(Sort.Order.desc("createdAt"))));

            return handleGenericSuccess(genericChannelPosts.toList(), genericChannelPosts.hasNext(), "[GET /recentposts]: Executed Successfully");
        }
        catch (Exception e) {
            return handleGenericException(e);
        }
    }


    @GetMapping("/apiTypes")
    public ResponseEntity<ExternalResponse<?>> getApiTypes() {
        try {
            return handleGenericSuccess(List.of(ApiType.values()), null, "[GET /apiTypes]: Executed Successfully");
        }
        catch (Exception e) {
            return handleGenericException(e);
        }
    }

}
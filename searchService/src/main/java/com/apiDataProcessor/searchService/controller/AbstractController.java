package com.apiDataProcessor.searchService.controller;

import com.apiDataProcessor.models.ExternalResponse;
import com.apiDataProcessor.searchService.repositories.CentralRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@Slf4j
public abstract class AbstractController {

    protected static final int MIN_PAGE = 1;
    protected static final int MIN_PAGE_SIZE = 1;
    protected static final int MAX_PAGE_SIZE = 50;

    protected final CentralRepository centralRepository;

    public AbstractController(CentralRepository centralRepository) {
        this.centralRepository = centralRepository;
    }

    /****************************************** PROTECTED METHODS ******************************************/

    protected ResponseEntity<ExternalResponse<?>> handleGenericException(Exception exception){
        log.error(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                ExternalResponse.builder().success(false).error("Internal Server Errror").build()
        );
    }

    protected <T>ResponseEntity<ExternalResponse<?>> handleGenericSuccess(List<T> posts, Boolean hasMore, String logMessage, Object... logMessageArguments) {
        log.info(logMessage, logMessageArguments);
        return ResponseEntity.status(HttpStatus.OK).body(
                ExternalResponse.<T>builder().success(true).data(posts).hasMore(hasMore).build()
        );
    }

    protected ResponseEntity<ExternalResponse<?>> handleInvalidPageOrPageSize(int page, int pageSize, String callingEndpoint) {
        log.error("[{}]: Invalid Page=[{}] or PageSize=[{}] requested", callingEndpoint, page, pageSize);

        String errorMessage = "Invalid Page[>=" + MIN_PAGE + "]=" + page + " or PageSize[" + MIN_PAGE_SIZE + " to " + MAX_PAGE_SIZE + "]=" + pageSize;

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                ExternalResponse.builder().success(false).error(errorMessage).build()
        );
    }

}

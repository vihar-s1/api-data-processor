package com.apiDataProcessor.producer.handler;

import com.apiDataProcessor.producer.service.RedditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RedditApiHandler implements ApiDataHandlerInterface {

    @Autowired
    private RedditService redditService;

    @Override
    public void fetchData() {
        redditService.fetchData();
    }
}

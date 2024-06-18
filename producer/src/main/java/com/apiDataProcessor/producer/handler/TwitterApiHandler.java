package com.apiDataProcessor.producer.handler;

import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
import com.apiDataProcessor.producer.service.ApiDataHandlerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TwitterApiHandler implements ApiDataHandlerInterface {

    private final ApiDataHandlerService apiDataHandlerService;

    @Value(value = "${twitter.api.token}")
    private String twitterBearerToken;

    public TwitterApiHandler(ApiDataHandlerService apiDataHandlerService) {
        this.apiDataHandlerService = apiDataHandlerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://api.twitter.com/2/tweets/sample/stream?tweet.fields=attachments,author_id,context_annotations,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,text,withheld&expansions=referenced_tweets.id";

        apiDataHandlerService.fetchData(
            uri,
            TwitterApiResponse.class,
            httpHeaders -> {
                httpHeaders.setBearerAuth(twitterBearerToken);
            }
        );
    }
}

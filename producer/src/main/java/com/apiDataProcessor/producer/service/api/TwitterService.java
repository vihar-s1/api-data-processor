package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
import com.apiDataProcessor.producer.service.ApiDataHandlerService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.apiDataProcessor.utils.utils.isEmpty;

@Service
public class TwitterService implements ApiServiceInterface{

    private final ApiDataHandlerService apiDataHandlerService;

    @Value(value = "${twitter.api.token}")
    private String twitterBearerToken;

    public TwitterService(ApiDataHandlerService apiDataHandlerService) {
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

    @Override
    public boolean isExecutable() {
        return !isEmpty(twitterBearerToken);
    }
}

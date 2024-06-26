package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
import com.apiDataProcessor.producer.service.ApiDataHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.apiDataProcessor.utils.utils.isEmpty;

@Slf4j
@Service
public class TwitterService extends ApiService {

    private final ApiDataHandlerService apiDataHandlerService;

    @Value(value = "${twitter.api.token}")
    private String twitterBearerToken;

    public TwitterService(ApiDataHandlerService apiDataHandlerService) {
        this.apiDataHandlerService = apiDataHandlerService;
    }

    @Override
    public void fetchData() {
        String uri = "https://api.twitter.com/2/tweets/sample/stream?tweet.fields=attachments,author_id,context_annotations,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,text,withheld&expansions=referenced_tweets.id";

        if (!isExecutable()) {
            log.warn("Twitter service not yet Authenticated. Please provide a Bearer Token.");
            return;
        }
        else if (!isAuthorized()) {
            log.warn("Twitter service not yet Authenticated. Please provide a valid Bearer Token.");
            return;
        }

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

    @Override
    public boolean isAuthorized() {
        if (!isExecutable()) {
            return false;
        }
        // check if the token is valid

        return true;
    }
}

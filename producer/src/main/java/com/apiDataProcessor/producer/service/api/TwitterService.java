package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
import com.apiDataProcessor.producer.service.ApiRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

import static com.apiDataProcessor.utils.utils.basicAuthHeader;
import static com.apiDataProcessor.utils.utils.isEmpty;

@Slf4j
@Service
public class TwitterService extends ApiService {

    @Value(value = "${twitter.api.key}")
    private String apiKey;
    @Value(value = "${twitter.api.secret}")
    private String apiSecret;

    private String twitterBearerToken = null;

    public TwitterService(ApiRequestService apiRequestService) {
        super(apiRequestService);
    }

    @Override
    public void fetchData() {
        String uri = "https://api.twitter.com/2/tweets/sample/stream?tweet.fields=attachments,author_id,context_annotations,created_at,entities,geo,id,in_reply_to_user_id,lang,possibly_sensitive,public_metrics,referenced_tweets,text,withheld&expansions=referenced_tweets.id";

        if (!isExecutable()) {
            log.warn("Twitter service not yet Authenticated. Please provide a Bearer Token.");
            return;
        }
        else if (!isAuthorized()) {
            if (isEmpty(getAccessToken())) {
                return;
            }
            log.info("Twitter service authenticated!!");
        }

        apiRequestService.fetchData(
                uri,
                TwitterApiResponse.class,
                httpHeaders -> httpHeaders.setBearerAuth(twitterBearerToken)
        );
    }

    @Override
    public boolean isExecutable() {
        return !isEmpty(apiKey) && !isEmpty(apiSecret);
    }

    @Override
    public boolean isAuthorized() {
        return !isEmpty(twitterBearerToken);
    }

    public String getAccessToken() {
        if (!isExecutable()) {
            return null;
        }

        Map<String, Object> responseBody;
        try {
            responseBody = apiRequestService.post(
                    "https://api.twitter.com/oauth2/token",
                    Map.of("Authorization", basicAuthHeader(apiKey, apiSecret)),
                    Map.of("grant_type", "client_credentials"),
                    null
            );
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred while fetching access token: {}", e.getMessage());
            return null;
        }

        if (responseBody == null || !responseBody.containsKey("access_token")) {
            log.error("Missing access token in response: {}", responseBody);
            return null;
        }

        this.twitterBearerToken = (String) responseBody.get("access_token");
        log.info("Successfully fetched Twitter Bearer Token");
        return this.twitterBearerToken;
    }
}

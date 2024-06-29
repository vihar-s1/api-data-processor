package com.apiDataProcessor.producer.service.api;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.reddit.RedditApiResponse;
import com.apiDataProcessor.producer.service.ApiRequestService;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.UUID;

import static com.apiDataProcessor.utils.utils.*;

@Slf4j
@Service
public class RedditService extends ApiService {
    private final String STATE = hashString(UUID.randomUUID().toString()); // randomizing state string

    @Value(value =  "${reddit.api.clientId}")
    private String clientId;
    @Value(value = "${reddit.api.clientSecret}")
    private String clientSecret;
    @Value(value = "${reddit.api.redirectUri}")
    private String redirectUri;

    private final String accessTokenUri = "https://www.reddit.com/api/v1/access_token";

    private String refreshToken = null;
    private String accessToken = null;
    private Timestamp accessTokenExpiryTimeStamp;

    public RedditService(ApiRequestService apiRequestService) {
        super(apiRequestService);
    }

    @Override
    public void fetchData() {

        if (!isExecutable()) {
            log.warn("Reddit service not yet Authenticated. Please provide Client ID, Client Secret and Redirect URI.");
            return;
        }
        else if (!isAuthorized()) {
            log.warn("Reddit service not yet Authenticated. Please authenticate via: {}", getAuthUrl());
            return;
        }
        else if (tokenExpired()) {
            try {
                this.accessToken = getAccessToken();
            } catch (IOException | InterruptedException eX) {
                log.error("Error occurred while fetching access token: {}", eX.getMessage());
                return;
            }
        }

        apiRequestService.fetchData(
                this.getDataUri(),
                RedditApiResponse.class,
                httpHeaders -> httpHeaders.setBearerAuth(this.accessToken)
        );
    }

    @Override
    public boolean isExecutable() {
        return !isEmpty(clientId) && !isEmpty(clientSecret) & !isEmpty(redirectUri);
    }

    @Override
    public boolean isAuthorized() {
        return !isEmpty(accessToken) && !isEmpty(refreshToken);
    }

    public ApiType getApiType() {
        return ApiType.REDDIT;
    }

    public boolean checkState(String state) {
        return state.equals(STATE);
    }

    public String getAuthUrl() {
        return "https://www.reddit.com/api/v1/authorize" +
                "?client_id=" + this.clientId +
                "&response_type=code" +
                "&state=" + this.STATE +
                "&redirect_uri=" + this.redirectUri +
                "&duration=permanent" +
                "&scope=read,identity";
    }

    public String getDataUri() {
        return "https://oauth.reddit.com/new";
    }

    public String getAccessToken(String code) throws IOException, InterruptedException {

        Map<String, String> requestParams = Maps.newHashMap();
        requestParams.put("grant_type", "authorization_code");
        requestParams.put("code", code);
        requestParams.put("redirect_uri", this.redirectUri);

        Map<String, Object> responseBody = apiRequestService.post(
                this.accessTokenUri, Map.of("Authorization", basicAuthHeader(clientId, clientSecret)), requestParams, null);


        if (responseBody == null || responseBody.getOrDefault("error", null) != null) {
            return null;
        }
        if (responseBody.getOrDefault("refresh_token", null) != null) {
            this.refreshToken = (String) responseBody.get("refresh_token");
        }
        this.accessToken = (String) responseBody.get("access_token");
        this.accessTokenExpiryTimeStamp = new Timestamp(System.currentTimeMillis() + ((Integer) responseBody.get("expires_in")) * 1000);
        return this.accessToken;
    }

    public String getAccessToken() throws IOException, InterruptedException {
        if (this.refreshToken == null) {
            return null;
        }
        if (this.accessToken != null && !tokenExpired()) {
            return this.accessToken;
        }


        Map<String, String> requestParams = Maps.newHashMap();
        requestParams.put("grant_type", "refresh_token");
        requestParams.put("refresh_token", this.refreshToken);

        Map<String, Object> responseBody = apiRequestService.post(this.accessTokenUri, Map.of("Authorization", basicAuthHeader(clientId, clientSecret)), requestParams, null);

        if (responseBody == null || responseBody.getOrDefault("error", null) != null) {
            return null;
        }
        this.accessToken = (String) responseBody.get("access_token");
        return this.accessToken;
    }

    public Map<String, String> getConfigs() {
        Map<String, String> configs = Maps.newHashMap();

        configs.put("clientId", this.clientId);
        configs.put("clientSecret", this.clientSecret);
        configs.put("redirectUri", this.redirectUri);
        configs.put("state", this.STATE);
        configs.put("refreshToken", this.refreshToken);
        configs.put("accessToken", this.accessToken);
        configs.put("accessTokenExpiryTimeStamp", this.accessTokenExpiryTimeStamp.toString());

        return configs;
    }

    private boolean tokenExpired() {
        // null expiry timestamp or a timestamp before current-time means token is expired
        return this.accessTokenExpiryTimeStamp == null || this.accessTokenExpiryTimeStamp.before(new Timestamp(System.currentTimeMillis()));
    }
}

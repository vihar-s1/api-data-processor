package com.apiDataProcessor.producer.service;

import com.apiDataProcessor.models.apiResponse.reddit.RedditApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Base64Util;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.Map;

@Service
@Slf4j
public class RedditService {
    private final String STATE = "checkIt";
//    private final String STATE = hashString(UUID.randomUUID().toString()); // randomizing state string

    @Value(value =  "${reddit.api.clientId}")
    private String clientId;
    @Value(value = "${reddit.api.clientSecret}")
    private String clientSecret;
    @Value(value = "${reddit.api.redirectUri}")
    private String redirectUri;

    private final String accessTokenUri = "https://www.reddit.com/api/v1/access_token";
    private final String dataUri = "https://oauth.reddit.com/new";

    private String refreshToken = null;
    private String accessToken = null;
    private Timestamp accessTokenExpiryTimeStamp;

    private final ApiDataHandlerService apiDataHandlerService;

    public RedditService(ApiDataHandlerService apiDataHandlerService) {
        this.apiDataHandlerService = apiDataHandlerService;
    }

    public boolean checkState(String state) {
        return state.equals(STATE);
    }

    @SuppressWarnings("unchecked")
    public String getAccessToken(String code) throws IOException, InterruptedException {
        String authenticationHeader = "Basic " + Base64Util.encode(clientId + ":" + clientSecret);
        String requestParam = "grant_type=authorization_code" + "&" + "code=" + code + "&" + "redirect_uri=" + this.redirectUri;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(accessTokenUri))
                .header("Authorization", authenticationHeader)
                .POST(HttpRequest.BodyPublishers.ofString(requestParam))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> responseBody = new ObjectMapper().readValue(response.body(), Map.class);

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

    @SuppressWarnings("unchecked")
    public String getAccessToken() throws IOException, InterruptedException {
        if (this.refreshToken == null) {
            return null;
        }
        if (this.accessToken != null && !expiredToken()) {
            return this.accessToken;
        }
        String authenticationHeader = "Basic " + Base64Util.encode(clientId + ":" + clientSecret);
        String requestParam = "grant_type=refresh_token" + "&" + "refresh_token=" + this.refreshToken;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(accessTokenUri))
                .header("Authorization", authenticationHeader)
                .POST(HttpRequest.BodyPublishers.ofString(requestParam))
                .build();

        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        Map<String, Object> responseBody = new ObjectMapper().readValue(response.body(), Map.class);

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

    public void fetchData() {
        // fetch data from Reddit
        if (this.accessToken == null) {
            if (this.refreshToken == null) {
                log.warn("Reddit service not yet Authenticated");
                return;
            }
            try {
                this.accessToken = getAccessToken();
            } catch (IOException | InterruptedException eX) {
                log.error("Error occurred while fetching access token: {}", eX.getMessage());
                return;
            }
        }
        else if (expiredToken()) {
            try {
                this.accessToken = getAccessToken();
            } catch (IOException | InterruptedException eX) {
                log.error("Error occurred while fetching access token: {}", eX.getMessage());
                return;
            }
        }

        apiDataHandlerService.fetchData(
                this.dataUri,
                RedditApiResponse.class,
                httpHeaders -> {
                    httpHeaders.setBearerAuth(this.accessToken);
                }
        );
    }

    private boolean expiredToken() {
        // null expiry timestamp or a timestamp before current-time means token is expired
        return this.accessTokenExpiryTimeStamp == null || this.accessTokenExpiryTimeStamp.before(new Timestamp(System.currentTimeMillis()));
    }
}

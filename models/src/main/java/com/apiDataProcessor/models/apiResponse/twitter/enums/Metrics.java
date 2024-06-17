package com.apiDataProcessor.models.apiResponse.twitter.enums;

import com.apiDataProcessor.models.apiResponse.twitter.Media;
import com.apiDataProcessor.models.apiResponse.twitter.Tweet;
import lombok.Getter;

import java.util.Set;

@Getter
public enum Metrics {
    IMPRESSION_COUNT(Set.of(Tweet.class)),
    URL_LINK_CLICKS(Set.of(Tweet.class)),
    USER_PROFILE_CLICKS(Set.of(Tweet.class)),
    LIKE_COUNT(Set.of(Tweet.class)),
    REPLY_COUNT(Set.of(Tweet.class)),
    RETWEET_COUNT(Set.of(Tweet.class)),
    QUOTE_COUNT(Set.of(Tweet.class)),

    PLAYBACK_0_COUNT(Set.of(Media.class)),
    PLAYBACK_25_COUNT(Set.of(Media.class)),
    PLAYBACK_50_COUNT(Set.of(Media.class)),
    PLAYBACK_75_COUNT(Set.of(Media.class)),
    PLAYBACK_100_COUNT(Set.of(Media.class)),
    VIEW_COUNT(Set.of(Media.class)),
    ;

    private final Set<Class<?>> metricClasses;

    Metrics(Set<Class<?>> metricClasses) {
        this.metricClasses = metricClasses;
    }

    public static Metrics getMetric(String metric) {
        for (Metrics m : Metrics.values()) {
            if (m.name().equalsIgnoreCase(metric)) {
                return m;
            }
        }
        return null;
    }
}

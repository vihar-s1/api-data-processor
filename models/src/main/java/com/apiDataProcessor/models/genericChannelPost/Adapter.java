package com.apiDataProcessor.models.genericChannelPost;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.joke.Joke;
import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.User;
import com.apiDataProcessor.models.apiResponse.twitter.Tweet;
import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Adapter {

    public static List<GenericChannelPost> toGenericChannelPost(JokeApiResponse apiResponse) {
        List<GenericChannelPost> channelPostList = new ArrayList<>();
        if (apiResponse == null || apiResponse.isError() || apiResponse.getAmount() == 0) {
            return channelPostList;
        }
        for (Joke joke : apiResponse.getJokes()) {
            GenericChannelPost channelPost = new GenericChannelPost();
            channelPost.setApiType(ApiType.JOKE);

            channelPost.setApiId(UUID.fromString(joke.getJoke()).toString());
            channelPost.setId( UUID.fromString(apiResponse.getId() + "-" + channelPost.getApiId()).toString() );

            channelPost.setJokeStatements( Arrays.stream(joke.getJoke().split("\n")).toList() );

            channelPostList.add(channelPost);
        }
        return channelPostList;
    }

    public static List<GenericChannelPost> toGenericChannelPost(RandomUserApiResponse apiResponse) {
        List<GenericChannelPost> channelPostList = new ArrayList<>();
        if (apiResponse == null || apiResponse.getId() == null || apiResponse.getResults() == null) {
            return channelPostList;
        }
        for (User randomUser : apiResponse.getResults()) {
            GenericChannelPost channelPost = new GenericChannelPost();
            channelPost.setApiType(ApiType.RANDOM_USER);

            UUID uuid = UUID.fromString(apiResponse.getId() + "-" + randomUser.hashCode());
            channelPost.setId( uuid.toString() );

            channelPost.setApiId(apiResponse.getId());
            channelPost.setUser(randomUser);

            channelPostList.add(channelPost);
        }

        return channelPostList;
    }

    public static List<GenericChannelPost> toGenericChannelPost(TwitterApiResponse apiResponse) {
        List<GenericChannelPost> channelPostList = new ArrayList<>();
        if (apiResponse == null || apiResponse.getData() == null || apiResponse.getData().isEmpty()) {
            return channelPostList;
        }
        for (Tweet tweet : apiResponse.getData()) {
            GenericChannelPost channelPost = new GenericChannelPost();
            channelPost.setApiType(ApiType.TWITTER);

            channelPost.setId( UUID.fromString(apiResponse.getId() + "-" + tweet.getId()).toString() );
            channelPost.setApiId(tweet.getId());

            channelPostList.add(channelPost);
        }

        return channelPostList;
    }
}

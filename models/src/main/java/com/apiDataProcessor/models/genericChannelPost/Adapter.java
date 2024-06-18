package com.apiDataProcessor.models.genericChannelPost;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.joke.Joke;
import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.User;
import com.apiDataProcessor.models.apiResponse.twitter.Tweet;
import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.List;

import static com.apiDataProcessor.utils.utils.hashString;

public class Adapter {

    public static List<GenericChannelPost> toGenericChannelPost(JokeApiResponse apiResponse) {
        List<GenericChannelPost> channelPostList = Lists.newArrayList();

        if (apiResponse == null || apiResponse.isError() || apiResponse.getAmount() == 0) {
            return channelPostList;
        }
        for (Joke joke : apiResponse.getJokes()) {
            GenericChannelPost channelPost = new GenericChannelPost();
            channelPost.setApiType(ApiType.JOKE);

            channelPost.setApiId(hashString(joke.getJoke()));
            channelPost.setId( hashString(apiResponse.getId() + channelPost.getApiId()) );

            channelPost.setJokeStatements( Arrays.stream(joke.getJoke().split("\n")).toList() );

            channelPostList.add(channelPost);
        }
        return channelPostList;
    }

    public static List<GenericChannelPost> toGenericChannelPost(RandomUserApiResponse apiResponse) {
        List<GenericChannelPost> channelPostList = Lists.newArrayList();
        if (apiResponse == null || apiResponse.getId() == null || apiResponse.getResults() == null) {
            return channelPostList;
        }
        for (User randomUser : apiResponse.getResults()) {
            GenericChannelPost channelPost = new GenericChannelPost();
            channelPost.setApiType(ApiType.RANDOM_USER);

            channelPost.setApiId(hashString(randomUser.toString()));
            channelPost.setId( hashString(apiResponse.getId() + channelPost.getApiId()) );

            channelPost.setUser(randomUser);

            channelPostList.add(channelPost);
        }

        return channelPostList;
    }

    public static List<GenericChannelPost> toGenericChannelPost(TwitterApiResponse apiResponse) {
        List<GenericChannelPost> channelPostList = Lists.newArrayList();
        if (apiResponse == null || apiResponse.getData() == null || apiResponse.getData().isEmpty()) {
            return channelPostList;
        }
        for (Tweet tweet : apiResponse.getData()) {
            GenericChannelPost channelPost = new GenericChannelPost();
            channelPost.setApiType(ApiType.TWITTER);

            channelPost.setApiId(tweet.getId());
            channelPost.setId( hashString(tweet.getId()) );

            channelPostList.add(channelPost);
        }

        return channelPostList;
    }
}

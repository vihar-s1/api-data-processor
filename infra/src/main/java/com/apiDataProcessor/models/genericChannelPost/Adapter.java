package com.apiDataProcessor.models.genericChannelPost;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.joke.Joke;
import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.User;
import com.apiDataProcessor.models.apiResponse.twitter.Tweet;
import com.apiDataProcessor.models.apiResponse.twitter.TwitterApiResponse;
import com.apiDataProcessor.models.genericChannelPost.enums.Language;
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
            channelPost.setId( hashString(channelPost.getApiId()) );

            channelPost.setJokeStatements( Arrays.stream(joke.getJoke().split("\n")).toList() );
            channelPost.addToAdditional("category", joke.getCategory());
            channelPost.addToAdditional("type", joke.getType());

            joke.getFlags().forEach((flag, exists) -> {
                if (exists) {
                    channelPost.addToTags(flag);
                }
            });

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
            channelPost.setId( hashString(channelPost.getApiId()) );

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

            channelPost.setConversationId(tweet.getConversationId());
            channelPost.setBody(tweet.getText());
            channelPost.setAuthorId(tweet.getAuthorId());
            channelPost.setCreatedAt(tweet.getCreatedAt());
            channelPost.setLanguage(Language.getLanguage(tweet.getLanguage()));

            if (tweet.getInReplyToUserId() != null) {
                channelPost.addToAdditional("inReplyToUserId", tweet.getInReplyToUserId());
            }
            if (tweet.getPossiblySensitive() != null) {
                channelPost.addToAdditional("possiblySensitive", tweet.getPossiblySensitive());
            }
            if (tweet.getEditHistoryTweetIDs() != null && !tweet.getEditHistoryTweetIDs().isEmpty()) {
                channelPost.addToAdditional("editHistoryTweetIDs", tweet.getEditHistoryTweetIDs());
            }
            // unhandled fields: attachments, editControls, entities, referencedTweets, replySettings, nonPublicMetrics, organicMetrics, promotedMetrics, publicMetrics

            channelPostList.add(channelPost);
        }

        return channelPostList;
    }
}

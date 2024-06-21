package com.apiDataProcessor.models.genericChannelPost;

import com.apiDataProcessor.models.ApiType;
import com.apiDataProcessor.models.apiResponse.joke.Joke;
import com.apiDataProcessor.models.apiResponse.joke.JokeApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.RandomUserApiResponse;
import com.apiDataProcessor.models.apiResponse.randomUser.User;
import com.apiDataProcessor.models.apiResponse.reddit.RedditApiResponse;
import com.apiDataProcessor.models.apiResponse.reddit.RedditApiResponseData;
import com.apiDataProcessor.models.apiResponse.reddit.RedditPost;
import com.apiDataProcessor.models.apiResponse.reddit.RedditPostData;
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
        if (apiResponse == null || apiResponse.getResults() == null) {
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

            channelPost.setParentId(tweet.getConversationId());
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

    public static List<GenericChannelPost> toGenericChannelPost(RedditApiResponse apiResponse) {
        List<GenericChannelPost> channelPostList = Lists.newArrayList();
        if (apiResponse == null || apiResponse.getData() == null) {
            return channelPostList;
        }

        RedditApiResponseData responseData = apiResponse.getData();
        if (responseData.getChildren() == null || responseData.getChildren().isEmpty()) {
            return channelPostList;
        }

        for (RedditPost post : responseData.getChildren()) {
            RedditPostData postData = post.getData();
            GenericChannelPost channelPost = new GenericChannelPost();

            channelPost.setApiType(ApiType.REDDIT);
            channelPost.setApiId(postData.getId());
            channelPost.setId( hashString(postData.getId()) );
            channelPost.setAuthor(postData.getAuthor());
            channelPost.setCreatedAt(new java.sql.Timestamp(postData.getCreatedUTC() * 1000));
            channelPost.setTitle(postData.getTitle());
            channelPost.setBody(postData.getSelftext());
            channelPost.setHtmlBody(postData.getSelftextHtml());

            channelPost.setParentId(postData.getSubredditId());
            channelPost.setParentName(postData.getSubreddit());

            channelPost.setTotalLikes(postData.getUps());
            channelPost.setTotalDislikes(postData.getDowns());

            channelPost.addToAdditional("domain", postData.getDomain());
            channelPost.addToAdditional("subredditType", postData.getSubredditType());
            channelPost.addToAdditional("subredditNamePrefixed", postData.getSubredditNamePrefixed());
            channelPost.addToAdditional("totalAwardsReceived", postData.getTotalAwardsReceived());
            channelPost.addToAdditional("numberOfComments", postData.getNumberOfComments());
            channelPost.addToAdditional("isVideo", postData.getIsVideo());
            channelPost.addToAdditional("permalink", postData.getPermalink());
            channelPost.addToAdditional("url", postData.getUrl());
            channelPost.addToAdditional("media", postData.getMedia());
            channelPost.addToAdditional("mediaOnly", postData.getMediaOnly());

            channelPostList.add(channelPost);
        }

        return channelPostList;
    }
}

package com.VersatileDataProcessor.Models.standardMediaData;

import com.VersatileDataProcessor.Models.ApiType;
import com.VersatileDataProcessor.Models.apiResponse.joke.Joke;
import com.VersatileDataProcessor.Models.apiResponse.joke.JokeApiResponse;
import com.VersatileDataProcessor.Models.apiResponse.randomUser.User;
import com.VersatileDataProcessor.Models.apiResponse.randomUser.RandomUserApiResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Adapter {
    public static List<StandardMediaData> toStandardMediaData(JokeApiResponse apiResponse) {
        List<StandardMediaData> mediaDataList = new ArrayList<>();
        if (apiResponse == null || apiResponse.isError() || apiResponse.getAmount() == 0) {
            return mediaDataList;
        }
        for (Joke joke : apiResponse.getJokes()) {
            StandardMediaData mediaData = new StandardMediaData();
            mediaData.setApiType(ApiType.JOKE);
            mediaData.setId(UUID.randomUUID().toString());
            mediaData.setApiId(apiResponse.getId());
            mediaData.setJokeStatements( Arrays.stream(joke.getJoke().split("\n")).toList() );

            mediaDataList.add(mediaData);
        }
        return mediaDataList;
    }

    public static List<StandardMediaData> toStandardMediaData(RandomUserApiResponse apiResponse) {
        List<StandardMediaData> mediaDataList = new ArrayList<>();
        if (apiResponse == null || apiResponse.getId() == null || apiResponse.getResults() == null) {
            return mediaDataList;
        }
        for (User randomUser : apiResponse.getResults()) {
            StandardMediaData mediaData = new StandardMediaData();
            mediaData.setApiType(ApiType.RANDOM_USER);
            mediaData.setId(UUID.randomUUID().toString());
            mediaData.setApiId(apiResponse.getId());
            mediaData.setUser(randomUser);

            mediaDataList.add(mediaData);
        }

        return mediaDataList;
    }
}

package com.eg.aoe2slackbot.service;

import com.eg.aoe2slackbot.entity.Aoe2UserConfig;
import com.eg.aoe2slackbot.entity.RatingRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingService {

    public RatingRecord getMostRecentRating(List<Aoe2UserConfig> aoe2UserConfigs, String profileNameOrSteamId) throws Exception {
        return this.getRatings(aoe2UserConfigs, profileNameOrSteamId, 1).get(0);
    }

    public List<RatingRecord> getRatings(List<Aoe2UserConfig> aoe2UserConfigs, String profileNameOrSteamId, Integer count) throws Exception {

        List<RatingRecord> ratingRecordList;

        String steamId = "";
        for (Aoe2UserConfig userConfig : aoe2UserConfigs) {
            if (profileNameOrSteamId.trim().equalsIgnoreCase(userConfig.getProfileName())) {
                steamId = userConfig.getSteamId();
            }
        }

        // If we don't find a mapping from a profile name, assume steam id is passed.
        if (StringUtils.isEmpty(steamId)) {
            steamId = profileNameOrSteamId;
        }

        OkHttpClient client = new OkHttpClient();

        HttpUrl url = new HttpUrl
                .Builder()
                .scheme("https")
                .host("aoe2.net")
                .addPathSegments("api/player/ratinghistory")
                .addQueryParameter("game", "aoe2de")
                .addQueryParameter("steam_id", steamId)
                .addQueryParameter("leaderboard_id", "4")
                .addQueryParameter("count", count.toString())
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType javaType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, RatingRecord.class);
        ratingRecordList = objectMapper.readValue(response.body().string(), javaType);

        return ratingRecordList;


    }


}

package com.eg.aoe2slackbot.service;

import com.eg.aoe2slackbot.entity.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

@Service
public class MatchService {

    private static final Logger LOG = LoggerFactory.getLogger(MatchService.class.getName());

    private RatingService ratingService;

    @Autowired
    public MatchService(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    public MatchResult getLastMatch(List<Aoe2UserConfig> aoe2UserConfigs, String profileNameOrSteamId) throws Exception {

        List<RatingRecord> ratingRecords = ratingService.getRatings(aoe2UserConfigs, profileNameOrSteamId, 2);

        String steamId = "";
        String profileName = "unset";
        for (Aoe2UserConfig userConfig : aoe2UserConfigs) {
            if (profileNameOrSteamId.trim().equalsIgnoreCase(userConfig.getProfileName())) {
                steamId = userConfig.getSteamId();
                profileName = userConfig.getProfileName();
            }
        }

        if ("unset".equals(profileName)) {
            for (Aoe2UserConfig userConfig : aoe2UserConfigs) {
                if (steamId.trim().equalsIgnoreCase(userConfig.getSteamId())) {
                    profileName = profileNameOrSteamId;
                }
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
                .addPathSegments("api/player/matches")
                .addQueryParameter("game", "aoe2de")
                .addQueryParameter("steam_id", steamId)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

        ObjectMapper objectMapper = new ObjectMapper();
        CollectionType javaType = objectMapper.getTypeFactory()
                .constructCollectionType(List.class, Match.class);
        List<Match> matches = objectMapper.readValue(response.body().string(), javaType);

        Match lastMatch = matches.get(0);

        MatchResult matchResult = new MatchResult();
        matchResult.setMatch(lastMatch);
        matchResult.setSteamId(steamId);
        matchResult.setName(profileName);
        matchResult.setVictorious(this.isVictorious(ratingRecords));


        for (MatchPlayer player : matchResult.getMatch().getMatchPlayerList()) {
            player.setCivName(RefDataService.aoe2RefData.getCivMap().get(player.getCiv().toString()));
        }

        return matchResult;

    }

    public Boolean isVictorious(List<RatingRecord> ratings)  {

        RatingRecord mostRecentRating = ratings.get(0);
        RatingRecord secondMostRecentRating = ratings.get(1);

        if (mostRecentRating.getNumberOfWins() > secondMostRecentRating.getNumberOfWins()) {
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public void postMatchResultsToSlack() {
        try {

            String slackWebhookUrl = System.getenv("SLACK_WEBHOOK_URL");

            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"),
                    "{\"text\" : " + "\"Slackweb hook call initiated by Heroku scheduler!\"}");

            Request request = new Request.Builder()
                    .url(slackWebhookUrl)
                    .post(requestBody)
                    .build();

            OkHttpClient client = new OkHttpClient();
            Call call = client.newCall(request);
            Response response = call.execute();


        } catch (Exception ex) {
            LOG.error("Error posting to Slack Webhook.", ex);
        }
    }
}

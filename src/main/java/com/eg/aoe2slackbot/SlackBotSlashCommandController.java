package com.eg.aoe2slackbot;


import com.eg.aoe2slackbot.entity.Aoe2UserConfig;
import com.eg.aoe2slackbot.entity.MatchResult;
import com.eg.aoe2slackbot.entity.RatingRecord;
import com.eg.aoe2slackbot.entity.SlackSlashCommandResponse;
import com.eg.aoe2slackbot.service.MatchService;
import com.eg.aoe2slackbot.service.RatingService;
import com.eg.aoe2slackbot.view.SlackMatchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.slack.api.Slack;
import com.slack.api.methods.request.pins.PinsListRequest;
import com.slack.api.methods.response.pins.PinsListResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class SlackBotSlashCommandController {

    private static final Logger LOG = LoggerFactory.getLogger(SlackBotSlashCommandController.class.getName());


    private List<Aoe2UserConfig> aoe2UserConfigs = new ArrayList<>();
    private RatingService ratingService;
    private MatchService matchService;

    @Autowired
    public SlackBotSlashCommandController(RatingService ratingService, MatchService matchService) {
        this.ratingService = ratingService;
        this.matchService = matchService;
    }

    @GetMapping(path="/aoe2/profileMap")
    public String getProfileMappings() {

        try {

            Slack slack = Slack.getInstance();
            String token = System.getenv("SLACK_TOKEN");
            String slackChannelId = System.getenv("PROFILE_MAP_SLACK_CHANNEL");

            PinsListRequest pinsListRequest = PinsListRequest.builder().token(token).channel(slackChannelId).build();

            PinsListResponse pins = slack.methods().pinsList(pinsListRequest);

            for (PinsListResponse.MessageItem  item : pins.getItems()) {
                String userConfigJson = item.getMessage().getText();
                if (userConfigJson.contains("PROFILE_MAP")) {
                    userConfigJson = userConfigJson.replace('\n', ' ').replace('`', ' ');
                    userConfigJson = userConfigJson.replace("PROFILE_MAP=", "");

                    ObjectMapper objectMapper = new ObjectMapper();
                    CollectionType javaType = objectMapper.getTypeFactory()
                            .constructCollectionType(List.class, Aoe2UserConfig.class);
                    aoe2UserConfigs = objectMapper.readValue(userConfigJson, javaType);

                    LOG.info("User configs loaded." + aoe2UserConfigs);
                }
            }

        } catch (Exception ex) {
            LOG.error("Error getting rating.", ex);
        }

        return "";

    }

    @PostMapping(path = "/aoe2")
    public ResponseEntity<String> getRating(@RequestParam(name = "text") String commandText) {

        getProfileMappings();

        try {

            if (commandText.startsWith("last-match")) {
                MatchResult matchResult = matchService.getLastMatch(aoe2UserConfigs, commandText.replace("last-match", "").trim());

                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("Content-Type",
                        "application/json");

                return ResponseEntity.ok()
                        .headers(responseHeaders)
                        .body(SlackMatchResult.getSlackResponseForMatchResult(matchResult));

            } else {
                RatingRecord ratingRecord = ratingService.getMostRecentRating(aoe2UserConfigs, commandText);
                SlackSlashCommandResponse slackSlashCommandResponse = new SlackSlashCommandResponse("Team Random Map Rank: " +
                        ratingRecord.getRating() + " as of " + DateTimeFormatter.RFC_1123_DATE_TIME.format(ratingRecord.getDate()));
                ObjectMapper objectMapper = new ObjectMapper();
                StringWriter stringWriter = new StringWriter();
                objectMapper.writeValue(stringWriter, slackSlashCommandResponse);
                return new ResponseEntity<String>(stringWriter.toString(), HttpStatus.OK);
            }
        } catch (Exception ex) {
            LOG.error("Error getting rating.", ex);
            return new ResponseEntity<String>("An error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

package com.eg.aoe2slackbot.view;

import com.eg.aoe2slackbot.entity.MatchPlayer;
import com.eg.aoe2slackbot.entity.MatchResult;
import com.eg.aoe2slackbot.service.RefDataService;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.model.block.DividerBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.MarkdownTextObject;
import org.springframework.util.StringUtils;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlackMatchResult {


    public static String getSlackResponseForMatchResult(MatchResult matchResult) throws Exception {


        String playerLink = getPlayerLink(matchResult.getSteamId(), matchResult.getName());
        Duration matchDuration = Duration.between(matchResult.getMatch().getStartedDate().toLocalDateTime(), matchResult.getMatch().getFinishedDate().toLocalDateTime());

        List<LayoutBlock> messageList = new ArrayList<>();

        SectionBlock titleBlock = SectionBlock
                .builder()
                .text(MarkdownTextObject
                        .builder()
                        .text(playerLink +
                                "\'s last match on leaderboard *" + RefDataService.aoe2RefData.getLeaderboardMap().get(matchResult.getMatch().getLeaderboard()) +
                                "* was a " + (matchResult.getVictorious() ? "WIN!!!" : "LOSS :-(") +
                                "  \nThere were " + matchResult.getMatch().getMatchPlayerList().size() + " players in the game and the map was *" +
                                        RefDataService.aoe2RefData.getMapTypeMap().get(matchResult.getMatch().getMapType()) +
                                "* of size " + RefDataService.aoe2RefData.getMapSizeMap().get(matchResult.getMatch().getMapSize()) +
                                ".  \nThe match concluded at " + DateTimeFormatter.RFC_1123_DATE_TIME.format(matchResult.getMatch().getFinishedDate()) +
                                " after lasting " + durationAsString(matchDuration))
                        .build())
                .build();

        messageList.add(titleBlock);

        Map<Integer, List<MatchPlayer>> teamPlayerMap = new HashMap<>();
        for (MatchPlayer player : matchResult.getMatch().getMatchPlayerList()) {

            if (teamPlayerMap.get(player.getTeam()) == null) {
                teamPlayerMap.put(player.getTeam(), new ArrayList<>());
            }

            teamPlayerMap.get(player.getTeam()).add(player);

        }

        for (Integer team : teamPlayerMap.keySet()) {

            StringBuffer playerSummary = new StringBuffer();
            for (MatchPlayer player : teamPlayerMap.get(team)) {

                playerSummary.append(getPlayerLink(player.getSteamId(), player.getName()))
                        .append(" (" + player.getCivName() + ") ")
                        .append(" Rating (" + player.getRating() + ")\n");

            }

            messageList.add(SectionBlock
                    .builder()
                    .text(MarkdownTextObject
                            .builder()
                            .text("Team " + team + "\n" +
                                    playerSummary)
                            .build())
                    .build());

            messageList.add(DividerBlock
                    .builder()
                    .build());

        }


        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        StringWriter stringWriter1 = new StringWriter();
        for (LayoutBlock block : messageList) {
            objectMapper.writeValue(stringWriter1, block);
            stringWriter1.write(",");
        }

        String blockData = StringUtils.trimTrailingCharacter(stringWriter1.toString(),',');

        String result = "{  \"response_type\": \"in_channel\", \"blocks\": [ " + blockData + "]}";

        return result;

    }

    public static String getPlayerLink(String steamId, String name) {

        return "<https://aoe2.net/#profile-" + steamId + "|" + name + ">";

    }

    // package private for accessibility in tests
    static String durationAsString(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        return hours + ":" + String.format("%02d", minutes - 60 * hours);
    }


}

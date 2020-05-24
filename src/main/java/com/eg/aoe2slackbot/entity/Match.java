package com.eg.aoe2slackbot.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Match {

    private Integer victory;
    private List<MatchPlayer> matchPlayerList = new ArrayList<>();
    private Long finished;
    private Long started;
    private Long opened;
    private Integer mapType;
    private Integer gameType;
    private Integer leaderboard;
    private Integer mapSize;


    public Integer getVictory() {
        return victory;
    }

    public void setVictory(Integer victory) {
        this.victory = victory;
    }

    public Boolean getIsVictory() {
        return victory == 1 ? Boolean.TRUE : Boolean.FALSE;
    }

    public List<MatchPlayer> getMatchPlayerList() {
        return matchPlayerList;
    }

    @JsonAlias({"players"})
    public void setMatchPlayerList(List<MatchPlayer> matchPlayerList) {
        this.matchPlayerList = matchPlayerList;
    }

    public Long getFinished() {
        return finished;
    }

    public void setFinished(Long finished) {
        this.finished = finished;
    }

    public Long getStarted() {
        return started;
    }

    public void setStarted(Long started) {
        this.started = started;
    }

    public Long getOpened() {
        return opened;
    }

    public void setOpened(Long opened) {
        this.opened = opened;
    }

    public Integer getMapType() {
        return mapType;
    }

    @JsonAlias({"map_type"})
    public void setMapType(Integer mapType) {
        this.mapType = mapType;
    }

    public Integer getGameType() {
        return gameType;
    }

    @JsonAlias({"game_type"})
    public void setGameType(Integer gameType) {
        this.gameType = gameType;
    }

    public Integer getLeaderboard() {
        return leaderboard;
    }

    @JsonAlias({"leaderboard_id"})
    public void setLeaderboard(Integer leaderboard) {
        this.leaderboard = leaderboard;
    }

    public Integer getMapSize() {
        return mapSize;
    }

    @JsonAlias({"map_size"})
    public void setMapSize(Integer mapSize) {
        this.mapSize = mapSize;
    }

    public ZonedDateTime getFinishedDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.finished), ZoneId.of("UTC-5"));
    }

    public ZonedDateTime getStartedDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.started), ZoneId.of("UTC-5"));
    }
}

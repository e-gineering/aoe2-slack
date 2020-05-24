package com.eg.aoe2slackbot.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchResult {

    private String name;
    private Match match;
    private Integer profileId;
    private String steamId;

    @JsonIgnore
    private Boolean isVictorious;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Match getMatch() {
        return match;
    }

    @JsonAlias({"last_match"})
    public void setMatch(Match match) {
        this.match = match;
    }

    public Boolean getVictorious() {
        return isVictorious;
    }

    public void setVictorious(Boolean victorious) {
        isVictorious = victorious;
    }

    public Integer getProfileId() {
        return profileId;
    }

    @JsonAlias({"profile_id"})
    public void setProfileId(Integer profileId) {
        this.profileId = profileId;
    }

    public String getSteamId() {
        return steamId;
    }

    @JsonAlias({"steam_id"})
    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }
}

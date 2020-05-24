package com.eg.aoe2slackbot.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MatchPlayer {

    Integer profileId;
    String steamId;
    Integer slot;
    Integer slotType;
    Integer rating;
    Integer team;
    Integer civ;
    String name;

    String civName;

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

    public Integer getSlot() {
        return slot;
    }

    public void setSlot(Integer slot) {
        this.slot = slot;
    }

    public Integer getSlotType() {
        return slotType;
    }

    @JsonAlias({"slot_type"})
    public void setSlotType(Integer slotType) {
        this.slotType = slotType;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getTeam() {
        return team;
    }

    public void setTeam(Integer team) {
        this.team = team;
    }

    public Integer getCiv() {
        return civ;
    }

    public void setCiv(Integer civ) {
        this.civ = civ;
    }

    public String getCivName() {
        return civName;
    }

    public void setCivName(String civName) {
        this.civName = civName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

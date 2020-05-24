package com.eg.aoe2slackbot.entity;

public class Aoe2UserConfig {

    private String profileName;
    private String steamId;

    public String getProfileName() {
        return profileName;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public String getSteamId() {
        return steamId;
    }

    public void setSteamId(String steamId) {
        this.steamId = steamId;
    }

    @Override
    public String toString() {
        return "Aoe2UserConfig{" +
                "profileName='" + profileName + '\'' +
                ", steamId='" + steamId + '\'' +
                '}';
    }
}

package com.eg.aoe2slackbot.entity;

import com.eg.aoe2slackbot.entity.refdata.*;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Aoe2RefData {

    private List<Age> ageList = new ArrayList<>();
    private List<Civ> civList = new ArrayList<>();
    private List<MapType> mapTypeList = new ArrayList<>();
    private List<GameType> gameTypeList = new ArrayList<>();
    private List<Leaderboard> leaderboardList = new ArrayList<>();
    private List<MapSize> mapSizeList = new ArrayList<>();


    private Map<String, String> ageMap = new HashMap<>();
    private Map<String, String> civMap = new HashMap<>();
    private Map<Integer, String> mapTypeMap = new HashMap<>();
    private Map<Integer, String> gameTypeMap = new HashMap<>();
    private Map<Integer, String> leaderboardMap = new HashMap<>();
    private Map<Integer, String> mapSizeMap = new HashMap<>();



    public List<Age> getAgeList() {
        return ageList;
    }

    @JsonAlias({"age"})
    public void setAgeList(List<Age> ageList) {
        this.ageList = ageList;
    }

    public List<Civ> getCivList() {
        return civList;
    }

    @JsonAlias({"civ"})
    public void setCivList(List<Civ> civList) {
        this.civList = civList;
    }

    public Map<String, String> getAgeMap() {
        return ageMap;
    }

    public void setAgeMap(Map<String, String> ageMap) {
        this.ageMap = ageMap;
    }

    public Map<String, String> getCivMap() {
        return civMap;
    }

    public void setCivMap(Map<String, String> civMap) {
        this.civMap = civMap;
    }

    public List<MapType> getMapTypeList() {
        return mapTypeList;
    }

    @JsonAlias({"map_type"})
    public void setMapTypeList(List<MapType> mapTypeList) {
        this.mapTypeList = mapTypeList;
    }

    public Map<Integer, String> getMapTypeMap() {
        return mapTypeMap;
    }

    public Map<Integer, String> getGameTypeMap() {
        return gameTypeMap;
    }

    public List<GameType> getGameTypeList() {
        return gameTypeList;
    }

    @JsonAlias({"game_type"})
    public void setGameTypeList(List<GameType> gameTypeList) {
        this.gameTypeList = gameTypeList;
    }

    public List<Leaderboard> getLeaderboardList() {
        return leaderboardList;
    }

    @JsonAlias({"leaderboard"})
    public void setLeaderboardList(List<Leaderboard> leaderboardList) {
        this.leaderboardList = leaderboardList;
    }

    public List<MapSize> getMapSizeList() {
        return mapSizeList;
    }

    @JsonAlias({"map_size"})
    public void setMapSizeList(List<MapSize> mapSizeList) {
        this.mapSizeList = mapSizeList;
    }


    public void setMapTypeMap(Map<Integer, String> mapTypeMap) {
        this.mapTypeMap = mapTypeMap;
    }

    public void setGameTypeMap(Map<Integer, String> gameTypeMap) {
        this.gameTypeMap = gameTypeMap;
    }

    public Map<Integer, String> getLeaderboardMap() {
        return leaderboardMap;
    }

    public void setLeaderboardMap(Map<Integer, String> leaderboardMap) {
        this.leaderboardMap = leaderboardMap;
    }

    public Map<Integer, String> getMapSizeMap() {
        return mapSizeMap;
    }

    public void setMapSizeMap(Map<Integer, String> mapSizeMap) {
        this.mapSizeMap = mapSizeMap;
    }

    public void buildMaps() {

        for (Age age : this.ageList) {
            this.ageMap.put(age.getId(), age.getValue());
        }

        for (Civ civ : this.civList) {
            this.civMap.put(civ.getId(), civ.getValue());
        }

        for (MapType mapType : this.mapTypeList) {
            this.mapTypeMap.put(Integer.parseInt(mapType.getId()), mapType.getValue());
        }

        for (GameType gameType : this.gameTypeList) {
            this.gameTypeMap.put(Integer.parseInt(gameType.getId()), gameType.getValue());
        }

        for (Leaderboard leaderboard : this.leaderboardList) {
            this.leaderboardMap.put(Integer.parseInt(leaderboard.getId()), leaderboard.getValue());
        }

        for (MapSize mapSize : this.mapSizeList) {
            this.mapSizeMap.put(Integer.parseInt(mapSize.getId()), mapSize.getValue());
        }


    }
}

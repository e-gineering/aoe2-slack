package com.eg.aoe2slackbot.entity;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public class RatingRecord {

    private Integer rating;

    @JsonAlias({"num_wins"})
    private Integer numberOfWins;

    @JsonAlias({"num_losses"})
    private Integer numberOfLosses;

    private Integer streak;

    private Integer drops;

    private Long timestamp;

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public Integer getNumberOfWins() {
        return numberOfWins;
    }

    public void setNumberOfWins(Integer numberOfWins) {
        this.numberOfWins = numberOfWins;
    }

    public Integer getNumberOfLosses() {
        return numberOfLosses;
    }

    public void setNumberOfLosses(Integer numberOfLosses) {
        this.numberOfLosses = numberOfLosses;
    }

    public Integer getStreak() {
        return streak;
    }

    public void setStreak(Integer streak) {
        this.streak = streak;
    }

    public Integer getDrops() {
        return drops;
    }

    public void setDrops(Integer drops) {
        this.drops = drops;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public ZonedDateTime getDate() {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(this.timestamp), ZoneId.of("UTC-5"));
    }

}

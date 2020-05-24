package com.eg.aoe2slackbot.entity;

public class SlackMessage {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "SlackMessage{" +
                "text='" + text + '\'' +
                '}';
    }
}

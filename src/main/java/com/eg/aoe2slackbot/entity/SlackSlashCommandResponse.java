package com.eg.aoe2slackbot.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SlackSlashCommandResponse {

    private String text;
    private String responseType;

    public SlackSlashCommandResponse(String text, String responseType) {
        this.text = text;
        this.responseType = responseType;
    }

    /**
     * The response type is defaulted to "in_channel" when using
     * this constructor.
     *
     * @param text Text to send.
     */
    public SlackSlashCommandResponse(String text) {
        this.text = text;
        this.responseType = "in_channel";
    }

    public SlackSlashCommandResponse() {
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @JsonProperty("response_type")
    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }
}

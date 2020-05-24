package com.eg.aoe2slackbot.entity.refdata;

import com.fasterxml.jackson.annotation.JsonAlias;

public abstract class Aoe2NetRefData {

    private String id;
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    @JsonAlias({"string"})
    public void setValue(String value) {
        this.value = value;
    }
}

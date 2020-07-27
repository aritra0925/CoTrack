package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Messages {
    @JsonProperty(value = "Messages")
    List<Message> messageList;
    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }
}

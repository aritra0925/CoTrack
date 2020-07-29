package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Message {
    @JsonProperty(value = "message")
    String message;

    @JsonProperty(value = "sender")
    User sender;

    @JsonProperty(value = "createdAt")
    long createdAt;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

}

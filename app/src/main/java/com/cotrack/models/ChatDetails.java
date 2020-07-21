package com.cotrack.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ChatDetails {
    @JsonProperty(value = "_id")
    private String _id;

    @JsonProperty(value = "type")
    private String type;

    @JsonProperty(value = "messageList")
    private List<Message> messageList;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Message> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<Message> messageList) {
        this.messageList = messageList;
    }

    public ChatDetails(String _id, String type, List<Message> messageList) {
        this._id = _id;
        this.type = type;
        this.messageList = messageList;
    }

    @Override
    public String toString() {
        return "ChatDetails{" +
                "_id='" + _id + '\'' +
                ", type='" + type + '\'' +
                ", messageList=" + messageList +
                '}';
    }
}

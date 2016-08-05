package com.marut.chat.model;

import com.marut.chat.persistence.RoomService;

/**
 * Created by marutsingh on 7/19/16.
 */
public class User {
    String uuid;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static String getEventName(String entity,ChatMessage.ChatEvents chatEvents){
        return String.format("%s.%s", entity, chatEvents.name());
    }
}

package com.marut.chat.model;

import com.marut.chat.persistence.RoomService;

/**
 * Created by marutsingh on 7/19/16.
 */
public class User {
    String uuid;

    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public static String getEventName(String entity,ChatMessage.ChatEvents chatEvents){
        return String.format("%s.%s", entity, chatEvents.name());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (!uuid.equals(user.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = uuid.hashCode();
        result = 31 * result + name.hashCode();
        return result;
    }
}

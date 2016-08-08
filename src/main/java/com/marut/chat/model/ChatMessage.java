package com.marut.chat.model;

/**
 * Created by marutsingh on 7/19/16.
 */
public class ChatMessage {
    String message;
    String fromUser;
    String toUser;
    String time = java.time.Instant.now().toString();

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    /**
     * Created by marutsingh on 7/19/16.
     */
    public static enum  ChatEvents {
        SEND_DIRECT_MESSAGE,DIRECT_MESSAGE_RECEIVED,ROOM_MESSAGE_RECEIVED,SUBSCRIBE_TO_ROOM,UNSUBSCRIBE_FROM_ROOM,DELETE_ROOM;
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }
}

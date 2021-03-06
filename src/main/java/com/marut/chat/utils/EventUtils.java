package com.marut.chat.utils;

import com.marut.chat.model.ChatMessage;

/**
 * Created by marutsingh on 8/5/16.
 */
public class EventUtils {

    public static String userRoomSubscriptionEvent(String userId){
        String roomSubscriptionEvent =
                String.format("%s.%s.%s.%s","room","subscribe",userId, ChatMessage.ChatEvents.SUBSCRIBE_TO_ROOM.name());
        return roomSubscriptionEvent;
    }

    public static String userRoomUnsubscriptionEvent(String userId){
        String roomSubscriptionEvent =
                String.format("%s.%s.%s.%s", "room", "unsubscribe", userId, ChatMessage.ChatEvents.UNSUBSCRIBE_FROM_ROOM.name());
        return roomSubscriptionEvent;
    }

    public static String roomDeleteEvent(String room){
        String roomSubscriptionEvent =
                String.format("%s.%s.%s", "room", room, ChatMessage.ChatEvents.DELETE_ROOM.name());
        return roomSubscriptionEvent;
    }

    public static String roomChatEvent(String room){
        String roomSubscriptionEvent =
                String.format("%s.%s.%s.%s", "room", "chat",room, ChatMessage.ChatEvents.ROOM_MESSAGE_RECEIVED.name());
        return roomSubscriptionEvent;
    }

    public static String userRoomChatEvent(String room){
        String roomSubscriptionEvent =
                String.format("%s.%s.%s.%s.%s", "room","user", "chat",room, ChatMessage.ChatEvents.ROOM_MESSAGE_RECEIVED.name());
        return roomSubscriptionEvent;
    }

    public static String directMessageEvent(String toUser){
        String directMessageEvent =
                String.format("%s.%s.%s.%s","user","chat",toUser, ChatMessage.ChatEvents.DIRECT_MESSAGE_RECEIVED.name());
        return directMessageEvent;
    }

    public static String directMessageTriggerEvent(String toUser){
        String directMessageEvent =
                String.format("%s.%s.%s.%s","user","chat",toUser, ChatMessage.ChatEvents.SEND_DIRECT_MESSAGE.name());
        return directMessageEvent;
    }
}

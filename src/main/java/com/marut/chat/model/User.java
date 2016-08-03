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

    public static String getEventName(User user,ChatMessage.ChatEvents chatEvents){
          return getEventName(user.getUuid(), chatEvents);
    }

    public static String getEventName(String entity,ChatMessage.ChatEvents chatEvents){
        return String.format("%s%s",entity,chatEvents.name());
    }

    /**
     *
     * @param ownerId ID of Owner
     * @param roomId Id of the room
     * @param userId User to be registered
     */
    public void subscribeToRoom(String ownerId,String roomId,String userId){
        Room room = new Room();
        //STEP1 -- Check if room belongs to owner
        RoomService roomService = new RoomService();
        roomService.registerUserToRoom(room,userId);
    }
}
